package com.miu30.common.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.Toast;

import com.miu30.common.MiuBaseApp;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author shiner
 */
public class UIUtils {
    public static final int ANIMATION_FADE_IN_TIME = 250;
    public static SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat dateFormateNoSecond = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static Handler uiHandler = new Handler(Looper.getMainLooper());

    public static boolean isGoogleTV(Context context) {
        return context.getPackageManager().hasSystemFeature("com.google.android.tv");
    }

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed
        // behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasJellyBeanMr1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean hasN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isHoneycombTablet(Context context) {
        return hasHoneycomb() && isTablet(context);
    }

    public static void toast(final Context ctx, final int resId, final int duration) {
        toast(ctx, ctx.getString(resId), duration);
    }

    /*public static void toast(final Context ctx, final String resId, final int duration) {

        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            Toast.makeText(ctx, resId, duration).show();
        } else {
            uiHandler.post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(ctx, resId, duration).show();
                }
            });
        }

    }*/
    static Toast MainToast;
    static Toast ItToast;

    public static void toast(final Context ctx, final String resId, final int duration) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            //这里这样是为了避免多次执行这个方法,弹出框叠加
            if (MainToast == null) {
                MainToast = Toast.makeText(ctx, resId, duration);
            } else {
                MainToast.setText(resId);
            }
            MainToast.show();
        } else {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (ItToast == null) {
                        ItToast = Toast.makeText(ctx, resId, duration);
                    } else {
                        ItToast.setText(resId);
                    }
                    ItToast.show();
                }
            });
        }
    }

    public static String toHexColor(int r, int g, int b) {
        return "#" + toBrowserHexValue(r) + toBrowserHexValue(g) + toBrowserHexValue(b);
    }

    private static String toBrowserHexValue(int number) {
        StringBuilder builder = new StringBuilder(Integer.toHexString(number & 0xff));
        while (builder.length() < 2) {
            builder.append("0");
        }
        return builder.toString().toUpperCase();
    }

    public static void safeOpenLink(Context context, Intent linkIntent) {
        try {
            context.startActivity(linkIntent);
        } catch (Exception e) {
            Toast.makeText(context, "不能打开链接", Toast.LENGTH_SHORT).show();
        }
    }

    public static View getIndexView(AbsListView listView, int itemIndex) {
        View v = null;
        try {
            int visiblePosition = listView.getFirstVisiblePosition();
            int targetViewIndex = itemIndex - visiblePosition;
            if (targetViewIndex >= 0 && targetViewIndex < listView.getChildCount()) {
                v = listView.getChildAt(targetViewIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    public static void goMarket(Context ctx, String pkg) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + pkg));
        safeOpenLink(ctx, intent);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void openImage(Context ctx, String fullName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(fullName));
        intent.setDataAndType(uri, "image/*");
        safeOpenLink(ctx, intent);
    }

    public static void openPath(Context ctx, String path) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(Uri.parse(path), "file/*");
        safeOpenLink(ctx, intent);
    }

    public static void hideSoftInput(Context ctx, View paramEditText) {
        ((InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(paramEditText.getWindowToken(), 0);
    }

    public static void showKeyBoard(final View paramEditText) {
        paramEditText.requestFocus();
        paramEditText.post(new Runnable() {
            @Override
            public void run() {
                ((InputMethodManager) paramEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .showSoftInput(paramEditText, 0);
            }
        });
    }

    public static DecimalFormat percentFormatePoint = new DecimalFormat("0.00");
    public static DecimalFormat percentFormate = new DecimalFormat("0");

    public static String showSize(long size) {
        String result = null;
        if (size / 1024 / 1024 > 0) {
            result = percentFormatePoint.format(((double) size) / 1024 / 1024) + "Mb";
        } else if (size / 1024 > 0) {
            result = (size / 1024) + "Kb";
        } else {
            result = "0Kb";
        }
        return result;
    }

    public static String showSize(long size, boolean withPoint) {
        DecimalFormat format = percentFormatePoint;
        if (!withPoint) {
            format = percentFormate;
        }
        String result = null;
        if (size / 1024 / 1024 > 0) {
            result = format.format(((double) size) / 1024 / 1024) + "Mb";
        } else if (size / 1024 > 0) {
            result = (size / 1024) + "Kb";
        } else {
            result = "0Kb";
        }
        return result;
    }

    public static String getMimeType(String fullName) {
        String type = null;
        int filenamePos = fullName.lastIndexOf('/');
        String filename = 0 <= filenamePos ? fullName.substring(filenamePos + 1) : fullName;
        String extension = null;
        if (!filename.isEmpty()) {
            int dotPos = filename.lastIndexOf('.');
            if (0 <= dotPos) {
                extension = filename.substring(dotPos + 1);
            }
        }
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static DecimalFormat distanceFormate = new DecimalFormat("0.0");

    public static String showDistance(double d) {
        if (d < 1000) {
            return (int) d + "米";
        } else {
            String i = distanceFormate.format(d / 1000.0) + "公里";
            return i;
        }
    }

    public static String showDistance(double d, String htmlColor) {
        if (d < 1000) {
            return "<font color='" + htmlColor + "'>" + (int) d + "</font>米";
        } else {
            String i = "<font color='" + htmlColor + "'>" + distanceFormate.format(d / 1000.0) + "</font>公里";
            return i;
        }
    }


    @SuppressLint("NewApi")
    public static boolean copy(String val) {
        boolean suc = true;
        try {
            int sdk = Build.VERSION.SDK_INT;
            if (sdk < Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) MiuBaseApp.self
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(val);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) MiuBaseApp.self
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText(val, val);
                clipboard.setPrimaryClip(clip);
            }
        } catch (Exception e) {
            e.printStackTrace();
            suc = false;
        }
        return suc;
    }

    public static DecimalFormat moneyFormate = new DecimalFormat("0.00");

    public static String delZero(float val) {
        String ret = String.valueOf(val);
        if (ret.endsWith(".0")) {
            return ret.substring(0, ret.length() - 2);
        } else if (ret.endsWith(".00")) {
            return ret.substring(0, ret.length() - 3);
        } else {
            return moneyFormate.format(val);
        }
    }

    public static Drawable getColorStateDrawable(String color_base) {
        if (TextUtils.isEmpty(color_base)) {
            return new ColorDrawable(Color.BLACK);
        }
        if (color_base.startsWith("#")) {
            color_base = color_base.substring(1);
        }
        int color_nor = 0;
        int color_press = 0;
        if (color_base.length() == 6) {
            color_nor = Color.parseColor("#FF" + color_base);
            color_press = Color.parseColor("#AA" + color_base);
        } else if (color_base.length() == 8) {
            color_nor = Color.parseColor(color_base);
            color_press = Color.parseColor("#AA" + color_base.substring(2));
        } else {
            return new ColorDrawable(Color.BLACK);
        }
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = new ColorDrawable(color_nor);
        Drawable pressed = new ColorDrawable(color_press);
        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        bg.addState(new int[]{android.R.attr.state_enabled}, normal);
        bg.addState(new int[]{}, normal);
        return bg;
    }

    public static boolean isMainProcess(Context ctx) {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager mActivityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                processName = appProcess.processName;
                break;
            }
        }
        String packageName = ctx.getPackageName();
        return processName.equals(packageName);
    }

    public static class Patterns {
        public static Pattern NUMBER = Pattern.compile("[0-9]+");
        public static Pattern MOBILE = Pattern.compile("^1(3|5|7|8)[0-9]{9}$");
        public static Pattern CAR_NUM = Pattern.compile("^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_a-z_0-9]{5}$");
    }

    public static String[] listToArray(List<String> list){
        String[] strings = new String[list.size()];
        return list.toArray(strings);
    }

}
