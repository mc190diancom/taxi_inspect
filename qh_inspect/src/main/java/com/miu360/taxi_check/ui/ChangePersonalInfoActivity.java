package com.miu360.taxi_check.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu30.common.util.FileUtil;
import com.miu360.inspect.R;
import com.miu360.taxi_check.App;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.Config;
import com.miu360.taxi_check.data.HttpRequest;
import com.miu30.common.data.UserPreference;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.ZFRYDetailInfo;
import com.miu360.taxi_check.thread.PhotoImageThread;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePersonalInfoActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.image_header)
	private LinearLayout image_header;
	@ViewInject(R.id.phone_number)
	private TextView phone_number;
	@ViewInject(R.id.zhifa_number)
	private TextView zhifa_number;
	@ViewInject(R.id.header)
	private ImageView header;
	@ViewInject(R.id.sex)
	private TextView sex;
	@ViewInject(R.id.area_belong)
	private TextView area_belong;

	private PopupWindow popWindow;
	private LayoutInflater layoutInflater;
	private TextView photograph, albums;
	private LinearLayout cancel;

	public static final int PHOTOZOOM = 0; // 相册/拍照
	public static final int PHOTOTAKE = 1; // 相册/拍照
	public static final int IMAGE_COMPLETE = 2; // 结果
	public static final int CROPREQCODE = 3; // 截取
	private String photoSavePath;// 保存路径
	private String photoSaveName;// 图pian名
	private String path;// 图片全路径

	UserPreference pref;

	BitmapUtils myBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_info);
		pref = new UserPreference(self);
		myBitmap = new BitmapUtils(self);
		myBitmap.configDefaultLoadFailedImage(R.drawable.mine_image_short);
		initView();

		layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		File file = new File(Environment.getExternalStorageDirectory(), "taxi_inspect");
		if (!file.exists())
			file.mkdirs();
		photoSavePath = Environment.getExternalStorageDirectory() + "/taxi_inspect/";
		photoSaveName = System.currentTimeMillis() + ".jpg";
	}

	ArrayList<ZFRYDetailInfo> arrayList;
	String postPath;

	private void initView() {
		ViewUtils.inject(self);
		new HeaderHolder().init(self, "个人信息");
		phone_number.setOnClickListener(this);
		image_header.setOnClickListener(this);
		postPath = Config.SERVER_ZFRY_PHOTO + pref.getString("user_name", null);
		myBitmap.display(header, postPath);

		arrayList = new ArrayList<>();
		final ZFRYDetailInfo info = new ZFRYDetailInfo();
		info.setZfzh(pref.getString("user_name", null));
		AsyncUtil.goAsync(new Callable<Result<List<ZFRYDetailInfo>>>() {

			@Override
			public Result<List<ZFRYDetailInfo>> call() throws Exception {
				return WeiZhanData.queryZFRYinfo(info);
			}
		}, new Callback<Result<List<ZFRYDetailInfo>>>() {

			@Override
			public void onHandle(Result<List<ZFRYDetailInfo>> result) {

				if (result.ok()) {
					arrayList.addAll(result.getData());
					zhifa_number.setText(arrayList.get(0).getZfzh());
					phone_number.setText(arrayList.get(0).getPhone());
					sex.setText(arrayList.get(0).getSex());
					area_belong.setText(arrayList.get(0).getZfdwmc());
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
					return;
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		if (v == phone_number) {
			Intent intent = new Intent(self, ChangPhoneNumberActivity.class);
			startActivityForResult(intent, 12345);
		} else if (v == image_header) {
			showPopupWindow(image_header);
		}
	}

	private void showPopupWindow(View parent) {
		if (popWindow == null) {
			View view = layoutInflater.inflate(R.layout.pop_select_photo, null);
			popWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
			initPop(view);
		}
		popWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
		popWindow.setFocusable(true);
		popWindow.setOutsideTouchable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}

	public void initPop(View view) {
		photograph = (TextView) view.findViewById(R.id.photograph);// 拍照
		albums = (TextView) view.findViewById(R.id.albums);// 相册
		cancel = (LinearLayout) view.findViewById(R.id.cancel);// 取消
		photograph.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popWindow.dismiss();
				/*photoSaveName = String.valueOf(System.currentTimeMillis()) + ".jpg";
				Uri imageUri = null;
				Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				imageUri = FileUtil.getFileUri(getApplicationContext(), new File(photoSavePath, photoSaveName));
				//imageUri = Uri.fromFile(new File(photoSavePath, photoSaveName));
				openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(openCameraIntent, PHOTOTAKE);*/

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				if (intent.resolveActivity(App.self.getPackageManager()) != null) {
					Uri uri = FileUtil.getFileUri(getApplicationContext(), new File(photoSavePath
							, photoSaveName));
					intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
					startActivityForResult(intent, PHOTOTAKE);
				}
			}
		});
		albums.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popWindow.dismiss();
				Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
				openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(openAlbumIntent, PHOTOZOOM);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popWindow.dismiss();

			}
		});
	}

	/**
	 * 图片选择及拍照结果
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		Uri uri = null;
		switch (requestCode) {
			case PHOTOZOOM:// 相册
				if (data == null) {
					return;
				}
				uri = data.getData();
				String[] proj = { MediaStore.Images.Media.DATA };
				Cursor cursor = managedQuery(uri, proj, null, null, null);
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				path = cursor.getString(column_index);// 图片在的路径
				Intent intent3 = new Intent(self, ClipActivity.class);
				intent3.putExtra("path", path);
				startActivityForResult(intent3, IMAGE_COMPLETE);
				break;
			case PHOTOTAKE:// 拍照
				path = photoSavePath + photoSaveName;
				uri = Uri.fromFile(new File(path));
				Intent intent2 = new Intent(self, ClipActivity.class);
				intent2.putExtra("path", path);
				startActivityForResult(intent2, IMAGE_COMPLETE);
				break;
			case IMAGE_COMPLETE:
				final String temppath = data.getStringExtra("path");
				header.setImageBitmap(getLoacalBitmap(temppath));
				// initHeadPhoto(temppath);
				Log.e("文件路径", temppath);
				String url = Config.SERVER_ZFRY_SAVE + pref.getString("user_name", null);
				PhotoImageThread thread = new PhotoImageThread(url, temppath, postPath, myBitmap);
				thread.start();

				break;
			case 12345:
				String phone = data.getStringExtra("phoneNumber");
				phone_number.setText(phone);
				break;
			default:
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void initHeadPhoto(String url) {
		File file = new File(url);
		try {
			Log.e("1", "进入");
			Log.e("2", Config.SERVER_ZFRY_SAVE + pref.getString("user_name", null));
			HttpRequest.uploadFile(Config.SERVER_ZFRY_SAVE + pref.getString("user_name", null), file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);

			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

}
