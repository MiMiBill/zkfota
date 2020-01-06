package com.zkys.pad.fota.app.versionupdate.bean;

public class PackageBean {


    /**
     * deviceType : 1
     * appType : 1
     * pkgName : com.zkys.pad.launcher.fota
     * pageSize : 20
     * remark : fota
     * id : 4
     * versionName : 1.1
     * pageNum : 1
     * url : http://qiniuapp.zgzkys.com/fota2.apk
     * versionCode : 2
     */

    private int deviceType;
    private int appType;
    private String pkgName;
    private int pageSize;
    private String remark;
    private int id;
    private String versionName;
    private int pageNum;
    private String url;
    private int versionCode;
    private int progress;
    private int flag; // 0 等待下载 1 开始下载 2 下载中 3 下载失败 4 下载成功 5 安装成功 6 安装失败 7 检校进度
    private String tag;
    private String appName;
    private int status; // 0 fota 1 ota 2 普通APP

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
}
