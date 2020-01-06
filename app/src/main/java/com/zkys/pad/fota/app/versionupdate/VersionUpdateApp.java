package com.zkys.pad.fota.app.versionupdate;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Message;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.zkys.pad.fota.app.activation.ActivationApp;
import com.zkys.pad.fota.app.versionupdate.bean.PackageBean;
import com.zkys.pad.fota.app.versionupdate.eventbus.DownLoadFinishEntity;
import com.zkys.pad.fota.app.versionupdate.eventbus.DownLoadingEntity;
import com.zkys.pad.fota.app.versionupdate.eventbus.InstallEntity;
import com.zkys.pad.fota.app.versionupdate.eventbus.VersionQueryFailEntity;
import com.zkys.pad.fota.app.versionupdate.handler.VersionUpdateHandler;
import com.zkys.pad.fota.app.versionupdate.topics.VersionUpdateTopics;
import com.zkys.pad.fota.app.versionupdate.ui.DownLoadingActivity;
import com.zkys.pad.fota.base.App;
import com.zkys.pad.fota.base.BaseApp;
import com.zkys.pad.fota.constant.FotaConstant;
import com.zkys.pad.fota.http.BaseBean;
import com.zkys.pad.fota.http.JsonCallback;
import com.zkys.pad.fota.http.UrlUtil;
import com.zkys.pad.fota.util.AppHeleper;
import com.zkys.pad.fota.util.AppInstallUtil;
import com.zkys.pad.fota.util.FileUtil;
import com.zkys.pad.fota.util.Logs;
import com.zkys.pad.fota.util.MobileInfoUtil;
import com.zkys.pad.fota.util.OtaUpgradeUtils;
import com.zkys.pad.fota.util.install.PackageUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;


/**
 * 版本更新检查并下载安装
 */
public class VersionUpdateApp extends BaseApp {

    private static final String TAG = "VersionUpdateApp";

    public static VersionUpdateApp versionUpdateApp = null;

    public static VersionUpdateApp getInstance() {
        if (versionUpdateApp == null) {
            versionUpdateApp = new VersionUpdateApp();
        }
        return versionUpdateApp;
    }

    private VersionUpdateHandler versionUpdateHandler;

    private List<PackageBean> packageList;
    public List<PackageBean> downLoadList;

    public int queryNum = 0;  // 查询接口请求失败次数

    @Override
    public void onStart() {
        super.onStart();
        versionUpdateHandler = new VersionUpdateHandler();
        packageList = new ArrayList<>();
        downLoadList = new ArrayList<>();
        queryVersion();
    }

    public void queryVersion() {
        downLoadList.clear();
        toQueryUi();
        OkGo.<BaseBean<List<PackageBean>>>get(UrlUtil.getPackageList())
                .tag(this)
                .params("code", MobileInfoUtil.getIMEI(App.getContext()))
                .params("hospitalId", ActivationApp.getInstance().getActivationBean().getHospitalId())
                .params("deptId", ActivationApp.getInstance().getActivationBean().getDeptId())
                .execute(new JsonCallback<BaseBean<List<PackageBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<List<PackageBean>>> response) {
                        Logs.d(TAG, "size:" + response.body().getData().size());
                        packageList = response.body().getData();
                        if (packageList.size() <= 0) {
                            EventBus.getDefault().post(new VersionQueryFailEntity("下载列表为空，请联系管理人员或技术人员"));
                            return;
                        }
                        checkFota();
                    }

                    @Override
                    public void onError(Response<BaseBean<List<PackageBean>>> response) {
                        super.onError(response);
                        queryNum++;
                        if (queryNum <= 10) {
                            versionUpdateHandler.sendEmptyMessageDelayed(VersionUpdateTopics.MSG_QUERY_FAIL, 1000 * 60);
                        } else {
                            Logs.e(TAG, "连续请求失败超过10次，停止请求");
                            queryNum = 0;
                            EventBus.getDefault().post(new VersionQueryFailEntity("下载列表查询失败，请联系管理人员或技术人员"));
                        }
                    }
                });
    }

    /**
     * 如果当前launcher小于10，弹出查询界面
     */
    private void toQueryUi() {
        try {
            PackageInfo packageInfo = AppHeleper.getPackageInfo(App.getContext(), FotaConstant.LauncherPckName);
            if (packageInfo == null || packageInfo.versionCode <= 10) {
                Logs.d(TAG, "launcher小于10，弹出查询进度界面");
                AppHeleper.startActivity(DownLoadingActivity.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查当前是否有Fota包需要下载安装
     */
    private void checkFota() {
        for (PackageBean bean : packageList) {
            if ("fota".equals(bean.getAppName())) {
                if (bean.getVersionCode() > AppHeleper.getPackageInfo(App.getContext(), bean.getPkgName()).versionCode) {
                    Logs.d(TAG, "fota需要下载，下载fota");
                    bean.setStatus(0);
                    downLoadList.add(bean);
                    return;
                }
            }
        }
        if (downLoadList.size() > 0) {
            startDownLoad();
            return;
        }
        checkOta();
    }

    /**
     * 检查当前是否有OTA包需要下载安装
     */
    private void checkOta() {
        for (PackageBean bean : packageList) {
            if ("ota".equals(bean.getTag())) {
                if (!Build.DISPLAY.equals(bean.getVersionName())) {
                    bean.setStatus(1);
                    downLoadList.add(bean);
                    return;
                }
            }
        }
        if (downLoadList.size() > 0) {
            startDownLoad();
            return;
        }
        checkPackList();
    }

    /**
     * 检查普通应用列表哪些需要下载安装
     */
    private void checkPackList() {
        downLoadList.clear();
        for (PackageBean packageBean : packageList) {
            // 如果包名未安装过，添加到下载列表
            if (!AppHeleper.isPackageInstalled(App.getContext(), packageBean.getPkgName())) {
                packageBean.setStatus(2);
                downLoadList.add(packageBean);
                continue;
            } else {
                // 当前已安装过，但是版本号不一样，则下载安装
                PackageInfo packageInfo = AppHeleper.getPackageInfo(App.getContext(), packageBean.getPkgName());
                if (packageInfo.versionCode != packageBean.getVersionCode()) {
                    packageBean.setStatus(2);
                    downLoadList.add(packageBean);
                    Logs.d(TAG, "后台版本：" + packageBean.getVersionCode());
                    Logs.d(TAG, "已装版本：" + packageInfo.versionCode);
                    continue;
                }
            }
        }

        if (downLoadList.size() <= 0) {
            Logs.d(TAG, "没有需要下载的应用，无需更新");
            return;
        }

        Logs.d(TAG, "当前需要安装的列表如下：");
        for (PackageBean packageBean : downLoadList) {
            Logs.d(TAG, "name：" + packageBean.getAppName() + "    pckName:" + packageBean.getPkgName() + "    versionCode:" + packageBean.getVersionCode());
        }
        startDownLoad();
    }

    /**
     * 开始下载
     */
    private void startDownLoad() {
        OkDownload.getInstance().setFolder("/sdcard/");
        for (final PackageBean packageBean : downLoadList) {
            GetRequest<File> request = OkGo.<File>get(packageBean.getUrl());
            OkDownload.request(packageBean.getUrl(), request)
                    .save()
                    .register(new DownloadListener(packageBean.getUrl()) {
                        @Override
                        public void onStart(Progress progress) {
                            packageBean.setFlag(1);
                            toDownLoadUi();
                        }

                        @Override
                        public void onProgress(Progress progress) {
                            downLoading(progress, packageBean);
                        }

                        @Override
                        public void onError(Progress progress) {
                            try {
                                Logs.e(TAG, packageBean.getAppName() + "下载失败：" + progress.exception.toString());
                                downLoadError(progress);
                                packageBean.setFlag(3);
                                toDownLoadUi();
                            } catch (Exception e) {
                                e.printStackTrace();
                                packageBean.setFlag(3);
                                toDownLoadUi();
                            }
                        }

                        @Override
                        public void onFinish(File file, Progress progress) {
                            packageBean.setFlag(4);
                            toDownLoadUi();
                            downLoadFinish(file.getPath(), packageBean.getPkgName(), packageBean.getStatus(), packageBean);
                        }

                        @Override
                        public void onRemove(Progress progress) {

                        }
                    }).start();
        }
    }

    /**
     * 如果当前launcher版本号小于10，则默认弹出下载界面
     */
    public void toDownLoadUi() {
        try {
            PackageInfo packageInfo = AppHeleper.getPackageInfo(App.getContext(), FotaConstant.LauncherPckName);
            if (packageInfo == null || packageInfo.versionCode <= 10) {
                Logs.d(TAG, "launcher小于10，弹出下载进度界面");
                EventBus.getDefault().post(new DownLoadingEntity(downLoadList));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载进度
     *
     * @param progress
     * @param bean
     */
    private void downLoading(Progress progress, PackageBean bean) {
        Logs.d(TAG, bean.getAppName() + ":" + (int) (progress.fraction * 100));
        bean.setProgress((int) (progress.fraction * 100));
        bean.setFlag(2);
        toDownLoadUi();
    }

    /**
     * 下载失败
     *
     * @param progress
     */
    private void downLoadError(Progress progress) {
        Message msg = new Message();
        msg.what = VersionUpdateTopics.MSG_DOWNLOAD_ERROR;
        msg.obj = progress;
        versionUpdateHandler.sendMessageDelayed(msg, 1000 * 60);
    }

    /**
     * 下载完成
     */
    private void downLoadFinish(final String path, final String pkgName, int status, PackageBean packageBean) {

        switch (status) {
            case 0:
            case 2:
                new Thread(new Runnable() {
                @Override
                public void run() {
//                    int result=AppInstallUtil.installSlient(App.getContext(),"/sdcard/test.apk",pkgName);
                        int result = PackageUtils.installSilent(App.getContext(), path);
                        if(result==1){
                            installEnd(path,pkgName,5);
                        }else {
                            installEnd(path,pkgName,6);
                        }
                    Logs.e(TAG, "result:" + result);
                }
            }).start();
                break;

            case 1:
                otaUpdate(path, packageBean);
                break;

        }
    }

    /**
     *  安装完成
     * @param path
     * @param pkgName
     */
    private void installEnd(String path,String pkgName,int result){
        for (PackageBean packageBean: VersionUpdateApp.getInstance().downLoadList){
            if(pkgName.equals(packageBean.getPkgName())){
                packageBean.setFlag(result);
                EventBus.getDefault().post(new DownLoadingEntity(VersionUpdateApp.getInstance().downLoadList));
                Logs.e(TAG,"安装完成："+result);
                return;
            }
        }
        if(pkgName.equals(FotaConstant.LauncherPckName)){
            EventBus.getDefault().post(new DownLoadFinishEntity());
        }
        // 删除安装文件
        FileUtil.deleteFile(path);
    }

    /**
     * 系统更新
     *
     * @param filePath
     */
    public void otaUpdate(final String filePath, final PackageBean bean) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OtaUpgradeUtils otaUpgradeUtils = new OtaUpgradeUtils(App.getContext());
                otaUpgradeUtils.upgradeFromOta(filePath, new OtaUpgradeUtils.ProgressListener() {
                    @Override
                    public void onProgress(int progress) {
                        Logs.d(TAG, "固件检校进度：" + progress);
                        sendOtaFlag(7, progress, bean);
                    }

                    @Override
                    public void onVerifyFailed(int errorCode, Object object) {
                        Logs.e(TAG, "检校失败:" + errorCode);
                        sendOtaFlag(8, 0, bean);
                    }

                    @Override
                    public void onCopyProgress(int progress) {
                        Logs.d(TAG, "复制进度：" + progress);
                        sendOtaFlag(9, progress, bean);
                    }

                    @Override
                    public void onCopyFailed(int errorCode, Object object) {
                        Logs.d(TAG, "固件复制失败");
                        sendOtaFlag(10, 0, bean);
                    }
                });
            }
        }).start();
    }

    /**
     * 固件更新结果
     *
     * @param flag
     * @param progress
     */
    private void sendOtaFlag(int flag, int progress, PackageBean bean) {
//        Message msg=new Message();
//        msg.what=VersionUpdateTopics.MSG_OTA_FLAG;
//        Bundle bundle=new Bundle();
//        bundle.putInt("flag",flag);
//        bundle.putInt("progress",progress);
//        msg.setData(bundle);
//        versionUpdateHandler.sendMessage(msg);

        bean.setFlag(flag);
        bean.setProgress(progress);
        toDownLoadUi();

    }
}
