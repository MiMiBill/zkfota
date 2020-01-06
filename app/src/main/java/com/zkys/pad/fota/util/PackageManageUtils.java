package com.zkys.pad.fota.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.zkys.pad.fota.BuildConfig;
import com.zkys.pad.fota.base.Global;
import com.zkys.pad.fota.entity.ApkDownloadEntity;
import com.zkys.pad.fota.entity.ApkDownloadUIEntity;
import com.zkys.pad.fota.util.install.PackageUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * App包管理工具类
 *
 */
public class PackageManageUtils {
    private static final PackageManageUtils ourInstance = new PackageManageUtils();

    public static PackageManageUtils getInstance() {
        return ourInstance;
    }

    private PackageManageUtils() {
    }


    public Map<String, ApkDownloadUIEntity> downloadApkMap = new HashMap<>();


    /**
     * 开始下载APK
     */
    public void downloadPckList(List<ApkDownloadEntity> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        boolean isUpdateSelf = false;
        int index = 0;
        //检查是否需要更新fota
        for (int i = 0; i < list.size(); i++) {
            ApkDownloadEntity apkEntity = list.get(i);
            if (TextUtils.isEmpty(apkEntity.getPkgName())) {
                continue;
            }
             if (TextUtils.equals(apkEntity.getPkgName(), Global.getContext().getPackageName()) && apkEntity.getVersionCode() != BuildConfig.VERSION_CODE
                  ) {
//            if (TextUtils.equals(apkEntity.getPkgName(), Global.getContext().getPackageName())
//                    && TextUtils.equals("1.0", BuildConfig.VERSION_NAME)) {
                isUpdateSelf = true;
                index = i;
                break;
            }
        }

        if (isUpdateSelf) {//开始下载安装fota
            downLoadFota(list.get(index));
        } else {

        }


    }

    /**
     * 安装fota
     * @param apkDownloadEntity
     */
    private void downLoadFota(ApkDownloadEntity apkDownloadEntity) {
        String url = apkDownloadEntity.getUrl();
        if(TextUtils.isEmpty(url)||url.equals("")){
            return;
        }
        FileUtil.deleteFile(FileUtil.SDCARD_FOLDER + "fota.apk");

        ApkDownloadUIEntity apkDownloadUIEntity = new ApkDownloadUIEntity();
        apkDownloadUIEntity.clone(apkDownloadEntity);
        apkDownloadUIEntity.setFilePath("fota.apk");

        downloadApkMap.put(apkDownloadEntity.getUrl(), apkDownloadUIEntity);
        OkGo.<File>get(apkDownloadEntity.getUrl()).tag(this)
                .execute(new FileCallback(FileUtil.SDCARD_FOLDER, "fota.apk") {

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        LogUtil.d("fileName:%s", progress.fileName);
                        ApkDownloadUIEntity entity = downloadApkMap.get(progress.fileName);
                        if (entity!=null) {
                            int fraction = (int) (progress.fraction * 100);
                            entity.setState(1);
                            entity.setProgress(fraction);
                        }
                    }

                    @SuppressLint("CheckResult")
                    @Override
                    public void onSuccess(Response<File> response) {
                        File file = response.body();
                        ApkDownloadUIEntity entity = downloadApkMap.get(file.getName());
                        if (entity != null) {
                            entity.setState(2);
                        }
                        Observable.just(file.getAbsolutePath())
                                .observeOn(Schedulers.io())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String apkPath) throws Exception {
                                        int resultCode = PackageUtils.installSilent(Global.getContext(), apkPath);
                                        if (resultCode != PackageUtils.INSTALL_SUCCEEDED) {
                                            LogUtil.d("升级失败");
                                        }
                                    }
                                });
                    }
                });


    }


}
