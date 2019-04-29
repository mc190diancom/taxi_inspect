package com.miu360.legworkwrit.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.miu30.common.async.Result;
import com.miu360.legworkwrit.mvp.model.entity.District;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;

import io.reactivex.Observable;


public interface CaseBasicContract {

    interface View extends IView {

    }

    interface Model extends IModel {
        Observable<Result<List<District>>> getDistrictInfo(String type);
        Observable<Result<JSONObject>> updateCase(Map<String, Object> map);
        Observable<Result<Void>> setInstrumentsFlag(Map<String, Object> map);
    }
}
