package com.txt.readerlibrary.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URLDecoder;

/**
 * 下载图片
 */

public class DownLoadFile {
    public static long downLoadId;
    private static Context context;

   public static String filePath=TxtAppConfig.YUYINPATH;

    public  interface   FilePathCallBack{

        void getFilePath(String path);
        void downloadProgress(int progress);
    }

    public static void setContext(Context context) {
        DownLoadFile.context = context;
    }

    private static DownLoadBroadcastReceiver mBroadcastReceiver;

    public static String getUrlName(String url){
        try {
            String output = URLDecoder.decode(url.substring(url.lastIndexOf("/")+1,url.lastIndexOf(".")), "UTF-8");
            TxtLogUtils.D("output--->"+output);
            return  output;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void cancelDownLoad(){
      downloadManager.remove(downLoadId);
    }

    public  static  String fileName;
  static Handler  handler=new Handler(){
       @Override
       public void handleMessage(Message msg) {
            mCallBack.downloadProgress((int)msg.what);
       }
   };
    public  static FilePathCallBack mCallBack;
  public static DownloadManager downloadManager;
    public  static DownloadManager.Request request;

//    public static  DownloadObserver observer;
    public static void downloadFile(Context context, String url, FilePathCallBack callBack) {
        setContext(context);
        setUrl(url);
       mCallBack=callBack;
        filePath=context.getCacheDir().getAbsolutePath()+"/"+filePath;
     fileName=url.substring(url.lastIndexOf("/")+1);

//      File  file=searchFileByFilePath(name+".txt",new File(filePath),url.substring(url.lastIndexOf(".")+1));
//        if (file!=null){
//            TxtLogUtils.D("存在file");
//        callBack.getFilePath(file.getAbsolutePath());
//            return;
//        }
        try {
            downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(url);
            request = new DownloadManager.Request(uri);
            // 通知栏中将出现的内容
//            request.setTitle("百度语音下载");
//            request.setDescription("下载一个大文件");
            // 下载过程和下载完成后通知栏有通知消息。
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE | DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

//            observer=new DownloadObserver(handler,context,downloadManager);

//            context.getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), true, observer);

            //可使用部分
            // 仅允许在WIFI连接情况下下载
//            request.setAllowedNetworkTypes(Request.NETWORK_WIFI);
//            /**设置漫游状态下是否可以下载*/
//            request.setAllowedOverRoaming(false);

//            // 通知栏中将出现的内容
//            request.setTitle("我的下载");
//            request.setDescription("下载一个大文件");
//            // 下载过程和下载完成后通知栏有通知消息。
//            request.setNotificationVisibility(Request.VISIBILITY_VISIBLE | Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//
//            // 此处可以由开发者自己指定一个文件存放下载文件。
//            // 如果不指定则Android将使用系统默认的
//            // request.setDestinationUri(Uri.fromFile(new File("")));
//
//            // 默认的Android系统下载存储目录
//            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "my.apk");


            //设置文件存放路径
            request.setDestinationInExternalPublicDir(filePath, url.substring(url.lastIndexOf("/")+1));

            downLoadId = downloadManager.enqueue(request);
//            observer.setDownId(downLoadId);

            mBroadcastReceiver = new DownLoadBroadcastReceiver(callBack);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            context.registerReceiver(mBroadcastReceiver, intentFilter);//注册来电监听
//            LogUtils.D("reference" + reference);
//            String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void unregister() {
        if (mBroadcastReceiver != null) {
            context.unregisterReceiver(mBroadcastReceiver);

            mBroadcastReceiver=null;
        }
    }

    private static final String TAG = "cc";
    private static class DownLoadBroadcastReceiver extends BroadcastReceiver {
     private  FilePathCallBack callBack;
        public DownLoadBroadcastReceiver(FilePathCallBack callBack) {
            this.callBack = callBack;
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: "+intent);
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (completeDownloadId == downLoadId) {//下载完成
                    String filepath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+filePath+"/"+fileName;
//                  observer.setDownLoading(false);
                    Log.d(TAG, "filePath---->"+filepath);
                    File file=new File(filepath);
                    if (file.exists()) {
                        callBack.getFilePath(file.getAbsolutePath());
                    }
                }
                unregister();
            }
        }
    }

    static File mfile;
  public   static String url;

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        DownLoadFile.url = url;
    }

    public String mAbsoluteFilePath;




//       s 代表后缀名
    //filename  表示文件的名称 包含后缀名
    //mFile 代表名称在的路径
    //根据文件后缀名查找文件是否存在
    public static File searchFileByFilePath(String fileName, File mFile, final String s) {
        TxtLogUtils.D("fileName--->" + fileName);
        TxtLogUtils.D("mFile.getAbsolutePath()--->"+mFile.getAbsolutePath());
        TxtLogUtils.D("s--->"+s);

//    /判断SD卡是否存在
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            if (!mFile.exists()){
                return null;
            }
            File[] files = mFile.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String filename) {
                    return filename.endsWith(s);//根据后缀名
                }
            });

            if (files.length > 0) {
                for (File file : files) {
                    TxtLogUtils.D("files--->" + file.getName());
                    if (fileName.equals(file.getName())) {
                        TxtLogUtils.D("找到文件");
                        return file;
                    }
                }
            }
        }
        ;
        return null;
    }

}

