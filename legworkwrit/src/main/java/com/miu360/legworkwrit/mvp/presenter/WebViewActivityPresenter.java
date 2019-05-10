package com.miu360.legworkwrit.mvp.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.epson.isv.eprinterdriver.Common.EpsPrinter;
import com.epson.isv.eprinterdriver.Common.EpsStatus;
import com.epson.isv.eprinterdriver.Ctrl.EPSetting;
import com.epson.isv.eprinterdriver.Ctrl.ISearchPrinterListener;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.miu30.common.MiuBaseApp;
import com.miu30.common.async.Result;
import com.miu30.common.util.CommonDialog;
import com.miu30.common.util.MyProgressDialog;
import com.miu30.common.util.UIUtils;
import com.miu30.common.util.Windows;
import com.miu30.common.app.MyErrorHandleSubscriber;
import com.miu360.legworkwrit.app.utils.RxUtils;
import com.miu360.legworkwrit.mvp.contract.WebViewActivityContract;
import com.miu360.legworkwrit.mvp.data.WifiPreference;
import com.miu360.legworkwrit.mvp.model.entity.ParentQ;
import com.miu360.legworkwrit.mvp.model.entity.WifiConfig;
import com.miu360.legworkwrit.util.DialogUtil;
import com.miu360.legworkwrit.util.MyWebViewUtil;
import com.miu360.legworkwrit.util.WebViewFactory;
import com.miu360.legworkwrit.util.WifiUtil;
import com.miu360.legworkwrit.util.printer.PrinterManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.Lazy;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
@SuppressLint("LogNotTimber")
public class WebViewActivityPresenter extends BasePresenter<WebViewActivityContract.Model, WebViewActivityContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    Lazy<WebViewFactory> webViewFactory;

    public static final String RECORD_ITEM = "record_item";

    private EpsPrinter printer;

    private Activity activity;

    private MyProgressDialog waiting;

    private Handler handler = new Handler(Looper.getMainLooper());

    private boolean isSearching = false;
    private boolean isPreView;

    private int pages = 1;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);

                switch (wifiState) {
                    case WifiManager.WIFI_STATE_ENABLED:
                        Log.d(PrinterManager.TAG, "wifi已打开");

                        WifiUtil.getInstance().scan();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                choosePrinter();
                            }
                        }, 1000);
                        break;
                    default:
                        PrinterManager.getInstance().cancelSearch();
                        printer = null;
                        break;
                }
            } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (info != null) {
                    if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                        WifiInfo wifiInfo = WifiUtil.getInstance().getConnectInfo();

                        if (chooseWifi != null) {
                            if (wifiInfo.getSSID() != null) {
                                //通过广播得到的wifi ssid在高版本上有双引号
                                if (wifiInfo.getSSID().equalsIgnoreCase("\"" + chooseWifi.getSsid() + "\"")
                                        || wifiInfo.getSSID().equalsIgnoreCase(chooseWifi.getSsid())) {
                                    Log.e(PrinterManager.TAG, "准备搜索打印机");

                                    if (!isSearching) {
                                        searchPrinter();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    };
    private boolean pageIsFinish;//当打印页数为最后一页
    private CommonDialog commonDialog;

    @Inject
    public WebViewActivityPresenter(WebViewActivityContract.Model model, WebViewActivityContract.View rootView) {
        super(model, rootView);
    }

    public void init(Activity activity, boolean isPreView) {
        this.activity = activity;
        this.isPreView = isPreView;
        if (!isPreView) {
            initWifi();
        }
        preView();
    }

    private void getAllWifis() {
        mModel.getAllPrinterWifis("selectWifiAll")
                .compose(RxUtils.<Result<List<WifiConfig>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<WifiConfig>>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<List<WifiConfig>> listResult) throws Exception {
                        if (listResult.ok()) {
                            wifiPreference.clearPreference();
                            List<WifiConfig> configs = listResult.getData();

                            if (configs != null && !configs.isEmpty()) {
                                for (WifiConfig config : configs) {
                                    wifiPreference.putWifi(config);
                                }
                            }

                            transform(configs);
                        } else {
                            Windows.confirm(activity, "获取wifi配置信息失败，是否重新获取？", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getAllWifis();
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    activity.finish();
                                }
                            });
                        }
                    }
                });
    }

    private WifiPreference wifiPreference;
    private Map<String, WifiConfig> wifiMap;
    private WifiConfig chooseWifi;

    private void initWifi() {
        wifiPreference = new WifiPreference(MiuBaseApp.self);
        transform(wifiPreference.getWifis());
    }

    private void transform(List<WifiConfig> configs) {
        if (configs == null || configs.isEmpty()) {
            getAllWifis();
        } else {
            wifiMap = new HashMap<>(configs.size());
            for (WifiConfig config : configs) {
                wifiMap.put(config.getSn(), config);
            }

            openWifi();
        }
    }

    private void openWifi() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        activity.registerReceiver(receiver, filter);

        if (!WifiUtil.getInstance().isOpen()) {
            WifiUtil.getInstance().open();
        }
    }

    public void choosePrinter() {
        Log.d(PrinterManager.TAG, "choosePrinter()");

        ArrayList<String> showWifiSns = new ArrayList<>();
        List<ScanResult> result = WifiUtil.getInstance().getScanResult();
        if (wifiMap != null && result != null) {
            Collection<WifiConfig> values = wifiMap.values();
            for (ScanResult scan : result) {
                for (WifiConfig config : values) {
                    if (config.getSsid().equals(scan.SSID)) {
                        showWifiSns.add(config.getSn());
                    }
                }
            }
        }

        final String[] showWifiSnsArray = showWifiSns.toArray(new String[showWifiSns.size()]);

        commonDialog = Windows.singleChoice(activity, "请选择需要连接的打印机", showWifiSnsArray, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                WifiConfig config = wifiMap.get(showWifiSnsArray[position]);
                mRootView.onCurrentChoosePrinter(config);
                connectWifi(config);
            }
        });
    }

    private void connectWifi(WifiConfig config) {
        this.chooseWifi = config;

        WifiInfo connectInfo = WifiUtil.getInstance().getConnectInfo();
        if (connectInfo.getSSID().equalsIgnoreCase("\"" + chooseWifi.getSsid() + "\"")
                || connectInfo.getSSID().equalsIgnoreCase(chooseWifi.getSsid())) {
            Log.e(PrinterManager.TAG, "准备搜索打印机");

            if (!isSearching) {
                searchPrinter();
            }
        } else {
            WifiUtil.getInstance()
                    .connect(WifiUtil.getInstance().createWifiInfo(config.getSsid(), config.getPassword(), 3));
        }
    }

    public void preView() {
        ParentQ parentQ = mRootView.getActivity().getIntent().getParcelableExtra(RECORD_ITEM);

        if (parentQ == null) {
            mRootView.showMessage("无效的文件");
            return;
        }

        webViewFactory.get().preView(parentQ);
    }

    private File toImage() {
        File file = webViewFactory.get().toFile();

        if (file != null && file.exists()) {
            return file;
        }

        return null;
    }

    private void hideWaiting() {
        if (waiting != null && waiting.isShowing()) {
            waiting.dismiss();
        }
    }

    private void showWaiting() {
        if (waiting != null && !waiting.isShowing()) {
            waiting.show();
        }
    }

    private Dialog printStatusDialog;
    private File file;

    public void choosePages() {
        boolean needChoosePage =
                mRootView.getActivity().getIntent().getBooleanExtra("needChoosePage", false);

        if (needChoosePage) {
            Windows.singleChoice(activity, "请选择打印份数", new String[]{"1", "2"}, new CommonDialog.OnDialogItemClickListener() {
                @Override
                public void dialogItemClickListener(int position) {
                    if (position == 0) {
                        pages = 1;
                    } else {
                        pages = 2;
                    }

                    checkPrintConfig();
                }
            });
        } else {
            checkPrintConfig();
        }
    }

    private void checkPrintConfig() {
        if (!WifiUtil.getInstance().isOpen()) {
            UIUtils.toast(activity, "正在打开wifi，请稍候重试", Toast.LENGTH_SHORT);
            WifiUtil.getInstance().open();
            return;
        }

        if (chooseWifi == null) {
            UIUtils.toast(activity, "请选择打印机", Toast.LENGTH_SHORT);
            return;
        }

        WifiInfo connectWifiInfo = WifiUtil.getInstance().getConnectInfo();
        if (connectWifiInfo == null || (!connectWifiInfo.getSSID().equalsIgnoreCase("\"" + chooseWifi.getSsid() + "\"")
                && !connectWifiInfo.getSSID().equalsIgnoreCase(chooseWifi.getSsid()))) {
            UIUtils.toast(activity, "未找到打印机的wifi信息，请稍候重试", Toast.LENGTH_SHORT);
            WifiUtil.getInstance()
                    .connect(WifiUtil.getInstance().createWifiInfo(chooseWifi.getSsid(), chooseWifi.getPassword(), 3));
            return;
        }

        if (file == null) {
            webViewFactory.get().configPrintWebView();

            int delay;
            if (Build.VERSION.SDK_INT >= 22) {
                delay = 300;
            } else {
                delay = 500;
            }

            final MyProgressDialog progressDialog = Windows.waiting(activity);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadScreenShot(progressDialog);
                }
            }, delay);
        } else {
            preparePrint();
        }
    }

    private void loadScreenShot(final MyProgressDialog dialog) {
        webViewFactory.get().toFile(new MyWebViewUtil.OnScreenshotListener() {
            @Override
            public void onScreenshot(File capture) {
                dialog.dismiss();
                file = capture;
                preparePrint();
            }
        });
    }

    private void preparePrint() {
        if (file != null && file.exists()) {
            if (printer != null) {
                startPrint(file);
            } else {
                UIUtils.toast(activity, "正在搜索打印机,请稍候重试", Toast.LENGTH_SHORT);
                if (!isSearching) {
                    searchPrinter();
                }
            }
        } else {
            UIUtils.toast(MiuBaseApp.self, "未找到文件", Toast.LENGTH_LONG);
        }
    }

    private void searchPrinter() {
        isSearching = true;
        PrinterManager.getInstance().startSearch(new ISearchPrinterListener() {
            @Override
            public void onSearchBegin() {
                Log.d(PrinterManager.TAG, "onSearchBegin()");
            }

            @Override
            public void onFindPrinter(EpsPrinter epsPrinter) {
                Log.d(PrinterManager.TAG, "onFindPrinter() , " + epsPrinter.getModelName());
                //搜索出来以后必须取消，否则会无法打印
                PrinterManager.getInstance().cancelSearch();

                UIUtils.toast(activity, "已连接到打印机", Toast.LENGTH_SHORT);
                printer = epsPrinter;
                EPSetting.instance().setSelEpsPrinter(epsPrinter);

                if (mRootView != null && mRootView.getActivity() != null && !mRootView.getActivity().isFinishing() && !mRootView.getActivity().isDestroyed()) {
                    mRootView.onCurrentConnectPrinter(chooseWifi);
                }
            }

            @Override
            public void onSearchFinished(int i) {
                Log.d(PrinterManager.TAG, "onSearchFinished() , i = " + i);
                isSearching = false;
                if (printer == null) {
                    UIUtils.toast(activity, "未扫描到打印机，请稍候重试", Toast.LENGTH_SHORT);
                    hideWaiting();
                }
            }
        });
    }

    private void startPrint(File file) {
        waiting = Windows.waiting(activity);

        PrinterManager.getInstance().print(file.getAbsolutePath(), new PrinterManager.IPrintStatusListener() {
            @Override
            public void onPrintBegin() {

            }

            @Override
            public void onPrintSuccess() {
                hideWaiting();
                UIUtils.toast(activity, "打印完成", Toast.LENGTH_SHORT);
                Intent intent = new Intent();
                intent.putExtra("pages", pages);
                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();
            }

            @Override
            public void onPrintStop() {
                hideWaiting();
                UIUtils.toast(activity, "停止打印", Toast.LENGTH_SHORT);
            }

            @Override
            public void onPrintCancel() {
                hideWaiting();
                UIUtils.toast(activity, "取消打印", Toast.LENGTH_SHORT);
            }

            @Override
            public void onPrintPause(EpsStatus status) {
                hideWaiting();

                printStatusDialog = DialogUtil.createStatusDialog(activity, status, new DialogUtil.PrinterActionListener() {
                    @Override
                    public void cancel() {
                        PrinterManager.getInstance().cancelPrint();
                    }

                    @Override
                    public void go() {
                        PrinterManager.getInstance().continuePrint();
                    }
                });

                printStatusDialog.show();
            }

            @Override
            public void onPrintResume() {
                if (printStatusDialog != null && printStatusDialog.isShowing()) {
                    printStatusDialog.dismiss();
                }

                showWaiting();
            }

            @Override
            public void onPrintError() {
                hideWaiting();
                if (!pageIsFinish) {
                    UIUtils.toast(activity, "打印出错，请重试", Toast.LENGTH_SHORT);
                }
                activity.finish();
            }

            @Override
            public void onPrintAutoContinue() {
                if (printStatusDialog != null && printStatusDialog.isShowing()) {
                    printStatusDialog.dismiss();
                }

                showWaiting();
            }

            @Override
            public void invalidFile() {
                UIUtils.toast(MiuBaseApp.self, "无效的打印文件", Toast.LENGTH_SHORT);
                hideWaiting();
            }

            @Override
            public void isPrinterBusy() {
                UIUtils.toast(MiuBaseApp.self, "打印机繁忙，请稍候再试", Toast.LENGTH_SHORT);
                hideWaiting();
            }

            @Override
            public void onCleanTime(int seconds) {

            }

            @Override
            public void onPageFinished(boolean isFinish) {
                if (isFinish) {
                    pageIsFinish = true;
                }
            }
        }, pages);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        if (!isPreView) {
            activity.unregisterReceiver(receiver);
            WifiUtil.getInstance().close();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (commonDialog != null) {
            commonDialog.dismiss();
            commonDialog = null;
        }
    }

    public void release() {
        PrinterManager.getInstance().cancelSearch();
    }

}
