package com.miu30.common.app;

import com.jess.arms.http.log.DefaultFormatPrinter;

import java.util.List;

import okhttp3.MediaType;
import timber.log.Timber;

/**
 * 这里只用于网络请求的日志输出和打印。如要制定特殊格式，请重写方法或 自己实现FormatPrinter
 */
public class MyDefaultFormatPrinter extends DefaultFormatPrinter {
    private static final String TAG = "MyDefaultFormatPrinter";

    @Override
    public void printJsonResponse(long chainMs, boolean isSuccessful, int code, String headers, MediaType contentType, String bodyString, List<String> segments, String message, String responseUrl) {

        Timber.tag("response").i(bodyString);
        super.printJsonResponse(chainMs, isSuccessful, code, headers, contentType, bodyString, segments, message, responseUrl);
    }
}
