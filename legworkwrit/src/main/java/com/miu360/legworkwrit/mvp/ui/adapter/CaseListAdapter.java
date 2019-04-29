package com.miu360.legworkwrit.mvp.ui.adapter;

import android.os.Handler;
import android.os.Looper;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.miu360.legworkwrit.R;
import com.miu360.legworkwrit.mvp.model.entity.Case;
import com.miu360.legworkwrit.util.TimeTool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

public class CaseListAdapter extends DefaultAdapter<Case> {
    private boolean isSingleSelection;
    private boolean isShowWithChecxBox;

    private List<Case> allCases;
    private List<Case> checkedCases;

    private SparseBooleanArray array;
    private Handler handler = new Handler(Looper.getMainLooper());

    private CommonListener listener;

    public CaseListAdapter(List<Case> infos, boolean isSingleSelection, boolean isShowWithChecxBox) {
        super(infos);

        this.isSingleSelection = isSingleSelection;
        this.isShowWithChecxBox = isShowWithChecxBox;
        this.allCases = infos;

        this.checkedCases = new ArrayList<>();
        this.array = new SparseBooleanArray();

        for (int i = 0; i < infos.size(); i++) {
            array.put(i, false);
        }
    }

    @Override
    public BaseHolder<Case> getHolder(final View v, int viewType) {
        return new BaseHolder<Case>(v) {
            @Override
            public void setData(final Case data, final int position) {
                TextView tvSort = v.findViewById(R.id.sort);
                TextView tvLicense = v.findViewById(R.id.license);
                TextView tvCarType = v.findViewById(R.id.car_type);
                TextView tvDriver = v.findViewById(R.id.driver);
                TextView tvTime = v.findViewById(R.id.time);
                CheckBox cb = v.findViewById(R.id.item_cb_check);

                tvSort.setText(String.valueOf(position + 1));
                tvLicense.setText(data.getVNAME());
                tvCarType.setText(data.getHYLB());
                tvDriver.setText(data.getBJCR());

                try {
                    long seconds = Long.valueOf(data.getCREATEUTC());
                    tvTime.setText(TimeTool.yyyyMMdd_HHmm.format(new Date(seconds * 1000)));
                } catch (Exception e) {
                    Timber.w(e);
                    tvTime.setText("");
                }

                v.findViewById(R.id.iv_go).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onGoNext(data);
                        }
                    }
                });

                if (isShowWithChecxBox) {
                    cb.setOnCheckedChangeListener(null);
                    cb.setChecked(array.get(position));
                    cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                if (isSingleSelection) {
                                    //单选
                                    for (int i = 0; i < array.size(); i++) {
                                        array.put(i, false);
                                    }
                                }
                            }

                            array.put(position, isChecked);
                            checkedCases.clear();

                            for (int i = 0; i < array.size(); i++) {
                                int key = array.keyAt(i);
                                if (array.get(key)) {
                                    checkedCases.add(allCases.get(key));
                                }
                            }

                            if (listener != null) {
                                listener.onCheckCases(checkedCases);
                            }

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    notifyDataSetChanged();
                                }
                            });
                        }
                    });
                } else {
                    cb.setVisibility(View.GONE);
                }
            }
        };
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.adapter_case_item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setCommonListener(CommonListener listener) {
        this.listener = listener;
    }

    public interface CommonListener {
        /*
         * 点击前进图片回调此方法
         */
        void onGoNext(Case c);

        /*
         * 回调选中的案件
         */
        void onCheckCases(List<Case> cases);
    }

}
