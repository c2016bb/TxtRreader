package com.txt.readerlibrary.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by User on 2017/7/20.
 */

public class AppConfig {
    public static String getDownLoadBookPath() {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/txtreader/DownLoad/";
//       String path= "/storage/emulated/0/cache/txtreader/DownLoad/book";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }
}
