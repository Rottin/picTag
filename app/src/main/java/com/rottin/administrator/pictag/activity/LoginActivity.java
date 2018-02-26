package com.rottin.administrator.pictag.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    //    private UserLoginTask mAuthTask = null;
    private final static String TAG = "LoginActivity";

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private TextView forgetPwd, msignUp;
    private View mProgressView;
    private View mLoginFormView;
    private String info, username, password;
    private ProgressBar mProgressbar;
    final private static String HOST="120.25.76.27";
    final private static int PORT=1222;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
// TODO: 2017/5/15 记住密码
        // TODO: 2017/5/25  网络状态检测
        //设置透明状态栏
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        //populateAutoComplete();

        forgetPwd = (TextView) findViewById(R.id.login_forget_text);
        msignUp = (TextView) findViewById(R.id.login_signup_text);
        forgetPwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPwdActivity.class);
                startActivity(intent);

            }
        });
        msignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                }
                attemptLogin();
            }
        });
        mProgressbar = (ProgressBar)findViewById(R.id.login_progressbar);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
//        if (mAuthTask != null) {
//            return;
//        }
        username = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
//        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(null);
            focusView = mEmailView;
            cancel = true;
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            mEmailView.startAnimation(shake);
            mEmailView.setText("");
            mEmailView.setHint("请输入用户名");
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(null);
            focusView = mPasswordView;
            cancel = true;
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            mPasswordView.startAnimation(shake);
            mPasswordView.setText("");
            mPasswordView.setHint("请输入密码");
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            // showProgress
            mProgressbar.setVisibility(View.VISIBLE);


            AsyncTask<Void, String, Void> read = new AsyncTask<Void, String, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    Socket socket = new Socket();

                    try {
                        Log.d(TAG, "尝试获取数据");
                        socket.connect(new InetSocketAddress(HOST, PORT));
                        // 获得输入流
                        final BufferedReader br = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));
                        final BufferedWriter bw = new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream()));

                        bw.write("[Login]" + username + "" + "," + password + "" + "\n");
                        bw.flush();
//                        Log.d(TAG,"获取到数据："+br.toString());
                        info = br.readLine();
                        publishProgress(info);
                    } catch (IOException e) {
                        // TODO: 2017/5/26 测试阶段先置为1，可以任意登录，实际应为0
                        Log.d(TAG,"异常");
                        info = "0";
                        publishProgress(info);
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                    Log.d(TAG, "判断返回数据"+info);

                    if (info == null) {
                        //Toast.makeText(SignupActivity.this, username, Toast.LENGTH_LONG).show();
                        Toast.makeText(LoginActivity.this, "登录失败，请检查网络连接或稍后再试", Toast.LENGTH_LONG).show();
                        mProgressbar.setVisibility(View.GONE);
                        return;
                    } else if (info.equals("1")) {
                        Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("username",username);
                        startActivity(intent);
                        LoginActivity.this.finish();
                        //System.out.println("成功");
                    } else if (info.equals("0")) {
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                        mProgressbar.setVisibility(View.GONE);
                        return;
                        //System.out.println("User Name has been used!");
                    } else {
                        Toast.makeText(LoginActivity.this, "服务器异常，请重试！", Toast.LENGTH_LONG).show();
                        mProgressbar.setVisibility(View.GONE);
                        return;
                        //System.out.println("服务器异常");
                    }
                    super.onProgressUpdate(values);
                }
            };
            read.execute();
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    /*public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            //showProgress(false);

            Log.d(TAG, "登录");
            if (success) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                mPasswordView.setError(null);
                mPasswordView.requestFocus();
                Animation shake = AnimationUtils.loadAnimation(getBaseContext(), R.anim.shake);
                mPasswordView.startAnimation(shake);
                mPasswordView.setText("");
                mPasswordView.setHint("密码错误！");
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            //showProgress(false);
        }
    }*/
}

