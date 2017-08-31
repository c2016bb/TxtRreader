package com.txtreader;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import com.txtreader.ttspluginlibray.IHostInterface;
import com.txtreader.ttspluginlibray.IPluginInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by User on 2017/8/31.
 */

public class BaiduTtsApp implements IHostInterface{

    private static final String TAG = "cc";
    Context context;
    Resources resources;
    AssetManager  assetManager;
    TtsCombin tts;
    IPluginInterface pluginInterface;
    @Override
    public void initPlugin(String appId,String appKey,String appSecreKey) {
        tts=new TtsCombin(context,mSampleDirPath,pluginInterface);
        tts.setAppArgs(appId,appKey,appSecreKey);
    }

    @Override
    public void speak(String str) {
        Log.d(TAG, "speak: tts--->"+tts);
        tts.startSpeek(str);
    }

    @Override
    public void stop() {
      tts.stop();
    }

    @Override
    public void resume() {
     tts.resume();
    }

    @Override
    public void release() {
      tts.release();
    }

    @Override
    public void setVolume(String value) {
        tts.setVolume(value);
    }

    @Override
    public void setSpeed(String value) {
          tts.setSpeed(value);
    }

    @Override
    public void setPitch(String value) {
           tts.setPitch(value);
    }

    @Override
    public void setSpeaker(String value) {
           tts.setSpeaker(value);
    }

    @Override
    public void setVocodeOptimLevel(String value) {
       tts.setVocodeOptimLevel(value);
    }

    @Override
    public void setStereoVolume(float leftVolume, float rightVolume) {
       tts.setStereoVolume(leftVolume, rightVolume);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context,AssetManager  assetManager,Resources resources,IPluginInterface pluginInterface) {
        this.context = context;
        this.assetManager=assetManager;
        this.resources=resources;
//        this.clazz=clazz;
        initialEnv();
       this.pluginInterface=pluginInterface;
        Log.d(TAG, "this,----> this");
        pluginInterface.getPluginClass(this);
    }

    private String mSampleDirPath;

    public static final String SAMPLE_DIR_NAME = "duTTS";

    public static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";
    public static final String SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male.dat";
    public static final String TEXT_MODEL_NAME = "bd_etts_text.dat";
    public static final String LICENSE_FILE_NAME = "temp_license";
    public static final String ENGLISH_SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female_en.dat";
    public static final String ENGLISH_SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male_en.dat";
    public static final String ENGLISH_TEXT_MODEL_NAME = "bd_etts_text_en.dat";

    private static final int PRINT = 0;
    private static final int UI_CHANGE_INPUT_TEXT_SELECTION = 1;
    private static final int UI_CHANGE_SYNTHES_TEXT_SELECTION = 2;

    private void initialEnv() {
        if (mSampleDirPath == null) {
            String sdcardPath = Environment.getExternalStorageDirectory().toString();
            mSampleDirPath = sdcardPath + "/" + SAMPLE_DIR_NAME;
            Log.i("cc---->","mSampleDirPath--->"+mSampleDirPath);
        }
        makeDir(mSampleDirPath);
        copyFromAssetsToSdcard(false, SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, TEXT_MODEL_NAME, mSampleDirPath + "/" + TEXT_MODEL_NAME);
//        copyFromAssetsToSdcard(false, LICENSE_FILE_NAME, mSampleDirPath + "/" + LICENSE_FILE_NAME);
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_TEXT_MODEL_NAME);
    }
    public String getTTPath(){
        return mSampleDirPath;
    }

    private void makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
            Log.i("cc-->","创建文件---1"+file.toString());
        }
        Log.i("cc-->","创建文件---》2"+file.toString());
    }

    /**
     * 将sample工程需要的资源文件拷贝到SD卡中使用（授权文件为临时授权文件，请注册正式授权）
     *
     * @param isCover 是否覆盖已存在的目标文件
     * @param source
     * @param dest
     */
    private void copyFromAssetsToSdcard(boolean isCover, String source, String dest) {
        File file = new File(dest);
        if (file.exists()){
            return;
        }
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            Log.i("cc","xcdd");
            try {
                is = assetManager.open(source);

                Log.i("cc","is--->"+is.toString());
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
                Log.i("cc","获取Assets值");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.i("cc","获取Assets异常1");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("cc","获取Assets异常2");
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
