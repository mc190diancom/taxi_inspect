package com.feidi.video.mvp.model;

import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import com.feidi.video.mvp.contract.VideoPlayContract;


public class VideoPlayModel extends BaseModel implements VideoPlayContract.Model {

    @Inject
    public VideoPlayModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

}