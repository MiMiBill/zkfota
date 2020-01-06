package com.zkys.pad.fota.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zkys.pad.fota.base.Global;
import com.zkys.pad.fota.entity.ActivePadInfo;
import com.zkys.pad.fota.entity.ApkDownloadEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 网络请求工具类
 */
public class HttpUtils {

    private static final HttpUtils ourInstance = new HttpUtils();
    public static HttpUtils getInstance() {
        return ourInstance;
    }

    private HttpUtils() {
    }


    Disposable disposableRetryPackageList;

    public void getPackageList() {
        ActivePadInfo.DataBean dataBean = ActiveUtils.getPadActiveInfo();

        //获取下载APK列表
        OkGo.<String>get(Constants.getPackageList())
                .tag(this)
                .params("code", MobileInfoUtil.getIMEI(Global.getContext()))
                .params("hospitalId", dataBean.getHospitalId())
                .params("deptId", dataBean.getDeptId())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Gson gson = new Gson();
                        String body = response.body();
//                        JsonObject
                        LogUtil.d(body);
                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            if (jsonObject.optInt("code")==200) {
                                List<ApkDownloadEntity> list=gson.fromJson(jsonObject.getString("data"),new TypeToken<List<ApkDownloadEntity>>(){}.getType());
                                if (list != null && list.size() > 0) {
                                    PackageManageUtils.getInstance().downloadPckList(list);
                                    return;
                                }
                                //出现错误,启动重试机制
                                retryGetPackageList();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            retryGetPackageList();
                        }


                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        //网络异常
                        retryGetPackageList();
                    }

                });

    }

    /**
     * 出现任何错误一分钟之后重试
     */
    private void retryGetPackageList() {
        RxUtil.closeDisposable(disposableRetryPackageList);
        disposableRetryPackageList = Observable.timer(1, TimeUnit.MINUTES)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        getPackageList();
                    }
                });

    }


}
