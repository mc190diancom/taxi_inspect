package com.miu360.legworkwrit.mvp.model;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.miu30.common.async.Result;
import com.miu30.common.ui.entity.DriverInfo;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu360.legworkwrit.mvp.contract.DetainCarDecideContract;
import com.miu360.legworkwrit.mvp.model.api.service.MyApis;
import com.miu360.legworkwrit.mvp.model.entity.AgencyInfo;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarDecideQ;
import com.miu360.legworkwrit.mvp.model.entity.Park;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;


@ActivityScope
public class DetainCarDecideModel extends BaseModel implements DetainCarDecideContract.Model {


    @Inject
    public DetainCarDecideModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Result<List<Park>>> getParkList(String type) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getParkList(type);
    }

    @Override
    public Observable<Result<List<VehicleInfo>>> getVehicleInfo(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getVehicleInfo(map);
    }

    @Override
    public Observable<Result<JSONObject>> addDetainCarDecide(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).addDetainCarDecide(map);
    }

    @Override
    public Observable<Result<List<DetainCarDecideQ>>> getInitInfo(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getDetainCarDecide(map);
    }

    @Override
    public Observable<Result<List<AgencyInfo>>> getAgencyInfos(String type) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getAgencyInfos(type);
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