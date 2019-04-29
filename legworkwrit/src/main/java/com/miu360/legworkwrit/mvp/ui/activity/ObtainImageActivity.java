package com.miu360.legworkwrit.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.blankj.utilcode.util.ActivityUtils;
import com.bumptech.glide.Glide;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.util.UIUtils;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.di.component.DaggerObtainImageComponent;
import com.miu360.legworkwrit.di.module.ObtainImageModule;
import com.miu360.legworkwrit.mvp.contract.ObtainImageContract;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;
import com.miu360.legworkwrit.mvp.presenter.ObtainImagePresenter;
import com.miu30.common.util.FileUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * 拍照或者从相册选取照片
 */
public class ObtainImageActivity extends BaseMvpActivity<ObtainImagePresenter> implements ObtainImageContract.View {
    private static final int TAKE_PHOTO = 0x0001;
    private static final int CHOOSE_PHOTO = 0x0002;

    @BindView(R2.id.view_switcher)
    ViewSwitcher switcher;
    @BindView(R2.id.iv_image)
    ImageView ivImage;
    @BindView(R2.id.tv_upload_state)
    TextView tvUploadSate;
    @BindView(R2.id.ibtn_upload)
    ImageButton ibtnUpload;

    private File photoFile;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerObtainImageComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .obtainImageModule(new ObtainImageModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_obtain_image; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        assert mPresenter != null;
        mPresenter.init(self);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_PHOTO) {
                display();
            } else if (requestCode == CHOOSE_PHOTO) {
                if (data != null && data.getData() != null) {
                    String photoPath = FileUtil.getPathFromUri(self, data.getData());
                    if (!TextUtils.isEmpty(photoPath)) {
                        photoFile = new File(photoPath);
                        display();
                    }
                }
            }
        }
    }

    private void display() {
        if (photoFile != null && photoFile.exists()) {
            Glide.with(self).load(photoFile).into(ivImage);
            if (switcher.getDisplayedChild() == 0) {
                switcher.showNext();
            }
        }
    }

    @SuppressLint("CheckResult")
    @OnClick(R2.id.ibtn_take_photo)
    public void takePhoto() {
        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            photoFile = new File(FileUtil.getDir(Environment.getExternalStorageDirectory(), "taxi_inspect")
                                    , System.currentTimeMillis() + ".png");
                            mPresenter.takePhoto(self, TAKE_PHOTO, photoFile);
                        } else {
                            showMessage("权限被拒绝,无法进行拍照");
                        }
                    }
                });
    }

    @SuppressLint("CheckResult")
    @OnClick(R2.id.ibtn_album)
    public void choosePhoto() {
        new RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            mPresenter.choosePhoto(self, CHOOSE_PHOTO);
                        } else {
                            showMessage("权限被拒绝,无法选取照片");
                        }
                    }
                });
    }

    @OnClick(R2.id.ibtn_back)
    public void showPreview() {
        if (switcher.getDisplayedChild() == 1) {
            switcher.showPrevious();
        }
    }

    @OnClick(R2.id.ibtn_upload)
    public void upload() {
        ibtnUpload.setClickable(false);
        mPresenter.upload(photoFile);
    }

    @Override
    public void uploadSuccess() {
        UIUtils.toast(self, "上传成功", Toast.LENGTH_SHORT);
        goUploadList();
    }

    @Override
    public void uploadFailure() {
        ibtnUpload.setClickable(true);
        tvUploadSate.setText("重新上传");
    }

    @Override
    public void saveSuccess(InquiryRecordPhoto info) {
        UIUtils.toast(self, "保存成功", Toast.LENGTH_SHORT);
        goUploadList();
    }

    private void goUploadList() {
        ibtnUpload.setClickable(true);
        ActivityUtils.finishActivity(UploadListActivity.class);
        setResult(RESULT_OK);
        Intent intent = new Intent(self, UploadListActivity.class);
        ActivityUtils.startActivity(intent);
        finish();
    }

    @Override
    public void saveFailure() {
        ibtnUpload.setClickable(true);
        UIUtils.toast(self, "保存失败", Toast.LENGTH_SHORT);
        tvUploadSate.setText("重新上传");
    }
}
