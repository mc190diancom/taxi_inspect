package com.miu360.taxi_check.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lubao.lubao.async.AsyncUtil;
import com.lubao.lubao.async.Callback;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseActivity;
import com.miu360.taxi_check.common.CommonDialog.OnDialogItemClickListener;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.PositionPreference;
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.common.isNull;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu30.common.ui.entity.FaGuiDetail;
import com.miu30.common.ui.entity.FaGuiDetailQ;
import com.miu30.common.ui.entity.JCItem;
import com.miu30.common.ui.entity.JCItemQ;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;
import com.miu360.taxi_check.view.ListViewHolder;

public class ChckItemLeiBieActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.leibie_show)
	private LinearLayout leibie_show;
	@ViewInject(R.id.check_leibie)
	private TextView check_leibie;
	@ViewInject(R.id.leibie_daima)
	private TextView leibie_daima;// 代码
	@ViewInject(R.id.leibie_neirong)
	private TextView leibie_neirong;// 检查内容
	@ViewInject(R.id.leibie_query)
	private TextView leibie_query;
	@ViewInject(R.id.leibie_hide)
	private TextView leibie_hide;

	ListViewHolder holder;
	CheckItemLeiBieAdapter adapter;
	ArrayList<String> list = new ArrayList<>();
	View root;
	boolean isOver = true;

	String resultString = "";

	HeaderHolder header;
	String[] itemOne;
	ArrayList<JCItem> arrayList;
	String bsf = "", hylb = "";
	private String HYLB, HYRYLX;
	private boolean isFirst = true;
	PositionPreference prefer;
	String checkPositionContext = "";
	String chaeckPositionJclb = "";
	private boolean isThisCheck = false;
	private JCItem info = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chck_item_lei_bie);
		initView();
		String hyry = prefer.getString("HYRYLX", "");
		if (!"".equals(hyry)) {
			if (HYRYLX.equals(hyry)) {
				isThisCheck = true;
				checkPositionContext = prefer.getString("resultItem", "");
				chaeckPositionJclb = prefer.getString("LBMS", "");
			}
		}
		holder = ListViewHolder.initList(self);
		adapter = new CheckItemLeiBieAdapter();
		root = LayoutInflater.from(self).inflate(R.layout.checkitemleibieadapterheader, null);

		holder.list.setMode(Mode.DISABLED);
		holder.list.setVisibility(View.GONE);
		holder.list.setAdapter(adapter);
		holder.list.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final JCItem info = (JCItem) parent.getItemAtPosition(position);
				queryWFQX(info);
			}
		});
	}

	private void initView() {
		ViewUtils.inject(self);
		header = new HeaderHolder();
		prefer = new PositionPreference(self);
		HYLB = getIntent().getStringExtra("HYLB");
		HYRYLX = getIntent().getStringExtra("HYRYLX");
		arrayList = new ArrayList<>();
		header.init(self, "检查项");
		header.leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		header.rightTextBtn.setVisibility(View.VISIBLE);
		header.rightTextBtn.setText("确认");
		header.rightTextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 如果进来发生了选择改变
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

		check_leibie.setOnClickListener(this);
		leibie_query.setOnClickListener(this);
		leibie_hide.setOnClickListener(this);
		initAllQuery();
	}

	private void queryWFQX(final JCItem info) {
		Intent intent = new Intent(self, CheckDetailWeiFaActivity.class);
		intent.putExtra("xqid", info.getZDLBID());
		intent.putExtra("lbms", info.getLBMS());
		intent.putExtra("lbmc", info.getLBMC());
		intent.putExtra("bm", info.getLBBM());
		startActivity(intent);
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
					prefer.setString("HYRYLX", HYRYLX);
					prefer.setString("LMBS", chaeckPositionJclb);
					intent.putExtra("resultItem", checkPositionContext);
					// 返回检查类别
					intent.putExtra("LBMS", chaeckPositionJclb);
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

	String jcnr;
	String code;
	String jclb;

	private void initAllQuery() {
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
						itemOne = new String[1];
						itemOne[0] = "";
						// UIUtils.toast(self, "查询不到法规", Toast.LENGTH_SHORT);
						return;
					}
					arrayList.addAll(result.getData());
					for (int i = 0; i < arrayList.size(); i++) {
						if (!list.contains(arrayList.get(i).getLBMS())) {
							list.add(arrayList.get(i).getLBMS());
						}
					}
					if (isFirst) {
						itemOne = new String[list.size() + 1];
						itemOne[0] = "全部";
						for (int i = 0; i < list.size(); i++) {
							itemOne[i + 1] = list.get(i);
						}
						isFirst = false;
					}
					holder.list.setVisibility(View.VISIBLE);
					adapter.notifyDataSetChanged();
				} else {
					itemOne = new String[1];
					itemOne[0] = "";
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
				}
			}
		});
	}

	private void initQuery() {
		arrayList.clear();
		final MyProgressDialog pd = Windows.waiting(self);
		final JCItemQ jq = new JCItemQ();
		jq.setHylb(HYLB);
		jq.setHyrylx(HYRYLX);
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
					holder.list.setVisibility(View.VISIBLE);
					adapter.notifyDataSetChanged();
				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_SHORT);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v == check_leibie) {
			Windows.singleChoice(self, "选择检查类别", itemOne, new OnDialogItemClickListener() {

				@Override
				public void dialogItemClickListener(int position) {
					check_leibie.setText(itemOne[position]);
				}
			});
		} else if (v == leibie_query) {
			code = leibie_daima.getText().toString();
			jcnr = leibie_neirong.getText().toString();
			jclb = check_leibie.getText().toString();
			if (TextUtils.isEmpty(jclb) && TextUtils.isEmpty(jcnr) && TextUtils.isEmpty(code)) {
				UIUtils.toast(self, "至少填写一项", Toast.LENGTH_SHORT);
				return;
			}
			if (!TextUtils.isEmpty(check_leibie.getText().toString())) {
				if ("全部".equals(check_leibie.getText().toString())) {
					initAllQuery();
				} else {
					initQuery();
				}
			} else {
				initQuery();
			}
		} else if (v == leibie_hide) {
			if (leibie_show.getVisibility() == View.GONE) {
				leibie_show.setVisibility(View.VISIBLE);
				leibie_hide.setText("隐藏");
			} else {
				leibie_show.setVisibility(View.GONE);
				leibie_hide.setText("展开");
			}
		}
	}

	int selectedIndex = 0;// 存储点击后的tag

	private class CheckItemLeiBieAdapter extends BaseAdapter {

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
				contview = LayoutInflater.from(self).inflate(R.layout.checkitemleibieadapter, arg2, false);
				holder = new ViewHolderCheckItemLeiBie();
				contview.setTag(holder);
				holder.jiancha_daima = (TextView) contview.findViewById(R.id.jiancha_daima);
				holder.jiancha_leibie = (TextView) contview.findViewById(R.id.jiancha_leibie);
				holder.jiancha_neirong = (TextView) contview.findViewById(R.id.jiancha_neirong);
				holder.jiancha_weifa = (CheckBox) contview.findViewById(R.id.jiancha_weifa);
			} else {
				holder = (ViewHolderCheckItemLeiBie) contview.getTag();
			}

			// holder.jiancha_daima.setText(isNull.isEmpty(arrayList.get(position).getLBBM()));
			holder.jiancha_leibie.setText(isNull.isEmpty(arrayList.get(position).getLBMS()));
			holder.jiancha_neirong.setText(isNull.isEmpty(arrayList.get(position).getLBMC()));
			holder.jiancha_weifa.setTag(position);
			// 如果是初始化更新的apadatr，则按照checkPositionContext，否则按照selectedIndex
			if (selectedIndex == 0) {
				boolean c = checkPositionContext.equals(arrayList.get(position).getLBMC());
				holder.jiancha_weifa.setChecked(c);
				if(isThisCheck && c){
					info = arrayList.get(position);
				}
			} else {
				if (selectedIndex == position) {
					holder.jiancha_weifa.setChecked(true);
				} else {
					holder.jiancha_weifa.setChecked(false);
				}
			}

			holder.jiancha_weifa.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Integer tag = (Integer) buttonView.getTag();
					if (isChecked) {
						info = arrayList.get(tag);
						checkPositionContext = arrayList.get(tag).getLBMC();
						chaeckPositionJclb = arrayList.get(tag).getLBMS();
						selectedIndex = tag;
						notifyDataSetChanged();
					} else {
						if (tag == selectedIndex) {
							info = null;
							checkPositionContext = "";
							chaeckPositionJclb = "";
						}
					}
				}
			});
			return contview;
		}

		private class ViewHolderCheckItemLeiBie {
			TextView jiancha_leibie;
			TextView jiancha_daima;
			TextView jiancha_neirong;
			CheckBox jiancha_weifa;
		}

	}

	@Override
	public void onBackPressed() {
		// 如果进来没有选择改变，而初始化发生改变，或者没有（有则是和上次选择违法的类型一样）
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
