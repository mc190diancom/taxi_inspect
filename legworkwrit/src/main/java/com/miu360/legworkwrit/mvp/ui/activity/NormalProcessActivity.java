package com.miu360.legworkwrit.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.base.BaseMvpActivity;
import com.miu30.common.config.Config;
import com.miu30.common.util.UIUtils;
import com.miu30.common.util.Windows;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.di.component.DaggerNormalProcessComponent;
import com.miu360.legworkwrit.di.module.NormalProcessModule;
import com.miu360.legworkwrit.mvp.contract.NormalProcessContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.BlType;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.CaseStatus;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;
import com.miu360.legworkwrit.mvp.presenter.NormalProcessPresenter;
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
 * 正常流程
 */
public class NormalProcessActivity extends BaseMvpActivity<NormalProcessPresenter> implements NormalProcessContract.View {
    @BindView(R2.id.view_switcher)
    ViewSwitcher viewSwitcher;
    @BindView(R2.id.rv_instrument)
    RecyclerView rvInstrument;
    @BindView(R2.id.btn_choose_instrument)
    Button btnChooseInstrument;

    @BindView(R2.id.cb_live_check_record)
    CheckBox cbLiveCheckRecord;                    //现场检查笔录（路检）
    @BindView(R2.id.cb_register_notice)
    CheckBox cbRegisterNotice;                     //先行登记通知书
    @BindView(R2.id.cb_detain_car_form)
    CheckBox cbDetainCarForm;                      //扣押车辆交接单
    @BindView(R2.id.cb_live_transcript)
    CheckBox cbLiveTranscript;                     //现场笔录
    @BindView(R2.id.cb_talk_notice)
    CheckBox cbTalkNotice;                         //谈话通知书
    @BindView(R2.id.cb_detain_car_decide)
    CheckBox cbDetainCarDecide;                    //扣押车辆决定书
    @BindView(R2.id.tv_tip)
    TextView tvTip;                    //选择规则提示

    @Inject
    ChooseInstrumentAdapter adapter;
    @Inject
    HeaderHolder header;
    private boolean isZFRY2;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerNormalProcessComponent
                .builder()
                .appComponent(appComponent)
                .normalProcessModule(new NormalProcessModule(this,this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_normal_process;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        header.init(this, "正常流程");
        initRecyclerView();
        Case c = CacheManager.getInstance().getCase();
        if (c == null) {
            UIUtils.toast(self, "未获取到案件信息", Toast.LENGTH_SHORT);
            finish();
            return;
        }
        assert mPresenter != null;
        ArrayList<BlType> types = getIntent().getParcelableArrayListExtra("bl_types");//从案件列表跳转过来，用于判断是否已经绑定过文书及绑定的文书类型
        isZFRY2 = getIntent().getBooleanExtra("isZFRY2", false);
        mPresenter.init(self, c);
        mPresenter.initCaseList(types
                , cbLiveTranscript, cbLiveCheckRecord, cbRegisterNotice
                , cbDetainCarForm, cbDetainCarDecide, cbTalkNotice);
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
                    case "扣押车辆交接单":
                        intent.setClass(self,DetainCarFormActivity.class);
                        ActivityUtils.startActivity(intent);
                        break;
                    case "谈话通知书":
                        intent.setClass(self,TalkNoticeActivity.class);
                        ActivityUtils.startActivity(intent);
                        break;
                    case "先行登记通知书":
                        intent.setClass(self,FristRegisterNoticeActivity.class);
                        ActivityUtils.startActivity(intent);
                        break;
                    case "现场笔录":
                        intent.setClass(self,LiveTranscriptActivity.class);
                        ActivityUtils.startActivity(intent);
                        break;
                    case "现场检查笔录（路检）":
                        intent.setClass(self,LiveCheckRecordActivity.class);
                        ActivityUtils.startActivity(intent);
                        break;
                    case "扣押车辆决定书":
                        intent.setClass(self,DetainCarDecideActivity.class);
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
        if (!cbDetainCarDecide.isChecked() && !cbDetainCarForm.isChecked() && !cbRegisterNotice.isChecked()
                && !cbLiveCheckRecord.isChecked() && !cbLiveTranscript.isChecked() && !cbTalkNotice.isChecked()) {
            showMessage("请至少选择一种文书");
            return;
        }

        if(isZFRY2){
            UIUtils.toast(self,"当前账号是执法人员2，没有文书添加权限",Toast.LENGTH_SHORT);
            return;
        }

        //this.mPresenter.clearChooseInstrument();
        //这里之所以不用checkbox.getText.toString是因为和接口所得不对应,chooseInstruments没法去和blTypeList的ALIASNAME对比
        if (cbLiveCheckRecord.isChecked()) {
            this.mPresenter.chooseInstrument(cbLiveCheckRecord.getText().toString(), Config.LIVERECORD,Config.ID_LIVERECORD,0);
        }else{
            this.mPresenter.removeInstrument(Config.LIVERECORD);
        }
        if (cbRegisterNotice.isChecked()) {
            this.mPresenter.chooseInstrument(cbRegisterNotice.getText().toString(), Config.FRISTREGISTER,Config.ID_FRISTREGISTER,0);
        }else{
            this.mPresenter.removeInstrument(Config.FRISTREGISTER);
        }
        if (cbTalkNotice.isChecked()) {
            this.mPresenter.chooseInstrument(cbTalkNotice.getText().toString(), Config.TALKNOTICE,Config.ID_TALKNOTICE,0);
        }else{
            this.mPresenter.removeInstrument(Config.TALKNOTICE);
        }
        if (cbDetainCarDecide.isChecked()) {
            this.mPresenter.chooseInstrument(cbDetainCarDecide.getText().toString(), Config.CARDECIDE, Config.ID_CARDECIDE,0);
        }else{
            this.mPresenter.removeInstrument(Config.CARDECIDE);
        }
        if (cbLiveTranscript.isChecked()) {
            this.mPresenter.chooseInstrument(cbLiveTranscript.getText().toString(), Config.LIVETRANSCRIPT,Config.ID_LIVETRANSCRIPT,0);
        }else{
            this.mPresenter.removeInstrument(Config.LIVETRANSCRIPT);
        }
        if (cbDetainCarForm.isChecked()) {
            this.mPresenter.chooseInstrument(cbDetainCarForm.getText().toString(), Config.CARFORM,Config.ID_CARFORM,0);
        }else{
            this.mPresenter.removeInstrument(Config.CARFORM);
        }
        mPresenter.chooseInstrumentFinish();
        //mPresenter.validInstrumentCorrect();
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
            Intent intent = new Intent(self, UploadListActivity.class);
            ActivityUtils.startActivity(intent);
        } else {
            Intent intent = new Intent(self, InquiryRecordActivity.class);
            ActivityUtils.startActivity(intent);
        }
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

    @OnClick(R2.id.btn_add_ask_instrument)
    public void addAskInstrument() {
        assert mPresenter != null;
        mPresenter.getInquiryRecordPhotoList();
    }

    @OnClick(R2.id.tv_tip)
    public void tipClick() {
        Windows.tipDialog(self);
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
        Intent intent = new Intent(activity, NormalProcessActivity.class);
        intent.putExtra("case", c);
        intent.putExtra("infos", infos);
        return intent;
    }


}
