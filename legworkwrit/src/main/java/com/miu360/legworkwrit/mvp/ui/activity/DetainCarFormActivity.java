package com.miu360.legworkwrit.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jess.arms.di.component.AppComponent;
import com.miu30.common.base.BaseData;
import com.miu30.common.config.Config;
import com.miu30.common.ui.LawSelectLocationActivity;
import com.miu30.common.ui.entity.AddMsg;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu30.common.util.FileUtil;
import com.miu30.common.util.FullListView;
import com.miu30.common.util.MyProgressDialog;
import com.miu30.common.util.UIUtils;
import com.miu30.common.util.Windows;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.R2;
import com.miu360.legworkwrit.di.component.DaggerDetainCarFormComponent;
import com.miu360.legworkwrit.di.module.DetainCarFormModule;
import com.miu360.legworkwrit.mvp.contract.DetainCarFormContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarForm;
import com.miu360.legworkwrit.mvp.model.entity.DetainCarFormQ;
import com.miu360.legworkwrit.mvp.model.entity.ParentQ;
import com.miu360.legworkwrit.mvp.model.entity.PictureID;
import com.miu360.legworkwrit.mvp.presenter.DetainCarFormPresenter;
import com.miu360.legworkwrit.mvp.ui.adapter.CarInfoAdapter;
import com.miu360.legworkwrit.util.DialogUtil;
import com.miu360.legworkwrit.util.GetUTCUtil;
import com.miu360.legworkwrit.util.InputFilterUtil;
import com.miu360.legworkwrit.util.PictureUtil;
import com.miu360.legworkwrit.util.TimeTool;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.simple.eventbus.Subscriber;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 扣押车辆交接单
 */
public class DetainCarFormActivity extends BaseInstrumentActivity<DetainCarFormPresenter> implements DetainCarFormContract.View, AdapterView.OnItemClickListener {
    @BindView(R2.id.tv_four_corner_photo_drop)
    TextView tvFourCornerPhotoDrop;                         //四角照片
    @BindView(R2.id.tv_car_kilometers_drop)
    TextView tvCarKilometersDrop;                           //车辆里程数
    @BindView(R2.id.tv_frame_number_drop)
    TextView tvFrameNumberDrop;                             //车架号

    @BindView(R2.id.ll_four_corner_photo)
    LinearLayout llFourCornerPhoto;
    @BindView(R2.id.ll_car_kilometers)
    LinearLayout llCarKilometers;
    @BindView(R2.id.ll_frame_number)
    LinearLayout llFrameNumber;

    @BindView(R2.id.line_corner_photo_drop)
    View lineCornerPhotoDrop;
    @BindView(R2.id.line_car_kilometers_drop)
    View lineCarKilometersDrop;
    @BindView(R2.id.line_frame_number_drop)
    View lineFrameNumberDrop;

    //车主姓名
    @BindView(R2.id.et_driver_name)
    @NotEmpty
    EditText etDriverName;
    //车牌号
    @BindView(R2.id.et_car_license)
    @NotEmpty
    EditText etCarLicense;
    //车身颜色
    @BindView(R2.id.et_car_color)
    @NotEmpty
    EditText etCarColor;
    //车型
    @BindView(R2.id.et_car_model)
    @NotEmpty
    EditText etCarModel;
    //厂牌型号
    @BindView(R2.id.et_brand_model)
    @NotEmpty
    EditText etBrandModel;
    //车架号
    @BindView(R2.id.et_car_frame)
    @NotEmpty
    EditText etCarFrame;
    //车公里数
    @BindView(R2.id.et_car_kilometers)
    @NotEmpty
    EditText etCarKilometers;

    //顶灯
    @BindView(R2.id.tv_top_light)
    @NotEmpty(message = "请选择顶灯数量")
    TextView tvTopLight;
    //计价器
    @BindView(R2.id.tv_price_meter)
    @NotEmpty(message = "请选择计价器数量")
    TextView tvPriceMeter;
    //空车票
    @BindView(R2.id.tv_empty_ticket)
    @NotEmpty(message = "请选择空车票数量")
    TextView tvEmptyTicket;
    //假证件
    @BindView(R2.id.tv_false_certificate)
    @NotEmpty(message = "请选择假证件数量")
    TextView tvFalseCertificate;
    //票据
    @BindView(R2.id.tv_bill)
    @NotEmpty(message = "请选择票据数量")
    TextView tvBill;
    //私人物品
    @BindView(R2.id.et_personal_belong)
    @NotEmpty
    EditText etPersonalBelong;
    //依据条例
    @BindView(R2.id.et_accord_rule)
    @NotEmpty
    EditText etAccordRule;
    //押送员1
    @BindView(R2.id.et_escort1)
    @NotEmpty
    EditText etEscort1;
    //押送员2
    @BindView(R2.id.et_escort2)
    @NotEmpty
    EditText etEscort2;

    //扣车时间
    @BindView(R2.id.tv_deducation_time)
    @NotEmpty(message = "请确认扣车时间")
    TextView tvDeducationTime;
    //扣车地址
    @BindView(R2.id.tv_deducation_address)
    @NotEmpty
    TextView tvDeducationAddress;
    //押送时间
    @BindView(R2.id.tv_escort_time)
    @NotEmpty(message = "请确认押送时间")
    TextView tvEscortTime;
    //存放车辆停车场
    @BindView(R2.id.tv_storage_car_park)
    @NotEmpty
    TextView tvStorageCarPark;

    //车辆前侧照片
    @BindView(R2.id.iv_car_front)
    ImageView ivCarFront;
    //车辆后侧照片
    @BindView(R2.id.iv_car_after)
    ImageView ivCarAfter;
    //车辆左侧照片
    @BindView(R2.id.iv_car_left)
    ImageView ivCarLeft;
    //车辆右侧照片
    @BindView(R2.id.iv_car_right)
    ImageView ivCarRight;
    //车辆里程数照片
    @BindView(R2.id.iv_car_kilometers)
    ImageView ivCarKilometers;
    //车架号照片
    @BindView(R2.id.iv_car_frame_number)
    ImageView ivCarFrameNumber;
    @BindView(R2.id.list_show_car_info)
    FullListView listShowCarInfo;
    @BindView(R2.id.ib_shaomiao_frame)
    ImageButton ibShaomiaoFrame;
    @BindView(R2.id.ib_shaomiao_kilometers)
    ImageButton ibShaomiaoKilometers;

    @Inject
    CarInfoAdapter mAdapter;

    //当前点击的ImageView
    private ImageView currentClickImageView;
    //当前图片
    private File currentPhoto;
    private DetainCarForm detainCarForm;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerDetainCarFormComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .detainCarFormModule(new DetainCarFormModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_detain_car_form; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        instrumentType = Config.T_CARFORM;
        header.init(self, "扣押车辆交接单");
        super.initData(savedInstanceState);
        etDriverName.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterChineseRegex, 10));
        etCarLicense.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterNumberChineseRegex, 8));
        etCarColor.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.chineseRegex, 6));
        etPersonalBelong.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterChineseRegex, 20));
        etEscort1.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterChineseRegex, 6));
        etEscort2.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterChineseRegex, 6));
        etCarModel.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterNumberChineseRegex, 20));
        etBrandModel.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterNumberChineseRegex, 20));
        etCarFrame.setFilters(InputFilterUtil.getInputFilters(InputFilterUtil.letterNumberRegex, 20));
        etAccordRule.setFocusable(false);
        etAccordRule.setFocusableInTouchMode(false);
        setETAccordRule();
        etCarKilometers.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etCarKilometers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etCarKilometers.removeTextChangedListener(this);

                if (start == 0 && s.toString().equals(".") && count == 1) {
                    //输入的第一个字符为"."
                    etCarKilometers.setText("");
                } else if (s.length() >= 7 + 1 && count != 0) {
                    //当整数位数输入到达被要求的上限,并且当前在输入字符,而不是减少字符
                    if (s.toString().contains(".")) {
                        //当前输入的有"."字符
                        String[] text = s.toString().split("\\.");
                        if (text.length >= 2 && text[1].length() > 2) {
                            //小数位数超数
                            etCarKilometers.setText(s.subSequence(0, s.toString().length() - 1));
                            etCarKilometers.setSelection(s.toString().length() - 1);
                        }
                    } else {
                        etCarKilometers.setText(s.subSequence(0, s.toString().length() - 1));
                        etCarKilometers.setSelection(s.toString().length() - 1);
                    }
                }

                etCarKilometers.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        assert mPresenter != null;
        mPresenter.init(self, mCase, tvDeducationTime, tvEscortTime, etCarLicense, etDriverName, tvDeducationAddress);
    }

    private void setETAccordRule() {
        assert mPresenter != null;
        etAccordRule.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //getCompoundDrawables() 可以获取一个长度为4的数组，
                //存放drawableLeft，Right，Top，Bottom四个图片资源对象
                //index=2 表示的是 drawableRight 图片资源对象
                Drawable drawable = etAccordRule.getCompoundDrawables()[2];
                if (drawable == null)
                    return false;
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                //drawable.getIntrinsicWidth() 获取drawable资源图片呈现的宽度
                if (event.getX() > etAccordRule.getWidth() - etAccordRule.getPaddingRight()
                        - drawable.getIntrinsicWidth() ) {
                    //进入这表示图片被选中，可以处理相应的逻辑了
                    mPresenter.showAccordRule(self, etAccordRule);
                }
                return false;
            }
        });

    }

    /**
     * 监听在填写之后，不然填写缓存或者服务器上的内容会触发监听
     */
    @Override
    public void addListener() {
        etCarLicense.addTextChangedListener(this);
        listShowCarInfo.setAdapter(mAdapter);
        listShowCarInfo.setOnItemClickListener(this);
    }

    @OnClick({R2.id.tv_frame_number_drop, R2.id.tv_car_kilometers_drop, R2.id.tv_four_corner_photo_drop, R2.id.tv_deducation_address, R2.id.tv_current_address_with_deducation})
    public void onViewClicked(View v) {
        if (mPresenter == null) return;
        int i = v.getId();
        if (i == R.id.tv_frame_number_drop) {
            Drawable drawable;

            if (llFrameNumber.getVisibility() == View.GONE) {
                llFrameNumber.setVisibility(View.VISIBLE);
                lineFrameNumberDrop.setVisibility(View.GONE);
                drawable = getResources().getDrawable(R.drawable.ic_up);
            } else {
                llFrameNumber.setVisibility(View.GONE);
                lineFrameNumberDrop.setVisibility(View.VISIBLE);
                drawable = getResources().getDrawable(R.drawable.ic_down);
            }

            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                tvFrameNumberDrop.setCompoundDrawables(null, null, drawable, null);
            }
        } else if (i == R.id.tv_car_kilometers_drop) {
            Drawable drawable;

            if (llCarKilometers.getVisibility() == View.GONE) {
                llCarKilometers.setVisibility(View.VISIBLE);
                lineCarKilometersDrop.setVisibility(View.GONE);
                drawable = getResources().getDrawable(R.drawable.ic_up);
            } else {
                llCarKilometers.setVisibility(View.GONE);
                lineCarKilometersDrop.setVisibility(View.VISIBLE);
                drawable = getResources().getDrawable(R.drawable.ic_down);
            }

            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                tvCarKilometersDrop.setCompoundDrawables(null, null, drawable, null);
            }
        } else if (i == R.id.tv_four_corner_photo_drop) {
            Drawable drawable;

            if (llFourCornerPhoto.getVisibility() == View.GONE) {
                llFourCornerPhoto.setVisibility(View.VISIBLE);
                lineCornerPhotoDrop.setVisibility(View.GONE);
                drawable = getResources().getDrawable(R.drawable.ic_up);
            } else {
                llFourCornerPhoto.setVisibility(View.GONE);
                lineCornerPhotoDrop.setVisibility(View.VISIBLE);
                drawable = getResources().getDrawable(R.drawable.ic_down);
            }

            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                tvFourCornerPhotoDrop.setCompoundDrawables(null, null, drawable, null);
            }
        } else if (i == R.id.tv_deducation_address || i == R.id.tv_current_address_with_deducation) {
            startActivityForResult(LawSelectLocationActivity.getLocationIntent(self
                    , tvDeducationAddress.getText().toString()
                    , "tvDeducationAddress")
                    , Config.LAWLOCATION);
        }
    }


    @OnClick({R2.id.tv_top_light, R2.id.tv_price_meter, R2.id.tv_empty_ticket, R2.id.tv_false_certificate, R2.id.tv_bill})
    public void chooseCount(TextView textView) {
        assert mPresenter != null;
        mPresenter.chooseCount(self, textView);
    }

    @OnClick({R2.id.tv_deducation_time, R2.id.tv_escort_time})
    public void chooseTime(TextView textView) {
        int i = textView.getId();
        if (i == R.id.tv_deducation_time) {
            assert mPresenter != null;
            mPresenter.startCalendar(tvDeducationTime, isUpdate);
        } else {
            if (TextUtils.isEmpty(tvDeducationTime.getText())) {
                UIUtils.toast(self, "请先确定扣车时间", Toast.LENGTH_SHORT);
                return;
            }
            assert mPresenter != null;
            mPresenter.endCalendar(tvDeducationTime, tvEscortTime, isUpdate);
        }

    }

    @OnClick(R2.id.tv_storage_car_park)
    public void chooseStorageCarPark(TextView textView) {
        assert mPresenter != null;
        mPresenter.showParkList(self, textView);
    }

    @Override
    public boolean validType() {
        if ("重新上传".equals(btnSave.getText().toString())) {
            if (detainCarForm != null) {
                assert mPresenter != null;
                mPresenter.uploadPhotos(getFiles(), detainCarForm);
            }
            return true;
        } else {
            return false;
        }
    }

    @OnClick({R2.id.iv_car_front, R2.id.iv_car_after, R2.id.iv_car_left, R2.id.iv_car_right, R2.id.iv_car_kilometers, R2.id.iv_car_frame_number})
    public void getPhoto(ImageView imageView) {
        this.currentClickImageView = imageView;

        if (isUpdate) {
            String url = "";
            if (imageView == ivCarFront) {
                Object tag = imageView.getTag(R.id.car_front_tag);
                if (tag != null) {
                    url = (String) tag;
                }
            } else if (imageView == ivCarAfter) {
                Object tag = imageView.getTag(R.id.car_after_tag);
                if (tag != null) {
                    url = (String) tag;
                }
            } else if (imageView == ivCarLeft) {
                Object tag = imageView.getTag(R.id.car_left_tag);
                if (tag != null) {
                    url = (String) tag;
                }
            } else if (imageView == ivCarRight) {
                Object tag = imageView.getTag(R.id.car_right_tag);
                if (tag != null) {
                    url = (String) tag;
                }
            } else if (imageView == ivCarKilometers) {
                Object tag = imageView.getTag(R.id.car_kilometers_tag);
                if (tag != null) {
                    url = (String) tag;
                }
            } else if (imageView == ivCarFrameNumber) {
                Object tag = imageView.getTag(R.id.car_frame_tag);
                if (tag != null) {
                    url = (String) tag;
                }
            }

            DialogUtil.showPhotoDialog(this, url);
        } else {
            assert mPresenter != null;
            mPresenter.getPhoto(self);
        }
    }

    @OnClick({R2.id.ib_shaomiao_frame, R2.id.ib_shaomiao_kilometers})
    public void takingPictures(ImageButton imageButton) {
        if (isUpdate) {
            UIUtils.toast(self, "照片不能进行编辑", Toast.LENGTH_SHORT);
            return;
        }
        int i = imageButton.getId();
        if (i == R.id.ib_shaomiao_frame) {
            this.currentClickImageView = ivCarFrameNumber;
        } else if (i == R.id.ib_shaomiao_kilometers) {
            this.currentClickImageView = ivCarKilometers;
        }
        assert mPresenter != null;
        mPresenter.takingPic(self);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1001) {
                assert mPresenter != null;
                compressPhoto(mPresenter.getTakePhotoFile().getPath());
            } else if (requestCode == 1002) {
                if (data != null && data.getData() != null) {
                    String photoPath = FileUtil.getPathFromUri(self, data.getData());
                    if (!TextUtils.isEmpty(photoPath)) {
                        compressPhoto(photoPath);
                    }
                }
            }
        }
    }

    private MyProgressDialog waiting;

    private void hideWaiting() {
        if (waiting != null && waiting.isShowing()) {
            waiting.dismiss();
        }
    }

    @SuppressLint("CheckResult")
    public void compressPhoto(final String path) {
        waiting = Windows.waiting(self);

        Observable.just(path)
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws IOException {
                        return PictureUtil.bitmapToPath(path);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        hideWaiting();
                        currentPhoto = new File(s);
                        display();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideWaiting();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void display() {
        Glide.with(self).load(currentPhoto).into(currentClickImageView);
        if (currentClickImageView == ivCarFront) {
            currentClickImageView.setTag(R.id.car_front_tag, currentPhoto);
        } else if (currentClickImageView == ivCarAfter) {
            currentClickImageView.setTag(R.id.car_after_tag, currentPhoto);
        } else if (currentClickImageView == ivCarLeft) {
            currentClickImageView.setTag(R.id.car_left_tag, currentPhoto);
        } else if (currentClickImageView == ivCarKilometers) {
            currentClickImageView.setTag(R.id.car_kilometers_tag, currentPhoto);
        } else if (currentClickImageView == ivCarFrameNumber) {
            currentClickImageView.setTag(R.id.car_frame_tag, currentPhoto);
        } else if (currentClickImageView == ivCarRight) {
            currentClickImageView.setTag(R.id.car_right_tag, currentPhoto);
        }
    }

    @Override
    public void onValidationSucceeded() {
        if (!isUpdate) {
            //第一次进入必须上传图片
            if (ivCarFront.getTag(R.id.car_front_tag) == null) {
                UIUtils.toast(self, "请添加车辆前侧照片", Toast.LENGTH_SHORT);
                return;
            }

            if (ivCarAfter.getTag(R.id.car_after_tag) == null) {
                UIUtils.toast(self, "请添加车辆后侧照片", Toast.LENGTH_SHORT);
                return;
            }

            if (ivCarLeft.getTag(R.id.car_left_tag) == null) {
                UIUtils.toast(self, "请添加车辆左侧照片", Toast.LENGTH_SHORT);
                return;
            }

            if (ivCarRight.getTag(R.id.car_right_tag) == null) {
                UIUtils.toast(self, "请添加车辆右侧照片", Toast.LENGTH_SHORT);
                return;
            }

            if (ivCarKilometers.getTag(R.id.car_kilometers_tag) == null) {
                UIUtils.toast(self, "请添加车辆里程数照片", Toast.LENGTH_SHORT);
                return;
            }

            if (ivCarFrameNumber.getTag(R.id.car_frame_tag) == null) {
                UIUtils.toast(self, "请添加车架号照片", Toast.LENGTH_SHORT);
                return;
            }
        }

        assert mPresenter != null;
        DetainCarFormQ detainCarFormQ = new DetainCarFormQ();
        detainCarFormQ.setZID(mCase.getID());
        detainCarFormQ.setCZXM(etDriverName.getText().toString());
        detainCarFormQ.setCPH(etCarLicense.getText().toString());
        detainCarFormQ.setCYS(etCarColor.getText().toString());
        detainCarFormQ.setCX(etCarModel.getText().toString());
        detainCarFormQ.setCPXH(etBrandModel.getText().toString());
        detainCarFormQ.setCJH(etCarFrame.getText().toString());
        detainCarFormQ.setDD(tvTopLight.getText().toString());
        detainCarFormQ.setJJQ(tvPriceMeter.getText().toString());
        detainCarFormQ.setKCP(tvEmptyTicket.getText().toString());
        detainCarFormQ.setJZJ(tvFalseCertificate.getText().toString());
        detainCarFormQ.setPJ(tvBill.getText().toString());
        detainCarFormQ.setSRWP(etPersonalBelong.getText().toString());
        detainCarFormQ.setYJTL(etAccordRule.getText().toString());
        detainCarFormQ.setKCSJ(String.valueOf(TimeTool.parseDate(tvDeducationTime.getText().toString()).getTime() / 1000));
        detainCarFormQ.setKCDD(tvDeducationAddress.getText().toString());
        detainCarFormQ.setYSSJ(String.valueOf(TimeTool.parseDate(tvEscortTime.getText().toString()).getTime() / 1000));
        detainCarFormQ.setCFTCC(tvStorageCarPark.getText().toString());
        detainCarFormQ.setYSY1(etEscort1.getText().toString());
        detainCarFormQ.setYSY2(etEscort2.getText().toString());
        detainCarFormQ.setZFDWQZSJ(GetUTCUtil.setEndTime(mPresenter.getLastEndTime(), detainCarFormQ.getYSSJ(), Config.UTC_CARFORM));
        detainCarFormQ.setCGLS(String.valueOf(Float.valueOf(etCarKilometers.getText().toString())));
        detainCarFormQ.setID(instrumentID);
        detainCarFormQ.setZFRY1(mCase.getZFZH1());
        detainCarFormQ.setZFRY2(mCase.getZHZH2());
        detainCarFormQ.setZH(mCase.getZH());
        if (1 == clickStatus) {
            startActivityForResult(WebViewActivity.getIntent(self, detainCarFormQ, false), 0x0100);
        } else if (!isUpdate) {
            mPresenter.addDetainCarForm(detainCarFormQ);
        } else {
            mPresenter.UpdateData(detainCarFormQ, checkChange(), followInstruments);
        }

    }

    @SuppressWarnings("all")
    @Subscriber(tag = Config.SELECTADD)
    public void showLocationContent(AddMsg addMsg) {
        if ("tvDeducationAddress".equals(addMsg.getType())) {
            tvDeducationAddress.setText(addMsg.getAddr());
        }
    }

    @Override
    public void getVehicleInfo(VehicleInfo info) {
        if (!isUpdate && info != null) {
            etCarColor.setText(info.getVehicleColor());
            etCarModel.setText(info.getVehicleType());
            etBrandModel.setText(info.getVehicleXH());
            etCarFrame.setText(info.getChejiaNumber());
            etCarKilometers.setText(info.getDriverMile());
        }
    }

    @Override
    public void onCreateSuccess(DetainCarForm detainCarForm) {
        this.detainCarForm = detainCarForm;
        if(!TextUtils.isEmpty(detainCarForm.getId()) && !"null".equals(detainCarForm.getId())){
            this.instrumentID = detainCarForm.getId();
        }
        if (!isUpdate) {
            UIUtils.toast(self, "添加扣押车辆交接单成功,开始上传图片", Toast.LENGTH_SHORT);
            assert mPresenter != null;
            mPresenter.uploadPhotos(getFiles(), detainCarForm);
        } else {
            UIUtils.toast(self, "修改扣押车辆交接单成功", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onUpdateSuccess(DetainCarForm detainCarForm) {
        this.detainCarForm = detainCarForm;
        this.instrumentID = detainCarForm.getId();
        UIUtils.toast(self, "修改扣押车辆交接单成功", Toast.LENGTH_SHORT);
    }

    private List<File> getFiles() {
        List<File> files = new ArrayList<>(6);
        files.add((File) ivCarFront.getTag(R.id.car_front_tag));
        files.add((File) ivCarAfter.getTag(R.id.car_after_tag));
        files.add((File) ivCarLeft.getTag(R.id.car_left_tag));
        files.add((File) ivCarRight.getTag(R.id.car_right_tag));
        files.add((File) ivCarKilometers.getTag(R.id.car_kilometers_tag));
        files.add((File) ivCarFrameNumber.getTag(R.id.car_frame_tag));
        return files;
    }

    @Override
    public void uploadSuccess() {
        UIUtils.toast(self, "上传图片成功", Toast.LENGTH_SHORT);
    }

    @Override
    public void uploadFailure(String msg) {
        UIUtils.toast(self, msg, Toast.LENGTH_SHORT);
        btnSave.setText("重新上传");
    }

    /*
     * 把现场检查笔录相关信息带入
     */
    @Override
    public void showViewPartContent() {
        if (CacheManager.getInstance().getLiveCheckRecord() != null) {
            etCarColor.setText(CacheManager.getInstance().getLiveCheckRecord().getVCOLOR());
            etCarModel.setText(CacheManager.getInstance().getLiveCheckRecord().getVLPP());
        }
        tvDeducationTime.setText(CacheManager.getInstance().getDetainTime());
        tvStorageCarPark.setText(CacheManager.getInstance().getPark());
        if (CacheManager.getInstance().getCarInfo() != null) {
            getVehicleInfo(CacheManager.getInstance().getCarInfo().get(0));
        }
    }

    @Override
    public void showTime(Date date, boolean isStart) {
        assert mPresenter != null;
        if (isStart) {
            if (!TextUtils.isEmpty(CacheManager.getInstance().getDetainTime()) && TimeTool.getYYHHmm(CacheManager.getInstance().getDetainTime()).getTime() > date.getTime()) {
                DialogUtil.showTipDialog(self, "扣车时间不能小于扣押车辆决定书的决定时间");
                return;
            } else if (Long.valueOf(mCase.getCREATEUTC()) * 1000 > date.getTime()) {
                DialogUtil.showTipDialog(self, "扣车时间不能小于案件创建时间");
                return;
            }
            tvDeducationTime.setText(TimeTool.yyyyMMdd_HHmm.format(date));
            if (date.getTime() > mPresenter.getLastEndTime()) {
                Date eDate = new Date();
                eDate.setTime(date.getTime() + Config.UTC_CARFORM * 1000);
                tvEscortTime.setText("");
                tvEscortTime.setHint(TimeTool.yyyyMMdd_HHmm.format(eDate));
            } else {
                tvEscortTime.setHint(TimeTool.yyyyMMdd_HHmm.format(mPresenter.getEndTime()));
            }

        } else {
            if (!TextUtils.isEmpty(tvDeducationTime.getText()) && TimeTool.getYYHHmm(tvDeducationTime.getText().toString()).getTime() >= date.getTime()) {
                DialogUtil.showTipDialog(self, "押送时间需大于扣车时间");
                return;
            }
            if (mPresenter.getLastEndTime() > date.getTime()) {
                DialogUtil.showTipDialog(self, "押送时间需大于上个文书时间和案件创建时间");
                return;
            }
            tvEscortTime.setText(TimeTool.yyyyMMdd_HHmm.format(date));
        }
    }

    @Override
    @SuppressWarnings("all")
    public void showPhotos(List<String> photoIds) {
        for (String item : photoIds) {
            String[] split = item.split(",");
            String id = split[0];
            String index = split[1];

            String url = Config.SERVER_BLLIST + "?type=dowmLoadKycljjdPhotoById&jsonStr="
                    + BaseData.gson.toJson(new PictureID(id, index));

            if ("1".equals(index)) {
                Glide.with(self).load(url).into(ivCarFront);
                ivCarFront.setTag(R.id.car_front_tag, url);
            } else if ("2".equals(index)) {
                Glide.with(self).load(url).into(ivCarAfter);
                ivCarAfter.setTag(R.id.car_after_tag, url);
            } else if ("3".equals(index)) {
                Glide.with(self).load(url).into(ivCarLeft);
                ivCarLeft.setTag(R.id.car_left_tag, url);
            } else if ("4".equals(index)) {
                Glide.with(self).load(url).into(ivCarRight);
                ivCarRight.setTag(R.id.car_right_tag, url);
            } else if ("5".equals(index)) {
                Glide.with(self).load(url).into(ivCarKilometers);
                ivCarKilometers.setTag(R.id.car_kilometers_tag, url);
            } else if ("6".equals(index)) {
                Glide.with(self).load(url).into(ivCarFrameNumber);
                ivCarFrameNumber.setTag(R.id.car_frame_tag, url);
            }
        }
    }

    @Override
    public void showViewContent(ParentQ parentQ) {
        DetainCarFormQ info = (DetainCarFormQ) parentQ;
        this.instrumentID = info.getID();
        etDriverName.setText(info.getCZXM());
        etCarLicense.setText(info.getCPH());
        etCarColor.setText(info.getCYS());
        etCarModel.setText(info.getCX());
        etBrandModel.setText(info.getCPXH());
        etCarFrame.setText(info.getCJH());
        tvTopLight.setText(info.getDD());
        tvPriceMeter.setText(info.getJJQ());
        tvEmptyTicket.setText(info.getKCP());
        tvFalseCertificate.setText(info.getJZJ());
        tvBill.setText(info.getPJ());
        setViewTime(info);
        etPersonalBelong.setText(info.getSRWP());
        etAccordRule.setText(info.getYJTL());
        tvDeducationAddress.setText(info.getKCDD());
        etEscort1.setText(info.getYSY1());
        etEscort2.setText(info.getYSY2());
        etCarKilometers.setText(info.getCGLS());
        setEffctNr(info.getCFTCC());
    }

    private void setEffctNr(String CFTCC) {
        if (isConfirmed() && !TextUtils.isEmpty(CacheManager.getInstance().getPark())) {
            tvStorageCarPark.setText(CacheManager.getInstance().getPark());
        } else {
            tvStorageCarPark.setText(CFTCC);
        }
    }

    //控件设置时间
    private void setViewTime(DetainCarFormQ info) {
        assert mPresenter != null;
        long duration;
        if (isConfirmed()) {//如果是待确认的状态\有决定书，扣车时间用决定书的扣车时间
            duration = TimeTool.parseDate2(info.getYSSJ()).getTime() - mPresenter.getLastEndTime();
            if (!TextUtils.isEmpty(CacheManager.getInstance().getDetainTime()) && duration > 0) {
                tvDeducationTime.setHint(CacheManager.getInstance().getDetainTime());
                tvEscortTime.setHint(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getYSSJ())));
            } else if (TextUtils.isEmpty(CacheManager.getInstance().getDetainTime()) && duration > 0) {
                tvDeducationTime.setHint(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getKCSJ())));
                tvEscortTime.setHint(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getYSSJ())));
            } else if (!TextUtils.isEmpty(CacheManager.getInstance().getDetainTime())) {
                tvDeducationTime.setHint(CacheManager.getInstance().getDetainTime());
            }
        } else {
            tvDeducationTime.setText(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getKCSJ())));
            tvEscortTime.setText(TimeTool.yyyyMMdd_HHmm.format(TimeTool.parseDate2(info.getYSSJ())));
        }
    }

    @Override
    public void showCaseContent() {
        assert mPresenter != null;
        StopChange = true;
        mPresenter.getVehicleInfo(mCase.getVNAME());
        etCarLicense.setText(mCase.getVNAME());
        etDriverName.setText(mCase.getBJCR());
        tvDeducationAddress.setText(mCase.getJCDD());
    }


    @Override
    public boolean checkChange() {
        boolean isNeedUpdateStatus = false;
        for (int i = 0; i < followInstruments.size(); i++) {
            if (Config.ID_CARFORM.equals(followInstruments.get(i).getId()) && 2 == followInstruments.get(i).getStatus()) {
                isNeedUpdateStatus = true;
            }
        }
        return isNeedUpdateStatus;
    }

    @Override
    public String getInstrumentId() {
        return Config.ID_CARFORM;
    }

    @Override
    public void getCarOrDriverInfo() {
        assert mPresenter != null;
        if (etCarLicense.getText().toString().length() < 6) {
            listShowCarInfo.setVisibility(View.GONE);
            return;
        }
        mPresenter.getCarInfo(etCarLicense.getText().toString());
    }

    @Override
    public void notifyAdapter() {
        listShowCarInfo.setVisibility(View.VISIBLE);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        VehicleInfo vehicleInfo = (VehicleInfo) parent.getItemAtPosition(position);
        StopChange = true;
        etCarLicense.setText(vehicleInfo.getVname());
        etCarColor.setText(vehicleInfo.getVehicleColor());
        etCarModel.setText(vehicleInfo.getVehicleType());
        etBrandModel.setText(vehicleInfo.getVehicleXH());
        etCarFrame.setText(vehicleInfo.getChejiaNumber());
        etCarKilometers.setText(vehicleInfo.getDriverMile());
        listShowCarInfo.setVisibility(View.GONE);
    }

}
