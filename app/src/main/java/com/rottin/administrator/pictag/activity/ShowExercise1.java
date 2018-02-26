package com.rottin.administrator.pictag.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rottin.administrator.pictag.HistoryDAO;
import com.rottin.administrator.pictag.R;
import com.rottin.administrator.pictag.domain.Exercise1;

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

import static android.R.attr.handle;

public class ShowExercise1 extends AppCompatActivity {

    private String id, type;
    private Exercise1 exercise1;
    private HistoryDAO dao;
    private ImageView img;
    private EditText edit1;
    private EditText edit2;
    private EditText edit3;
    private EditText edit4;
    private Button btn;
    private String picid;
    private Socket socket;
    private String url;
    private TextView page;
    private Bitmap nbmp;
    private String mUsername;
    private String sendstr;
    private BufferedReader reader;
    private BufferedWriter writer;

    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    System.out.println("111");
                    Bitmap bmp = (Bitmap) msg.obj;
                    img.setImageBitmap(bmp);
                    break;
                case 0x555:
                    Toast.makeText(ShowExercise1.this, "你没有输入任何信息！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_excercise);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        id = intent.getExtras().get("id").toString();
        type = intent.getExtras().get("type").toString();
        dao = new HistoryDAO(this);
        exercise1 = dao.getInfoById1(id);
        picid = exercise1.getPicid();

        img = (ImageView) findViewById(R.id.imageView1);
        edit1 = (EditText) findViewById(R.id.editText1);
        edit2 = (EditText) findViewById(R.id.editText2);
        edit3 = (EditText) findViewById(R.id.editText3);
        edit4 = (EditText) findViewById(R.id.editText4);
        btn = (Button) findViewById(R.id.first_button1);
        page = (TextView) findViewById(R.id.page);
        page.setVisibility(View.GONE);
        mUsername = getIntent().getStringExtra("userName");
//        edit1.setFocusable(false);
//        edit2.setFocusable(false);
//        edit3.setFocusable(false);
//        edit4.setFocusable(false);

        edit1.setText(exercise1.getT1());
        edit2.setText(exercise1.getT2());
        edit3.setText(exercise1.getT3());
        edit4.setText(exercise1.getT4());
        //btn.setVisibility(View.GONE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //send();
                if(edit1.getText().toString().trim().length()==0&&edit2.getText().toString().trim().length()==0&&edit3.getText().toString().trim().length()==0&&edit4.getText().toString().trim().length()==0){
                    Toast.makeText(ShowExercise1.this, "你没有输入任何信息！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //数据写入本地历史记录数据库
                dao = new HistoryDAO(getBaseContext());
                dao.deleteById(id, type);
                dao.insert1(picid, edit1.getText().toString(), edit2.getText().toString(),
                        edit3.getText().toString(), edit4.getText().toString(), mUsername);




                Intent intent = new Intent(ShowExercise1.this, HistoryActivity.class);
                intent.putExtra("username",mUsername);
                startActivity(intent);


                finish();
            }
        });


        //初始化图片
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Bitmap bmp = getURLimage(picid);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = bmp;
                System.out.println("000");
                handle.sendMessage(msg);

            }
        }).start();
    }

    public void send() {


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

}
