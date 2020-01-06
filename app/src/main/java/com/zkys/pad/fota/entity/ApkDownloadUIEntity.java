package com.zkys.pad.fota.entity;

/**
 * apk下载安装列表UI交互
 */
public class ApkDownloadUIEntity extends ApkDownloadEntity {

    private int progress;
    private int state; // -1.下载失败  0 连接中 1.下载中 2.安装中 3.不在安装时间段 4.安装成功 5.安装失败
    private String filePath;

    public String getFilePath() {
        return filePath == null ? "" : filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void clone(ApkDownloadEntity apkDownloadEntity) {
        setPkgName(apkDownloadEntity.getPkgName());
        setVersionCode(apkDownloadEntity.getVersionCode());
        setUrl(apkDownloadEntity.getUrl());
        setAppName(apkDownloadEntity.getAppName());
        setMd5(apkDownloadEntity.getMd5());

    }
}
