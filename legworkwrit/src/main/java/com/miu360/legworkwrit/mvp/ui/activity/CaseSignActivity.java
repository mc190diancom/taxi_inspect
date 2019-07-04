package com.miu360.legworkwrit.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.arms.di.component.AppComponent;
import com.miu30.common.base.BaseMvpActivity;
import com.miu30.common.config.Config;
import com.miu30.common.util.UIUtils;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.di.component.DaggerCaseSignComponent;
import com.miu360.legworkwrit.di.module.CaseSignModule;
import com.miu360.legworkwrit.mvp.contract.CaseSignContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.AdministrativePenalty;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarDecideQ;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarFormQ;
import com.miu360.legworkwrit.mvp.model.entity.FristRegisterQ;
import com.miu360.legworkwrit.mvp.model.entity.LiveCheckRecordQ;
import com.miu360.legworkwrit.mvp.model.entity.LiveTranscript;
import com.miu360.legworkwrit.mvp.model.entity.ParentQ;
import com.miu360.legworkwrit.mvp.model.entity.TalkNoticeQ;
import com.miu360.legworkwrit.mvp.presenter.CaseSignPresenter;
import com.miu360.legworkwrit.mvp.presenter.WebViewActivityPresenter;
import com.miu360.legworkwrit.mvp.ui.adapter.GridViewAdapter;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;
import com.miu360.legworkwrit.util.SignUtil;
import com.miu360.legworkwrit.util.TimeTool;
import javax.inject.Inject;

import butterknife.BindView;

public class CaseSignActivity extends BaseMvpActivity<CaseSignPresenter> implements CaseSignContract.View{
    @BindView(R2.id.wv_document)
    WebView wvDocument;
    @BindView(R2.id.gv_sign)
    GridView gvSign;
    @BindView(R2.id.gv_signed)
    GridView gvSigned;
    @BindView(R2.id.ll_sign)
    LinearLayout llSign;
    @BindView(R2.id.view)
    View view;
    @BindView(R2.id.ll_signed)
    LinearLayout llSigned;
    @BindView(R2.id.tv_tip)
    TextView tvTip;

    @Inject
    HeaderHolder header;

    GridViewAdapter gridViewAdapter, gridViewAdapter2;
    private String type;//用于区分文书类型
    private String sfzh;//身份证号
    private ParentQ parentQ;

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
        header.init(self, "签字预览");
        type = getIntent().getStringExtra("type");
        assert mPresenter != null;
        mPresenter.init(self,wvDocument);
        sfzh = CacheManager.getInstance().getSFZH();
        parentQ = getIntent().getParcelableExtra("record_item");
        header.setUpRightTextBtn("文书打印", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.Sign();
                //mPresenter.uploadImage();
            }
        });
        initAdapter();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.initSignParam(parentQ,type,sfzh);
            }
        },300);

    }

    /**
     * 初始化适配器相关
     */
    private void initAdapter() {
        assert mPresenter != null;
        gridViewAdapter = mPresenter.getGVAdapter(type);
        gvSign.setAdapter(gridViewAdapter);
        setAdapterListner(gvSign);

        gridViewAdapter2 = mPresenter.getGV2Adapter();
        setAdapterListner(gvSigned);
        gvSigned.setAdapter(gridViewAdapter2);

        if(TextUtils.isEmpty(sfzh)){
            llSign.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            tvTip.setVisibility(View.VISIBLE);
        }
    }

    private void setAdapterListner(GridView gridView) {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("被检查人".equals(mPresenter.getList().get(position))) {
                    SignUtil.getInstance(self).startSign();
                }else if("情况属实".equals(mPresenter.getList().get(position))){
                    SignUtil.getInstance(self).setNotation2();
                }
            }
        });
    }

    @Override
    public Activity getActivity() {
        return self;
    }

    @Override
    public void uploadSuccess(boolean Result) {
        if(!Result){
            UIUtils.toast(self,"数据包上传失败", Toast.LENGTH_SHORT);
            return;
        }
        Intent intent = null;
        switch (type){
            case Config.LIVERECORD:
                intent = WebViewActivity.getIntent(self, (LiveCheckRecordQ) parentQ, false);
                break;
            case Config.ADMINISTRATIVE:
                intent = WebViewActivity.getIntent(self, (AdministrativePenalty) parentQ, false);
                break;
            case Config.TALKNOTICE:
                intent = WebViewActivity.getIntent(self, (TalkNoticeQ) parentQ, false);
                break;
            case Config.FRISTREGISTER:
                intent = WebViewActivity.getIntent(self, (FristRegisterQ) parentQ, false);
                break;
            case Config.CARFORM:
                intent = WebViewActivity.getIntent(self, (DetainCarFormQ) parentQ, false);
                break;
            case Config.CARDECIDE:
                intent = WebViewActivity.getIntent(self, (DetainCarDecideQ) parentQ, false);
                break;
            case Config.LIVETRANSCRIPT:
                intent = WebViewActivity.getIntent(self, (LiveTranscript) parentQ, false);
                break;
        }
        startActivityForResult(intent,0x0001);
    }

    @Override
    public void signResult(int signResult) {
        if(signResult == 3){
            llSign.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }else{
            gridViewAdapter.notifyDataSetChanged();
        }
        gridViewAdapter2.notifyDataSetChanged();
    }

    @Override
    public void finish() {
        SignUtil.getInstance(self).release();
        super.finish();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
        intent.putExtra("type",Config.LIVERECORD);
        return intent;
    }

    /*
     * 行政处罚决定书
     */
    public static Intent getIntent(Activity activity, AdministrativePenalty administrativePenalty, boolean isPreview) {
        Intent intent = new Intent(activity, CaseSignActivity.class);
        administrativePenalty.setLocalPath(Config.PATHROOT + "/Android/data/" + activity.getPackageName() + "/files/" + Config.ADMINISTRATIVE + ".html");
        administrativePenalty.setFileName(Config.ADMINISTRATIVE + ".jpg");
        if (!TextUtils.isEmpty(administrativePenalty.getBCSJ()) && administrativePenalty.getBCSJ().length() > 13) {
            administrativePenalty.setBCSJ(TimeTool.parseStr(administrativePenalty.getBCSJ()));
        }
        if (!TextUtils.isEmpty(administrativePenalty.getSDSJ()) && administrativePenalty.getSDSJ().length() > 13) {
            administrativePenalty.setSDSJ(TimeTool.parseStr(administrativePenalty.getSDSJ()));
        }
        if (!TextUtils.isEmpty(administrativePenalty.getQSSJ()) && administrativePenalty.getQSSJ().length() > 13) {
            administrativePenalty.setQSSJ(TimeTool.parseStr(administrativePenalty.getQSSJ()));
        }
        if (!TextUtils.isEmpty(administrativePenalty.getXZJGSJ()) && administrativePenalty.getXZJGSJ().length() > 13) {
            administrativePenalty.setXZJGSJ(TimeTool.parseStr(administrativePenalty.getXZJGSJ()));
        }
        intent.putExtra(WebViewActivityPresenter.RECORD_ITEM, administrativePenalty);
        intent.putExtra("type",Config.ADMINISTRATIVE);
        return intent;
    }

    /*
     * 先行登记通知书
     */
    public static Intent getIntent(Activity activity, FristRegisterQ fristRegisterQ, boolean isPreview) {
        Intent intent = new Intent(activity, CaseSignActivity.class);
        fristRegisterQ.setLocalPath(Config.PATHROOT + "/Android/data/" + activity.getPackageName() + "/files/" + Config.FRISTREGISTER + ".html");
        fristRegisterQ.setFileName(Config.FRISTREGISTER + ".jpg");
        if (!TextUtils.isEmpty(fristRegisterQ.getJDSJ()) && fristRegisterQ.getJDSJ().length() > 13) {
            fristRegisterQ.setJDSJ(TimeTool.parseStr(fristRegisterQ.getJDSJ()));
        }
        if (!TextUtils.isEmpty(fristRegisterQ.getXZJGSJ()) && fristRegisterQ.getXZJGSJ().length() > 13) {
            fristRegisterQ.setXZJGSJ(TimeTool.parseStr(fristRegisterQ.getXZJGSJ()));
        }
        if (!TextUtils.isEmpty(fristRegisterQ.getQSSJ()) && fristRegisterQ.getQSSJ().length() > 13) {
            fristRegisterQ.setQSSJ(TimeTool.parseStr(fristRegisterQ.getQSSJ()));
        }
        if (!TextUtils.isEmpty(fristRegisterQ.getZFSJ()) && fristRegisterQ.getZFSJ().length() > 13) {
            fristRegisterQ.setZFSJ(TimeTool.parseStr(fristRegisterQ.getZFSJ()));
        }
        intent.putExtra(WebViewActivityPresenter.RECORD_ITEM, fristRegisterQ);
        intent.putExtra("type",Config.FRISTREGISTER);
        return intent;
    }

    /*
     * 扣押车辆决定书
     */
    public static Intent getIntent(Activity activity, DetainCarDecideQ detainCarDecideQ, boolean isPreview) {
        Intent intent = new Intent(activity, CaseSignActivity.class);
        detainCarDecideQ.setLocalPath(Config.PATHROOT + "/Android/data/" + activity.getPackageName() + "/files/" + Config.CARDECIDE + ".html");
        detainCarDecideQ.setFileName(Config.CARDECIDE + ".jpg");
        if (!TextUtils.isEmpty(detainCarDecideQ.getJDSJ()) && detainCarDecideQ.getJDSJ().length() > 13) {
            detainCarDecideQ.setJDSJ(TimeTool.parseStr(detainCarDecideQ.getJDSJ()));
        }
        if (!TextUtils.isEmpty(detainCarDecideQ.getZFSJ()) && detainCarDecideQ.getZFSJ().length() > 13) {
            detainCarDecideQ.setZFSJ(TimeTool.parseStr(detainCarDecideQ.getZFSJ()));
        }
        if (!TextUtils.isEmpty(detainCarDecideQ.getXZJGSJ()) && detainCarDecideQ.getXZJGSJ().length() > 13) {
            detainCarDecideQ.setXZJGSJ(TimeTool.parseStr(detainCarDecideQ.getXZJGSJ()));
        }
        if (!TextUtils.isEmpty(detainCarDecideQ.getQSSJ()) && detainCarDecideQ.getQSSJ().length() > 13) {
            detainCarDecideQ.setQSSJ(TimeTool.parseStr(detainCarDecideQ.getQSSJ()));
        }
        intent.putExtra(WebViewActivityPresenter.RECORD_ITEM, detainCarDecideQ);
        intent.putExtra("type",Config.CARDECIDE);
        return intent;
    }

    /*
     * 现场笔录
     */
    public static Intent getIntent(Activity activity, LiveTranscript liveTranscript, boolean isPreview) {
        Intent intent = new Intent(activity, CaseSignActivity.class);
        liveTranscript.setLocalPath(Config.PATHROOT + "/Android/data/" + activity.getPackageName() + "/files/" + Config.LIVETRANSCRIPT + ".html");
        liveTranscript.setFileName(Config.LIVETRANSCRIPT + ".jpg");
        if (!TextUtils.isEmpty(liveTranscript.getSSSJ1()) && liveTranscript.getSSSJ1().length() > 13) {
            liveTranscript.setSSSJ1(TimeTool.parseStr(liveTranscript.getSSSJ1()));
        }
        if (!TextUtils.isEmpty(liveTranscript.getSSSJ2()) && liveTranscript.getSSSJ2().length() > 13) {
            liveTranscript.setSSSJ2(TimeTool.parseStr(liveTranscript.getSSSJ2()));
        }
        if (!TextUtils.isEmpty(liveTranscript.getDSRQZSJ()) && liveTranscript.getDSRQZSJ().length() > 13) {
            liveTranscript.setDSRQZSJ(TimeTool.parseStr(liveTranscript.getDSRQZSJ()));
        }
        if (!TextUtils.isEmpty(liveTranscript.getZFSJ()) && liveTranscript.getZFSJ().length() > 13) {
            liveTranscript.setZFSJ(TimeTool.parseStr(liveTranscript.getZFSJ()));
        }
        intent.putExtra(WebViewActivityPresenter.RECORD_ITEM, liveTranscript);
        intent.putExtra("type",Config.LIVETRANSCRIPT);
        return intent;
    }

    /*
     * 谈话通知书
     */
    public static Intent getIntent(Activity activity, TalkNoticeQ talkNoticeQ, boolean isPreview) {
        Intent intent = new Intent(activity, CaseSignActivity.class);
        talkNoticeQ.setLocalPath(Config.PATHROOT + "/Android/data/" + activity.getPackageName() + "/files/" + Config.TALKNOTICE + ".html");
        talkNoticeQ.setFileName(Config.TALKNOTICE + ".jpg");
        if (!TextUtils.isEmpty(talkNoticeQ.getXZJGSJ()) && talkNoticeQ.getXZJGSJ().length() > 13) {
            talkNoticeQ.setXZJGSJ(TimeTool.parseStr(talkNoticeQ.getXZJGSJ()));
        }
        if (!TextUtils.isEmpty(talkNoticeQ.getQSSJ()) && talkNoticeQ.getQSSJ().length() > 13) {
            talkNoticeQ.setQSSJ(TimeTool.parseStr(talkNoticeQ.getQSSJ()));
        }
        if (!TextUtils.isEmpty(talkNoticeQ.getSDSJ()) && talkNoticeQ.getSDSJ().length() > 13) {
            talkNoticeQ.setSDSJ(TimeTool.parseStr(talkNoticeQ.getSDSJ()));
        }
        intent.putExtra(WebViewActivityPresenter.RECORD_ITEM, talkNoticeQ);
        intent.putExtra("type",Config.TALKNOTICE);
        return intent;
    }

    /*
     * 车辆扣押交接单
     */
    public static Intent getIntent(Activity activity, DetainCarFormQ detainCarFormQ, boolean isPreview) {
        Intent intent = new Intent(activity, CaseSignActivity.class);
        detainCarFormQ.setLocalPath(Config.PATHROOT + "/Android/data/" + activity.getPackageName() + "/files/" + Config.CARFORM + ".html");
        detainCarFormQ.setFileName(Config.CARFORM + ".jpg");
        if (!TextUtils.isEmpty(detainCarFormQ.getKCSJ()) && detainCarFormQ.getKCSJ().length() > 13) {
            detainCarFormQ.setKCSJ(TimeTool.parseStr(detainCarFormQ.getKCSJ()));
        }
        if (!TextUtils.isEmpty(detainCarFormQ.getYSSJ()) && detainCarFormQ.getYSSJ().length() > 13) {
            detainCarFormQ.setYSSJ(TimeTool.parseStr(detainCarFormQ.getYSSJ()));
        }
        if (!TextUtils.isEmpty(detainCarFormQ.getZFDWQZSJ()) && detainCarFormQ.getZFDWQZSJ().length() > 13) {
            detainCarFormQ.setZFDWQZSJ(TimeTool.parseStr(detainCarFormQ.getZFDWQZSJ()));
        }
        if(!TextUtils.isEmpty(detainCarFormQ.getYJTL()) && detainCarFormQ.getYJTL().contains("《") && detainCarFormQ.getYJTL().contains("》")){
            try {
                detainCarFormQ.setYJTL(detainCarFormQ.getYJTL().substring(1,detainCarFormQ.getYJTL().length() -1));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        intent.putExtra(WebViewActivityPresenter.RECORD_ITEM, detainCarFormQ);
        intent.putExtra("type",Config.CARFORM);
        return intent;
    }
}
