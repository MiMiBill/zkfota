package com.zkys.pad.fota.http;

import com.zkys.pad.fota.base.App;
import com.zkys.pad.fota.util.AppHeleper;

public class UrlUtil {

    /**
     *  获取默认host
     * @return
     */
    private static String getBaseHost(){
        if(AppHeleper.isApkInDebug(App.getContext())){
            return "http://test.pad.zgzkys.com";
        }
        return "http://pad.zgzkys.com";
    }

    /**
     *  查询平板激活状态
     * @return
     */
    public static String getActivation(){
        return getBaseHost()+"/pad/newList";
    }

    /**
     *  获取下载列表
     * @return
     */
    public static String getPackageList(){
        return getBaseHost()+"/appPackage/list";
    }

}
