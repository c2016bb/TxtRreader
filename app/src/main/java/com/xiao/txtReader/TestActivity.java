package com.xiao.txtReader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.txt.readerlibrary.ReadActivity;
import com.txt.readerlibrary.TxtReader;
import com.txt.readerlibrary.db.BookList;
import com.txt.readerlibrary.util.Fileutil;
import com.txt.readerlibrary.utils.AppConfig;
import com.txt.readerlibrary.utils.DownLoadFile;
import com.txt.readerlibrary.utils.LogUtils;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

        String url="http://dzs.qisuu.com/txt/1619.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Button btn=(Button) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TxtReader.getTxtReader().openBook(url,TestActivity.this);
            }
        });
    }






}
