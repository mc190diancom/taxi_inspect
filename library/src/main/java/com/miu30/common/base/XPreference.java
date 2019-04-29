package com.miu30.common.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.Set;

public class XPreference {
    private static final String DEFAULT_SP_NAME = "prefs_def";

    private String mSpName;
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    public XPreference(String mSpName, Context mContext) {
        super();
        this.mContext = mContext;
        if (TextUtils.isEmpty(mSpName)) {
            this.mSpName = DEFAULT_SP_NAME;
        } else {
            this.mSpName = mSpName;
        }
    }

    public boolean hasKey(Context context, final String key) {
        return getPreferences().contains(key);
    }

    public String getString(String key, String defaultValue) {
        return getPreferences().getString(key, defaultValue);
    }

    public boolean setString(String key, String value) {
        return getPreferences().edit().putString(key, value).commit();
    }

    public boolean setStringSet(String key, Set<String> value) {
        return getPreferences().edit().putStringSet(key, value).commit();
    }

    public Set<String> getStringSet(String key) {
        return getPreferences().getStringSet(key, null);
    }

    public Set<String> getStringSet(String key, Set<String> defaultValues) {
        return getPreferences().getStringSet(key, defaultValues);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return getPreferences().getBoolean(key, defaultValue);
    }

    public boolean setBoolean(String key, boolean value) {
        return getPreferences().edit().putBoolean(key, value).commit();
    }

    public int getInt(String key, int defaultValue) {
        return getPreferences().getInt(key, defaultValue);
    }

    public boolean setInt(String key, int value) {
        return getPreferences().edit().putInt(key, value).commit();
    }

    public float getFloat(String key, float defaultValue) {
        return getPreferences().getFloat(key, defaultValue);
    }

    public boolean setFloat(String key, float value) {
        return getPreferences().edit().putFloat(key, value).commit();
    }

    public long getLong(String key, long defaultValue) {
        return getPreferences().getLong(key, defaultValue);
    }

    public boolean setLong(String key, long value) {
        return getPreferences().edit().putLong(key, value).commit();
    }

    public boolean remove(String key) {
        return getPreferences().edit().remove(key).commit();
    }

    public boolean clearPreference() {
        return getPreferences().edit().clear().commit();
    }

    protected SharedPreferences getPreferences() {
        if (mSharedPreferences == null) {
            mSharedPreferences = mContext.getSharedPreferences(mSpName,
                    Context.MODE_PRIVATE);
        }
        return mSharedPreferences;
    }
}
