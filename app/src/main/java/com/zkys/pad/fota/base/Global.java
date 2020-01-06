package com.zkys.pad.fota.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;

import java.util.Date;


/**
 * 全局类：屏幕尺寸，主线程运行，toast
 *
 */
public  class Global {

    public static final String APP_PATH = "app_path";
    public static boolean isShowToast = true;

    public static Context mContext;

    public static int mScreenWidth;
    public static int mScreenHeight;
    public static float mDensity;

    public static Context getContext() {
        return mContext;
    }

    public static int getMargin53(){
        return (int) (mScreenWidth * 0.053);
    }
    public static int getMargin56(){
        return (int) (mScreenWidth * 0.056);
    }



    public static void autoDialogParams(Dialog dialog){
        WindowManager.LayoutParams params =
                dialog.getWindow().getAttributes();
        params.width = (int) ((double) mScreenWidth * 0.9); // 宽度设置为屏幕的0.65;
        params.height = (int) ((double)mScreenHeight *0.55); // 高度设置为屏幕的0.6 ;
        dialog.getWindow().setAttributes(params);
    }




    public static void init(Context context) {
        Logger.d("contextssssss", context);
        mContext = context;

        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        mScreenWidth =  dm.widthPixels;
        mScreenHeight =  dm.heightPixels;
        mDensity =  dm.density;

    }

    /**
     * dp转px
     * @param dp
     * @return
     */
    public static int dp2px(int dp) {
        return (int) (mDensity * dp + 0.5f);
    }

    public static Handler mHandler = new Handler();

    /** 判断当前是否在主线程运行 */
    public static boolean isMainThread() {
        // 主线程的looper对象 == 当前线程的looper对象
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /** 主线程运行 */
    public static void runONMainThread(Runnable run) {
        if (isMainThread()) {
            run.run();
        } else { // 子线程
            mHandler.post(run);
        }
    }




    /**
     * 重启程序
     */
    public static void restartApplication() {
        final Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(Global.mContext.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Global.mContext.startActivity(intent);
    }


    /**
     * 主线程调用
     * @param run
     * @param tiem
     */
    public static void runONMainThread(Runnable run , int tiem) {
        if (isMainThread()) {
            mHandler.postDelayed(run, tiem);
        } else { // 子线程
            mHandler.postDelayed(run, tiem);
        }
    }
    public static void postDelayed(Runnable run , int tiem) {
        if (isMainThread()) {
            mHandler.postDelayed(run, tiem);
        } else { // 子线程
            mHandler.postDelayed(run, tiem);
        }
    }

    public static void removeCallbacks(Runnable run ) {
            mHandler.removeCallbacks(run);
    }

    /**
     * 加载布局
     * @param layout
     * @param parent
     * @return
     */
    public static View inflate(int layout, ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(layout, parent, false);
    }

    /**
     * 获取除以10的时间戳
     * @return
     */
    public static long getTIMESTAMP() {
        long TIMESTAMP = System.currentTimeMillis();
        TIMESTAMP = TIMESTAMP/1000;
        return TIMESTAMP;
    }

    /**
     * 获取除以10的时间戳
     * @return
     */
    public static long getTIMESTAMP(Date date) {
        long timeStamp= date.getTime();
        timeStamp = timeStamp / 1000;
        return timeStamp;
    }



}






















