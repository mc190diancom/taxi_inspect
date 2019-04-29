package com.miu360.taxi_check.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu30.common.config.Config;
import com.miu30.common.data.UserPreference;
import com.miu30.common.ui.entity.DriverInfo;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.inspect.R;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.AllCaseQ;
import com.miu360.legworkwrit.mvp.model.entity.BlType;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.CaseID;
import com.miu360.legworkwrit.mvp.model.entity.UTC;
import com.miu360.legworkwrit.mvp.ui.activity.CaseBlListActivity;
import com.miu360.taxi_check.BaseFragment;
import com.miu360.taxi_check.common.ChooseInputType;
import com.miu360.taxi_check.common.CommonDialog.OnDialogItemClickListener;
import com.miu360.taxi_check.common.DateVer;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.CompanyInfo;
import com.miu360.taxi_check.model.HistoryInspectCountRecordInfo;
import com.miu360.taxi_check.model.HistoryInspectRecordModelNew;
import com.miu360.taxi_check.ui.CaseHistoryListActivity;
import com.miu360.taxi_check.ui.FindRecordActivity;
import com.miu360.taxi_check.ui.HistoryInspcetRecordActivity;
import com.miu360.taxi_check.ui.InspectRecordDetailActivity;
import com.miu360.taxi_check.util.UIUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Callable;

public class HistoryRecordFragment extends BaseFragment implements OnClickListener {
    @ViewInject(R.id.check_time)
    private TextView check_time;
    @ViewInject(R.id.over_time)
    private TextView over_time;
    @ViewInject(R.id.check_week)
    private CheckBox check_week;// 一周
    @ViewInject(R.id.check_currentday)
    private CheckBox check_currentday;// 当天
    @ViewInject(R.id.check_month)
    private CheckBox check_month;// 一个月
    @ViewInject(R.id.check_all)
    private CheckBox check_all;// 所有
    @ViewInject(R.id.rg)
    private LinearLayout rg;
    @ViewInject(R.id.query)
    private TextView query;
    @ViewInject(R.id.inspect_hangye)
    private TextView inspect_hangye;
    @ViewInject(R.id.case_type)
    private TextView case_type;
    @ViewInject(R.id.driver_licence_number)
    private EditText driver_licence_number;
    @ViewInject(R.id.driver_name)
    private EditText driver_name;
    @ViewInject(R.id.vehicle_number)
    private EditText vehicle_number;
    @ViewInject(R.id.inspect_result)
    private TextView inspect_result;
    @ViewInject(R.id.ll1)
    private LinearLayout ll1;
    @ViewInject(R.id.ll2)
    private LinearLayout ll2;

    String[] itemOne = {"巡游车", "非法经营出租汽车"};
    String[] itemTwo = {"正常", "违规"};
    String[] itemThree = {"正常", "表扬", "批教", "处罚", "警告", "处罚并转内勤", "警告并转内勤"};
    // 根据车牌、准驾证号获取的信息
    String color;
    String carType;
    String sfzh = "";
    String company = "";
    String carCompany;
    String jyxkz;
    String frdb;
    boolean isSuccful = false;
    boolean isSfCompany = false;
    boolean isOnGoing = false;
    // 初始化第一次进入这个界面时间
    long currtTime;
    // 今天0点的时间数
    long zero;

    String weekTime;
    String dayTime;
    String monthTime;
    String currtTimes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_find_record, null);
        initView(view);
        return view;
    }

    private void initView(View root) {
        ViewUtils.inject(this, root);
//		check_week.setChecked(true);

        check_currentday.setChecked(true);
        check_currentday.setOnClickListener(this);
        check_week.setOnClickListener(this);
        check_month.setOnClickListener(this);
        check_all.setOnClickListener(this);
        ChooseInputType input = new ChooseInputType();
        input.init(driver_name);
        initCheckChange();
        initTextContext();
        if (((FindRecordActivity) act).getIsLaw()) {
            query();
        }
    }

    private void initCheckChange() {
        check_currentday.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check_currentday.setEnabled(false);
                    check_week.setEnabled(true);
                    check_month.setEnabled(true);
                    check_all.setEnabled(true);

                    check_week.setChecked(false);
                    check_month.setChecked(false);
                    check_all.setChecked(false);
                } else {
                    check_currentday.setEnabled(true);
                }
            }
        });

        check_week.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check_currentday.setEnabled(true);
                    check_week.setEnabled(false);
                    check_month.setEnabled(true);
                    check_all.setEnabled(true);

                    check_currentday.setChecked(false);
                    check_month.setChecked(false);
                    check_all.setChecked(false);
                } else {
                    check_week.setEnabled(true);
                }
            }
        });

        check_month.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check_currentday.setEnabled(true);
                    check_week.setEnabled(true);
                    check_month.setEnabled(false);
                    check_all.setEnabled(true);

                    check_week.setChecked(false);
                    check_currentday.setChecked(false);
                    check_all.setChecked(false);
                } else {
                    check_month.setEnabled(true);
                }
            }
        });

        check_all.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check_currentday.setEnabled(true);
                    check_week.setEnabled(true);
                    check_month.setEnabled(true);
                    check_all.setEnabled(false);

                    check_week.setChecked(false);
                    check_month.setChecked(false);
                    check_currentday.setChecked(false);
                } else {
                    check_all.setEnabled(true);
                }
            }
        });
    }

    private void selectQuickTime(String s) {

        Calendar calendar = Calendar.getInstance();
        switch (s) {

            case "当天":
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                break;
            case "一周":
                calendar.add(Calendar.DAY_OF_MONTH, -7);
                break;
            case "一月":
                calendar.add(Calendar.MONTH, -1);
                break;
            case "所有":
                calendar.set(1970, 1, 1, 0, 0);
                break;

            default:

                break;
        }

        check_time.setText(new SimpleDateFormat(datePatterShow).format(calendar.getTime()));
        check_time.setTag(calendar.getTimeInMillis());
        over_time.setText(new SimpleDateFormat(datePatterShow).format(new Date()));
        over_time.setTag(new Date().getTime());

    }

    private void initTextContext() {
        check_time.setOnClickListener(this);
        query.setOnClickListener(this);
        over_time.setOnClickListener(this);
        inspect_hangye.setOnClickListener(this);
        inspect_result.setOnClickListener(this);
        case_type.setOnClickListener(this);
        currtTime = System.currentTimeMillis();
        zero = currtTime / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
        long time = currtTime - (1000 * 60 * 60 * 24 * 7);
        long mtime = currtTime - (1000 * 60 * 60 * 24 * 30);

        dayTime = new SimpleDateFormat(datePatterShow).format(new Date(zero));
        weekTime = new SimpleDateFormat(datePatterShow).format(new Date(time));
        monthTime = new SimpleDateFormat(datePatterShow).format(new Date(mtime));
        currtTimes = new SimpleDateFormat(datePatterShow).format(new Date(currtTime));

        check_time.setTag(zero);
        check_time.setText(new SimpleDateFormat(datePatterShow).format(new Date(zero)));
        over_time.setText(new SimpleDateFormat(datePatterShow).format(new Date()));
        over_time.setTag(System.currentTimeMillis());
    }

    private final String datePatterShow = "yyyy-MM-dd HH:mm";
    private final String datePatter = "yyyyMMddHHmm";

    // 设置开始时间选择器
    private void chooseDate1(final View v) {
        Windows.selectDateTime(act, (long) v.getTag(), new Callback<Long>() {

            @Override
            public void onHandle(Long result) {
                if (result > (long) over_time.getTag()) {
                    UIUtils.toast(act, "开始时间不能大于结束时间", Toast.LENGTH_SHORT);
                    return;
                }
                String resultTime = new SimpleDateFormat(datePatterShow).format(new Date(result));
                ((TextView) v).setText(resultTime);
                v.setTag(result);

                if (resultTime.equals(weekTime)) {
                    check_week.setChecked(true);
                } else if (resultTime.equals(monthTime)) {
                    check_month.setChecked(true);
                } else if (resultTime.equals(dayTime)) {
                    check_currentday.setChecked(true);
                } else {
                    check_currentday.setChecked(false);
                    check_week.setChecked(false);
                    check_month.setChecked(false);
                    check_all.setChecked(false);
                }
                /*
                 * if(currtTime !=0){ long pathTime = currtTime - result;
                 * Log.e("pathTime", "pathTime:"+resultTime+"===="+dayTime);
                 * if(resultTime.equals(weekTime)){ check_week.setChecked(true);
                 * }else if(resultTime.equals(monthTime)){
                 * check_month.setChecked(true); }else
                 * if(resultTime.equals(dayTime)){
                 * check_currentday.setChecked(true); }else{
                 * check_currentday.setChecked(false);
                 * check_week.setChecked(false); check_month.setChecked(false);
                 * check_all.setChecked(false); }
                 *
                 * }
                 */
            }
        });
    }

    // 设置结束时间选择器
    private void chooseDate2(final View v) {
        Windows.selectDateTime(act, (long) v.getTag(), new Callback<Long>() {

            @Override
            public void onHandle(Long result) {
                if (result > System.currentTimeMillis()) {
                    UIUtils.toast(act, "结束时间不能大于当前时间", Toast.LENGTH_SHORT);
                    return;
                }
                String resultTime = new SimpleDateFormat(datePatterShow).format(new Date(result));
                if (!resultTime.equals(currtTimes)) {
                    check_currentday.setChecked(false);
                    check_week.setChecked(false);
                    check_month.setChecked(false);
                    check_all.setChecked(false);
                } else {

                }
                ((TextView) v).setText(new SimpleDateFormat(datePatterShow).format(new Date(result)));
                v.setTag(result);

            }
        });
    }

    String type = "巡游车";
    String resultInsepct = "0";

    @Override
    public void onClick(View v) {
        if (v == check_time) {
            chooseDate1(check_time);
        } else if (v == over_time) {
            chooseDate2(over_time);
        } else if (v == check_currentday) {
            selectQuickTime("当天");

        } else if (v == check_week) {
            selectQuickTime("一周");

        } else if (v == check_month) {
            selectQuickTime("一月");

        } else if (v == check_all) {
            selectQuickTime("所有");

        } else if (v == query) {
            query();
        } else if (v == inspect_hangye) {
            Windows.singleChoice(act, "选择稽查行业", itemOne, new OnDialogItemClickListener() {

                @Override
                public void dialogItemClickListener(int position) {
                    inspect_hangye.setText(itemOne[position]);
                }
            });
        } else if (v == inspect_result) {
            Windows.singleChoice(act, "请选择稽查结果", itemTwo, new OnDialogItemClickListener() {

                @Override
                public void dialogItemClickListener(int position) {
                    inspect_result.setText(itemTwo[position]);
                    resultInsepct = position + "";
                }
            });
        } else if (v == case_type) {
            Windows.singleChoice(act, "选择案件性质", itemThree, new OnDialogItemClickListener() {

                @Override
                public void dialogItemClickListener(int position) {
                    case_type.setText(itemThree[position]);
                    if(position > 2){
                        ll1.setVisibility(View.GONE);
                        ll2.setVisibility(View.GONE);
                    }else{
                        ll1.setVisibility(View.VISIBLE);
                        ll2.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    private void query() {
        long start_time = (long) check_time.getTag();
        long end_time = (long) over_time.getTag();

        if (DateVer.VerEndCurrentDate(act, end_time)) {
            return;
        }
        if (DateVer.VerStartCurrentDate(act, start_time)) {
            return;
        }
        if (DateVer.VerStartDate(act, start_time, end_time)) {
            return;
        }
        if (DateVer.IsSame(act, start_time, end_time)) {
            return;
        }
        if (isOnGoing) {
            UIUtils.toast(act, "正在查询，请稍后", Toast.LENGTH_SHORT);
            return;
        }
        final long startTime = start_time / 1000;
        final long endTime = end_time / 1000;
        final String carNumber = vehicle_number.getText().toString();
        final String driverName = driver_name.getText().toString();
        final String driverLicence = driver_licence_number.getText().toString();

        if (TextUtils.isEmpty(driverLicence) && TextUtils.isEmpty(carNumber) && TextUtils.isEmpty(driverName)
                && TextUtils.isEmpty(resultInsepct)) {
            UIUtils.toast(act, "至少填写一项", Toast.LENGTH_LONG);
            return;
        }
        final MyProgressDialog pd = Windows.waiting(act);
        final HistoryInspectCountRecordInfo info = new HistoryInspectCountRecordInfo();
        if (carNumber.equals("")) {
            info.setVname(null);
        } else {
            info.setVname(carNumber.toUpperCase());
        }
        info.setHylb(inspect_hangye.getText().toString());//type
        info.setZfdwmc(null);
        info.setCyzgz(driverLicence);
        info.setDriverName(driverName);
        info.setStartTime(startTime + "");
        info.setEndTime(endTime + "");
        info.setZfzh(new UserPreference(act).getString("user_name", null));
        info.setStartIndex(0);
        info.setEndIndex(10);
        info.setCode("");
        if (!TextUtils.isEmpty(case_type.getText()) && (case_type.getText().toString().contains("处罚") || case_type.getText().toString().contains("警告"))) {
            AllCaseQ allCaseQ = new AllCaseQ();
            allCaseQ.setVNAME(info.getVname());
            allCaseQ.setSTARTUTC(info.getStartTime());
            allCaseQ.setENDUTC(info.getEndTime());
            allCaseQ.setHYLB(info.getHylb());
            allCaseQ.setZFZH(info.getZfzh());
            if (case_type.getText().toString().contains("警告")) {
                allCaseQ.setCFFS("警告");
            } else {
                allCaseQ.setCFFS("处罚");
            }
            if (case_type.getText().toString().contains("内勤")) {
                allCaseQ.setSTATUS("1");
            } else {
                allCaseQ.setSTATUS("0");
            }
            allCaseQ.setSTARTINDEX("0");
            allCaseQ.setENDINDEX("20");
            selectHistoryCaseRecord(pd, allCaseQ);
        } else {
            if (case_type.getText().toString().contains("正常")) {
                info.setStatus("0");
            } else if (case_type.getText().toString().contains("表扬")) {
                info.setStatus("2");
            } else {
                info.setStatus("3");
            }
            selectHistoryInspectRecord(startTime, endTime, carNumber, driverName, driverLicence, pd, info);
        }
    }

    /*
     * 案件历史记录查询
     */
    private void selectHistoryCaseRecord(final MyProgressDialog pd, final AllCaseQ info) {
        AsyncUtil.goAsync(new Callable<Result<List<Case>>>() {

            @Override
            public Result<List<Case>> call() throws Exception {
                return WeiZhanData.queryHistoryCaseRecordInfo(info);
            }
        }, new Callback<Result<List<Case>>>() {

            @Override
            public void onHandle(Result<List<Case>> result) {
                if (result.ok()) {
                    if (result.getData() == null || result.getData().toString().equals("[]")) {
                        pd.dismiss();
                        UIUtils.toast(act, "未查询到案件记录", Toast.LENGTH_LONG);
                        return;
                    }
                    if (1 == result.getData().size()) {
                        getBlTimeList(pd, result.getData().get(0));
                    } else {
                        pd.dismiss();
                        Intent intent = new Intent(act, CaseHistoryListActivity.class);
                        intent.putExtra("caseInfo", info);
                        startActivity(intent);
                    }
                } else {
                    pd.dismiss();
                    UIUtils.toast(act, result.getErrorMsg(), Toast.LENGTH_LONG);
                }
            }
        });
    }

    private void getBlTimeList(final MyProgressDialog pd,final Case c) {
        AsyncUtil.goAsync(new Callable<Result<List<UTC>>>() {

            @Override
            public Result<List<UTC>> call() throws Exception {
                return WeiZhanData.queryBlTimeInfo(new CaseID(c.getID()));
            }
        }, new Callback<Result<List<UTC>>>() {

            @Override
            public void onHandle(Result<List<UTC>> result) {
                if (result.ok()) {
                    List<UTC> utcs= result.getData();
                    if(utcs != null && !utcs.isEmpty()){
                        for(int i =0;i<utcs.size();i++){
                            if(Config.T_ZPDZ.equals(utcs.get(i).getName())){
                                getCaseBlList(pd,"无".equals(utcs.get(i).getStartUTC()),c);
                                break;
                            }
                        }
                    }
                } else {
                    pd.dismiss();
                    UIUtils.toast(act, "查询失败", Toast.LENGTH_LONG);
                }
            }
        });
    }

    private void getCaseBlList(final MyProgressDialog pd,final boolean isExist,final Case c) {
        AsyncUtil.goAsync(new Callable<Result<List<BlType>>>() {

            @Override
            public Result<List<BlType>> call() throws Exception {
                return WeiZhanData.queryCaseBlInfo(new CaseID(c.getID()));
            }
        }, new Callback<Result<List<BlType>>>() {

            @Override
            public void onHandle(Result<List<BlType>> result) {
                pd.dismiss();
                if (result.ok()) {
                    ArrayList<BlType> types = new ArrayList<>();
                    ArrayList<String> colNames = new ArrayList<>();
                    for (int i = 0; i < result.getData().size(); i++) {
                        if (!colNames.contains(result.getData().get(i).getCOLUMNNAME())) {
                            colNames.add(result.getData().get(i).getCOLUMNNAME());
                            types.add(result.getData().get(i));
                        }
                        if(Config.T_ZPDZ.equals(result.getData().get(i).getTABLENAME()) && isExist){
                            types.remove(result.getData().get(i));
                        }
                    }

                    CacheManager.getInstance().putPrintTimes(types);
                    CacheManager.getInstance().putCase(c);
                    Intent intent = new Intent(act, CaseBlListActivity.class);
                    intent.putParcelableArrayListExtra("bl_types", types);
                    startActivity(intent);
                } else {
                    UIUtils.toast(act, result.getErrorMsg(), Toast.LENGTH_LONG);
                }
            }
        });
    }

    /*
     * 稽查历史记录查询
     */
    private void selectHistoryInspectRecord(final long startTime, final long endTime, final String carNumber, final String driverName, final String driverLicence, final MyProgressDialog pd, final HistoryInspectCountRecordInfo info) {
        AsyncUtil.goAsync(new Callable<Result<List<HistoryInspectRecordModelNew>>>() {

            @Override
            public Result<List<HistoryInspectRecordModelNew>> call() throws Exception {
                return WeiZhanData.queryHistoryInsepctRecordInfoNew(info);
            }
        }, new Callback<Result<List<HistoryInspectRecordModelNew>>>() {

            @Override
            public void onHandle(Result<List<HistoryInspectRecordModelNew>> result) {
                pd.dismiss();
                if (result.ok()) {
                    if (result.getData() == null || result.getData().toString().equals("[]")) {
                        UIUtils.toast(act, "未查询到稽查记录", Toast.LENGTH_LONG);
                        return;
                    }
                    if (1 == result.getData().get(0).getTotalNum() || result.getData().size() == 1) {
                        findInfo(result.getData().get(0));
                    } else {
                        Intent intent = new Intent(act, HistoryInspcetRecordActivity.class);
                        intent.putExtra("car_number", carNumber.toUpperCase());
                        intent.putExtra("type", inspect_hangye.getText().toString());
                        intent.putExtra("driver_name", driverName);
                        intent.putExtra("number_chongye", driverLicence);
                        intent.putExtra("status", info.getStatus());
                        intent.putExtra("start", startTime);
                        intent.putExtra("end", endTime);
                        startActivity(intent);
                    }
                } else {
                    UIUtils.toast(act, result.getErrorMsg(), Toast.LENGTH_LONG);
                }
            }
        });
    }

    private void findInfo(final HistoryInspectRecordModelNew record) {
        isOnGoing = true;
        if (record.getVname() != null && !record.getVname().equals("")) {
            final VehicleInfo infox = new VehicleInfo();
            infox.setVname(record.getVname().toUpperCase());
            infox.setStartIndex(0);
            infox.setEndIndex(10);
            AsyncUtil.goAsync(new Callable<Result<List<VehicleInfo>>>() {

                @Override
                public Result<List<VehicleInfo>> call() throws Exception {
                    return WeiZhanData.queryVehicleInfo(infox);
                }
            }, new Callback<Result<List<VehicleInfo>>>() {

                @Override
                public void onHandle(Result<List<VehicleInfo>> result) {
                    if (result.ok()) {
                        if (result.getData().toString().equals("[]")) {
                            // UIUtils.toast(act, "未查询到稽查记录",
                            // Toast.LENGTH_SHORT);
                            return;
                        }
                        if (result.getData().size() != 0) {
                            color = result.getData().get(0).getVehicleColor();
                            carType = result.getData().get(0).getVehicleType();
                            carCompany = result.getData().get(0).getCompany();
                            isSuccful = true;
                        }

                    } else {
                        UIUtils.toast(act, result.getErrorMsg(), Toast.LENGTH_SHORT);
                    }
                }
            });
        }

        if (record.getCorpname() != null && !record.getCorpname().equals("")) {
            final CompanyInfo info = new CompanyInfo();
            info.setCompanyName(record.getCorpname());
            info.setStartIndex(0);
            info.setEndIndex(10);
            AsyncUtil.goAsync(new Callable<Result<List<CompanyInfo>>>() {

                @Override
                public Result<List<CompanyInfo>> call() throws Exception {
                    return WeiZhanData.queryYeHuInfo(info);
                }
            }, new Callback<Result<List<CompanyInfo>>>() {

                @Override
                public void onHandle(Result<List<CompanyInfo>> result) {
                    if (result.ok()) {
                        if (result.getData().toString().equals("[]")) {
                            // UIUtils.toast(act, "未查询到稽查记录",
                            // Toast.LENGTH_SHORT);
                            return;
                        }
                        if (0 != result.getData().size()) {
                            isSfCompany = true;
                            jyxkz = result.getData().get(0).getYehuLicenceNumber();
                            frdb = result.getData().get(0).getFarenDb();
                        }
                        if (record.getJdkh() != null && !record.getJdkh().equals("")) {
                            QueryDriverInfo(record, true);
                        } else {
                            QueryDriverInfo(record, false);
                        }
                    } else {
                        UIUtils.toast(act, result.getErrorMsg(), Toast.LENGTH_SHORT);
                    }
                }
            });
        } else {
            if (record.getJdkh() != null && !record.getJdkh().equals("")) {
                QueryDriverInfo(record, true);
            } else {
                QueryDriverInfo(record, false);
            }
        }

    }

    private void QueryDriverInfo(final HistoryInspectRecordModelNew record, boolean query) {
        if (query) {
            final DriverInfo info = new DriverInfo();
            // info.setDriverName(name);
            info.setJianduNumber(record.getJdkh());
            info.setStartIndex(0);
            info.setEndIndex(10);
            AsyncUtil.goAsync(new Callable<Result<List<DriverInfo>>>() {

                @Override
                public Result<List<DriverInfo>> call() throws Exception {
                    return WeiZhanData.queryDriverInfo(info);
                }
            }, new Callback<Result<List<DriverInfo>>>() {

                @Override
                public void onHandle(Result<List<DriverInfo>> result) {
                    if (result.ok()) {
                        if (result.getData().toString().equals("[]")) {
                        }
                        if (result.getData().size() != 0) {
                            sfzh = result.getData().get(0).getId();
                            company = result.getData().get(0).getCompanyName();
                        }
                        goTO(record);
                    } else {
                        UIUtils.toast(act, result.getErrorMsg(), Toast.LENGTH_SHORT);
                    }
                }
            });
        } else {
            goTO(record);
        }
    }

    private void goTO(final HistoryInspectRecordModelNew record) {
        isOnGoing = false;
        Intent intent = new Intent(act, InspectRecordDetailActivity.class);
        intent.putExtra("HistoryInspectRecordModelNew", record);
        intent.putExtra("color", color);
        intent.putExtra("carType", carType);
        intent.putExtra("sfzh", sfzh);
        intent.putExtra("company", company);
        intent.putExtra("carCompany", carCompany);
        intent.putExtra("jyxkz", jyxkz);
        intent.putExtra("frdb", frdb);
        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
        isSuccful = false;
        isSfCompany = false;
    }

}
