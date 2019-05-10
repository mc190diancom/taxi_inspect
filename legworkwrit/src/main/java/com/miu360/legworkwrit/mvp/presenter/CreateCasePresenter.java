package com.miu360.legworkwrit.mvp.presenter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.miu30.common.async.Result;
import com.miu30.common.data.UserPreference;
import com.miu30.common.ui.entity.queryZFRYByDWMC;
import com.miu30.common.util.CommonDialog;
import com.miu30.common.util.UIUtils;
import com.miu30.common.util.Windows;
import com.miu360.legworkwrit.R;
import com.miu30.common.app.MyErrorHandleSubscriber;
import com.miu360.legworkwrit.app.utils.RxUtils;
import com.miu360.legworkwrit.mvp.contract.CreateCaseContract;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.District;
import com.miu360.legworkwrit.mvp.model.entity.InquiryRecordPhoto;
import com.miu360.legworkwrit.util.DialogUtil;
import com.miu360.legworkwrit.util.MapUtil;
import com.miu360.legworkwrit.util.RequestParamsUtil;
import com.miu360.legworkwrit.util.TimeTool;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class CreateCasePresenter extends BasePresenter<CreateCaseContract.Model, CreateCaseContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public UserPreference user;

    private Activity activity;
    private String[] itemOne = {"巡游车", "网约车", "非法经营出租汽车"};
    private String[] itemZfry;
    private List<queryZFRYByDWMC> mList;
    private String[] itemDistrict;
    private String[] itemCityAbbreviation = {"京", "沪", "津", "渝", "黑", "吉", "辽", "蒙", "冀", "新", "甘", "青", "陕", "宁", "豫", "鲁", "晋", "皖", "鄂", "湘", "苏", "川", "黔", "滇", "桂", "藏", "浙", "赣", "粤", "闽", "台", "琼", "港", "澳"};

    @Inject
    public CreateCasePresenter(CreateCaseContract.Model model, CreateCaseContract.View rootView) {
        super(model, rootView);
    }

    /*
     * 初始化视图填写内容
     */
    public void init(Activity activity,  TextView tvLawEnforcementPersonne1, TextView tvCreationTime, TextView tvSourceCase) {
        this.activity = activity;
        tvLawEnforcementPersonne1.setText(user.getString("user_name_update_info", ""));
        if(TextUtils.isEmpty(tvCreationTime.getHint())){
            initDefaultCreateTime(tvCreationTime);
        }
        tvSourceCase.setText(activity.getResources().getString(R.string.check_case));

        getZfry2(false,null);
        setListToArray(CacheManager.getInstance().getDistrict());
    }

    private void initDefaultCreateTime(TextView tvCreationTime) {
        ArrayList<InquiryRecordPhoto> infos = activity.getIntent().getParcelableArrayListExtra("infos");

        if (infos != null && infos.size() > 0) {
            //创建了询问笔录，则案件的创建时间必须在询问笔录开始时间的前一分钟
            tvCreationTime.setHint(TimeTool.yyyyMMdd_HHmm.format(new Date(getStartTime(infos) * 1000 - 60 * 1000)));
        } else {
            //没有创建询问笔录，默认使用当前时间
            tvCreationTime.setHint(TimeTool.yyyyMMdd_HHmm.format(Calendar.getInstance().getTime()));
        }
    }

    /**
     * show选择行业时的弹窗
     */
    public void showWindowForHy(final TextView tv) {
        Windows.singleChoice(activity, "选择稽查行业", itemOne, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                tv.setText(itemOne[position]);
            }
        });
    }

    /**
     * show选择执法人员时的弹窗
     */
    public void showWindowForZfry(final String zfry,final TextView tvLawEnforcementPersonne2) {
        if (itemZfry == null) {
            UIUtils.toast(activity, "正在加载数据，请稍后", Toast.LENGTH_SHORT);
            getZfry2(false,null);
            return;
        }
        Windows.singleChoice(activity, "请选择执法人员", itemZfry, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                if(zfry.equals(itemZfry[position])){
                    UIUtils.toast(activity,"不能和执法人员1相同",Toast.LENGTH_SHORT);
                    return;
                }
                tvLawEnforcementPersonne2.setText(itemZfry[position]);
                mRootView.onReturnCurrentZFRY(mList.get(position));
            }
        });
    }

    /**
     * 初始化日期控件以及返回点击的时间内容
     */
    public void showDate(String time) {
        Date date = new Date();
        if (!TextUtils.isEmpty(time)) {
            date = TimeTool.getYYHHmm(time);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        DialogUtil.TimePicker(activity, "创建时间", calendar, new DialogUtil.dateCallBack() {
            @Override
            public void returnDate(Date date) {
                mRootView.showTime(TimeTool.yyyyMMdd_HHmm.format(date));
            }
        });
    }

    /*
     * 初始化处罚类型以及返回选择的内容
     */
    public void showPunishType(final Activity activity) {
        DialogUtil.showCommonDialogNoContent(activity, activity.getString(R.string.select_type), activity.getString(R.string.warning), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRootView.showPunishType(activity.getString(R.string.warning));
            }
        }, activity.getString(R.string.punish), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRootView.showPunishType(activity.getString(R.string.punish));
            }
        });
    }

    /*
     * 区域选择
     */
    private void getDistrict() {
        mModel.getDistrictInfo("getAreaInfo")
                .compose(RxUtils.<Result<List<District>>>applySchedulers(mRootView))
                .subscribe(new MyErrorHandleSubscriber<Result<List<District>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<District>> result) {
                        if (result.ok()) {
                            setListToArray(result.getData());
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }

                });
    }

    /*
     * 获取执法人员2
     */
    public void getZfry2(final boolean isShowLoading,final String zfry2) {//是从历史稽查跳转过来才会用到这两个字段
        Map<String, Object> map = new MapUtil().getMap("query_Zfry_Name", RequestParamsUtil.RequestZFRYNameInfo());
        mModel.getCheZuList(map)
                .compose(RxUtils.<Result<List<queryZFRYByDWMC>>>applySchedulers(mRootView,isShowLoading))
                .subscribe(new MyErrorHandleSubscriber<Result<List<queryZFRYByDWMC>>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<List<queryZFRYByDWMC>> listResult) {
                        mList = listResult.getData();
                        List<String> list = new ArrayList<>();
                        for (int i = 0; i < listResult.getData().size(); i++) {
                            list.add(listResult.getData().get(i).getNAME());
                            if(isShowLoading && listResult.getData().get(i).getNAME().contains(zfry2)){//如果是从历史稽查跳转过来的，因为没有zfzh2，这里根据zfry姓名去查了一下再显示
                                mRootView.showZfry2(listResult.getData().get(i));
                            }
                        }
                        itemZfry = UIUtils.listToArray(list);
                    }

                });
    }

    /*
     * 创建案件
     */
    public void createCase(final Case c) {
        Map<String, Object> map = new MapUtil().getMap("addCase", RequestParamsUtil.RequestCaseInfo(c));
        mModel.createCase(map)
                .compose(RxUtils.<Result<JSONObject>>applySchedulers(mRootView, true))
                .subscribe(new MyErrorHandleSubscriber<Result<JSONObject>>(mErrorHandler) {

                    @Override
                    public void onNextResult(Result<JSONObject> result) {
                        if (result.ok()) {
                            try {
                                String id = result.getData().optString("id");
                                String zh = result.getData().optString("zh");
                                c.setID(id);
                                c.setZH(zh);
                                mRootView.onCreateSuccess(c);
                            } catch (Exception e) {
                                e.printStackTrace();
                                mRootView.showMessage("生成id有误，请重试");
                            }
                        } else {
                            mRootView.showMessage(result.getMsg());
                        }
                    }

                });
    }

    private void setListToArray(List<District> districts){
        if(districts == null){
            return;
        }
        List<String> list = new ArrayList<>();
        for (int i = 0; i < districts.size(); i++) {
            list.add(districts.get(i).getLBMC());
        }
        itemDistrict = UIUtils.listToArray(list);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }

    public boolean verifyCreateTime(String s) {
        ArrayList<InquiryRecordPhoto> infos = activity.getIntent().getParcelableArrayListExtra("infos");

        if (infos == null || infos.size() <= 0) {
            return true;
        } else {
            long currentTime = TimeTool.getYYHHmm(s).getTime() / 1000;
            long inquiryRecordStartTime = getStartTime(infos);
            if (currentTime >= inquiryRecordStartTime) {
                DialogUtil.showTipDialog(activity, "案件创建时间必须在询问笔录的开始时间之前");
                return false;
            } else if (inquiryRecordStartTime - currentTime < 60) {
                DialogUtil.showTipDialog(activity, "案件创建时间必须在询问笔录的开始时间前1分钟或者更早");
                return false;
            }
        }

        return true;
    }

    private long getStartTime(ArrayList<InquiryRecordPhoto> infos) {
        long startTime = TimeTool.parseDate(infos.get(0).getSTARTUTC()).getTime() / 1000;
        long temp;

        for (InquiryRecordPhoto info : infos) {
            temp = TimeTool.parseDate(info.getSTARTUTC()).getTime() / 1000;
            if (temp < startTime) {
                startTime = temp;
            }
        }

        return startTime;
    }

    /*
     * 选择区域下拉框
     */
    public void showInspectDistrict(final TextView tvLawEnforcementDistrict) {
        if (itemDistrict == null) {
            UIUtils.toast(activity, "正在加载数据，请稍后", Toast.LENGTH_SHORT);
            getDistrict();
            return;
        }
        Windows.singleChoice(activity, "选择执法区域", itemDistrict, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                tvLawEnforcementDistrict.setText(itemDistrict[position]);
            }
        });
    }

    public void showWindowForCity(final TextView tvCityAbbreviation) {
        Windows.singleChoice(activity, "选择城市简称", itemCityAbbreviation, new CommonDialog.OnDialogItemClickListener() {
            @Override
            public void dialogItemClickListener(int position) {
                tvCityAbbreviation.setText(itemCityAbbreviation[position]);
            }
        });
    }


    public void setLawEnforcementDistrict(TextView tvLawEnforcementDistrict,String district) {
        List<String> districts = new ArrayList<>();
        districts.add("朝阳区");
        districts.add("东城区");
        districts.add("丰台区");
        districts.add("海淀区");
        districts.add("石景山区");
        districts.add("外省区");
        districts.add("西城区");
        if(districts.contains(district)){
            tvLawEnforcementDistrict.setText(district);
        }
    }
}
