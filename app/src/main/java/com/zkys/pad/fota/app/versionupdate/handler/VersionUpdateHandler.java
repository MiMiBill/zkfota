package com.zkys.pad.fota.app.versionupdate.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.lzy.okgo.model.Progress;
import com.lzy.okserver.OkDownload;
import com.zkys.pad.fota.app.versionupdate.VersionUpdateApp;
import com.zkys.pad.fota.app.versionupdate.topics.VersionUpdateTopics;

public class VersionUpdateHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case VersionUpdateTopics.MSG_DOWNLOAD_ERROR:
                // 重新下载任务
                Progress progress = (Progress) msg.obj;
                OkDownload.restore(progress);
                break;

            case VersionUpdateTopics.MSG_QUERY_FAIL:
                // 请求查询接口失败，重新查询
                VersionUpdateApp.getInstance().queryVersion();
                break;

            case VersionUpdateTopics.MSG_OTA_FLAG:
                Bundle bundle=msg.getData();
                
                break;
        }
    }
}
