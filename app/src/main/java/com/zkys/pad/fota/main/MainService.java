package com.zkys.pad.fota.main;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.zkys.pad.fota.app.activation.ActivationApp;
import com.zkys.pad.fota.app.versionupdate.InstallService;
import com.zkys.pad.fota.app.versionupdate.eventbus.InstallEntity;
import com.zkys.pad.fota.base.App;
import com.zkys.pad.fota.util.AppInstallUtil;
import com.zkys.pad.fota.util.Logs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainService extends IntentService {

    private final String TAG="MainService";

    public MainService() {
        super("MainService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }


    @Override
    public void onCreate() {
        super.onCreate();
        Logs.i(TAG,"onCreate");
        EventBus.getDefault().register(this);
        init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logs.i(TAG,"onStartCommand");
        return START_STICKY;
    }

    private void init(){
        ActivationApp.getInstance().onStart();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void install(final InstallEntity entity){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                boolean res=AppInstallUtil.installApp(entity.getPath());
//                Logs.e(TAG,"res:"+res);
//            }
//        }).start();
        Intent intent=new Intent(this,InstallService.class);
        startService(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
