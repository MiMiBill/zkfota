package com.zkys.pad.fota.util;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.zkys.pad.fota.app.versionupdate.VersionUpdateApp;
import com.zkys.pad.fota.app.versionupdate.bean.PackageBean;
import com.zkys.pad.fota.app.versionupdate.eventbus.DownLoadingEntity;
import com.zkys.pad.fota.base.App;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class AppInstallUtil {

    private static final String TAG="AppInstallUtil";

    /**
     * 静默安装
     *
     * @param context
     * @param filePath
     * @return 0 means normal, 1 means file not exist, 2 means other exception error
     */
    public static int installSlient(Context context, String filePath, String packName) {
        File file = new File(filePath);
        if (filePath == null || filePath.length() == 0 || (file = new File(filePath)) == null || file.length() <= 0
                || !file.exists() || !file.isFile()) {
            return 1;
        }
        String[] args;
        int sdkVersion=Integer.valueOf(Build.VERSION.SDK);
        if(sdkVersion>=24){
            Logs.i(TAG,"7.0静默安装");
            args = new String[]{"pm", "install", "-i", "com.zkys.pad.launcher.fota","--user","0",filePath};
        }else {
            Logs.i(TAG,"其他静默安装");
            args = new String[]{"pm", "install", "-r", filePath};
        }

        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();
        int result = 0;
        try {
            process = processBuilder.start();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }
            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = 2;
        } catch (Exception e) {
            e.printStackTrace();
            result = 2;
        } finally {
            try {
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
        // TODO should add memory is not enough here
        if (successMsg.toString().contains("Success") || successMsg.toString().contains("success")) {
            installEnd(filePath,packName,5);
        } else {
            installEnd(filePath,packName,6);
        }
        Log.e("installSlient", "successMsg:" + successMsg + ", ErrorMsg:" + errorMsg);
        return result;
    }

    /**
     *  安装完成
     * @param path
     * @param pkgName
     */
    private static void installEnd(String path,String pkgName,int result){
        for (PackageBean packageBean: VersionUpdateApp.getInstance().downLoadList){
            if(pkgName.equals(packageBean.getPkgName())){
                packageBean.setFlag(result);
                EventBus.getDefault().post(new DownLoadingEntity(VersionUpdateApp.getInstance().downLoadList));
                Logs.e(TAG,"安装完成："+result);
//                App.getContext().sendBroadcast(new Intent("installEnd"));
                return;
            }
        }
        // 删除安装文件
        FileUtil.deleteFile(path);
    }

    /**
     * 静默安装
     */
    public static boolean clientInstall(String apkPath){
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("chmod 777 "+apkPath);
            PrintWriter.println("export LD_LIBRARY_PATH=/vendor/lib:/system/lib");
            PrintWriter.println("pm install -r "+apkPath);
//          PrintWriter.println("exit");
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(process!=null){
                process.destroy();
            }
        }
        return false;
    }


    public static boolean installApp(String apkPath) {
        Logs.e(TAG,"install:"+apkPath);
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();
        try {
            process = new ProcessBuilder("pm", "install", "-i", "com.zkys.pad.launcher.fota","--user","0",apkPath).start();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }
            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
        } catch (Exception e) {

        } finally {
            try {
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (Exception e) {

            }
            if (process != null) {
                process.destroy();
            }
        }
        Log.e("result", "" + errorMsg.toString());
        //如果含有“success”单词则认为安装成功
        return successMsg.toString().equalsIgnoreCase("success");
    }


    private static boolean returnResult(int value){
        // 代表成功
        if (value == 0) {
            return true;
        } else if (value == 1) { // 失败
            return false;
        } else { // 未知情况
            return false;
        }
    }
}
