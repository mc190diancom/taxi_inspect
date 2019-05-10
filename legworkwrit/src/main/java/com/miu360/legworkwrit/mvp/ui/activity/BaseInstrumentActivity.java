package com.miu360.legworkwrit.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.jess.arms.mvp.IPresenter;
import com.miu30.common.base.BaseMvpActivity;
import com.miu30.common.config.Config;
import com.miu30.common.util.CommonDialog;
import com.miu30.common.util.UIUtils;
import com.miu30.common.util.Windows;
import com.miu30.common.util.validError;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.app.service.GeneralInformationService;
import com.miu360.legworkwrit.mvp.contract.BaseInstrumentContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.AdministrativePenalty;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.CaseStatus;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarDecideQ;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarFormQ;
import com.miu360.legworkwrit.mvp.model.entity.FristRegisterQ;
import com.miu360.legworkwrit.mvp.model.entity.LiveCheckRecordQ;
import com.miu360.legworkwrit.mvp.model.entity.LiveTranscript;
import com.miu360.legworkwrit.mvp.model.entity.ParentQ;
import com.miu360.legworkwrit.mvp.model.entity.PrintTimes;
import com.miu360.legworkwrit.mvp.model.entity.TalkNoticeQ;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * <p> presenter
 * 所有文书对应的Activity基类
 * 主要实现缩放以及拖动功能
 */
public abstract class BaseInstrumentActivity<P extends IPresenter> extends BaseMvpActivity<P> implements Validator.ValidationListener, BaseInstrumentContract, TextWatcher {
    @BindView(R2.id.btn_preview)
    Button btnPreview;
    @BindView(R2.id.btn_save)
    Button btnSave;

    @Inject
    HeaderHolder header;

    @Inject
    Validator mValidator;

    public Case mCase;
    public boolean isUpdate;//判断文书是修改状态还是第一次填写
    public int clickStatus;//0保存，1直接预览，2保存后预览
    public boolean isZFRY2;//当前账号是否是执法人员2
    public ArrayList<CaseStatus> followInstruments;

    public boolean StopChange = false;//用于监听车牌号输入框变化

    public String startUtc = "";
    public String endUtc = "";

    /**
     * 文书id，打印需要使用
     * 1、已填写文书在请求文书信息接口时会返回
     * 2、第一次保存文书会返回，修改文书也会返回
     */
    protected String instrumentID;
    //文书类型
    protected String instrumentType;
    private int updateAllCaseStatus = 33;

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (initCase()) return;
        followInstruments = getIntent().getParcelableArrayListExtra("WritedInstrument");
        isZFRY2 = getIntent().getBooleanExtra("isZFRY2", false);
        header.setUpRightTextBtn("常规信息", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("WritedInstrument", followInstruments);
                intent.putExtra("CurrentInstrument", getInstrumentId());
                intent.putExtra("isZFRY2", isZFRY2);
                intent.setClass(self, CaseBasicActivity.class);
                startActivityForResult(intent,updateAllCaseStatus);
            }
        });
        submitClick();
        //注册验证
        mValidator.setValidationListener(this);
    }

    /*
     * 初始化案件信息
     */
    public boolean initCase() {
        mCase = CacheManager.getInstance().getCase();
        if (mCase == null) {
            UIUtils.toast(self, "未获取到案件信息", Toast.LENGTH_SHORT);
            finish();
            return true;
        }
        return false;
    }

    /*
     * 防止点击保存双击
     */
    @SuppressWarnings("all")
    private void submitClick() {
        RxView.clicks(btnSave)
                .throttleFirst(2, TimeUnit.SECONDS) //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        if (validType()) return;
                        if (isZFRY2) {
                            UIUtils.toast(self, "当前账号是执法人员2，不能进行文书编辑", Toast.LENGTH_SHORT);
                            return;
                        }
                        clickStatus = 0;
                        mValidator.validate();
                    }
                });

        RxView.clicks(btnPreview)
                .throttleFirst(1, TimeUnit.SECONDS) //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        clickStatus = 1;
                        if (!isUpdate || isConfirmed()) {
                            clickStatus = 2;
                        }
                        mValidator.validate();
                    }
                });
    }

    public boolean validType() {
        return false;
    }

    /*
     * 根据返回结果码判断文书是第一次添加还是修改
     */
    @Override
    public void showResult(ParentQ info, int resultCode) {
        if (resultCode == Config.RESULT_ERROR) {//请求失败
            UIUtils.toast(self, "初始化信息获取失败", Toast.LENGTH_SHORT);
            finish();
        } else if (resultCode == Config.RESULT_SUCCESS) {//请求成功且已经填写过该文书
            isUpdate = true;
            if (info == null) return;
            showViewContent(info);
            if (isConfirmed()) {
                showCaseContent();
            }
        } else {//请求成功且第一次填写；不做任何操作
            isUpdate = false;
            showViewPartContent();
        }
        addListener();//所有信息返填后，增加监听
    }

    @Override
    public void onValidationSucceeded() {
    }

    @Override
    public void onValidationFailed(List<ValidationError> list) {
        UIUtils.toast(self, validError.getFailedContent(self, list), Toast.LENGTH_SHORT);
    }

    Intent intent = null;
    @Override
    public void startWebActivity(final ParentQ parentQ) {
        switch (instrumentType){
            case Config.T_LIVERECORD:
                intent = WebViewActivity.getIntent(self, (LiveCheckRecordQ) parentQ, false);
                break;
            case Config.T_ADMINISTRATIVE:
                intent = WebViewActivity.getIntent(self, (AdministrativePenalty) parentQ, false);
                break;
            case Config.T_TALKNOTICE:
                intent = WebViewActivity.getIntent(self, (TalkNoticeQ) parentQ, false);
                break;
            case Config.T_FRISTREGISTER:
                intent = WebViewActivity.getIntent(self, (FristRegisterQ) parentQ, false);
                break;
            case Config.T_CARFORM:
                intent = WebViewActivity.getIntent(self, (DetainCarFormQ) parentQ, false);
                break;
            case Config.T_CARDECIDE:
                intent = WebViewActivity.getIntent(self, (DetainCarDecideQ) parentQ, false);
                break;
            case Config.T_LIVETRANSCRIPT:
                intent = WebViewActivity.getIntent(self, (LiveTranscript) parentQ, false);
                break;
        }
        if(clickStatus == 2){//因为的是点击预览，所以保存后直接跳转预览
            followInstruments.clear();
            isUpdate = true;
            startActivityForResult(intent,0x0001);
        }else{//因为点击的是保存，所以是提示预览
            Windows.singleChoice2(self
                    , "保存成功，是否跳转预览?"
                    , new String[]{"是", "否"}
                    , new CommonDialog.OnDialogItemClickListener() {
                        @Override
                        public void dialogItemClickListener(int position) {
                            if (position == 0) {
                                followInstruments.clear();
                                isUpdate = true;
                                startActivityForResult(intent,0x0001);
                            }else{
                                finish();
                            }
                        }
                    });
        }
    }

    /**
     * 判断当前文书是否是待确认的文书
     * return true是待确认的状态，false否之
     */
    public boolean isConfirmed() {
        for (int i = 0; i < followInstruments.size(); i++) {
            if (followInstruments.get(i).getId().equals(getInstrumentId()) && 2 == followInstruments.get(i).getStatus()) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("all")
    @Subscriber(tag = Config.UPDATECASE)
    public void updateInfo(boolean isUpdate) {
        initCase();
        showCaseContent();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (StopChange) {
            StopChange = false;
            return;
        }
        getCarOrDriverInfo();
    }

    //因为不是所有文书倒要用到这几个方法,所以不做成抽象的
    public void getCarOrDriverInfo() {
    }

    public void showViewPartContent() {
    }

    public void addListener() {
    }

    public abstract void showViewContent(ParentQ info);

    public abstract void showCaseContent();

    public abstract boolean checkChange();

    public abstract String getInstrumentId();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == updateAllCaseStatus&& resultCode == RESULT_OK){
            for (int i = 0; i < followInstruments.size(); i++) {
                followInstruments.get(i).setStatus(2);
            }
        } else if (resultCode == RESULT_OK) {
            if (data != null) {
                final int times = calPrintTimes(data);
                System.out.println("testtest:id==="+instrumentID);
                if (times > 0) {
                    final PrintTimes printTimes = new PrintTimes();
                    printTimes.setID(instrumentID);
                    printTimes.setTABLENAME(instrumentType);
                    printTimes.setPRINTTIMES(String.valueOf(times));
                    startModifyPrintTimesService(printTimes);
                }
            }
        }
    }

    private int calPrintTimes(Intent intent) {
        int pages = intent.getIntExtra("pages", 0);
        try {
            int oldPages = Integer.valueOf(CacheManager.getInstance().getPrintTimes(instrumentType));
            pages = pages + oldPages;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pages;
    }

    private void startModifyPrintTimesService(PrintTimes printTimes) {
        Intent intent = new Intent(this, GeneralInformationService.class);
        intent.putExtra("Flag", Config.SERVICE_PRINT);
        intent.putExtra("PrintTimes", printTimes);
        System.out.println("testtest:预览打印开始记录次数");
        startService(intent);
    }

}
