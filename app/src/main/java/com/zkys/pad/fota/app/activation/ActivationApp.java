package com.zkys.pad.fota.app.activation;

import android.content.Intent;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.zkys.pad.fota.app.activation.eventbus.ActitationFinishEntity;
import com.zkys.pad.fota.app.activation.handler.ActivationHandler;
import com.zkys.pad.fota.app.activation.topics.ActivationTopics;
import com.zkys.pad.fota.app.activation.ui.ActivateActivity;
import com.zkys.pad.fota.base.App;
import com.zkys.pad.fota.base.BaseApp;
import com.zkys.pad.fota.app.activation.bean.ActivationBean;
import com.zkys.pad.fota.constant.FotaTopics;
import com.zkys.pad.fota.http.BaseBean;
import com.zkys.pad.fota.http.JsonCallback;
import com.zkys.pad.fota.http.UrlUtil;
import com.zkys.pad.fota.util.Logs;
import com.zkys.pad.fota.util.MobileInfoUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 *   激活
 */
public class ActivationApp extends BaseApp {

    public final static String TAG="ActivationApp";

    public static ActivationApp activationApp=null;

    public static ActivationApp getInstance(){
        if(activationApp==null){
            activationApp=new ActivationApp();
        }
        return activationApp;
    }

    private ActivationHandler activationHandler;
    public ActivationBean activationBean;

    @Override
    public void onStart() {
        super.onStart();
        queryIsActivation();
        activationHandler=new ActivationHandler();
    }

    /**
     *  查询当前是否已经激活
     */
    public void queryIsActivation(){
        OkGo.getInstance().cancelTag(UrlUtil.getActivation());
        OkGo.<BaseBean<List<ActivationBean>>>post(UrlUtil.getActivation())
                .params("code",MobileInfoUtil.getIMEI(App.getContext()))
                .tag(UrlUtil.getActivation())
                .execute(new JsonCallback<BaseBean<List<ActivationBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<List<ActivationBean>>> response) {
                        try {
                            querySuccess(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Logs.e(TAG,"激活查询carsh："+e.getMessage());
                            reQuery();
                        }
                    }

                    @Override
                    public void onError(Response<BaseBean<List<ActivationBean>>> response) {
                        super.onError(response);
                        Logs.i(TAG,"激活信息查询失败，1分钟后重新查询");
                        toActivity();
                        reQuery();
                    }
                });
    }

    /**
     *  重新查询激活信息
     */
    private void reQuery(){


        activationHandler.sendEmptyMessageDelayed(ActivationTopics.MSG_QUERY_ACTIVATION_FAIL,1000*60);
    }

    /**
     *  激活信息查询成功
     * @param response
     * @throws Exception
     */
    private void querySuccess(Response<BaseBean<List<ActivationBean>>> response) throws Exception{
        if(response.body().getData()==null||response.body().getData().size()<=0){
            Logs.i(TAG,"激活信息查询成功，但data为空，重新查询");
            toActivity();
            reQuery();
            return;
        }
        ActivationBean bean=response.body().getData().get(0);
        if(bean.getActivetion()!=1){
            Logs.i(TAG,"激活信息查询成功，但Activetion不是激活状态，重新查询");
            toActivity();
            reQuery();
            return;
        }
        EventBus.getDefault().post(new ActitationFinishEntity());
        activationBean=bean;
        handler.sendEmptyMessage(FotaTopics.MSG_ACTIVATION_SUCCESS);
    }

    private void toActivity(){
        Intent intent=new Intent(App.getContext(), ActivateActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        App.getContext().startActivity(intent);
    }

    /**
     *   返回所有激活信息
     * @return
     */
    public ActivationBean getActivationBean() {
        return activationBean;
    }
}
