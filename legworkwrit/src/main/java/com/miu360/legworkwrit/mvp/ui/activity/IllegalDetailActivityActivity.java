package com.miu360.legworkwrit.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.base.BaseMvpActivity;
import com.miu30.common.ui.entity.JCItem;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.di.component.DaggerIllegalDetailActivityComponent;
import com.miu360.legworkwrit.di.module.IllegalDetailActivityModule;
import com.miu360.legworkwrit.mvp.contract.IllegalDetailActivityContract;
import com.miu360.legworkwrit.mvp.presenter.IllegalDetailActivityPresenter;
import com.miu360.legworkwrit.mvp.ui.adapter.IllegalDetailAdapter;
import com.miu360.legworkwrit.mvp.ui.adapter.IllegalHistoryAdapter;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


public class IllegalDetailActivityActivity extends BaseMvpActivity<IllegalDetailActivityPresenter> implements IllegalDetailActivityContract.View {

    @BindView(R2.id.et_search)
    EditText etSearch;

    @BindView(R2.id.ll_search)
    LinearLayout llSearch;

    @Inject
    HeaderHolder headerHolder;
    @BindView(R2.id.rv_illegal)
    RecyclerView rvIllegal;
    @BindView(R2.id.lv_history)
    ListView lvHistory;
    @Inject
    LinearLayoutManager mLayoutManager;

    @Inject
    IllegalDetailAdapter illegalDetailAdapter;

    List<JCItem> filterList;

    IllegalDetailAdapter filterAdapter;
    @BindView(R2.id.btn_sarch)
    Button btnSarch;

    private int mode = 1;
    private static final int SELECT_MODE = 1;
    private static final int SEARCH_MODE = 2;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerIllegalDetailActivityComponent
                .builder()
                .appComponent(appComponent)
                .illegalDetailActivityModule(new IllegalDetailActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_illegal_detail;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initView();
        assert mPresenter != null;
        mPresenter.getIllegalDetailList();
    }

    private void initView() {
        initHeader();
        initRecycleView();
        initFilterView();
        assert mPresenter != null;
        mPresenter.initListener();
        mPresenter.initIllegalHistory();
        doSearch();
    }

    private void initHeader() {
        headerHolder.init(this, "涉嫌违法事由");
        headerHolder.setUpRightTextBtn("搜索", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchView(View.VISIBLE);
                mode = SEARCH_MODE;
            }
        });
        headerHolder.leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        if (mode == SELECT_MODE) {
            finish();
        } else {
            rvIllegal.setAdapter(illegalDetailAdapter);
            showSearchView(View.GONE);
            mode = SELECT_MODE;
        }
    }

    @SuppressLint("CheckResult")
    private void doSearch() {
        RxView.clicks(btnSarch)
                .debounce(200, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .map(new Function<Object, List<JCItem>>() {
                    @Override
                    public List<JCItem> apply(Object o) throws Exception {
                        filterList.clear();
                        String trim = etSearch.getText().toString().trim();
                        if (TextUtils.isEmpty(trim)) {
                            rvIllegal.setAdapter(illegalDetailAdapter);
                            return filterList;
                        }
                        List<JCItem> infos = illegalDetailAdapter.getInfos();
                        for (JCItem info : infos) {
                            if (info.getLBMC().contains(trim)) {
                                filterList.add(info);
                            }
                        }
                        return filterList;
                    }

                }).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<JCItem>>() {
            @Override
            public void accept(List<JCItem> jcItems) throws Exception {
                lvHistory.setVisibility(View.GONE);
                rvIllegal.setAdapter(filterAdapter);
            }
        });
    }

    private void initRecycleView() {
        rvIllegal.setLayoutManager(mLayoutManager);
        rvIllegal.setItemAnimator(new DefaultItemAnimator());
        rvIllegal.setAdapter(illegalDetailAdapter);
    }

    private void initFilterView() {
        filterList = new ArrayList<>();
        filterAdapter = new IllegalDetailAdapter(filterList, true);
        filterAdapter.setOutSideOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener<JCItem>() {
            @Override
            public void onItemClick(View view, int viewType, JCItem data, int position) {
                assert mPresenter != null;
                mPresenter.setResult(data);
            }
        });
    }

    private void showSearchView(int visible) {
        llSearch.setVisibility(visible);
        lvHistory.setVisibility(visible);
    }

    @OnClick({R2.id.ib_left_btn_search_mode})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.ib_left_btn_search_mode) {
            back();
        }

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void getIllegalDetailSuccess(List<JCItem> data) {

    }

    @Override
    public void getIllegalHistorySuccess(final List<JCItem> data) {
        lvHistory.setAdapter(new IllegalHistoryAdapter(this, data));
        lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                assert mPresenter != null;
                mPresenter.setResult(data.get(position));
            }
        });
    }

}
