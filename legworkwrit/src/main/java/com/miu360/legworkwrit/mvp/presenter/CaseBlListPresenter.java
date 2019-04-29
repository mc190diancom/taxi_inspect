package com.miu360.legworkwrit.mvp.presenter;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.miu30.common.async.Result;
import com.miu30.common.base.BaseData;
import com.miu30.common.config.Config;
import com.miu360.legworkwrit.app.MyErrorHandleSubscriber;
import com.miu360.legworkwrit.app.utils.RxUtils;
import com.miu360.legworkwrit.mvp.contract.CaseBlListContract;
import com.miu360.legworkwrit.mvp.model.entity.AdministrativePenalty;
import com.miu360.legworkwrit.mvp.model.entity.BlType;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.CaseID;
import com.miu360.legworkwrit.mvp.model.entity.CaseStatus;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarDecideQ;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarFormQ;
import com.miu360.legworkwrit.mvp.model.entity.FristRegisterQ;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;
import com.miu360.legworkwrit.mvp.model.entity.LiveCheckRecordQ;
import com.miu360.legworkwrit.mvp.model.entity.LiveTranscript;
import com.miu360.legworkwrit.mvp.model.entity.TalkNoticeQ;
import com.miu360.legworkwrit.util.MapUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


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
@ActivityScope
public class CaseBlListPresenter extends BasePresenter<CaseBlListContract.Model, CaseBlListContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    ArrayList<CaseStatus> chooseInstruments;

    private Case c;

    @Inject
    public CaseBlListPresenter(CaseBlListContract.Model model, CaseBlListContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }

    public void init(ArrayList<BlType> types,Case c) {
        this.c = c;
        if (types != null && types.size() > 0) {
            for (BlType type : types) {
                if(Config.ZPDZ.equals(type.getCOLUMNNAME())){
                    chooseInstrument("询问笔录",Config.ID_ZPDZ );
                } else if (Config.LIVERECORD.equals(type.getCOLUMNNAME())) {
                    chooseInstrument("现场检查笔录（路检）",Config.ID_LIVERECORD );
                } else if (Config.ADMINISTRATIVE.equals(type.getCOLUMNNAME())) {
                    chooseInstrument("行政处罚决定书", Config.ID_ADMINISTRATIVE);
                } else if (Config.FRISTREGISTER.equals(type.getCOLUMNNAME())) {
                    chooseInstrument("先行登记通知书",Config.ID_FRISTREGISTER );
                } else if (Config.CARFORM.equals(type.getCOLUMNNAME())) {
                    chooseInstrument("扣押车辆交接单",Config.ID_CARFORM );
                } else if (Config.LIVETRANSCRIPT.equals(type.getCOLUMNNAME())) {
                    chooseInstrument("现场笔录",Config.ID_LIVETRANSCRIPT );
                } else if (Config.TALKNOTICE.equals(type.getCOLUMNNAME())) {
                    chooseInstrument("谈话通知书",Config.ID_TALKNOTICE );
                } else if (Config.CARDECIDE.equals(type.getCOLUMNNAME())) {
                    chooseInstrument("扣押车辆决定书",Config.ID_CARDECIDE );
                }
            }

        }

    }

    private void chooseInstrument(String instrument,  String id) {
        chooseInstruments.add(new CaseStatus(instrument,id,1));
    }


    /*
     * 根据案件id查询谈话通知书
     */
    public void getTalkNotice() {
        Map<String, String> params = new HashMap<>();
        params.put("ZID", c.getID());
        Map<String, Object> map = new MapUtil().getMap("getThtzsById", BaseData.gson.toJson(params));
        mModel.getTalkNoticeById(map)
                .compose(RxUtils.<Result<List<TalkNoticeQ>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<TalkNoticeQ>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<TalkNoticeQ>> result) {
                        if (result.ok()) {
                            if (result.getData() != null && result.getData().size() > 0) {
                                mRootView.getTalkNoticeSuccess(result.getData().get(0));
                            } else {
                                mRootView.showMessage("该案件未填写谈话通知书");
                            }
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }
                });
    }

    /**
     * 根据案件id查询现场检查笔录
     */
    public void getLiveCheckRecord() {
        Map<String, String> params = new HashMap<>();
        params.put("ZID", c.getID());
        Map<String, Object> map = new MapUtil().getMap("getXcjcblInfoByZid", BaseData.gson.toJson(params));
        mModel.getLiveCheckRecordById(map)
                .compose(RxUtils.<Result<List<LiveCheckRecordQ>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<LiveCheckRecordQ>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<LiveCheckRecordQ>> result) {
                        if (result.ok()) {
                            if (result.getData() != null && result.getData().size() > 0) {
                                mRootView.getLiveCheckRecordSuccess(result.getData().get(0));
                            } else {
                                mRootView.showMessage("该案件未填写现场检查笔录");
                            }
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }
                });
    }

    /**
     * 根据案件id查询行政处罚决定书
     */
    public void getAdministrativePenalty() {
        Map<String, String> params = new HashMap<>();
        params.put("ZID", c.getID());
        Map<String, Object> map = new MapUtil().getMap("getXzcfjdsInfoById", BaseData.gson.toJson(params));

        mModel.getAdministrativePenaltyById(map)
                .compose(RxUtils.<Result<List<AdministrativePenalty>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<AdministrativePenalty>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<AdministrativePenalty>> result) {
                        if (result.ok()) {
                            if (result.getData() != null && result.getData().size() > 0) {
                                mRootView.getAdministrativePenaltySuccess(result.getData().get(0));
                            } else {
                                mRootView.showMessage("该案件未填写行政处罚决定书");
                            }
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }
                });
    }

    /**
     * 根据案件id查询先行登记通知书
     */
    public void getFristRegister() {
        Map<String, String> params = new HashMap<>();
        params.put("ZID", c.getID());
        Map<String, Object> map = new MapUtil().getMap("getDjbctzsInfoById", BaseData.gson.toJson(params));

        mModel.getFristRegisterById(map)
                .compose(RxUtils.<Result<List<FristRegisterQ>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<FristRegisterQ>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<FristRegisterQ>> result) {
                        if (result.ok()) {
                            if (result.getData() != null && result.getData().size() > 0) {
                                mRootView.getFristRegisterSuccess(result.getData().get(0));
                            } else {
                                mRootView.showMessage("该案件未填写先行登记通知书");
                            }
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }
                });
    }

    /**
     * 通过案件id查询车辆交接单
     */
    public void getDetainCarForm() {
        Map<String, String> params = new HashMap<>();
        params.put("ZID", c.getID());
        Map<String, Object> map = new MapUtil().getMap("getKycljjdInfoById", BaseData.gson.toJson(params));

        mModel.getDetainCarFormById(map)
                .compose(RxUtils.<Result<List<DetainCarFormQ>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<DetainCarFormQ>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<DetainCarFormQ>> result) {
                        if (result.ok()) {
                            if (result.getData() != null && result.getData().size() > 0) {
                                mRootView.getDetainCarFormSuccess(result.getData().get(0));
                            } else {
                                mRootView.showMessage("该案件未填写车辆交接单");
                            }
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }
                });
    }

    /**
     * 根据案件id查询扣押车辆决定书
     */
    public void getDetainCarDecide() {
        Map<String, String> params = new HashMap<>();
        params.put("ZID", c.getID());
        Map<String, Object> map = new MapUtil().getMap("getKycljdsInfoById", BaseData.gson.toJson(params));

        mModel.getDetainCarDecideById(map)
                .compose(RxUtils.<Result<List<DetainCarDecideQ>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<DetainCarDecideQ>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<DetainCarDecideQ>> result) {
                        if (result.ok()) {
                            if (result.getData() != null && result.getData().size() > 0) {
                                mRootView.getDetainCarDecideSuccess(result.getData().get(0));
                            } else {
                                mRootView.showMessage("该案件未填写车辆决定书");
                            }
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }
                });
    }

    /**
     * 根据案件id查询现场笔录
     */
    public void getLiveTranscript() {
        Map<String, String> params = new HashMap<>();
        params.put("ZID", c.getID());
        Map<String, Object> map = new MapUtil().getMap("getXcblById", BaseData.gson.toJson(params));

        mModel.getLiveTranscriptById(map)
                .compose(RxUtils.<Result<List<LiveTranscript>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<LiveTranscript>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<LiveTranscript>> result) {
                        if (result.ok()) {
                            if (result.getData() != null && result.getData().size() > 0) {
                                mRootView.getLiveTranscriptSuccess(result.getData().get(0));
                            } else {
                                mRootView.showMessage("该案件未填写现场笔录");
                            }
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }
                });
    }

    /**
     * 根据案件id查询询问笔录
     */
    public void getInquiryRecord() {
        Map<String, String> params = new HashMap<>();
        params.put("ZID", c.getID());
        Map<String, Object> map = new MapUtil().getMap("getXwblPhotoList", BaseData.gson.toJson(params));
        mModel.getInquiryRecordPhotoList(map)
                .compose(RxUtils.<Result<List<InquiryRecordPhoto>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<InquiryRecordPhoto>>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<List<InquiryRecordPhoto>> result) {
                        if (result.ok()) {
                            if (result.getData() != null && result.getData().size() > 0) {
                                mRootView.getPhotoListSuccess();
                            } else {
                                mRootView.showMessage("该案件未填写询问笔录");
                            }
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }
                });
    }
}
