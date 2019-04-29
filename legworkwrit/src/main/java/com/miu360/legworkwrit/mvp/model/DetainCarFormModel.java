package com.miu360.legworkwrit.mvp.model;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.miu30.common.async.Result;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu360.legworkwrit.mvp.contract.DetainCarFormContract;
import com.miu360.legworkwrit.mvp.model.api.service.MyApis;
import com.miu360.legworkwrit.mvp.model.entity.AccordRule;
import com.miu360.legworkwrit.mvp.model.entity.AgencyInfo;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarFormQ;
import com.miu360.legworkwrit.mvp.model.entity.Park;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MultipartBody;


@ActivityScope
public class DetainCarFormModel extends BaseModel implements DetainCarFormContract.Model {

    @Inject
    public DetainCarFormModel(IRepositoryManager repositoryManager) {
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
    public Observable<Result<JSONObject>> addDetainCarForm(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).addDetainCarForm(map);
    }

    @Override
    public Observable<Result<Void>> detainCarFormUploadPhotos(Map<String, Object> map, List<MultipartBody.Part> parts) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).detainCarFormUploadPhotos(map, parts);
    }

    @Override
    public Observable<Result<List<AgencyInfo>>> getAgencyInfos(String type) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getAgencyInfos(type);
    }

    @Override
    public Observable<Result<List<DetainCarFormQ>>> getInitInfo(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getDetainCarForm(map);
    }

    @Override
    public Observable<Result<List<AccordRule>>> getAccordRuleList(String type) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getAccordRuleList(type);
    }

    @Override
    public Observable<Result<Void>> setInstrumentsFlag(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getBindBl(map);
    }
}