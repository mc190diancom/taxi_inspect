package com.miu360.taxi_check.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.miu360.inspect.R;
import com.miu360.taxi_check.BaseFragment;
import com.miu360.taxi_check.adapter.HtmlAdapter;
import com.miu360.taxi_check.common.Config;
import com.miu30.common.data.UserPreference;
import com.miu360.taxi_check.model.CardQr;
import com.miu30.common.util.Store2SdUtil;
import com.miu360.taxi_check.view.ListViewHolder;

import java.io.File;
import java.util.ArrayList;

public class HistoryQrFragment extends BaseFragment {
	ListViewHolder holder;
	private View root;
	HtmlAdapter mAdapter;
    ArrayList<String> list = new ArrayList<>();
	
	HtmlAdapter mAdapter3;
	ArrayList<String> list3 = new ArrayList<>();
	
	HtmlAdapter mAdapter2;
	ArrayList<String> list2 = new ArrayList<>();
	
	BitmapUtils mBitmap;
	int type;
	UserPreference pref;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.temporarysavefragment, null);
		pref = new UserPreference(act);
		initView(root);
		initData();
		
		return root;
	}

	public static HistoryQrFragment newInstance(int type) {
		HistoryQrFragment f = new HistoryQrFragment();
		Bundle args = new Bundle();
		args.putInt("type", type);
		f.setArguments(args);
		return f;
	}

	private void initData(){
		File file = new File(Config.PATH+Config.ID_FILE_NAME);
		if(!file.exists()){
			return;
		}
		ArrayList<CardQr> qrlist = Store2SdUtil.getInstance(act)
				.readInArrayObject(Config.ID_FILE_NAME,
						new TypeToken<ArrayList<CardQr>>() {
						});
		
		holder.list.onRefreshComplete();
		if(qrlist.isEmpty()){
			return;
		}
		for(int i=0,len=qrlist.size();i<len;i++){
			if("2".equals(qrlist.get(i).getId())){
				list2.add(qrlist.get(i).getPath());
			}else if("3".equals(qrlist.get(i).getId())){
				list3.add(qrlist.get(i).getPath());
			}else{
				list.add(qrlist.get(i).getPath());
			}
		}
		if(1 == type){
			mAdapter.notifyDataSetChanged();
			holder.mayShowEmpty(list.size());
		}else if(2 == type){
			mAdapter2.notifyDataSetChanged();
			holder.mayShowEmpty(list2.size());
		}else if(3 == type){
			mAdapter3.notifyDataSetChanged();
			holder.mayShowEmpty(list3.size());
		}
	}
	
	private void initView(View root) {
		ViewUtils.inject(this, root);
		mBitmap = new BitmapUtils(act);
		holder = ListViewHolder.initList(act, root);
		this.type = getArguments().getInt("type");
		if(1 == type){
			mAdapter = new HtmlAdapter(getActivity(), list);
			holder.list.setAdapter(mAdapter);
		}else if(2 == type){
			mAdapter2 = new HtmlAdapter(getActivity(), list2);
			holder.list.setAdapter(mAdapter2);
		}else if(3 == type){
			mAdapter3 = new HtmlAdapter(getActivity(), list3);
			holder.list.setAdapter(mAdapter3);
		}
		
		holder.list.setMode(Mode.DISABLED);
		holder.list.setRefreshing();
	}


}
