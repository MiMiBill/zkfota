package com.zkys.pad.fota.util;

import android.util.Log;

import com.orhanobut.logger.Logger;

/**
 * explain:自定义Log日志输出
 * Date: 2016-10-12  15：32.
 */
public class Logs {

    private static boolean isDebug = true;

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, "---->        "+msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, "---->        "+msg);
        }
    }
    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, "---->        "+msg);

        }
    }
    public static void w(String tag, String msg){
        if (isDebug){
            Log.w(tag, "---->        "+msg);
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
