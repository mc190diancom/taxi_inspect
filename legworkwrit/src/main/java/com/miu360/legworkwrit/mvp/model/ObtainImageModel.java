package com.miu360.legworkwrit.mvp.model;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.miu30.common.async.Result;
import com.miu360.legworkwrit.mvp.contract.ObtainImageContract;
import com.miu360.legworkwrit.mvp.model.api.service.MyApis;
import com.miu360.legworkwrit.mvp.model.entity.UTC;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MultipartBody;


@ActivityScope
public class ObtainImageModel extends BaseModel implements ObtainImageContract.Model {


    @Inject
    public ObtainImageModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Result<Void>> inquiryRecordUploadPhotos(Map<String, Object> map, MultipartBody.Part part) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).inquiryRecordUploadPhotos(map, part);
    }

    @Override
    public Observable<Result<List<UTC>>> getBlTime(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).selectCaseBlTime(map);
    }
}