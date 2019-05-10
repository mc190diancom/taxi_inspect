package com.miu360.legworkwrit.mvp.presenter;

import android.text.TextUtils;
import android.view.View;

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.miu30.common.MiuBaseApp;
import com.miu30.common.async.Result;
import com.miu30.common.base.BaseData;
import com.miu30.common.config.Config;
import com.miu30.common.ui.entity.JCItem;
import com.miu30.common.app.MyErrorHandleSubscriber;
import com.miu360.legworkwrit.app.utils.RxUtils;
import com.miu360.legworkwrit.mvp.contract.IllegalDetailActivityContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.data.IllegalHistoryPreference;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.IllegalDetailParams;
import com.miu360.legworkwrit.mvp.model.entity.JCItemWrapper;
import com.miu360.legworkwrit.mvp.ui.adapter.IllegalDetailAdapter;
import com.miu360.legworkwrit.util.MapUtil;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class IllegalDetailActivityPresenter extends BasePresenter<IllegalDetailActivityContract.Model, IllegalDetailActivityContract.View> {
    public static final String ORIGINAL = "original";
    public static final String FILTER = "filter";

    @Inject
    RxErrorHandler mErrorHandler;

    //去重的数据，用于展示
    @Inject
    List<JCItem> arrayList;

    //未去重的数据
    private List<JCItem> allData = new ArrayList<>();

    @Inject
    IllegalDetailAdapter illegalDetailAdapter;


    private IllegalHistoryPreference history = new IllegalHistoryPreference(MiuBaseApp.self);

    @Inject
    public IllegalDetailActivityPresenter(IllegalDetailActivityContract.Model model, IllegalDetailActivityContract.View rootView) {
        super(model, rootView);

    }

    public void initListener() {
        illegalDetailAdapter.setOutSideOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener<JCItem>() {
            @Override
            public void onItemClick(View view, int viewType, JCItem data, int position) {
                setResult(data);
            }
        });
    }

    public void setResult(final JCItem data) {
        mRootView.showLoading();

        Observable
                .create(new ObservableOnSubscribe<List<String>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                        List<String> illegalSituation = new ArrayList<>(arrayList.size());

                        for (JCItem item : allData) {
                            if (!TextUtils.isEmpty(data.getLBMC()) && data.getLBMC().equals(item.getLBMC())) {
                                illegalSituation.add(item.getXMMC());
                            }
                        }

                        illegalSituation.add(0, "无");

                        history.putIllegalHistory(CacheManager.getInstance().getCase().getHYLB()
                                , CacheManager.getInstance().getCase().getCFFS()
                                , data);
                        emitter.onNext(illegalSituation);
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<String> list) {
                        EventBus.getDefault().post(new JCItemWrapper(data, list), Config.ILLEGAL);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mRootView.hideLoading();
                        mRootView.getActivity().finish();
                    }
                });
    }

    public void getIllegalDetailList() {
        Case c = CacheManager.getInstance().getCase();
        if (c == null) {
            return;
        }

        IllegalDetailParams params = new IllegalDetailParams();
        params.setHYLB(c.getHYLB());
        if ("警告".equals(c.getCFFS())) {
            params.setCFBZ("警告");
        } else {
            params.setCFBZ("");
        }

        Map<String, Object> map = new MapUtil().getMap("selectJcxByJg", BaseData.gson.toJson(params));
        mModel.getIllegalDetail(map)
                .compose(RxUtils.<Result<List<JCItem>>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<List<JCItem>>>(mErrorHandler) {
                    @Override
                    public void onNextResult(Result<List<JCItem>> listResult) throws Exception {
                        if (listResult.ok()) {
                            List<JCItem> data = listResult.getData();
                            allData.addAll(listResult.getData());
                            //利用 HashSet 去除违法行为重复的内容
                            HashSet<JCItem> remove = new HashSet<>(data.size());
                            remove.addAll(data);
                            arrayList.addAll(remove);

                            data.clear();
                            illegalDetailAdapter.notifyDataSetChanged();
                            mRootView.getIllegalDetailSuccess(arrayList);
                        } else {
                            mRootView.showMessage(listResult.getMsg());
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }

    public void initIllegalHistory() {
        List<JCItem> items = history.getIllegalHistory(CacheManager.getInstance().getCase().getHYLB()
                , CacheManager.getInstance().getCase().getCFFS());

        if (items != null) {
            mRootView.getIllegalHistorySuccess(items);
        }
    }
}
