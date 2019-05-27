package com.miu360.taxi_check.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu30.common.data.UserPreference;
import com.miu30.common.util.MyProgressDialog;
import com.miu30.common.util.PermissionManager;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.Config;
import com.miu360.taxi_check.common.MsgConfig;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.data.UserData;
import com.miu360.taxi_check.model.LoginModel;
import com.miu360.taxi_check.service.DownloadService;
import com.miu360.taxi_check.util.MD5;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.util.UpdateUtils;
import com.miu360.taxi_check.util.UpdateUtils.OnUpdateClicklsener;

import java.io.File;
import java.util.concurrent.Callable;

public class LoginActivity extends BaseActivity implements OnClickListener {

    @ViewInject(R.id.et_name)
    private EditText user_name;
    @ViewInject(R.id.et_psw)
    private EditText user_psw;
    @ViewInject(R.id.btn_login)
    private Button loginBtn;
    @ViewInject(R.id.cb_psw)
    private CheckBox remenberPswCb;

    SharedPreferences preferences;
    Editor edit;

    private AlertDialog dialog;
    private TextView tv_Log;
    private ProgressBar update_Progress;// 进度条
    private TextView update_Description;// 显示当前更新进度
    private View viewWindow;
    private String newmVersion;
    private String oldmVersion;
    UpdateUtils updateUtils;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_login);
        initView();
        // checkGps();
        requestPermissions();
        UserPreference pref = new UserPreference(self);
        remenberPswCb.setChecked(pref.getIsChecked());
        if (remenberPswCb.isChecked()) {
            user_psw.setText(pref.getString("user_psw", null));
            user_name.setText(pref.getString("user_name", null));
            remenberPswCb.setChecked(true);
        }
    }

    /*Manifest.permission.SEND_SMS,                 //短信权限
    Manifest.permission.READ_SMS,
     Manifest.permission.READ_CONTACTS,            //读取联系人
    Manifest.permission.RECORD_AUDIO              //录音权限*/
    private void requestPermissions() {
        String[] permissions = new String[]{

                Manifest.permission.WRITE_EXTERNAL_STORAGE,   //访问存储卡
                Manifest.permission.READ_PHONE_STATE,         //获取手机状态
                Manifest.permission.ACCESS_FINE_LOCATION,     //定位权限
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,                   //照相机权限

        };

        new PermissionManager(this) {
            @Override
            protected void success() {
                queryVsCode();
            }

            @Override
            protected void failure() {
                finish();
            }

            @Override
            protected void error(Throwable throwable) {
                ToastUtils.showShort("请求权限出错");
            }
        }.requestPermissions(permissions);
    }

    private void checkGps() {
        if (!isGpgOpened(self)) {
            Windows.confirm(self, "您未打开GPS，存在较大定位不准风险，请立即打开", new OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIUtils.safeOpenLink(self, new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
        }
    }

    public static boolean isGpgOpened(Context ctx) {
        LocationManager alm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);// 位置管理器
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    private void initView() {
        ViewUtils.inject(self);
        UserPreference pref = new UserPreference(self);
        String passWordCheck = pref.getString("user_psw", null);
        registerMsgReceiver();

        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String name = user_name.getText().toString();
                String pass = user_psw.getText().toString();
                if (MsgConfig.isUpdate) {
                    UIUtils.toast(self, "检查更新中，请稍后", Toast.LENGTH_SHORT);
                    return;
                }

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass)) {
                    login();
                } else {
                    UIUtils.toast(self, "用户名或密码不能为空", Toast.LENGTH_SHORT);
                }
                break;
        }

    }

    private void queryVsCode() {
        if (updateUtils == null) {
            updateUtils = new UpdateUtils(this);
        } else {
            updateUtils.queryVersion();
        }
        if (!MsgConfig.isUpdate) {
            MsgConfig.isUpdate = true;
        }
        updateUtils.setOnUpdateCliclenser(new OnUpdateClicklsener() {
            @Override
            public void onUpdate(boolean isUpdate, boolean isqueryOk, String LOG, int length, String mVersion) {
                newmVersion = mVersion;
                try {
                    oldmVersion = self.getPackageManager().getPackageInfo(self.getPackageName(), 0).versionCode + "";
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }
                if (isqueryOk) {
                    if (isUpdate) {
                        MsgConfig.isUpdate = true;
                        if (fileIsExists(newmVersion)) {
                            MsgConfig.isUpdate = false;
                            installApk();
                            return;
                        }
                        if (dialog != null) {
                            dialog.cancel();
                        }
                        View view = LayoutInflater.from(self).inflate(R.layout.item_update_layout, null);
                        dialog = Windows.confirmUpDialog(self, view);
                        dialog.show();
                        viewWindow = dialog.getWindow().getDecorView();
                        tv_Log = (TextView) viewWindow.findViewById(R.id.tv_Log);
                        update_Progress = (ProgressBar) viewWindow.findViewById(R.id.update_Progress);
                        update_Description = (TextView) viewWindow.findViewById(R.id.update_Description);
                        tv_Log.setText(LOG);
                        showDownload(mVersion, length);
                    } else {
                        MsgConfig.isUpdate = false;
                    }
                } else if (!isqueryOk) {
                    MsgConfig.isUpdate = false;
                }

            }
        });

    }

    public void showDownload(String versioin, int length) {
        String url = Config.SERVER_SPECIAL + "?type=downloadApk&version=" + versioin;
        Intent myIntent = new Intent(self, DownloadService.class);
        myIntent.putExtra("URL", url);
        myIntent.putExtra("versioin", versioin);
        myIntent.putExtra("oldversioin", oldmVersion);
        myIntent.putExtra("length", length);
        self.startService(myIntent);
    }

    private void login() {
        final MyProgressDialog pd = Windows.waiting(self);
        AsyncUtil.goAsync(new Callable<Result<String>>() {

            @Override
            public Result<String> call() throws Exception {
                return UserData.login_new(user_name.getText().toString(), MD5.md5(user_psw.getText().toString()));
            }
        }, new Callback<Result<String>>() {

            @Override
            public void onHandle(Result<String> result) {
                pd.dismiss();
                if (result.ok()) {
                    String res = result.getData();
                    LoginModel info = new Gson().fromJson(res, LoginModel.class);
                    if (info == null) {

                    } else {
                        if (info.getImei_code() != null && info.getSim_code() != null) {
                            UserPreference pref = new UserPreference(self);
                            pref.setIsCheckd(remenberPswCb.isChecked());
                            pref.setString("user_name", user_name.getText().toString());
                            pref.setString("user_psw", user_psw.getText().toString());
                            pref.setString("login_id", info.getRode());
                            if (!TextUtils.isEmpty(info.getUserName())) {
                                pref.setString("user_name_update_info", info.getUserName());
                            }
                            Intent target = new Intent();
                            target.setAction("com.miu360.taxi_check.finshAll");
                            target.putExtra("ignore", "");
                            LocalBroadcastManager.getInstance(self).sendBroadcast(target);
                            goNext();
                        } else {
                            UIUtils.toast(self, "用户信息有误", Toast.LENGTH_SHORT);
                        }
                    }
                } else {
                    UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
                }
                /*
                 * if (result.ok()) { UserPreference pref = new
                 * UserPreference(self);
                 * pref.setIsCheckd(remenberPswCb.isChecked());
                 * pref.setString("user_name", user_name.getText().toString());
                 * pref.setString("user_psw", user_psw.getText().toString());
                 *
                 * Intent target = new Intent();
                 * target.setAction("com.miu360.taxi_check.finshAll");
                 * target.putExtra("ignore", "");
                 * LocalBroadcastManager.getInstance(self).sendBroadcast(target)
                 * ; goNext(); }
                 */
            }
        });
    }

    public void registerMsgReceiver() {
        IntentFilter filter = new IntentFilter("com.miu360.update");
        registerReceiver(msgReceiver, filter);
    }

    public void unregisterMsgReceiver() {
        try {
            unregisterReceiver(msgReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BroadcastReceiver msgReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int progress = intent.getIntExtra("update", 0);

            if (progress == -1) {
                MsgConfig.isUpdate = false;

                String tishi = "检查新版本失败！请在：我的-关于-检查新版本，检查更新";
                Windows.confirm(self, 0, "更新失败", tishi, "确定", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NotificationManager mNotifyManager = (NotificationManager) getSystemService(
                                Context.NOTIFICATION_SERVICE);
                        mNotifyManager.cancel(0);
                        if (dialog != null) {
                            dialog.cancel();
                        }

                    }

                }, null, new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                    }
                }, 0, null);
                return;
            }
            if (viewWindow != null) {
                update_Progress.setProgress(progress);
                update_Description.setText("当前下载进度:" + progress + "%");
                if (progress == 99) {
                    dialog.dismiss();
                }
            }

        }
    };

    public boolean fileIsExists(String versioin) {
        try {
            File f = new File(Config.PATH + versioin + ".apk");
            Log.e("zar", "length==" + f.length());

            if (f.exists() && f.length() > 45197386) {
                return true;
            }
        } catch (Exception e) {
            // TODO: handle exceptionreturn false;}return true;}
            return false;
        }
        return false;
    }

    protected void onResume() {
        //queryVsCode();
        super.onResume();
    }

    ;

    @Override
    protected void onDestroy() {
        unregisterMsgReceiver();
        super.onDestroy();
    }

    private void installApk() {
        File apkfile = new File(Config.PATH + newmVersion + ".apk");
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        startActivity(i);
    }

    protected void goNext() {
        startActivity(new Intent(self, MainActivity.class));
        finish();
    }
}
