package com.miu360.taxi_check.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.miu360.taxi_check.BaseFragment;
import com.miu360.taxi_check.common.CommonDialog.OnDialogItemClickListener;
import com.miu360.taxi_check.common.DateVer;
import com.miu30.common.util.MyProgressDialog;
import com.miu360.taxi_check.common.Windows;
import com.miu30.common.data.UserPreference;
import com.miu360.taxi_check.data.WeiZhanData;
import com.miu360.taxi_check.model.InspectTongJi;
import com.miu360.taxi_check.model.InspectTongJiQ;
import com.miu360.taxi_check.model.groupName;
import com.miu360.taxi_check.util.UIUtils;

public class InspectRecordCountFragment extends BaseFragment implements OnClickListener {

	@ViewInject(R.id.inspect_hangye)
	private TextView inspect_hangye;// 选择行业
	@ViewInject(R.id.check_time)
	private TextView check_time;// 开始时间
	@ViewInject(R.id.over_time)
	private TextView over_time;// 结束时间
	@ViewInject(R.id.query)
	private TextView query;// 查询
	/*@ViewInject(R.id.lydd)
	private LinearLayout lydd;*/
	@ViewInject(R.id.count_ly)
	private LinearLayout count_ly;
	@ViewInject(R.id.list)
	private com.handmark.pulltorefresh.library.PullToRefreshListView listview;
	/*@ViewInject(R.id.dd)
	private TextView dd;*/

	/*@ViewInject(R.id.dadui_normal)
	private TextView dadui_normal;
	@ViewInject(R.id.dadui_biaoyan)
	private TextView dadui_biaoyan;
	@ViewInject(R.id.dadui_pijiao)
	private TextView dadui_pijiao;
	@ViewInject(R.id.dadui_jinggao)
	private TextView dadui_jinggao;
	@ViewInject(R.id.dadui_chufa)
	private TextView dadui_chufa;
	@ViewInject(R.id.dadui_total)
	private TextView dadui_total;*/

	String[] itemsOne = { "全部"};//,"巡游车","非法经营出租汽车"
	UserPreference pref;
	countAdapter adapter;
	private boolean ssdd= false;

	/*private class simpleAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			viewHolderCount holder;
			if (convertView == null) {
				holder = new viewHolderCount();
				convertView = LayoutInflater.from(act).inflate(R.layout.inspectcountrecordbody, null);
				convertView.setTag(holder);
				holder.dadui_name = (TextView) convertView.findViewById(R.id.dadui_name);
				holder.dadui_normal = (TextView) convertView.findViewById(R.id.dadui_normal);
				holder.dadui_illegal = (TextView) convertView.findViewById(R.id.dadui_illegal);
				holder.dadui_total = (TextView) convertView.findViewById(R.id.dadui_total);
			} else {
				holder = (viewHolderCount) convertView.getTag();
			}
			holder.dadui_name.setText(list.get(position).getZfdwmc());
			holder.dadui_normal.setText(list.get(position).getNormal() + "");
			holder.dadui_illegal.setText(list.get(position).getIllegal() + "");
			holder.dadui_total.setText(list.get(position).getTotal() + "");
			return convertView;
		}
	}*/

	private class viewHolderCount {
		TextView dadui_name;
		TextView dadui_normal;
		TextView dadui_illegal;
		TextView dadui_total;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.activity_find_all, null);

		initView(root);

		return root;
	}


	private void initView(View root) {
		ViewUtils.inject(this, root);
		adapter = new countAdapter(act, rows);
		listview.setAdapter(adapter);
		listview.setMode(Mode.DISABLED);
		query.setOnClickListener(this);
		//inspect_hangye.setOnClickListener(this);
		check_time.setOnClickListener(this);
		over_time.setOnClickListener(this);
		check_time.setTag(System.currentTimeMillis());
		check_time.setText(new SimpleDateFormat(datePatterShow).format(new Date(System.currentTimeMillis())));
		over_time.setTag(System.currentTimeMillis());
		over_time.setText(new SimpleDateFormat(datePatterShow).format(new Date()));
		pref = new UserPreference(act);
		QuerySSDD();
	}

	@Override
	public void onClick(View v) {
		if (v == check_time) {
			chooseDate(check_time);
		} else if (v == over_time) {
			chooseDate2(over_time);
		} else if (v == inspect_hangye) {
			Windows.singleChoice(act, "", itemsOne, new OnDialogItemClickListener() {

				@Override
				public void dialogItemClickListener(int position) {
					inspect_hangye.setText(itemsOne[position]);
				}
			});
		} else if (v == query) {
			if(ssdd){
				QueryZfDw();
			}else{
				UIUtils.toast(act, "正常查询所属大队，请稍等...", Toast.LENGTH_SHORT);
			}

		}
	}


	private void QuerySSDD() {
		final String zfzh =pref.getString("user_name", "");
		AsyncUtil.goAsync(new Callable<Result<List<groupName>>>() {

			@Override
			public Result<List<groupName>> call() throws Exception {
				return WeiZhanData.querysSDD(zfzh);
			}
		}, new Callback<Result<List<groupName>>>() {

			@Override
			public void onHandle(Result<List<groupName>> result) {
				ssdd = true;
				if (result.ok()) {
					if("".equals(result.getData()) || result.getData()==null){
						UIUtils.toast(act, "该人员没有所属大队", Toast.LENGTH_SHORT);
						return;
					}
					list =result.getData();
				} else {
				}
			}
		});
	}


	List<groupName> list = new ArrayList<>();
	ArrayList<InspectTongJi.rows> rows = new ArrayList<>();
	int s1,s2,s3,s4,s5,total;
	//查询出有执法记录的大队
	private void QueryZfDw() {
		s1 = 0;
		s2 = 0;
		s3 = 0;
		s4 = 0;
		s5 = 0;
		total = 0;
		rows.clear();
		long start = (long) check_time.getTag();
		long end = (long) over_time.getTag();
		if (DateVer.VerEndCurrentDate(act, end)) {
			return;
		}
		if (DateVer.VerStartCurrentDate(act, start)) {
			return;
		}
		if (DateVer.VerStartDate(act, start, end)) {
			return;
		}
		final InspectTongJiQ tjQ = new InspectTongJiQ();
		//pref.getString("zfdwmc", "")
		tjQ.setDd("");
		tjQ.setStartTime(start/1000);
		tjQ.setEndTime(end/1000);
		tjQ.setStartIndex(0);
		tjQ.setEndIndex(5);
		final MyProgressDialog pd = Windows.waiting(act);
		AsyncUtil.goAsync(new Callable<Result<InspectTongJi>>() {

			@Override
			public Result<InspectTongJi> call() throws Exception {
				return WeiZhanData.queryInspectTongJiInfo(tjQ);
			}
		}, new Callback<Result<InspectTongJi>>() {

			@Override
			public void onHandle(Result<InspectTongJi> result) {
				pd.dismiss();
				if (result.ok()) {
					if("".equals(result.getData()) || result.getData()==null){
						return;
					}
					Log.e("rows", "rows:"+list);
					if(list == null || list.get(0).getGROUPNAME() == null){
						groupName in = new groupName();
						in.setGROUPNAME(pref.getString("zfdwmc", ""));
						list.add(in);
					}else{
						if(list.size()==0){
							groupName in = new groupName();
							in.setGROUPNAME(pref.getString("zfdwmc", ""));
							list.add(in);
						}
					}

					InspectTongJi tj = new InspectTongJi();
					tj = result.getData();
					ArrayList<String> s = new ArrayList<>();
					for(int i =0,len =tj.getRows().size();i<len;i++){
						if(s.contains(tj.getRows().get(i).getZFDWMC())){
							int x = s.indexOf(tj.getRows().get(i).getZFDWMC());
							Log.e("record", "record:"+rows.get(x).getSTAT0()+"==="+tj.getRows().get(i).getSTAT0());
							rows.get(x).setSTAT0(tj.getRows().get(i).getSTAT0()+rows.get(x).getSTAT0());
							Log.e("record", "record:"+rows.get(x).getSTAT0()+"==="+tj.getRows().get(i).getSTAT0());
							rows.get(x).setSTAT2(tj.getRows().get(i).getSTAT2()+rows.get(x).getSTAT2());
							rows.get(x).setSTAT3(tj.getRows().get(i).getSTAT3()+rows.get(x).getSTAT3());
							rows.get(x).setSTAT4(tj.getRows().get(i).getSTAT4()+rows.get(x).getSTAT4());
							rows.get(x).setSTAT5(tj.getRows().get(i).getSTAT5()+rows.get(x).getSTAT5());
						}else{
							s.add(tj.getRows().get(i).getZFDWMC());
							rows.add(tj.getRows().get(i));
						}
						//break;
						/*for(int j = 0,lenj=list.size();j<lenj;j++){
							Log.e("rows", "rows:"+list.get(j).getGROUPNAME()+"==="+tj.getRows().get(i).getZFDWMC());
							//if(tj.getRows().get(i).getZFDWMC().equals(list.get(j).getGROUPNAME())){

							//}

						}*/
					}

					Log.e("rows", "rows:"+rows);
					if(rows != null){
						if(rows.size() != 0){
							count_ly.setVisibility(View.VISIBLE);
						}else{
						}
						adapter.notifyDataSetChanged();
					}else{

					}
				} else {
					UIUtils.toast(act, result.getErrorMsg(), Toast.LENGTH_SHORT);
				}
			}
		});
	}

	private final String datePatterShow = "yyyy-MM-dd HH:mm";
	private final String datePatter = "yyyyMMddHHmm";

	private void chooseDate(final View v) {
		Windows.selectDateTime(act, (Long) v.getTag(), new Callback<Long>() {

			@Override
			public void onHandle(Long result) {
				//如果选择的开始时间超过了结束时间
				if(result>(long)over_time.getTag()){
					UIUtils.toast(act, "开始时间不能超过结束时间", Toast.LENGTH_SHORT);
					return;
				}
				((TextView) v).setText(new SimpleDateFormat(datePatterShow).format(new Date(result)));
				v.setTag(result);
			}
		});
	}

	private void chooseDate2(final View v) {
		Windows.selectDateTime(act, (Long) v.getTag(), new Callback<Long>() {

			@Override
			public void onHandle(Long result) {
				//如果结束时间超过了当前时间
				if(result>System.currentTimeMillis()){
					UIUtils.toast(act, "结束时间不能超过当前时间", Toast.LENGTH_SHORT);
					return;
				}
				((TextView) v).setText(new SimpleDateFormat(datePatterShow).format(new Date(result)));
				v.setTag(result);
			}
		});
	}


	class countAdapter extends BaseAdapter {

		ArrayList<InspectTongJi.rows> list;
		LayoutInflater mInflater;

		public countAdapter(Context ctx, ArrayList<InspectTongJi.rows> list) {
			this.list = list;
			mInflater = LayoutInflater.from(ctx);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			InspectCountHolderView holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.inspect_count_listitem, parent, false);
				holder = new InspectCountHolderView();
				convertView.setTag(holder);
				holder.STAT0 = (TextView) convertView.findViewById(R.id.dadui_normal);
				holder.STAT2 = (TextView) convertView.findViewById(R.id.dadui_biaoyan);
				holder.STAT3 = (TextView) convertView.findViewById(R.id.dadui_pijiao);
				holder.STAT4 = (TextView) convertView.findViewById(R.id.dadui_jinggao);
				holder.STAT5 = (TextView) convertView.findViewById(R.id.dadui_chufa);
				holder.STAT = (TextView) convertView.findViewById(R.id.dadui_total);
				holder.ZFDWMC = (TextView) convertView.findViewById(R.id.duibie);
			} else {
				holder = (InspectCountHolderView) convertView.getTag();
			}
			holder.STAT0.setText(list.get(position).getSTAT0()+"");
			holder.STAT2.setText(list.get(position).getSTAT2()+"");
			holder.STAT3.setText(list.get(position).getSTAT3()+"");
			holder.STAT4.setText(list.get(position).getSTAT4()+"");
			holder.STAT5.setText(list.get(position).getSTAT5()+"");
			holder.STAT.setText(list.get(position).getSTAT0()+list.get(position).getSTAT2()+list.get(position).getSTAT3()
					+list.get(position).getSTAT4()+list.get(position).getSTAT5()+"");
			holder.ZFDWMC.setText(list.get(position).getZFDWMC());
			return convertView;
		}

	}

	class InspectCountHolderView {
		public TextView STAT0;
		public TextView STAT2;
		public TextView STAT3;
		public TextView STAT4;
		public TextView STAT5;
		public TextView STAT;
		public TextView ZFDWMC;
	}

}
