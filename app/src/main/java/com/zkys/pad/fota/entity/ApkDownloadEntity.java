package com.zkys.pad.fota.entity;

/**
 * APK安装列表
 */
public class ApkDownloadEntity {

    private String pkgName;
    private int versionCode;
    private String url;
    private String appName;
    private String md5;


    public String getPkgName() {
        return pkgName == null ? "" : pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getUrl() {
        return url == null ? "" : url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppName() {
        return appName == null ? "" : appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getMd5() {
        return md5 == null ? "" : md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
