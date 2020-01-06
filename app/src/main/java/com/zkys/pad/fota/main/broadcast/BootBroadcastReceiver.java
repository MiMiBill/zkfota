package com.zkys.pad.fota.main.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zkys.pad.fota.main.MainService;
import com.zkys.pad.fota.util.LogUtil;
import com.zkys.pad.fota.util.Logs;

public class BootBroadcastReceiver extends BroadcastReceiver {

    private final String action="android.intent.action.BOOT_COMPLETED";
    private final String TAG="BootBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(action.equals(intent.getAction())){
            Logs.d(TAG,"收到开机广播，启动MainService");
            Intent i=new Intent(context, MainService.class);
            context.startService(i);
        }
    }
}
