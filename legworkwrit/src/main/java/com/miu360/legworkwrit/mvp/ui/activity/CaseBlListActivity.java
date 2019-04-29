package com.miu360.legworkwrit.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.util.UIUtils;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.di.component.DaggerCaseBlListComponent;
import com.miu360.legworkwrit.di.module.CaseBlListModule;
import com.miu360.legworkwrit.mvp.contract.CaseBlListContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.AdministrativePenalty;
import com.miu360.legworkwrit.mvp.model.entity.BlType;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.CaseStatus;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarDecideQ;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarFormQ;
import com.miu360.legworkwrit.mvp.model.entity.FristRegisterQ;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;
import com.miu360.legworkwrit.mvp.model.entity.LiveCheckRecordQ;
import com.miu360.legworkwrit.mvp.model.entity.LiveTranscript;
import com.miu360.legworkwrit.mvp.model.entity.TalkNoticeQ;
import com.miu360.legworkwrit.mvp.presenter.CaseBlListPresenter;
import com.miu360.legworkwrit.mvp.ui.adapter.ChooseInstrumentAdapter;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/20/2018 11:31
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class CaseBlListActivity extends BaseMvpActivity<CaseBlListPresenter> implements CaseBlListContract.View {

    @Inject
    ChooseInstrumentAdapter adapter;

    @Inject
    HeaderHolder header;

    @BindView(R2.id.rv_instrument)
    RecyclerView rvInstrument;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCaseBlListComponent
                .builder()
                .appComponent(appComponent)
                .caseBlListModule(new CaseBlListModule(this,this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_case_bl_list;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        header.init(self,"正常流程");
        initRecyclerView();
        Case c = CacheManager.getInstance().getCase();
        if (c == null) {
            UIUtils.toast(self, "未获取到案件信息", Toast.LENGTH_SHORT);
            finish();
            return;
        }
        if("处罚".equals(c.getCFFS())){
            header.setTitle("正常流程");
        }else{
            header.setTitle("简易流程");
        }
        assert mPresenter != null;
        ArrayList<BlType> types = getIntent().getParcelableArrayListExtra("bl_types");//从案件列表跳转过来，用于判断是否已经绑定过文书及绑定的文书类型
        mPresenter.init(types,c);
    }

    private void initRecyclerView() {
        adapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                if (data == null) return;
                CaseStatus caseStatus = (CaseStatus) data;
                assert mPresenter != null;
                switch (caseStatus.getBlType()) {
                    case "询问笔录":
                        mPresenter.getInquiryRecord();
                        break;
                    case "扣押车辆交接单":
                        mPresenter.getDetainCarForm();
                        break;
                    case "谈话通知书":
                        mPresenter.getTalkNotice();
                        break;
                    case "先行登记通知书":
                        mPresenter.getFristRegister();
                        break;
                    case "现场笔录":
                        mPresenter.getLiveTranscript();
                        break;
                    case "现场检查笔录（路检）":
                        mPresenter.getLiveCheckRecord();
                        break;
                    case "扣押车辆决定书":
                        mPresenter.getDetainCarDecide();
                        break;
                    case "行政处罚决定书":
                        mPresenter.getAdministrativePenalty();
                        break;
                    default:
                        break;
                }
            }
        });
        rvInstrument.setLayoutManager(new LinearLayoutManager(self));
        rvInstrument.setAdapter(adapter);
    }

    @Override
    public void getLiveCheckRecordSuccess(LiveCheckRecordQ liveCheckRecordQ) {
        ActivityUtils.startActivity(WebViewActivity.getIntent(self, liveCheckRecordQ, true));
    }

    @Override
    public void getAdministrativePenaltySuccess(AdministrativePenalty administrativePenalty) {
        ActivityUtils.startActivity(WebViewActivity.getIntent(self, administrativePenalty, true));
    }

    @Override
    public void getFristRegisterSuccess(FristRegisterQ fristRegisterQ) {
        ActivityUtils.startActivity(WebViewActivity.getIntent(self, fristRegisterQ, true));
    }

    @Override
    public void getDetainCarFormSuccess(DetainCarFormQ detainCarFormQ) {
        ActivityUtils.startActivity(WebViewActivity.getIntent(self, detainCarFormQ, true));
    }

    @Override
    public void getDetainCarDecideSuccess(DetainCarDecideQ detainCarDecideQ) {
        ActivityUtils.startActivity(WebViewActivity.getIntent(self, detainCarDecideQ, true));
    }

    @Override
    public void getLiveTranscriptSuccess(LiveTranscript liveTranscript) {
        ActivityUtils.startActivity(WebViewActivity.getIntent(self, liveTranscript, true));
    }

    @Override
    public void getTalkNoticeSuccess(TalkNoticeQ talkNoticeQ) {
        ActivityUtils.startActivity(WebViewActivity.getIntent(self, talkNoticeQ, true));
    }

    @Override
    public void getPhotoListSuccess() {
        Intent intent = new Intent(self, UploadListActivity.class);
        intent.putExtra("history",true);
        ActivityUtils.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        CacheManager.getInstance().removeCase();
        super.onDestroy();
    }
}
