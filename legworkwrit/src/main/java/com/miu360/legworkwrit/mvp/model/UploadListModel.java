package com.miu360.legworkwrit.mvp.model;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.miu30.common.async.Result;
import com.miu360.legworkwrit.mvp.contract.UploadListContract;
import com.miu360.legworkwrit.mvp.model.api.service.MyApis;
import com.miu360.legworkwrit.mvp.model.entity.BlType;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;


@ActivityScope
public class UploadListModel extends BaseModel implements UploadListContract.Model {


    @Inject
    public UploadListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Result<List<InquiryRecordPhoto>>> getInquiryRecordPhotoList(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getInquiryRecordPhotoList(map);
    }

    @Override
    public Observable<Result<Void>> updateInquiryRecord(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).updateInquiryRecord(map);
    }

    @Override
    public Observable<Result<Void>> deleteInquiryRecord(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).deleteInquiryRecord(map);
    }

    @Override
    public Observable<Result<List<BlType>>> getBlType(String getBlType) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getBlType(getBlType);
    }

    @Override
    public Observable<Result<Void>> removeBindID(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getBindBl(map);
    }

    @Override
    public Observable<Result<Void>> deleteAllInquiryRecord(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).deleteAllInquiryRecord(map);
    }
}