package com.rottin.administrator.pictag.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.rottin.administrator.pictag.R;

public class MallActivity extends AppCompatActivity {

    ImageView imgMall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall);
        imgMall = (ImageView)findViewById(R.id.imgMall);
        imgMall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
