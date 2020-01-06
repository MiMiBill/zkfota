package com.zkys.pad.fota.app.versionupdate;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.zkys.pad.fota.util.AppInstallUtil;
import com.zkys.pad.fota.util.Logs;

public class InstallService extends IntentService {

    private static final String TAG="InstallService";

    public InstallService() {
        super("InstallService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        boolean res=AppInstallUtil.installApp("/sdcard/tim.apk");
        Logs.e(TAG,"res:"+res);
    }
}
