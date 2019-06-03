package com.miu30.common.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.delegate.IFragment;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.integration.cache.CacheType;
import com.jess.arms.integration.lifecycle.FragmentLifecycleable;
import com.jess.arms.mvp.IPresenter;
import com.jess.arms.mvp.IView;
import com.jess.arms.utils.ArmsUtils;
import com.miu30.common.util.MyProgressDialog;
import com.miu30.common.util.Windows;
import com.trello.rxlifecycle2.android.FragmentEvent;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.miu30.common.MiuBaseApp.self;

/**
 * 作者：wanglei on 2019/6/3.
 * 邮箱：forwlwork@gmail.com
 */
public abstract class BaseMvpFragment<P extends IPresenter> extends Fragment implements IView, IFragment, FragmentLifecycleable {
    private MyProgressDialog pd;
    private Unbinder unbinder;

    protected final String TAG = this.getClass().getSimpleName();
    private final BehaviorSubject<FragmentEvent> mLifecycleSubject = BehaviorSubject.create();
    private Cache<String, Object> mCache;
    @Inject
    @Nullable
    protected P mPresenter;//如果当前页面逻辑简单, Presenter 可以为 null

    @NonNull
    @Override
    public synchronized Cache<String, Object> provideCache() {
        if (mCache == null) {
            mCache = ArmsUtils.obtainAppComponentFromContext(getActivity()).cacheFactory().build(CacheType.FRAGMENT_CACHE);
        }
        return mCache;
    }

    @NonNull
    @Override
    public final Subject<FragmentEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = initView(inflater, container, savedInstanceState);
        if (rootView != null) {
            unbinder = ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    public void showLoading() {
        if (pd == null) {
            pd = Windows.waiting(self);
        } else if (!pd.isShowing()) {
            pd.show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null && unbinder != Unbinder.EMPTY) {
            unbinder.unbind();
        }

        if (mPresenter != null) {
            mPresenter.onDestroy();
        }

        mPresenter = null;
        unbinder = null;
    }

    @Override
    public void hideLoading() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {

    }

    @Override
    public void setData(@Nullable Object data) {

    }

    /**
     * 是否使用eventBus,默认为使用(true)，
     */
    @Override
    public boolean useEventBus() {
        return true;
    }
}
