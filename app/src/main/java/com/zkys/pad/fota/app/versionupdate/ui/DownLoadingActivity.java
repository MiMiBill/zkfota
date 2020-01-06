package com.zkys.pad.fota.app.versionupdate.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zkys.pad.fota.R;
import com.zkys.pad.fota.app.versionupdate.VersionUpdateApp;
import com.zkys.pad.fota.app.versionupdate.bean.PackageBean;
import com.zkys.pad.fota.app.versionupdate.eventbus.DownLoadFinishEntity;
import com.zkys.pad.fota.app.versionupdate.eventbus.DownLoadingEntity;
import com.zkys.pad.fota.app.versionupdate.eventbus.VersionQueryFailEntity;
import com.zkys.pad.fota.app.versionupdate.ui.adapter.DownLoadingAdapter;
import com.zkys.pad.fota.base.App;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 查询下载列表及下载进度界面
 */
public class DownLoadingActivity extends Activity {

    @BindView(R.id.tv_query)
    TextView tvQuery;
    @BindView(R.id.listview)
    ListView listview;

    private DownLoadingAdapter adapter;
    private List<PackageBean> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloading);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initData();
        registerBroadCast();
    }

    private void initData(){
        list=new ArrayList<>();
        list.addAll(VersionUpdateApp.getInstance().downLoadList);
        adapter=new DownLoadingAdapter(App.getContext(),list);
        listview.setAdapter(adapter);
        listview.setDivider(null);
    }

    /**
     *   安装失败消息
     * @param entity
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void queryFail(VersionQueryFailEntity entity) {
        tvQuery.setText(entity.getMsg());
    }

    /**
     *  安装进度及状态
     * @param entity
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void downLoading(DownLoadingEntity entity){
        list.clear();
        list.addAll(entity.getList());
        adapter.notifyDataSetChanged();
        listview.setVisibility(View.VISIBLE);
        tvQuery.setVisibility(View.GONE);
    }

    /**
     *  关闭界面
     * @param entity
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void finish(DownLoadFinishEntity entity){
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(receiver);
    }

    private void registerBroadCast(){
        IntentFilter filter=new IntentFilter("installEnd");
        registerReceiver(receiver,filter);
    }

    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("installEnd")){
                list.clear();
                list.addAll(VersionUpdateApp.getInstance().downLoadList);
                adapter.notifyDataSetChanged();
                listview.setVisibility(View.VISIBLE);
                tvQuery.setVisibility(View.GONE);
            }
        }
    };
}
