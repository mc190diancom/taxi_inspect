package com.miu360.taxi_check.ui;

import java.io.IOException;

import com.hitown.idcard.HitownIDcard;
import com.hitown.idcard.HitownIDcard.IdCardCallBack;
import com.hitown.idcard.Info;
import com.miu360.inspect.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IdcardScanActivity extends Activity implements IdCardCallBack {

	private static final String TAG = "IdcardScanActivity";
	private PowerManager.WakeLock mWakeLock;

	private boolean isFirst = true;
	private static final int DIALOG_RECONNECT = 1000;
	private static final int DIALOG_EXIT = 1010;
	private static final int MSG_INIT = 1000;
	private static final int MSG_INIT_RESULT = 1001;

	private RelativeLayout mScanLayout;
	private LinearLayout mInfoLayout;

	private ImageView mPhoto;
	private TextView mWraning, mName, mSex, mNation, mBirthday, mCardNum, mAddress, mDepart, mDate, mGuidInfo;
	private ImageView mScanImg;
	private TextView mResult;
	private AnimationDrawable mAnimation;
	HitownIDcard hitownIDcard = null;
	int countSuccess = 0, countFail = 0;
	boolean isBack = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.idcard_main);
		initView();
		hitownIDcard = new HitownIDcard();

		Log.i("", "--IdcardScanActivity  filesDir=" + IdcardScanActivity.this.getFilesDir().getAbsolutePath());
	}

	@Override
	protected void onStart() {
		super.onStart();

		sendBroadcast(new Intent("android.intent.ACTION_DEVICE_POWER_SERVICE_STOP"));
		if (isFirst) {
			new InitTask().execute("load init");
			isFirst = false;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mWakeLock == null) {
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(
					PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE,
					this.getClass().getCanonicalName());
			mWakeLock.acquire();
		}
	}

	@Override
	protected void onPause() {
		if (mWakeLock != null && mWakeLock.isHeld()) {
			mWakeLock.release();
			mWakeLock = null;
		}
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "onStop");
		try {
			if(hitownIDcard != null){
				hitownIDcard.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			hitownIDcard=null;
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		// mAnimation.stop();//M:Tim 20140804
		sendBroadcast(new Intent("android.intent.ACTION_DEVICE_POWER_SERVICE_START"));
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 按下BACK，同时没有重复
			isBack = true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initView() {
		mScanLayout = (RelativeLayout) findViewById(R.id.idcard_scan_layout);
		mInfoLayout = (LinearLayout) findViewById(R.id.finger_info_layout);
		mPhoto = (ImageView) findViewById(R.id.idcard_info_photo);
		mWraning = (TextView) findViewById(R.id.idcard_info_warning);
		mWraning.setVisibility(View.GONE);
		mName = (TextView) findViewById(R.id.idcard_name);
		mSex = (TextView) findViewById(R.id.idcard_sex);
		mNation = (TextView) findViewById(R.id.idcard_nation);
		mBirthday = (TextView) findViewById(R.id.idcard_birthday);
		mCardNum = (TextView) findViewById(R.id.idcard_num);
		mAddress = (TextView) findViewById(R.id.idcard_address);
		mDepart = (TextView) findViewById(R.id.idcard_department);
		mDate = (TextView) findViewById(R.id.idcard_date);
		mResult = (TextView) findViewById(R.id.result);
		mScanImg = (ImageView) findViewById(R.id.idcard_scan_img);
		mGuidInfo = (TextView) findViewById(R.id.idcard_scan_guidInfo);
		mScanImg.setImageResource(R.drawable.idcard_scan_anim);
		// mScanImg.setBackgroundResource(R.anim.idcard_scan_anim);
		mAnimation = (AnimationDrawable) mScanImg.getDrawable();
		mGuidInfo.setText("设备正在初始化，请稍候...");
		mInfoLayout.setVisibility(View.GONE);
		mScanLayout.setVisibility(View.VISIBLE);
	}

	private void updateInfo(Bundle bu) {
		if (bu == null) {
			mPhoto.setImageBitmap((Bitmap) bu.getParcelable("photo"));
			mName.setText(bu.getString("name"));
			mSex.setText(bu.getString("sex"));
			mNation.setText(bu.getString("nation"));
			mBirthday.setText(bu.getString("birthday"));
			mCardNum.setText(bu.getString("idCode"));
			mAddress.setText(bu.getString("address"));
			mDepart.setText(bu.getString("department"));
			mDate.setText(bu.getString("startDate") + bu.getString("endDate"));
		} else {
			mPhoto.setImageBitmap((Bitmap) bu.getParcelable("photo"));
			mName.setText(bu.getString("name"));
			mSex.setText(bu.getString("sex"));
			mNation.setText(bu.getString("nation"));
			mBirthday.setText(bu.getString("birthday"));
			mCardNum.setText(bu.getString("idCode"));
			mAddress.setText(bu.getString("address"));
			mDepart.setText(bu.getString("department"));
			mDate.setText(bu.getString("startDate") + bu.getString("endDate"));
			String name = bu.getString("name");
			String id = bu.getString("idCode");
			Intent intent = new Intent();
			intent.putExtra("driverName", name);
			intent.putExtra("driverID", id);
			intent.putExtra("isOver", false);
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	private Info getInfoDef() {
		Info info = new Info();
		info.setPeopleName("刘萌");
		info.setPeopleSex("男");
		info.setPeopleNation("汉");
		info.setPeopleBirthday("1966年08月03日");
		info.setPeopleIDCode("152801196608030331");
		info.setPeopleAddress("内蒙古巴彦淖尔市临河区曙光西街付9栋2单元303号");
		info.setDepartment("天津市河东分局");
		info.setStartDate("2011年10月22日");
		info.setEndDate("2023年10月21日");
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.idcard_defphoto);
		info.setPeoplePhoto(bitmap);
		return info;
	}

	private void updateGuidInfo(int arg1, int arg2, Object obj) {
		Log.v("IdcardScanActivity", "updateGuidInfo:arg1 = " + arg1);
		switch (arg1) {
			case HitownIDcard.RESULT_INIT_SUCCED:
				// Toast.makeText(IdcardScanActivity.this, "初始化完成",
				// Toast.LENGTH_SHORT).show();
				hitownIDcard.getIdCardInfo();
				mGuidInfo.setText(getString(R.string.idcard_scan_guid));
				// mGuidInfo.setText("XXXXXXXXXXXXXXXXXXXXXXX");

				break;
			case HitownIDcard.IDCARD_SETP_FIND:
				break;
			case HitownIDcard.IDCARD_SETP_SELECT:
				break;
			case HitownIDcard.IDCARD_SETP_READ:

				mInfoLayout.setVisibility(View.VISIBLE);
				mScanLayout.setVisibility(View.GONE);
				updateInfo(((Info) obj).getBundleInfo());
				if (mAnimation.isRunning()) {
					mAnimation.stop();
				}
				mResult.setText("识别率:" + ++countSuccess * 100 / (countSuccess + countFail) + "%\n成功识别" + countSuccess
						+ "次\n失败识别" + countFail + "次");

				break;
			case -HitownIDcard.IDCARD_SETP_READ:
				mInfoLayout.setVisibility(View.GONE);
				mScanLayout.setVisibility(View.VISIBLE);
				if (!mAnimation.isRunning()) {
					mAnimation.start();
				}
				mGuidInfo.setText("扫描失败，请重新扫描！");
				mResult.setText("识别率:" + countSuccess * 100 / (countSuccess + ++countFail) + "%\n成功识别" + countSuccess
						+ "次\n失败识别" + countFail + "次");
				break;
			case HitownIDcard.RESULT_ERRO_READ:// M:20140803
				Log.v("IdcardScanActivity", "updateGuidInfo:arg1 = " + arg1);
				Log.v("IdcardScanActivity", "mAnimation.isRunning: " + mAnimation.isRunning());
				if (!mAnimation.isRunning()) {
					mAnimation.start();
				}
				break;
			// M:TIM 20140804
			case HitownIDcard.IDCARD_DEVICE:
				mGuidInfo.setText("设备连接成功！");
				break;
			case -HitownIDcard.IDCARD_DEVICE:
				mGuidInfo.setText("设备连接失败，请检查设备！");
				break;
		/*
		 * case HitownIDcard.IDCARD_DEVICE_POWERKEY: mGuidInfo.setText(
		 * "电源键 Down"); break;
		 */
			// ==end
			case HitownIDcard.IDCARD_DEVICE_POWER_WARN:
				showDialog(DIALOG_EXIT);
				break;
			default:
				Log.v("IdcardScanActivity", "updateGuidInfo:arg1 = " + arg1);
				mGuidInfo.setText("设备异常，请检查设备！");
				if (mAnimation.isRunning()) {
					mAnimation.stop();
				}
				if (!isBack) {
					showDialog(DIALOG_RECONNECT);
				}
				break;
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.d("IdcardScanActivity", "handleMsg:msg.what = " + msg.what);

			switch (msg.what) {
				case 0:
					mAnimation.start();
					break;
				case 1:
					updateGuidInfo(msg.arg1, msg.arg2, msg.obj);
					break;
				default:
					break;
			}
		}
	};

	@Override
	public void onReceived(int what, int arg1, int arg2, Object obj) {

		mHandler.obtainMessage(what, arg1, arg2, obj).sendToTarget();
	}

	private class InitTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			Log.v(TAG, "doInBackground  " + params[0]);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				hitownIDcard.init(IdcardScanActivity.this, IdcardScanActivity.this);
			} catch (Exception e) {
				// TODO: handle exception
			}			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		if (id == DIALOG_RECONNECT) {
			Builder builder = new AlertDialog.Builder(this).setTitle("连接失败").setMessage("连接失败，请确定配件是否与手机接触良好！")
					.setPositiveButton("重试", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							new InitTask().execute("reload init");
						}
					}).setNegativeButton("退出", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								hitownIDcard.close();
							} catch (Exception e) {
								e.printStackTrace();
								hitownIDcard=null;
							}
							sendBroadcast(new Intent("android.intent.ACTION_DEVICE_POWER_SERVICE_START"));
							isFirst = true;
							finish();
						}
					}).setOnKeyListener(new OnKeyListener() {

						@Override
						public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
							if (keyCode == KeyEvent.KEYCODE_BACK) {
								try {
									hitownIDcard.close();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								sendBroadcast(new Intent("android.intent.ACTION_DEVICE_POWER_SERVICE_START"));
								isFirst = true;
								finish();
								android.os.Process.killProcess(android.os.Process.myPid());
								System.exit(0);
							}
							return false;
						}
					});
			Dialog dialog = builder.create();
			dialog.setCanceledOnTouchOutside(false);
			return dialog;
		} else if (id == DIALOG_EXIT) {
			new Thread() {

				@Override
				public void run() {
					super.run();
					try {
						Thread.sleep(5 * 1000);
						try {
							hitownIDcard.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						sendBroadcast(new Intent("android.intent.ACTION_DEVICE_POWER_SERVICE_START"));
						isFirst = true;
						finish();
						android.os.Process.killProcess(android.os.Process.myPid());
						System.exit(0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
			Builder builder = new AlertDialog.Builder(this).setTitle("附配件电量过低！").setMessage("为确保二代证正常使用,请及时充电\n5秒后自动退出")
					.setNegativeButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								hitownIDcard.close();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							sendBroadcast(new Intent("android.intent.ACTION_DEVICE_POWER_SERVICE_START"));
							isFirst = true;
							finish();
							android.os.Process.killProcess(android.os.Process.myPid());
							System.exit(0);
						}
					}).setOnKeyListener(new OnKeyListener() {

						@Override
						public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
							if (keyCode == KeyEvent.KEYCODE_BACK) {
								try {
									hitownIDcard.close();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								sendBroadcast(new Intent("android.intent.ACTION_DEVICE_POWER_SERVICE_START"));
								isFirst = true;
								finish();
								android.os.Process.killProcess(android.os.Process.myPid());
								System.exit(0);
							}
							return false;
						}
					});
			Dialog dialog = builder.create();
			dialog.setCanceledOnTouchOutside(false);
			return dialog;
		}
		return super.onCreateDialog(id);
	}
}
