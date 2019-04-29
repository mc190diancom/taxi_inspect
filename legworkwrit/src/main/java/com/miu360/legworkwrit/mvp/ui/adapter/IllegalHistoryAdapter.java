package com.miu360.legworkwrit.mvp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miu30.common.ui.entity.JCItem;
import com.miu360.legworkwrit.R;

import java.util.List;

/**
 * 作者：wanglei on 2019/3/7.
 * 邮箱：forwlwork@gmail.com
 */
public class IllegalHistoryAdapter extends BaseAdapter {
    private List<JCItem> data;
    private LayoutInflater inflater;
    private int size = 0;

    public IllegalHistoryAdapter(Context context, List<JCItem> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        if (data != null) {
            this.size = data.size();
        }
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        return data == null ? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_illegal_history, null, false);
            holder = new Holder();
            holder.textView = convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.textView.setText(data.get(position).getLBMC());
        return convertView;
    }

    private class Holder {
        TextView textView;
    }

}
