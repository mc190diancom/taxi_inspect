package com.miu360.legworkwrit.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.jess.arms.di.component.AppComponent;
import com.miu30.common.base.BaseMvpActivity;
import com.miu30.common.config.Config;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.di.component.DaggerCaseSignComponent;
import com.miu360.legworkwrit.di.module.CaseSignModule;
import com.miu360.legworkwrit.mvp.contract.CaseSignContract;
import com.miu360.legworkwrit.mvp.model.entity.LiveCheckRecordQ;
import com.miu360.legworkwrit.mvp.model.entity.ParentQ;
import com.miu360.legworkwrit.mvp.presenter.CaseSignPresenter;
import com.miu360.legworkwrit.mvp.presenter.WebViewActivityPresenter;
import com.miu360.legworkwrit.mvp.ui.adapter.GridViewAdapter;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;
import com.miu360.legworkwrit.util.SignUtil;
import com.miu360.legworkwrit.util.TimeTool;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


public class CaseSignActivity extends BaseMvpActivity<CaseSignPresenter> implements CaseSignContract.View {
    @BindView(R2.id.wv_document)
    WebView wvDocument;
    @BindView(R2.id.gv_sign)
    GridView gvSign;
    @BindView(R2.id.gv_signed)
    GridView gvSigned;

    @Inject
    HeaderHolder header;

    GridViewAdapter gridViewAdapter,gridViewAdapter2;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCaseSignComponent
                .builder()
                .appComponent(appComponent)
                .caseSignModule(new CaseSignModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_case_sign;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        header.init(self,"签字预览");

        final LiveCheckRecordQ liveCheckRecordQ = getIntent().getParcelableExtra("record_item");
        SignUtil.getInstance(self).initApi(liveCheckRecordQ.getLocalPath());

        final List<String> mList = new ArrayList<>();
        mList.add("被检查人");
        final  List<String> mList2 = new ArrayList<>();
        mList2.add("执法人员1");
        mList2.add("执法人员2");
        gridViewAdapter = new GridViewAdapter(mList,self);
        gridViewAdapter2 = new GridViewAdapter(mList2,self);
        gvSign.setAdapter(gridViewAdapter);
        gvSign.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               if("被检查人".equals(mList.get(position))){
                   SignUtil.getInstance(self).setSignObject(liveCheckRecordQ.getBJCR(),TextUtils.isEmpty(liveCheckRecordQ.getSFZH()) ? liveCheckRecordQ.getCYZGZ() : liveCheckRecordQ.getSFZH());
                   SignUtil.getInstance(self).startSignActivity();
               }
            }
        });
        gvSigned.setAdapter(gridViewAdapter2);
        mPresenter.init(self);

       // mPresenter.uploadSignFile(liveCheckRecordQ.getID());
    }

    /*
     * 现场检查笔录(路检)
     */
    public static Intent getIntent(Activity activity, LiveCheckRecordQ liveCheckRecordQ, boolean isPreview) {
        Intent intent = new Intent(activity, CaseSignActivity.class);
        liveCheckRecordQ.setLocalPath(Config.PATHROOT + "/Android/data/" + activity.getPackageName() + "/files/" + Config.LIVERECORD + ".html");
        liveCheckRecordQ.setFileName(Config.LIVERECORD + ".jpg");
        if (!TextUtils.isEmpty(liveCheckRecordQ.getBJCSJ()) && liveCheckRecordQ.getBJCSJ().length() > 13) {
            liveCheckRecordQ.setBJCSJ(TimeTool.parseStr(liveCheckRecordQ.getBJCSJ()));
        }
        if (!TextUtils.isEmpty(liveCheckRecordQ.getJCSJ1()) && liveCheckRecordQ.getJCSJ1().length() > 13) {
            liveCheckRecordQ.setJCSJ1(TimeTool.parseStr(liveCheckRecordQ.getJCSJ1()));
        }
        if (!TextUtils.isEmpty(liveCheckRecordQ.getJCSJ2()) && liveCheckRecordQ.getJCSJ2().length() > 13) {
            liveCheckRecordQ.setJCSJ2(TimeTool.parseStr(liveCheckRecordQ.getJCSJ2()));
        }
        if (!TextUtils.isEmpty(liveCheckRecordQ.getZFSJ()) && liveCheckRecordQ.getZFSJ().length() > 13) {
            liveCheckRecordQ.setZFSJ(TimeTool.parseStr(liveCheckRecordQ.getZFSJ()));
        }
        intent.putExtra(WebViewActivityPresenter.RECORD_ITEM, liveCheckRecordQ);
        intent.putExtra("isPreview", isPreview);
        intent.putExtra("needChoosePage", false);
        return intent;
    }

    @Override
    public Activity getActivity() {
        return self;
    }
}
