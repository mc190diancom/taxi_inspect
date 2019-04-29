package com.miu360.legworkwrit.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.miu360.legworkwrit.mvp.contract.CaseSearchContract;


@ActivityScope
public class CaseSearchModel extends BaseModel implements CaseSearchContract.Model {


    @Inject
    public CaseSearchModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

}