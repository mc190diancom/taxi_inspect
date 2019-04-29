package com.miu360.legworkwrit.mvp.contract;

import android.app.Activity;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.miu30.common.async.Result;
import com.miu360.legworkwrit.mvp.model.entity.AdministrativePenalty;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarDecideQ;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarFormQ;
import com.miu360.legworkwrit.mvp.model.entity.FristRegisterQ;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;
import com.miu360.legworkwrit.mvp.model.entity.LiveCheckRecordQ;
import com.miu360.legworkwrit.mvp.model.entity.LiveTranscript;
import com.miu360.legworkwrit.mvp.model.entity.TalkNoticeQ;

import java.util.List;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;

import io.reactivex.Observable;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/20/2018 11:31
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public interface CaseBlListContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void getLiveCheckRecordSuccess(LiveCheckRecordQ liveCheckRecordQ);

        void getAdministrativePenaltySuccess(AdministrativePenalty administrativePenalty);

        void getFristRegisterSuccess(FristRegisterQ fristRegisterQ);

        void getDetainCarFormSuccess(DetainCarFormQ detainCarFormQ);

        void getDetainCarDecideSuccess(DetainCarDecideQ detainCarDecideQ);

        void getLiveTranscriptSuccess(LiveTranscript liveTranscript);

        void getTalkNoticeSuccess(TalkNoticeQ talkNoticeQ);

        void getPhotoListSuccess();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<Result<List<TalkNoticeQ>>> getTalkNoticeById(Map<String, Object> map);

        Observable<Result<List<LiveCheckRecordQ>>> getLiveCheckRecordById(Map<String, Object> map);

        Observable<Result<List<AdministrativePenalty>>> getAdministrativePenaltyById(Map<String, Object> map);

        Observable<Result<List<FristRegisterQ>>> getFristRegisterById(Map<String, Object> map);

        Observable<Result<List<DetainCarFormQ>>> getDetainCarFormById(Map<String, Object> map);

        Observable<Result<List<DetainCarDecideQ>>> getDetainCarDecideById(Map<String, Object> map);

        Observable<Result<List<LiveTranscript>>> getLiveTranscriptById(Map<String, Object> map);

        Observable<Result<List<InquiryRecordPhoto>>> getInquiryRecordPhotoList(Map<String, Object> map);
    }
}
