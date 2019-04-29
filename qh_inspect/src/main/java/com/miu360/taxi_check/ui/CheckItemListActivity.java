package com.miu360.taxi_check.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.lidroid.xutils.ViewUtils;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu30.common.ui.entity.FaGuiDetail;
import com.miu30.common.ui.entity.FaGuiDetailQ;
import com.miu30.common.ui.entity.JCItem;
import com.miu30.common.ui.entity.JCItemQ;
import com.miu360.taxi_check.util.UIUtils;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.common.isNull;
import com.miu360.taxi_check.view.HeaderHolder;
import com.miu360.taxi_check.view.ListViewHolder;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CheckItemListActivity extends BaseActivity implements OnClickListener {
	ListViewHolder holder;
	HeaderHolder header;
	List<JCItem> arrayList;
	CheckItemListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basic_result_item_list);
		initView();
		initHolerList();
	}

	private void initView() {
		ViewUtils.inject(self);
		header = new HeaderHolder();
		header.init(self, "法规列表");
		hylb = getIntent().getStringExtra("hylb");
		hylx = getIntent().getStringExtra("hylx");
		jclb = getIntent().getStringExtra("jclb");
		code = getIntent().getStringExtra("code");
		jcnr = getIntent().getStringExtra("jcnr");
		if("全部".equals(jclb)){
			jclb = "";
		}
		adapter = new CheckItemListAdapter();
		arrayList = new ArrayList<>();
		initHolerList();
		header.leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		initQuery();
	}


	private void initHolerList() {
		holder = ListViewHolder.initList(self);
		holder.list.setMode(Mode.DISABLED);
		holder.list.setVisibility(View.GONE);
		holder.list.setAdapter(adapter);
		holder.list.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				final JCItem info = (JCItem) parent.getItemAtPosition(position);
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
							if(result.getData().toString().equals("[]") || result.getData() == null){
								UIUtils.toast(self, "该法规没有详情", Toast.LENGTH_SHORT);
								return;
							}
							Intent intent = new Intent(self,CheckDetailWeiFaActivity.class);
							intent.putExtra("xqid", info.getZDLBID());
							intent.putExtra("lbms", info.getLBMS());
							intent.putExtra("lbmc", info.getLBMC());
							intent.putExtra("bm", info.getLBBM());
							startActivity(intent);
						}else {
							UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
						}
					}
				});
			}
		});
	}

	String jcnr;
	String code;
	String jclb;
	String hylb;
	String hylx;
	private void initQuery() {
		arrayList.clear();
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
						finish();
						return;
					}
					arrayList.addAll(result.getData());
					holder.list.setVisibility(View.VISIBLE);
					adapter.notifyDataSetChanged();
				} else {
					finish();
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {

	}

	int selectedIndex = 0;//存储点击后的tag
	private class CheckItemListAdapter extends BaseAdapter {

		ViewHolderCheckItemLeiBie holder;

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

		@Override
		public View getView(final int position, View contview, ViewGroup arg2) {
			holder = null;
			if (contview == null) {
				contview = LayoutInflater.from(self).inflate(R.layout.checkitemlist_adapter, arg2, false);
				holder = new ViewHolderCheckItemLeiBie();
				contview.setTag(holder);
				holder.jiancha_daima = (TextView) contview.findViewById(R.id.jiancha_daima);
				holder.jiancha_leibie = (TextView) contview.findViewById(R.id.jiancha_leibie);
				holder.jiancha_neirong = (TextView) contview.findViewById(R.id.jiancha_neirong);
				holder.jiancha_id = (TextView)  contview.findViewById(R.id.jiancha_id);
			} else {
				holder = (ViewHolderCheckItemLeiBie) contview.getTag();
			}

			holder.jiancha_daima.setText(isNull.isEmpty(arrayList.get(position).getLBBM()));
			holder.jiancha_leibie.setText(isNull.isEmpty(arrayList.get(position).getLBMS()));
			holder.jiancha_neirong.setText(isNull.isEmpty(arrayList.get(position).getLBMC()));
			holder.jiancha_id.setText(isNull.isEmpty(arrayList.get(position).getZDLBID()));
			return contview;
		}

		private class ViewHolderCheckItemLeiBie {
			TextView jiancha_leibie;
			TextView jiancha_daima;
			TextView jiancha_neirong;
			TextView jiancha_id;
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
