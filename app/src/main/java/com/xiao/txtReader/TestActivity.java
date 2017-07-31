package com.xiao.txtReader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.txt.readerlibrary.TxtReader;

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
                TxtReader.getTxtReader().openBookByUrl(url,TestActivity.this);
            }
        });
    }






}
