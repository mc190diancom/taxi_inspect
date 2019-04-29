package com.miu360.taxi_check.ui;

import java.io.File;

import com.miu360.clipheadphoto.clip.ClipImageLayout;
import com.miu360.inspect.R;
import com.miu360.taxi_check.util.ImageTools;
import com.miu360.taxi_check.util.ImageUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ClipActivity extends Activity {
	private ClipImageLayout mClipImageLayout;
	private String path;
	private ProgressDialog loadingDialog;
	TextView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clipimage);
		back = (TextView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// 这步必须要加
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		loadingDialog = new ProgressDialog(this);
		loadingDialog.setTitle("请稍后...");
		path = getIntent().getStringExtra("path");
		if (TextUtils.isEmpty(path) || !(new File(path).exists())) {
			Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show();
			return;
		}

		Bitmap bitmap = ImageTools.convertToBitmap(path, 600, 600);
		if (bitmap == null) {
			Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show();
			return;
		}
		mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
		mClipImageLayout.setBitmap(bitmap);
		((Button) findViewById(R.id.id_action_clip)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				loadingDialog.show();
				new Thread(new Runnable() {
					@Override
					public void run() {
						Bitmap bitmap = mClipImageLayout.clip();
						String path = Environment.getExternalStorageDirectory() + "/ClipHeadPhoto/cache/"
								+ System.currentTimeMillis() + ".jpg";
						Bitmap newBitMap = ImageUtil.getResizeScaleBitmap(bitmap, 100, 100);
						ImageTools.savePhotoToSDCard(newBitMap, path);
						loadingDialog.dismiss();
						Intent intent = new Intent();
						intent.putExtra("path", path);
						setResult(RESULT_OK, intent);
						finish();
					}
				}).start();
			}
		});
	}
}
