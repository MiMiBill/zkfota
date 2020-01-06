package com.zkys.pad.fota.util;


import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.zkys.pad.fota.entity.ActivePadInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 平板激活配置相关信息
 */
public class ActiveUtils {


    public static void setActiveInfo(ActivePadInfo.DataBean entity) {
        Gson gson = new Gson();
        String data = gson.toJson(entity);
        data = Base64.encodeToString(data.getBytes(), Base64.DEFAULT);
        FileOutputStream fileOutputStream = null;
        DataOutputStream ds = null;
        try {
            File file = new File(Constants.FILE_ACTIVE_INFO);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file.toString());
            ds = new DataOutputStream(fileOutputStream);
            ds.writeUTF(data);
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();

                } catch (Exception e) {
                    e.getStackTrace();
                }
                if (ds != null) {
                    try {
                        ds.close();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
            }
        }
    }

    public static ActivePadInfo.DataBean getPadActiveInfo() {
        FileInputStream fis = null;
        DataInputStream dis = null;
        try {
            File file = new File(Constants.FILE_ACTIVE_INFO);
            if (!file.getParentFile().exists()) {
                return null;
            }
            if (!file.exists()) {
                return null;
            }
            fis = new FileInputStream(file.toString());
            dis = new DataInputStream(fis);
            String data = dis.readUTF();
            data = new String(Base64.decode(data, Base64.DEFAULT), "UTF-8");
            Gson gson = new Gson();
            return gson.fromJson(data, ActivePadInfo.DataBean.class);
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();

                } catch (Exception e) {
                    e.getStackTrace();
                }
                if (dis != null) {
                    try {
                        dis.close();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
            }
        }
        return null;
    }

    public static boolean hadActived(Context context) {
        ActivePadInfo.DataBean entity = getPadActiveInfo();
        if (entity != null) {
            if (entity.getActivetion() == 1
                    && !TextUtils.isEmpty(MobileInfoUtil.getICCID(context))
                    && TextUtils.equals(entity.getIccid(), MobileInfoUtil.getICCID(context))//要保证有手机卡
                    && TextUtils.equals(Constants.getHost(), entity.getHost())) {//保证域名一致
                return true;
            }
        }
        return false;
    }

    public static String getPhoneNumber() {
//        if (true) {
//            return "18508429187";
//        }
        String phone = "13800138000";
        ActivePadInfo.DataBean entity = getPadActiveInfo();
        if (entity != null) {
            if (!TextUtils.isEmpty(entity.getSimMobile()) && entity.getSimMobile().length() == 11) {
                return entity.getSimMobile();
            }
        }
        Logger.d("phone:%s", phone);
        return phone;
    }

    public static String getRequestHeader() {
        ActivePadInfo.DataBean entity = getPadActiveInfo();
        return entity == null ? "" : entity.getPad();
    }
}
