package com.miu30.common.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.miu30.common.async.AsyncUtil;
import com.miu30.common.async.Callback;
import com.miu30.common.async.ExceptionHandler;
import com.miu30.common.async.Result;
import com.miu30.common.config.Config;
import com.miu30.common.config.Constance;
import com.miu30.common.config.MsgConfig;
import com.miu30.common.data.CommonData;
import com.miu30.common.data.UserPreference;
import com.miu30.common.ui.adapter.LocationAdapter;
import com.miu30.common.ui.entity.AddMsg;
import com.miu30.common.ui.entity.HistoryInspectCountRecordInfo;
import com.miu30.common.ui.entity.HistoryInspectRecordModel;
import com.miu30.common.ui.entity.LatLngMsg;
import com.miu30.common.ui.entity.LocationDetial;
import com.miu30.common.util.ClearEditText;
import com.miu30.common.util.FullList2View;
import com.miu30.common.util.GpsManger;
import com.miu30.common.util.UIUtils;
import com.miu30.common.util.Windows;
import com.miu30.common.util.isCommon;
import com.miu360.library.R;
import com.miu360.library.R2;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LawSelectLocationActivity extends Activity
        implements TextWatcher, OnClickListener, OnItemClickListener {


    @BindView(R2.id.left_btn)
    ImageButton leftBtn;
    @BindView(R2.id.iv_mine)
    LinearLayout ivMine;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.sure)
    TextView sure;
    @BindView(R2.id.title)
    RelativeLayout title;
    @BindView(R2.id.common_address)
    TextView commonAddress;
    @BindView(R2.id.common)
    LinearLayout common;
    @BindView(R2.id.address_history)
    FullList2View addressHistory;
    @BindView(R2.id.search_edit)
    ClearEditText searchEdit;
    @BindView(R2.id.aim_location)
    ImageButton aimLocation;
    @BindView(R2.id.divider)
    View divider;
    @BindView(R2.id.address_list)
    ListView addressList;
    @BindView(R2.id.address_history_list)
    ListView addressHistoryList;
    private LocationAdapter adapter;

    private String add = "";
    private ArrayList<LocationDetial> locationDetials;

    public static String CHOSE_LOCATION = "chose_location";
    boolean isChanged = false;
    private String contextAdd = "";
    private ArrayList<String> historyList = new ArrayList<>();
    private ArrayList<LatLngMsg> historyCdList = new ArrayList<>();
    private ArrayList<LatLngMsg> historyCordList = new ArrayList<>();
    private addressAdapter addAdapter;
    // 是否常用
    boolean iscommon = false;
    boolean IsFirst = true;
    boolean addressisOK = true;
    boolean isOpenGps = false;

    //传过来的类型，用于区分eventbus回馈数据
    private String type;

    private double lat,lon;
    DbUtils dbUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_law_select_location);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化控件
     */
    protected void initView() {
        ViewUtils.inject(this);
        locationDetials = new ArrayList<>();
        adapter = new LocationAdapter(this, locationDetials);
        addressList.setAdapter(adapter);
        type = getIntent().getStringExtra("type");
        commonAddress.setOnClickListener(this);
        aimLocation.setOnClickListener(this);
        sure.setOnClickListener(this);
        addressList.setOnItemClickListener(this);
        leftBtn.setOnClickListener(this);
        add = getIntent().getStringExtra("thisAdd");
        dbUtils = DbUtils.create(LatLngMsg.getDaoConfig());
        dbUtils.configDebug(true);
        getCommonDetailInfo();
        addAdapter = new addressAdapter();
        addressHistory.setAdapter(addAdapter);
        addressHistory.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isChanged = true;
                LatLngMsg latLngMsg = historyCordList.get(position);
                lat = latLngMsg.getLat();
                lon = latLngMsg.getLng();
                contextAdd = latLngMsg.getName();
                iscommon = true;
                finish();
            }
        });
        if (!TextUtils.isEmpty(add) && !("common").equals(add)) {
            common.setVisibility(View.VISIBLE);
            searchEdit.setText(add);
        } else if (("common").equals(add)) {
            common.setVisibility(View.VISIBLE);
            initLocation(true);
        }
        searchEdit.addTextChangedListener(this);
    }

    private class addressAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return historyCordList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return historyCordList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            viewHolderLicence holder_driver;
            if (arg1 == null) {
                holder_driver = new viewHolderLicence();
                arg1 = LayoutInflater.from(LawSelectLocationActivity.this).inflate(R.layout.listaddressadapter,
                        arg2, false);
                arg1.setTag(holder_driver);

                holder_driver.addText = arg1.findViewById(R.id.addresName);

            } else {
                holder_driver = (viewHolderLicence) arg1.getTag();
            }
            holder_driver.addText.setText(historyCordList.get(arg0).getName());
            return arg1;
        }

    }

    private class viewHolderLicence {
        TextView addText;
    }

    /**
     * 定位
     */
    protected void initLocation(final boolean show) {
        searchEdit.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!isCommon.outOfChina(MsgConfig.lat, MsgConfig.lng) && MsgConfig.lat != 0.0) {
                    searchEdit.removeTextChangedListener(LawSelectLocationActivity.this);
                    reverseGeoCode1(searchEdit, MsgConfig.lat, MsgConfig.lng, show);
                    MsgConfig.select_lng = 0;
                    MsgConfig.select_lat = 0;
                } else {
                    if (!IsFirst) {
                        UIUtils.toast(LawSelectLocationActivity.this, "GPS信号差，请拿到开阔地段！", Toast.LENGTH_SHORT);
                    }
                    IsFirst = false;
                    isChanged = false;
                }
            }
        }, 1000);
    }

    /**
     * 获取数据
     */
    protected void searchData(final String text) {
        AsyncUtil.goAsync(new Callable<Result<ArrayList<LocationDetial>>>() {

            @Override
            public Result<ArrayList<LocationDetial>> call() throws Exception {
                return CommonData.qureyLocation(text);
            }
        }, new Callback<Result<ArrayList<LocationDetial>>>() {

            @Override
            public void onHandle(Result<ArrayList<LocationDetial>> result) {
                locationDetials.clear();
                if (result.ok()) {
                    locationDetials.addAll(result.getData());
                    if (locationDetials.size() > 0) {
                        adapter.notifyDataSetChanged();
                        addressisOK = true;
                    } else {
                        addressisOK = false;
                    }
                } else {
                    UIUtils.toast(LawSelectLocationActivity.this, result.getMsg(), Toast.LENGTH_SHORT);
                }

            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    private Runnable runnable;
    private int loc_type;// 状态记录，0，定位中，1，定位成功

    /**
     * 文字改变
     */
    @Override
    public void onTextChanged(final CharSequence s, int start, int before, int count) {
        addressisOK = false;
        if (s.length() == 0) {
            searchEdit.getHandler().removeCallbacks(runnable);
            locationDetials.clear();
            adapter.notifyDataSetChanged();
            MsgConfig.select_lng = 0;
            MsgConfig.select_lat = 0;
        } else {
            searchEdit.getHandler().removeCallbacks(runnable);
            runnable = new Runnable() {
                @Override
                public void run() {
                    searchData(s.toString());
                }
            };
            searchEdit.getHandler().postDelayed(runnable, 600);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 撤销
     */
    @Override
    public void onClick(View v) {
        if (v == aimLocation) {
            if (!GpsManger.isOPen(this)) {
                Windows.confirm(LawSelectLocationActivity.this, "请先开启GPS", new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        isOpenGps = true;
                        GpsManger.openGps(LawSelectLocationActivity.this);
                    }
                });
            } else {
                isChanged = true;
                initLocation(true);
            }
        } else if (v == sure) {
            contextAdd = searchEdit.getText().toString();
            if (TextUtils.isEmpty(contextAdd) && !"tvLTInspectAddress".equals(type)) {
                UIUtils.toast(LawSelectLocationActivity.this, "地址名不能设置为空", Toast.LENGTH_SHORT);
                return;
            } else {
                MsgConfig.common_lat = 0;
                MsgConfig.common_lng = 0;
            }
            isChanged = true;
            finish();
        } else if (v == commonAddress) {
            if (addressHistory.getVisibility() == View.VISIBLE) {
                addressHistory.setVisibility(View.GONE);
                return;
            }
            if(historyCordList != null && historyCordList.size() >0){
                addressHistory.setVisibility(View.VISIBLE);
                addAdapter.notifyDataSetChanged();
            }
        } else if (v == leftBtn) {
            // 如果常用地址存在经纬度，说明选择的是常用地址
            if (MsgConfig.common_lat != 0) {
                iscommon = true;
            }
            finish();
        }

    }

    /*
     * 获取常用地址信息
     */
    private void getCommonDetailInfo() {
        AsyncUtil.goAsync(new Callable<Result<List<LatLngMsg>>>() {

            @Override
            public Result<List<LatLngMsg>> call() throws Exception {
                Result<List<LatLngMsg>> ret = new Result<>();
                try {
                    List<LatLngMsg> cache = dbUtils.findAll(Selector.from(LatLngMsg.class).orderBy("id", true));
                    if (cache == null) {
                        cache = new ArrayList<>();
                    }

                    if(cache.size() > 5){//删除大于5条后面的数据
                        for(int i = cache.size()-1;i > 4;i--){
                            dbUtils.delete(LatLngMsg.class, WhereBuilder.b("id", "=", cache.get(i).getId()));
                            cache.remove(i);
                        }
                    }

                    ret.setData(cache);
                } catch (Exception e) {
                    ret.setThrowable(e);
                }
                return ret;
            }
        }, new Callback<Result<List<LatLngMsg>>>() {

            @Override
            public void onHandle(Result<List<LatLngMsg>> result) {
                if (result.ok()) {
                    try {
                        historyCordList.clear();
                        historyCordList.addAll(result.getData());
                    }catch (Exception e){

                    }
                }
            }
        });
    }

    /**
     * 通过经纬度获取位置
     */
    public void reverseGeoCode1(final TextView tv, final double lats, final double lng, final boolean show) {
        AsyncUtil.goAsync(new Callable<Result<String>>() {

            @Override
            public Result<String> call() throws Exception {
                return CommonData.queryHistoryTrack1(lats, lng);
            }
        }, new Callback<Result<String>>() {

            @Override
            public void onHandle(Result<String> result) {
                if (result.ok()) {
                    loc_type = 1;
                    if (show) {
                        tv.setText(result.getData());
                        contextAdd = result.getData();
                    }
                    lat = lats;
                    lon = lng;
                } else {
                    loc_type = 0;
                }
                searchEdit.addTextChangedListener(LawSelectLocationActivity.this);
            }
        });
    }

    /**
     * 用户自选位置
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (locationDetials.get(position).location == null) {
            Toast.makeText(this, "无法获取经纬度！", Toast.LENGTH_SHORT).show();
        } else {
            isChanged = true;
            lat = locationDetials.get(position).location.lng;
            lon = locationDetials.get(position).location.lat;
            MsgConfig.common_lat = 0;
            MsgConfig.common_lng = 0;
            contextAdd = locationDetials.get(position).name;
            finish();
        }
    }

    @Override
    protected void onResume() {
        if (isOpenGps && GpsManger.isOPen(this)) {
            isChanged = true;
            initLocation(true);
        } else {
            isOpenGps = false;
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (isChanged) {
            Intent intent = new Intent(CHOSE_LOCATION);
            intent.putExtra("shoudong_location", contextAdd);
            intent.putExtra("isCommon", iscommon);
            LatLngMsg latLngMsg = new LatLngMsg(lat,lon,contextAdd);
            if(!TextUtils.isEmpty(contextAdd)){
                try {
                    for (int i = 0; i < historyCordList.size(); i++) {
                        if(latLngMsg.getName().equals(historyCordList.get(i).getName())){
                            dbUtils.delete(LatLngMsg.class, WhereBuilder.b("id", "=", historyCordList.get(i).getId()));
                        }
                    }
                    dbUtils.save(latLngMsg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            sendBroadcast(intent);
            EventBus.getDefault().post(new AddMsg(contextAdd,iscommon,type,(long)(lat*1E6d),(long)(lon*1E6d)), Config.SELECTADD);
        }
        dbUtils.close();
        super.onDestroy();
    }

    public static Intent getLocationIntent(Activity activity,String content,String type){
        Intent intent = new Intent(activity,LawSelectLocationActivity.class);
        intent.putExtra("thisAdd",content);
        intent.putExtra("type",type);
        return intent;
    }
}
