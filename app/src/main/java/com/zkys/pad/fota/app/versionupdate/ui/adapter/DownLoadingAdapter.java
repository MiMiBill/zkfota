package com.zkys.pad.fota.app.versionupdate.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zkys.pad.fota.R;
import com.zkys.pad.fota.app.versionupdate.bean.PackageBean;

import java.util.List;

public class DownLoadingAdapter extends BaseAdapter {

    private Context context;
    private List<PackageBean> list;

    public DownLoadingAdapter(Context context, List<PackageBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.lv_item_downloading,null);
            holder.tv_appName= (TextView) convertView.findViewById(R.id.tv_appName);
            holder.tv_state= (TextView) convertView.findViewById(R.id.tv_state);
            holder.tv_proess= (TextView) convertView.findViewById(R.id.tv_proess);
            holder.progressBar= (ProgressBar) convertView.findViewById(R.id.progressBar);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        holder.tv_appName.setText(list.get(position).getAppName());
        holder.progressBar.setProgress(list.get(position).getProgress());
        holder.tv_proess.setText(list.get(position).getProgress()+"%");

        switch (list.get(position).getFlag()){
            case 0:
                holder.tv_state.setText("等待下载");
                break;

            case 1:
                holder.tv_state.setText("开始下载");
                break;

            case 2:
                holder.tv_state.setText("下载中");
                break;

            case 3:
                holder.tv_state.setText("下载失败");
                break;

            case 4:
                holder.tv_state.setText("安装中");
                break;

            case 5:
                holder.tv_state.setText("安装成功");
                break;

            case 6:
                holder.tv_state.setText("安装失败");
                break;

            case 7:
                holder.tv_state.setText("检校中");
                break;

            case 8:
                holder.tv_state.setText("检校失败,请联系管理或技术人员");
                break;

            case 9:
                holder.tv_state.setText("复制中");
                break;

            case 10:
                holder.tv_state.setText("复制失败，请联系管理或技术人员");
                break;
        }

        return convertView;
    }

    class ViewHolder{
        TextView tv_appName,tv_state,tv_proess;
        ProgressBar progressBar;
    }
}
