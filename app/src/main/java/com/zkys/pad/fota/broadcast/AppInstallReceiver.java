package com.zkys.pad.fota.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.zkys.pad.fota.util.LogUtil;

/**
 * APK重新安装
 */
public class AppInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        LogUtil.d("fota apk重新启动 222");
        if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
            Uri data = intent.getData();
            LogUtil.d("fota apk重新启动");
            LogUtil.d("fota 更新安装成功  重新启动.....");//            if (data != null && context.getPackageName().equals(data.getEncodedSchemeSpecificPart())) {

            // 重新启动APP
            Intent intentToStart = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            context.startActivity(intentToStart);
//            if (data != null && context.getPackageName().equals(data.getEncodedSchemeSpecificPart())) {
//            if (TextUtils.equals(BuildConfig.APPLICATION_ID, data.getEncodedSchemeSpecificPart())) {
//                LogUtil.d("fota 更新安装成功  重新启动.....");//            if (data != null && context.getPackageName().equals(data.getEncodedSchemeSpecificPart())) {
//
//                // 重新启动APP
//                Intent intentToStart = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
//                context.startActivity(intentToStart);
//
//            }
        }
    }
}
