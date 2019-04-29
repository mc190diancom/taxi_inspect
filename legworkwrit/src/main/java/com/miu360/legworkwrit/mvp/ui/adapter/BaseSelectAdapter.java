package com.miu360.legworkwrit.mvp.ui.adapter;


import android.support.v4.util.ArrayMap;
import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.mvp.ui.holder.BaseSelectHolder;

import java.util.List;
import java.util.Map;

public abstract class BaseSelectAdapter<T> extends DefaultAdapter<T> {

    private Map<Integer, T> checkMap;
    private boolean selectOnly = true;

    public BaseSelectAdapter(List<T> infos, boolean selectOnly) {
        super(infos);
        this.selectOnly = selectOnly;
        checkMap = new ArrayMap<>();
    }


    @Override
    public void onBindViewHolder(BaseHolder<T> holder, int position) {
        BaseSelectHolder baseSelectHolder = (BaseSelectHolder) holder;
        baseSelectHolder.setCheckedData(checkMap.containsKey(position), mInfos.get(position), position);
    }

    /**
     * 若果不想用此布局，请复写此方法
     *
     * @param viewType
     * @return
     */
    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_select;
    }

    /**
     * 若要进行单选，多选，请实现这个ClickListener
     *
     * @param OnRecyclerViewItemClickListener
     */
    public void setOutSideOnItemClickListener(final OnRecyclerViewItemClickListener<T> OnRecyclerViewItemClickListener) {
        setOnItemClickListener(new OnRecyclerViewItemClickListener<T>() {
            @Override
            public void onItemClick(View view, int viewType, T t, int position) {
                if (selectOnly) {
                    clearChecked();
                }
                checkMap.put(position, t);
                if (selectOnly) {
                    notifyDataSetChanged();
                } else {
                    notifyItemChanged(position);
                }
                if (OnRecyclerViewItemClickListener != null) {
                    OnRecyclerViewItemClickListener.onItemClick(view, viewType, t, position);
                }

            }
        });
    }

    /**
     * 若要进行单选，多选，请实现这个ClickListener 可取消如checkBox
     *
     * @param OnRecyclerViewItemClickListener
     */
    public void setOutSideCancelOnItemClickListener(final OnRecyclerViewItemClickListener<T> OnRecyclerViewItemClickListener) {
        setOnItemClickListener(new OnRecyclerViewItemClickListener<T>() {
            @Override
            public void onItemClick(View view, int viewType, T t, int position) {
                if (selectOnly) {
                    clearChecked();
                    checkMap.put(position, t);
                    notifyDataSetChanged();
                } else {
                    if (checkMap.containsKey(position)) {
                        checkMap.remove(position);
                        notifyDataSetChanged();
                    } else {
                        checkMap.put(position, t);
                        notifyItemChanged(position);
                    }
                }
                if (OnRecyclerViewItemClickListener != null) {
                    OnRecyclerViewItemClickListener.onItemClick(view, viewType, t, position);
                }

            }
        });
    }

    public Map<Integer, T> getcheckMap() {
        return checkMap;
    }

    public void clearChecked() {
        if (checkMap != null) {
            checkMap.clear();
        }
        notifyDataSetChanged();
    }
}
