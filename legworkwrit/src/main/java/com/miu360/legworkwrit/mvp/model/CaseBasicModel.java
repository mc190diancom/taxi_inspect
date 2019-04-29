package com.miu360.legworkwrit.mvp.model;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.miu30.common.async.Result;
import com.miu360.legworkwrit.mvp.contract.CaseBasicContract;
import com.miu360.legworkwrit.mvp.model.api.service.MyApis;
import com.miu360.legworkwrit.mvp.model.entity.District;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;


@ActivityScope
public class CaseBasicModel extends BaseModel implements CaseBasicContract.Model {


    @Inject
    public CaseBasicModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Result<List<District>>> getDistrictInfo(String type) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getDistrict(type);
    }

    @Override
    public Observable<Result<JSONObject>> updateCase(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).updateCase(map);
    }

    @Override
    public Observable<Result<Void>> setInstrumentsFlag(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getBindBl(map);
    }

}