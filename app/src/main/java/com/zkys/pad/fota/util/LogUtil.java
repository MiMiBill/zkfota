package com.zkys.pad.fota.util;

import android.util.Log;

import com.orhanobut.logger.Logger;

/**
 * explain:自定义Log日志输出
 * Date: 2016-10-12  15：32.
 */
public class LogUtil {

    private static boolean isDebug = true;

    public static void d(Object obj) {
        if (isDebug) {
            Logger.d(obj);
        }
    }
    public static void d(String msg, Object... obj) {
        if (isDebug) {
            Logger.d(msg, obj);
        }
    }

    public static void i(String msg, Object... obj) {
        if (isDebug) {
            Logger.i(msg, obj);
        }
    }
    public static void e(String msg, Object... obj) {
        if (isDebug) {
            Logger.e(msg, obj);

        }
    }
    public static void w(String msg, Object... obj){
        if (isDebug){
            Logger.w(msg, obj);
        }
    }
    public static void e(Throwable throwable, String msg, Object... obj) {
        if (isDebug) {
            Logger.e(throwable, msg, obj);
        }
    }
    public static void v(String msg, Object... obj) {
        if (isDebug) {
            Logger.v(msg, obj);
        }
    }
    public static void json(String json) {
        if (isDebug) {
            Logger.json(json);
        }
    }
}
