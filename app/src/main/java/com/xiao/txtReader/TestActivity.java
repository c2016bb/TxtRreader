package com.xiao.txtReader;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.txt.readerlibrary.TxtReader;

public class TestActivity extends AppCompatActivity {
//  String url="http://117.71.57.47:10000/resource/uploadFiles/2018/01/18/bd226d0a-d72e-40ca-83fa-21374c764369.txt";
//
        String url="http://117.71.57.47:10000/resource/uploadFiles/2018/02/10/f5170c0a-87b4-41b7-bb92-cb113f249e17.txt";
    String APKPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/baidutts.apk";
    String  yuurl="http://117.71.57.47:10000/resource/uploadFiles/baidutts.apk";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Button btn=(Button) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TxtReader.getTxtReader().
//                TxtReader.newInstance(TestActivity.this).openBookByUrl(url,TestActivity.this,"希望在天上");

                TxtReader.newInstance(TestActivity.this).setYuyinUrl(yuurl).setYuyinKey("9926863","QHNP9sN1WIQ5YueG7hOK2Gbg","4019c6f96bb0929a34671820fcf04f29").openBookByUrl(url,TestActivity.this);
//
//                TxtReader.getTxtReader().setYuYinPath(APKPATH).openBookByUrl(url,TestActivity.this);
            }
        });
    }






}
