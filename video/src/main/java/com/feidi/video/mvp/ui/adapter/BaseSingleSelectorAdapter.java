package com.feidi.video.mvp.ui.adapter;

import android.view.View;
import android.widget.CheckBox;

import com.feidi.video.R;
import com.feidi.video.mvp.model.entity.ISelector;
import com.feidi.video.mvp.ui.adapter.listener.OnItemContentSelectedChangeListener;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

/**
 * 作者：wanglei on 2019/6/3.
 * 邮箱：forwlwork@gmail.com
 * <p>
 * 实现单选功能的Adapter
 */
public abstract class BaseSingleSelectorAdapter extends DefaultAdapter<ISelector> {
    private List<ISelector> infos;

    private CheckBox lastCheckBox = null;
    private int lastCheckedPosition = 0;

    private OnItemContentSelectedChangeListener<ISelector> onItemContentSelectedChangeListener;

    BaseSingleSelectorAdapter(List<ISelector> infos) {
        super(infos);
        this.infos = infos;
    }

    @Override
    public BaseHolder<ISelector> getHolder(final View v, int viewType) {
        return new BaseHolder<ISelector>(v) {
            @Override
            public void setData(final ISelector selector, final int position) {
                final CheckBox checkBox = v.findViewById(R.id.item_cb_des);
                checkBox.setChecked(selector.isSelected());
                setCheckBoxText(selector, checkBox);
                checkBox.setTag(position);

                if (position == 0 && selector.isSelected() && checkBox.isChecked()) {
                    lastCheckBox = checkBox;
                    lastCheckedPosition = 0;
                }

                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        int clickPosition = (int) cb.getTag();
                        boolean isChecked = cb.isChecked();

                        if (isChecked) {
                            if (lastCheckBox != null) {
                                lastCheckBox.setChecked(false);
                                infos.get(lastCheckedPosition).setSelected(false);
                            }

                            lastCheckBox = cb;
                            lastCheckedPosition = clickPosition;
                        } else {
                            lastCheckBox = null;
                        }

                        selector.setSelected(isChecked);
                        if (onItemContentSelectedChangeListener != null) {
                            onItemContentSelectedChangeListener.onSelectedChange(cb, selector, clickPosition, isChecked);
                        }
                    }
                });
            }
        };
    }

    public abstract void setCheckBoxText(ISelector data, CheckBox checkBox);

    public void setOnItemContentSelectedChangeListener(OnItemContentSelectedChangeListener<ISelector> onItemContentSelectedChangeListener) {
        this.onItemContentSelectedChangeListener = onItemContentSelectedChangeListener;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_common;
    }
}
