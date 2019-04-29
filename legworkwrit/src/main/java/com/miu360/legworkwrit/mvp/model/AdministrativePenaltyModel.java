package com.miu360.legworkwrit.mvp.model;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.miu30.common.async.Result;
import com.miu30.common.ui.entity.DriverInfo;
import com.miu30.common.ui.entity.FaGuiDetail;
import com.miu360.legworkwrit.mvp.contract.AdministrativePenaltyContract;
import com.miu360.legworkwrit.mvp.model.api.service.MyApis;
import com.miu360.legworkwrit.mvp.model.entity.AdministrativePenalty;
import com.miu360.legworkwrit.mvp.model.entity.IllegalDetail;
import com.miu360.legworkwrit.mvp.model.entity.IllegalDetailItem;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;


@ActivityScope
public class AdministrativePenaltyModel extends BaseModel implements AdministrativePenaltyContract.Model {


    @Inject
    public AdministrativePenaltyModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Result<List<FaGuiDetail>>> getJcxHyRyFgXq(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getWFQX(map);
    }

    @Override
    public Observable<Result<JSONObject>> submitAdministrativeData(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getAdministrativeData(map);
    }

    @Override
    public Observable<Result<List<AdministrativePenalty>>> getInitInfo(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getAdministrativePenalty(map);
    }

    @Override
    public Observable<Result<List<IllegalDetail>>> getIllegalDetailList(String type) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getIllegalDetailList(type);
    }

    @Override
    public Observable<Result<Void>> setInstrumentsFlag(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getBindBl(map);
    }

    @Override
    public Observable<Result<List<DriverInfo>>> getDriverInfo(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getDriverInfo(map);
    }

    @Override
    public Observable<Result<List<IllegalDetailItem>>> getIllegalDetail(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getIllegalContent(map);
    }
}