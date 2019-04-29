package com.miu360.legworkwrit.di.module;

import android.app.Activity;

import com.jess.arms.di.scope.ActivityScope;
import com.miu360.legworkwrit.mvp.contract.CaseBlListContract;
import com.miu360.legworkwrit.mvp.model.CaseBlListModel;
import com.miu360.legworkwrit.mvp.model.entity.CaseStatus;
import com.miu360.legworkwrit.mvp.ui.adapter.ChooseInstrumentAdapter;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/20/2018 11:32
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public class CaseBlListModule {

    private CaseBlListContract.View view;
    private Activity activity;

    /**
     * 构建CaseListModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     */
    public CaseBlListModule(CaseBlListContract.View view,Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    @ActivityScope
    @Provides
    CaseBlListContract.View provideCaseBlListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CaseBlListContract.Model provideCaseBlListModel(CaseBlListModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    ChooseInstrumentAdapter provideChooseInstrumentAdatper(ArrayList<CaseStatus> caseStatus) {
        return new ChooseInstrumentAdapter(caseStatus, activity);
    }

    @ActivityScope
    @Provides
    ArrayList<CaseStatus> provideCaseStatusList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    HeaderHolder providesHeader() {
        return new HeaderHolder();
    }
}