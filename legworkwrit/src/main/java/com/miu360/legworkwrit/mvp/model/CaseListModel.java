package com.miu360.legworkwrit.mvp.model;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.miu30.common.async.Result;
import com.miu360.legworkwrit.mvp.contract.CaseListContract;
import com.miu360.legworkwrit.mvp.model.api.service.MyApis;
import com.miu360.legworkwrit.mvp.model.entity.BlType;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;
import com.miu360.legworkwrit.mvp.model.entity.UTC;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MultipartBody;


@ActivityScope
public class CaseListModel extends BaseModel implements CaseListContract.Model {


    @Inject
    public CaseListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Result<List<Case>>> getCaseListData(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).selectCaseAll(map);
    }

    @Override
    public Observable<Result<ArrayList<BlType>>> getBlType(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getBlTypeByCase(map);
    }

    @Override
    public Observable<Result<List<UTC>>> getBlTime(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).selectCaseBlTime(map);
    }

    @Override
    public Observable<Result<Void>> sendBackOffice(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).sendBackOffice(map);
    }

    @Override
    public Observable<Result<List<InquiryRecordPhoto>>> getInquiryRecordPhotoList(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getInquiryRecordPhotoList(map);
    }

    @Override
    public Observable<Result<Void>> inquiryRecordUploadPhotos(Map<String, Object> map, MultipartBody.Part part) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).inquiryRecordUploadPhotos(map, part);
    }
}