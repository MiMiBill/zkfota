package com.zkys.pad.fota.app.activation.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.Response;
import com.zkys.pad.fota.BuildConfig;
import com.zkys.pad.fota.R;
import com.zkys.pad.fota.app.activation.eventbus.ActitationFinishEntity;
import com.zkys.pad.fota.entity.ActivePadInfo;
import com.zkys.pad.fota.util.ActiveUtils;
import com.zkys.pad.fota.util.Constants;
import com.zkys.pad.fota.util.HttpUtils;
import com.zkys.pad.fota.util.LogUtil;
import com.zkys.pad.fota.util.MobileInfoUtil;
import com.zkys.pad.fota.util.QrCodeUtils;
import com.zkys.pad.fota.util.RxUtil;
import com.zkys.pad.fota.widght.FancyToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 激活页面
 */
public class ActivateActivity extends Activity {

    @BindView(R.id.hide_btn)
    Button hideBtn;
    @BindView(R.id.tv_active_result)
    TextView tvActiveResult;
    @BindView(R.id.tv_tip_maintain)
    TextView tvTipMaintain;
    @BindView(R.id.tv_active_result_success)
    TextView tvActiveResultSuccess;
    @BindView(R.id.tv_active_result_fail)
    TextView tvActiveResultFail;
    @BindView(R.id.tv_tip_maintain_zk)
    TextView tvTipMaintainZk;
    @BindView(R.id.tvIccid)
    TextView tvIccid;
    @BindView(R.id.tvImei)
    TextView tvImei;
    @BindView(R.id.tvVersionName)
    TextView tvVersionName;
    @BindView(R.id.iv_active_pad_qrcode)
    ImageView ivActivePadQrcode;
    @BindView(R.id.bt_next)
    Button btNext;

    //激活查询轮询
    private Disposable disposableCheckActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initData();
    }

    private void initData() {
        tvVersionName.setText(String.format("版本号:%s", BuildConfig.VERSION_NAME));
        tvIccid.setText(String.format("iccid:%s", MobileInfoUtil.getICCID(getContext())));
        tvImei.setText(String.format("imei:%s", MobileInfoUtil.getIMEI(getContext())));
        ivActivePadQrcode.setImageBitmap(QrCodeUtils.generateBitmap(MobileInfoUtil.getICCID(this) + ","
                + MobileInfoUtil.getIMEI(this)+","+android.os.Build.MODEL+","+android.os.Build.BRAND, 232, 232));

//        checkHadActiveDi();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void finishByEventbus(ActitationFinishEntity entity){
        this.finish();
    }

    /**
     * 查询是否激活一分钟一次
     */
    private void checkHadActiveDi() {
        checkHadActive();
        RxUtil.closeDisposable(disposableCheckActive);
        disposableCheckActive = Observable.interval(1, TimeUnit.MINUTES)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        checkHadActive();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e(throwable, throwable.getMessage());
                    }
                });
    }

    /**
     * 查询是否激活
     */
    private void checkHadActive() {
        //重新请求服务器
        String iccid = MobileInfoUtil.getICCID(getContext());
        if (TextUtils.isEmpty(iccid)) {
            FancyToast.makeText("非法设备！");
            return;
        }
        if (ActiveUtils.hadActived(getContext())) {
            //已经激活成功，进入主页面
            RxUtil.closeDisposable(disposableCheckActive);
            activateSuccessView();
            activateSuccess();
        } else {
            bindingDevice2(MobileInfoUtil.getIMEI(getContext()));
        }
    }

    /**
     * 查询是否激活
     *
     * @param imei
     */
    private void bindingDevice2(String imei) {
        OkGo.<String>post(Constants.getQueryPadActiveState())
                .tag(this)
                .params("code", imei)
//                                .params("code", "padImei") //测试使用
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Gson gson = new Gson();
                        ActivePadInfo activePadInfo = gson.fromJson(response.body(), ActivePadInfo.class);
                        List<ActivePadInfo.DataBean> data = activePadInfo.getData();
                        if (data != null && data.size() > 0) {

                            int activetion = activePadInfo.getData().get(0).getActivetion();
                            if (activetion == 0) {
                                //未激活
                                activeFailView();
                            } else if (TextUtils.isEmpty(activePadInfo.getData().get(0).getSimMobile()) || activePadInfo.getData().get(0).getSimMobile().length() != 11) {
                                FancyToast.makeText("手机号码为空");
                            } else if (activetion == 1) {
                                //已激活
                                ActivePadInfo.DataBean dataBean = activePadInfo.getData().get(0);
                                dataBean.setHost(Constants.getHost());
                                ActiveUtils.setActiveInfo(dataBean);
                                activateSuccessView();
                                //添加统一header
                                HttpHeaders headers = new HttpHeaders();
                                headers.put("PAD", activePadInfo.getData().get(0).getPad());
                                OkGo.getInstance().addCommonHeaders(headers);
                                activateSuccess();
                            }
                        } else {
                            //未激活
                            activeFailView();
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        //网络异常
                        activeFailView();
                    }
                });
    }


    /**
     * 激活失败
     */
    private void activeFailView() {
        tvActiveResult.setVisibility(View.INVISIBLE);
        tvTipMaintain.setVisibility(View.INVISIBLE);
        tvActiveResultSuccess.setVisibility(View.INVISIBLE);
        tvActiveResultFail.setVisibility(View.INVISIBLE);
        tvTipMaintainZk.setVisibility(View.VISIBLE);
        ivActivePadQrcode.setVisibility(View.VISIBLE);
        tvTipMaintainZk.setText("稍后自动查询是否激活");
    }

    /**
     * 激活成功
     */
    private void activateSuccessView() {
        tvActiveResult.setVisibility(View.INVISIBLE);
        tvTipMaintain.setVisibility(View.INVISIBLE);
        tvActiveResultSuccess.setVisibility(View.VISIBLE);
        tvActiveResultFail.setVisibility(View.INVISIBLE);
        tvTipMaintainZk.setVisibility(View.VISIBLE);
        ivActivePadQrcode.setVisibility(View.INVISIBLE);
        tvTipMaintainZk.setText("激活成功,正在初始化平板");
    }


    /**
     * 激活成功要做的事情
     */
    private void activateSuccess() {

        HttpUtils.getInstance().getPackageList();

       /* ActivePadInfo.DataBean dataBean = ActiveUtils.getPadActiveInfo();
        //获取下载APK列表
        OkGo.<String>get(Constants.getPackageList())
                .tag(this)
                .params("code", MobileInfoUtil.getIMEI(getContext()))
                .params("hospitalId", dataBean.getHospitalId())
                .params("deptId", dataBean.getDeptId())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Gson gson = new Gson();
                        String body = response.body();
                        LogUtil.d(body);

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        //网络异常
                        activeFailView();
                    }
                });*/


    }

    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
