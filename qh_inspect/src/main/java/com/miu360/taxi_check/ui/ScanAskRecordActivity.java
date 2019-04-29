package com.miu360.taxi_check.ui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.miu360.inspect.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author yd 开始扫描
 */
public class ScanAskRecordActivity extends Activity implements SurfaceHolder.Callback {

	private static final String TAG = "CamerassActivity";
	public static final String PATH = Environment.getExternalStorageDirectory().toString() + "/wtimage/";
	private String strCaptureFilePath = PATH + "/camera_snap.jpg";
	public static final Uri IMAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	// 预览尺寸 默认设置
	public int WIDTH = 320;// 640;//1024;
	public int HEIGHT = 240;// 480;//768;
	// 拍摄尺寸 默认设置
	public int srcwidth = 2048;// 1600;//2048;final
	public int srcheight = 1536;// 1200;//1536;final
	// 裁切尺寸
	private int cutwidth = 1300;// 1845;//1100;
	private int cutheight = 200;// 1155;//750;
	// 证件类型
	int nMainID = 2;
	private int width, height;
	private ImageButton toplefts1, toprights1, bottomlefts1, bottomrights1, leftcuts1, rightcuts1;
	private ImageView top_left, top_right, bottom_left, bottom_right, left_cut, right_cut;
	private ImageButton backbtn, confirmbtn, resetbtn, takepicbtn, lighton, lightoff, cuton, cutoff;
	private TextView back_reset_text, take_recog_text, light_text, cut_text;
	public String LicenseNo, Phone, DSRName, NLText, NLText1, NLText2;
	private Bitmap bitmap;
	private Camera camera;
	private String path = "";
	public int recogType = -1;// 自动识别、划线识别
	private List<String> focusModes;
	private ImageView imageView;
	private SurfaceView surfaceView;
	private RelativeLayout rlyaout;
	public long fastClick = 0;
	private byte[] imagedata;
	private Boolean cut = true;
	private ToneGenerator tone;
	private SurfaceHolder surfaceHolder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
		setContentView(R.layout.activity_scan_ask_record);

		nMainID = Integer.parseInt("2");

		// 设置拍摄尺寸
		Intent intentget = this.getIntent();
		srcwidth = intentget.getIntExtra("srcwidth", 2048);
		srcheight = intentget.getIntExtra("srcheight", 1536);
		WIDTH = intentget.getIntExtra("WIDTH", 640);
		HEIGHT = intentget.getIntExtra("HEIGHT", 480);
		recogType = intentget.getIntExtra("recogType", 1);
		nMainID = intentget.getIntExtra("nMainID", 2);

		LicenseNo = intentget.getStringExtra("LicenseNo");
		Phone = intentget.getStringExtra("Phone");
		DSRName = intentget.getStringExtra("DSRName");
		NLText = intentget.getStringExtra("NLText");
		NLText1 = intentget.getStringExtra("NLText1");
		NLText2 = intentget.getStringExtra("NLText2");

		findview();
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(ScanAskRecordActivity.this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}

	private void findview() {
		back_reset_text = (TextView) findViewById(R.id.back_and_reset_texts);
		back_reset_text.setTextColor(Color.BLACK);
		take_recog_text = (TextView) findViewById(R.id.take_and_confirm_texts);
		take_recog_text.setTextColor(Color.BLACK);
		light_text = (TextView) findViewById(R.id.light_on_off_texts);
		light_text.setTextColor(Color.BLACK);
		cut_text = (TextView) findViewById(R.id.cut_on_off_texts);
		cut_text.setTextColor(Color.BLACK);

		int button_width = (int) (height * 0.125);
		int button_distance = (int) (height * 0.1);

		RelativeLayout.LayoutParams lParams = new RelativeLayout.LayoutParams(button_width, button_width);
		lParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		lParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		lParams.topMargin = button_distance;
		backbtn = (ImageButton) findViewById(R.id.backbtns);
		backbtn.setLayoutParams(lParams);
		backbtn.setOnClickListener(new mClickListener());
		resetbtn = (ImageButton) findViewById(R.id.reset_btns);
		resetbtn.setLayoutParams(lParams);
		resetbtn.setOnClickListener(new mClickListener());

		lParams = new RelativeLayout.LayoutParams(button_width, button_width);
		lParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		lParams.addRule(RelativeLayout.BELOW, R.id.backbtns);
		lParams.topMargin = button_distance;
		takepicbtn = (ImageButton) findViewById(R.id.takepic_btns);
		takepicbtn.setLayoutParams(lParams);
		takepicbtn.setOnClickListener(new mClickListener());
		confirmbtn = (ImageButton) findViewById(R.id.confirm_btns);
		confirmbtn.setLayoutParams(lParams);
		confirmbtn.setOnClickListener(new mClickListener());

		lParams = new RelativeLayout.LayoutParams(button_width, button_width);
		lParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		lParams.addRule(RelativeLayout.BELOW, R.id.confirm_btns);
		lParams.topMargin = button_distance;
		lighton = (ImageButton) findViewById(R.id.lightons);
		lighton.setLayoutParams(lParams);
		lighton.setOnClickListener(new mClickListener());
		lightoff = (ImageButton) findViewById(R.id.lightoffs);
		lightoff.setLayoutParams(lParams);
		lightoff.setOnClickListener(new mClickListener());

		lParams = new RelativeLayout.LayoutParams(button_width, button_width);
		lParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		lParams.addRule(RelativeLayout.BELOW, R.id.lightons);
		lParams.topMargin = button_distance;
		cuton = (ImageButton) findViewById(R.id.cutons);
		cuton.setLayoutParams(lParams);
		cuton.setOnClickListener(new mClickListener());
		cutoff = (ImageButton) findViewById(R.id.cutoffs);
		cutoff.setLayoutParams(lParams);
		cutoff.setOnClickListener(new mClickListener());

		top_left = (ImageView) findViewById(R.id.toplefts1);
		top_right = (ImageView) findViewById(R.id.toprights1);
		bottom_left = (ImageView) findViewById(R.id.bottomlefts1);
		bottom_right = (ImageView) findViewById(R.id.bottomrights1);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (height * 0.18),
				(int) (height * 0.18));
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		top_left.setLayoutParams(layoutParams);

		layoutParams = new RelativeLayout.LayoutParams((int) (height * 0.18), (int) (height * 0.18));
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.idcard_rightlyaouts);
		top_right.setLayoutParams(layoutParams);

		layoutParams = new RelativeLayout.LayoutParams((int) (height * 0.18), (int) (height * 0.18));
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		bottom_left.setLayoutParams(layoutParams);

		layoutParams = new RelativeLayout.LayoutParams((int) (height * 0.18), (int) (height * 0.18));
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.idcard_rightlyaouts);
		bottom_right.setLayoutParams(layoutParams);

		int margin = 0;
		int cutImageLayoutHeight = 0;
		if (srcwidth == 1280 || srcwidth == 960) {
			margin = (int) ((height * 1.333) * 0.165);
			cutImageLayoutHeight = (int) (height * 0.135);
		}
		if (srcwidth == 1600 || srcwidth == 1200) {
			margin = (int) ((height * 1.333) * 0.19);
			cutImageLayoutHeight = (int) (height * 0.108);
		}
		if (srcwidth == 2048 || srcwidth == 1536) {
			margin = (int) ((height * 1.333) * 0.22);
			cutImageLayoutHeight = (int) (height * 0.13);
		}
		left_cut = (ImageView) findViewById(R.id.leftcuts1);
		right_cut = (ImageView) findViewById(R.id.rightcuts1);
		layoutParams = new RelativeLayout.LayoutParams((int) (cutImageLayoutHeight * 0.6), cutImageLayoutHeight);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		layoutParams.leftMargin = margin;
		left_cut.setLayoutParams(layoutParams);

		layoutParams = new RelativeLayout.LayoutParams((int) (cutImageLayoutHeight * 0.6), cutImageLayoutHeight);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.idcard_rightlyaouts);
		layoutParams.rightMargin = margin;
		right_cut.setLayoutParams(layoutParams);

		imageView = (ImageView) findViewById(R.id.backimageViews);
		surfaceView = (SurfaceView) findViewById(R.id.surfaceViwes);
		rlyaout = (RelativeLayout) findViewById(R.id.idcard_rightlyaouts);
		int layout_width = (int) (width - ((height * 4) / 3));
		RelativeLayout.LayoutParams lP = new RelativeLayout.LayoutParams(layout_width, height);
		lP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		lP.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		rlyaout.setLayoutParams(lP);
		if (nMainID == 1100 || nMainID == 1101) {
			left_cut.setBackgroundResource(R.drawable.leftcuts);
			right_cut.setBackgroundResource(R.drawable.rightcuts);
			showTwoCutImageView();
		} else {
			top_left.setBackgroundResource(R.drawable.top_lefts);
			bottom_left.setBackgroundResource(R.drawable.bottom_lefts);
			top_right.setBackgroundResource(R.drawable.top_rights);
			bottom_right.setBackgroundResource(R.drawable.bottom_rights);
			showFourImageView();
		}
	}

	private void showFourImageView() {
		left_cut.setVisibility(View.INVISIBLE);
		right_cut.setVisibility(View.INVISIBLE);
		top_left.setVisibility(View.VISIBLE);
		top_right.setVisibility(View.VISIBLE);
		bottom_left.setVisibility(View.VISIBLE);
		bottom_right.setVisibility(View.VISIBLE);

	}

	private void hideFourImageView() {
		top_left.setVisibility(View.INVISIBLE);
		top_right.setVisibility(View.INVISIBLE);
		bottom_left.setVisibility(View.INVISIBLE);
		bottom_right.setVisibility(View.INVISIBLE);
	}

	private void showTwoCutImageView() {
		left_cut.setVisibility(View.VISIBLE);
		right_cut.setVisibility(View.VISIBLE);
		top_left.setVisibility(View.INVISIBLE);
		top_right.setVisibility(View.INVISIBLE);
		bottom_left.setVisibility(View.INVISIBLE);
		bottom_right.setVisibility(View.INVISIBLE);
	}

	private void hideTwoCutImageView() {
		left_cut.setVisibility(View.INVISIBLE);
		right_cut.setVisibility(View.INVISIBLE);
	}

	public boolean isEffectClick() {
		long lastClick = System.currentTimeMillis();
		long diffTime = lastClick - fastClick;
		if (diffTime > 5000) {
			fastClick = lastClick;
			return true;
		}
		return false;
	}

	private class mClickListener implements OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.backbtns:
					Intent intent1 = getIntent();
					Intent intent = null;
					if (intent1.getStringExtra("qf").equals("2")) {
						// intent= new Intent(ScanAskRecordActivity.this,
						// SiteRecordsActivity.class);
					} else if (intent1.getStringExtra("qf").equals("1")) {
						// intent= new Intent(ScanAskRecordActivity.this,
						// XWBL01Activity.class);
						intent.putExtra("TypeNo", "0");
					}
					intent.putExtra("selectPath", path);
					// }
					intent.putExtra("path", "3");
					// 设置识别自动裁切
					intent.putExtra("iscut", true);
					intent.putExtra("recogType", recogType);
					intent.putExtra("nMainID", "100");

					/*************************/

					intent.putExtra("AutoID", intent1.getStringExtra("AutoID"));
					intent.putExtra("CaseId", intent1.getStringExtra("CaseId"));
					intent.putExtra("AutoNo", intent1.getStringExtra("AutoNo"));
					intent.putExtra("CodeID", intent1.getStringExtra("CodeID"));
					intent.putExtra("CodeText", intent1.getStringExtra("CodeText"));
					intent.putExtra("kdy", intent1.getStringExtra("kdy"));
					intent.putExtra("DocumentNo", intent1.getStringExtra("DocumentNo"));
					intent.putExtra("isdy", intent1.getStringExtra("isdy"));
					intent.putExtra("smstr", intent1.getStringExtra("smstr"));

					intent.putExtra("DocumentNumber", intent1.getStringExtra("DocumentNumber"));
					intent.putExtra("Type", intent1.getStringExtra("Type"));
					intent.putExtra("DocumentTitle", intent1.getStringExtra("DocumentTitle"));

					intent.putExtra("LicenseNo", LicenseNo);
					intent.putExtra("Phone", Phone);
					intent.putExtra("DSRName", DSRName);
					intent.putExtra("NLText", NLText);
					intent.putExtra("NLText1", NLText1);
					intent.putExtra("NLText2", NLText2);
					// intent.putExtra("CarS", CarS);
					/************************/
					startActivity(intent);
					finish();
					break;
				// 拍照
				case R.id.takepic_btns:
					takepicbtn.setEnabled(false);
					takePicture();
					break;
				// 重拍
				case R.id.reset_btns:
					if (nMainID == 1100 || nMainID == 1101) {
						showTwoCutImageView();
					} else {
						showFourImageView();
					}
					takepicbtn.setVisibility(View.VISIBLE);
					take_recog_text.setText("拍照");
					backbtn.setVisibility(View.VISIBLE);
					back_reset_text.setText("返回");
					imageView.setImageDrawable(null);
					resetbtn.setVisibility(View.INVISIBLE);
					confirmbtn.setVisibility(View.INVISIBLE);
					takepicbtn.setEnabled(true);
					if (null != bitmap) {
						bitmap.recycle();
						bitmap = null;
					}
					camera.startPreview();
					break;
				// 确定
				case R.id.confirm_btns:
					if (isEffectClick()) {
						confirmbtn.setEnabled(false);
						if (nMainID == 1100 || nMainID == 1101) {
							hideTwoCutImageView();
						} else {
							hideFourImageView();
						}
						takepicbtn.setVisibility(View.VISIBLE);
						backbtn.setVisibility(View.VISIBLE);
						resetbtn.setVisibility(View.INVISIBLE);
						confirmbtn.setVisibility(View.INVISIBLE);
						imageView.setImageDrawable(null);
						savephoto();
					} else {
						Log.i(TAG, "confirmbtn click invalid");
					}
					break;
				case R.id.lightons:
					lightoff.setVisibility(View.VISIBLE);
					lighton.setVisibility(View.INVISIBLE);
					light_text.setText("关闪光灯");
					// 开启闪光灯
					Camera.Parameters parameters = camera.getParameters();
					parameters.set("flash-mode", "on");
					camera.setParameters(parameters);
					break;
				case R.id.lightoffs:
					lighton.setVisibility(View.VISIBLE);
					lightoff.setVisibility(View.INVISIBLE);
					light_text.setText("开闪光灯");
					// 关闭闪光灯
					Camera.Parameters parameters2 = camera.getParameters();
					parameters2.set("flash-mode", "off");
					camera.setParameters(parameters2);
					break;
				case R.id.cutons:
					cuton.setVisibility(View.INVISIBLE);
					cutoff.setVisibility(View.VISIBLE);
					cut_text.setText("关闭裁切");
					cut = true;
					break;

				case R.id.cutoffs:
					cuton.setVisibility(View.VISIBLE);
					cutoff.setVisibility(View.INVISIBLE);
					cut_text.setText("打开裁切");
					cut = false;
					break;
			}

		}

	}

	/* 拍照后回显 */
	private PictureCallback PictureCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.i(TAG, "onPictureTaken");
			BitmapFactory.Options opts = new BitmapFactory.Options();
			// 设置成了true,不占用内存，只获取bitmap宽高
			opts.inJustDecodeBounds = true;
			// 根据内存大小设置采样率
			// 需要测试！
			int SampleSize = computeSampleSize(opts, -1, 2048 * 1536);
			opts.inSampleSize = SampleSize;
			opts.inJustDecodeBounds = false;
			opts.inPurgeable = true;
			opts.inInputShareable = true;
			// opts.inNativeAlloc = true;
			// //属性设置为true，可以不把使用的内存算到VM里。SDK默认不可设置这个变量，只能用反射设置。
			try {
				Field field = BitmapFactory.Options.class.getDeclaredField("inNativeAlloc");
				field.set(opts, true);
			} catch (Exception e) {
				Log.i(TAG, "Exception inNativeAlloc");
			}
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
			if (srcwidth == 2048 && srcheight == 1536) {
				Matrix matrix = new Matrix();
				matrix.postScale(0.625f, 0.625f);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			}
			if (srcwidth == 1600 && srcheight == 1200) {
				Matrix matrix = new Matrix();
				matrix.postScale(0.8f, 0.8f);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			}
			imagedata = data;
			/* 创建文件 */
			File dir = new File(PATH);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File myCaptureFile = new File(strCaptureFilePath);
			try {
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
				/* 采用压缩转档方法 */
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
				/* 调用flush()方法，更新BufferStream */
				bos.flush();
				/* 结束OutputStream */
				bos.close();
				// 隐藏焦点图片和行驶证外框

				if (nMainID == 1100 || nMainID == 1101) {
					hideTwoCutImageView();
				} else {
					hideFourImageView();
				}

				/* 将拍照下来且保存完毕的图文件，显示出来 */
				imageView.setImageBitmap(bitmap);
				takepicbtn.setVisibility(View.INVISIBLE);
				backbtn.setVisibility(View.INVISIBLE);
				resetbtn.setVisibility(View.VISIBLE);
				back_reset_text.setText("重拍");
				confirmbtn.setVisibility(View.VISIBLE);
				take_recog_text.setText("确认");
				confirmbtn.setEnabled(true);
				resetCamera();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	};

	/* 相机重置 */
	private void resetCamera() {
		if (camera != null) {
			camera.stopPreview();

		}
	}

	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128
				: (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/* 拍照对焦 */
	/* 拍照 */
	public void takePicture() {
		if (camera != null) {
			try {
				if (focusModes != null && focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
					camera.autoFocus(autoFocusCallback);
				} else {
					camera.takePicture(shutterCallback, null, PictureCallback);
					Toast.makeText(getBaseContext(), "不支持自动对焦", Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
				camera.stopPreview();
				camera.startPreview();
				takepicbtn.setEnabled(true);
				Toast.makeText(this, "自动对焦失败", Toast.LENGTH_SHORT).show();
				Log.i(TAG, "exception:" + e.getMessage());
			}
		}
	}

	AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			if (success) {
				camera.takePicture(shutterCallback, null, PictureCallback);
			} else {
				camera.takePicture(shutterCallback, null, PictureCallback);
			}
		}
	};

	// 快门按下的时候onShutter()被回调拍照声音
	private ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			if (tone == null)
				// 发出提示用户的声音
				tone = new ToneGenerator(1, // AudioManager.AUDIOFOCUS_REQUEST_GRANTED
						ToneGenerator.MIN_VOLUME);
			tone.startTone(ToneGenerator.TONE_PROP_BEEP);
		}
	};

	/* 保存图片并送识别 */
	private void savephoto() {

		// 系统时间
		long datetime = System.currentTimeMillis();
		// 图像名称
		Date date = new Date();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddhhmmss");
		String sysDatetime = fmt.format(date.getTime());
		// String name = "idcard" + date.getTime() + ".jpg";
		String name = "idcard" + sysDatetime + ".jpg";
		// 存储图像（PATH目录）
		Uri uri = insertImage(getContentResolver(), name, datetime, PATH, name, bitmap, imagedata);// bm
		// bitmap

		// 裁切
		if ((nMainID == 1100 || nMainID == 1101) && recogType == 1) {
			if (srcwidth == 1280 || srcwidth == 960) {
				cutwidth = 750;
				cutheight = 130;
			}
			if (srcwidth == 2048 || srcwidth == 1536) {
				cutwidth = 1300;
				cutheight = 200;
			}
			cutwidth = 750;
			cutheight = 130;
			CutPhotos cutPhoto = new CutPhotos(ScanAskRecordActivity.this, cutwidth, cutheight);
			path = cutPhoto.getCutPhotoPath(bitmap, name.replace(".jpg", "_cut"), PATH);
		} else {
			path = PATH + "/" + name;
		}
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}

		if (recogType == 3 && (1100 == nMainID || 1101 == nMainID)) {
			Intent intent1 = getIntent();
			Intent intent = null;
			if (intent1.getStringExtra("qf").equals("2")) {
				// intent = new Intent(ScanAskRecordActivity.this,
				// SiteRecordsActivity.class);
			} else if (intent1.getStringExtra("qf").equals("1")) {
				// intent = new Intent(ScanAskRecordActivity.this,
				// XWBL01Activity.class);
				intent.putExtra("TypeNo", "0");
			}
			intent.putExtra("selectPath", path);
			// 设置识别自动裁切
			intent.putExtra("iscut", true);
			intent.putExtra("recogType", recogType);
			intent.putExtra("nMainID", nMainID);

			/*************************/
			// Intent intent1 = getIntent();
			intent.putExtra("AutoID", intent1.getStringExtra("AutoID"));
			intent.putExtra("CaseId", intent1.getStringExtra("CaseId"));
			intent.putExtra("AutoNo", intent1.getStringExtra("AutoNo"));
			intent.putExtra("CodeID", intent1.getStringExtra("CodeID"));
			intent.putExtra("CodeText", intent1.getStringExtra("CodeText"));
			intent.putExtra("kdy", intent1.getStringExtra("kdy"));
			intent.putExtra("DocumentNo", intent1.getStringExtra("DocumentNo"));
			intent.putExtra("isdy", intent1.getStringExtra("isdy"));
			intent.putExtra("smstr", intent1.getStringExtra("smstr"));
			intent.putExtra("LicenseNo", LicenseNo);
			intent.putExtra("Phone", Phone);
			intent.putExtra("DocumentNumber", intent1.getStringExtra("DocumentNumber"));
			intent.putExtra("Type", intent1.getStringExtra("Type"));
			intent.putExtra("DocumentTitle", intent1.getStringExtra("DocumentTitle"));
			// intent.putExtra("ObjectPerson", ObjectPerson);
			// intent.putExtra("Age", Age);
			// intent.putExtra("Sex",Sex);
			// intent.putExtra("Address", Address);
			// intent.putExtra("IdentityNo", IdentityNo);

			// intent.putExtra("CarS", CarS);
			/************************/

			startActivity(intent);
		} else {
			Intent intent1 = getIntent();
			Intent intent = null;
			if (intent1.getStringExtra("qf").equals("2")) {
				// intent = new Intent(ScanAskRecordActivity.this,
				// SiteRecordsActivity.class);
			} else if (intent1.getStringExtra("qf").equals("1")) {
				// intent = new Intent(ScanAskRecordActivity.this,
				// XWBL01Activity.class);
				intent.putExtra("TypeNo", "0");

			}
			intent.putExtra("path", path);
			intent.putExtra("cut", cut);
			// 设置识别自动裁切
			intent.putExtra("iscut", true);
			intent.putExtra("nMainID", nMainID);

			/*************************/
			// Intent intent1 = getIntent();
			intent.putExtra("AutoID", intent1.getStringExtra("AutoID"));
			intent.putExtra("CaseId", intent1.getStringExtra("CaseId"));
			intent.putExtra("AutoNo", intent1.getStringExtra("AutoNo"));
			intent.putExtra("CodeID", intent1.getStringExtra("CodeID"));
			intent.putExtra("CodeText", intent1.getStringExtra("CodeText"));
			intent.putExtra("kdy", intent1.getStringExtra("kdy"));
			intent.putExtra("DocumentNo", intent1.getStringExtra("DocumentNo"));
			intent.putExtra("isdy", intent1.getStringExtra("isdy"));
			intent.putExtra("smstr", intent1.getStringExtra("smstr"));
			intent.putExtra("LicenseNo", LicenseNo);
			intent.putExtra("Phone", Phone);
			intent.putExtra("DocumentNumber", intent1.getStringExtra("DocumentNumber"));
			intent.putExtra("Type", intent1.getStringExtra("Type"));
			intent.putExtra("DocumentTitle", intent1.getStringExtra("DocumentTitle"));
			/************************/

			startActivity(intent);
		}
	}

	// 存储图像并将信息添加入媒体数据库
	private Uri insertImage(ContentResolver cr, String name, long dateTaken, String directory, String filename,
							Bitmap source, byte[] jpegData) {
		OutputStream outputStream = null;
		String filePath = directory + filename;
		try {
			File dir = new File(directory);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(directory, filename);
			if (file.createNewFile()) {
				outputStream = new FileOutputStream(file);
				if (source != null) {
					source.compress(CompressFormat.JPEG, 100, outputStream);
				} else {
					outputStream.write(jpegData);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (Throwable t) {
				}
			}
		}
		ContentValues values = new ContentValues(7);
		values.put(MediaStore.Images.Media.TITLE, name);
		values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
		values.put(MediaStore.Images.Media.DATE_TAKEN, dateTaken);
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		values.put(MediaStore.Images.Media.DATA, filePath);
		return cr.insert(IMAGE_URI, values);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.i(TAG, "surfaceCreated");
		// 获得Camera对象
		takepicbtn.setEnabled(true);
		if (null == camera) {
			camera = Camera.open();
		}

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		if (camera != null) {
			try {
				Camera.Parameters parameters = camera.getParameters();
				parameters.setPictureFormat(PixelFormat.JPEG);
				parameters.setPreviewSize(WIDTH, HEIGHT);
				parameters.setPictureSize(srcwidth, srcheight);
				camera.setParameters(parameters);
				camera.setPreviewDisplay(holder);
				camera.startPreview();
				focusModes = parameters.getSupportedFocusModes();
			} catch (IOException e) {
				camera.release();
				camera = null;
				e.printStackTrace();
			}
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.i(TAG, "surfaceDestroyed");
		if (camera != null) {
			camera.stopPreview();
			camera.release();
			camera = null;
		}
		finish();
	}

}
