package com.txt.readerlibrary;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.SQLException;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.txt.readerlibrary.base.BaseLibrayActivity;
import com.txt.readerlibrary.util.BrightnessUtil;
import com.txt.readerlibrary.db.BookList;
import com.txt.readerlibrary.db.BookMarks;
import com.txt.readerlibrary.dialog.PageModeDialog;
import com.txt.readerlibrary.dialog.SettingDialog;
import com.txt.readerlibrary.util.CommonUtil;
import com.txt.readerlibrary.util.PageFactory;
import com.txt.readerlibrary.utils.LogUtils;
import com.txt.readerlibrary.view.PageWidget;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by Administrator on 2016/7/15 0015.
 */
public class ReadActivity extends BaseLibrayActivity {
    private static final String TAG = "ReadActivity";
    private final static String EXTRA_BOOK = "bookList";
    private final static int MESSAGE_CHANGEPROGRESS = 1;
    PageWidget bookpage;
//    @BindView(R.id.toolbar)
    Toolbar toolbar;
//    @BindView(R.id.appbar)
    AppBarLayout appbar;
//    @BindView(R.id.tv_stop_read)
//    TextView tv_stopRead;
    TextView tv_progress;
    RelativeLayout rl_progress;
//    @BindView(R.id.tv_pre)
    TextView tvPre;
//    @BindView(R.id.sb_progress)
    SeekBar sb_progress;
//    @BindView(R.id.tv_next)
    TextView tvNext;
//    @BindView(R.id.tv_directory)
    TextView tvDirectory;
//    @BindView(R.id.tv_dayornight)
    TextView tv_dayornight;
//    @BindView(R.id.tv_pagemode)
    TextView tv_pagemode;
//    @BindView(R.id.tv_setting)
    TextView tv_setting;
//    @BindView(R.id.bookpop_bottom)
    LinearLayout bookpopBottom;
//    @BindView(R.id.rl_bottom)
    RelativeLayout rl_bottom;

    private Config config;
    private WindowManager.LayoutParams lp;
    private BookList bookList;
    private PageFactory pageFactory;
    private int screenWidth, screenHeight;
    // popwindow是否显示
    private Boolean isShow = false;
    private SettingDialog mSettingDialog;
    private PageModeDialog mPageModeDialog;
    private Boolean mDayOrNight;
    // 语音合成客户端
    private boolean isSpeaking = false;

    // 接收电池信息更新的广播
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                Log.e(TAG, Intent.ACTION_BATTERY_CHANGED);
                int level = intent.getIntExtra("level", 0);
                pageFactory.updateBattery(level);
            } else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                Log.e(TAG, Intent.ACTION_TIME_TICK);
                pageFactory.updateTime();
            }
        }
    };

    @Override
    public int getLayoutRes() {
        return R.layout.activity_read;
    }


    private  void initView(){
       bookpage=(PageWidget)findViewById(R.id.bookpage);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        appbar=(AppBarLayout)findViewById(R.id.appbar);
        tv_progress=(TextView) findViewById(R.id.tv_progress);
        rl_progress=(RelativeLayout) findViewById(R.id.rl_progress);
        tvPre=(TextView) findViewById(R.id.tv_pre);
        sb_progress=(SeekBar) findViewById(R.id.sb_progress);
        tvNext=(TextView) findViewById(R.id.tv_next);
        tvDirectory=(TextView) findViewById(R.id.tv_directory);
        tv_dayornight=(TextView) findViewById(R.id.tv_dayornight);
        tv_pagemode=(TextView) findViewById(R.id.tv_pagemode);
        tv_setting=(TextView) findViewById(R.id.tv_setting);
        bookpopBottom=(LinearLayout) findViewById(R.id.bookpop_bottom);
       rl_bottom=(RelativeLayout) findViewById(R.id.rl_bottom);

        tv_progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rl_progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tvPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageFactory.preChapter();
            }
        });
        sb_progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageFactory.nextChapter();
            }
        });
        tvDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReadActivity.this, MarkActivity.class);
                intent.putExtra(CommonUtil.IS_NET_URL_CAN,bookList.isNetUrl());
                startActivity(intent);
            }
        });
        tv_dayornight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDayOrNight();
            }
        });
        tv_pagemode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideReadSetting();
                mPageModeDialog.show();
            }
        });
       tv_setting.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               hideReadSetting();
               mSettingDialog.show();
           }
       });
        bookpopBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rl_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    @Override
    protected void initData() {
        initView();
//       initialEnv();
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 19) {
            bookpage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        toolbar.setTitle("");

        setSupportActionBar(toolbar);

//        顶部栏  返回按钮
        toolbar.setNavigationIcon(R.mipmap.return_button);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        config = Config.getInstance();
        pageFactory = PageFactory.getInstance();

        //注册电池监听
        IntentFilter mfilter = new IntentFilter();
        mfilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mfilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(myReceiver, mfilter);

        mSettingDialog = new SettingDialog(this);
        mPageModeDialog = new PageModeDialog(this);
        //获取屏幕宽高
        WindowManager manage = getWindowManager();
        Display display = manage.getDefaultDisplay();

        Point displaysize = new Point();

        display.getSize(displaysize);

        screenWidth = displaysize.x;
        screenHeight = displaysize.y;

        //保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //隐藏
        hideSystemUI();

        //改变屏幕亮度
        if (!config.isSystemLight()) {
            BrightnessUtil.setBrightness(this, config.getLight());
        }
        //获取intent中的携带的信息
        Intent intent = getIntent();
        bookList = (BookList) intent.getSerializableExtra(EXTRA_BOOK);

        bookpage.setPageMode(config.getPageMode());
        pageFactory.setPageWidget(bookpage);

        try {
            pageFactory.openBook(bookList);
            LogUtils.D(bookList.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "打开电子书失败", Toast.LENGTH_SHORT).show();
        }

        initDayOrNight();

//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                initialTts();
//            }
//        }.start();
//        initialTts();
    }

    @Override
    protected void initListener() {
        sb_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            float pro;

            // 触发操作，拖动
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pro = (float) (progress / 10000.0);
                showProgress(pro);
            }

            // 表示进度条刚开始拖动，开始拖动时候触发的操作
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            // 停止拖动时候
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                pageFactory.changeProgress(pro);
            }
        });

        mPageModeDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                hideSystemUI();
            }
        });

        mPageModeDialog.setPageModeListener(new PageModeDialog.PageModeListener() {
            @Override
            public void changePageMode(int pageMode) {
                bookpage.setPageMode(pageMode);
            }
        });

        mSettingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                hideSystemUI();
            }
        });

        mSettingDialog.setSettingListener(new SettingDialog.SettingListener() {
            @Override
            public void changeSystemBright(Boolean isSystem, float brightness) {
                if (!isSystem) {
                    BrightnessUtil.setBrightness(ReadActivity.this, brightness);
                } else {
                    int bh = BrightnessUtil.getScreenBrightness(ReadActivity.this);
                    BrightnessUtil.setBrightness(ReadActivity.this, bh);
                }
            }

            @Override
            public void changeFontSize(int fontSize) {
                pageFactory.changeFontSize(fontSize);
            }

            @Override
            public void changeTypeFace(Typeface typeface) {
                pageFactory.changeTypeface(typeface);
            }

            @Override
            public void changeBookBg(int type) {
                pageFactory.changeBookBg(type);
            }
        });

        pageFactory.setPageEvent(new PageFactory.PageEvent() {
            @Override
            public void changeProgress(float progress) {
                Message message = new Message();
                message.what = MESSAGE_CHANGEPROGRESS;
                message.obj = progress;
                mHandler.sendMessage(message);
            }
        });

        bookpage.setTouchListener(new PageWidget.TouchListener() { //页面触摸设置
            @Override
            public void center() {
                if (isShow) {
                    hideReadSetting();
                } else {
                    showReadSetting();
                }
            }

            @Override
            public Boolean prePage() {
                if (isShow || isSpeaking) {
                    return false;
                }

                pageFactory.prePage();
                if (pageFactory.isfirstPage()) {
                    return false;
                }

                return true;
            }

            @Override
            public Boolean nextPage() {
                Log.e("setTouchListener", "nextPage");
                if (isShow || isSpeaking) {
                    return false;
                }

                pageFactory.nextPage();
                if (pageFactory.islastPage()) {
                    return false;
                }
                return true;
            }

            @Override
            public void cancel() {
                pageFactory.cancelPage();
            }
        });

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_CHANGEPROGRESS:
                    float progress = (float) msg.obj;
                    setSeekBarProgress(progress);
                    break;
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        if (!isShow) {
            hideSystemUI();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pageFactory.clear();
        bookpage = null;
        unregisterReceiver(myReceiver);
        isSpeaking = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isShow) {
                hideReadSetting();
                return true;
            }
            if (mSettingDialog.isShowing()) {
                mSettingDialog.hide();
                return true;
            }
            if (mPageModeDialog.isShowing()) {
                mPageModeDialog.hide();
                return true;
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.read, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_add_bookmark) {
            if (pageFactory.getCurrentPage() != null) {
                List<BookMarks> bookMarksList=null;
                if (bookList.isNetUrl()) {
                    bookMarksList = DataSupport.where("txtUrl = ? and begin = ?", pageFactory.getTxtUrl(), pageFactory.getCurrentPage().getBegin() + "").find(BookMarks.class);
                }else{
                    bookMarksList = DataSupport.where("bookpath = ? and begin = ?", pageFactory.getBookPath(), pageFactory.getCurrentPage().getBegin() + "").find(BookMarks.class);
                }
                if (!bookMarksList.isEmpty()) {
                    Toast.makeText(ReadActivity.this, "该书签已存在", Toast.LENGTH_SHORT).show();
                } else {
                    BookMarks bookMarks = new BookMarks();
                    String word = "";
                    for (String line : pageFactory.getCurrentPage().getLines()) {
                        word += line;
                    }
                    try {
                        SimpleDateFormat sf = new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm ss");
                        String time = sf.format(new Date());
                        bookMarks.setTime(time);
                        bookMarks.setBegin(pageFactory.getCurrentPage().getBegin());
                        bookMarks.setText(word);
                        if (bookList.isNetUrl()) {
                            bookMarks.setTxtUrl(pageFactory.getTxtUrl());
                        }else{
                            bookMarks.setBookpath(pageFactory.getBookPath());
                        }
                        bookMarks.save();

                        Toast.makeText(ReadActivity.this, "书签添加成功", Toast.LENGTH_SHORT).show();
                    } catch (SQLException e) {
                        Toast.makeText(ReadActivity.this, "该书签已存在", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(ReadActivity.this, "添加书签失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else if(id == R.id.action_search) {
            Intent intent=new Intent(ReadActivity.this,FileChooserActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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

//    public BookPageWidget getPageWidget() {
//        return bookpage;
//    }

    /**
     * 隐藏菜单。沉浸式阅读
     */
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        //  | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    //显示书本进度
    public void showProgress(float progress) {
        if (rl_progress.getVisibility() != View.VISIBLE) {
            rl_progress.setVisibility(View.VISIBLE);
        }
        setProgress(progress);
    }

    //隐藏书本进度
    public void hideProgress() {
        rl_progress.setVisibility(View.GONE);
    }

    public void initDayOrNight() {
        mDayOrNight = config.getDayOrNight();
        if (mDayOrNight) {
            tv_dayornight.setText(getResources().getString(R.string.read_setting_day));
        } else {
            tv_dayornight.setText(getResources().getString(R.string.read_setting_night));
        }
    }

    //改变显示模式
    public void changeDayOrNight() {
        if (mDayOrNight) {
            mDayOrNight = false;
            tv_dayornight.setText(getResources().getString(R.string.read_setting_night));
        } else {
            mDayOrNight = true;
            tv_dayornight.setText(getResources().getString(R.string.read_setting_day));
        }
        config.setDayOrNight(mDayOrNight);
        pageFactory.setDayOrNight(mDayOrNight);
    }

    private void setProgress(float progress) {
        DecimalFormat decimalFormat = new DecimalFormat("00.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p = decimalFormat.format(progress * 100.0);//format 返回的是字符串
        tv_progress.setText(p + "%");
    }

    public void setSeekBarProgress(float progress) {
        sb_progress.setProgress((int) (progress * 10000));
    }

    private void showReadSetting() {
        isShow = true;
        rl_progress.setVisibility(View.GONE);

        if (isSpeaking) {
//            Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_top_enter);
//            rl_read_bottom.startAnimation(topAnim);
//            rl_read_bottom.setVisibility(View.VISIBLE);
        } else {
            showSystemUI();

            Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_enter);
            Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_top_enter);
            rl_bottom.startAnimation(topAnim);
            appbar.startAnimation(topAnim);
//        ll_top.startAnimation(topAnim);
            rl_bottom.setVisibility(View.VISIBLE);
//        ll_top.setVisibility(View.VISIBLE);
            appbar.setVisibility(View.VISIBLE);
        }
    }

    private void hideReadSetting() {
        isShow = false;
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_exit);
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_top_exit);
        if (rl_bottom.getVisibility() == View.VISIBLE) {
            rl_bottom.startAnimation(topAnim);
        }
        if (appbar.getVisibility() == View.VISIBLE) {
            appbar.startAnimation(topAnim);
        }
//        if (rl_read_bottom.getVisibility() == View.VISIBLE) {
//            rl_read_bottom.startAnimation(topAnim);
//        }
//        ll_top.startAnimation(topAnim);
        rl_bottom.setVisibility(View.GONE);
//        rl_read_bottom.setVisibility(View.GONE);
//        ll_top.setVisibility(View.GONE);
        appbar.setVisibility(View.GONE);
        hideSystemUI();
    }
//    private  void  getActivityMetaData(){
//        try {
//            ActivityInfo info=this.getPackageManager()
//                    .getActivityInfo(getComponentName(),
//                            PackageManager.GET_META_DATA);
//            appId =info.metaData.getInt("BaiDuTts_AppId")+"";
//            Log.i("cc","appId--->"+appId);
//            appKey=info.metaData.getString("BaiDuTts_ApiKey");
//            appSecreKey=info.metaData.getString("BaiDuTts_SecretKey");
//        }catch ( Exception e){
//            e.printStackTrace();
//        }
//
//    }
//    String appId;
//    String appKey;
//    String  appSecreKey;
//    private void initialTts() {
//        getActivityMetaData();
//        Log.i("cc","appId--->"+appId+"appKey--->"+appKey+"appSecreKey--->"+appSecreKey);
//        this.mSpeechSynthesizer = SpeechSynthesizer.getInstance();
//        this.mSpeechSynthesizer.setContext(this);
//        this.mSpeechSynthesizer.setSpeechSynthesizerListener(this);
//        // 文本模型文件路径 (离线引擎使用)
//        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, mSampleDirPath + "/" //TxtReader.getTxtReader().getTTPath()
//                + TEXT_MODEL_NAME);
//        // 声学模型文件路径 (离线引擎使用)
//        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,mSampleDirPath + "/"
//                + SPEECH_FEMALE_MODEL_NAME);
//        // 本地授权文件路径,如未设置将使用默认路径.设置临时授权文件路径，LICENCE_FILE_NAME请替换成临时授权文件的实际路径，仅在使用临时license文件时需要进行设置，如果在[应用管理]中开通了正式离线授权，不需要设置该参数，建议将该行代码删除（离线引擎）
//        // 如果合成结果出现临时授权文件将要到期的提示，说明使用了临时授权文件，请删除临时授权即可。
////        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, ((AppContext)getApplication()).getTTPath() + "/"
////                + AppContext.LICENSE_FILE_NAME);
//        // 请替换为语音开发者平台上注册应用得到的App ID (离线授权)
//        this.mSpeechSynthesizer.setAppId(appId/*这里只是为了让Demo运行使用的APPID,请替换成自己的id。*/);
//        // 请替换为语音开发者平台注册应用得到的apikey和secretkey (在线授权)
//        this.mSpeechSynthesizer.setApiKey(appKey,
//                appSecreKey/*这里只是为了让Demo正常运行使用APIKey,请替换成自己的APIKey*/);
//        // 发音人（在线引擎），可用参数为0,1,2,3。。。（服务器端会动态增加，各值含义参考文档，以文档说明为准。0--普通女声，1--普通男声，2--特别男声，3--情感男声。。。）
//        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
//        // 设置Mix模式的合成策略
//        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
//        // 授权检测接口(只是通过AuthInfo进行检验授权是否成功。)
//        // AuthInfo接口用于测试开发者是否成功申请了在线或者离线授权，如果测试授权成功了，可以删除AuthInfo部分的代码（该接口首次验证时比较耗时），不会影响正常使用（合成使用时SDK内部会自动验证授权）
//        AuthInfo authInfo = this.mSpeechSynthesizer.auth(TtsMode.MIX);
//
//        if (authInfo.isSuccess()) {
//            Log.e(TAG, "auth success");
//        } else {
//            String errorMsg = authInfo.getTtsError().getDetailMessage();
//            Log.e(TAG, "auth failed errorMsg=" + errorMsg);
//        }
//
//        // 初始化tts
//        mSpeechSynthesizer.initTts(TtsMode.MIX);
//        // 加载离线英文资源（提供离线英文合成功能）
//        int result = mSpeechSynthesizer.loadEnglishModel(mSampleDirPath + "/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath
//                + "/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
//
//      Log.i("cc","result--->"+result);
////        toPrint("loadEnglishModel result=" + result);
////
////        //打印引擎信息和model基本信息
////        printEngineInfo();
//    }


//    /*
//    * @param arg0
//    */
//    @Override
//    public void onSynthesizeStart(String s) {
//
//    }
//
//    /**
//     * 合成数据和进度的回调接口，分多次回调
//     *
//     * @param utteranceId
//     * @param data        合成的音频数据。该音频数据是采样率为16K，2字节精度，单声道的pcm数据。
//     * @param progress    文本按字符划分的进度，比如:你好啊 进度是0-3
//     */
//    @Override
//    public void onSynthesizeDataArrived(String utteranceId, byte[] data, int progress) {
//
//    }
//
//    /**
//     * 合成正常结束，每句合成正常结束都会回调，如果过程中出错，则回调onError，不再回调此接口
//     *
//     * @param utteranceId
//     */
//    @Override
//    public void onSynthesizeFinish(String utteranceId) {
//
//    }
//
//    /**
//     * 播放开始，每句播放开始都会回调
//     *
//     * @param utteranceId
//     */
//    @Override
//    public void onSpeechStart(String utteranceId) {
//
//    }
//
//    /**
//     * 播放进度回调接口，分多次回调
//     *
//     * @param utteranceId
//     * @param progress    文本按字符划分的进度，比如:你好啊 进度是0-3
//     */
//    @Override
//    public void onSpeechProgressChanged(String utteranceId, int progress) {
//
//    }
//
//    /**
//     * 播放正常结束，每句播放正常结束都会回调，如果过程中出错，则回调onError,不再回调此接口
//     *
//     * @param utteranceId
//     */
//    @Override
//    public void onSpeechFinish(String utteranceId) {
//        pageFactory.nextPage();
//        if (pageFactory.islastPage()) {
//            isSpeaking = false;
//            Toast.makeText(ReadActivity.this, "小说已经读完了", Toast.LENGTH_SHORT);
//        } else {
//            isSpeaking = true;
//            mSpeechSynthesizer.speak(pageFactory.getCurrentPage().getLineToString());
//        }
//    }
//
//    /**
//     * 当合成或者播放过程中出错时回调此接口
//     *
//     * @param utteranceId
//     * @param error       包含错误码和错误信息
//     */
//    @Override
//    public void onError(String utteranceId, SpeechError error) {
//        mSpeechSynthesizer.stop();
//        isSpeaking = false;
//        Log.e(TAG, error.description);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
