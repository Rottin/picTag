package com.rottin.administrator.pictag.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.net.InetSocketAddress;
import java.net.Socket;

public class UserDetailActivity extends AppCompatActivity {

    private Button confirmButton;
    private EditText usernameEt, goalEt, priceEt, addressEt, emailEt, phoneNumberEt, passwordEt, passwordConfirmEt;
    private TextView returnText, editText;
    private String info, info1,username, email, password, phone, passwordConfirm,
            address, photoid, mark, goal, oldpassword;
    final private static String TAG ="UserDetailActivity";
    final private static String HOST="120.25.76.27";
    final private static int PORT=1222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        returnText = (TextView) findViewById(R.id.userdetail_return);
        editText = (TextView) findViewById(R.id.userdetail_edit);
        usernameEt = (EditText) findViewById(R.id.userdetail_username);
        goalEt = (EditText) findViewById(R.id.userdetail_goal);
        priceEt = (EditText) findViewById(R.id.userdetail_price);
        addressEt = (EditText) findViewById(R.id.userdetail_address);
        emailEt = (EditText) findViewById(R.id.userdetail_email);
        phoneNumberEt = (EditText) findViewById(R.id.userdetail_phone);
        passwordEt = (EditText) findViewById(R.id.userdetail_password);
        passwordConfirmEt = (EditText) findViewById(R.id.userdetail_password_confirm);
        confirmButton = (Button) findViewById(R.id.userdetail_button);
        confirmButton.setVisibility(View.INVISIBLE);
        passwordConfirmEt.setVisibility(View.INVISIBLE);
        usernameEt.setEnabled(false);
        goalEt.setEnabled(false);
        priceEt.setEnabled(false);
        addressEt.setEnabled(false);
        emailEt.setEnabled(false);
        phoneNumberEt.setEnabled(false);
        passwordEt.setEnabled(false);
        //初始化
        username=getIntent().getExtras().get("username").toString();
        getDetails();

        returnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordConfirmEt.setVisibility(View.VISIBLE);
                confirmButton.setVisibility(View.VISIBLE);
                //usernameEt.setEnabled(true);
                addressEt.setEnabled(true);
                emailEt.setEnabled(true);
                phoneNumberEt.setEnabled(true);
                passwordEt.setEnabled(true);
                passwordEt.setText("");
                passwordEt.setHint("若要修改密码，请输入原密码");
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameEt.getText().toString();
                address = addressEt.getText().toString();
                email = emailEt.getText().toString();
                phone = phoneNumberEt.getText().toString();
                passwordConfirm = passwordConfirmEt.getText().toString();
                password = passwordEt.getText().toString();
                //如果新密码为空，则不需修改密码
                if(!(passwordConfirm.equals("")||passwordConfirm==null)){
                    password = passwordEt.getText().toString();
                    passwordConfirm = passwordConfirmEt.getText().toString();
                }
                attemptUpdate();
            }
        });
    }

    private void getDetails() {

        AsyncTask<Void, String, Void> read = new AsyncTask<Void, String, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Socket socket = new Socket();
                Message msg = new Message();
                info1="0";

                try {
                    socket.connect(new InetSocketAddress(HOST, PORT));
                    // 获得输入流
                    final BufferedReader br = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    final BufferedWriter bw = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream()));

                    bw.write("[Userinfo]" + username + "\n");
                    bw.flush();
                    String details = br.readLine();
                    String infos[]=details.split(",");
                    email = infos[1];
                    phone = infos[2];
                    password = infos[3];
                    address=infos[4];
                    photoid=infos[5];
                    mark=infos[6];
                    goal=infos[7];
                    passwordConfirm = password;
                    oldpassword = password;
                    Log.d(TAG,"获取用户信息："+"username:"+username+"email:"+email+"phone:"+phone+"password:"+password
                            +"address:"+address+"photoid:"+photoid+"mark:"+mark+"goal:"+goal);
                    info1="1";
                    publishProgress(info1);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG,"获取用户数据异常");
                    info1="0";
                    publishProgress(info1);
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                if(info1.equals("1")) {
                    usernameEt.setText(username);
                    goalEt.setText(goal);
                    priceEt.setText(mark);
                    addressEt.setText(address);
                    emailEt.setText(email);
                    phoneNumberEt.setText(phone);
                    passwordEt.setText(password);
                }
                else {
                    Toast.makeText(UserDetailActivity.this,"获取用户信息失败，请检查网络",Toast.LENGTH_SHORT).show();
                }
                super.onProgressUpdate(values);
            }
        };
        read.execute();
    }

    private void attemptUpdate() {

        if(!username.matches("[\\_a-zA-Z0-9u4e00-u9fa5]*")){
            Toast.makeText(UserDetailActivity.this, "用户名格式不正确",Toast.LENGTH_SHORT).show();
            usernameEt.setText("");
            usernameEt.requestFocus();
            usernameEt.setHint("用户名不允许出现特殊符号");
            return;
        }else if (username.length()<2||username.length()>20){
            Toast.makeText(UserDetailActivity.this, "用户名长度错误",Toast.LENGTH_SHORT).show();
            usernameEt.setText("");
            usernameEt.requestFocus();
            usernameEt.setHint("用户名长度范围为2-20");
            return;
        }else if(!email.matches("\\w+@\\w+\\.\\w+")){
            Toast.makeText(UserDetailActivity.this, "邮箱格式不正确",Toast.LENGTH_SHORT).show();
            emailEt.setText("");
            emailEt.requestFocus();
            emailEt.setHint("请输入正确的邮箱");
            return;
        }else if(phone.length()!=11){
            Toast.makeText(UserDetailActivity.this, "手机号格式不正确",Toast.LENGTH_SHORT).show();
            phoneNumberEt.setText("");
            phoneNumberEt.requestFocus();
            phoneNumberEt.setHint("请输入中国大陆11位手机号");
            return;
        }else if(password.length()<6||password.length()>16) {
            Toast.makeText(UserDetailActivity.this, "密码长度错误", Toast.LENGTH_SHORT).show();
            passwordEt.setText("");
            passwordEt.requestFocus();
            passwordEt.setHint("密码长度范围为6-16");
            return;
        }else if(passwordConfirm.length()<6||passwordConfirm.length()>16) {
            Toast.makeText(UserDetailActivity.this, "密码长度错误", Toast.LENGTH_SHORT).show();
            passwordConfirmEt.setText("");
            passwordConfirmEt.requestFocus();
            passwordConfirmEt.setHint("密码长度范围为6-16");
            return;
        }else if(!password.equals(oldpassword)){
            Toast.makeText(UserDetailActivity.this, "原密码不正确",Toast.LENGTH_SHORT).show();
            passwordEt.setText("");
            passwordEt.requestFocus();
            passwordEt.setHint("请输入原密码以修改密码");
            return;
        }

        AsyncTask<Void, String, Void> read = new AsyncTask<Void, String, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Socket socket = new Socket();
                Message msg = new Message();
                info="0";

                try {
                    socket.connect(new InetSocketAddress(HOST, PORT));
                    // 获得输入流
                    final BufferedReader br = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    final BufferedWriter bw = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream()));

                    bw.write("[UpdateUinfo]" + username +","+ email +","+ phone
                            +","+ passwordConfirm + ","+address +","+ photoid +","+ mark + ","+goal + "\n");
                    bw.flush();
                    info = br.readLine();
                    publishProgress(info);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                if (info == null) {
                    //Toast.makeText(SignupActivity.this, username, Toast.LENGTH_LONG).show();
                    Toast.makeText(UserDetailActivity.this, "更新失败，请检查网络", Toast.LENGTH_LONG).show();
                } else if (info.equals("1")) {
                    Toast.makeText(UserDetailActivity.this, "更新成功", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(UserDetailActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("username",username);
                    startActivity(intent);
                    UserDetailActivity.this.finish();
                    //System.out.println("成功");
                } else if (info.equals("0")) {
                    Toast.makeText(UserDetailActivity.this, "更新失败，请重试", Toast.LENGTH_LONG).show();

                    //System.out.println("User Name has been used!");
                } else {
                    Toast.makeText(UserDetailActivity.this, "服务器异常，请重试！", Toast.LENGTH_LONG).show();
                    //System.out.println("服务器异常");
                }
                super.onProgressUpdate(values);
            }
        };
        read.execute();
    }
}
