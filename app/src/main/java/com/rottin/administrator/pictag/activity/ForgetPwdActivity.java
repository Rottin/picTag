package com.rottin.administrator.pictag.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rottin.administrator.pictag.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ForgetPwdActivity extends AppCompatActivity {

    private Button FindPwd;
    private EditText mEmail, mUserName, mPassword;
    private Animation shake;
    private TextView back;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(ForgetPwdActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(ForgetPwdActivity.this,"信息有误",Toast.LENGTH_SHORT).show();
                    break;

            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        FindPwd = (Button) findViewById(R.id.foget_button);
        mEmail = (EditText) findViewById(R.id.forget_email);
        mPassword = (EditText) findViewById(R.id.forget_password);
        mUserName = (EditText) findViewById(R.id.forget_username);
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        back=(TextView)findViewById(R.id.forgetpwd_return);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetPwdActivity.this,LoginActivity.class);
                startActivity(intent);
                ForgetPwdActivity.this.finish();
            }
        });
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
                }else if (mUserName.getText().toString().equals("")) {
                    mUserName.startAnimation(shake);
                    mUserName.setHint("请输入用户名！");
                }else if(mPassword.getText().toString().equals("")){
                    mPassword.startAnimation(shake);
                    mPassword.setHint("请输入密码！");
                }else{
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            try {
                                Socket socket = new Socket("120.25.76.27", 1222);
                                // 获得输入流
                                BufferedReader br = new BufferedReader(
                                        new InputStreamReader(socket.getInputStream()));
                                BufferedWriter bw = new BufferedWriter(
                                        new OutputStreamWriter(socket.getOutputStream()));
                                bw.write("[Userinfo]"+mUserName.getText().toString()+"\n");
                                bw.flush();
                                Log.i("tag", "[Userinfo]"+mUserName.getText().toString()+"\n");

                                String[] newstr = null;
                                newstr = br.readLine().split(",");

                                bw.close();
                                br.close();
                                socket.close();
                                if(newstr[1].equals(mEmail.getText().toString())){
                                    socket = new Socket("120.25.76.27", 1222);
                                    // 获得输入流
                                    br = new BufferedReader(
                                            new InputStreamReader(socket.getInputStream()));
                                    bw = new BufferedWriter(
                                            new OutputStreamWriter(socket.getOutputStream()));
                                    bw.write("[UpdateUinfo]"+mUserName.getText().toString()+","+newstr[1]+","+newstr[2]+","+mPassword.getText().toString()+","+newstr[4]+","+newstr[5]+","+newstr[6]+","+newstr[7]+"\n");
                                    bw.flush();
                                    Log.i("tag", "[UpdateUinfo]"+mUserName.getText().toString()+","+newstr[1]+","+newstr[2]+","+mPassword.getText().toString()+","+newstr[4]+","+newstr[5]+","+newstr[6]+","+newstr[7]+"\n");

                                    bw.close();
                                    br.close();
                                    socket.close();
                                    Message msg = new Message();
                                    msg.what = 0;
                                    handler.sendMessage(msg);

                                }else{
                                    Message msg = new Message();
                                    msg.what = 1;
                                    handler.sendMessage(msg);
                                }




                            } catch (UnknownHostException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ForgetPwdActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
}
