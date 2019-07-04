package com.miu360.legworkwrit.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.MiuBaseApp;
import com.miu30.common.base.BaseMvpActivity;
import com.miu30.common.util.UIUtils;
import com.miu30.common.util.Windows;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.di.component.DaggerCaseListComponent;
import com.miu360.legworkwrit.di.module.CaseListModule;
import com.miu360.legworkwrit.mvp.contract.CaseListContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.data.PhotoPreference;
import com.miu360.legworkwrit.mvp.model.entity.BlType;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.presenter.CaseListPresenter;
import com.miu360.legworkwrit.mvp.ui.adapter.CaseListAdapter;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;
import com.miu360.legworkwrit.util.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 案件列表,有三个入口
 * 1、从上传列表进入，其中CheckBox只能单选
 * 2、从主页进入，其中CheckBox可以多选
 * 3、从文件打印进入，其中CheckBox只能单选
 */
public class CaseListActivity extends BaseMvpActivity<CaseListPresenter> implements CaseListContract.View, CaseListAdapter.CommonListener {
    public static final int FROM_UPLOAD_LIST = 1;
    public static final int FROM_HOME = 2;
    public static final int FROM_PRINTER = 3;

    @BindView(R2.id.case_list)
    RecyclerView caseList;
    @BindView(R2.id.btn_common)
    Button btnCommon;

    CaseListAdapter adapter;

    @Inject
    HeaderHolder header;
    @Inject
    List<Case> cases;

    private int from;
    private List<Case> checkedCases;
    private boolean isPrint = false;

    private EndlessRecyclerOnScrollListener mScrollListener;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCaseListComponent
                .builder()
                .appComponent(appComponent)
                .caseListModule(new CaseListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_case_list;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        header.init(self, "案件列表");
        initFrom();
    }

    private void initFrom() {
        btnCommon.setClickable(false);
        btnCommon.setBackgroundResource(R.drawable.bg_unclick_button_shape);

        from = getIntent().getIntExtra("from", FROM_HOME);
        if (from == FROM_PRINTER) {
            btnCommon.setText("打印");
            adapter = new CaseListAdapter(cases, true, true);
        } else if (from == FROM_UPLOAD_LIST) {
            btnCommon.setText("确定");
            adapter = new CaseListAdapter(cases, true, true);
        } else {
            btnCommon.setText("送内勤");
            adapter = new CaseListAdapter(cases, true, true);
        }

        initRecyclerView();

        assert mPresenter != null;
        mPresenter.init(self);
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(self);
        mScrollListener = new EndlessRecyclerOnScrollListener(manager) {
            @Override
            public void onLoadMore(int current_page) {
                if (cases.size() >= 20) {
                    mPresenter.getCaseListData(current_page);
                }
            }
        };
        caseList.addOnScrollListener(mScrollListener);
        adapter.setHasStableIds(true);
        caseList.setLayoutManager(manager);
        caseList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        caseList.setAdapter(adapter);
        adapter.setCommonListener(this);
    }

    @Override
    public void getCaseListSuccess(List<Case> wrappers, int page) {
        if (1 == page) {
            cases.clear();
        }
        cases.addAll(wrappers);
        adapter.notifyDataSetChanged();
        mScrollListener.setLoading(false);
    }

    @Override
    public void getCaseBlTypeSuccess(Case c, ArrayList<BlType> types) {
        Intent intent;
        CacheManager.getInstance().putPrintTimes(types);
        if(!TextUtils.isEmpty(c.getSFZH())){
            CacheManager.getInstance().putSFZH(c.getSFZH());
        }
        if (isPrint) {
            if (types != null && types.size() > 0) {
                CacheManager.getInstance().putPrintTimes(types);
                intent = new Intent(self, WritPrintListActivity.class);
                intent.putExtra("case", c);
                intent.putParcelableArrayListExtra("bl_types", types);
            } else {
                UIUtils.toast(self, "该案件未选择填写任何文书", Toast.LENGTH_SHORT);
                return;
            }
        } else {
            CacheManager.getInstance().putCase(c);
            if ("警告".equals(c.getCFFS())) {
                intent = new Intent(self, SimpleProcessActivity.class);
            } else {
                intent = new Intent(self, NormalProcessActivity.class);
            }
            intent.putExtra("isZFRY2", MiuBaseApp.user.getString("user_name", "").equals(c.getZHZH2()));
            intent.putParcelableArrayListExtra("bl_types", types);
        }

        ActivityUtils.startActivity(intent);
    }

    @Override
    public void uploadPhotosSuccess() {
        setResult(RESULT_OK);
        new PhotoPreference().clearPreference();
        UIUtils.toast(self, "保存成功", Toast.LENGTH_SHORT);
        finish();
    }

    @OnClick(R2.id.btn_common)
    public void btnCommonClick() {
        if (from == FROM_UPLOAD_LIST) {
            mPresenter.getCaseBlTimeFromUpload(checkedCases.get(0));
        } else if (from == FROM_PRINTER) {
            isPrint = true;
            mPresenter.getCaseBlTime(checkedCases.get(0), false);
        } else {
            Windows.confirm(self, "是否确认送内勤？", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //送内勤
                    mPresenter.getCaseBlTime(checkedCases.get(0), true);
                }
            }, new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                }
            });

        }
    }

    @Override
    public void onGoNext(Case c) {
        isPrint = false;
        mPresenter.getCaseBlTime(c, false);
    }

    @Override
    public void onCheckCases(List<Case> cases) {
        this.checkedCases = cases;

        if (cases != null && cases.size() > 0) {
            btnCommon.setClickable(true);
            btnCommon.setBackgroundResource(R.drawable.bg_common_button_shape);
        } else {
            btnCommon.setClickable(false);
            btnCommon.setBackgroundResource(R.drawable.bg_unclick_button_shape);
        }
    }
}
