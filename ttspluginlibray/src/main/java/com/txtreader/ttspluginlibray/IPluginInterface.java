package com.txtreader.ttspluginlibray;

/**
 * Created by User on 2017/8/31.
 */

public interface IPluginInterface {
    void getPluginClass(IHostInterface hostInterface);
    void onSynStart(String s);
    void onSynDataArrived(String s, byte[] bytes, int i);
    void onSynFinish(String s);
    void onSpeStart(String s);
    void onSpeProgressChanged(String s, int i);
    void onSpeFinish(String s);
    void onError(String s, String speechError);
    void speekResult(Boolean result);

}


