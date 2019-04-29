package com.miu360.legworkwrit.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.miu30.common.async.Result;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;
import com.miu360.legworkwrit.mvp.model.entity.UTC;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;


public interface ObtainImageContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void uploadSuccess();

        void uploadFailure();

        void saveSuccess(InquiryRecordPhoto photo);

        void saveFailure();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<Result<Void>> inquiryRecordUploadPhotos(Map<String, Object> map, MultipartBody.Part part);

        Observable<Result<List<UTC>>> getBlTime(Map<String, Object> map);
    }
}
