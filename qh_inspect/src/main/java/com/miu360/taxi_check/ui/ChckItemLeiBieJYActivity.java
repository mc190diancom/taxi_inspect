package com.miu360.taxi_check.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import com.miu360.taxi_check.common.Windows;
import com.miu360.taxi_check.data.WeiFaCheckPreference;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.FaGuiDetailAllInfo;
import com.miu360.taxi_check.model.FaGuiQueryInfo;
import com.miu360.taxi_check.model.JCLBQ;
import com.miu360.taxi_check.model.JclbModel;
import com.miu360.taxi_check.util.UIUtils;
import com.miu360.taxi_check.view.HeaderHolder;
import com.miu360.taxi_check.view.ListViewHolder;

public class ChckItemLeiBieJYActivity extends BaseActivity implements OnClickListener {

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
	ArrayList<FaGuiDetailAllInfo> testList = new ArrayList<>();
	ArrayList<String> list = new ArrayList<>();
	View root;
	boolean isOver = true;

	String resultString = "";
	String bjczt = "";

	HeaderHolder header;
	String[] itemOne;
	ArrayList<JclbModel> arrayListJclb;
	FaGuiQueryInfo info = new FaGuiQueryInfo();
	//对获取的数据进行排序
	Comparator<FaGuiDetailAllInfo> comparator = new Comparator<FaGuiDetailAllInfo>() {
		public int compare(FaGuiDetailAllInfo f1, FaGuiDetailAllInfo f2) {
			// 先排JCLB
			if (!f1.getJCLB().equals(f2.getJCLB())) {
				return f1.getJCLB().compareTo(f2.getJCLB());
			} else if (!f1.getBM().equals(f2.getBM())) {
				return f1.getBM().compareTo(f2.getBM());
			} else{

			}
			return 0;
		}
	};

	//对获取的数据进行排序
	Comparator<JclbModel> comparator2 = new Comparator<JclbModel>() {
		public int compare(JclbModel f1, JclbModel f2) {
			// 先排JCLB
			if (!f1.getJCLB().equals(f2.getJCLB())) {
				return f1.getJCLB().compareTo(f2.getJCLB());
			} else{

			}
			return 0;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chck_item_lei_bie);
		initView();
		arrayListJclb = new ArrayList<>();
		bjczt = getIntent().getStringExtra("bjczt");
		info.setHYLB(getIntent().getStringExtra("HYLB"));

		info.setStartIndex(0);
		info.setEndIndex(120);

		final JCLBQ jclbq = new JCLBQ();
		if(bjczt!=null && bjczt!=""){
			jclbq.setBjczt(bjczt);
		}else{
			jclbq.setBjczt("出租汽车驾驶员");
		}
		AsyncUtil.goAsync(new Callable<Result<List<JclbModel>>>() {

			@Override
			public Result<List<JclbModel>> call() throws Exception {
				return WeiZhanData.getJclb_old(jclbq);
			}
		}, new Callback<Result<List<JclbModel>>>() {

			@Override
			public void onHandle(Result<List<JclbModel>> result) {
				if (result.ok()) {
					if(info.getHYLB()!=null){
						if(info.getHYLB().contains("非营运车")){
							JclbModel c = new JclbModel();
							c.setJCLB("00全部");
							arrayListJclb.add(c);
							arrayListJclb.add(result.getData().get(0));

							Collections.sort(arrayListJclb, comparator2);
						}else{
							arrayListJclb.addAll(result.getData());
							//arrayListJclb.remove(0);
							JclbModel c = new JclbModel();
							c.setJCLB("00全部");
							arrayListJclb.add(c);
							Collections.sort(arrayListJclb, comparator2);
						}
						itemOne = new String[arrayListJclb.size()];
						for (int i = 0; i < arrayListJclb.size(); i++) {
							itemOne[i] = arrayListJclb.get(i).getJCLB();
						}
					}
				} else {
					//UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
				}
			}
		});

		holder = ListViewHolder.initList(self);
		adapter = new CheckItemLeiBieAdapter();
		//root = LayoutInflater.from(self).inflate(R.layout.checkitemleibieadapterheader, null);

		holder.list.setMode(Mode.DISABLED);
		holder.list.setVisibility(View.GONE);
		holder.list.setAdapter(adapter);
		//holder.list.getRefreshableView().addHeaderView(root);
		holder.list.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				FaGuiDetailAllInfo info = (FaGuiDetailAllInfo) arg0.getItemAtPosition(arg2);
				// Intent intent = new Intent(self,
				// CheckDetailWeiFaActivity.class);
				// intent.putExtra("FaGuiDetailAllInfo", info);
				// startActivity(intent);
			}
		});
		queryAll();
	}



	private void initView() {
		ViewUtils.inject(self);
		WeiFaCheckPreference pref = new WeiFaCheckPreference(self);
		int count = pref.getInt("resultPeopleSize", -1);
		for (int i = 0; i < count; i++) {
			String s = pref.getString("checkPositionContext_" + i, "");
			if (!TextUtils.isEmpty(s)) {
				checkPositionContext.add(s);
			}
		}

		header = new HeaderHolder();
		header.init(self, "检查项");
		header.leftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isOver) {
					Intent intent = new Intent();
					intent.putExtra("result", resultString);
					//intent.putExtra(name, value);
					setResult(RESULT_OK, intent);
				}

				finish();
			}
		});
		header.rightTextBtn.setVisibility(View.VISIBLE);
		header.rightTextBtn.setText("确认");
		header.rightTextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isOver = false;
				Intent intent = new Intent();
				WeiFaCheckPreference prefer = new WeiFaCheckPreference(self);
				prefer.clearPreference();
				prefer.setInt("resultPeopleSize", checkPositionContext.size());
				intent.putExtra("resultPeopleSize", checkPositionContext.size());
				Iterator<String> iter = checkPositionContext.iterator();
				int count = 0;
				while(iter.hasNext()){
					String s = iter.next();
					prefer.setString("checkPositionContext_"+count, s);
					intent.putExtra("resultPeople_"+count, s);
					count++;
				}
				intent.putStringArrayListExtra("jclb", jclbList);
				intent.putStringArrayListExtra("wfxwId", wfxwIdList);
				intent.putStringArrayListExtra("wfqxId", wfqxIdList);
				intent.putExtra("checkBJCZT", bjczt);

				setResult(RESULT_OK, intent);
				finish();
			}
		});
		leibie_hide.setOnClickListener(this);
		check_leibie.setOnClickListener(this);
		leibie_query.setOnClickListener(this);
	}

	String jcnr;
	String code;
	String jclb;

	private void initQuery() {
		holder.list.setVisibility(View.GONE);
		jcnr = leibie_neirong.getText().toString();
		code = leibie_daima.getText().toString().toUpperCase();
		jclb = check_leibie.getText().toString();
		FaGuiQueryInfo info = new FaGuiQueryInfo();
		info.setCODE(code);
		info.setJCLB(jclb);
		info.setJCNR(jcnr);
		info.setBjczt(bjczt);
		info.setHYLB(getIntent().getStringExtra("HYLB"));
		/*info.setStartIndex(0 + testList.size());
		info.setEndIndex(10 + testList.size());*/
		if(info.getJCLB()!=null){
			if(info.getJCLB().contains("00全部")){
				info.setJCLB("");
				queryFenLei(info);
			}else{
				queryFenLei(info);
			}
		}else{
			queryFenLei(info);
		}
	}

	private void queryAll(){
		final FaGuiQueryInfo infoall = new FaGuiQueryInfo();
		infoall.setHYLB(getIntent().getStringExtra("HYLB"));
		infoall.setBjczt(bjczt);

		final MyProgressDialog pd = Windows.waiting(self);
		AsyncUtil.goAsync(new Callable<Result<List<FaGuiDetailAllInfo>>>() {

			@Override
			public Result<List<FaGuiDetailAllInfo>> call() throws Exception {
				return WeiZhanData.queryCheckItemInfo(infoall);
			}
		}, new Callback<Result<List<FaGuiDetailAllInfo>>>() {

			@Override
			public void onHandle(Result<List<FaGuiDetailAllInfo>> result) {
				pd.dismiss();
				if (result.ok()) {
					if (result.getData().toString().equals("[]")) {
						UIUtils.toast(self, "查不到数据", Toast.LENGTH_LONG);
						return;
					}

					holder.list.setVisibility(View.VISIBLE);
					for(int i=0,len = result.getData().size();i<len;i++){
						if(!result.getData().get(i).getBH().contains("00")){
							testList.add(result.getData().get(i));
						}
					}
					Collections.sort(testList, comparator);
					adapter.notifyDataSetChanged();

				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
				}
			}
		});
		/*AsyncUtil.goAsync(new Callable<Result<List<FaGuiDetailAllInfo>>>() {

			@Override
			public Result<List<FaGuiDetailAllInfo>> call() throws Exception {
				return WeiZhanData.queryFaGuiAllInfo(info);
			}
		}, new Callback<Result<List<FaGuiDetailAllInfo>>>() {

			@Override
			public void onHandle(Result<List<FaGuiDetailAllInfo>> result) {
				holder.list.onRefreshComplete();
				if (result.ok()) {
					if (result.getData() == null) {
						UIUtils.toast(self, "无信息", Toast.LENGTH_LONG);
						return;
					}
					if (result.getData().toString().equals("[]")) {
						UIUtils.toast(self, "无信息", Toast.LENGTH_LONG);
						return;
					}
					holder.list.setVisibility(View.VISIBLE);
					//testList.addAll(result.getData());
					for(int i=0,len = result.getData().size();i<len;i++){
						if(!result.getData().get(i).getBH().contains("00")){
							testList.add(result.getData().get(i));
						}
					}
					Collections.sort(testList, comparator);
					adapter.notifyDataSetChanged();

				} else {
					//UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
				}
			}
		});*/

	}

	private void queryFenLei(final FaGuiQueryInfo info){
		final MyProgressDialog pd = Windows.waiting(self);
		AsyncUtil.goAsync(new Callable<Result<List<FaGuiDetailAllInfo>>>() {

			@Override
			public Result<List<FaGuiDetailAllInfo>> call() throws Exception {
				return WeiZhanData.queryCheckItemInfo(info);
			}
		}, new Callback<Result<List<FaGuiDetailAllInfo>>>() {

			@Override
			public void onHandle(Result<List<FaGuiDetailAllInfo>> result) {
				pd.dismiss();
				if (result.ok()) {
					if (result.getData().toString().equals("[]")) {
						UIUtils.toast(self, "查不到数据", Toast.LENGTH_LONG);
						return;
					}

					holder.list.setVisibility(View.VISIBLE);
					for(int i=0,len = result.getData().size();i<len;i++){
						if(!result.getData().get(i).getBH().contains("00")){
							testList.add(result.getData().get(i));
						}
					}
					Collections.sort(testList, comparator);
					adapter.notifyDataSetChanged();

				} else {
					UIUtils.toast(self, result.getErrorMsg(), Toast.LENGTH_LONG);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v == leibie_hide) {
			if (leibie_show.getVisibility() == View.GONE) {
				leibie_show.setVisibility(View.VISIBLE);
			} else {
				leibie_show.setVisibility(View.GONE);
			}
		} else if (v == check_leibie) {
			Windows.singleChoice(self, "选择检查类别", itemOne, new OnDialogItemClickListener() {

				@Override
				public void dialogItemClickListener(int position) {
					check_leibie.setText(itemOne[position]);
					testList.clear();
					initQuery();
				}
			});
		} else if (v == leibie_query) {
			if (TextUtils.isEmpty(check_leibie.getText().toString()) &&
					TextUtils.isEmpty(leibie_neirong.getText().toString())&&
					TextUtils.isEmpty(leibie_daima.getText().toString().toUpperCase())) {
				UIUtils.toast(self, "请输入请求参数", Toast.LENGTH_SHORT);
				return;
			}
			testList.clear();
			initQuery();
		}
	}

	HashMap<Integer, String> result = new HashMap<>();
	//	HashSet<Integer> checkPosition = new HashSet<Integer>();
	ArrayList<Integer> position = new ArrayList<>();
	HashSet<String> resultStr = new HashSet<>();
	ArrayList<String> resultArr = new ArrayList<>();

	ArrayList<String> jclbList = new ArrayList<>();//存储勾选内容的检查类别

	ArrayList<String> wfxwIdList = new ArrayList<>();//违法行为id
	ArrayList<String> wfqxIdList = new ArrayList<>();//违法情形id

	HashSet<String> checkPositionContext = new HashSet<String>();

	private class CheckItemLeiBieAdapter extends BaseAdapter {

		ViewHolderCheckItemLeiBie holder;

		@Override
		public int getCount() {
			return testList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return testList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2) {

			holder = null;
			if (arg1 == null) {
				arg1 = LayoutInflater.from(self).inflate(R.layout.checkitemleibieadapter, arg2, false);
				holder = new ViewHolderCheckItemLeiBie();
				arg1.setTag(holder);
				holder.jiancha_daima = (TextView) arg1.findViewById(R.id.jiancha_daima);
				holder.jiancha_leibie = (TextView) arg1.findViewById(R.id.jiancha_leibie);
				holder.jiancha_neirong = (TextView) arg1.findViewById(R.id.jiancha_neirong);
				holder.jiancha_weifa = (CheckBox) arg1.findViewById(R.id.jiancha_weifa);
			} else {
				holder = (ViewHolderCheckItemLeiBie) arg1.getTag();
			}

			holder.jiancha_daima.setText(testList.get(arg0).getBM());
			holder.jiancha_leibie.setText(testList.get(arg0).getJCLB());
			holder.jiancha_neirong.setText(testList.get(arg0).getJCJG());
			holder.jiancha_weifa.setTag(arg0);
			//Log.e("长度", checkPositionContext.size()+"");
			boolean b = checkPositionContext.contains(testList.get(arg0).getJCJG());
//			boolean c = checkPosition.contains(arg0);
//			if (!checkPosition.isEmpty()) {
//				if (checkPosition.iterator().next().equals("1") && arg0 == 1) {
//					System.out.println();
//				}
//			}

			holder.jiancha_weifa.setChecked(b);
			holder.jiancha_weifa.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Integer tag = (Integer) buttonView.getTag();
					if (isChecked) {
//						checkPosition.add(tag);
						checkPositionContext.add(testList.get(tag).getJCJG());
						//wfxwIdList
						String jclbNR = testList.get(tag).getJCLB().substring(2);
						if(!jclbList.contains(jclbNR)){
							jclbList.add(jclbNR);
						}

						String wfxwId = testList.get(tag).getWFXW_ID();
						if(!wfxwIdList.contains(wfxwId)){
							wfxwIdList.add(wfxwId);
						}

						String wfqxId = testList.get(tag).getWFQX_ID();
						if(!wfqxIdList.contains(wfqxId)){
							wfqxIdList.add(wfqxId);
						}

//						result.put(tag, testList.get(tag).getJCJG());
					} else {
//						result.remove(tag);
//						checkPosition.remove(tag);
						jclbList.remove(testList.get(tag).getJCLB());
						wfxwIdList.remove(testList.get(tag).getWFXW_ID());
						wfqxIdList.remove(testList.get(tag).getWFQX_ID());
						checkPositionContext.remove(testList.get(tag).getJCJG());
					}

				}
			});
			return arg1;
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
		Intent intent = new Intent();
		intent.putExtra("resultPeopleSize", resultArr.size());


		for (int i = 0; i < resultArr.size(); i++) {
			intent.putExtra("resultPeople_" + i, resultArr.get(i));
		}
		intent.putExtra("checkBJCZT", bjczt);
		setResult(RESULT_OK, intent);
		super.onBackPressed();
	}

}
