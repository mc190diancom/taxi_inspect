package com.miu360.taxi_check.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.CommonDialog.OnDialogItemClickListener;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu30.common.ui.entity.HYTypeQ;
import com.miu30.common.ui.entity.HyRyFg;
import com.miu30.common.ui.entity.JCItem;
import com.miu30.common.ui.entity.JCItemQ;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;

public class FaGuiActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.hylb)
	private TextView hylb_tv;
	@ViewInject(R.id.hylx)
	private TextView hylx_tv;
	@ViewInject(R.id.jclb)
	private TextView jclb_tv;
	@ViewInject(R.id.jcnr)
	private EditText jcnr_et;
	@ViewInject(R.id.code)
	private EditText code_et;
	@ViewInject(R.id.query)
	private TextView query;

	String[] itemOne = { "巡游车","网约车"};
	String[] itemTwo= {"无证驾驶员","巡游出租汽车驾驶员","出租汽车营业站调度员","巡游出租汽车经营者","设立出租汽车营业站的单位"};
	String[] itemThree = {"全部"};//,"经营者","人车不符","私改计价器"
	ArrayList<JCItem> arrayList;
	ArrayList<HyRyFg> fgList;
	ArrayList<String> LbList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fa_gui);
		initView();
		arrayList = new ArrayList<>();
		fgList = new ArrayList<>();
		LbList = new ArrayList<>();
		hylx_tv.setText(itemTwo[0]);
		jclb_tv.setText(itemThree[0]);
		queryLX();
	}

	private void initView() {
		ViewUtils.inject(self);
		new HeaderHolder().init(self, "执法依据");
		query.setOnClickListener(this);
		hylb_tv.setOnClickListener(this);
		hylx_tv.setOnClickListener(this);
		jclb_tv.setOnClickListener(this);
	}

	String jcnr;
	String code;
	String jclb;
	String hylb;
	String hylx;

	@Override
	public void onClick(View v) {
		if (v == query) {
			jcnr = jcnr_et.getText().toString();
			code = code_et.getText().toString();
			jclb = jclb_tv.getText().toString();
			hylb = hylb_tv.getText().toString();
			hylx = hylx_tv.getText().toString();
			Intent intent = new Intent(self, CheckItemListActivity.class);
			intent.putExtra("jcnr", jcnr);
			intent.putExtra("code", code);
			intent.putExtra("jclb", jclb);
			intent.putExtra("hylb", hylb);
			intent.putExtra("hylx", hylx);
			startActivity(intent);
		} else if (v == hylb_tv) {
			Windows.singleChoice(self, "请选择行业类别", itemOne, new OnDialogItemClickListener() {

				@Override
				public void dialogItemClickListener(int position) {
					hylb_tv.setText(itemOne[position]);
					queryLX();
				}
			});
		}  else if (v == hylx_tv) {
			Windows.singleChoice(self, "请选择人员类型", itemTwo, new OnDialogItemClickListener() {

				@Override
				public void dialogItemClickListener(int position) {
					hylx_tv.setText(itemTwo[position]);
					queryLB();
				}
			});
		}  else if (v == jclb_tv) {
			Windows.singleChoice(self, "请选择检查类别", itemThree, new OnDialogItemClickListener() {

				@Override
				public void dialogItemClickListener(int position) {
					jclb_tv.setText(itemThree[position]);
				}
			});
		}

	}


	private void queryLX() {
		//final MyProgressDialog pd = Windows.waiting(self);
		final HYTypeQ info = new HYTypeQ();
		info.setHylb(hylb_tv.getText().toString());
		fgList.clear();
		AsyncUtil.goAsync(new Callable<Result<List<HyRyFg>>>() {

			@Override
			public Result<List<HyRyFg>> call() throws Exception {
				return WeiZhanData.queryCheckItemLXByHY(info);
			}
		}, new Callback<Result<List<HyRyFg>>>() {

			@Override
			public void onHandle(Result<List<HyRyFg>> result) {
				//pd.dismiss();
				if (result.ok()) {
					if(result.getData().toString().equals("[]") || result.getData() == null){
						itemTwo = new String[0];
						return;
					}
					fgList.addAll(result.getData());
					itemTwo = new String[fgList.size()];
					for(int i=0,len=fgList.size();i<len;i++){
						itemTwo[i] = fgList.get(i).getLBMC();
					}
					hylx_tv.setText(itemTwo[0]);
					queryLB();
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
				}
			}
		});
	}


	private void queryLB() {
		arrayList.clear();
		LbList.clear();
		hylb = hylb_tv.getText().toString();
		hylx = hylx_tv.getText().toString();
		final MyProgressDialog pd = Windows.waiting(self);
		final JCItemQ jq = new JCItemQ();
		jq.setBjczt(hylx);
		jq.setHylb(hylb);
//		jq.setHyrylx(hylx);
		AsyncUtil.goAsync(new Callable<Result<List<JCItem>>>() {

			@Override
			public Result<List<JCItem>> call() throws Exception {
				return WeiZhanData.queryCheckItem(jq);
			}
		}, new Callback<Result<List<JCItem>>>() {

			@Override
			public void onHandle(Result<List<JCItem>> result) {
				pd.dismiss();
				if (result.ok()) {
					if (result.getData().toString().equals("[]") || result.getData() == null) {
						UIUtils.toast(self, "查不到数据", Toast.LENGTH_SHORT);
						return;
					}
					arrayList.addAll(result.getData());
					LbList.add("全部");
					for(int i=0,len =arrayList.size();i<len;i++){
						if(!LbList.contains(arrayList.get(i).getLBMS())){
							LbList.add(arrayList.get(i).getLBMS());
						}
					}
					itemThree = new String[LbList.size()];
					for(int i=0,len =LbList.size();i<len;i++){
						itemThree[i] = LbList.get(i);
					}
					jclb_tv.setText(itemThree[0]);
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
				}
			}
		});
	}

	private void queryFG() {
		arrayList.clear();
		jcnr = jcnr_et.getText().toString();
		code = code_et.getText().toString();
		jclb = jclb_tv.getText().toString();
		hylb = hylb_tv.getText().toString();
		hylx = hylx_tv.getText().toString();
		final MyProgressDialog pd = Windows.waiting(self);
		final JCItemQ jq = new JCItemQ();
		jq.setHylb(hylb);
		jq.setHyrylx(hylx);
		jq.setLbmc(jcnr);
		jq.setLbms(jclb);
		jq.setNew_bm(code);
		AsyncUtil.goAsync(new Callable<Result<List<JCItem>>>() {

			@Override
			public Result<List<JCItem>> call() throws Exception {
				return WeiZhanData.queryCheckItemByInfo(jq);
			}
		}, new Callback<Result<List<JCItem>>>() {

			@Override
			public void onHandle(Result<List<JCItem>> result) {
				pd.dismiss();
				if (result.ok()) {
					if (result.getData().toString().equals("[]") || result.getData() == null) {
						UIUtils.toast(self, "查不到数据", Toast.LENGTH_SHORT);
						return;
					}
					arrayList.addAll(result.getData());
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
				}
			}
		});
	}
}
