package com.miu360.legworkwrit.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.epson.isv.eprinterdriver.Common.EpsStatus;
import com.miu30.common.config.Config;
import com.miu30.common.util.MyProgressDialog;
import com.miu30.common.util.UIUtils;
import com.miu30.common.util.Windows;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.UTC;
import com.miu360.legworkwrit.mvp.ui.adapter.MultipleChoiceDialogAdapter;
import com.miu360.legworkwrit.mvp.ui.view.CommonDialog;
import com.miu360.legworkwrit.util.dateUtil.builder.TimePickerBuilder;
import com.miu360.legworkwrit.util.dateUtil.listener.OnTimeSelectListener;
import com.miu360.legworkwrit.util.dateUtil.view.TimePickerView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Murphy on 2018/9/30.
 */
public class DialogUtil {

    public static void selectDialog(Activity activity, String title, final String item1, final String item2, final callBack mCallback) {
        View view = View.inflate(activity, R.layout.layout_dialog, null);
        TextView tv_title = view.findViewById(R.id.tv_title);
        Button btn_punish = view.findViewById(R.id.btn_punish);
        Button btn_warning = view.findViewById(R.id.btn_warning);
        tv_title.setText(title);
        btn_punish.setText(item1);
        btn_warning.setText(item2);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.width = 880;//定义宽度
        lp.height = 420;//定义高度
        alertDialog.getWindow().setAttributes(lp);

        btn_punish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.returnResult(item1);
                alertDialog.dismiss();
            }
        });
        btn_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.returnResult(item2);
                alertDialog.dismiss();
            }
        });
    }

    public interface callBack {
        void returnResult(String result);
    }

    public interface dateCallBack {
        void returnDate(Date date);
    }

    /**
     * 无内容的CommonDialog
     *
     * @param context
     * @param title
     * @param okText
     * @param onOkClickListener
     * @param cancelText
     * @param onCancelClickListener
     * @return
     */
    public static CommonDialog showCommonDialogNoContent(Context context, String title, String okText, View.OnClickListener onOkClickListener, String cancelText, View.OnClickListener onCancelClickListener) {
        CommonDialog commonDialog = new CommonDialog(context, title, okText, onOkClickListener, cancelText, onCancelClickListener, true);
        commonDialog.show();
        return commonDialog;
    }

    public static void TimePicker(final Activity activity, final String title, final Calendar calendar, final dateCallBack mCallBack) {
        View view = activity.getCurrentFocus();
        if (view != null) {//隐藏软键盘
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        if (Build.VERSION.SDK_INT < 21) {
            final MyProgressDialog pd = Windows.waiting(activity);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pd.dismiss();
                }
            }, 500);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showTimeView(activity, title, calendar, mCallBack);
                }
            }, 100);
        } else {
            showTimeView(activity, title, calendar, mCallBack);
        }

    }

    private static void showTimeView(Activity activity, String title, Calendar calendar, final dateCallBack mCallBack) {
        if (activity == null) {
            return;
        }
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        Calendar startc = Calendar.getInstance();
        Calendar endc = Calendar.getInstance();
        startc.set(Calendar.YEAR, 2010);
        endc.add(Calendar.YEAR, 2);
        if (calendar.getTime().getTime() < startc.getTime().getTime()) {
            startc = calendar;
            startc.add(Calendar.YEAR, -2);
        } else if (calendar.getTime().getTime() > endc.getTime().getTime()) {
            endc = calendar;
            endc.add(Calendar.YEAR, 2);
        }

        TimePickerView pvTime = new TimePickerBuilder(activity, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mCallBack.returnDate(date);
            }
        })
                .setTitleText(title)
                .setDate(calendar)
                .setRangDate(startc, endc)
                .setLineSpacingMultiplier(3.5f)
                .setTitleBgColor(activity.getResources().getColor(R.color.white))
                .setCancelColor(activity.getResources().getColor(R.color.color999))
                .setSubmitColor(activity.getResources().getColor(R.color.light_blue))
                .setDividerColor(activity.getResources().getColor(R.color.lucency))
                .setType(new boolean[]{true, true, true, true, true, false})
                .build();
        pvTime.show();
    }

    public interface MultipleChoiceCallback {
        void multipleChoice(List<String> choices);
    }

    public static void showMultipleChoiceDialog(final Context context, String title, String[] items, List<String> list, final MultipleChoiceCallback callback) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_multiple_choice_dialog, null, false);

        TextView tvTitle = view.findViewById(R.id.tv_title);
        ListView lvContent = view.findViewById(R.id.lv_content);

        final MultipleChoiceDialogAdapter adapter = new MultipleChoiceDialogAdapter(context, items, list);
        tvTitle.setText(title);
        lvContent.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.setView(view).create();

        view.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getChoiceItems().size() <= 0) {
                    UIUtils.toast(context, "请至少选择一项", Toast.LENGTH_SHORT);
                } else {
                    if (callback != null) {
                        callback.multipleChoice(adapter.getChoiceItems());
                    }

                    dialog.dismiss();
                }
            }
        });

        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static Dialog showUploadDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.UploadDialog);
        dialog.setContentView(R.layout.layout_upload_dialog);
        dialog.show();
        return dialog;
    }

    public static void showTipDialog(Context context, String content) {
        final Dialog dialog = new Dialog(context, R.style.TimeLineDialog);
        dialog.setContentView(R.layout.layout_tip_dialog);

        TextView tvContent = dialog.findViewById(R.id.tv_content);
        tvContent.setText(content);

        dialog.findViewById(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void showPhotoDialog(Context context, String url) {
        final Dialog dialog = new Dialog(context, R.style.TimeLineDialog);
        dialog.setContentView(R.layout.layout_photo_dialog);
        ImageView ivPreview = dialog.findViewById(R.id.imageView);
        LinearLayout ll = dialog.findViewById(R.id.ll_show);
        Glide.with(context).load(url).into(ivPreview);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //-------------------------------------- 时间轴弹窗相关 --------------------------------------

    public interface OnNoCaseListener {
        void noCase();
    }

    public static void showTimeLineDialog(Context context, @NonNull String type, OnNoCaseListener listener) {
        Case c = CacheManager.getInstance().getCase();

        if (c == null) {
            listener.noCase();
            return;
        }

        final Dialog dialog = new Dialog(context, R.style.TimeLineDialog);
        dialog.setContentView(R.layout.layout_timeline);
        addContent(c, dialog, type);
        dialog.findViewById(R.id.tv_i_know).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @SuppressLint("InflateParams")
    private static void addContent(Case c, Dialog dialog, String type) {
        FrameLayout flContainer = dialog.findViewById(R.id.fl_container);
        LayoutInflater inflater = LayoutInflater.from(dialog.getContext());
        View contentView;

        if ("警告".equals(c.getCFFS())) {
            //简易流程
            contentView = getSimpleProcessContentView(type, inflater);
        } else {
            //正常流程
            contentView = getNormalProcessContentView(type, inflater);
        }

        flContainer.addView(contentView);
    }

    private static View getSimpleProcessContentView(String type, LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.include_timeline_administrative, null, false);

        setCurrentViewStyle(view, type);
        setCurrentViewData(view);

        return view;
    }

    @SuppressLint("InflateParams")
    private static View getNormalProcessContentView(String type, LayoutInflater inflater) {
        View view;

        int i = CacheManager.getInstance().getChooseInstrumentType();

        if (i == 1) {
            view = inflater.inflate(R.layout.include_timeline_frist_register, null, false);
        } else if (i == 2) {
            view = inflater.inflate(R.layout.include_timeline_live, null, false);
        } else {
            view = inflater.inflate(R.layout.include_timeline_talk_notice, null, false);
        }

        setCurrentViewStyle(view, type);
        setCurrentViewData(view);
        return view;
    }

    private static void setCurrentViewData(View view) {
        if (CacheManager.getInstance().getUTC(Config.T_FRISTREGISTER) != null) {
            setTextViewText(view, Config.T_FRISTREGISTER, CacheManager.getInstance().getUTC(Config.T_FRISTREGISTER));
        }

        if (CacheManager.getInstance().getUTC(Config.T_TALKNOTICE) != null) {
            setTextViewText(view, Config.T_TALKNOTICE, CacheManager.getInstance().getUTC(Config.T_TALKNOTICE));
        }

        if (CacheManager.getInstance().getUTC(Config.T_CARDECIDE) != null) {
            setTextViewText(view, Config.T_CARDECIDE, CacheManager.getInstance().getUTC(Config.T_CARDECIDE));
        }

        if (CacheManager.getInstance().getUTC(Config.T_LIVETRANSCRIPT) != null) {
            setTextViewText(view, Config.T_LIVETRANSCRIPT, CacheManager.getInstance().getUTC(Config.T_LIVETRANSCRIPT));
        }

        if (CacheManager.getInstance().getUTC(Config.T_LIVERECORD) != null) {
            setTextViewText(view, Config.T_LIVERECORD, CacheManager.getInstance().getUTC(Config.T_LIVERECORD));
        }

        if (CacheManager.getInstance().getUTC(Config.T_CARFORM) != null) {
            setTextViewText(view, Config.T_CARFORM, CacheManager.getInstance().getUTC(Config.T_CARFORM));
        }

        if (CacheManager.getInstance().getUTC(Config.T_ADMINISTRATIVE) != null) {
            setTextViewText(view, Config.T_ADMINISTRATIVE, CacheManager.getInstance().getUTC(Config.T_ADMINISTRATIVE));
        }

        if (CacheManager.getInstance().getUTC(Config.T_ZPDZ) != null) {
            setTextViewText(view, Config.T_ZPDZ, CacheManager.getInstance().getUTC(Config.T_ZPDZ));
        }
    }

    private static void setTextViewText(View rootView, String type, UTC utc) {
        if (utc != null) {
            for (int i = 0; i < 4; i++) {
                View itemView = rootView.findViewWithTag(type + "_" + i);
                if (itemView != null && itemView instanceof TextView) {
                    if (i == 0) {
                        if ((!TextUtils.isEmpty(utc.getStartUTC()) && !"无".equals(utc.getStartUTC())) && (!TextUtils.isEmpty(utc.getFlag()) && !"2".equals(utc.getFlag()))) {
                            ((TextView) itemView).setText(utc.getStartUTC());
                        }
                    } else {
                        if ((!TextUtils.isEmpty(utc.getEndUTC()) && !"无".equals(utc.getEndUTC())) && (!TextUtils.isEmpty(utc.getFlag()) && !"2".equals(utc.getFlag()))) {
                            ((TextView) itemView).setText(utc.getEndUTC());
                        }
                    }
                }
            }
        }
    }

    private static void setCurrentViewStyle(View rootView, String type) {
        for (int i = 0; i < 4; i++) {
            View itemView = rootView.findViewWithTag(type + "_" + i);

            if (itemView != null) {
                if (itemView instanceof TextView) {
                    ((TextView) itemView).setTextColor(Color.RED);
                } else {
                    itemView.setBackgroundColor(Color.RED);
                }
            }
        }
    }

    //------------------------------------------------------------------------------------------

    //-------------------------------------- 打印机弹窗相关 ---------------------------------------

    public interface PrinterActionListener {
        void cancel();

        void go();
    }

    public static Dialog createStatusDialog(Context context, EpsStatus status, final PrinterActionListener listener) {
        String message = status.toString();
        final boolean continueable = status.isJobContinue();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(false);

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.cancel();
                }
            }
        });

        if (continueable) {
            builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.go();
                    }
                }
            });
        }

        return builder.create();
    }

    //-------------------------------------------------------------------------------------------

}
