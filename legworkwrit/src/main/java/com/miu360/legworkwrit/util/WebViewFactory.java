package com.miu360.legworkwrit.util;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.webkit.WebView;

import com.miu360.legworkwrit.mvp.model.entity.ParentQ;

import java.io.File;

public class WebViewFactory {
    private WebView mWebView;
    private ParentQ parentQ;
    private final HtmlHandle htmlHandle;

    private WebViewFactory(Activity activity, @IdRes int webViewId) {
        mWebView = MyWebViewUtil.getPreViewWebView(activity, webViewId);
        htmlHandle = new HtmlHandle();
    }

    public static WebViewFactory create(Activity activity, @IdRes int webViewId) {
        return new WebViewFactory(activity, webViewId);
    }

    public void preView(ParentQ parentQ) {
        this.parentQ = parentQ;
        htmlHandle.setParentQ(parentQ);
        mWebView.addJavascriptInterface(htmlHandle, "test");
        mWebView.loadUrl("file://" + parentQ.getLocalPath());
    }

    public File toFile() {
        if (parentQ == null) {
            return null;
        }

        return MyWebViewUtil.getFullWebViewSnapshot(mWebView, parentQ.getFileName());
    }

    public void toFile(MyWebViewUtil.OnScreenshotListener listener) {
        if (parentQ == null) {
            return;
        }

        MyWebViewUtil.getScreenshot(mWebView, parentQ.getFileName(), listener);
    }

    public void configPrintWebView() {
        if (parentQ == null) {
            return;
        }

        mWebView.getSettings().setUseWideViewPort(false);
        mWebView.getSettings().setLoadWithOverviewMode(false);
    }

}
