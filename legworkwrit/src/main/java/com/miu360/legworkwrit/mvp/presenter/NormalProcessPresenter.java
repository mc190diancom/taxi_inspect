package com.miu360.legworkwrit.mvp.presenter;

import android.app.Activity;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.Toast;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.miu30.common.async.Result;
import com.miu30.common.base.BaseData;
import com.miu30.common.config.Config;
import com.miu30.common.util.CommonDialog;
import com.miu30.common.util.MyProgressDialog;
import com.miu30.common.util.UIUtils;
import com.miu30.common.util.Windows;
import com.miu30.common.app.MyErrorHandleSubscriber;
import com.miu360.legworkwrit.app.service.GeneralInformationService;
import com.miu360.legworkwrit.app.utils.RxUtils;
import com.miu360.legworkwrit.mvp.contract.NormalProcessContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.data.PhotoPreference;
import com.miu360.legworkwrit.mvp.model.entity.BLID;
import com.miu360.legworkwrit.mvp.model.entity.BlType;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.CaseID;
import com.miu360.legworkwrit.mvp.model.entity.CaseStatus;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhotoParams;
import com.miu360.legworkwrit.util.GetUTCUtil;
import com.miu360.legworkwrit.util.MapUtil;
import com.miu360.legworkwrit.util.RequestParamsUtil;
import com.miu360.legworkwrit.util.TimeTool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

@ActivityScope
public class NormalProcessPresenter extends BasePresenter<NormalProcessContract.Model, NormalProcessContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    ArrayList<CaseStatus> chooseInstruments;

    private Case mCase;
    private List<BlType> blTypeList;

    private ArrayList<String> chooseInstruments2 = new ArrayList<>();//用于判断选中文书的type
    private ArrayList<String> lastInstruments = new ArrayList<>();//上一次保存的文书类别
    private ArrayList<String> addInstruments = new ArrayList<>();//要新增的文书类别
    private ArrayList<String> removeInstruments = new ArrayList<>();//要移除的文书类别

    private List<String> listOne = new ArrayList<>();//第一种文书种类
    private List<String> listTwo = new ArrayList<>();//第二种文书种类
    private List<String> listThree = new ArrayList<>();//第三种文书种类

    //该案件是否已经绑定的有文书
    private boolean alreadyHasInstrument = false;

    private Activity activity;

    private MyProgressDialog waiting;

    private boolean isAddInquiry;
    private String inquiryStr;
    private boolean isUpload = false;

    @Inject
    public NormalProcessPresenter(NormalProcessContract.Model model, NormalProcessContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        CacheManager.getInstance().removeKey();
        super.onDestroy();
        this.mErrorHandler = null;
    }

    //选择文书
    public void chooseInstrument(String instrument, String type, String id, int status) {
        if (!chooseInstruments2.contains(type)) {
            chooseInstruments.add(new CaseStatus(instrument, id, status));
            chooseInstruments2.add(type);
        }
    }

    //文书状态发生变化
    public void InstrumentStatusChange(Map<String, Integer> updateDatas) {
        if (updateDatas == null || updateDatas.isEmpty()) return;
        for (int i = 0; i < chooseInstruments.size(); i++) {
            if (updateDatas.get(chooseInstruments.get(i).getBlType()) != null) {
                chooseInstruments.set(i, new CaseStatus(chooseInstruments.get(i).getBlType(), chooseInstruments.get(i).getId(), updateDatas.get(chooseInstruments.get(i).getBlType())));
            }
        }
        mRootView.showChooseInstrument();
    }

    //移除已有文书文书
    public void removeInstrument(String type) {
        if (chooseInstruments2.contains(type)) {
            chooseInstruments.remove(chooseInstruments2.indexOf(type));
            chooseInstruments2.remove(type);
        }
    }

    public void chooseInstrumentFinish() {
        chooseInstruments.trimToSize();
        chooseInstruments2.trimToSize();

        if (alreadyHasInstrument) {
            mRootView.switchView();

        } else {
            bindInstruments();
        }

        mRootView.showChooseInstrument();
    }

    public void clearChooseInstrument(){
        chooseInstruments.clear();
    }

    public void init(Activity activity, Case c) {
        this.activity = activity;
        this.mCase = c;

        getBlType();//获取所有文书信息
        initValidRule();//初始化文书选择的验证规则集合
        Intent intent = new Intent(activity, GeneralInformationService.class);
        intent.putExtra("Flag", Config.SERVICE_CAR);
        intent.putExtra("vname", c.getVNAME());
        activity.startService(intent);
    }

    /*
     * 获取笔录列表
     */
    private void getBlType() {
        mModel.getBlType("getBlType")
                .compose(RxUtils.<Result<List<BlType>>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<List<BlType>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<BlType>> result) {
                        if (result.ok()) {
                            blTypeList = result.getData();
                            if (alreadyHasInstrument) {
                                for (int i = 0, len = blTypeList.size(); i < len; i++) {//循环判断哪些文书被选中，取到对应id
                                    if (chooseInstruments2.contains(blTypeList.get(i).getCOLUMNNAME())) {
                                        lastInstruments.add(blTypeList.get(i).getID());
                                    }
                                }
                                alreadyHasInstrument = false;
                            }
                        }
                    }
                });
    }

    /*
     * 初始化文书选择情况
     */
    public void initCaseList(ArrayList<BlType> types, CheckBox cbLiveTranscript, CheckBox cbLiveCheckRecord, CheckBox cbRegisterNotice
            , CheckBox cbDetainCarForm, CheckBox cbDetainCarDecide, CheckBox cbTalkNotice) {
        if (types != null && types.size() > 0) {
            alreadyHasInstrument = true;

            for (BlType type : types) {
                if (Config.LIVERECORD.equals(type.getCOLUMNNAME())) {
                    cbLiveCheckRecord.setChecked(true);
                    chooseInstrument("现场检查笔录（路检）", Config.LIVERECORD, Config.ID_LIVERECORD, type.getFLAG());
                } else if (Config.FRISTREGISTER.equals(type.getCOLUMNNAME())) {
                    cbRegisterNotice.setChecked(true);
                    chooseInstrument("先行登记通知书", Config.FRISTREGISTER, Config.ID_FRISTREGISTER, type.getFLAG());
                } else if (Config.CARFORM.equals(type.getCOLUMNNAME())) {
                    cbDetainCarForm.setChecked(true);
                    chooseInstrument("扣押车辆交接单", Config.CARFORM, Config.ID_CARFORM, type.getFLAG());
                } else if (Config.LIVETRANSCRIPT.equals(type.getCOLUMNNAME())) {
                    cbLiveTranscript.setChecked(true);
                    chooseInstrument("现场笔录", Config.LIVETRANSCRIPT, Config.ID_LIVETRANSCRIPT, type.getFLAG());
                } else if (Config.TALKNOTICE.equals(type.getCOLUMNNAME())) {
                    cbTalkNotice.setChecked(true);
                    chooseInstrument("谈话通知书", Config.TALKNOTICE, Config.ID_TALKNOTICE, type.getFLAG());
                } else if (Config.CARDECIDE.equals(type.getCOLUMNNAME())) {
                    cbDetainCarDecide.setChecked(true);
                    chooseInstrument("扣押车辆决定书", Config.CARDECIDE, Config.ID_CARDECIDE, type.getFLAG());
                }
            }

            chooseInstrumentFinish();
        } else {
            isAddInquiry = true;
        }
    }

    //获取已填写文书的列表
    public ArrayList<CaseStatus> getHasWritedInstrument() {
        ArrayList<CaseStatus> hasWrited = new ArrayList<>();
        for (int i = 0; i < chooseInstruments.size(); i++) {
            if (chooseInstruments.get(i).getStatus() == 1 || chooseInstruments.get(i).getStatus() == 2) {
                hasWrited.add(chooseInstruments.get(i));
            }
        }
        return hasWrited;
    }

    /*
     * 初始化验证规则
     */
    private void initValidRule() {
        listOne.add(Config.FRISTREGISTER);
        listOne.add(Config.CARFORM);
        listOne.add(Config.LIVERECORD);
        listTwo.add(Config.CARDECIDE);
        listTwo.add(Config.LIVETRANSCRIPT);
        listTwo.add(Config.CARFORM);
        listTwo.add(Config.LIVERECORD);
        listThree.add(Config.TALKNOTICE);
        listThree.add(Config.LIVERECORD);
    }

    /*
     * 绑定文书
     */
    private void bindInstruments() {
        if (blTypeList == null) {
            mRootView.showMessage("正在重新获取文书类型ID，请稍后");
            getBlType();
            return;
        }
        addInstruments.clear();
        removeInstruments.clear();

        if (isAddInquiry) {//如果需要添加询问笔录
            for (BlType type : blTypeList) {
                if (Config.ZPDZ.equals(type.getCOLUMNNAME())) {
                    addInstruments.add(type.getID());
                    inquiryStr = type.getID();
                }
            }
        }

        BLID addBlid = new BLID(mCase.getID());
        BLID removeBlid = new BLID(mCase.getID());
        StringBuilder addTypeid = new StringBuilder();
        StringBuilder removeTypeid = new StringBuilder();
        for (int i = 0, len = blTypeList.size(); i < len; i++) {//循环判断哪些文书被选中，取到对应id
            //上次保存的文书中不含有这个ID；且这次在选择列表中(后面个判断是因为checkbox是用COLUMNNAME区分的)
            if (!lastInstruments.contains(blTypeList.get(i).getID()) && chooseInstruments2.contains(blTypeList.get(i).getCOLUMNNAME())) {
                addInstruments.add(blTypeList.get(i).getID());
            }
            //上次保存的文书中含有这个ID；这次不在选择列表中
            if (lastInstruments.contains(blTypeList.get(i).getID()) && !chooseInstruments2.contains(blTypeList.get(i).getCOLUMNNAME())) {
                removeInstruments.add(blTypeList.get(i).getID());
            }
        }
        for (int i = 0; i < addInstruments.size(); i++) {
            addTypeid.append(addInstruments.get(i));
            if (i != addInstruments.size() - 1) {
                addTypeid.append(",");
            }
        }
        for (int i = 0; i < removeInstruments.size(); i++) {
            removeTypeid.append(removeInstruments.get(i));
            if (i != removeInstruments.size() - 1) {
                removeTypeid.append(",");
            }
        }
        addBlid.setTYPEID(addTypeid.toString());
        removeBlid.setTYPEID(removeTypeid.toString());
        if (removeInstruments.isEmpty() && !addInstruments.isEmpty()) {
            waiting = Windows.waiting(activity);
            addInstruments(addBlid);
        } else if (!removeInstruments.isEmpty()) {
            waiting = Windows.waiting(activity);
            removeInstruments(removeBlid, addBlid);
        } else {
            UIUtils.toast(activity, "你的选择未做任何改变，请重新选择", Toast.LENGTH_SHORT);
        }
    }

    private void hideWaiting() {
        if (waiting != null && waiting.isShowing()) {
            waiting.dismiss();
        }
    }

    /*
     * 移除文书
     */
    private void removeInstruments(BLID removeBlid, final BLID addBlid) {
        Map<String, Object> map = new MapUtil().getMap("deleteUpdateBlList", RequestParamsUtil.RequestBLInfo(removeBlid));
        mModel.removeBindID(map)
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<Void>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<Void> result) {
                        if (result.ok()) {
                            clearTimeCache();
                            lastInstruments.removeAll(removeInstruments);
                            if (addInstruments.isEmpty()) {
                                bindSuccess();
                                mRootView.showMessage(result.getMsg());
                            } else {
                                addInstruments(addBlid);
                            }
                        } else {
                            hideWaiting();
                        }
                    }
                });
    }

    /*
     * 清理和时间有关的缓存
     */
    private void clearTimeCache() {
        for (int i = 0, len = blTypeList.size(); i < len; i++) {
            if (removeInstruments.contains(blTypeList.get(i).getID())) {
                CacheManager.getInstance().removeUTC(blTypeList.get(i).getTABLENAME());
            }
        }
    }

    /*
     * 绑定文书
     */
    private void addInstruments(BLID info) {
        Map<String, Object> map = new MapUtil().getMap("addUpdateBlList", RequestParamsUtil.RequestBLInfo(info));
        mModel.bindID(map)
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<Void>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<Void> result) {
                        if (result.ok()) {
                            lastInstruments.addAll(addInstruments);
                            if (isAddInquiry) {
                                lastInstruments.remove(inquiryStr);
                                isAddInquiry = false;
                            }
                            bindSuccess();
                        } else {
                            hideWaiting();
                        }
                        mRootView.showMessage(result.getMsg());
                    }
                });
    }

    /*
     *  文书绑定改动成功
     */
    private void bindSuccess() {
//        if (one) {
//            CacheManager.getInstance().putChooseInstrumentType(1);
//        } else if (two) {
//            CacheManager.getInstance().putChooseInstrumentType(2);
//        } else if (three) {
//            CacheManager.getInstance().putChooseInstrumentType(3);
//        }
        putInstrumentTypeToCache();
        mRootView.bindSuccess();

        ArrayList<InquiryRecordPhoto> infos = activity.getIntent().getParcelableArrayListExtra("infos");

        hideWaiting();

        if (infos != null && infos.size() > 0 && !isUpload) {
            uploadPhotos(infos);
        }
    }

    private void putInstrumentTypeToCache() {
        if (chooseInstruments2.contains(Config.TALKNOTICE)) {
            CacheManager.getInstance().putChooseInstrumentType(3);
        } else if (chooseInstruments2.contains(Config.FRISTREGISTER)) {
            CacheManager.getInstance().putChooseInstrumentType(1);
        } else if (chooseInstruments2.contains(Config.CARDECIDE) || chooseInstruments2.contains(Config.LIVETRANSCRIPT)) {
            CacheManager.getInstance().putChooseInstrumentType(2);
        } else {
            CacheManager.getInstance().putChooseInstrumentType(1);
        }
    }

    private int allPhotoCount;
    private ArrayList<InquiryRecordPhoto> infos;

    private void uploadPhotos(ArrayList<InquiryRecordPhoto> infos) {
        this.allPhotoCount = infos.size();
        this.infos = infos;

        uploadInfo(0);
    }

    private PhotoPreference photoPreference = new PhotoPreference();

    private void uploadInfo(final int index) {
        InquiryRecordPhotoParams params = new InquiryRecordPhotoParams();
        params.setZID(mCase.getID());
        params.setSTARTUTC(String.valueOf(TimeTool.parseDate(infos.get(index).getSTARTUTC()).getTime() / 1000));
        params.setENDUTC(String.valueOf(TimeTool.parseDate(infos.get(index).getENDUTC()).getTime() / 1000));

        Map<String, Object> map = new MapUtil().getMap("upLoadXwblPhoto", BaseData.gson.toJson(params));

        mModel.inquiryRecordUploadPhotos(map, MapUtil.getPart(new File(infos.get(index).getZPLJ())))
                .compose(RxUtils.<Result<Void>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<Void>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<Void> result) {
                        if (result.ok()) {
                            photoPreference.removePhoto(infos.get(index));

                            int count = index + 1;
                            if (count == allPhotoCount) {
                                isUpload = true;
                                GetUTCUtil.updateInquiryRecordUTC(infos);
                                mRootView.uploadPhotosSuccess();
                            } else {
                                uploadInfo(count);
                            }
                        } else {
                            showReuploadPhotoDialog(index);
                        }
                    }
                });
    }

    private void showReuploadPhotoDialog(final int index) {
        Windows.singleChoice(activity
                , "第" + (index + 1) + "份询问笔录上传失败,是否重新上传?"
                , new String[]{"是", "否"}
                , new CommonDialog.OnDialogItemClickListener() {
                    @Override
                    public void dialogItemClickListener(int position) {
                        if (position == 0) {
                            uploadInfo(index);
                        }
                    }
                });
    }

    public void getInquiryRecordPhotoList() {
        Map<String, Object> map = new MapUtil().getMap("getXwblPhotoList", BaseData.gson.toJson(new CaseID(mCase.getID())));

        mModel.getInquiryRecordPhotoList(map)
                .compose(RxUtils.<Result<List<InquiryRecordPhoto>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<InquiryRecordPhoto>>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<List<InquiryRecordPhoto>> result) {
                        if (result.ok()) {
                            mRootView.getPhotoListSuccess(result.getData());
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }
                });
    }

    private boolean one = false, two = false, three = false;

    /*
     * 验证文书选择的正确性
     */
    public void validInstrumentCorrect() {
        one = false;
        two = false;
        three = false;
        if (listOne.containsAll(chooseInstruments2)) {
            one = true;
        }
        if (listTwo.containsAll(chooseInstruments2)) {
            two = true;
        }
        if (listThree.containsAll(chooseInstruments2)) {
            three = true;
        }
        if (one || two || three) {
            chooseInstrumentFinish();
        } else {
            Windows.tipDialog(activity);
        }
    }
}
