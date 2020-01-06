package com.zkys.pad.fota.base;

import android.os.Handler;
import android.os.Message;

import com.zkys.pad.fota.app.versionupdate.VersionUpdateApp;
import com.zkys.pad.fota.constant.FotaTopics;
import com.zkys.pad.fota.util.Logs;

public class BaseApp {

    private static final String TAG="BaseApp";

    public void onStart(){};

    protected void onStop(){};

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case FotaTopics.MSG_ACTIVATION_SUCCESS:
                    Logs.d(TAG,"激活成功,检查下载更新");
                    VersionUpdateApp.getInstance().onStart();
                    break;
            }
        }
    };

}
