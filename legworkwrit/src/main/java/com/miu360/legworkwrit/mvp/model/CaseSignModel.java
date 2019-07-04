package com.miu360.legworkwrit.mvp.model;

import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import com.miu30.common.async.Result;
import com.miu360.legworkwrit.mvp.contract.CaseSignContract;
import com.miu360.legworkwrit.mvp.model.api.service.MyApis;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;


public class CaseSignModel extends BaseModel implements CaseSignContract.Model {

    @Inject
    public CaseSignModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Result<Void>> uploadSignFile(Map<String, Object> map, MultipartBody.Part part) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).uploadSignFile(map, part);
    }

    @Override
    public Observable<Result<Void>> testAnySignEncPackage(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).testAnySignEncPackage(map);
    }

    @Override
    public Observable<Result<Void>> asyncSignAddJob(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).asyncSignAddJob(map);
    }

    @Override
    public Observable<Result<Void>> pdfsign(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).pdfsign(map);
    }
}