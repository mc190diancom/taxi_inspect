package com.miu360.legworkwrit.mvp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miu360.legworkwrit.R;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mData;

    public GridViewAdapter(List<String> mData,Context context){
        this.mContext = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        if (null != mData) {
            return mData.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //优化内容
        ViewHolder viewHolder;
        //如果conview是空
        if (convertView == null) {
            //把xml文件id转化为一个view对象
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_case_sign_item,
                    parent, false);
            //创建对象
            viewHolder = new ViewHolder();
            //将控件放在viewholder中
            viewHolder.name = convertView.findViewById(R.id.tv_name);
            //用setTag方法将viewHolder在View中保存
            convertView.setTag(viewHolder);
        } else {
            //从view中取出
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //显示图片和文字
        viewHolder.name.setText(mData.get(position));
        return convertView;
    }

    class ViewHolder{
        TextView name;
    }
}

