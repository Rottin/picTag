package com.rottin.administrator.pictag;

import android.content.Intent;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

public class ForgetPwdActivity extends AppCompatActivity {

    private Button FindPwd;
    private EditText mEmail;
    private Animation shake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        FindPwd = (Button) findViewById(R.id.foget_button);
        mEmail = (EditText) findViewById(R.id.forget_email);
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        FindPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                //邮箱为空
                if (TextUtils.isEmpty(email)) {
                    mEmail.startAnimation(shake);
                    mEmail.setHint("请输入注册邮箱！");
                } else if (!isEmailValid(email)) {
                    mEmail.startAnimation(shake);
                    mEmail.setText("");
                    mEmail.setHint("邮箱格式不正确！");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ForgetPwdActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
}
