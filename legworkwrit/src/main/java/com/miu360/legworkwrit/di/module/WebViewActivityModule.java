package com.miu360.legworkwrit.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.mvp.contract.WebViewActivityContract;
import com.miu360.legworkwrit.mvp.model.WebViewActivityModel;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;
import com.miu360.legworkwrit.util.WebViewFactory;


@Module
public class WebViewActivityModule {
    private WebViewActivityContract.View view;

    /**
     * 构建WebViewActivityModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public WebViewActivityModule(WebViewActivityContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    WebViewActivityContract.View provideWebViewActivityView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    WebViewActivityContract.Model provideWebViewActivityModel(WebViewActivityModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    WebViewFactory provideWebViewFactory() {
        return WebViewFactory.create(view.getActivity(), R.id.web_content);
    }

    @ActivityScope
    @Provides
    HeaderHolder provideHeaderHolder() {
        return new HeaderHolder();
    }
}