package com.miu360.legworkwrit.mvp.data;

import android.content.Context;

import com.miu30.common.base.XPreference;
import com.miu360.legworkwrit.mvp.model.entity.WifiConfig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 作者：wanglei on 2019/1/3.
 * 邮箱：forwlwork@gmail.com
 */
public class WifiPreference extends XPreference {
    private static final String NAME = "wifi_pref";

    private static final String KEY = "wifi_info";

    public WifiPreference(Context mContext) {
        super(NAME, mContext);
    }

    public void putWifi(WifiConfig config) {
        Set<String> wifis = new HashSet<>(getPreferences().getStringSet(KEY, new HashSet<String>()));
        wifis.add(config.getSn() + "/" + config.getSsid() + "/" + config.getPassword());
        setStringSet(KEY, wifis);
    }

    public List<WifiConfig> getWifis() {
        Set<String> wifis = getStringSet(KEY);

        if (wifis != null && !wifis.isEmpty()) {
            List<WifiConfig> wifiConfigs = new ArrayList<>(wifis.size());
            WifiConfig config;

            for (String item : wifis) {
                String[] split = item.split("/");
                config = new WifiConfig();
                config.setSn(split[0]);
                config.setSsid(split[1]);
                config.setPassword(split[2]);
                wifiConfigs.add(config);
            }

            return wifiConfigs;
        }

        return null;
    }

}
