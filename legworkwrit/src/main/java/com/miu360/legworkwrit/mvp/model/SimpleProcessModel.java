package com.miu360.legworkwrit.mvp.model;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.miu30.common.async.Result;
import com.miu360.legworkwrit.mvp.contract.SimpleProcessContract;
import com.miu360.legworkwrit.mvp.model.api.service.MyApis;
import com.miu360.legworkwrit.mvp.model.entity.BlType;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MultipartBody;


@ActivityScope
public class SimpleProcessModel extends BaseModel implements SimpleProcessContract.Model {


    @Inject
    public SimpleProcessModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Result<List<BlType>>> getBlType(String BlType) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getBlType(BlType);
    }

    @Override
    public Observable<Result<Void>> bindID(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getBindBl(map);
    }

    @Override
    public Observable<Result<Void>> removeBindID(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getBindBl(map);
    }

    @Override
    public Observable<Result<Void>> inquiryRecordUploadPhotos(Map<String, Object> map, MultipartBody.Part part) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).inquiryRecordUploadPhotos(map, part);
    }

    @Override
    public Observable<Result<List<InquiryRecordPhoto>>> getInquiryRecordPhotoList(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getInquiryRecordPhotoList(map);
    }
}