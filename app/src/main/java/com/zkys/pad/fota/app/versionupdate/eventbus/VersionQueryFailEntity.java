package com.zkys.pad.fota.app.versionupdate.eventbus;

public class VersionQueryFailEntity {

    public String msg;

    public VersionQueryFailEntity(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
