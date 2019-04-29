package com.miu360.legworkwrit.mvp.contract;

import android.app.Activity;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.miu30.common.async.Result;
import com.miu30.common.ui.entity.JCItem;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;


public interface IllegalDetailActivityContract {

    interface View extends IView {

        Activity getActivity();

        void getIllegalDetailSuccess(List<JCItem> data);

        void getIllegalHistorySuccess(List<JCItem> data);
    }

    interface Model extends IModel {
        Observable<Result<List<JCItem>>> getIllegalDetail(Map<String, Object> map);
    }
}
