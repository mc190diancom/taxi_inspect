package com.miu360.legworkwrit.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.base.BaseMvpActivity;
import com.miu30.common.util.UIUtils;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.di.component.DaggerUploadListComponent;
import com.miu360.legworkwrit.di.module.UploadListModule;
import com.miu360.legworkwrit.mvp.contract.UploadListContract;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;
import com.miu360.legworkwrit.mvp.presenter.UploadListPresenter;
import com.miu360.legworkwrit.mvp.ui.adapter.UploadListAdapter;
import com.miu360.legworkwrit.mvp.ui.widget.HeaderHolder;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 上传列表
 */
public class UploadListActivity extends BaseMvpActivity<UploadListPresenter> implements UploadListContract.View {
    private static final String TAG = "Upload";

    private static final int FINISH_CODE = 100;

    @BindView(R2.id.rv_upload_list)
    RecyclerView recyclerView;
    @BindView(R2.id.ll_add)
    LinearLayout llAdd;
    @BindView(R2.id.ll_button_container)
    LinearLayout llButtonContainer;

    @Inject
    HeaderHolder header;

    UploadListAdapter adapter;

    boolean isHistory;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerUploadListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .uploadListModule(new UploadListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_upload_list; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        isHistory = getIntent().getBooleanExtra("history",false);
        if(isHistory){
            header.init(this, "询问笔录列表");
            llAdd.setVisibility(View.GONE);
        }else{
            header.init(this, "上传列表");
            header.rightTextBtn.setText("全部删除");
            header.rightTextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.deleteAllPhotos();
                }
            });
            llAdd.setVisibility(View.VISIBLE);
        }

        initRecyclerView();
        mPresenter.init(self, llButtonContainer);
    }

    private void initRecyclerView() {
        adapter = new UploadListAdapter(isHistory,self,mPresenter.getPhotos()) {
            @Override
            public void delete(InquiryRecordPhoto info, int itemCount) {
                mPresenter.deleteUploadInfo(info);
            }

            @Override
            public void modifyStartTime(InquiryRecordPhoto info) {
                mPresenter.modifyStartTime(info);
            }

            @Override
            public void modifyEndTime(InquiryRecordPhoto info) {
                mPresenter.modifyEndTime(info);
            }
        };

        llAdd.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(self));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == FINISH_CODE) {
                finish();
            }
        }
    }

    @OnClick(R2.id.ll_add)
    public void add() {
        Intent intent = new Intent(self, InquiryRecordActivity.class);
        intent.putParcelableArrayListExtra("infos", mPresenter.getPhotos());
        ActivityUtils.startActivity(intent);
    }

    @OnClick(R2.id.btn_create_case)
    public void createCase() {
        if (mPresenter.getCheckedCount() <= 0) {
            UIUtils.toast(self, "请至少选择一项进行创建案件", Toast.LENGTH_SHORT);
            return;
        }

        Intent intent = new Intent(self, CreateCaseActivity.class);
        intent.putParcelableArrayListExtra("infos", mPresenter.getCheckedInfos());
        startActivityForResult(intent, FINISH_CODE);
    }

    @OnClick(R2.id.btn_save_to_exist)
    public void saveToExist() {
        if (mPresenter.getCheckedCount() <= 0) {
            UIUtils.toast(self, "请至少选择一项进行保存", Toast.LENGTH_SHORT);
            return;
        }

        Intent intent = new Intent(self, CaseListActivity.class);
        intent.putParcelableArrayListExtra("infos", mPresenter.getCheckedInfos());
        intent.putExtra("from", CaseListActivity.FROM_UPLOAD_LIST);
        startActivityForResult(intent, FINISH_CODE);
    }

    @Override
    public void modifyStartTimeSuccess() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void modifyEndTimeSuccess() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void deleteUploadInfoSuccess() {
        adapter.notifyDataSetChanged();
        if (mPresenter.getPhotosCount() >= 3 || isHistory) {
            llAdd.setVisibility(View.GONE);
        } else {
            llAdd.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadUploadInfoSuccess() {
        adapter.notifyDataSetChanged();
        if (mPresenter.getPhotosCount() >= 3 || isHistory) {
            llAdd.setVisibility(View.GONE);
        } else {
            llAdd.setVisibility(View.VISIBLE);
        }
    }

}
