package com.miu360.taxi_check.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.lidroid.xutils.ViewUtils;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.PositionPreference;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu30.common.ui.entity.FaGuiDetail;
import com.miu360.taxi_check.model.FaGuiDetailAllInfo;
import com.miu30.common.ui.entity.FaGuiDetailQ;
import com.miu30.common.ui.entity.JCItem;
import com.miu30.common.ui.entity.JCItemQ;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;
import com.miu360.taxi_check.view.ListViewHolder;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class VehicleCheckActivity extends BaseActivity {

	ListViewHolder holder;
	CheckItemDetailAdapter adapter;
	List<JCItem> arrayList;
	View root;
	HeaderHolder header;
	String resultString = "";
	ArrayList<Integer> positionS = new ArrayList<>();
	ArrayList<String> resultArr = new ArrayList<>();
	PositionPreference prefer;
	private String HYLB, HYRYLX;
	String checkPositionContext = "";
	String checkPositionJclb = "";
	private boolean isThisCheck = false;
	private JCItem info = null;

	// 对获取的数据进行排序
	Comparator<FaGuiDetailAllInfo> comparator = new Comparator<FaGuiDetailAllInfo>() {
		public int compare(FaGuiDetailAllInfo f1, FaGuiDetailAllInfo f2) {
			// 先排JCLB
			if (!f1.getJCLB().equals(f2.getJCLB())) {
				return f1.getJCLB().compareTo(f2.getJCLB());
			} else if (!f1.getBM().equals(f2.getBM())) {
				return f1.getBM().compareTo(f2.getBM());
			} else {

			}
			return 0;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vehicle_check);
		initView();
		root = LayoutInflater.from(self).inflate(R.layout.checkitemadapterheader, null);
		holder = ListViewHolder.initList(self);
		holder.list.setMode(Mode.BOTH);
		holder.list.setRefreshing();
		holder.list.setAdapter(adapter);
		String hyry = prefer.getString("HYRYLX", "");
		if (!"".equals(hyry)) {
			if (HYRYLX.equals(hyry)) {
				isThisCheck = true;
				checkPositionContext = prefer.getString("resultItem", "");
				checkPositionJclb = prefer.getString("LBMS", "");
			}
		}
		holder.list.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final JCItem info = (JCItem) parent.getItemAtPosition(position);
				queryWFQX(info);
			}
		});
		initData();
	}

	private void queryWFQX(final JCItem info) {
		Intent intent = new Intent(self, CheckDetailWeiFaActivity.class);
		intent.putExtra("xqid", info.getZDLBID());
		intent.putExtra("lbms", info.getLBMS());
		intent.putExtra("lbmc", info.getLBMC());
		intent.putExtra("bm", info.getLBBM());
		startActivity(intent);
	}

	private void initData() {
		arrayList.clear();
		final JCItemQ jcQ = new JCItemQ();
		final MyProgressDialog d = Windows.waiting(self);
		jcQ.setHylb(HYLB);
		jcQ.setHyrylx(HYRYLX);
		AsyncUtil.goAsync(new Callable<Result<List<JCItem>>>() {

			@Override
			public Result<List<JCItem>> call() throws Exception {
				return WeiZhanData.getJclb(jcQ);
			}
		}, new Callback<Result<List<JCItem>>>() {

			@Override
			public void onHandle(Result<List<JCItem>> result) {
				d.dismiss();
				if (result.ok()) {
					if (result.getData().toString().equals("[]") || result.getData() == null) {
						// UIUtils.toast(self, "查询不到法规", Toast.LENGTH_SHORT);
						return;
					}
					arrayList.addAll(result.getData());
					holder.list.setVisibility(View.VISIBLE);
					adapter.notifyDataSetChanged();
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
				}
			}
		});
	}

	private void initView() {
		ViewUtils.inject(self);
		header = new HeaderHolder();
		header.init(self, "查看");
		header.rightTextBtn.setVisibility(View.VISIBLE);
		header.rightTextBtn.setText("确认");
		prefer = new PositionPreference(self);
		adapter = new CheckItemDetailAdapter();
		arrayList = new ArrayList<>();
		HYLB = getIntent().getStringExtra("HYLB");
		HYRYLX = getIntent().getStringExtra("HYRYLX");
		header.rightTextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (info != null) {
					RetrunWFQX(info);
				} else {// 如果进来没有选择改变，如果初始化有勾选的内容，或者没有（其实与进来点击返回无差异）
					Intent intent = new Intent();
					if (isThisCheck) {
						intent.putExtra("resultItem", "thischeck");
					} else {
						intent.putExtra("resultItem", "nochange");
					}
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
	}

	private void RetrunWFQX(final JCItem info) {
		final MyProgressDialog d = Windows.waiting(self);
		final FaGuiDetailQ fgQ = new FaGuiDetailQ();
		fgQ.setXqid(info.getZDLBID());
		AsyncUtil.goAsync(new Callable<Result<List<FaGuiDetail>>>() {

			@Override
			public Result<List<FaGuiDetail>> call() throws Exception {
				return WeiZhanData.getFaGuiDetails(fgQ);
			}
		}, new Callback<Result<List<FaGuiDetail>>>() {

			@Override
			public void onHandle(Result<List<FaGuiDetail>> result) {
				d.dismiss();
				if (result.ok()) {
					if (result.getData().toString().equals("[]") || result.getData() == null) {
						UIUtils.toast(self, "该法规没有详情", Toast.LENGTH_SHORT);
						return;
					}
					Intent intent = new Intent();
					prefer.clearPreference();
					prefer.setString("resultItem", checkPositionContext);
					prefer.setString("LBMS", checkPositionJclb);
					prefer.setString("HYRYLX", HYRYLX);
					intent.putExtra("resultItem", checkPositionContext);
					intent.putExtra("LBMS", checkPositionJclb);
					intent.putExtra("wfqxId", result.getData().get(0).getZDLBXM_ID());
					intent.putExtra("wfxwId", info.getZDLBID());
					setResult(RESULT_OK, intent);
					finish();
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
				}
			}
		});
	}

	int selectedIndex = 0;// 存储点击后的tag

	public class CheckItemDetailAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return arrayList.size();
		}

		@Override
		public Object getItem(int position) {
			return arrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void initDataS() {

		}

		@Override
		public View getView(final int position, View contentview, ViewGroup arg2) {
			ViewHolderCheckItem holder = null;
			if (contentview == null) {
				contentview = LayoutInflater.from(self).inflate(R.layout.checkitemadapter, arg2, false);
				holder = new ViewHolderCheckItem();
				contentview.setTag(holder);
				holder.jiancha_daima = (TextView) contentview.findViewById(R.id.jiancha_daima);
				holder.jiancha_leibie = (TextView) contentview.findViewById(R.id.jiancha_leibie);
				holder.jiancha_neirong = (TextView) contentview.findViewById(R.id.jiancha_neirong);
				holder.jiancha_rg = (RadioGroup) contentview.findViewById(R.id.jiancha_rg);
				holder.jiancha_weifa = (RadioButton) contentview.findViewById(R.id.jiancha_weifa);
			} else {
				holder = (ViewHolderCheckItem) contentview.getTag();
			}

			// holder.jiancha_daima.setText(arrayList.get(position).getLBBM());
			holder.jiancha_leibie.setText(arrayList.get(position).getLBMS());
			holder.jiancha_neirong.setText(arrayList.get(position).getLBMC());
			holder.jiancha_weifa.setTag(position);
			holder.jiancha_rg.setTag(position);
			// 如果是初始化更新的apadatr，则按照checkPositionContext，否则按照selectedIndex
			if (selectedIndex == 0) {
				boolean c = checkPositionContext.equals(arrayList.get(position).getLBMC());
				holder.jiancha_rg.check(c ? R.id.jiancha_weifa : R.id.jiancha_zhengchang);
				if(isThisCheck && c){
					info = arrayList.get(position);
				}
			} else {
				if (selectedIndex == position) {
					holder.jiancha_rg.check(true ? R.id.jiancha_weifa : R.id.jiancha_zhengchang);
				} else {
					holder.jiancha_rg.check(false ? R.id.jiancha_weifa : R.id.jiancha_zhengchang);
				}
			}
			// boolean c =
			// checkPositionContext.contains(arrayList.get(position).getLBMC());
			// holder.jiancha_rg.check(c ? R.id.jiancha_weifa :
			// R.id.jiancha_zhengchang);
			holder.jiancha_weifa.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Integer tag = (Integer) buttonView.getTag();
					if (isChecked) {
						info = arrayList.get(tag);
						checkPositionContext = arrayList.get(tag).getLBMC();
						checkPositionJclb = arrayList.get(tag).getLBMS();
						selectedIndex = tag;
						notifyDataSetChanged();
					} else {
						if (tag == selectedIndex) {
							info = null;
							checkPositionContext = "";
							checkPositionJclb = "";
						}
					}
				}
			});
			return contentview;
		}

	}

	private class ViewHolderCheckItem {
		TextView jiancha_leibie;
		TextView jiancha_daima;
		TextView jiancha_neirong;
		RadioButton jiancha_weifa;
		RadioGroup jiancha_rg;

	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		if (isThisCheck) {
			intent.putExtra("resultItem", "checkNoChange");
		} else {
			intent.putExtra("resultItem", "nochange");
		}
		setResult(RESULT_OK, intent);
		finish();
		super.onBackPressed();
	}

}
