package com.zkys.pad.fota.util;

/**
 * Created by 92QC_JSB_123 on 2017/10/24.
 */

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.zkys.pad.fota.base.Global;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/15.
 * 捕获异常
 */

public class AppUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    //程序的Context对象
    private Context applicationContext;

    private volatile boolean crashing;

    /**
     * 日期格式器
     */
    private DateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /**
     * 单例
     */
    private static AppUncaughtExceptionHandler sAppUncaughtExceptionHandler;

    public static synchronized AppUncaughtExceptionHandler getInstance() {
        if (sAppUncaughtExceptionHandler == null) {
            synchronized (AppUncaughtExceptionHandler.class) {
                if (sAppUncaughtExceptionHandler == null) {
                    sAppUncaughtExceptionHandler = new AppUncaughtExceptionHandler();
                }
            }
        }
        return sAppUncaughtExceptionHandler;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        applicationContext = context.getApplicationContext();
        crashing = false;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (crashing) {
            return;
        }
        crashing = true;

        // 打印异常信息
        ex.printStackTrace();
        // 我们没有处理异常 并且默认异常处理不为空 则交给系统处理
        if (!handlelException(ex) && mDefaultHandler != null) {
            // 系统处理
            mDefaultHandler.uncaughtException(thread, ex);
        }
//        byebye();
    }

    private void byebye() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    private boolean handlelException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        try {
            // 异常信息
            String crashReport = getCrashReport(ex);
            Log.e("TAG===", crashReport);
            // TODO: 上传日志到服务器
            // 保存到sd卡
            saveExceptionToSdcard(crashReport);
//            上传到后台
            uploadToserver();
            // 提示对话框
            showPatchDialog();
//            重启app
            restartApp();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 上传文件
     */
    private void uploadToserver() {
//便利文件夹下的文件，一个个文件上传

     /*   Retrofit retrofit = new Retrofit.Builder().baseUrl(new HttpConfig().getBaseUrl()).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
        CarClubApi uploadService = retrofit.create(CarClubApi.class);
        String fileName = "Crash.log";
        if (SdcardConfig.getInstance().hasSDCard()) {
            String path = SdcardConfig.LOG_FOLDER;
            File file = new File(path, fileName);
        *//*String path = "/storage/emulated/0/Pictures/1477553156332.jpg";

        File file = new File(path);*//*

            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            *//**
         * 创建多部分拿上面的请求体做参数
         * img 是上传是的参数key,根据需要更改为自己的
         *//*
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

           *//* Observable<String> stringObservable = uploadService.uploadFile(body);

            stringObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
                @Override
                public void accept(@NonNull String s) throws Exception {
                    LogUtil.e("上传成功========" + s);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(@NonNull Throwable throwable) throws Exception {
                    LogUtil.e("上传失败========" + throwable.getMessage());
                }
            });
*/
    }

    private void showPatchDialog() {
        /*Intent intent = lo.newIntent(applicationContext, getApplicationName(applicationContext), null);
        applicationContext.startActivity(intent);*/
    }
    private void restartApp(){
        Global.restartApplication();
    }

    private String getApplicationName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        String name = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(
                    context.getApplicationInfo().packageName, 0);
            name = (String) packageManager.getApplicationLabel(applicationInfo);
        } catch (final PackageManager.NameNotFoundException e) {
            String[] packages = context.getPackageName().split(".");
            name = packages[packages.length - 1];
        }
        return name;
    }

    /**
     * 获取异常信息
     *
     * @param ex
     * @return
     */
    private String getCrashReport(Throwable ex) {
        StringBuffer exceptionStr = new StringBuffer();
        PackageInfo pinfo = null;
        try {
            pinfo = Global.getContext().getPackageManager().getPackageInfo(Global.getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (pinfo != null) {
            if (ex != null) {
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = formatter.format(new Date());
                exceptionStr.append("发生时间：" + time + "\n");
                //app版本信息
                exceptionStr.append("app版本号：" + pinfo.versionName + "\n");
//				exceptionStr.append("_" + pinfo.versionCode + "\n");

                //手机系统信息
                exceptionStr.append("系统版本：" + Build.VERSION.RELEASE);
                exceptionStr.append("_");
                exceptionStr.append(Build.VERSION.SDK_INT + "\n");

                //手机制造商
                exceptionStr.append("手机制造商: " + Build.MANUFACTURER + "\n");

                //手机型号
                exceptionStr.append("手机型号: " + Build.MODEL + "\n");

                String errorStr = ex.getLocalizedMessage();
                if (TextUtils.isEmpty(errorStr)) {
                    errorStr = ex.getMessage();
                }
                if (TextUtils.isEmpty(errorStr)) {
                    errorStr = ex.toString();
                }
                exceptionStr.append("Exception: " + errorStr + "\n");
                StackTraceElement[] elements = ex.getStackTrace();
                if (elements != null) {
                    for (int i = 0; i < elements.length; i++) {
                        exceptionStr.append(elements[i].toString() + "\n");
                    }
                }
            } else {
                exceptionStr.append("no exception. Throwable is null\n");
            }
            return exceptionStr.toString();
        } else {
            return "";
        }
    }

    /**
     * 保存错误报告到sd卡
     *
     * @param errorReason
     */
    private void saveExceptionToSdcard(String errorReason) {
        try {
            Log.e("CrashDemo", "AppUncaughtExceptionHandler执行了一次");
//            String time = mFormatter.format(new Date());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fileName = sdf.format(new Date())+ ".log";
//            String fileName = "Crash-" + time + ".log";
//            String fileName = "Crash.log";
            // TODO 等待启用
            /*if (SdcardConfig.getInstance().hasSDCard()) {
                String path = SdcardConfig.LOG_FOLDER;
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName, true);
                fos.write(errorReason.getBytes());
                fos.close();
            }*/
        } catch (Exception e) {
            Log.e("CrashDemo", "an error occured while writing file..." + e.getMessage());
        }
    }


}
