package com.miu360.legworkwrit.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.miu30.common.async.Result;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu360.legworkwrit.mvp.model.entity.AccordRule;
import com.miu360.legworkwrit.mvp.model.entity.AgencyInfo;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarForm;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarFormQ;
import com.miu360.legworkwrit.mvp.model.entity.Park;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;


public interface DetainCarFormContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends BaseInstrumentContract {
        void getVehicleInfo(VehicleInfo info);

        void onCreateSuccess(DetainCarForm detainCarForm);

        void onUpdateSuccess(DetainCarForm detainCarForm);

        void uploadSuccess();

        void uploadFailure(String msg);

        void showPhotos(List<String> photosIds);

        void notifyAdapter();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<Result<List<Park>>> getParkList(String type);

        Observable<Result<List<AccordRule>>> getAccordRuleList(String type);

        Observable<Result<List<VehicleInfo>>> getVehicleInfo(Map<String, Object> map);

        Observable<Result<JSONObject>> addDetainCarForm(Map<String, Object> map);

        Observable<Result<Void>> detainCarFormUploadPhotos(Map<String, Object> map, List<MultipartBody.Part> parts);

        Observable<Result<List<AgencyInfo>>> getAgencyInfos(String type);

        Observable<Result<List<DetainCarFormQ>>> getInitInfo(Map<String, Object> map);

        Observable<Result<Void>> setInstrumentsFlag(Map<String, Object> map);

    }
}
