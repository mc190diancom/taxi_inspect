package com.miu360.legworkwrit.mvp.model;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.miu30.common.async.Result;
import com.miu30.common.ui.entity.queryZFRYByDWMC;
import com.miu360.legworkwrit.mvp.contract.CreateCaseContract;
import com.miu360.legworkwrit.mvp.model.api.service.MyApis;
import com.miu360.legworkwrit.mvp.model.entity.District;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MultipartBody;


@ActivityScope
public class CreateCaseModel extends BaseModel implements CreateCaseContract.Model {

    @Inject
    public CreateCaseModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Result<List<queryZFRYByDWMC>>> getCheZuList(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getCheZuList(map);
    }

    @Override
    public Observable<Result<JSONObject>> createCase(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).addCase(map);
    }

    @Override
    public Observable<Result<List<District>>> getDistrictInfo(String type) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getDistrict(type);
    }

    @Override
    public Observable<Result<Void>> inquiryRecordUploadPhotos(Map<String, Object> map, MultipartBody.Part part) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).inquiryRecordUploadPhotos(map, part);
    }
}