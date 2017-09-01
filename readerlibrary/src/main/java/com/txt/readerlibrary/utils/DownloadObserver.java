package com.txt.readerlibrary.utils;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;

/**
 * Created by User on 2017/9/1.
 */

public class DownloadObserver extends ContentObserver {

    private Handler mHandler;
    private Context mContext;
    private int progress;
    private DownloadManager mDownloadManager;
    private DownloadManager.Query query;
    private Cursor cursor;
    private Boolean isDownLoading=false;
    private long downId=-1;

    public void setDownId(long downId) {
        this.downId = downId;
        query = new DownloadManager.Query().setFilterById(downId);
    }

    public void setDownLoading(Boolean downLoading) {
        isDownLoading = downLoading;
    }

    @SuppressLint("NewApi")
    public DownloadObserver(Handler handler, Context context,DownloadManager mDownloadManager) {
        super(handler);
        this.mHandler = handler;
        this.mContext = context;
        this.mDownloadManager = mDownloadManager;
    }

    private static final String TAG = "cc";
    boolean downloading = true;
    @SuppressLint("NewApi")
    @Override
    public void onChange(boolean selfChange) {
        // 每当/data/data/com.android.providers.download/database/database.db变化后，触发onCHANGE，开始具体查询
        super.onChange(selfChange);
        Log.d(TAG, "onChange:---> ");
        //
        if (query==null||!downloading){
            return;
        }

        if (cursor==null) {
            cursor = mDownloadManager.query(query);
        }

          if (cursor!=null&&cursor.moveToFirst()) {
//              cursor.moveToFirst();
              int bytes_downloaded = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
              int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
              int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
              progress = (int) ((bytes_downloaded * 100) / bytes_total);
              Log.d(TAG, "bytes_downloaded------>" + bytes_downloaded);
              Log.d(TAG, "bytes_total---->" + bytes_total);
              Log.d(TAG, "progress--->" + progress);
//            mHandler.sendEmptyMessageDelayed(progress, 100);
              if (status == DownloadManager.STATUS_SUCCESSFUL) {
                  cursor.close();
                  downloading = false;
              }
          }
        }
    }
