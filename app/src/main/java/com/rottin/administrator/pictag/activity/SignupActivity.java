package com.rottin.administrator.pictag.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rottin.administrator.pictag.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SignupActivity extends AppCompatActivity {

    private String username, email, phoneNumber, password, passwordConfirm, info;
    private EditText usernameEt, emailEt, phoneNumberEt, passwordEt, passwordConfirmEt;
    private Button signupButton;
    private ProgressBar progressBar;
    private static final String HOST = "120.25.76.27";
    private static final int PORT = 1222;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.rgb(63, 81, 181));
        }
        initControl();
    signupButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                connect();
           }
        });
    }

    private void initControl() {
        usernameEt = (EditText) findViewById(R.id.signup_username);
        emailEt = (EditText) findViewById(R.id.signup_email);
        phoneNumberEt = (EditText) findViewById(R.id.signup_phone);
        passwordEt = (EditText) findViewById(R.id.signup_password);
        passwordConfirmEt = (EditText) findViewById(R.id.signup_password_confirm);
        signupButton = (Button) findViewById(R.id.signup_button);
        progressBar=(ProgressBar) findViewById(R.id.signup_progress);
    }

    public void connect(){

        username = usernameEt.getText().toString();
        email = emailEt.getText().toString();
        phoneNumber = phoneNumberEt.getText().toString();
        password = passwordEt.getText().toString();
        passwordConfirm = passwordConfirmEt.getText().toString();
        int flag = 1;
        //使用while会死循环
            if(!username.matches("[\\_a-zA-Z0-9u4e00-u9fa5]*")){
                Toast.makeText(SignupActivity.this, "用户名格式不正确",Toast.LENGTH_SHORT).show();
                usernameEt.setText("");
                usernameEt.requestFocus();
                usernameEt.setHint("用户名不允许出现特殊符号");
                return;
            }else if (username.length()<2||username.length()>20){
                Toast.makeText(SignupActivity.this, "用户名长度错误",Toast.LENGTH_SHORT).show();
                usernameEt.setText("");
                usernameEt.requestFocus();
                usernameEt.setHint("用户名长度范围为2-20");
                return;
            }else if(!email.matches("\\w+@\\w+\\.\\w+")){
                Toast.makeText(SignupActivity.this, "邮箱格式不正确",Toast.LENGTH_SHORT).show();
                emailEt.setText("");
                emailEt.requestFocus();
                emailEt.setHint("请输入正确的邮箱");
                return;
            }else if(phoneNumber.length()!=11){
                Toast.makeText(SignupActivity.this, "手机号格式不正确",Toast.LENGTH_SHORT).show();
                phoneNumberEt.setText("");
                phoneNumberEt.requestFocus();
                phoneNumberEt.setHint("请输入中国大陆11位手机号");
                return;
            }else if(password.length()<6||password.length()>16) {
                Toast.makeText(SignupActivity.this, "密码长度错误", Toast.LENGTH_SHORT).show();
                passwordEt.setText("");
                passwordEt.requestFocus();
                passwordEt.setHint("密码长度范围为6-16");
                return;
            }
            else if (password.equals(passwordConfirm) == false) {
                Toast.makeText(SignupActivity.this, "两次密码输入不一致！", Toast.LENGTH_LONG).show();
                passwordConfirmEt.setText("");
                passwordConfirmEt.requestFocus();
                passwordConfirmEt.setHint("请重复输入密码");
                return;
            }else
                flag = 0;

            progressBar.setVisibility(View.VISIBLE);

            AsyncTask<Void, String, Void>  read = new AsyncTask<Void, String, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    Socket socket = new Socket();
                    try {
                        socket.connect(new InetSocketAddress(HOST, PORT));
                        // 获得输入流
                        final BufferedReader br = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));
                        final BufferedWriter bw = new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream()));
                        bw.write("[Register]"+username+","+email+","+phoneNumber+","+password+"\n");
                        bw.flush();
                        info = br.readLine();
                        //info =null;
                        publishProgress(info);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                    if(info == null){
                        //Toast.makeText(SignupActivity.this, username, Toast.LENGTH_LONG).show();
                        Toast.makeText(SignupActivity.this, "服务器异常！", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }else if(info.contains("[A]")) {
                        Toast.makeText(SignupActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignupActivity.this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("username",username);
                        startActivity(intent);
                        finish();
                        //System.out.println("成功");
                    }else if(info.contains("[D]")) {
                        Toast.makeText(SignupActivity.this, info+",please try again", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        //System.out.println("User Name has been used!");
                    }
                    else {
                        Toast.makeText(SignupActivity.this, "服务器异常，请重试！", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        //System.out.println("服务器异常");
                    }
                    super.onProgressUpdate(values);
                }
            };
            read.execute();
    }
}


