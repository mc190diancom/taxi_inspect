package com.miu360.taxi_check.ui;

import java.io.BufferedReader;
//缓存
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.ContentBody;
import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.miu360.taxi_check.App;
import com.miu360.taxi_check.util.UIUtils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class CreateCaseActivity extends Activity {
	private ProgressDialog pd;

	public static final String PATH = Environment.getExternalStorageDirectory().toString() + "/AndroidWT";
	public static final String TAG = "CreateCaseActivity";
	private String selectPath;
	private boolean isCatchPreview = false;
	private boolean isCatchPicture = false;
	private int srcwidth;
	private int srcheight;
	private int WIDTH;
	private int HEIGHT;
	int nMainID = 0;
	int[] nSubID;
	public static int DIALOG_ID = -1;
	public boolean isVINRecog = false;
	public int recogType = -1;// 1代表自动识别，2代表划框识别，3代表划线识别
	private Boolean cutBoolean = true;
	private String resultFileNameString = "";

	public int mCarS = 0, mSpinnerP1 = 0, mSpinnerP2 = 0, mSpinnerP3 = 0, mSpinnerP4 = 0, mSpinnerP5 = 0,
			mSpinnerP6 = 0, mSpinnerP7 = 0, mSpinnerP8 = 0;
	private final int DATE_DIALOG = 0;
	private final int TIME_DIALOG = 1;
	String date3;
	boolean ccbcres;
	boolean Isnetwork;
	private boolean scren;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// 解决4.0版本不能在主线程里面访问网络的问题
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// 调用识别身份证扫描
		GetIDCardInfo();
		if (getIntent().getBooleanExtra("takephoto_direct", false)) {
			ClickAll();
		}
	}

	private float titleFontSize;
	private float screenWidth; // 屏幕宽
	private float screenHeight; // 屏幕高

	private void ClickAll() {
		// 创建身份证扫描事件

		Log.i(TAG, "isCatchPreview=" + isCatchPreview + " isCatchPicture=" + isCatchPicture);
		nMainID = 2;
		// if (isCatchPreview == true && isCatchPicture == true) {
		Intent intent = new Intent();
		Log.i(TAG, "拍摄分辨率为: " + srcwidth + " * " + srcheight);
		Log.i(TAG, "预览分辨率为: " + WIDTH + " * " + HEIGHT);

		intent.putExtra("srcwidth", srcwidth);
		intent.putExtra("srcheight", srcheight);
		/***************/

		intent.putExtra("TheCaseSource", mSpinnerP1 + "");
		intent.putExtra("IndustryType", mSpinnerP2 + "");
		intent.putExtra("ProcessingMode", mSpinnerP3 + "");
		intent.putExtra("ContingentName", mSpinnerP4 + "");
		intent.putExtra("OrganNo", mSpinnerP5 + "");
		intent.putExtra("CarGroup", mSpinnerP6 + "");
		intent.putExtra("EnforcementPerson1", mSpinnerP7 + "");
		intent.putExtra("EnforcementPerson2", mSpinnerP8 + "");
		// intent.putExtra("CarS", mCarS);
		/**************/

		intent.setClass(CreateCaseActivity.this, CamerassActivity.class);

		startActivityForResult(intent, 1);

	}

	public int Getmesh() {
		ConnectivityManager mConnectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mTelephony = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		if (info == null || !mConnectivity.getBackgroundDataSetting()) {
			return 0;
		}
		int netType = info.getType();
		int netSubtype = info.getSubtype();
		if (netType == ConnectivityManager.TYPE_WIFI) { // WIFI
			return 1;
		} else if (netType == ConnectivityManager.TYPE_MOBILE && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
				&& !mTelephony.isNetworkRoaming()) {
			return 2;
		} else {
			return 0;
		}
	}

	private void GetIDCardInfo() {
		// 调用识别
		Intent intentget = this.getIntent();
		selectPath = intentget.getStringExtra("path");
		cutBoolean = intentget.getBooleanExtra("cut", true);
		nMainID = intentget.getIntExtra("nMainID", 0);

		String LicenseNo = intentget.getStringExtra("LicenseNo");
		String Phone = intentget.getStringExtra("Phone");

		String TheCaseSource = intentget.getStringExtra("TheCaseSource");
		String IndustryType = intentget.getStringExtra("IndustryType");
		String ProcessingMode = intentget.getStringExtra("ProcessingMode");
		String ContingentName = intentget.getStringExtra("ContingentName");
		String OrganNo = intentget.getStringExtra("OrganNo");
		String CarGroup = intentget.getStringExtra("CarGroup");
		String EnforcementPerson1 = intentget.getStringExtra("EnforcementPerson1");
		String EnforcementPerson2 = intentget.getStringExtra("EnforcementPerson2");
		// int CarSs=intentget.getIntExtra("CarS", 0);
		if (selectPath != null && !selectPath.equals("")) {

			int index = selectPath.lastIndexOf("/");
			resultFileNameString = (String) selectPath.subSequence(index + 1, selectPath.length());

			try {
				String logopath = "";
				// String logopath = getSDPath() + "/photo_logo.png";
				Intent intent = new Intent("wintone.idcard");
				Bundle bundle = new Bundle();
				int nSubID[] = null;// {0x0001};
				bundle.putString("cls", getClass().getName());
				bundle.putInt("nTypeInitIDCard", 0); // 保留，传0即可
				bundle.putString("lpFileName", selectPath);// 指定的图像路径
				bundle.putInt("nTypeLoadImageToMemory", 0);// 0不确定是哪种图像，1可见光图，2红外光图，4紫外光图
				if (nMainID == 1000) {
					nSubID[0] = 3;
				}
				bundle.putInt("nMainID", nMainID); // 证件的主类型。6是行驶证，2是二代证，这里只可以传一种证件主类型。每种证件都有一个唯一的ID号，可取值见证件主类型说明
				bundle.putIntArray("nSubID", nSubID); // 保存要识别的证件的子ID，每个证件下面包含的子类型见证件子类型说明。nSubID[0]=null，表示设置主类型为nMainID的所有证件。
				// bundle.putBoolean("GetSubID", true); //GetSubID得到识别图像的子类型id
				// bundle.putString("lpHeadFileName",
				// "/mnt/sdcard/head.jpg");//保存路径名，后缀只能为jpg、bmp、tif
				// bundle.putBoolean("GetVersionInfo", true); //获取开发包的版本信息

				// 读设置到文件里的sn
				File file = new File(PATH);
				String snString = null;
				if (file.exists()) {
					String filePATH = PATH + "/IdCard.sn";
					File newFile = new File(filePATH);
					if (newFile.exists()) {
						BufferedReader bfReader = new BufferedReader(new FileReader(newFile));
						snString = bfReader.readLine().toUpperCase();
						bfReader.close();
					} else {
						bundle.putString("sn", "");
					}
					if (snString != null && !snString.equals("")) {
						bundle.putString("sn", snString);
						// String string = (String) bundle.get("sn");
						// Toast.makeText(getApplicationContext(),
						// "snString=="+string, 3000).show();
					} else {
						bundle.putString("sn", "");
					}
				} else {
					bundle.putString("sn", "");
				}

				// bundle.putString("datefile",
				// Environment.getExternalStorageDirectory().toString()+"/wtdate.lsc");
				// bundle.putString("devcode", "SBGAQC7EZAIAXRY");
				// bundle.putString("versionfile",
				// Environment.getExternalStorageDirectory().toString()+"/wtversion.lsc");
				// bundle.putString("sn", "UTL25WSMJVPYJE6YY7HMYY33A");邓瑶冬的手机
				bundle.putString("sn", "UT6NQWRTLFYYP3QYY1ZDYYB5N");// 专线测试机
				// 序列号激活方式,XS4XAYRWEFRY248YY4LHYY178已使用
				// bundle.putString("authfile", ""); // 文件激活方式

				bundle.putString("logo", logopath); // logo路径，logo显示在识别等待页面右上角
				bundle.putBoolean("isCut", cutBoolean); // 如不设置此项默认自动裁切
				bundle.putString("returntype", "withvalue");// 返回值传递方式withvalue带参数的传值方式（新传值方式）
				/***************/
				bundle.putString("userdata",
						LicenseNo + ";" + Phone + ";" + TheCaseSource + ";" + IndustryType + ";" + ProcessingMode + ";"
								+ ContingentName + ";" + OrganNo + ";" + CarGroup + ";" + EnforcementPerson1 + ";"
								+ EnforcementPerson2);// +";"+CarSs
				/*********************/

				intent.putExtras(bundle);
				startActivityForResult(intent, 8);
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "没有找到应用程序" + "wintone.idcard", 0).show();
			}

		} else {
			checkCameraParameters();
			nMainID = 2;// readMainID();
		}
	}

	public void checkCameraParameters() {
		// 读取支持的预览尺寸
		Camera camera = null;
		try {
			camera = Camera.open();
			if (camera != null) {
				// 读取支持的预览尺寸,优先选择640后320
				Camera.Parameters parameters = camera.getParameters();
				List<Integer> SupportedPreviewFormats = parameters.getSupportedPreviewFormats();
				for (int i = 0; i < SupportedPreviewFormats.size(); i++) {
					System.out.println("PreviewFormats=" + SupportedPreviewFormats.get(i));
				}
				Log.i(TAG, "preview-size-values:" + parameters.get("preview-size-values"));
				List<Camera.Size> previewSizes = splitSize(parameters.get("preview-size-values"), camera);// parameters.getSupportedPreviewSizes();
				for (int i = 0; i < previewSizes.size(); i++) {
					if (previewSizes.get(i).width == 640 && previewSizes.get(i).height == 480) {
						isCatchPreview = true;
						WIDTH = 640;
						HEIGHT = 480;
						break;
					}
					if (previewSizes.get(i).width == 320 && previewSizes.get(i).height == 240) {
						isCatchPreview = true;
						WIDTH = 320;
						HEIGHT = 240;
					}
				}
				Log.i(TAG, "isCatchPreview=" + isCatchPreview);

				// 读取支持的相机尺寸,优先选择1280后1600后2048
				List<Integer> SupportedPictureFormats = parameters.getSupportedPictureFormats();
				for (int i = 0; i < SupportedPictureFormats.size(); i++) {
					System.out.println("PictureFormats=" + SupportedPictureFormats.get(i));
				}
				Log.i(TAG, "picture-size-values:" + parameters.get("picture-size-values"));
				List<Camera.Size> PictureSizes = splitSize(parameters.get("picture-size-values"), camera);// parameters.getSupportedPictureSizes();
				for (int i = 0; i < PictureSizes.size(); i++) {
					if (PictureSizes.get(i).width == 2048 && PictureSizes.get(i).height == 1536) {
						if (isCatchPicture == true) {
							break;
						}
						isCatchPicture = true;
						srcwidth = 2048;
						srcheight = 1536;
					}
					if (PictureSizes.get(i).width == 1600 && PictureSizes.get(i).height == 1200) {
						isCatchPicture = true;
						srcwidth = 1600;
						srcheight = 1200;
					}
					if (PictureSizes.get(i).width == 1280 && PictureSizes.get(i).height == 960) {
						isCatchPicture = true;
						srcwidth = 1280;
						srcheight = 960;
						break;
					}
				}
				Log.i(TAG, "isCatchPicture=" + isCatchPicture);
			}
			camera.release();
			camera = null;
		} catch (Exception e) {
			e.printStackTrace();
			camera.release();
			camera = null;
		} finally {
			if (camera != null) {
				try {
					camera.release();
					camera = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private ArrayList<Size> splitSize(String str, Camera camera) {
		if (str == null)
			return null;
		StringTokenizer tokenizer = new StringTokenizer(str, ",");
		ArrayList<Size> sizeList = new ArrayList<Size>();
		while (tokenizer.hasMoreElements()) {
			Size size = strToSize(tokenizer.nextToken(), camera);
			if (size != null)
				sizeList.add(size);
		}
		if (sizeList.size() == 0)
			return null;
		return sizeList;
	}

	private Size strToSize(String str, Camera camera) {
		if (str == null)
			return null;
		int pos = str.indexOf('x');
		if (pos != -1) {
			String width = str.substring(0, pos);
			String height = str.substring(pos + 1);
			return camera.new Size(Integer.parseInt(width), Integer.parseInt(height));
		}
		return null;
	}

	public static final String ACTION_GET_ID_CARD = "action.get_id_card";

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "onActivityResult");

		if (requestCode == 8 && resultCode == RESULT_OK) {
			// 读识别返回值
			int ReturnAuthority = data.getIntExtra("ReturnAuthority", -100000);// 取激活状态
			int ReturnInitIDCard = data.getIntExtra("ReturnInitIDCard", -100000);// 取初始化返回值
			int ReturnLoadImageToMemory = data.getIntExtra("ReturnLoadImageToMemory", -100000);// 取读图像的返回值
			int ReturnRecogIDCard = data.getIntExtra("ReturnRecogIDCard", -100000);// 取识别的返回值

			Log.i(TAG, "ReturnLPFileName:" + data.getStringExtra("ReturnLPFileName"));

			if (ReturnAuthority == 0 && ReturnInitIDCard == 0 && ReturnLoadImageToMemory == 0
					&& ReturnRecogIDCard > 0) {

				String[] fieldname = (String[]) data.getSerializableExtra("GetFieldName");
				String[] fieldvalue = (String[]) data.getSerializableExtra("GetRecogResult");
				String ReturnLPFileName = data.getStringExtra("ReturnLPFileName");
				String ReturnUserData = data.getStringExtra("ReturnUserData");

				if (null != ReturnLPFileName) {
					// Bitmap bitmap = getLoacalBitmap(ReturnLPFileName); //
					// 从本地取图片
					// imageViewidcardo.setImageBitmap(bitmap); // 设置Bitmap
				}

				if (null != ReturnUserData) {
					String[] userdata = ReturnUserData.split(";");
					if (userdata.length > 0) {
						String mIDs = userdata[0];
						String NLText3s = userdata[1];
						Integer TheCaseSources = Integer.valueOf(userdata[2]);
						Integer IndustryTypes = Integer.valueOf(userdata[3]);
						Integer ProcessingModes = Integer.valueOf(userdata[4]);
						Integer ContingentNames = Integer.valueOf(userdata[5]);
						Integer OrganNos = Integer.valueOf(userdata[6]);
						Integer CarGroups = Integer.valueOf(userdata[7]);
						Integer EnforcementPerson1s = Integer.valueOf(userdata[8]);
						Integer EnforcementPerson2s = Integer.valueOf(userdata[9]);
						// CarSs=Integer.valueOf(userdata[10]);
					}
				}

				if (null != fieldvalue) {
					// 保留, 姓名, 性别, 民族, 出生, 住址, 公民身份号码
					String DSRNameTexts = fieldvalue[1];
					// UIUtils.toast(App.self, Arrays.toString(fieldvalue), 0);
					String NLTexts = fieldvalue[4];
					String NLText1s = fieldvalue[5];
					String NLText2s = fieldvalue[6];
					String gendergroup = fieldvalue[2];
					Intent retData = new Intent();
					retData.putExtra("name", fieldvalue[1]);
					retData.putExtra("id_card", fieldvalue[6]);
					setResult(RESULT_OK, data);

					Intent bd = new Intent(ACTION_GET_ID_CARD);
					bd.putExtra("name", fieldvalue[1]);
					bd.putExtra("id_card", fieldvalue[6]);
					LocalBroadcastManager.getInstance(App.self).sendBroadcast(bd);
				}
				finish();
			} else {
				String str = "";
				if (ReturnAuthority == -100000) {
					str = "未识别   代码： " + ReturnAuthority;
				} else if (ReturnAuthority != 0) {
					str = "激活失败 代码：" + ReturnAuthority;
				} else if (ReturnInitIDCard != 0) {
					str = "识别初始化失败 代码：" + ReturnInitIDCard;
				} else if (ReturnLoadImageToMemory != 0) {
					if (ReturnLoadImageToMemory == 3) {
						str = "识别载入图像失败，请重新识别 代码：" + ReturnLoadImageToMemory;
					} else if (ReturnLoadImageToMemory == 1) {
						str = "识别载入图像失败，识别初始化失败,请重试 代码：" + ReturnLoadImageToMemory;
					} else {
						str = "识别载入图像失败 代码：" + ReturnLoadImageToMemory;
					}
				} else if (ReturnRecogIDCard != 0) {
					str = "识别失败 代码：" + ReturnRecogIDCard;
				}
				UIUtils.toast(App.self, str, Toast.LENGTH_SHORT);
				finish();
			}
		} else {
			finish();
		}
	}

	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString();

	}

	/**
	 * 加载本地图片 http://bbs.3gstdy.com
	 *
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

	public String post(String pathToOurFile, String urlServer, String TemName)
			throws ClientProtocolException, IOException, JSONException {
		HttpClient httpclient = new DefaultHttpClient();
		// 设置通信协议版本
		httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		Charset charset = Charset.forName(HTTP.UTF_8);

		HttpPost httppost = new HttpPost(urlServer);
		File file = new File(pathToOurFile);

		MultipartEntity mpEntity = new MultipartEntity(); // 文件传输

		// SimpleDateFormat sDateFormat = new
		// SimpleDateFormat("yyyyMMddHHmmss");
		// String date = sDateFormat.format(new java.util.Date());
		// String[] fn = TemName.split("_");
		// String extraFn = TemName.substring(TemName.lastIndexOf("."));
		@SuppressWarnings("deprecation")
		ContentBody cbFile = new FileBody(file, TemName, "application/x-www-form-urlencoded", HTTP.UTF_8);//

		mpEntity.addPart("userfile", cbFile);

		httppost.setEntity(mpEntity);

		// 这行很重要
		System.out.println("executing request " + httppost.getRequestLine());

		HttpResponse response = httpclient.execute(httppost);
		HttpEntity resEntity = response.getEntity();

		System.out.println(response.getStatusLine());// 通信Ok
		String json = "";
		String path = "";
		if (resEntity != null) {
			json = EntityUtils.toString(resEntity, "utf-8");
			JSONObject p = null;
			try {
				p = new JSONObject(json);
				path = (String) p.get("path");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (resEntity != null) {
			resEntity.consumeContent();
		}

		httpclient.getConnectionManager().shutdown();
		return path;
	}

	public void deleteFiles(String... fileNames) {
		if (fileNames.length <= 0)
			return;
		ContentResolver cr = getContentResolver();
		for (int i = 0; i < fileNames.length; i++) {
			File file = new File(fileNames[i]);
			if (file.exists())
				file.delete();
			cr.delete(Media.EXTERNAL_CONTENT_URI, Media.DATA + "=?", fileNames);
			cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + " LIKE ?",
					fileNames);
		}
	}

	@SuppressLint("SimpleDateFormat")
	public static String getTime3() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

}
