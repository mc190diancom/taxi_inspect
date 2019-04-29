package com.miu360.taxi_check.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;

import com.google.zxing.client.android.CaptureActivity;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hitown.hitownsdk.HitownAC3508Control;
import com.hitown.hitownsdk.HitownAC3508Control.PrinterCallback;
import com.lubao.lubao.async.AsyncUtil;
import com.miu360.inspect.R;
import com.miu360.inspect.R.id;
import com.miu360.inspect.R.layout;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.data.HtmlData;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.Driver;
import com.miu360.taxi_check.util.UIUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


public class QRcodeActivity extends Activity implements PrinterCallback {
    private TextView tvShow;

    protected Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            handleMsg(msg);
            return true;
        }
    });
    private static final int MSG_BARCODE_SCANNING = 2;
    private static final int MSG_BARCODE_RESULT = 3;
    private int san_times;

    protected void handleMsg(Message msg) {
        switch (msg.what) {

            case 0:
                showToast((String) msg.obj);
                break;
            case 1:
                handleResult((Integer) msg.obj);
                break;
            case 200:
                break;
            // start add by liaoyl
            case MSG_BARCODE_SCANNING:
                // 扫描
                tvShow.setText("扫描...");
                break;
            case MSG_BARCODE_RESULT:
                String result = (String) msg.obj;
                if ("未扫描到有效信息".equals(msg.obj)) {
                    if (end) {
                        return;
                    }
                    if (san_times < 5) {
                        try {
                            Thread.sleep(100);
                            HitownAC3508Control.getInstance().scanBarcode();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        san_times++;
                    } else {
                        showToast("扫描失败");
                        finish();
                    }
                } else {
                    loadInfo(result);

                }
                // 扫描完毕
                break;
            default:
                break;
        }
    }

    private void loadInfo(final String resultString) {
        Log.e("zar", "zar" + resultString);

        if (!resultString.contains("219.232.196.42")) {
            UIUtils.toast(QRcodeActivity.this, "二维码错误，扫描失败！", Toast.LENGTH_LONG);
            finish();
        }
        final Dialog d = Windows.waiting(QRcodeActivity.this);
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
                    resultIntent.putExtra("result", result.getData());
                    resultIntent.putExtra("isOver", true);
                    showToast("扫描成功");
                    setResult(RESULT_OK, resultIntent);
                } else {
                    UIUtils.toast(QRcodeActivity.this, result.getErrorMsg(), Toast.LENGTH_SHORT);
                }
                finish();
            }
        });
    }
	/*private void loadInfo(final String resultString) {
		Log.e("2", resultString);
		if (!resultString.contains("219.232.196.42")) {
			UIUtils.toast(this, "扫描信息错误", Toast.LENGTH_LONG);
			finish();
		}
		final Dialog d = Windows.waiting(this);
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
					setResult(RESULT_OK, resultIntent);
					finish();
				} else {
					UIUtils.toast(QRcodeActivity.this, result.getErrorMsg(), Toast.LENGTH_SHORT);
				}
				finish();
			}
		});
	}*/

    private boolean end;
    private static final String BAUDRATE_FILE = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/qh_baudrate.txt";
    private String StringBaudRate;
    private boolean isBaudRateLow = false;
    private int[] elevel_array = new int[10];

    /**
     * 显示结果
     */
    private void handleResult(int result) {
        String info = "";
        switch (result) {
            case HitownAC3508Control.RESULT_OPEN_SUCCESS:
                boolean exists = new java.io.File(BAUDRATE_FILE).exists();
                // 115200
                if (!exists) {
                    writeFile(BAUDRATE_FILE, "9600");
                    StringBaudRate = "9600";
                } else {
                    StringBaudRate = readFile(BAUDRATE_FILE);
                }
                if (0 == StringBaudRate.compareTo("9600")) {
                    isBaudRateLow = true;
                } else {
                    isBaudRateLow = false;
                }
                HitownAC3508Control.getInstance().changeBaudRate(isBaudRateLow);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < elevel_array.length; i++) {
                    elevel_array[i] = 0;
                }

                info = "打开设备成功";
                san_times = 0;
                mHandler.obtainMessage(MSG_BARCODE_SCANNING).sendToTarget();
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            lockScreen();
                            HitownAC3508Control.getInstance().scanBarcode();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 300);
                break;
            case HitownAC3508Control.RESULT_OPEN_FAIL:
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, 0);
                break;
            case HitownAC3508Control.RESULT_ERRO_SENDCMD:
                info = "发送命令失败!";
                break;
            case HitownAC3508Control.RESULT_ERRO_READ:
                info = "读取数据失败!";
                break;
            case HitownAC3508Control.RESULT_ERRO_CLOSE:
                info = "关闭设备失败!";
                break;
            default:
                info = "default..";
                break;
        }
        showToast(info);
    }

    protected void printException(Exception e) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream(
                new File(Environment.getExternalStorageDirectory(), "qr.txt"));
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        for (int i = 0; i < stackTraceElements.length; i++) {
            fileOutputStream.write((stackTraceElements[i].toString() + "\n").getBytes());
        }
        fileOutputStream.close();
    }

    /**
     * 打印toast
     */
    protected void showToast(String msg) {
        if (!msg.equals("")) {
            UIUtils.toast(this, msg, Toast.LENGTH_SHORT);
        } else {

        }
    }

    /**
     * 写成文件
     */
    public static void writeFile(String path, String data) {
        FileOutputStream f_write = null;
        try {
            f_write = new FileOutputStream(path);
            f_write.write(data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                f_write.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取文件
     */
    public String readFile(String path) {
        FileInputStream f_read = null;
        byte[] data = new byte[10];
        String mstring;
        int i = 0;
        try {
            f_read = new FileInputStream(path);
            f_read.read(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                f_read.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mstring = String.format("%x", data[0] - 0x30);
        while (data[++i] >= 0x30)
            mstring += String.format("%x", data[i] - 0x30);
        return mstring;
    }

    private WakeLock mWakeLock;

    @SuppressWarnings("deprecation")
    protected void lockScreen() {
        if (mWakeLock == null) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(
                    PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE,
                    "QRcodeActivity");
            mWakeLock.acquire();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        tvShow = (TextView) findViewById(R.id.tv);
        PullToRefreshListView list = new PullToRefreshListView(getApplicationContext());
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                HitownAC3508Control.getInstance().init(QRcodeActivity.this);
            }
        }, 500);
    }

    @Override
    protected void onPause() {
        if (mWakeLock != null) {
            mWakeLock.release();
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        end = true;
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        try {
            HitownAC3508Control.getInstance().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("result", data.getStringExtra("result"));
            resultIntent.putExtra("type", data.getIntExtra("type", 0));
            resultIntent.putExtra("isOver", true);
            setResult(RESULT_OK, resultIntent);
        }
        finish();
    }

    @Override
    public void onBarcodeReceiveResult(byte[] data) {
        String result = "";

        if (data.length <= 5) {
            result = "未扫描到有效信息";
        } else {
            if (!result.matches("[a-zA-Z0-9]+")) {
                result = new String(data);
            } else {
                result = "无效信息";
            }
        }
        mHandler.obtainMessage(MSG_BARCODE_RESULT, result).sendToTarget();
    }

    @Override
    public void onReceiveErro(int erro) {
        String erroMsg = "";
        switch (erro) {
            case HitownAC3508Control.BARCODE_ERRO_INIT:
                erroMsg = "设备初始化失败！";
                break;
            case HitownAC3508Control.BARCODE_ERRO_NOTREADY:
                erroMsg = "设备初始化未完成，请稍等";
                break;
            case HitownAC3508Control.BARCODE_ERRO_BUSY:
                erroMsg = "设备正忙...";
                break;
            case HitownAC3508Control.BARCODE_ERRO_SCAN:
                erroMsg = "读取数据失败！";
                mHandler.obtainMessage(MSG_BARCODE_RESULT, "未扫描到有效信息").sendToTarget();
                break;
            case HitownAC3508Control.BARCODE_ERRO_CLOSE:
                erroMsg = "关闭设备失败!";
                break;
            default:
                erroMsg = "";
                break;
        }
        showToast(erroMsg);
    }

    @Override
    public void onReceiveLogs(String logs) {
        mHandler.obtainMessage(0, logs).sendToTarget();
    }

    @Override
    public void onReceiveResult(int result) {
        mHandler.obtainMessage(1, result).sendToTarget();
    }
}
