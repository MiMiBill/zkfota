package com.zkys.pad.fota.app.versionupdate.eventbus;

public class InstallEntity {

    private String path;

    public InstallEntity(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
