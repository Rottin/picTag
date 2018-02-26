package com.rottin.administrator.pictag.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.rottin.administrator.pictag.HistoryDAO;
import com.rottin.administrator.pictag.R;

public class ThirdExerciseActivity extends AppCompatActivity {

    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private TextView text;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private Button btn;
    private int count = 1,flag;
    private ProgressBar mProgressBar1, mProgressBar2, mProgressBar3, mProgressBar4;
    BufferedReader reader;
    BufferedWriter writer;
    Socket socket;
    String picid;
    int[] checkbox;
    private String url,mUsername;
    String tag;
    String[] newstr;
    int length;
    String[] picids = new String[5];
    private HistoryDAO dao;
    private Bitmap bmp = null;
    private Bitmap bmp2= null;
    private Bitmap bmp3 = null;
    private Bitmap bmp4 = null;
    private TextView page;
    private Bitmap nbmp;


    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    System.out.println("111");
                    checkBox1.setVisibility(View.VISIBLE);
                    bmp = (Bitmap) msg.obj;
                    img1.setImageBitmap(bmp);
                    mProgressBar1.setVisibility(View.GONE);
                    mProgressBar2.setVisibility(View.GONE);
                    mProgressBar3.setVisibility(View.GONE);
                    mProgressBar4.setVisibility(View.GONE);
                    text.setText(tag);
                    break;
                case 2:
                    System.out.println("222");
                    checkBox2.setVisibility(View.VISIBLE);
                    bmp2 = (Bitmap) msg.obj;
                    img2.setImageBitmap(bmp2);
                    break;
                case 3:
                    System.out.println("333");
                    checkBox3.setVisibility(View.VISIBLE);
                    bmp3 = (Bitmap) msg.obj;
                    img3.setImageBitmap(bmp3);
                    break;
                case 4:
                    System.out.println("444");
                    checkBox4.setVisibility(View.VISIBLE);
                    bmp4 = (Bitmap) msg.obj;
                    img4.setImageBitmap(bmp4);
                    break;
                case 0x555:
                    Toast.makeText(ThirdExerciseActivity.this, "你没有选择任何选项！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_exercise);

        img1 = (ImageView) findViewById(R.id.imageView1);
        img2 = (ImageView) findViewById(R.id.imageView2);
        img3 = (ImageView) findViewById(R.id.imageView3);
        img4 = (ImageView) findViewById(R.id.imageView4);
        text = (TextView) findViewById(R.id.textView2);
        btn = (Button) findViewById(R.id.button1);
        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
        checkBox1.clearAnimation();
        checkBox2.clearAnimation();
        checkBox3.clearAnimation();
        checkBox4.clearAnimation();
        checkBox1.setVisibility(View.GONE);
        checkBox2.setVisibility(View.GONE);
        checkBox3.setVisibility(View.GONE);
        checkBox4.setVisibility(View.GONE);
        mProgressBar1 = (ProgressBar) findViewById(R.id.Third_guess_progressbar1);
        mProgressBar2 = (ProgressBar) findViewById(R.id.Third_guess_progressbar2);
        mProgressBar3 = (ProgressBar) findViewById(R.id.Third_guess_progressbar3);
        mProgressBar4 = (ProgressBar) findViewById(R.id.Third_guess_progressbar4);

        checkbox = new int[4];

        count = getIntent().getIntExtra("count3", 1);
        mUsername=getIntent().getStringExtra("username");
        page = (TextView) findViewById(R.id.page);
        page.setText(""+(count+9)+"/10");

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                send();
                if(checkbox[0]==0&&checkbox[1]==0&&checkbox[2]==0&&checkbox[3]==0){
                    Toast.makeText(ThirdExerciseActivity.this, "你没有选择任何选项！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //数据写入本地历史记录数据库
                dao = new HistoryDAO(getBaseContext());
                String selected = "";
                for(int i=0;i<4;i++){
                    selected+= checkbox[i];
                }
                try {
                    dao.insert3(picids[0],picids[1],picids[2],picids[3],tag,selected,mUsername);
                }catch (NullPointerException e){
                    picids = new String[4];
                    for(int i=0;i<=3;i++)
                        picids[i]="";
                    dao.insert3(picids[0],picids[1],picids[2],picids[3],tag,selected,mUsername);
                }
                Log.d("ThirdExercise","selected:"+selected);


                if(flag==0)
                    return;
                if (count < 1) {
                    Intent intent = new Intent(ThirdExerciseActivity.this, ThirdExerciseActivity.class);
                    intent.putExtra("count3", ++count);
                    intent.putExtra("username",mUsername);
                    startActivity(intent);
                    Snackbar.make(v, "你完成了一组测试", Snackbar.LENGTH_SHORT).show();
                    if(bmp!=null&&!bmp.isRecycled()){
                        bmp.recycle();
                        bmp=null;
                    }
                    if(bmp2!=null&&!bmp2.isRecycled()){
                        bmp2.recycle();
                        bmp2 = null;
                    }
                    if(bmp3!=null&&!bmp3.isRecycled()){
                        bmp3.recycle();
                        bmp3 = null;
                    }
                    if(bmp4!=null&&!bmp4.isRecycled()){
                        bmp4.recycle();
                        bmp4 = null;
                    }
                    System.gc();
                    finish();
                } else if (count == 1) {
                    Toast.makeText(ThirdExerciseActivity.this, "你完成了一组测试", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ThirdExerciseActivity.this, MainActivity.class);
                    intent.putExtra("username", mUsername);
                    startActivity(intent);
                    if(bmp!=null&&!bmp.isRecycled()){
                        bmp.recycle();
                        bmp=null;
                    }
                    if(bmp2!=null&&!bmp2.isRecycled()){
                        bmp2.recycle();
                        bmp2 = null;
                    }
                    if(bmp3!=null&&!bmp3.isRecycled()){
                        bmp3.recycle();
                        bmp3 = null;
                    }
                    if(bmp4!=null&&!bmp4.isRecycled()){
                        bmp4.recycle();
                        bmp4 = null;
                    }
                    System.gc();
                    finish();
                }
            }
        });

        checkBox1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkbox[0] = 1;
                } else
                    checkbox[0] = 0;

            }
        });
        checkBox2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkbox[1] = 1;
                } else
                    checkbox[1] = 0;

            }
        });
        checkBox3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkbox[2] = 1;
                } else
                    checkbox[2] = 0;

            }
        });
        checkBox4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkbox[3] = 1;
                } else
                    checkbox[3] = 0;

            }
        });

        mProgressBar1.setVisibility(View.VISIBLE);
        mProgressBar2.setVisibility(View.VISIBLE);
        mProgressBar3.setVisibility(View.VISIBLE);
        mProgressBar4.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    socket = new Socket("120.25.76.27", 1222);
                    // 获得输入流
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    BufferedWriter bw = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream()));
                    bw.write("[GetQ3]\n");
                    bw.flush();

                    newstr = null;
                    String str = br.readLine();
                    Log.i("tag", str);
                    newstr = str.split(",");

                    tag = newstr[0].substring(4);

                    length = newstr.length;
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Log.i("length", "" + length);
                for (int i = 1; i < length; i++) {
                    Bitmap bmp = getURLimage(url, i);
                    Message msg = new Message();
                    msg.what = i;
                    msg.obj = bmp;
                    System.out.println("000");
                    handle.sendMessage(msg);
                }
            }
        }).start();
    }

    //加载图片
    public Bitmap getURLimage(String url, int i) {
        //Socket socket = new Socket();

        Bitmap bmp = null;
        Log.d("Third", "getURLImageing...");
        try {
            Log.d("Third", "trying...,i:"+i+",newstr[i]:"+newstr[i]);
            picid = newstr[i];
            picids[i-1]=picid;
            Log.d("Third", "picid:"+picid);
//            socket.close();

            socket = new Socket("120.25.76.27", 1222);
            // 获得输入流
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
            bw.write("[GetPic]" + picid + ",p\n");
            bw.flush();

            url = br.readLine();
            url = "http://www.bi-home.cn/software/pic/" + url;

            Log.d("Third", "获取到url："+url);
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
            url = "";
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
//				public boolean receive(int i){
//					 try {
//
//
//			        } catch (SocketTimeoutException e){
//			        	Message msg = new Message();
//			        	msg.what = 0x123;
//			        	msg.obj  = "网络连接超时！！";
//			        } catch(IndexOutOfBoundsException x){
//			        	Message msg = new Message();
//			        	msg.what = 0x122;
//			        	msg.obj  = "网络连接超时！！";
//			        }
//					 catch (IOException e) {
//			            e.printStackTrace();
//			        }
//					 return false;
//				}

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
                            sendstr += "," + newstr[i + 1] + "," + tag;
                    }


                    if (sendstr == "[Submit]" + mUsername) {
                        Message msg = new Message();
                        msg.what = 0x555;
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
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(ThirdExerciseActivity.this, MainActivity.class);
            intent.putExtra("username",mUsername);
            startActivity(intent);
            if(bmp!=null&&!bmp.isRecycled()){
                bmp.recycle();
                bmp=null;
            }
            if(bmp2!=null&&!bmp2.isRecycled()){
                bmp2.recycle();
                bmp2 = null;
            }
            if(bmp3!=null&&!bmp3.isRecycled()){
                bmp3.recycle();
                bmp3 = null;
            }
            if(bmp4!=null&&!bmp4.isRecycled()){
                bmp4.recycle();
                bmp4 = null;
            }
            System.gc();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
