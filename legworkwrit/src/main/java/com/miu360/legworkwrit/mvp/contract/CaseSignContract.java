package com.miu360.legworkwrit.mvp.contract;

import android.app.Activity;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.miu30.common.async.Result;

import java.util.List;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;

import io.reactivex.Observable;
import okhttp3.MultipartBody;


public interface CaseSignContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        Activity getActivity();
        void uploadSuccess(boolean Result);
        void signResult(int isSuccess);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

        Observable<Result<Void>> uploadSignFile(Map<String, Object> paramsMap, MultipartBody.Part part);

        Observable<Result<Void>> asyncSignAddJob(Map<String, Object> map);

        Observable<Result<Void>> testAnySignEncPackage(Map<String, Object> map);

        Observable<Result<Void>> pdfsign(Map<String, Object> map);
    }
}
