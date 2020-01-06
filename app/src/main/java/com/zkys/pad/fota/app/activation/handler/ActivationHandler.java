package com.zkys.pad.fota.app.activation.handler;

import android.os.Handler;
import android.os.Message;

import com.zkys.pad.fota.app.activation.ActivationApp;
import com.zkys.pad.fota.app.activation.topics.ActivationTopics;
import com.zkys.pad.fota.util.Logs;

public class ActivationHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case ActivationTopics.MSG_QUERY_ACTIVATION_FAIL:
                Logs.e(ActivationApp.TAG,"重新查询激活信息");
                ActivationApp.getInstance().queryIsActivation();
                break;
        }
    }
}
