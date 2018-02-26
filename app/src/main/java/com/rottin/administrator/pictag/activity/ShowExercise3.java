package com.rottin.administrator.pictag.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rottin.administrator.pictag.HistoryDAO;
import com.rottin.administrator.pictag.R;
import com.rottin.administrator.pictag.domain.Exercise3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

public class ShowExercise3 extends AppCompatActivity {

    private String id, type;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private Bitmap bmp = null;
    private TextView text;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private Button btn;
    private Socket socket;
    private String[] picids;
    private String tag;
    private HistoryDAO dao;
    private Exercise3 exercise3;
    private String url;
    private String selected;
    private TextView page;
    private Bitmap nbmp;
    private Bitmap bmp2= null;
    private Bitmap bmp3 = null;
    private Bitmap bmp4 = null;
    private String mUsername;
    private BufferedReader reader;
    private BufferedWriter writer;
    int[] checkbox;
    String[] newstr;

    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    System.out.println("111");
                    Bitmap bmp = (Bitmap) msg.obj;
                    img1.setImageBitmap(bmp);
//                    text.setText(tag);
                    break;
                case 1:
                    System.out.println("222");
                    Bitmap bmp2 = (Bitmap) msg.obj;
                    img2.setImageBitmap(bmp2);
                    break;
                case 2:
                    System.out.println("333");
                    Bitmap bmp3 = (Bitmap) msg.obj;
                    img3.setImageBitmap(bmp3);
                    break;
                case 3:
                    System.out.println("444");
                    Bitmap bmp4 = (Bitmap) msg.obj;
                    img4.setImageBitmap(bmp4);
                    break;
                case 0x555:
                    Toast.makeText(ShowExercise3.this, "你没有选择任何选项！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_exercise);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        id = intent.getExtras().get("id").toString();
        type = intent.getExtras().get("type").toString();
        dao = new HistoryDAO(this);
        exercise3 = dao.getInfoById3(id);
        picids = new String[4];
        picids[0] = exercise3.getPicid1();
        picids[1] = exercise3.getPicid2();
        picids[2] = exercise3.getPicid3();
        picids[3] = exercise3.getPicid4();
        mUsername = getIntent().getStringExtra("userName");
        checkbox = new int[4];
        tag = exercise3.getT();

        img1 = (ImageView) findViewById(R.id.imageView1);
        img2 = (ImageView) findViewById(R.id.imageView2);
        img3 = (ImageView) findViewById(R.id.imageView3);
        img4 = (ImageView) findViewById(R.id.imageView4);
        text = (TextView) findViewById(R.id.textView2);
        btn = (Button) findViewById(R.id.button1);
//        btn.clearAnimation();
//        btn.setVisibility(View.GONE);
        page = (TextView) findViewById(R.id.page);
        page.setVisibility(View.GONE);
        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
        selected = exercise3.getSelected();
        Log.i("selected",selected);
        if(selected.charAt(0) == '1'){
            checkBox1.clearAnimation();
            checkBox1.setChecked(true);
            checkbox[0] = 1;
        }
        if(selected.charAt(1) == '1'){
            checkBox2.clearAnimation();
            checkBox2.setChecked(true);
            checkbox[1] = 1;
        }
        if(selected.charAt(2) == '1'){
            checkBox3.clearAnimation();
            checkBox3.setChecked(true);
            checkbox[2] = 1;
        }
        if(selected.charAt(3) == '1'){
            checkBox4.clearAnimation();
            checkBox4.setChecked(true);
            checkbox[3] = 1;
        }
//        checkBox1.setEnabled(false);
//        checkBox2.setEnabled(false);
//        checkBox3.setEnabled(false);
//        checkBox4.setEnabled(false);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                send();
                if(checkbox[0]==0&&checkbox[1]==0&&checkbox[2]==0&&checkbox[3]==0){
                    Toast.makeText(ShowExercise3.this, "你没有选择任何选项！", Toast.LENGTH_SHORT).show();
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
                dao.deleteById(id, type);
                Log.d("ThirdExercise","selected:"+selected);



                    Intent intent = new Intent(ShowExercise3.this, HistoryActivity.class);
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

        text.setText(exercise3.getT());
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {

                    for (int i = 0; i < 4; i++) {
                        bmp = getURLimage(picids[i]);
                        Message msg = new Message();
                        msg.what = i;
                        msg.obj = bmp;
                        System.out.println("000");
                        handle.sendMessage(msg);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    protected void send() {
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
                            sendstr += "," + picids[0] + "," + tag;
                    }


                    if (sendstr == "[Submit]" + mUsername) {
                        Message msg = new Message();
                        msg.what = 0x555;
                        handle.sendMessage(msg);
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

    public Bitmap getURLimage(String picid) {
        Bitmap bmp = null;
        BufferedWriter bw;
        BufferedReader br;
        try {
            socket = new Socket("120.25.76.27", 1222);
            // 获得输入流
            br = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            bw = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
            bw.write("[GetPic]" + picid + ",p\n");
            bw.flush();
            Log.i("getpicid",picid);
            url = br.readLine();
            url = "http://www.bi-home.cn/software/pic/" + url;

            Log.i("url","http://www.bi-home.cn/software/pic/" + url);
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
}