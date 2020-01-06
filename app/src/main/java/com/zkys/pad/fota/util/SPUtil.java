package com.zkys.pad.fota.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zkys.pad.fota.base.Global;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Johnny-R on 2017/6/27.
 * 一个用户id文件
 * 一个账号对应一个文件
 */
public class SPUtil {

    public static final String  HOST = "host";

//    public static final String HOST_VERSION = "host_version";
//    public static final String AUTO_OFF = "AUTO_OFF";
//    public static final String SHOW_LOG = "show_log";
//
//    public static final String PAD_CONFIG_VOLUME_RATE = "PadConfigEntityVolume";
//    public static final String PAD_CONFIG_SIGNAL_LEVEL = "PAD_CONFIG_SIGNAL_LEVEL";
//    public static final String PAD_CONFIG_SIGNAL_TYPE = "PAD_CONFIG_SIGNAL_TYPE";
//    public static final String PAD_CONFIG_OPEN_NETWORK_SPEEK = "PAD_CONFIG_OPEN_NETWORK_SPEEK";


    private static SharedPreferences getSharedPreferences() {
        SharedPreferences sp = Global.getContext().getApplicationContext().getSharedPreferences("zkys_pad_fota", Context.MODE_PRIVATE);
        return sp;
    }


    public static void putString(String key, String value) {
        SharedPreferences sp = getSharedPreferences();
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }


    public static String getString(String key) {
        SharedPreferences sp = getSharedPreferences();
        return sp.getString(key, "");
    }

    public static void putLong(String key, long value) {
        SharedPreferences sp = getSharedPreferences();
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong(key, value);
        edit.commit();
    }

    public static long getLong(String key) {
        SharedPreferences sp = getSharedPreferences();
        return sp.getLong(key, -1);
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences sp = getSharedPreferences();
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static boolean getBoolean(String key) {
        SharedPreferences sp = getSharedPreferences();
        return sp.getBoolean(key, false);
    }

    /**
     * 保存集合到本地
     *
     * @param key
     * @param value
     */
    public static void saveMapData(@NonNull String key, @NonNull Map<String, String> value) {
        Gson gson = new Gson();
        String editRescueOrder = gson.toJson(value);
        SPUtil.putString(key, editRescueOrder);
    }

    public static Map<String, String> getMapData(@NonNull String key) {
        if (TextUtils.isEmpty(getString(key))) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> map = gson.fromJson(getString(key), type);
        return map;

    }
//
}
