package com.miu360.taxi_check.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.miu360.inspect.R;
import com.miu360.taxi_check.common.Config;
import com.miu360.taxi_check.model.NetCarQrInfo;
import com.miu360.taxi_check.model.NetPersonQrInfo;

public class PersonQrAdapter extends BaseAdapter {

	ArrayList<NetPersonQrInfo> list;
	LayoutInflater mInflater;
	BitmapUtils mBitmap;

	public PersonQrAdapter(Context ctx, ArrayList<NetPersonQrInfo> list) {
		this.list = list;
		mInflater = LayoutInflater.from(ctx);
		mBitmap = new BitmapUtils(ctx);
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
		PersonQrHolderView holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.net_person_qrinfo_dapter, parent, false);
			holder = new PersonQrHolderView();
			convertView.setTag(holder);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.sex = (TextView) convertView.findViewById(R.id.sex);
			holder.age = (TextView) convertView.findViewById(R.id.age);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.cardtype = (TextView) convertView.findViewById(R.id.cardtype);
			holder.carno = (TextView) convertView.findViewById(R.id.carno);
			holder.fangweima = (TextView) convertView.findViewById(R.id.fangweima);
			holder.header_image = (ImageView) convertView.findViewById(R.id.header_image);
		} else {
			holder = (PersonQrHolderView) convertView.getTag();
		}
		holder.name.setText(list.get(position).getNM());
		holder.sex.setText(list.get(position).getXB());
		holder.age.setText(list.get(position).getAGE());
		holder.date.setText(list.get(position).getZCYXQ());
		holder.cardtype.setText(list.get(position).getPERSONTYPE());
		holder.carno.setText(list.get(position).getCPH());
		holder.fangweima.setText(list.get(position).getFWM());
		if(list.get(position).getPICTIME() == null){
			list.get(position).setPICTIME("2017");
		}
		mBitmap.display(holder.header_image, Config.SERVER_TAXIINFO+"?type="+"queryIdPhoto"
				+"&pcode="+list.get(position).getPCODE()+"&picctime="+list.get(position).getPICTIME().replace(" ", "%"));

		Log.e("图片路径", "图片路径"+Config.SERVER_TAXIINFO+"?type="+"queryIdPhoto"
				+"&pcode="+list.get(position).getPCODE()+"&picctime="+list.get(position).getPICTIME());
		return convertView;
	}

}

class PersonQrHolderView {
	public TextView name;
	public TextView sex;
	public TextView age;
	public TextView date;
	public TextView cardtype;
	public TextView carno;
	public TextView fangweima;
	ImageView header_image;
}
