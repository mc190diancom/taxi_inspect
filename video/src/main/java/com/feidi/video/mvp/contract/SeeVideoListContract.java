package com.feidi.video.mvp.contract;

import com.feidi.video.mvp.model.entity.CameraInfo;
import com.feidi.video.mvp.model.entity.VideoAddress;
import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.miu30.common.async.Result;

import java.util.List;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;

import io.reactivex.Observable;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/04/2019 16:11
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public interface SeeVideoListContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        void notifyAdapter();

        void getVideoAddressSuccess(String rtspUrl);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

        Observable<Result<List<CameraInfo>>> getCameraList(Map<String, Object> map);

        Observable<Result<VideoAddress>> getVideoAddress(Map<String, Object> map);
    }
}
