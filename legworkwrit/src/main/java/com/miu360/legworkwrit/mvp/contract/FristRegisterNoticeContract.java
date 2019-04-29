package com.miu360.legworkwrit.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.miu30.common.async.Result;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu360.legworkwrit.mvp.model.entity.AgencyInfo;
import com.miu360.legworkwrit.mvp.model.entity.FristRegisterQ;
import com.miu360.legworkwrit.mvp.model.entity.Park;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;


public interface FristRegisterNoticeContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends BaseInstrumentContract {
        void showSaveWay(String saveWay);

        void showCarOwner(String carOwner);

        void showChooseCarMsg(List<String> carMsg);

        void showChooseCardMsg(List<String> cardMsg);

        void onGetVehicleInfo(VehicleInfo vehicleInfo);

        void notifyAdapter();

        void getID(String id);

        void setPark(Park park);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<Result<List<Park>>> getParkList(String type);

        Observable<Result<JSONObject>> addFristRegister(Map<String, Object> map);

        Observable<Result<List<FristRegisterQ>>> getInitInfo(Map<String, Object> map);

        Observable<Result<List<AgencyInfo>>> getAgencyInfos(String type);

        Observable<Result<Void>> setInstrumentsFlag(Map<String, Object> map);

        Observable<Result<List<VehicleInfo>>> getVehicleInfo(Map<String, Object> map);

        Observable<Result<List<AgencyInfo>>> getAgencyInfosByZFZH(Map<String, Object> map);
    }
}
