package com.zkys.pad.fota.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.zkys.pad.fota.app.activation.ui.ActivateActivity;
import com.zkys.pad.fota.base.App;

public class AppHeleper {

    /**
     *  获取当前是否是debug版本
     * @param context
     * @return
     */
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断给定的包名是否已安装
     *
     * @param packName 给定的包名
     * @return 如果已安装返回true，否则返回false
     * */
    public static boolean isPackageInstalled(Context context,String packName) {
        return getPackageInfo(context,packName) != null;
    }

    /**
     * 根据包名直接返回PackageInfo
     *
     * @param packageName
     *            指定包名
     * */
    public static PackageInfo getPackageInfo(Context context, String packageName) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return info;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /**
     *   跳转
     * @param c
     */
    public static void startActivity(Class c){
        Intent intent=new Intent(App.getContext(), c);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        App.getContext().startActivity(intent);
    }
}
