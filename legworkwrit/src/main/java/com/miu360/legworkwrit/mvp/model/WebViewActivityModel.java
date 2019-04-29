package com.miu360.legworkwrit.mvp.model;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.miu30.common.async.Result;
import com.miu360.legworkwrit.mvp.contract.WebViewActivityContract;
import com.miu360.legworkwrit.mvp.model.api.service.MyApis;
import com.miu360.legworkwrit.mvp.model.entity.WifiConfig;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;


@ActivityScope
public class WebViewActivityModel extends BaseModel implements WebViewActivityContract.Model {


    @Inject
    public WebViewActivityModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Result<List<WifiConfig>>> getAllPrinterWifis(String type) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getAllPrinterWifiConfig(type);
    }
}