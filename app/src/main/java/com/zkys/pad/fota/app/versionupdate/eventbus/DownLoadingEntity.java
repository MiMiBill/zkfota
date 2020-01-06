package com.zkys.pad.fota.app.versionupdate.eventbus;

import com.zkys.pad.fota.app.versionupdate.bean.PackageBean;

import java.util.List;

public class DownLoadingEntity {

    private List<PackageBean> list;

    public DownLoadingEntity(List<PackageBean> list) {
        this.list = list;
    }

    public List<PackageBean> getList() {
        return list;
    }

    public void setList(List<PackageBean> list) {
        this.list = list;
    }
}
