package com.txtreader;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.txtreader.ttspluginlibray.IPluginInterface;

import java.lang.reflect.Method;

/**
 * Created by User on 2017/8/31.
 */

public class TtsCombin{
    Context context;
    String mSampleDirPath;
    private SpeechSynthesizer mSpeechSynthesizer;
    IPluginInterface  pluginInterface;

    public TtsCombin(Context context, String mSampleDirPath, IPluginInterface pluginInterface) {
        this.context = context;
        this.mSampleDirPath = mSampleDirPath;
        this.pluginInterface=pluginInterface;
    }

    public void setAppArgs(String appId,String appKey,String appSecreKey){
        this.appId=appId;
        this.appKey=appKey;
        this.appSecreKey=appSecreKey;
        initialTts();
    }

    public Context getContext() {
//        mSampleDirPath = BaiduTtsApp.getTxtReader().getTTPath();

        return context;
    }

    String appId;
    String appKey;
    String appSecreKey;

    private  class  Speech implements SpeechSynthesizerListener{
        @Override
        public void onSynthesizeStart(String s) {
               pluginInterface.onSpeStart(s);
        }

        @Override
        public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {
              pluginInterface.onSynDataArrived(s,bytes,i);
        }

        @Override
        public void onSynthesizeFinish(String s) {
                pluginInterface.onSpeFinish(s);
        }

        @Override
        public void onSpeechStart(String s) {
                 pluginInterface.onSpeStart(s);
        }

        @Override
        public void onSpeechProgressChanged(String s, int i) {
               pluginInterface.onSpeProgressChanged(s,i);
        }

        @Override
        public void onSpeechFinish(String s) {
                pluginInterface.onSpeFinish(s);
        }

        @Override
        public void onError(String s, SpeechError speechError) {
            pluginInterface.onError(s,speechError.toString());
        }
    }

    private static final String TAG = "cc";
    private void initialTts() {
//        this.getActivityMetaData();

        Log.i("cc", "appId--->" + this.appId + "appKey--->" + this.appKey + "appSecreKey--->" + this.appSecreKey);
        this.mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        this.mSpeechSynthesizer.setContext(context);
        this.mSpeechSynthesizer.setSpeechSynthesizerListener(new Speech());
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, this.mSampleDirPath + "/" + "bd_etts_text.dat");
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, this.mSampleDirPath + "/" + "bd_etts_speech_female.dat");
        this.mSpeechSynthesizer.setAppId(this.appId);
        this.mSpeechSynthesizer.setApiKey(this.appKey, this.appSecreKey);
        // 发音人（在线引擎），可用参数为0,1,2,3。。。（服务器端会动态增加，各值含义参考文档，以文档说明为准。0--普通女声，1--普通男声，2--特别男声，3--情感男声。。。）
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "5");
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOCODER_OPTIM_LEVEL, "0");

        // 授权检测接口(只是通过AuthInfo进行检验授权是否成功。)
        // AuthInfo接口用于测试开发者是否成功申请了在线或者离线授权，如果测试授权成功了，可以删除AuthInfo部分的代码（该接口首次验证时比较耗时），不会影响正常使用（合成使用时SDK内部会自动验证授权）
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        AuthInfo authInfo = this.mSpeechSynthesizer.auth(TtsMode.MIX);
        if(authInfo.isSuccess()) {
            Log.e("ReadActivity", "auth success");
        } else {
            String result = authInfo.getTtsError().getDetailMessage();
            Log.e("ReadActivity", "auth failed errorMsg=" + result);
        }

        this.mSpeechSynthesizer.initTts(TtsMode.MIX);
        int result1 = this.mSpeechSynthesizer.loadEnglishModel(this.mSampleDirPath + "/" + "bd_etts_text_en.dat", this.mSampleDirPath + "/" + "bd_etts_speech_female_en.dat");
        Log.i("cc", "result--->" + result1);
//        try{
//        Method method=clazz.getMethod("start",String.class);
//        method.setAccessible(true);
//        method.invoke(clazz,"result--->"+result1);
//        }catch (Exception e){
//            Log.d(TAG, "e--->"+e);
//        }
    }

      //设置音量   5 中级音量  范围[0,9]
    public  void setVolume(String value){
        Log.d(TAG, "setVolume:"+value);
        if(this.mSpeechSynthesizer != null) {
            this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, value);
        }
    }
     //5 中级语速  范围[0,9]
    public void setSpeed(String value){//设置语速
        Log.d(TAG, "setSpeed: "+value);
        if(this.mSpeechSynthesizer != null) {
            this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, value);
        }
    }
     // 5 中调，范围[0- 9]
    public void setPitch(String value){
        Log.d(TAG, "setPitch: "+value);
        if(this.mSpeechSynthesizer != null) {
            this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, value);
        }
    }
   //  0：普通女声， 1：普通男声  2：特别男声  3：情感男声
    public void setSpeaker(String value){
        Log.d(TAG, "setSpeaker: "+value);
        if(this.mSpeechSynthesizer != null) {
            this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, value);
        }
    }


//    该接口用来设置播放器的音量的衰减值，范围为[0.0f-1.0f]，0.0f 时为静音，1.0f 时为最大音量。注
//    意此接口与 PARAM_VOLUME 参数的设置不同，PARAM_VOLUME 设置的是合成时的音量，而
//    该接口设置的是播放时的音量。
     public void setStereoVolume (float leftVolume, float rightVolume){
         Log.d(TAG, "setStereoVolume: ");
         if(this.mSpeechSynthesizer != null) {
             this.mSpeechSynthesizer.setStereoVolume(leftVolume,rightVolume);
         }
     }

//    PARAM_VOCODER_OPTIM_LEVEL
//    （离线合成参数）  合成引擎速度优化等级，取值范围[0, 2]，值越大速度越快（离线引擎）

    public void setVocodeOptimLevel(String value){
        Log.d(TAG, "setVocodeOptimLevel: "+value);
        if(this.mSpeechSynthesizer != null) {
            this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOCODER_OPTIM_LEVEL, value);
        }
    }

     //开始阅读
public  void startSpeek(String str){
    Log.d(TAG, "startSpeek:this.mSpeechSynthesizer----> +"+this.mSpeechSynthesizer);

                if(this.mSpeechSynthesizer != null) {
                int intent1 = this.mSpeechSynthesizer.speak(str);
                    Log.d(TAG, "startSpeek: "+intent1);
                if(intent1 < 0) {
                    pluginInterface.speekResult(false);
                    Log.e("ReadActivity", "error,please look up error code in doc or URL:http://yuyin.baidu.com/docs/tts/122 ");
                } else {
                    pluginInterface.speekResult(true);
//                    this.hideReadSetting();
//                    this.isSpeaking = true;
                }
            }

}

    public void stop() {
     if (mSpeechSynthesizer!=null){
         mSpeechSynthesizer.stop();
     }
    }

    public void resume() {
    if (mSpeechSynthesizer!=null){
        mSpeechSynthesizer.resume();

    }
    }

    public void release() {
        if (mSpeechSynthesizer!=null){
            mSpeechSynthesizer.release();
        }
    }


}
