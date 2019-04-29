package com.miu360.legworkwrit.mvp.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.miu360.legworkwrit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：wanglei on 2018/10/17.
 * 邮箱：forwlwork@gmail.com
 */
public class MultipleChoiceDialogAdapter extends BaseAdapter {
    private String[] data;
    private Context context;

    private List<String> items;
    private List<String> list;

    public MultipleChoiceDialogAdapter(@NonNull Context context, @NonNull String[] data, List<String> list) {
        this.data = data;
        this.context = context;
        this.list = list;
        this.items = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_multiple_choice, null, false);
            holder.checkBox = convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.checkBox.setText(data[position]);
        /*if(list != null && list.contains(data[position])){
            holder.checkBox.setChecked(true);
            items.add(data[position]);
        }else {
            holder.checkBox.setChecked(false);
        }*/
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    items.add(data[position]);
                } else {
                    items.remove(data[position]);
                }
            }
        });

        return convertView;
    }

    private class Holder {
        CheckBox checkBox;
    }

    public List<String> getChoiceItems() {
        return items;
    }

}
