package com.miu360.legworkwrit.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.miu30.common.async.Result;
import com.miu30.common.ui.entity.DriverInfo;
import com.miu30.common.ui.entity.FaGuiDetail;
import com.miu30.common.ui.entity.JCItem;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu360.legworkwrit.mvp.model.entity.District;
import com.miu360.legworkwrit.mvp.model.entity.LiveCheckRecordQ;
import com.miu360.legworkwrit.mvp.model.entity.UTC;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;

import io.reactivex.Observable;


public interface LiveCheckRecordContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends BaseInstrumentContract {
        void onGetVehicleInfo(VehicleInfo vehicleInfo);

        void notifyAdapter(boolean isCar);

        void getID(String id);

        void verifyTimeFinish(boolean result);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<Result<List<JCItem>>> getJclb(Map<String, Object> map);

        Observable<Result<List<District>>> getDistrictInfo(String type);

        Observable<Result<List<VehicleInfo>>> getVehicleInfo(Map<String, Object> map);

        Observable<Result<JSONObject>> submitLiveRecordInfo(Map<String, Object> map);

        Observable<Result<List<FaGuiDetail>>> getJcxHyRyFgXq(Map<String, Object> map);

        Observable<Result<List<LiveCheckRecordQ>>> getInitInfo(Map<String, Object> map);

        Observable<Result<Void>> setInstrumentsFlag(Map<String, Object> map);

        Observable<Result<List<UTC>>> getBlTime(Map<String, Object> map);


        Observable<Result<List<DriverInfo>>> getDriverInfo(Map<String, Object> map);
    }
}
