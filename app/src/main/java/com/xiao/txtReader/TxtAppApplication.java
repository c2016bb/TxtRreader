package com.xiao.txtReader;

import android.os.Environment;
import android.util.Log;

import com.txt.readerlibrary.TxtReader;

import org.litepal.LitePalApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.txt.readerlibrary.TxtReader.context;


/**
 * Created by Administrator on 2016/7/8 0008.
 */
public class TxtAppApplication extends LitePalApplication {
    @Override
    public void onCreate() {
        super.onCreate();
//        initialEnv();
        TxtReader.initlize(this);
    }

}
