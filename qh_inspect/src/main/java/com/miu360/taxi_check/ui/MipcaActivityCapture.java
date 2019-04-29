package com.miu360.taxi_check.ui;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.Callable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.lubao.lubao.async.AsyncUtil;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.data.HtmlData;
import com.miu360.taxi_check.model.Driver;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.zxing.camera.CameraManager;
import com.miu360.taxi_check.zxing.decoding.CaptureActivityHandler;
import com.miu360.taxi_check.zxing.decoding.InactivityTimer;
import com.miu360.taxi_check.zxing.view.ViewfinderView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.Toast;

/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class MipcaActivityCapture extends BaseActivity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private boolean vibrate;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		// ViewUtil.addTopView(getApplicationContext(), this,
		// R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * 处理扫描结果
	 *
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		if (resultString.equals("")) {
			Toast.makeText(MipcaActivityCapture.this, "Scan failed!", Toast.LENGTH_SHORT).show();
		} else {
			loadInfo(resultString);
		}
	}

	private void loadInfo(final String resultString) {
		Log.e("2", resultString);
		if (!resultString.contains("219.232.196.42")) {
			UIUtils.toast(self, "二维码不正确", Toast.LENGTH_LONG);
			finish();
		}
		// 外网地址替换成内网
		final Dialog d = Windows.waiting(self);
		AsyncUtil.goAsync(new Callable<com.lubao.lubao.async.Result<Driver>>() {

			@Override
			public com.lubao.lubao.async.Result<Driver> call() throws Exception {

				return HtmlData.getDriverInfo(resultString.replace("219.232.196.42", "10.252.2.66"));
			}
		}, new com.lubao.lubao.async.Callback<com.lubao.lubao.async.Result<Driver>>() {

			@Override
			public void onHandle(com.lubao.lubao.async.Result<Driver> result) {
				d.dismiss();

				if (result.ok()) {
					Intent resultIntent = new Intent();
					resultIntent.putExtra("result", result.getData());
					resultIntent.putExtra("isOver", true);
					Log.e("Driver值", result.getData().toString());
					setResult(RESULT_OK, resultIntent);
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
				}
				finish();
			}
		});
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);
			//
			// AssetFileDescriptor file = getResources().openRawResourceFd(
			// R.raw.beep);
			// try {
			// mediaPlayer.setDataSource(file.getFileDescriptor(),
			// file.getStartOffset(), file.getLength());
			// file.close();
			// mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
			// mediaPlayer.prepare();
			// } catch (IOException e) {
			// mediaPlayer = null;
			// }
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}