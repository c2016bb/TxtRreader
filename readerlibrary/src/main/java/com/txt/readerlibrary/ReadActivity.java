//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.txt.readerlibrary;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.SQLException;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.txt.readerlibrary.Config;
import com.txt.readerlibrary.FileChooserActivity;
import com.txt.readerlibrary.MarkActivity;
import com.txt.readerlibrary.TxtReader;
import com.txt.readerlibrary.R.anim;
import com.txt.readerlibrary.R.id;
import com.txt.readerlibrary.R.layout;
import com.txt.readerlibrary.R.menu;
import com.txt.readerlibrary.R.mipmap;
import com.txt.readerlibrary.R.string;
import com.txt.readerlibrary.base.BaseLibrayActivity;
import com.txt.readerlibrary.db.BookList;
import com.txt.readerlibrary.db.BookMarks;
import com.txt.readerlibrary.dialog.PageModeDialog;
import com.txt.readerlibrary.dialog.SettingDialog;
import com.txt.readerlibrary.dialog.PageModeDialog.PageModeListener;
import com.txt.readerlibrary.dialog.SettingDialog.SettingListener;
import com.txt.readerlibrary.util.BrightnessUtil;
import com.txt.readerlibrary.util.PageFactory;
import com.txt.readerlibrary.util.PageFactory.PageEvent;
import com.txt.readerlibrary.view.PageWidget;
import com.txt.readerlibrary.view.PageWidget.TouchListener;
import com.txtreader.ttspluginlibray.IHostInterface;
import com.txtreader.ttspluginlibray.IPluginInterface;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.litepal.crud.DataSupport;

import dalvik.system.DexClassLoader;

public class ReadActivity extends BaseLibrayActivity {
    private static final String TAG = "cc";
    private static final String EXTRA_BOOK = "bookList";
    private static final int MESSAGE_CHANGEPROGRESS = 1;



    PageWidget bookpage;
    Toolbar toolbar;
    AppBarLayout appbar;
    TextView tv_stopRead;
    RelativeLayout rl_read_bottom;
    TextView tv_progress;
    RelativeLayout rl_progress;
    TextView tvPre;
    SeekBar sb_progress;
    TextView tvNext;
    TextView tvDirectory;
    TextView tv_dayornight;
    TextView tv_pagemode;
    TextView tv_setting;
    LinearLayout bookpopBottom;
    RelativeLayout rl_bottom;
    private Config config;
    private LayoutParams lp;
    private BookList bookList;
    private PageFactory pageFactory;
    private int screenWidth;
    private int screenHeight;
    private Boolean isShow = Boolean.valueOf(false);
    private SettingDialog mSettingDialog;
    private PageModeDialog mPageModeDialog;
    private Boolean mDayOrNight;

    private  SeekBar yuyinSpeedBar;

//    private SpeechSynthesizer mSpeechSynthesizer;
    private boolean isSpeaking = false;
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("android.intent.action.BATTERY_CHANGED")) {
                Log.e("ReadActivity", "android.intent.action.BATTERY_CHANGED");
                int level = intent.getIntExtra("level", 0);
                ReadActivity.this.pageFactory.updateBattery(level);
            } else if(intent.getAction().equals("android.intent.action.TIME_TICK")) {
                Log.e("ReadActivity", "android.intent.action.TIME_TICK");
                ReadActivity.this.pageFactory.updateTime();
            }

        }
    };
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case 1:
                    float progress = ((Float)msg.obj).floatValue();
                    ReadActivity.this.setSeekBarProgress(progress);
                default:
            }
        }
    };
    String appId;
    String appKey;
    String appSecreKey;

    public ReadActivity() {
    }

    public int getLayoutRes() {
        return R.layout.activity_read;
    }

    private void initView() {
        this.bookpage = (PageWidget)this.findViewById(R.id.bookpage);
        this.toolbar = (Toolbar)this.findViewById(R.id.toolbar);
        this.appbar = (AppBarLayout)this.findViewById(R.id.appbar);
        this.tv_stopRead = (TextView)this.findViewById(R.id.tv_stop_read);
        this.rl_read_bottom = (RelativeLayout)this.findViewById(R.id.rl_read_bottom);
        this.tv_progress = (TextView)this.findViewById(R.id.tv_progress);
        this.rl_progress = (RelativeLayout)this.findViewById(R.id.rl_progress);
        this.tvPre = (TextView)this.findViewById(R.id.tv_pre);
        this.sb_progress = (SeekBar)this.findViewById(R.id.sb_progress);
        this.tvNext = (TextView)this.findViewById(R.id.tv_next);
        this.tvDirectory = (TextView)this.findViewById(R.id.tv_directory);
        this.tv_dayornight = (TextView)this.findViewById(R.id.tv_dayornight);
        this.tv_pagemode = (TextView)this.findViewById(R.id.tv_pagemode);
        this.tv_setting = (TextView)this.findViewById(R.id.tv_setting);
        this.bookpopBottom = (LinearLayout)this.findViewById(R.id.bookpop_bottom);
        this.rl_bottom = (RelativeLayout)this.findViewById(R.id.rl_bottom);
        this.yuyinSpeedBar=(SeekBar)findViewById(R.id.yuyin_speed_sb);
        this.tv_progress.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
        this.rl_progress.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
        this.tvPre.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ReadActivity.this.pageFactory.preChapter();
            }
        });
        this.sb_progress.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
        this.tvNext.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ReadActivity.this.pageFactory.nextChapter();
            }
        });
        this.tvDirectory.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ReadActivity.this, MarkActivity.class);
                ReadActivity.this.startActivity(intent);
            }
        });
        this.tv_dayornight.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ReadActivity.this.changeDayOrNight();
            }
        });
        this.tv_pagemode.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ReadActivity.this.hideReadSetting();
                ReadActivity.this.mPageModeDialog.show();
            }
        });
        this.tv_setting.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ReadActivity.this.hideReadSetting();
                ReadActivity.this.mSettingDialog.show();
            }
        });
        this.bookpopBottom.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
        this.rl_bottom.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
        this.tv_stopRead.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (hostInterface!=null) {
                    hostInterface.stop();
                    ReadActivity.this.isSpeaking = false;
                    ReadActivity.this.hideReadSetting();
                }

            }
        });

        yuyinSpeedBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                     int progress=seekBar.getProgress();
                Log.d(TAG, "progress---->"+progress);
                if (hostInterface!=null){
                    hostInterface.setPitch(progress/10+"");
                }
            }
        });

    }

    protected void initData() {
        this.initView();
        if(VERSION.SDK_INT >= 14 && VERSION.SDK_INT < 19) {
            this.bookpage.setLayerType(1, (Paint)null);
        }

        this.toolbar.setTitle("");
        this.setSupportActionBar(this.toolbar);
        this.toolbar.setNavigationIcon(mipmap.return_button);
        this.toolbar.setNavigationOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ReadActivity.this.finish();
            }
        });
        this.config = Config.getInstance();
        this.pageFactory = PageFactory.getInstance();
        IntentFilter mfilter = new IntentFilter();
        mfilter.addAction("android.intent.action.BATTERY_CHANGED");
        mfilter.addAction("android.intent.action.TIME_TICK");
        this.registerReceiver(this.myReceiver, mfilter);
        this.mSettingDialog = new SettingDialog(this);
        this.mPageModeDialog = new PageModeDialog(this);
        WindowManager manage = this.getWindowManager();
        Display display = manage.getDefaultDisplay();
        Point displaysize = new Point();
        display.getSize(displaysize);
        this.screenWidth = displaysize.x;
        this.screenHeight = displaysize.y;
        this.getWindow().addFlags(128);
        this.hideSystemUI();
        if(!this.config.isSystemLight().booleanValue()) {
            BrightnessUtil.setBrightness(this, this.config.getLight());
        }

        Intent intent = this.getIntent();
        this.bookList = (BookList)intent.getSerializableExtra("bookList");
        this.bookpage.setPageMode(this.config.getPageMode());
        this.pageFactory.setPageWidget(this.bookpage);

        try {
            this.pageFactory.openBook(this.bookList);
            Log.i("cc", this.bookList.toString());
        } catch (IOException var7) {
            var7.printStackTrace();
            Toast.makeText(this, "打开电子书失败", Toast.LENGTH_SHORT).show();
        }

        this.initDayOrNight();
    }

    protected void initListener() {
        this.sb_progress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            float pro;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this.pro = (float)((double)progress / 10000.0D);
                ReadActivity.this.showProgress(this.pro);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                ReadActivity.this.pageFactory.changeProgress(this.pro);
            }
        });
        this.mPageModeDialog.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                ReadActivity.this.hideSystemUI();
            }
        });
        this.mPageModeDialog.setPageModeListener(new PageModeListener() {
            public void changePageMode(int pageMode) {
                ReadActivity.this.bookpage.setPageMode(pageMode);
            }
        });
        this.mSettingDialog.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                ReadActivity.this.hideSystemUI();
            }
        });
        this.mSettingDialog.setSettingListener(new SettingListener() {
            public void changeSystemBright(Boolean isSystem, float brightness) {
                if(!isSystem.booleanValue()) {
                    BrightnessUtil.setBrightness(ReadActivity.this, brightness);
                } else {
                    int bh = BrightnessUtil.getScreenBrightness(ReadActivity.this);
                    BrightnessUtil.setBrightness(ReadActivity.this, bh);
                }

            }

            public void changeFontSize(int fontSize) {
                ReadActivity.this.pageFactory.changeFontSize(fontSize);
            }

            public void changeTypeFace(Typeface typeface) {
                ReadActivity.this.pageFactory.changeTypeface(typeface);
            }

            public void changeBookBg(int type) {
                ReadActivity.this.pageFactory.changeBookBg(type);
            }
        });
        this.pageFactory.setPageEvent(new PageEvent() {
            public void changeProgress(float progress) {
                Message message = new Message();
                message.what = 1;
                message.obj = Float.valueOf(progress);
                ReadActivity.this.mHandler.sendMessage(message);
            }
        });
        this.bookpage.setTouchListener(new TouchListener() {
            public void center() {
                if(ReadActivity.this.isShow.booleanValue()) {
                    ReadActivity.this.hideReadSetting();
                } else {
                    ReadActivity.this.showReadSetting();
                }

            }

            public Boolean prePage() {
                if(!ReadActivity.this.isShow.booleanValue() && !ReadActivity.this.isSpeaking) {
                    ReadActivity.this.pageFactory.prePage();
                    return ReadActivity.this.pageFactory.isfirstPage()?Boolean.valueOf(false):Boolean.valueOf(true);
                } else {
                    return Boolean.valueOf(false);
                }
            }

            public Boolean nextPage() {
                Log.e("setTouchListener", "nextPage");
                if(!ReadActivity.this.isShow.booleanValue() && !ReadActivity.this.isSpeaking) {
                    ReadActivity.this.pageFactory.nextPage();
                    return ReadActivity.this.pageFactory.islastPage()?Boolean.valueOf(false):Boolean.valueOf(true);
                } else {
                    return Boolean.valueOf(false);
                }
            }

            public void cancel() {
                ReadActivity.this.pageFactory.cancelPage();
            }
        });
    }

    protected void onResume() {
        super.onResume();
        if(!this.isShow.booleanValue()) {
            this.hideSystemUI();
        }
        if (hostInterface!=null) {
            hostInterface.resume();
        }
    }

    protected void onStop() {
        super.onStop();
        if (hostInterface!=null) {
            hostInterface.stop();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        this.pageFactory.clear();
        this.bookpage = null;
        this.unregisterReceiver(this.myReceiver);
        this.isSpeaking = false;
        if (hostInterface!=null) {
            hostInterface.release();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == 4) {
            if(this.isShow.booleanValue()) {
                this.hideReadSetting();
                return true;
            }

            if(this.mSettingDialog.isShowing()) {
                this.mSettingDialog.hide();
                return true;
            }

            if(this.mPageModeDialog.isShowing()) {
                this.mPageModeDialog.hide();
                return true;
            }

            this.finish();
        }

        return super.onKeyDown(keyCode, event);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.read, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_add_bookmark) {
            if(this.pageFactory.getCurrentPage() != null) {
                List intent = DataSupport.where(new String[]{"bookpath = ? and begin = ?", this.pageFactory.getBookPath(), this.pageFactory.getCurrentPage().getBegin() + ""}).find(BookMarks.class);
                if(!intent.isEmpty()) {
                    Toast.makeText(this, "该书签已存在", Toast.LENGTH_LONG).show();
                } else {
                    BookMarks bookMarks = new BookMarks();
                    String word = "";

                    String time;
                    for(Iterator e = this.pageFactory.getCurrentPage().getLines().iterator(); e.hasNext(); word = word + time) {
                        time = (String)e.next();
                    }

                    try {
                        SimpleDateFormat e1 = new SimpleDateFormat("yyyy-MM-dd HH:mm ss");
                        time = e1.format(new Date());
                        bookMarks.setTime(time);
                        bookMarks.setBegin(this.pageFactory.getCurrentPage().getBegin());
                        bookMarks.setText(word);
                        bookMarks.setBookpath(this.pageFactory.getBookPath());
                        bookMarks.save();
                        Toast.makeText(this, "书签添加成功", Toast.LENGTH_LONG).show();
                    } catch (SQLException var8) {
                        Toast.makeText(this, "该书签已存在",Toast.LENGTH_LONG).show();
                    } catch (Exception var9) {
                        Toast.makeText(this, "添加书签失败",Toast.LENGTH_LONG).show();
                    }
                }
            }
        } else if(id ==R.id.action_read_book) {
            if (hostInterface==null) {
                getDeclaredMethod();
            }else{
                hostInterface.speak(this.pageFactory.getCurrentPage().getLineToString());
            }

//            this.initialTts();
//            if(this.mSpeechSynthesizer != null) {
//                this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "5");
//                this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
//                this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");
//                this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
//                this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOCODER_OPTIM_LEVEL, "0");
//                int intent1 = this.mSpeechSynthesizer.speak(this.pageFactory.getCurrentPage().getLineToString());
//                if(intent1 < 0) {
//                    Log.e("ReadActivity", "error,please look up error code in doc or URL:http://yuyin.baidu.com/docs/tts/122 ");
//                } else {
//                    this.hideReadSetting();
//                    this.isSpeaking = true;
//                }
//            }
        } else if(id == R.id.action_read_book) {
            Intent intent2 = new Intent(this, FileChooserActivity.class);
            this.startActivity(intent2);
        }

        return super.onOptionsItemSelected(item);
    }






    String YUYINAPKPATH = TxtReader.getTxtReader().getYuYinPath();
    DexClassLoader mClassLoader;


    @Override
    protected void attachBaseContext(Context newBase) {
        new Thread(){
            @Override
            public void run() {
                //创建一个属于我们自己插件的ClassLoader，我们分析过只能使用DexClassLoader
                String cachePath = ReadActivity.this.getCacheDir().getAbsolutePath();
                File file1= new File(YUYINAPKPATH);
                if (file1.exists()){
                    mClassLoader = new DexClassLoader(YUYINAPKPATH, cachePath,cachePath, getClassLoader());
                }else{
                    Log.i("cc","文件1不存在");
                }
            }
        }.start();
        super.attachBaseContext(newBase);
    }

    private AssetManager assetManager;
    private Resources newResource;

    private  String dopage="com.txtreader.BaiduTtsApp";

    private  Context context=this;
private void getDeclaredMethod(){
//    appId="9926863";
//    appKey="QHNP9sN1WIQ5YueG7hOK2Gbg";
//    appSecreKey="4019c6f96bb0929a34671820fcf04f29";
    getActivityMetaData();

    Log.d(TAG, "appId---->"+appId);
    try {
        //创建我们自己的Resource

        assetManager = AssetManager.class.newInstance();
        Method addAssetPathMethod = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
        addAssetPathMethod.setAccessible(true);
//            addAssetPathMethod.invoke(assetManager, mPath);
        addAssetPathMethod.invoke(assetManager, YUYINAPKPATH);

        Method ensureStringBlocks = AssetManager.class.getDeclaredMethod("ensureStringBlocks");
        ensureStringBlocks.setAccessible(true);
        ensureStringBlocks.invoke(assetManager);

        Resources supResource = getResources();
        Log.e(TAG, "supResource = " + supResource);
        newResource = new Resources(assetManager, supResource.getDisplayMetrics(), supResource.getConfiguration());
        Log.d(TAG, "设置 getResource = " + getResources());
        Log.d(TAG, "设置 getAssets() = " + getAssets());
//        mTheme = newResource.newTheme();
//        mTheme.setTo(super.getTheme());

        Log.d(TAG, "mClassLoader----->"+mClassLoader);
            Class clazz=mClassLoader.loadClass(dopage);
            IPluginInterface plu=new PluginsIml();
        Log.d(TAG, "clazz---->"+clazz);

            if (clazz!=null){
                Method method=clazz.getMethod("setContext",Context.class,AssetManager.class,Resources.class,IPluginInterface.class);
                method.setAccessible(true);
                method.invoke(clazz.newInstance(), context, assetManager, supResource,plu);
            }

    } catch (Exception e) {
        Log.e(TAG, "走了我的callActivityOnCreate 错了 = " + e.getMessage());
    }
}

    IHostInterface   hostInterface;

public  class PluginsIml implements IPluginInterface{
    @Override
    public void getPluginClass(IHostInterface hostInterface) {//与plugin初始化
        ReadActivity.this.hostInterface=hostInterface;
        hostInterface.initPlugin(appId,appKey,appSecreKey);
        hostInterface.speak(pageFactory.getCurrentPage().getLineToString());
        Log.d(TAG, "getPluginClass: hostInterface----->"+hostInterface);
    }

    @Override
    public void onSynStart(String s) {
    onSynthesizeStart(s);
    }

    @Override
    public void onSynDataArrived(String s, byte[] bytes, int i) {
          onSynthesizeDataArrived(s,bytes,i);
    }

    @Override
    public void onSynFinish(String s) {
            onSynthesizeFinish(s);
    }

    @Override
    public void onSpeStart(String s) {
        onSpeechStart(s);
    }

    @Override
    public void onSpeProgressChanged(String s, int i) {
        onSpeechProgressChanged(s,i);
    }

    @Override
    public void onSpeFinish(String s) {
      onSpeechFinish(s);
    }

    @Override
    public void onError(String s, String speechError) {
    onSpeError(s,speechError);
    }

    @Override
    public void speekResult(Boolean result) {
        if (result){
            hideReadSetting();
           isSpeaking = true;
        }
    }
}


    public static boolean openBook(final BookList bookList, Activity context) {
        if (bookList == null) {
            throw new NullPointerException("BookList can not be null");
        }

        Intent intent = new Intent(context, ReadActivity.class);
        intent.putExtra(EXTRA_BOOK, bookList);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //销毁目标Activity和它之上的所有Activity，重新创建目标Activity
        context.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        context.startActivity(intent);
        return true;
    }

    private void hideSystemUI() {
        this.getWindow().getDecorView().setSystemUiVisibility(5892);
    }

    private void showSystemUI() {
        this.getWindow().getDecorView().setSystemUiVisibility(5376);
    }

    public void showProgress(float progress) {
        if(this.rl_progress.getVisibility() != View.VISIBLE) {
            this.rl_progress.setVisibility(View.VISIBLE);
        }
        this.setProgress(progress);
    }

    public void hideProgress() {
        this.rl_progress.setVisibility(View.GONE);
    }

    public void initDayOrNight() {
        this.mDayOrNight = Boolean.valueOf(this.config.getDayOrNight());
        if(this.mDayOrNight.booleanValue()) {
            this.tv_dayornight.setText(this.getResources().getString(string.read_setting_day));
        } else {
            this.tv_dayornight.setText(this.getResources().getString(string.read_setting_night));
        }
    }

    public void changeDayOrNight() {
        if(this.mDayOrNight.booleanValue()) {
            this.mDayOrNight = Boolean.valueOf(false);
            this.tv_dayornight.setText(this.getResources().getString(string.read_setting_night));
        } else {
            this.mDayOrNight = Boolean.valueOf(true);
            this.tv_dayornight.setText(this.getResources().getString(string.read_setting_day));
        }

        this.config.setDayOrNight(this.mDayOrNight.booleanValue());
        this.pageFactory.setDayOrNight(this.mDayOrNight);
    }

    private void setProgress(float progress) {
        DecimalFormat decimalFormat = new DecimalFormat("00.00");
        String p = decimalFormat.format((double)progress * 100.0D);
        this.tv_progress.setText(p + "%");
    }

    public void setSeekBarProgress(float progress) {
        this.sb_progress.setProgress((int)(progress * 10000.0F));
    }

    private void showReadSetting() {
        this.isShow = Boolean.valueOf(true);
        this.rl_progress.setVisibility(View.GONE);
        Animation bottomAnim;
        if(this.isSpeaking) {
            bottomAnim = AnimationUtils.loadAnimation(this, anim.dialog_top_enter);
            this.rl_read_bottom.startAnimation(bottomAnim);
            this.rl_read_bottom.setVisibility(View.VISIBLE);
        } else {
            this.showSystemUI();
            bottomAnim = AnimationUtils.loadAnimation(this, anim.dialog_enter);
            Animation topAnim = AnimationUtils.loadAnimation(this, anim.dialog_top_enter);
            this.rl_bottom.startAnimation(topAnim);
            this.appbar.startAnimation(topAnim);
            this.rl_bottom.setVisibility(View.VISIBLE);
            this.appbar.setVisibility(View.VISIBLE);
        }

    }

    private void hideReadSetting() {
        this.isShow = Boolean.valueOf(false);
        Animation bottomAnim = AnimationUtils.loadAnimation(this, anim.dialog_exit);
        Animation topAnim = AnimationUtils.loadAnimation(this, anim.dialog_top_exit);
        if(this.rl_bottom.getVisibility() == View.VISIBLE) {
            this.rl_bottom.startAnimation(topAnim);
        }

        if(this.appbar.getVisibility() == View.VISIBLE) {
            this.appbar.startAnimation(topAnim);
        }

        if(this.rl_read_bottom.getVisibility() == View.VISIBLE) {
            this.rl_read_bottom.startAnimation(topAnim);
        }

        this.rl_bottom.setVisibility(View.GONE);
        this.rl_read_bottom.setVisibility(View.GONE);
        this.appbar.setVisibility(View.GONE);
        this.hideSystemUI();
    }

    private void getActivityMetaData() {
        try {
            ActivityInfo e = this.getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            this.appId = e.metaData.getInt("BaiDuTts_AppId") + "";
            Log.i("cc", "appId--->" + this.appId);
            this.appKey = e.metaData.getString("BaiDuTts_ApiKey");
            this.appSecreKey = e.metaData.getString("BaiDuTts_SecretKey");
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }


    public void onSynthesizeStart(String s) {
    }

    public void onSynthesizeDataArrived(String utteranceId, byte[] data, int progress) {
    }

    public void onSynthesizeFinish(String utteranceId) {
    }

    public void onSpeechStart(String utteranceId) {
    }

    public void onSpeechProgressChanged(String utteranceId, int progress) {
    }

    public void onSpeechFinish(String utteranceId) {
        this.pageFactory.nextPage();
        if(this.pageFactory.islastPage()) {
            this.isSpeaking = false;
            Toast.makeText(this, "小说已经读完了", Toast.LENGTH_LONG).show();
        } else {
            this.isSpeaking = true;
            hostInterface.speak(this.pageFactory.getCurrentPage().getLineToString());
        }

    }

    public void onSpeError(String utteranceId, String error) {
        hostInterface.stop();
        this.isSpeaking = false;
        Log.e("ReadActivity", error);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
