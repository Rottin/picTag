package com.rottin.administrator.pictag;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class SignupActivity extends AppCompatActivity {

    private String username, email, phoneNumber, password, passwordConfirm;
    private EditText usernameEt, emailEt, phoneNumberEt, passwordEt, passwordConfirmEt;
    private Button signupButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.rgb(63, 81, 181));
        }
        usernameEt = (EditText) findViewById(R.id.signup_username);
        emailEt = (EditText) findViewById(R.id.signup_email);
        phoneNumberEt = (EditText) findViewById(R.id.signup_phone);
        passwordEt = (EditText) findViewById(R.id.signup_password);
        passwordConfirmEt = (EditText) findViewById(R.id.signup_password_confirm);
        
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
