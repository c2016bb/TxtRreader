package com.txtreader.ttspluginlibray;

/**
 * Created by User on 2017/8/31.
 */

public interface IHostInterface {
      void speak(String str);
      void stop();
      void resume();
      void  release();
      void  initPlugin(String appId,String appKey,String appSecreKey);
      void setVolume(String value);//音量 标准 5  [0,9]
      void setSpeed(String value);//语速  标准 5  [0,9]
      void setPitch(String value);//音调  标准  5 [0,9]
      void setSpeaker(String value);//发音人 0：普通女声， 1：普通男声  2：特别男声  3：情感男声
      void setVocodeOptimLevel(String value);// （离线合成参数）  合成引擎速度优化等级，取值范围[0, 2]，值越大速度越快（离线引擎）
      void setStereoVolume (float leftVolume, float rightVolume);//    该接口用来设置播放器的音量的衰减值，范围为[0.0f-1.0f]，0.0f 时为静音，1.0f 时为最大音量。


}
