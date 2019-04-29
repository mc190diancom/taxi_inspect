package com.miu360.legworkwrit.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.miu30.common.async.Result;
import com.miu360.legworkwrit.mvp.model.entity.BlType;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;
import com.miu360.legworkwrit.mvp.model.entity.UTC;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;


public interface CaseListContract {

    interface View extends IView {
        void getCaseListSuccess(List<Case> cases,int page);

        void getCaseBlTypeSuccess(Case c, ArrayList<BlType> types);

        void uploadPhotosSuccess();

    }

    interface Model extends IModel {
        Observable<Result<List<Case>>> getCaseListData(Map<String, Object> map);

        Observable<Result<ArrayList<BlType>>> getBlType(Map<String, Object> map);

        Observable<Result<List<UTC>>> getBlTime(Map<String, Object> map);

        Observable<Result<Void>> sendBackOffice(Map<String, Object> map);

        Observable<Result<List<InquiryRecordPhoto>>> getInquiryRecordPhotoList(Map<String, Object> map);

        Observable<Result<Void>> inquiryRecordUploadPhotos(Map<String, Object> map, MultipartBody.Part part);
    }
}
