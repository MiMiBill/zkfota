package com.zkys.pad.fota.util;

import android.os.Environment;

import java.io.File;

public class FileUtil {

    /**
     * sdcard
     */
    public static final String SDCARD_FOLDER = Environment.getExternalStorageDirectory().toString() + File.separator;

    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }
}
