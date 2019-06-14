package com.miu30.common.util;

import android.app.Activity;
import android.os.Environment;
import android.webkit.WebView;

import java.io.File;
import java.io.IOException;

public class WebToPdfUtil {

    public static void convertWebViewToPdf(Activity context, WebView webView,final ConvertListener convertListener){
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/qh_inspect/");
        final String fileName="writ.pdf";
        File path2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/qh_inspect/"+fileName);
        if(!path2.exists()){
            path2.getParentFile().mkdirs();
            try {
                path2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        PdfView.createWebPrintJob(context, webView, path, fileName, new PdfView.Callback() {

            @Override
            public void success(String path) {
                convertListener.convertSuccess(path);
            }

            @Override
            public void failure() {
                convertListener.convertFailure();
            }
        });
    }

    public interface ConvertListener{
        void convertSuccess(String path);
        void convertFailure();
    }
}
