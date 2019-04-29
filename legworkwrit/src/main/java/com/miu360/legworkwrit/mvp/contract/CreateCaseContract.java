package com.miu360.legworkwrit.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.miu30.common.async.Result;
import com.miu30.common.ui.entity.queryZFRYByDWMC;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.District;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;


public interface CreateCaseContract {

    interface View extends IView {
        void showTime(String time);

        void showPunishType(String type);

        void onReturnCurrentZFRY(queryZFRYByDWMC zfry);

        void onCreateSuccess(Case c);

        void uploadPhotosSuccess();

        void showZfry2(queryZFRYByDWMC zfry);
    }

    interface Model extends IModel {
        Observable<Result<List<queryZFRYByDWMC>>> getCheZuList(Map<String, Object> map);

        Observable<Result<JSONObject>> createCase(Map<String, Object> map);

        Observable<Result<List<District>>> getDistrictInfo(String type);

        Observable<Result<Void>> inquiryRecordUploadPhotos(Map<String, Object> map, MultipartBody.Part part);
    }
}
