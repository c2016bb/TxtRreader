package com.txt.readerlibrary.utils;

import android.util.Log;


/**
 * Created by CC on 2016/6/4.
 */

public class LogUtils {
    private static final String TAG = "-CC-";

    public static void D(String msg) {
        if (BuildConfig.DEBUG)
            Log.d(TAG, String.valueOf(msg));
    }

    public static void V(String msg) {
        if (BuildConfig.DEBUG)
            Log.v(TAG, String.valueOf(msg));
    }

    public static void I(String msg) {
        if (BuildConfig.DEBUG)
            Log.i(TAG, String.valueOf(msg));
    }
    public static void i(Object str, Object msg) {
        if (BuildConfig.DEBUG)
            Log.i(TAG+""+str+" ", "  "+msg);
    }

    public static void E(String msg) {
        if (BuildConfig.DEBUG)
            Log.e(TAG, String.valueOf(msg));
    }



}
