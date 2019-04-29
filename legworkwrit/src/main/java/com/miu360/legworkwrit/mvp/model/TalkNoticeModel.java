package com.miu360.legworkwrit.mvp.model;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.miu30.common.async.Result;
import com.miu360.legworkwrit.mvp.contract.TalkNoticeContract;
import com.miu360.legworkwrit.mvp.model.api.service.MyApis;
import com.miu360.legworkwrit.mvp.model.entity.AgencyInfo;
import com.miu360.legworkwrit.mvp.model.entity.TalkNoticeQ;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;


@ActivityScope
public class TalkNoticeModel extends BaseModel implements TalkNoticeContract.Model {

    @Inject
    public TalkNoticeModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }


    @Override
    public Observable<Result<JSONObject>> addTalkNotice(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).addTalkNotice(map);
    }

    @Override
    public Observable<Result<List<AgencyInfo>>> getAgencyInfos(String type) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getAgencyInfos(type);
    }

    @Override
    public Observable<Result<List<TalkNoticeQ>>> getInitInfo(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getTalkNotice(map);
    }

    @Override
    public Observable<Result<Void>> setInstrumentsFlag(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getBindBl(map);
    }

    @Override
    public Observable<Result<List<AgencyInfo>>> getAgencyInfosByZFZH(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getPrinterAgencyInfoMsg(map);
    }
}