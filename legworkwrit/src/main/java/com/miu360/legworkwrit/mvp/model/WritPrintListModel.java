package com.miu360.legworkwrit.mvp.model;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.miu30.common.async.Result;
import com.miu360.legworkwrit.mvp.contract.WritPrintListContract;
import com.miu360.legworkwrit.mvp.model.api.service.MyApis;
import com.miu360.legworkwrit.mvp.model.entity.AdministrativePenalty;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarDecideQ;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarFormQ;
import com.miu360.legworkwrit.mvp.model.entity.FristRegisterQ;
import com.miu360.legworkwrit.mvp.model.entity.LiveCheckRecordQ;
import com.miu360.legworkwrit.mvp.model.entity.LiveTranscript;
import com.miu360.legworkwrit.mvp.model.entity.TalkNoticeQ;
import com.miu360.legworkwrit.mvp.model.entity.WifiConfig;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;


@ActivityScope
public class WritPrintListModel extends BaseModel implements WritPrintListContract.Model {


    @Inject
    public WritPrintListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Result<List<TalkNoticeQ>>> getTalkNoticeById(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getTalkNotice(map);
    }

    @Override
    public Observable<Result<List<LiveCheckRecordQ>>> getLiveCheckRecordById(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getInitLiveCheckRecord(map);
    }

    @Override
    public Observable<Result<List<AdministrativePenalty>>> getAdministrativePenaltyById(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getAdministrativePenalty(map);
    }

    @Override
    public Observable<Result<List<FristRegisterQ>>> getFristRegisterById(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getFristRegister(map);
    }

    @Override
    public Observable<Result<List<DetainCarFormQ>>> getDetainCarFormById(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getDetainCarForm(map);
    }

    @Override
    public Observable<Result<List<DetainCarDecideQ>>> getDetainCarDecideById(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getDetainCarDecide(map);
    }

    @Override
    public Observable<Result<List<LiveTranscript>>> getLiveTranscriptById(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getLiveTranscript(map);
    }

    @Override
    public Observable<Result<List<WifiConfig>>> getPrinterWifiConfigMsg(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getPrinterWifiConfigMsg(map);
    }

    @Override
    public Observable<Result<List<WifiConfig>>> getAllPrinterWifiConfig(String type) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getAllPrinterWifiConfig(type);
    }

    @Override
    public Observable<Result<String>> modifyPrintTimes(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).modifyPrintTimes(map);
    }
}