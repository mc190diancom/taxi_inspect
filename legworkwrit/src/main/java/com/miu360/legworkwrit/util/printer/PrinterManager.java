package com.miu360.legworkwrit.util.printer;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.epson.isv.eprinterdriver.Common.EpsStatus;
import com.epson.isv.eprinterdriver.Common.ServiceIntent;
import com.epson.isv.eprinterdriver.Ctrl.EPSetting;
import com.epson.isv.eprinterdriver.Ctrl.EPrintManager;
import com.epson.isv.eprinterdriver.Ctrl.IPrintListener;
import com.epson.isv.eprinterdriver.Ctrl.ISearchPrinterListener;

/**
 * 作者：wanglei on 2018/10/21.
 * 邮箱：forwlwork@gmail.com
 */
@SuppressLint("LogNotTimber")
public class PrinterManager {
    public static final String TAG = "PrinterManager";

    static {
        System.loadLibrary("jpeg-9b");
    }

    private EPrintManager manager;

    private PrinterManager() {
        manager = EPrintManager.instance();
    }

    private static class Holder {
        private static final PrinterManager INSTANCE = new PrinterManager();
    }

    public static PrinterManager getInstance() {
        return Holder.INSTANCE;
    }

    public void startSearch(ISearchPrinterListener listener) {
        Log.d(TAG, "start search ");
        manager.addSearchListener(listener);
        manager.findPrinter(30);
    }

    public void cancelSearch() {
        manager.cancelFindPrinter();
        Log.d(TAG, "cancel search.");
    }

    public void print(final String imagePath, @NonNull final IPrintStatusListener listener, int pages) {
        if (manager.isPrintBusy()) {
            listener.isPrinterBusy();
            return;
        }

        new PrinterSetting().configDefaultSetting(pages);

        manager.addPrintListener(new IPrintListener() {
            @Override
            public void onPrintBegin() {
                Log.d(TAG, "onPrintBegin()");
                System.gc();
                listener.onPrintBegin();
            }

            @Override
            public String onPageBegin(int pageNum) {
                Log.d(TAG, "onPrintBegin:" + pageNum);
                System.gc();
                return imagePath;
            }

            @Override
            public boolean onPageFinished(int finishedNum) {
                Log.d(TAG, "onPageFinished() , i = " + finishedNum);
                int totalPageNum = EPSetting.instance().getTotalPages();
                listener.onPageFinished(finishedNum <= totalPageNum);
                return finishedNum <= totalPageNum;
            }

            @Override
            public void onPrintFinished(int factor) {
                Log.d(TAG, "onPrintFinished() , i = " + factor);
                System.gc();
                switch (factor) {
                    case ServiceIntent.StopFactor.PrintSuccess:
                        listener.onPrintSuccess();
                        break;
                    case ServiceIntent.StopFactor.PrinterStopButton:
                        listener.onPrintStop();
                        break;
                    case ServiceIntent.StopFactor.UserCancel:
                        listener.onPrintCancel();
                        break;
                    default:
                        listener.onPrintError();
                        break;
                }
            }

            @Override
            public void onPrintPause(int i, int i1, EpsStatus epsStatus) {
                Log.d(TAG, "onPrintPause() , i = " + i + " , i1 = " + i1);
                listener.onPrintPause(epsStatus);
            }

            @Override
            public void onPrintResume() {
                Log.d(TAG, "onPrintResume()");
                listener.onPrintResume();
            }

            @Override
            public void onPrintAutoContinue() {
                Log.d(TAG, "onPrintAutoContinue()");
                listener.onPrintAutoContinue();
            }

            @Override
            public void onCleaningTime(int i) {
                Log.d(TAG, "onCleaningTime() , i = " + i);
                listener.onCleanTime(i);
            }
        });

        Log.d(TAG, "path = " + imagePath);
        int ret = manager.startPrint();
        Log.d(TAG, "ret = " + ret);
    }

    public void cancelPrint() {
        if (manager.isPrintBusy()) {
            manager.cancelPrint();
        }
    }

    public void continuePrint() {
        manager.continuePrint();
    }

    private EPrintManager.EPRINT_FILETYPE getFiletype(String filename) {
        if (TextUtils.isEmpty(filename)) {
            return null;
        }
        if (filename.toLowerCase().endsWith("bmp")) {
            return EPrintManager.EPRINT_FILETYPE.BMP;
        }

        return EPrintManager.EPRINT_FILETYPE.JPEG;
    }

    public interface IPrintStatusListener {
        /*
         * 开始打印
         */
        void onPrintBegin();

        /*
         * 打印成功
         */
        void onPrintSuccess();

        /*
         * 打印停止
         */
        void onPrintStop();

        /*
         * 打印取消
         */
        void onPrintCancel();

        /*
         * 打印暂停
         */
        void onPrintPause(EpsStatus status);

        /*
         * 正在打印
         */
        void onPrintResume();

        /*
         * 打印出错
         */
        void onPrintError();

        /*
         * 清洁时间
         */
        void onCleanTime(int seconds);

        void onPageFinished(boolean isFinish);

        void onPrintAutoContinue();

        /*
         * 无效的打印文件
         */
        void invalidFile();

        /*
         * 打印机繁忙
         */
        void isPrinterBusy();
    }

}
