package com.miu360.legworkwrit.mvp.model;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.miu30.common.async.Result;
import com.miu360.legworkwrit.mvp.contract.InquiryRecordContract;
import com.miu360.legworkwrit.mvp.model.api.service.MyApis;
import com.miu360.legworkwrit.mvp.model.entity.UTC;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;


@ActivityScope
public class InquiryRecordModel extends BaseModel implements InquiryRecordContract.Model {


    @Inject
    public InquiryRecordModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Result<List<UTC>>> getBlTime(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).selectCaseBlTime(map);
    }
}