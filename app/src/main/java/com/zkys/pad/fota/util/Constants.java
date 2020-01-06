package com.zkys.pad.fota.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;

import java.io.File;

public class Constants {

    public static final String FILE_ACTIVE_INFO = Environment.getExternalStorageDirectory().toString() + File.separator + "zkys" + File.separator + "active.info";


    //    public static final String HOST_DEFAULT = "http://pad.zgzkys.com";
    public static final String HOST_DEFAULT = "http://test.pad.zgzkys.com";


    public static String getHost() {
        String host = SPUtil.getString(SPUtil.HOST);
        if (TextUtils.isEmpty(host)) {
            return HOST_DEFAULT;
        }
        return host;
    }

    /**
     * 平板激活状态
     *
     * @return
     */
    public static String getQueryPadActiveState() {
        return Constants.getHost() + "/pad/newList";
    }

    /**
     * 获取安装下载apk列表
     * @return
     */
    public static String getPackageList() {
        return Constants.getHost() + "/appPackage/list";
    }





    public static final boolean checkHidePwd(Context context, String pwd) {
        String cryp = MobileInfoUtil.getICCID(context) + "," + MobileInfoUtil.getIMEI(context);
        String base64 = Base64.encodeToString(cryp.getBytes(), Base64.DEFAULT);
        String date = FormatUtils.FormatDateUtil.formatDate("yyyyMMdd");
        String key = String.valueOf((base64 + date).hashCode());
        String keyPwd = key.substring(key.length() - 6);
        LogUtil.d("keyPwd: %s", keyPwd);
        return TextUtils.equals(keyPwd, pwd);

    }
}
