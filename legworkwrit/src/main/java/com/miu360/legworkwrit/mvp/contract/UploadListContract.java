package com.miu360.legworkwrit.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.miu30.common.async.Result;
import com.miu360.legworkwrit.mvp.model.entity.BlType;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;


public interface UploadListContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void modifyStartTimeSuccess();

        void modifyEndTimeSuccess();

        void deleteUploadInfoSuccess();

        void loadUploadInfoSuccess();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<Result<List<InquiryRecordPhoto>>> getInquiryRecordPhotoList(Map<String, Object> map);

        Observable<Result<Void>> updateInquiryRecord(Map<String, Object> map);

        Observable<Result<Void>> deleteInquiryRecord(Map<String, Object> map);

        Observable<Result<List<BlType>>> getBlType(String getBlType);

        Observable<Result<Void>> removeBindID(Map<String, Object> map);

        Observable<Result<Void>> deleteAllInquiryRecord(Map<String, Object> map);
    }
}
