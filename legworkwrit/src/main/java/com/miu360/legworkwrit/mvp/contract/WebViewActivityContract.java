package com.miu360.legworkwrit.mvp.contract;

import android.app.Activity;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.miu30.common.async.Result;
import com.miu360.legworkwrit.mvp.model.entity.WifiConfig;

import java.util.List;

import io.reactivex.Observable;


public interface WebViewActivityContract {

    interface View extends IView {

        Activity getActivity();

        void onCurrentChoosePrinter(WifiConfig config);

        void onCurrentConnectPrinter(WifiConfig config);
    }

    interface Model extends IModel {
        Observable<Result<List<WifiConfig>>> getAllPrinterWifis(String type);
    }
}
