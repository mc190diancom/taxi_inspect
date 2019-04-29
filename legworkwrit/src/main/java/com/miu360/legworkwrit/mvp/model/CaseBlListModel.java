package com.miu360.legworkwrit.mvp.model;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.miu30.common.async.Result;
import com.miu360.legworkwrit.mvp.contract.CaseBlListContract;
import com.miu360.legworkwrit.mvp.model.api.service.MyApis;
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

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MultipartBody;


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
@ActivityScope
public class CaseBlListModel extends BaseModel implements CaseBlListContract.Model {

    @Inject
    public CaseBlListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Result<List<TalkNoticeQ>>> getTalkNoticeById(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getTalkNotice(map);
    }

    @Override
    public Observable<Result<List<LiveCheckRecordQ>>> getLiveCheckRecordById(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getInitLiveCheckRecord(map);
    }

    @Override
    public Observable<Result<List<AdministrativePenalty>>> getAdministrativePenaltyById(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getAdministrativePenalty(map);
    }

    @Override
    public Observable<Result<List<FristRegisterQ>>> getFristRegisterById(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getFristRegister(map);
    }

    @Override
    public Observable<Result<List<DetainCarFormQ>>> getDetainCarFormById(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getDetainCarForm(map);
    }

    @Override
    public Observable<Result<List<DetainCarDecideQ>>> getDetainCarDecideById(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getDetainCarDecide(map);
    }

    @Override
    public Observable<Result<List<LiveTranscript>>> getLiveTranscriptById(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getLiveTranscript(map);
    }

    @Override
    public Observable<Result<List<InquiryRecordPhoto>>> getInquiryRecordPhotoList(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(MyApis.class).getInquiryRecordPhotoList(map);
    }

}