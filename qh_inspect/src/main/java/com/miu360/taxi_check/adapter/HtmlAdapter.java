package com.miu360.taxi_check.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.BitmapUtils;
import com.miu360.inspect.R;
import com.miu360.taxi_check.common.Config;
import com.miu360.taxi_check.model.NetCarQrInfo;

public class HtmlAdapter extends BaseAdapter {

	ArrayList<String> list;
	Context ctx;
	BitmapUtils mBitmap;

	public HtmlAdapter(Context ctx, ArrayList<String> list) {
		this.list = list;
		this.ctx =ctx;
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
		//HtmlHolderView holder = null;
		//if (convertView == null) {
		convertView = LayoutInflater.from(ctx).inflate(R.layout.net_car_qrinfo_dapter, parent, false);
		String data = list.get(position);
		Log.e("zzar", "zzar:"+data);
		JSONObject jsona = null;
		JSONObject jsona2 = null;
		JSONArray jsonArray = null;
		try {
			jsona = new JSONObject(data);
			jsonArray = jsona.getJSONArray("info");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(320, 400);
		params.setMargins(15, 15, 0, 0);
		if(!"无".equals(jsona.optString("img")) && !"".equals(jsona.optString("img"))){
			ImageView img = new ImageView(ctx);
			img.setLayoutParams(params2);
			String imgadd = jsona.optString("img").replace(" ", "");
			mBitmap.configDefaultLoadFailedImage(R.drawable.default_photo);
			mBitmap.display(img, Config.SERVER_TAXIINFO+"?type=queryRqCodePhoto&url="+imgadd);
			((LinearLayout) convertView).addView(img);
		}
		for(int i=0,len=jsonArray.length();i<len;i++){
			TextView tv=new TextView(ctx);
			tv.setLayoutParams(params);
			tv.setText(jsonArray.optString(i));
			((LinearLayout) convertView).addView(tv);
		}
		/*} else {
			holder = (HtmlHolderView) convertView.getTag();
		}*/
		return convertView;
	}

}

class HtmlHolderView {
	public TextView VName;
	public TextView CarOwn;
	public TextView CarModel;
	public TextView CarColor;
	public TextView PlatformName;
	public TextView Date;
	public TextView VIN;
	public TextView EngineNO;
	public TextView CarState;
	public TextView SecurityCode;
}
