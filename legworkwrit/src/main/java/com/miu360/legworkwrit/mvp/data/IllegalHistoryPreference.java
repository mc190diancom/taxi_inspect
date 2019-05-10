package com.miu360.legworkwrit.mvp.data;

import android.content.Context;
import android.text.TextUtils;

import com.miu30.common.base.BaseData;
import com.miu30.common.base.XPreference;
import com.miu30.common.ui.entity.JCItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 作者：wanglei on 2019/3/7.
 * 邮箱：forwlwork@gmail.com
 */
public class IllegalHistoryPreference extends XPreference {
    private static final String NAME = "illegal_history";

    public IllegalHistoryPreference(Context mContext) {
        super(NAME, mContext);
    }

    //----------------------------------- 根据行业类别和处罚方式来缓存历史选择的5条涉嫌违法事由 -----------------------------

    public void putIllegalHistory(String HYLB, String CFFS, JCItem item) {
        if (TextUtils.isEmpty(HYLB) || TextUtils.isEmpty(CFFS) || item == null) {
            return;
        }

        String key = HYLB + CFFS;

        Set<String> allPref = getStringSet(key);
        TreeSet<String> all = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                TimeStampWithJCItem item1 = BaseData.gson.fromJson(o1, TimeStampWithJCItem.class);
                TimeStampWithJCItem item2 = BaseData.gson.fromJson(o2, TimeStampWithJCItem.class);
                return (int) (item2.getTime() - item1.getTime());
            }
        });
        if (allPref != null) {
            all.addAll(allPref);
        }

        TimeStampWithJCItem itemWithTime = new TimeStampWithJCItem(System.currentTimeMillis(), item);

        if (!all.isEmpty()) {
            String same = "";

            for (String s : all) {
                TimeStampWithJCItem temp = BaseData.gson.fromJson(s, TimeStampWithJCItem.class);
                if (item.equals(temp.getItem())) {
                    same = s;
                    break;
                }
            }

            if (!TextUtils.isEmpty(same)) {
                all.remove(same);
            }
        }

        if (all.size() >= 5) {
            all.pollLast();
        }

        all.add(BaseData.gson.toJson(itemWithTime));
        setStringSet(key, all);
    }

    public List<JCItem> getIllegalHistory(String HYLB, String CFFS) {
        Set<String> all = getStringSet(HYLB + CFFS);

        if (all != null && !all.isEmpty()) {
            TreeSet<String> allSort = new TreeSet<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    TimeStampWithJCItem item1 = BaseData.gson.fromJson(o1, TimeStampWithJCItem.class);
                    TimeStampWithJCItem item2 = BaseData.gson.fromJson(o2, TimeStampWithJCItem.class);
                    return (int) (item2.getTime() - item1.getTime());
                }
            });
            allSort.addAll(all);

            List<JCItem> result = new ArrayList<>(all.size());
            for (String s : allSort) {
                TimeStampWithJCItem item = BaseData.gson.fromJson(s, TimeStampWithJCItem.class);
                result.add(item.getItem());
            }

            return result;
        }

        return null;
    }



    //-------------------------------------------------------------------------------------------------------------

    private static class TimeStampWithJCItem {
        private long time;
        private JCItem item;

        TimeStampWithJCItem(long time, JCItem item) {
            this.time = time;
            this.item = item;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public JCItem getItem() {
            return item;
        }

        public void setItem(JCItem item) {
            this.item = item;
        }
    }

}
