package com.txt.readerlibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.txt.readerlibrary.Config;
import com.txt.readerlibrary.R;


/**
 * Created by Administrator on 2016/8/30 0030.
 */
public class PageModeDialog extends Dialog {

    TextView tv_simulation;
    TextView tv_cover;
    TextView tv_slide;
    TextView tv_none;

    private Config config;
    private PageModeListener pageModeListener;

    private PageModeDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    public PageModeDialog(Context context) {
        this(context, R.style.setting_dialog);
    }

    public PageModeDialog(Context context, int themeResId) {
        super(context, themeResId);

    }

    private  void  initView(){
        tv_simulation=(TextView)findViewById(R.id.tv_simulation);
        tv_cover=(TextView)findViewById(R.id.tv_cover);
        tv_slide=(TextView)findViewById(R.id.tv_slide);
        tv_none=(TextView)findViewById(R.id.tv_none);


       tv_cover.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               selectPageMode(Config.PAGE_MODE_COVER);
               setPageMode(Config.PAGE_MODE_COVER);
           }
       });
        tv_none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPageMode(Config.PAGE_MODE_NONE);
                setPageMode(Config.PAGE_MODE_NONE);
            }
        });
        tv_slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPageMode(Config.PAGE_MODE_SLIDE);
                setPageMode(Config.PAGE_MODE_SLIDE);
            }
        });
        tv_simulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPageMode(Config.PAGE_MODE_SIMULATION);
                setPageMode(Config.PAGE_MODE_SIMULATION);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);
        setContentView(R.layout.dialog_pagemode);

        initView();

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);

        config = Config.getInstance();
        selectPageMode(config.getPageMode());
    }


    //设置翻页
    public void setPageMode(int pageMode) {
        config.setPageMode(pageMode);
        if (pageModeListener != null) {
            pageModeListener.changePageMode(pageMode);
        }
    }
    //选择怕翻页
    private void selectPageMode(int pageMode) {
        if (pageMode == Config.PAGE_MODE_SIMULATION) {
            setTextViewSelect(tv_simulation, true);
            setTextViewSelect(tv_cover, false);
            setTextViewSelect(tv_slide, false);
            setTextViewSelect(tv_none, false);
        } else if (pageMode == Config.PAGE_MODE_COVER) {
            setTextViewSelect(tv_simulation, false);
            setTextViewSelect(tv_cover, true);
            setTextViewSelect(tv_slide, false);
            setTextViewSelect(tv_none, false);
        } else if (pageMode == Config.PAGE_MODE_SLIDE) {
            setTextViewSelect(tv_simulation, false);
            setTextViewSelect(tv_cover, false);
            setTextViewSelect(tv_slide, true);
            setTextViewSelect(tv_none, false);
        } else if (pageMode == Config.PAGE_MODE_NONE) {
            setTextViewSelect(tv_simulation, false);
            setTextViewSelect(tv_cover, false);
            setTextViewSelect(tv_slide, false);
            setTextViewSelect(tv_none, true);
        }
    }

    //设置按钮选择的背景
    private void setTextViewSelect(TextView textView, Boolean isSelect) {
        if (isSelect) {
            textView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.button_select_bg));
            textView.setTextColor(getContext().getResources().getColor(R.color.read_dialog_button_select));
        } else {
            textView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.button_bg));
            textView.setTextColor(getContext().getResources().getColor(R.color.white));
        }
    }

    public void setPageModeListener(PageModeListener pageModeListener) {
        this.pageModeListener = pageModeListener;
    }

    public interface PageModeListener {
        void changePageMode(int pageMode);
    }
}
