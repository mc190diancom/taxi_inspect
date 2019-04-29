package com.miu360.legworkwrit.mvp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miu30.common.ui.entity.DriverInfo;
import com.miu30.common.ui.entity.VehicleInfo;
import com.miu360.legworkwrit.R;

import java.util.ArrayList;

public class CarInfoAdapter extends BaseAdapter {
    private ArrayList<VehicleInfo> list_driverName;
    private Context mContext;

    public CarInfoAdapter(ArrayList<VehicleInfo> list_driverName, Context mContext){
        this.list_driverName = list_driverName;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return list_driverName.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list_driverName.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        viewHolderLicence holder_driver;
        if (view == null) {
            holder_driver = new viewHolderLicence();
            view = LayoutInflater.from(mContext).inflate(R.layout.list_info_adapter, parent, false);
            view.setTag(holder_driver);

            holder_driver.info =  view.findViewById(R.id.info);

        } else {
            holder_driver = (viewHolderLicence) view.getTag();
        }
        holder_driver.info.setText(list_driverName.get(position).getVname());
        return view;
    }

    private class viewHolderLicence {
        TextView info;
    }
}
