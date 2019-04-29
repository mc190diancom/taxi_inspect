package com.miu360.taxi_check.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.ChooseInputType;
import com.miu360.taxi_check.common.CommonDialog.OnDialogItemClickListener;
import com.miu360.taxi_check.common.DateVer;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.Driver;
import com.miu360.taxi_check.model.HYLBInfo;
import com.miu360.taxi_check.model.HuoYunWeiFaInfo;
import com.miu360.taxi_check.model.LvYouWeiFaInfo;
import com.miu360.taxi_check.model.ShengJiWeiFaInfo;
import com.miu360.taxi_check.model.WaterTranspt;
import com.miu360.taxi_check.model.WaterTransptQ;
import com.miu360.taxi_check.model.WeiFaInfo;
import com.miu360.taxi_check.model.WeiFa_New;
import com.miu360.taxi_check.model.WeiFa_NewQ;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

public class WeiZhangQueryActivity extends BaseActivity implements OnClickListener {

    /*
     * @ViewInject(R.id.saomiao_one) private ImageButton saomiaoone;
     *
     * @ViewInject(R.id.saomiao_two) private ImageButton saomiaotwo;
     */
    @ViewInject(R.id.basic_query_btn)
    private Button btn_query;
    @ViewInject(R.id.back)
    private Button btn_back;
    @ViewInject(R.id.industry)
    private TextView industry_et;
    @ViewInject(R.id.license_plate)
    private EditText license_plateEt;
    @ViewInject(R.id.name)
    private EditText nameEt;
    @ViewInject(R.id.number_card)
    private EditText number_cardEt;
    @ViewInject(R.id.unit_name)
    private EditText unit_nameEt;
    @ViewInject(R.id.start_time)
    private TextView start_timeTv;
    @ViewInject(R.id.end_time)
    private TextView end_timeTv;

    @ViewInject(R.id.ll_vname)
    private LinearLayout ll_vname;
    @ViewInject(R.id.ll_driver)
    private LinearLayout ll_driver;
    @ViewInject(R.id.ll_number)
    private LinearLayout ll_number;
    @ViewInject(R.id.ll_company)
    private LinearLayout ll_company;
    @ViewInject(R.id.ll_time)
    private LinearLayout ll_time;
    @ViewInject(R.id.tv_cphm)
    private TextView tv_cphm;

    String[] items = {"巡游", "旅游", "省际", "货运", "化危", "租赁", "水运", "维修"};
    String[] item = {"近1年", "近2年", "全部"};
    String car_Vname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei_zhang_query);
        car_Vname = getIntent().getStringExtra("car_Vname");
        initView();

    }

    public void initView() {
        ViewUtils.inject(self);
        new HeaderHolder().init(self, "违法信息");
        /*
         * saomiaoone.setOnClickListener(this);
         * saomiaotwo.setOnClickListener(this);
         */


        btn_query.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        industry_et.setOnClickListener(this);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -(Calendar.getInstance().get(Calendar.YEAR) - 1970));
        start_timeTv.setTag(calendar.getTimeInMillis());
        start_timeTv.setOnClickListener(this);
        end_timeTv.setOnClickListener(this);
        end_timeTv.setText("");
        end_timeTv.setTag(System.currentTimeMillis());
        license_plateEt.setText(car_Vname);
        ChooseInputType input = new ChooseInputType();
        input.init(unit_nameEt);
        input.init(nameEt);
        initDate();
    }

    private void initDate() {
        final MyProgressDialog pd = Windows.waiting(self);
        AsyncUtil.goAsync(new Callable<Result<List<HYLBInfo>>>() {

            @Override
            public Result<List<HYLBInfo>> call() throws Exception {
                return WeiZhanData.queryHYLB();
            }
        }, new Callback<Result<List<HYLBInfo>>>() {

            @Override
            public void onHandle(Result<List<HYLBInfo>> result) {
                pd.dismiss();
                if (result.ok()) {
                    if (result.getData() == null || result.getData().toString().equals("[]")) {
                        //UIUtils.toast(self, "查不到此信息", Toast.LENGTH_SHORT);
                        return;
                    }
                    items = new String[result.getData().size() + 1];
                    items[0] = "全部";
                    for (int i = 0, len = result.getData().size(); i < len; i++) {
                        items[i + 1] = result.getData().get(i).getHYLB();
                    }
                    industry_et.setText(items[0]);
                } else {
                    UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 0 || requestCode == 1) {
            Driver d = (Driver) data.getSerializableExtra("result");
            nameEt.setText(d.getName());
            number_cardEt.setText(d.getDriverLicence());
        }
    }

    ArrayList<WeiFaInfo> arrayList;
    String dangShiRen;
    String car_number;
    String ziGeZH;
    String companyName;
    String industry;
    String start_time;
    String over_time;

    @Override
    public void onClick(View v) {
        /*
         * if (v == saomiaoone) { Intent intent = new Intent(self,
         * MipcaActivityCapture.class); startActivityForResult(intent, 0); }
         * else if (v == saomiaotwo) { Intent intent = new Intent(self,
         * MipcaActivityCapture.class); startActivityForResult(intent, 1); }
         * else
         */
        if (v == industry_et) {
            System.err.println("items.length=" + items.length);
            if (items == null || items.length == 0) {
                return;
            }
            Windows.singleChoice(self, "选择类别", items, new OnDialogItemClickListener() {
                @Override
                public void dialogItemClickListener(int position) {
                    industry_et.setText(items[position]);
                    if ("租赁".contains(industry_et.getText().toString()) || "水运".contains(industry_et.getText().toString()) || "维修".contains(industry_et.getText().toString())) {
                        ll_driver.setVisibility(View.GONE);
                        ll_number.setVisibility(View.GONE);
                        ll_vname.setVisibility(View.GONE);
                    } else {
                        ll_driver.setVisibility(View.VISIBLE);
                        ll_number.setVisibility(View.VISIBLE);
                        ll_vname.setVisibility(View.VISIBLE);

                    }

                    if ("水运".contains(industry_et.getText().toString()) || "维修".contains(industry_et.getText().toString())) {
                        ll_vname.setVisibility(View.GONE);
                    } else {
                        ll_vname.setVisibility(View.VISIBLE);
                    }


//							if("水运".contains(industry_et.getText().toString()) || "维修".contains(industry_et.getText().toString())){
//								ll_vname.setVisibility(View.GONE);
//								vname_view.setVisibility(View.GONE);
//								car_number_two.setText("");
//							}else{
//								vname_ll.setVisibility(View.VISIBLE);
//								vname_view.setVisibility(View.VISIBLE);
//							}


                }
            });

        } else if (v == btn_query) {
            dangShiRen = nameEt.getText().toString();
            car_number = license_plateEt.getText().toString();
            ziGeZH = number_cardEt.getText().toString();
            companyName = unit_nameEt.getText().toString();
            industry = industry_et.getText().toString();
            start_time = new SimpleDateFormat(datePatter).format(new Date((long) start_timeTv.getTag()));
            over_time = new SimpleDateFormat(datePatter).format(new Date((long) end_timeTv.getTag()));

            if (TextUtils.isEmpty(dangShiRen) && TextUtils.isEmpty(car_number) && TextUtils.isEmpty(ziGeZH)
                    && TextUtils.isEmpty(companyName)) {
                UIUtils.toast(self, "至少填写一项", Toast.LENGTH_SHORT);
                return;
            }
            if (DateVer.VerStartCurrentDate(self, (long) start_timeTv.getTag())) {
                return;
            }
            if (DateVer.VerEndCurrentDate(self, (long) end_timeTv.getTag())) {
                return;
            }
            if (DateVer.VerStartDate(self, (long) start_timeTv.getTag(), (long) end_timeTv.getTag())) {
                return;
            }
            if (TextUtils.isEmpty(start_timeTv.getText().toString())) {
                start_time = "197001010000";
            }
            if (TextUtils.isEmpty(end_timeTv.getText().toString())) {
                over_time = new SimpleDateFormat(datePatter).format(new Date(System.currentTimeMillis()));
            }

            if (industry.equals("水运") || industry.equals("维修")) {

            } else {
                if (car_number.length() < 3 && TextUtils.isEmpty(dangShiRen) && TextUtils.isEmpty(ziGeZH)
                        && TextUtils.isEmpty(companyName)) {
                    UIUtils.toast(self, "至少输入3位至以上查询字符", Toast.LENGTH_SHORT);
                    return;
                }
                if (ziGeZH.length() < 3 && TextUtils.isEmpty(dangShiRen) && TextUtils.isEmpty(car_number)
                        && TextUtils.isEmpty(companyName)) {
                    UIUtils.toast(self, "至少输入3位至以上查询字符", Toast.LENGTH_SHORT);
                    return;
                }
                if (companyName.length() < 3 && TextUtils.isEmpty(dangShiRen) && TextUtils.isEmpty(ziGeZH)
                        && TextUtils.isEmpty(car_number)) {
                    UIUtils.toast(self, "至少输入3位至以上查询字符", Toast.LENGTH_SHORT);
                    return;
                }
            }
            query();

        } else if (v == start_timeTv) {

            Windows.singleChoice(self, "选择检查时间", item, new OnDialogItemClickListener() {

                @Override
                public void dialogItemClickListener(int position) {
                    Calendar calendar = Calendar.getInstance();
                    switch (position) {
                        case 0:
                            calendar.add(Calendar.YEAR, -1);
                            break;
                        case 1:
                            calendar.add(Calendar.YEAR, -2);
                            break;
                        case 2:
                            calendar.add(Calendar.YEAR, -(Calendar.getInstance().get(Calendar.YEAR) - 1970));
                            break;
                        default:
                            break;
                    }
                    start_timeTv.setText(item[position]);
                    start_timeTv.setTag(calendar.getTimeInMillis());
                    end_timeTv.setTag(new Date().getTime());
                }
            });
        } else if (v == btn_back) {
            finish();
        }
    }

    private void query() {
        final MyProgressDialog pd = Windows.waiting(self);
        final ArrayList<WeiFa_New> arrayListInfo = new ArrayList<>();
        final WeiFa_NewQ info = new WeiFa_NewQ();
        if ("全部".equals(industry)) {
            info.setHylb("");
        } else {
            info.setHylb(industry);
        }
        info.setStartUtc(start_time);
        info.setEndUtc(over_time);
        info.setDsr(dangShiRen);
        info.setZjzh(ziGeZH);
        info.setClph(car_number.toUpperCase());
        info.setCompName(companyName);

        AsyncUtil.goAsync(new Callable<Result<List<WeiFa_New>>>() {

            @Override
            public Result<List<WeiFa_New>> call() throws Exception {
                return WeiZhanData.queryWeiFaInfo_New(info);
            }
        }, new Callback<Result<List<WeiFa_New>>>() {

            @Override
            public void onHandle(Result<List<WeiFa_New>> result) {
                pd.dismiss();
                if (result.ok()) {
                    if (result.getData() == null || result.getData().isEmpty() || result.getData().toString().equals("[]")) {
                        UIUtils.toast(self, "查不到此信息", Toast.LENGTH_SHORT);
                        return;
                    }
                    arrayListInfo.addAll(result.getData());
                    if (result.getData().size() == 1) {
                        Intent intent = new Intent(self, WeiZhangInfoActivity.class);
                        intent.putExtra("weifaInfo", arrayListInfo.get(0));
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(self, WeiZhangQueryResultInfoActivity.class);
                        intent.putExtra("hylb", industry);
                        intent.putExtra("dangShiRen", dangShiRen);
                        intent.putExtra("car_number", car_number.toUpperCase());
                        intent.putExtra("ziGeZH", ziGeZH);
                        intent.putExtra("companyName", companyName);
                        intent.putExtra("start_time", start_time);
                        intent.putExtra("over_time", over_time);
                        startActivity(intent);
                    }
                } else {
                    UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private final String datePatterShow = "yyyy-MM-dd HH:mm";
    private final String datePatter = "yyyyMMddHHmm";

    private void query1() {
        final MyProgressDialog d = Windows.waiting(self);
        arrayList = new ArrayList<>();
        final WeiFaInfo info = new WeiFaInfo();
        info.setVname(car_number.toUpperCase());
        info.setDanshiren(dangShiRen);
        info.setJdkh(ziGeZH);
        info.setCorpName(companyName);
        info.setCheckTime(start_time);
        info.setOverTime(over_time);
        info.setHylb(industry);
        info.setStartIndex(0);
        info.setEndIndex(10);
        AsyncUtil.goAsync(new Callable<Result<List<WeiFaInfo>>>() {

            @Override
            public Result<List<WeiFaInfo>> call() throws Exception {
                return WeiZhanData.queryWeiFaInfo(info);
            }
        }, new Callback<Result<List<WeiFaInfo>>>() {

            @Override
            public void onHandle(Result<List<WeiFaInfo>> result) {
                d.dismiss();
                if (result.ok()) {
                    if (result.getData().toString().equals("[]")) {
                        UIUtils.toast(self, "查不到此信息", Toast.LENGTH_LONG);
                        return;
                    }
                    arrayList.addAll(result.getData());
                    if (result.getData().get(0).getTotalNum() == 1) {
                        Intent intent = new Intent(self, WeiZhangInfoActivity.class);
                        intent.putExtra("weifaInfo", arrayList.get(0));
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(self, WeiZhangQueryResultInfoActivity.class);
                        intent.putExtra("name", dangShiRen);
                        intent.putExtra("industry", industry);
                        intent.putExtra("car_number", car_number.toUpperCase());
                        intent.putExtra("number", ziGeZH);
                        intent.putExtra("unit_name", companyName);
                        intent.putExtra("start_time", start_time);
                        intent.putExtra("over_time", over_time);
                        startActivity(intent);
                    }
                } else {
                    UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
                }
            }
        });
    }

    // 查询旅游
    private void query2() {
        final MyProgressDialog pd = Windows.waiting(self);
        final ArrayList<LvYouWeiFaInfo> arrayListInfo = new ArrayList<>();
        final LvYouWeiFaInfo info = new LvYouWeiFaInfo();
        info.setHylb("旅游");
        info.setVname(car_number);
        info.setDanshiren(dangShiRen);
        info.setJdkh(ziGeZH);
        info.setCorpName(companyName);
        info.setCheckTime(start_time);
        info.setOverTime(over_time);
        info.setStartIndex(0);
        info.setEndIndex(20);
        AsyncUtil.goAsync(new Callable<Result<List<LvYouWeiFaInfo>>>() {

            @Override
            public Result<List<LvYouWeiFaInfo>> call() throws Exception {
                return WeiZhanData.queryLvYouWeiFaInfo(info);
            }
        }, new Callback<Result<List<LvYouWeiFaInfo>>>() {

            @Override
            public void onHandle(Result<List<LvYouWeiFaInfo>> result) {
                pd.dismiss();

                if (result.ok()) {
                    if (result.getData().toString().equals("[]")) {
                        UIUtils.toast(self, "查不到此信息", Toast.LENGTH_SHORT);
                        return;
                    }
                    arrayListInfo.addAll(result.getData());
                    if (result.getData().get(0).getTotalNum() == 1) {
                        Intent intent = new Intent(self, WeiZhangLvYouInfoActivity.class);
                        intent.putExtra("LvYouWeiFaInfo", arrayListInfo.get(0));
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(self, WeiZhangQueryLvYouResultInfoActivity.class);
                        intent.putExtra("hylb", "旅游");
                        intent.putExtra("dangShiRen", dangShiRen);
                        intent.putExtra("car_number", car_number.toUpperCase());
                        intent.putExtra("ziGeZH", ziGeZH);
                        intent.putExtra("companyName", companyName);
                        intent.putExtra("start_time", start_time);
                        intent.putExtra("over_time", over_time);
                        startActivity(intent);
                    }
                } else {
                    UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
                }
            }
        });
    }

    // 查询省际
    private void query3() {
        final MyProgressDialog pd = Windows.waiting(self);
        final ArrayList<ShengJiWeiFaInfo> arrayListInfo = new ArrayList<>();
        final ShengJiWeiFaInfo info = new ShengJiWeiFaInfo();
        info.setHylb("客运");
        info.setVname(car_number);
        info.setDanshiren(dangShiRen);
        info.setJdkh(ziGeZH);
        info.setCorpName(companyName);
        info.setCheckTime(start_time);
        info.setOverTime(over_time);
        info.setStartIndex(0);
        info.setEndIndex(20);
        AsyncUtil.goAsync(new Callable<Result<List<ShengJiWeiFaInfo>>>() {

            @Override
            public Result<List<ShengJiWeiFaInfo>> call() throws Exception {
                return WeiZhanData.queryShengJiWeiFaInfo(info);
            }
        }, new Callback<Result<List<ShengJiWeiFaInfo>>>() {

            @Override
            public void onHandle(Result<List<ShengJiWeiFaInfo>> result) {
                pd.dismiss();
                if (result.ok()) {
                    if (result.getData().toString().equals("[]")) {
                        UIUtils.toast(self, "查不到此信息", Toast.LENGTH_SHORT);
                        return;
                    }
                    arrayListInfo.addAll(result.getData());
                    if (result.getData().get(0).getTotalNum() == 1) {
                        Intent intent = new Intent(self, WeiZhangShengJiInfoActivity.class);
                        intent.putExtra("ShengJiWeiFaInfo", arrayListInfo.get(0));
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(self, WeiZhangQueryShengJiResultInfoActivity.class);
                        intent.putExtra("hylb", "客运");
                        intent.putExtra("dangShiRen", dangShiRen);
                        intent.putExtra("car_number", car_number.toUpperCase());
                        intent.putExtra("ziGeZH", ziGeZH);
                        intent.putExtra("companyName", companyName);
                        intent.putExtra("start_time", start_time);
                        intent.putExtra("over_time", over_time);
                        startActivity(intent);
                    }
                } else {
                    UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
                }
            }
        });
    }

    // 查询货运
    private void query4() {
        final MyProgressDialog pd = Windows.waiting(self);
        final ArrayList<HuoYunWeiFaInfo> arrayListInfo = new ArrayList<>();
        final HuoYunWeiFaInfo info = new HuoYunWeiFaInfo();
        info.setHylb("货运");
        info.setVname(car_number);
        info.setDanshiren(dangShiRen);
        info.setCyzgz(ziGeZH);

        info.setCorpName(companyName);
        info.setCheckTime(start_time);
        info.setOverTime(over_time);
        info.setStartIndex(0);
        info.setEndIndex(20);
        AsyncUtil.goAsync(new Callable<Result<List<HuoYunWeiFaInfo>>>() {

            @Override
            public Result<List<HuoYunWeiFaInfo>> call() throws Exception {
                return WeiZhanData.queryHuoYunWeiFaInfo(info);
            }
        }, new Callback<Result<List<HuoYunWeiFaInfo>>>() {

            @Override
            public void onHandle(Result<List<HuoYunWeiFaInfo>> result) {
                pd.dismiss();

                if (result.ok()) {
                    if (result.getData().toString().equals("[]")) {
                        UIUtils.toast(self, "查不到此信息", Toast.LENGTH_SHORT);
                        return;
                    }
                    arrayListInfo.addAll(result.getData());
                    if (result.getData().get(0).getTotalNum() == 1) {
                        Intent intent = new Intent(self, WeiZhangHuoYunInfoActivity.class);
                        intent.putExtra("HuoYunWeiFaInfo", arrayListInfo.get(0));
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(self, WeiZhangQueryHuoYunResultInfoActivity.class);
                        intent.putExtra("hylb", "货运");
                        intent.putExtra("dangShiRen", dangShiRen);
                        intent.putExtra("car_number", car_number.toUpperCase());
                        intent.putExtra("ziGeZH", ziGeZH);
                        intent.putExtra("companyName", companyName);
                        intent.putExtra("start_time", start_time);
                        intent.putExtra("over_time", over_time);
                        startActivity(intent);
                    }
                } else {
                    UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
                }
            }
        });

    }

    // 查询化危
    private void query5() {
        final MyProgressDialog pd = Windows.waiting(self);
        final ArrayList<HuoYunWeiFaInfo> arrayListInfo = new ArrayList<>();
        final HuoYunWeiFaInfo info = new HuoYunWeiFaInfo();
        info.setHylb("危险");
        info.setVname(car_number);
        info.setDanshiren(dangShiRen);
        info.setCyzgz(ziGeZH);

        info.setCorpName(companyName);
        info.setCheckTime(start_time);
        info.setOverTime(over_time);
        info.setStartIndex(0);
        info.setEndIndex(20);
        AsyncUtil.goAsync(new Callable<Result<List<HuoYunWeiFaInfo>>>() {

            @Override
            public Result<List<HuoYunWeiFaInfo>> call() throws Exception {
                return WeiZhanData.queryHuoYunWeiFaInfo(info);
            }
        }, new Callback<Result<List<HuoYunWeiFaInfo>>>() {

            @Override
            public void onHandle(Result<List<HuoYunWeiFaInfo>> result) {
                pd.dismiss();

                if (result.ok()) {
                    if (result.getData().toString().equals("[]")) {
                        UIUtils.toast(self, "查不到此信息", Toast.LENGTH_SHORT);
                        return;
                    }
                    arrayListInfo.addAll(result.getData());
                    if (result.getData().get(0).getTotalNum() == 1) {
                        Intent intent = new Intent(self, WeiZhangHuoYunDangerousInfoActivity.class);
                        intent.putExtra("HuoYunWeiFaInfo", arrayListInfo.get(0));
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(self, WeiZhangQueryHuoYunDangerousResultInfoActivity.class);
                        intent.putExtra("hylb", "危险");
                        intent.putExtra("dangShiRen", dangShiRen);
                        intent.putExtra("car_number", car_number.toUpperCase());
                        intent.putExtra("ziGeZH", ziGeZH);
                        intent.putExtra("companyName", companyName);
                        intent.putExtra("start_time", start_time);
                        intent.putExtra("over_time", over_time);
                        startActivity(intent);
                    }
                } else {
                    UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
                }
            }
        });
    }

    // 查询租赁
    private void query6() {
        final MyProgressDialog pd = Windows.waiting(self);
        new Thread() {

            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pd.dismiss();
                UIUtils.toast(self, "无违法信息", Toast.LENGTH_SHORT);
            }

        }.start();
    }

    // 查询水运
    private void query7() {
        final MyProgressDialog pd = Windows.waiting(self);
        final ArrayList<WaterTranspt> arrayListInfo = new ArrayList<>();
        final WaterTransptQ info = new WaterTransptQ();
        info.setHylb("水运");
        info.setCompName(companyName);
        AsyncUtil.goAsync(new Callable<Result<List<WaterTranspt>>>() {

            @Override
            public Result<List<WaterTranspt>> call() throws Exception {
                return WeiZhanData.queryWaterAndWeiXiuWeiFaInfo(info);
            }
        }, new Callback<Result<List<WaterTranspt>>>() {

            @Override
            public void onHandle(Result<List<WaterTranspt>> result) {
                pd.dismiss();
                if (result.ok()) {
                    if (result.getData().toString().equals("[]")) {
                        UIUtils.toast(self, "查不到此信息", Toast.LENGTH_SHORT);
                        return;
                    }
                    arrayListInfo.addAll(result.getData());
                    if (arrayListInfo.size() == 1) {
                        Intent intent = new Intent(self, WeiZhangShuiYunAndWeiXiuInfoActivity.class);
                        intent.putExtra("WaterAndWeiXiuWeiFaInfo", arrayListInfo.get(0));
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(self, WeiZhangQueryShuiYunResultInfoActivity.class);
                        intent.putExtra("hylb", "水运");
                        intent.putExtra("companyName", companyName);
                        startActivity(intent);
                    }
                } else {
                    UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
                }
            }
        });

    }

    // 查询维修
    private void query8() {
        final MyProgressDialog pd = Windows.waiting(self);
        final ArrayList<WaterTranspt> arrayListInfo = new ArrayList<>();
        final WaterTransptQ info = new WaterTransptQ();
        info.setHylb("维修");
        info.setCompName(companyName);
        AsyncUtil.goAsync(new Callable<Result<List<WaterTranspt>>>() {

            @Override
            public Result<List<WaterTranspt>> call() throws Exception {
                return WeiZhanData.queryWaterAndWeiXiuWeiFaInfo(info);
            }
        }, new Callback<Result<List<WaterTranspt>>>() {

            @Override
            public void onHandle(Result<List<WaterTranspt>> result) {
                pd.dismiss();
                if (result.ok()) {
                    if (result.getData().toString().equals("[]")) {
                        UIUtils.toast(self, "查不到此信息", Toast.LENGTH_SHORT);
                        return;
                    }
                    arrayListInfo.addAll(result.getData());
                    if (arrayListInfo.size() == 1) {
                        Intent intent = new Intent(self, WeiZhangShuiYunAndWeiXiuInfoActivity.class);
                        intent.putExtra("WaterAndWeiXiuWeiFaInfo", arrayListInfo.get(0));
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(self, WeiZhangQueryShuiYunResultInfoActivity.class);
                        intent.putExtra("hylb", "维修");
                        intent.putExtra("companyName", companyName);
                        startActivity(intent);
                    }
                } else {
                    UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
                }
            }
        });
    }
}
