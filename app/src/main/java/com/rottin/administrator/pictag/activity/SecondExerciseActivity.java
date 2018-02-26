package com.rottin.administrator.pictag.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rottin.administrator.pictag.HistoryDAO;
import com.rottin.administrator.pictag.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

public class SecondExerciseActivity extends AppCompatActivity {

    private ImageView img;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private EditText edit;
    private Button btn;
    private int count = 3,flag;
    BufferedReader reader;
    BufferedWriter writer;
    Socket socket;
    String picid;
    String[] newstr = new String[5];
    String[] str;
    int[] checkbox;
    private String url,mUsername;
    private HistoryDAO dao;
    private ProgressBar mProgressBar;
    private Bitmap bmp = null;
    private TextView page;
    private Bitmap nbmp;

    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    bmp = (Bitmap) msg.obj;
                    img.setImageBitmap(bmp);
                    mProgressBar.setVisibility(View.GONE);

                    str = new String[5];
                    for (int i = 1; i < newstr.length; i++) {
                        str[i] = newstr[i];
                    }
                    //Log.i("a", str[0]);
                    //Log.i("a", str[1]);
                    //Log.i("a", str[2]);
                    if (str[1] != null)
                        Log.i("a", "true");
                    if (str[1] != null)
                        checkBox1.setText(str[1]);
                    else{
                        checkBox1.setText("无");
                        checkBox1.setClickable(false);
                    }
                    if (str[2] != null)
                        checkBox2.setText(str[2]);
                    else{
                        checkBox2.setText("无");
                        checkBox2.setClickable(false);
                    }
                    if (str[3] != null)
                        checkBox3.setText(str[3]);
                    else{
                        checkBox3.setText("无");
                        checkBox3.setClickable(false);
                    }
                    if (str[4] != null)
                        checkBox4.setText(str[4]);
                    else{
                        checkBox4.setText("无");
                        checkBox4.setClickable(false);
                    }
                    break;
                case 0x555:
                    Toast.makeText(SecondExerciseActivity.this, "你没有选择或输入任何信息！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_exercise);

        img = (ImageView) findViewById(R.id.imageView1);
        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
        edit = (EditText) findViewById(R.id.editText1);
        btn = (Button) findViewById(R.id.button1);
        mProgressBar = (ProgressBar) findViewById(R.id.second_guess_progressbar);
        checkbox = new int[4];
        count = getIntent().getIntExtra("count2", 3);
        mUsername=getIntent().getStringExtra("username");
        page = (TextView) findViewById(R.id.page);
        page.setText(""+(count+5)+"/10");

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                send();
                if(checkbox[0]==0&&checkbox[1]==0&&checkbox[2]==0&&checkbox[3]==0&&edit.getText().toString().trim().length() == 0){
                    Toast.makeText(SecondExerciseActivity.this, "你没有选择或输入任何信息！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //数据写入本地历史记录数据库
                dao = new HistoryDAO(getBaseContext());
                String selected = "";
                for(int i=0;i<4;i++){
                    selected+= checkbox[i];
                }
                try {
                    dao.insert2(picid, str[1], str[2], str[3],str[4],selected,mUsername);
                }catch (NullPointerException e){
                    str = new String[5];
                    for(int i=0;i<=4;i++)
                        str[i]="";
                    dao.insert2(picid, str[1], str[2], str[3],str[4],selected,mUsername);
                }

                Log.d("SecondExercise","selected:"+selected);


                if(flag==0)
                    return;
                if (count < 3) {
                    Intent intent = new Intent(SecondExerciseActivity.this, SecondExerciseActivity.class);
                    intent.putExtra("count2", ++count);
                    intent.putExtra("username",mUsername);
                    startActivity(intent);
                } else if (count == 3) {
                    Intent intent = new Intent(SecondExerciseActivity.this, ThirdExerciseActivity.class);
                    intent.putExtra("count3", 0);
                    intent.putExtra("username",mUsername);
                    startActivity(intent);//finish?
                }
                if(bmp!=null&&!bmp.isRecycled()){
                    bmp.recycle() ;
                    bmp=null;
                    System.gc();
                }
                finish();
            }
        });

        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkbox[0] = 1;
                } else
                    checkbox[0] = 0;

            }
        });
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkbox[1] = 1;
                } else
                    checkbox[1] = 0;

            }
        });
        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkbox[2] = 1;
                } else
                    checkbox[2] = 0;

            }
        });
        checkBox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkbox[3] = 1;
                } else
                    checkbox[3] = 0;

            }
        });

        mProgressBar.setVisibility(View.VISIBLE);
        //新建线程加载图片信息，发送到消息队列中
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Bitmap bmp = getURLimage(url);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = bmp;
                System.out.println("000");
                handle.sendMessage(msg);

            }
        }).start();

    }
    //加载图片
    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            socket = new Socket("120.25.76.27", 1222);
            // 获得输入流
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
            bw.write("[GetQ2]\n");
            bw.flush();

            newstr = null;
            String str = br.readLine();
            Log.i("tag", str);
            newstr = str.split(",");

            picid = newstr[0].substring(4);
            socket.close();

            socket = new Socket("120.25.76.27", 1222);
            // 获得输入流
            br = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            bw = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
            bw.write("[GetPic]" + picid + ",p\n");
            bw.flush();

            url = br.readLine();
            url = "http://www.bi-home.cn/software/pic/" + url;

            //Log.i("tag", url);
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            if(bmp.getWidth()>4096||bmp.getHeight()>4096){
                nbmp = Bitmap.createBitmap(bmp,0,0,bmp.getWidth()/2,bmp.getHeight()/2);
                bmp.recycle();
                return  nbmp;
            }
            is.close();
        } catch (UnknownHostException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (NullPointerException e) {
            Message msg = new Message();
            msg.what = 0x122;
            msg.obj = "网络连接超时！！";
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return bmp;
    }

    protected void send() {
        flag=1;
        // TODO Auto-generated method stub
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Socket socket1;
                    socket1 = new Socket("120.25.76.27", 1222);
                    writer = new BufferedWriter(
                            new OutputStreamWriter(socket1.getOutputStream()));
                    reader = new BufferedReader(
                            new InputStreamReader(socket1.getInputStream()));
                    String sendstr = "[Submit]" + mUsername;
                    for (int i = 0; i < 4; i++) {
                        if (checkbox[i] == 1)
                            sendstr += "," + picid + "," + newstr[i + 1];
                    }

                    if ((sendstr == "[Submit]" + mUsername) && (edit.getText().toString().trim().length() == 0)) {
                        Message msg = new Message();
                        msg.what = 0x555;
                        handle.sendMessage(msg);
                        flag=0;
                        return;
                    } else if (edit.getText().toString().trim().length() != 0)
                        sendstr += "," + picid + "," + edit.getText().toString() + "\n";
                    else
                        sendstr += "\n";
                    //writer.write("[Submit]"+"username"+", "+"picid"+","+edit1.getText().toString()+"picid"+","+edit2.getText().toString()+"\n");
                    writer.write(sendstr);
                    writer.flush();
                    //Log.i("tag", reader.readLine());

                    writer.close();
                    socket1.close();
                    //edit.setText("");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }
        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO: 2017/7/6 中途终止测试
        if( event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(SecondExerciseActivity.this, MainActivity.class);
            intent.putExtra("username",mUsername);
            startActivity(intent);
            if(bmp!=null&&!bmp.isRecycled()){
                bmp.recycle() ;
                bmp=null;
                System.gc();
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


}
