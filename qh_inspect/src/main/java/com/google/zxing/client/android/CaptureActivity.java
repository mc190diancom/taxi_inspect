/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.camera.CameraManager;
import com.google.zxing.client.android.result.ResultHandler;
import com.lubao.lubao.async.AsyncUtil;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.Config;
import com.miu360.taxi_check.common.ELog;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.Driver;
import com.miu360.taxi_check.model.NetPersonQrInfo;
import com.miu360.taxi_check.util.UIUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends BaseActivity implements SurfaceHolder.Callback {

	private static final String TAG = CaptureActivity.class.getSimpleName();

	private static final long DEFAULT_INTENT_RESULT_DURATION_MS = 1500L;
	private static final long BULK_MODE_SCAN_DELAY_MS = 1000L;

	private static final String[] ZXING_URLS = { "http://zxing.appspot.com/scan", "zxing://scan/" };

	private static final int HISTORY_REQUEST_CODE = 0x0000bacc;

	private static final Collection<ResultMetadataType> DISPLAYABLE_METADATA_TYPES = EnumSet.of(
			ResultMetadataType.ISSUE_NUMBER, ResultMetadataType.SUGGESTED_PRICE,
			ResultMetadataType.ERROR_CORRECTION_LEVEL, ResultMetadataType.POSSIBLE_COUNTRY);

	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private Result savedResultToShow;
	private ViewfinderView viewfinderView;
	private View resultView;
	private Result lastResult;
	private boolean hasSurface;
	private boolean copyToClipboard;
	private IntentSource source;
	private String sourceUrl;
	private ScanFromWebPageManager scanFromWebPageManager;
	private Collection<BarcodeFormat> decodeFormats;
	private Map<DecodeHintType, ?> decodeHints;
	private String characterSet;

	private InactivityTimer inactivityTimer;

	private AmbientLightManager ambientLightManager;

	ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	CameraManager getCameraManager() {
		return cameraManager;
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 闁告绮敮锟介柡宥呮喘椤ｄ粙寮介敓锟�
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.capture);
		// setContentView(R.layout.main);

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		ambientLightManager = new AmbientLightManager(this);

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		// historyManager must be initialized here to update the history
		// preference

		// CameraManager must be initialized here, not in onCreate(). This is
		// necessary because we don't
		// want to open the camera driver and measure the screen size if we're
		// going to show the help on
		// first launch. That led to bugs where the scanning rectangle was the
		// wrong size and partially
		// off screen.
		cameraManager = new CameraManager(getApplication());

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		viewfinderView.setCameraManager(cameraManager);

		resultView = findViewById(R.id.result_view);

		handler = null;
		lastResult = null;

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

		ambientLightManager.start(cameraManager);

		inactivityTimer.onResume();

		Intent intent = getIntent();

		copyToClipboard = prefs.getBoolean(PreferencesActivity.KEY_COPY_TO_CLIPBOARD, true)
				&& (intent == null || intent.getBooleanExtra(Intents.Scan.SAVE_HISTORY, true));

		source = IntentSource.NONE;
		sourceUrl = null;
		scanFromWebPageManager = null;
		decodeFormats = null;
		characterSet = null;

		if (intent != null) {

			String action = intent.getAction();
			String dataString = intent.getDataString();

			if (Intents.Scan.ACTION.equals(action)) {

				// Scan the formats the intent requested, and return the result
				// to the calling activity.
				source = IntentSource.NATIVE_APP_INTENT;
				decodeFormats = DecodeFormatManager.parseDecodeFormats(intent);
				decodeHints = DecodeHintManager.parseDecodeHints(intent);

				if (intent.hasExtra(Intents.Scan.WIDTH) && intent.hasExtra(Intents.Scan.HEIGHT)) {
					int width = intent.getIntExtra(Intents.Scan.WIDTH, 0);
					int height = intent.getIntExtra(Intents.Scan.HEIGHT, 0);
					if (width > 0 && height > 0) {
						cameraManager.setManualFramingRect(width, height);
					}
				}
				if (intent.hasExtra(Intents.Scan.CAMERA_ID)) {
					int cameraId = intent.getIntExtra(Intents.Scan.CAMERA_ID, -1);
					if (cameraId >= 0) {
						cameraManager.setManualCameraId(cameraId);
					}
				}

				String customPromptMessage = intent.getStringExtra(Intents.Scan.PROMPT_MESSAGE);

			} else if (dataString != null && dataString.contains("http://www.google")
					&& dataString.contains("/m/products/scan")) {

				// Scan only products and send the result to mobile Product
				// Search.
				source = IntentSource.PRODUCT_SEARCH_LINK;
				sourceUrl = dataString;
				decodeFormats = DecodeFormatManager.PRODUCT_FORMATS;

			} else if (isZXingURL(dataString)) {

				// Scan formats requested in query string (all formats if none
				// specified).
				// If a return URL is specified, send the results there.
				// Otherwise, handle it ourselves.
				source = IntentSource.ZXING_LINK;

				sourceUrl = dataString;
				Uri inputUri = Uri.parse(dataString);
				scanFromWebPageManager = new ScanFromWebPageManager(inputUri);
				decodeFormats = DecodeFormatManager.parseDecodeFormats(inputUri);
				// Allow a sub-set of the hints to be specified by the caller.
				decodeHints = DecodeHintManager.parseDecodeHints(inputUri);

			}

			characterSet = intent.getStringExtra(Intents.Scan.CHARACTER_SET);
		}

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			// The activity was paused but not stopped, so the surface still
			// exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(surfaceHolder);
		} else {
			// Install the callback and wait for surfaceCreated() to init the
			// camera.
			surfaceHolder.addCallback(this);
		}
	}

	private int getCurrentOrientation() {
		int rotation = getWindowManager().getDefaultDisplay().getRotation();
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			switch (rotation) {
			case Surface.ROTATION_0:
			case Surface.ROTATION_90:
				return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
			default:
				return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
			}
		} else {
			switch (rotation) {
			case Surface.ROTATION_0:
			case Surface.ROTATION_270:
				return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
			default:
				return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
			}
		}
	}

	private static boolean isZXingURL(String dataString) {
		if (dataString == null) {
			return false;
		}
		for (String url : ZXING_URLS) {
			if (dataString.startsWith(url)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		ambientLightManager.stop();

		cameraManager.closeDriver();
		// historyManager = null; // Keep for onActivityResult
		if (!hasSurface) {
			SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			surfaceHolder.removeCallback(this);
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (source == IntentSource.NATIVE_APP_INTENT) {
				setResult(RESULT_CANCELED);
				finish();
				return true;
			}
			if ((source == IntentSource.NONE || source == IntentSource.ZXING_LINK) && lastResult != null) {
				restartPreviewAfterDelay(0L);
				return true;
			}
			break;
		case KeyEvent.KEYCODE_FOCUS:
		case KeyEvent.KEYCODE_CAMERA:
			// Handle these events so they don't launch the Camera app
			return true;
		// Use volume up/down to turn on light
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			cameraManager.setTorch(false);
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			cameraManager.setTorch(true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
		// Bitmap isn't used yet -- will be used soon
		if (handler == null) {
			savedResultToShow = result;
		} else {
			if (result != null) {
				savedResultToShow = result;
			}
			if (savedResultToShow != null) {
				Message message = Message.obtain(handler, R.id.decode_succeeded, savedResultToShow);
				handler.sendMessage(message);
			}
			savedResultToShow = null;
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	/**
	 * A valid barcode has been found, so give an indication of success and show
	 * the results.
	 *
	 * @param rawResult
	 *            The contents of the barcode.
	 * @param scaleFactor
	 *            amount by which thumbnail was scaled
	 * @param barcode
	 *            A greyscale bitmap of the camera data which was decoded.
	 */
	String DriverImage="";
	public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
		inactivityTimer.onActivity();
		lastResult = rawResult;
		// ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(
		// this, rawResult);
		//
		// boolean fromLiveScan = barcode != null;
		// if (fromLiveScan) {
		//
		// // Then not from history, so beep/vibrate and we have an image to
		// // draw on
		//
		// drawResultPoints(barcode, scaleFactor, rawResult);
		// }
		String resultString = rawResult.getText();
		if (resultString.equals("")) {
			Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
		} else {
			loadInfo(resultString);
		}
	}

	int type = 1;//1是准驾证号，2是网约车人证，3是网约车车证
	private void loadInfo(final String resultString) {
		Log.e("zar", "zar" + resultString);
		if(resultString.contains("219.232.196.42/wr")){
			type =3;
		}else if(resultString.contains("219.232.196.21")){
			type =2;
		}else if(resultString.contains("219.232.196.42/read")){
			type =1;
		}else if(resultString.contains("114.251.208.37")){
			type =4;
		}else{
			type =0;
		}
		getCarInfo(resultString);
		/*if (!resultString.contains("219.232.196.42") && !resultString.contains("219.232.196.21")) {
			UIUtils.toast(self, "二维码错误，扫描失败！", Toast.LENGTH_LONG);
			finish();
		}
		
		if(resultString.contains("219.232.196.42/read")){
			getCarInfo(resultString);
		}else if(resultString.contains("219.232.196.42/wr")){
			getCarInfo(resultString);
		}else if(resultString.contains("219.232.196.21")){
			String url = "http://219.232.196.21/bktcglphone/wyAppqueryUserInfo.action?pcode="+resultString.split("=")[1];
			Log.e("resultQr", url);
			getCarInfo(resultString);
		}else{
			getCarInfo(resultString);
		}*/
		

	}



	private void getDriverInfo(final String resultString){
		DriverImage=resultString.split("=")[1];
		final Dialog d = Windows.waiting(self);
		AsyncUtil.goAsync(new Callable<com.lubao.lubao.async.Result<Driver>>() {

			@Override
			public com.lubao.lubao.async.Result<Driver> call() throws Exception {
				// TODO Auto-generated method stub
				return WeiZhanData.getDriverInfo(resultString);
			}
		}, new com.lubao.lubao.async.Callback<com.lubao.lubao.async.Result<Driver>>() {

			@Override
			public void onHandle(com.lubao.lubao.async.Result<Driver> result) {
				d.dismiss();

				if (result.ok()) {
					Intent resultIntent = new Intent();
					Driver driver=result.getData();
					driver.setHead(Config.SERVER_TAXIINFO+"/queryPhoto/"+DriverImage);					
					ELog.d(TAG, "reqzar:" + Config.SERVER_TAXIINFO+"/queryPhoto/"+DriverImage);
					resultIntent.putExtra("result", driver);
					resultIntent.putExtra("type", "person_info");
					resultIntent.putExtra("isOver", true);
					UIUtils.toast(self, "扫描成功", Toast.LENGTH_LONG);
					setResult(RESULT_OK, resultIntent);
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
				}
				finish();
			}
		});
	}

	
	private void getCarInfo(final String resultString) {
		final Dialog d = Windows.waiting(self);
		AsyncUtil.goAsync(new Callable<com.lubao.lubao.async.Result<String>>() {

			@Override
			public com.lubao.lubao.async.Result<String> call() throws Exception {
				return WeiZhanData.getCarInfo(resultString);
			}
		}, new com.lubao.lubao.async.Callback<com.lubao.lubao.async.Result<String>>() {

			@Override
			public void onHandle(com.lubao.lubao.async.Result<String> result) {
				d.dismiss();
				if (result.ok()) {
					Log.e("resultQr", result.getData().toString());
					Intent resultIntent = new Intent();
					String car_info=result.getData();
					resultIntent.putExtra("result", car_info);
					resultIntent.putExtra("type", type);
					resultIntent.putExtra("isOver", true);
					setResult(RESULT_OK, resultIntent);
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
				}
				finish();
			}
		});
	}
	

	private void getNetDriverInfo(final String resultString) {
		final Dialog d = Windows.waiting(self);
		AsyncUtil.goAsync(new Callable<com.lubao.lubao.async.Result<NetPersonQrInfo>>() {

			@Override
			public com.lubao.lubao.async.Result<NetPersonQrInfo> call() throws Exception {
				return WeiZhanData.getNetDriverInfo(resultString);
			}
		}, new com.lubao.lubao.async.Callback<com.lubao.lubao.async.Result<NetPersonQrInfo>>() {

			@Override
			public void onHandle(com.lubao.lubao.async.Result<NetPersonQrInfo> result) {
				d.dismiss();
				if (result.ok()) {
					if(result.getData() == null || "".equals(result.getData())){
						UIUtils.toast(self, "扫描失败，请重新扫描！", Toast.LENGTH_LONG);
						finish();
					}else{
						Intent resultIntent = new Intent();
						NetPersonQrInfo person_info=result.getData();
						resultIntent.putExtra("result", person_info);
						resultIntent.putExtra("type", "net_person_info");
						resultIntent.putExtra("isOver", true);
						UIUtils.toast(self, "扫描成功", Toast.LENGTH_LONG);
						setResult(RESULT_OK, resultIntent);
						finish();
					}
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
					finish();
				}
				
			}
		});
	}
	/**
	 * Superimpose a line for 1D or dots for 2D to highlight the key features of
	 * the barcode.
	 *
	 * @param barcode
	 *            A bitmap of the captured image.
	 * @param scaleFactor
	 *            amount by which thumbnail was scaled
	 * @param rawResult
	 *            The decoded results which contains the points to draw.
	 */
	private void drawResultPoints(Bitmap barcode, float scaleFactor, Result rawResult) {
		ResultPoint[] points = rawResult.getResultPoints();
		if (points != null && points.length > 0) {
			Canvas canvas = new Canvas(barcode);
			Paint paint = new Paint();
			paint.setColor(getResources().getColor(R.color.result_points));
			if (points.length == 2) {
				paint.setStrokeWidth(4.0f);
				drawLine(canvas, paint, points[0], points[1], scaleFactor);
			} else if (points.length == 4 && (rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A
					|| rawResult.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
				// Hacky special case -- draw two lines, for the barcode and
				// metadata
				drawLine(canvas, paint, points[0], points[1], scaleFactor);
				drawLine(canvas, paint, points[2], points[3], scaleFactor);
			} else {
				paint.setStrokeWidth(10.0f);
				for (ResultPoint point : points) {
					if (point != null) {
						canvas.drawPoint(scaleFactor * point.getX(), scaleFactor * point.getY(), paint);
					}
				}
			}
		}
	}

	private static void drawLine(Canvas canvas, Paint paint, ResultPoint a, ResultPoint b, float scaleFactor) {
		if (a != null && b != null) {
			canvas.drawLine(scaleFactor * a.getX(), scaleFactor * a.getY(), scaleFactor * b.getX(),
					scaleFactor * b.getY(), paint);
		}
	}

	// Put up our own UI for how to handle the decoded contents.
	private void handleDecodeInternally(Result rawResult, ResultHandler resultHandler, Bitmap barcode) {

		CharSequence displayContents = "";
		try {
			displayContents = new String(resultHandler.getDisplayContents().toString().getBytes("ISO-8859-1"),
					"GB2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

		if (resultHandler.getDefaultButtonID() != null
				&& prefs.getBoolean(PreferencesActivity.KEY_AUTO_OPEN_WEB, false)) {
			resultHandler.handleButtonPress(resultHandler.getDefaultButtonID());
			return;
		}

		viewfinderView.setVisibility(View.GONE);
		resultView.setVisibility(View.VISIBLE);

		TextView contentsTextView = (TextView) findViewById(R.id.contents_text_view);
		contentsTextView.setText(displayContents);
		int scaledSize = Math.max(22, 32 - displayContents.length() / 4);
		contentsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaledSize);

	}

	// Briefly show the contents of the barcode, then handle the result outside
	// Barcode Scanner.
	private void handleDecodeExternally(Result rawResult, ResultHandler resultHandler, Bitmap barcode) {

		if (barcode != null) {
			viewfinderView.drawResultBitmap(barcode);
		}

		long resultDurationMS;
		if (getIntent() == null) {
			resultDurationMS = DEFAULT_INTENT_RESULT_DURATION_MS;
		} else {
			resultDurationMS = getIntent().getLongExtra(Intents.Scan.RESULT_DISPLAY_DURATION_MS,
					DEFAULT_INTENT_RESULT_DURATION_MS);
		}

	}

	private void sendReplyMessage(int id, Object arg, long delayMS) {
		if (handler != null) {
			Message message = Message.obtain(handler, id, arg);
			if (delayMS > 0L) {
				handler.sendMessageDelayed(message, delayMS);
			} else {
				handler.sendMessage(message);
			}
		}
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a
			// RuntimeException.
			if (handler == null) {
				handler = new CaptureActivityHandler(this, decodeFormats, decodeHints, characterSet, cameraManager);
			}
			decodeOrStoreSavedBitmap(null, null);
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			Log.w(TAG, "Unexpected error initializing camera", e);
			displayFrameworkBugMessageAndExit();
		}
	}

	private void displayFrameworkBugMessageAndExit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage(getString(R.string.msg_camera_framework_bug));
		builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
		builder.setOnCancelListener(new FinishListener(this));
		builder.show();
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}
}
