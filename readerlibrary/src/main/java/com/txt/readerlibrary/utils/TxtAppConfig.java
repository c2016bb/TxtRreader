package com.txt.readerlibrary.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by User on 2017/7/20.
 */

public class TxtAppConfig {
    public static String getDownLoadBookPath(Context context) {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+context.getCacheDir().getAbsolutePath()+"/BookDownLoad/";
//       String path= "/storage/emulated/0/cache/txtreader/DownLoad/book";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }
    public static  String YUYINPATH="yupinPlugin";

}
