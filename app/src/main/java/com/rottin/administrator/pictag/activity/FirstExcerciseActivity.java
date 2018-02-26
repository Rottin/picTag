package com.rottin.administrator.pictag.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SyncStatusObserver;
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
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

public class FirstExcerciseActivity extends AppCompatActivity {

    private ImageView img;
    private EditText edit1;
    private EditText edit2;
    private EditText edit3;
    private EditText edit4;
    private Button btn;
    private String mUsername;
    private Thread t1;
    private int count = 3,flag;
    BufferedReader reader;
    BufferedWriter writer;
    Socket socket;
    String picid;
    private ProgressBar mProgressBar;
    //private String url = "http://www.bi-home.cn/software/pic/1.jpg";
    private String url;
    //int picid = 1;
    private HistoryDAO dao;
    private Bitmap bmp = null;
    private TextView page;
    private Bitmap nbmp;
    private String sendstr;

    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    System.out.println("111");
                    bmp = (Bitmap) msg.obj;
                    img.setImageBitmap(bmp);
                    mProgressBar.setVisibility(View.GONE);

                    break;
                case 0x555:
                    Toast.makeText(FirstExcerciseActivity.this, "你没有输入任何信息！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_excercise);

        img = (ImageView) findViewById(R.id.imageView1);
        edit1 = (EditText) findViewById(R.id.editText1);
        edit2 = (EditText) findViewById(R.id.editText2);
        edit3 = (EditText) findViewById(R.id.editText3);
        edit4 = (EditText) findViewById(R.id.editText4);
        btn = (Button) findViewById(R.id.first_button1);
        mProgressBar = (ProgressBar) findViewById(R.id.first_guess_progressbar);
        page = (TextView) findViewById(R.id.page);


        count = getIntent().getIntExtra("count1", 3);
        mUsername=getIntent().getStringExtra("username");

        page.setText(""+(count+1)+"/10");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                send();
                if(edit1.getText().toString().trim().length()==0&&edit2.getText().toString().trim().length()==0&&edit3.getText().toString().trim().length()==0&&edit4.getText().toString().trim().length()==0){
                    Toast.makeText(FirstExcerciseActivity.this, "你没有输入任何信息！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //数据写入本地历史记录数据库
                dao = new HistoryDAO(getBaseContext());
                dao.insert1(picid, edit1.getText().toString(), edit2.getText().toString(),
                        edit3.getText().toString(), edit4.getText().toString(), mUsername);


                if(flag==0)
                    return;
                if (count < 3) {
                    Intent intent = new Intent(FirstExcerciseActivity.this, FirstExcerciseActivity.class);
                    intent.putExtra("count1", ++count);
                    intent.putExtra("username",mUsername);
                    startActivity(intent);
                } else if (count == 3) {
                    Intent intent = new Intent(FirstExcerciseActivity.this, SecondExerciseActivity.class);
                    intent.putExtra("count2", 0);
                    intent.putExtra("username",mUsername);
                    startActivity(intent);
                }
                //handle.removeCallbacks(Runnable);
                if(bmp!=null&&!bmp.isRecycled()){
                    bmp.recycle() ;
                    bmp=null;
                    System.gc();
                }
                finish();
            }
        });
        //新建线程加载图片信息，发送到消息队列中
         new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                mProgressBar.setVisibility(View.VISIBLE);
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
            bw.write("[GetQ1]\n");
            bw.flush();
            picid = br.readLine().substring(4);
            Log.i("tag", picid);
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
//            if(BitmapFactory.decodeStream(is).getHeight()>4096||BitmapFactory.decodeStream(is).getWidth()>4096){
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 2;
//                bmp = BitmapFactory.decodeStream(is, null,options);
//            }else{
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

    public void send() {
        flag=1;

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
                    sendstr = "[Submit]" + mUsername;
                    if (edit1.getText().toString().trim().length() != 0)
                        sendstr += "," + picid + "," + edit1.getText().toString();
                    if (edit2.getText().toString().trim().length() != 0)
                        sendstr += "," + picid + "," + edit2.getText().toString();
                    if (edit3.getText().toString().trim().length() != 0)
                        sendstr += "," + picid + "," + edit3.getText().toString();
                    if (edit4.getText().toString().trim().length() != 0)
                        sendstr += "," + picid + "," + edit4.getText().toString();
                    if (sendstr.equals("[Submit]" + mUsername)) {
                        Message msg=new Message();
                        msg.what=0x555;
                        handle.sendMessage(msg);
                        flag=0;
                        return;
                    } else
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
//		String sendstr  = edit.getText().toString();
//		//edit.setText("");
//		socketThread.Send(sendstr);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(FirstExcerciseActivity.this, MainActivity.class);
            intent.putExtra("username",mUsername);
            startActivity(intent);
//            switch (count){
//                case 0:
//                    Intent intent = new Intent(FirstExcerciseActivity.this, FirstExcerciseActivity.class);
//                    intent.putExtra("username",mUsername);
//                    startActivity(intent);
//                    break;
//                case 1:
//                    intent = new Intent(FirstExcerciseActivity.this, FirstExcerciseActivity.class);
//                    intent.putExtra("count1", --count);
//                    intent.putExtra("username",mUsername);
//                    startActivity(intent);
//                    break;
//                case 2:
//                    intent = new Intent(FirstExcerciseActivity.this, FirstExcerciseActivity.class);
//                    intent.putExtra("count1", --count);
//                    intent.putExtra("username",mUsername);
//                    startActivity(intent);
//                    break;
//                case 3:
//                    intent = new Intent(FirstExcerciseActivity.this, FirstExcerciseActivity.class);
//                    intent.putExtra("count1", --count);
//                    intent.putExtra("username",mUsername);
//                    startActivity(intent);
//                    break;
//
//            }
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


