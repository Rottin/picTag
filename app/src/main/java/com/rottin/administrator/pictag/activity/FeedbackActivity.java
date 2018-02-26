package com.rottin.administrator.pictag.activity;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rottin.administrator.pictag.R;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Calendar;

public class FeedbackActivity extends AppCompatActivity {

    private EditText contentEdittext;
    private Button sendButton;
    private String content = "", mUsername;
    private String info;
    private final static String HOST = "120.25.76.27", TAG = "FeedbackActivity";
    private final static int PORT = 1222;
    private TextView backText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mUsername = getIntent().getExtras().get("username").toString();
        backText = (TextView) findViewById(R.id.feedback_return);
        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(FeedbackActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent.putExtra("username",mUsername);
                //startActivity(intent);
                FeedbackActivity.this.finish();
            }
        });
        contentEdittext = (EditText) findViewById(R.id.feedback_content);
        sendButton = (Button) findViewById(R.id.feedback_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = contentEdittext.getText().toString().trim();
                if (content.equals("")) {
                    Snackbar.make(v, "反馈内容请不要为空", Snackbar.LENGTH_SHORT).show();
                } else {
                    Calendar calendar = Calendar.getInstance();
                    final int year = calendar.get(Calendar.YEAR);
                    final int month = calendar.get(Calendar.MONTH) + 1;
                    final int day = calendar.get(Calendar.DAY_OF_MONTH);
                    final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    final int minute = calendar.get(Calendar.MINUTE);
                    final int second = calendar.get(Calendar.SECOND);
                    //测试
                    Log.d(TAG, " " + year + month + day + hour + minute + second);

                    AsyncTask<Void, String, Void> read = new AsyncTask<Void, String, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            Socket socket = new Socket();
                            try {
                                socket.connect(new InetSocketAddress(HOST, PORT));
                                final BufferedWriter bw = new BufferedWriter(
                                        new OutputStreamWriter(socket.getOutputStream()));
                                bw.write("[FeedBack]" + mUsername + "," + year + "," + month + ","
                                        + day + "," + hour + "," + minute + "," + second + "\n");
                                bw.flush();
                                //Log.d(TAG,"第一次发送");
                                bw.write(content);
                                bw.flush();
                                //Log.d(TAG,"第二次发送");
                                publishProgress();
                                socket.close();
                            } catch (IOException e) {
//                                Log.d(TAG,"异常");
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onProgressUpdate(String... values) {
                            Toast.makeText(FeedbackActivity.this, "感谢反馈", Toast.LENGTH_SHORT).show();
                            // TODO: 2017/7/5 跳转回主界面
                            //Intent intent = new Intent(FeedbackActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            //intent.putExtra("username",mUsername);
                            //startActivity(intent);
                            FeedbackActivity.this.finish();
                            super.onProgressUpdate(values);
                        }
                    };
                    read.execute();
                }
            }
        });
    }
}
