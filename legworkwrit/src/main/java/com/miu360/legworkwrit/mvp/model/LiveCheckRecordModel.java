package com.miu360.legworkwrit.mvp.model;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.miu30.common.async.Result;
import com.miu30.common.ui.entity.DriverInfo;
import com.miu30.common.ui.entity.FaGuiDetail;
import com.miu30.common.ui.entity.JCItem;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu360.legworkwrit.mvp.contract.LiveCheckRecordContract;
import com.miu360.legworkwrit.mvp.model.api.service.MyApis;
import com.miu360.legworkwrit.mvp.model.entity.District;
import com.miu360.legworkwrit.mvp.model.entity.LiveCheckRecordQ;
import com.miu360.legworkwrit.mvp.model.entity.UTC;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;


@ActivityScope
public class LiveCheckRecordModel extends BaseModel implements LiveCheckRecordContract.Model {


    @Inject
    public LiveCheckRecordModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Result<List<JCItem>>> getJclb(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getJclb(map);
    }

    @Override
    public Observable<Result<List<District>>> getDistrictInfo(String type) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getDistrict(type);
    }

    @Override
    public Observable<Result<List<VehicleInfo>>> getVehicleInfo(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getVehicleInfo(map);
    }

    @Override
    public Observable<Result<JSONObject>> submitLiveRecordInfo(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).addXcjcblLjInfo(map);
    }

    @Override
    public Observable<Result<List<FaGuiDetail>>> getJcxHyRyFgXq(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getWFQX(map);
    }

    @Override
    public Observable<Result<List<LiveCheckRecordQ>>> getInitInfo(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getInitLiveCheckRecord(map);
    }

    @Override
    public Observable<Result<Void>> setInstrumentsFlag(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getBindBl(map);
    }

    @Override
    public Observable<Result<List<UTC>>> getBlTime(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).selectCaseBlTime(map);
    }

    @Override
    public Observable<Result<List<DriverInfo>>> getDriverInfo(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getDriverInfo(map);
    }
}