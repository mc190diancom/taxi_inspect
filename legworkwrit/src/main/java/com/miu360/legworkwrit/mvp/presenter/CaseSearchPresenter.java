package com.miu360.legworkwrit.mvp.presenter;

import android.app.Activity;
import android.widget.TextView;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.miu30.common.util.CommonDialog;
import com.miu30.common.util.Windows;
import com.miu360.legworkwrit.mvp.contract.CaseSearchContract;
import com.miu360.legworkwrit.util.DialogUtil;
import com.miu360.legworkwrit.util.TimeTool;

import java.util.Date;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class CaseSearchPresenter extends BasePresenter<CaseSearchContract.Model, CaseSearchContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    private Activity activity;

    private String[] allIndustryCategory = {"巡游车", "网约车", "非法经营出租汽车", "全部"};

    @Inject
    public CaseSearchPresenter(CaseSearchContract.Model model, CaseSearchContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }

    public void chooseIndustryCategory(final TextView textView) {
        Windows.singleChoice(activity, "请选择行业类别", allIndustryCategory, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                textView.setText(allIndustryCategory[position]);
            }
        });
    }

    public void init(Activity self) {
        this.activity = self;
    }

    public void chooseStartTime(final TextView textView) {
        DialogUtil.TimePicker(activity, "请选择开始时间", null, new DialogUtil.dateCallBack() {
            @Override
            public void returnDate(Date date) {
                textView.setText(TimeTool.yyyyMMdd_HHmm.format(date));
            }
        });
    }

    public void chooseEndTime(final TextView textView) {
        DialogUtil.TimePicker(activity, "请选择结束时间", null, new DialogUtil.dateCallBack() {
            @Override
            public void returnDate(Date date) {
                textView.setText(TimeTool.yyyyMMdd_HHmm.format(date));
            }
        });
    }
}
