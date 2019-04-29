package com.miu360.legworkwrit.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.miu30.common.async.Result;
import com.miu30.common.ui.entity.DriverInfo;
import com.miu30.common.ui.entity.FaGuiDetail;
import com.miu360.legworkwrit.mvp.model.entity.AdministrativePenalty;
import com.miu360.legworkwrit.mvp.model.entity.IllegalDetail;
import com.miu360.legworkwrit.mvp.model.entity.IllegalDetailItem;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;


public interface AdministrativePenaltyContract {

    interface View extends BaseInstrumentContract {
        void notifyAdapter();

        void getID(String id);
    }

    interface Model extends IModel {
        Observable<Result<List<FaGuiDetail>>> getJcxHyRyFgXq(Map<String, Object> map);

        Observable<Result<JSONObject>> submitAdministrativeData(Map<String, Object> map);

        Observable<Result<List<AdministrativePenalty>>> getInitInfo(Map<String, Object> map);

        Observable<Result<List<IllegalDetail>>> getIllegalDetailList(String type);

        Observable<Result<Void>> setInstrumentsFlag(Map<String, Object> map);

        Observable<Result<List<DriverInfo>>> getDriverInfo(Map<String, Object> map);

        Observable<Result<List<IllegalDetailItem>>> getIllegalDetail(Map<String, Object> map);
    }
}
