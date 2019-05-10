package com.miu360.legworkwrit.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.base.BaseMvpActivity;
import com.miu30.common.config.Config;
import com.miu30.common.util.UIUtils;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.di.component.DaggerSimpleProcessComponent;
import com.miu360.legworkwrit.di.module.SimpleProcessModule;
import com.miu360.legworkwrit.mvp.contract.SimpleProcessContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.data.PhotoPreference;
import com.miu360.legworkwrit.mvp.model.entity.BlType;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.CaseStatus;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;
import com.miu360.legworkwrit.mvp.presenter.SimpleProcessPresenter;
import com.miu360.legworkwrit.mvp.ui.adapter.ChooseInstrumentAdapter;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 简易流程
 */
public class SimpleProcessActivity extends BaseMvpActivity<SimpleProcessPresenter> implements SimpleProcessContract.View {
    @BindView(R2.id.view_switcher)
    ViewSwitcher viewSwitcher;
    @BindView(R2.id.rv_instrument)
    RecyclerView rvInstrument;

    @BindView(R2.id.cb_live_check_record)
    CheckBox cbLiveCheckRecord;                                //现场检查笔录（路检）
    @BindView(R2.id.cb_administrative_punish_decide)
    CheckBox cbAdministrativePunishDecide;                     //行政处罚决定书

    @Inject
    HeaderHolder header;

    @Inject
    ChooseInstrumentAdapter adapter;

    Case mCase;
    List<CheckBox> cbs;
    private boolean isZFRY2;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSimpleProcessComponent
                .builder()
                .appComponent(appComponent)
                .simpleProcessModule(new SimpleProcessModule(this,this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_simple_process;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        header.init(self, "简易流程");
        initRecyclerView();
        cbs = new ArrayList<>();
        mCase = CacheManager.getInstance().getCase();
        if (mCase == null) {
            UIUtils.toast(self, "未获取到案件信息", Toast.LENGTH_SHORT);
            finish();
            return;
        }
        assert mPresenter != null;
        ArrayList<BlType> types = getIntent().getParcelableArrayListExtra("bl_types");
        isZFRY2 = getIntent().getBooleanExtra("isZFRY2", false);
        mPresenter.initData(self, mCase);
        mPresenter.initCaseList(types,cbLiveCheckRecord,cbAdministrativePunishDecide);
    }

    private void initRecyclerView() {
        adapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                if(data == null) return;
                CaseStatus caseStatus = (CaseStatus) data;
                Intent intent = new Intent();
                assert mPresenter != null;
                ArrayList<CaseStatus> caseStatusList = mPresenter.getHasWritedInstrument();
                intent.putParcelableArrayListExtra("WritedInstrument",caseStatusList);
                if(isZFRY2 && 0 == caseStatus.getStatus()){
                    UIUtils.toast(self,"当前账号是执法人员2，没有文书编辑权限",Toast.LENGTH_SHORT);
                    return;
                }
                intent.putExtra("isZFRY2",isZFRY2);
                switch (caseStatus.getBlType()) {
                    case "现场检查笔录（路检）":
                        intent.setClass(self,LiveCheckRecordActivity.class);
                        ActivityUtils.startActivity(intent);
                        break;
                    case "行政处罚决定书":
                        intent.setClass(self,AdministrativePenaltyActivity.class);
                        ActivityUtils.startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
        rvInstrument.setLayoutManager(new LinearLayoutManager(self));
        rvInstrument.setAdapter(adapter);
    }

    @OnClick(R2.id.btn_choose_instrument)
    @SuppressWarnings("all")
    public void chooseInstrument() {
        if (!cbLiveCheckRecord.isChecked() && !cbAdministrativePunishDecide.isChecked()) {
            showMessage("请至少选择一种文书");
            return;
        }

        if(isZFRY2){
            UIUtils.toast(self,"当前账号是执法人员2，没有文书添加权限",Toast.LENGTH_SHORT);
            return;
        }

        if (cbLiveCheckRecord.isChecked()) {
            this.mPresenter.chooseInstrument(cbLiveCheckRecord.getText().toString(), Config.LIVERECORD,Config.ID_LIVERECORD,0);
        }else {
            this.mPresenter.removeInstrument(Config.LIVERECORD);
        }
        if (cbAdministrativePunishDecide.isChecked()) {
            this.mPresenter.chooseInstrument(cbAdministrativePunishDecide.getText().toString(), Config.ADMINISTRATIVE,Config.ID_ADMINISTRATIVE,0);
        }else {
            this.mPresenter.removeInstrument(Config.ADMINISTRATIVE);
        }

        this.mPresenter.chooseInstrumentFinish();
    }

    @OnClick(R2.id.btn_reselect_instrument)
    public void reselectInstrument() {
        if (viewSwitcher.getDisplayedChild() == 1) {
            if(isZFRY2){
                UIUtils.toast(self,"当前账号是执法人员2，没有文书重选权限",Toast.LENGTH_SHORT);
                return;
            }
            viewSwitcher.showPrevious();
        }
    }

    @Override
    public void showChooseInstrument() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void bindSuccess() {
        viewSwitcher.showNext();
    }

    @Override
    public void switchView() {
        viewSwitcher.showNext();
    }

    @Override
    public void uploadPhotosSuccess() {
        UIUtils.toast(self, "上传询问笔录成功", Toast.LENGTH_SHORT);
    }

    @Override
    public void getPhotoListSuccess(List<InquiryRecordPhoto> data) {
        if (data != null && data.size() > 0) {
            ActivityUtils.startActivity(UploadListActivity.class);
        } else {
            ActivityUtils.startActivity(InquiryRecordActivity.class);
        }
    }

    @OnClick(R2.id.btn_add_ask_instrument)
    public void addAskInstrument() {
        mPresenter.getInquiryRecordPhotoList();
    }

    /**
     * 当文书状态发生改变，更新集合
     */
    @SuppressWarnings("all")
    @Subscriber(tag = Config.UPDATECASESTATUS)
    public void updateInfo(Map<String,Integer> updateDatas) {
        mPresenter.InstrumentStatusChange(updateDatas);
    }

    public static Intent getCaseIntent(Activity activity, Case c, ArrayList<InquiryRecordPhoto> infos) {
        Intent intent = new Intent(activity, SimpleProcessActivity.class);
        intent.putExtra("case", c);
        intent.putParcelableArrayListExtra("infos", infos);
        return intent;
    }
}
