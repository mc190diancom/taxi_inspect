package com.miu360.legworkwrit.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.miu30.common.async.Result;
import com.miu360.legworkwrit.mvp.model.entity.AdministrativePenalty;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarDecideQ;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarFormQ;
import com.miu360.legworkwrit.mvp.model.entity.FristRegisterQ;
import com.miu360.legworkwrit.mvp.model.entity.LiveCheckRecordQ;
import com.miu360.legworkwrit.mvp.model.entity.LiveTranscript;
import com.miu360.legworkwrit.mvp.model.entity.TalkNoticeQ;
import com.miu360.legworkwrit.mvp.model.entity.WifiConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;


public interface WritPrintListContract {

    interface View extends IView {
        void onGetTimes(ArrayList<String> data);

        void getLiveCheckRecordSuccess(LiveCheckRecordQ liveCheckRecordQ);

        void getAdministrativePenaltySuccess(AdministrativePenalty administrativePenalty);

        void getFristRegisterSuccess(FristRegisterQ fristRegisterQ);

        void getDetainCarFormSuccess(DetainCarFormQ detainCarFormQ);

        void getDetainCarDecideSuccess(DetainCarDecideQ detainCarDecideQ);

        void getLiveTranscriptSuccess(LiveTranscript liveTranscript);

        void getTalkNoticeSuccess(TalkNoticeQ talkNoticeQ);

        void modifyPrintTimesSuccess(String instrumentType, String times);
    }

    interface Model extends IModel {
        Observable<Result<List<TalkNoticeQ>>> getTalkNoticeById(Map<String, Object> map);

        Observable<Result<List<LiveCheckRecordQ>>> getLiveCheckRecordById(Map<String, Object> map);

        Observable<Result<List<AdministrativePenalty>>> getAdministrativePenaltyById(Map<String, Object> map);

        Observable<Result<List<FristRegisterQ>>> getFristRegisterById(Map<String, Object> map);

        Observable<Result<List<DetainCarFormQ>>> getDetainCarFormById(Map<String, Object> map);

        Observable<Result<List<DetainCarDecideQ>>> getDetainCarDecideById(Map<String, Object> map);

        Observable<Result<List<LiveTranscript>>> getLiveTranscriptById(Map<String, Object> map);

        Observable<Result<List<WifiConfig>>> getPrinterWifiConfigMsg(Map<String, Object> map);

        Observable<Result<List<WifiConfig>>> getAllPrinterWifiConfig(String type);

        Observable<Result<String>> modifyPrintTimes(Map<String, Object> map);
    }
}
