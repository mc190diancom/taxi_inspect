package com.miu360.taxi_check.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu30.common.config.Config;
import com.miu30.common.util.MyProgressDialog;
import com.miu30.common.util.Windows;
import com.miu360.inspect.R;
import com.miu360.legworkwrit.mvp.data.CacheManager;
import com.miu360.legworkwrit.mvp.model.entity.AllCaseQ;
import com.miu360.legworkwrit.mvp.model.entity.BlType;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.mvp.model.entity.CaseID;
import com.miu360.legworkwrit.mvp.model.entity.UTC;
import com.miu360.legworkwrit.mvp.ui.activity.CaseBlListActivity;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.adapter.CasehistoryListAdapter;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;
import com.miu360.taxi_check.view.ListViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class CaseHistoryListActivity extends BaseActivity implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2<ListView> {
    ListViewHolder holder;
    CasehistoryListAdapter adapter;
    ArrayList<Case> arrayList = new ArrayList<>();
    AllCaseQ info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_history_list);
        initView();
    }

    private HeaderHolder header;

    private void initView() {
        header = new HeaderHolder();
        header.init(self, "案件列表");
        holder = ListViewHolder.initList(self);
        info = getIntent().getParcelableExtra("caseInfo");
        adapter = new CasehistoryListAdapter(self, arrayList);
        holder.list.setAdapter(adapter);
        holder.list.setOnItemClickListener(this);
        holder.list.setOnRefreshListener(this);
        holder.list.setMode(PullToRefreshBase.Mode.BOTH);
        holder.list.setRefreshing();
    }

    private void initData(final Boolean refresh) {
        if (refresh) {
            arrayList.clear();
            adapter.notifyDataSetChanged();
        }
        if (info != null) {
            info.setSTARTINDEX(String.valueOf(arrayList.size() + 1));
            info.setENDINDEX(String.valueOf(arrayList.size() + 20));
            getCaseData();
        }

    }


    /*
     * 案件历史记录查询
     */
    private void getCaseData() {
        final MyProgressDialog pd = Windows.waiting(self);
        AsyncUtil.goAsync(new Callable<Result<List<Case>>>() {

            @Override
            public Result<List<Case>> call() throws Exception {
                return WeiZhanData.queryHistoryCaseRecordInfo(info);
            }
        }, new Callback<Result<List<Case>>>() {

            @Override
            public void onHandle(Result<List<Case>> result) {
                pd.dismiss();
                if (result.ok()) {
                    holder.list.onRefreshComplete();
                    if (result.ok()) {
                        arrayList.addAll(result.getData());
                        adapter.notifyDataSetChanged();
                    } else {
                        UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
                    }
                    holder.mayShowEmpty(arrayList.size());
                    //header.setTitle(String.format("查询列表(%s条)", arrayList.isEmpty() ? 0 : arrayList.get(0).getTotalNum()));
                } else {
                    UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Case c = (Case) parent.getItemAtPosition(position);
        getBlTimeList(c);
    }

    private void getBlTimeList(final Case c) {
        final MyProgressDialog pd = Windows.waiting(self);
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
                                getCaseBlList(c,"无".equals(utcs.get(i).getStartUTC()),pd);
                                break;
                            }
                        }
                    }
                } else {
                    pd.dismiss();
                    UIUtils.toast(self, "查询失败", Toast.LENGTH_LONG);
                }
            }
        });
    }

    private void getCaseBlList(final Case c,final boolean isExist,final MyProgressDialog pd) {
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
                    Intent intent = new Intent(self, CaseBlListActivity.class);
                    intent.putParcelableArrayListExtra("bl_types", types);
                    startActivity(intent);
                } else {
                    UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
                }
            }
        });
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        initData(true);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        initData(false);
    }
}
