package com.rottin.administrator.pictag.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.rottin.administrator.pictag.R;

public class AboutActivity extends AppCompatActivity {

    private TextView return_text;
    private String mUsername;
    private Button checkUpdateButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        if(Build.VERSION.SDK_INT >= 21){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#3140a0"));
        }
        mUsername=getIntent().getExtras().get("username").toString();
        return_text=(TextView) findViewById(R.id.about_return);
        checkUpdateButton = (Button)findViewById(R.id.checkUpdate);
        checkUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread();
                try {
                    thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Snackbar.make(v,"已是最新版本！",Snackbar.LENGTH_SHORT).show();
            }
        });
        return_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(AboutActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent.putExtra("username",mUsername);
                //startActivity(intent);
                AboutActivity.this.finish();
            }
        });
    }
}
