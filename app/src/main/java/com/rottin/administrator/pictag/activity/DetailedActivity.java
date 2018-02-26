package com.rottin.administrator.pictag.activity;

import com.rottin.administrator.pictag.R;
import com.rottin.administrator.pictag.data.BitmapHelper;
import com.rottin.administrator.pictag.data.GlobalData;
import com.rottin.administrator.pictag.vo.GoodsVO;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import static android.provider.Telephony.Carriers.PORT;

@SuppressLint("HandlerLeak")
public class DetailedActivity extends Activity {

    final private static String HOST = "120.25.76.27", TAG = "DetailedActivity";
    final private static int PORT = 1222;
    ImageView image;
    TextView name;
    TextView info;
    TextView price;
    Button collect;
    Button addCar;
    Thread thread;
    private int id;
    private GoodsVO goods;
    private AlertDialog dialog;
    private String userName;
    private String infos[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        init();
        Intent intent = getIntent();
        userName = getIntent().getStringExtra("username");
        id = Integer.parseInt(intent.getExtras().getString("goodsId"));
        id--;
        Log.i("data", "" + id);
        setData(id);
        new Thread(downloadRun).start();

    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    setData(id);
                    System.out.println("111");
                    Bitmap bmp = (Bitmap) msg.obj;
                    image.setImageBitmap(bmp);
                    break;
                case 1:
                    Toast.makeText(DetailedActivity.this, "收藏成功",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(DetailedActivity.this, "已收藏",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Intent intent = new Intent(
                            DetailedActivity.this,
                            AffirmActivity.class);
                    intent.putExtra("goodsId1", id);
                    Log.i("id1", "" + id);
                    intent.putExtra("userName", userName);
                    intent.putExtra("updateStr", (msg.obj).toString());
                    startActivity(intent);
                    finish();
                    break;
                case 4:
                    Toast.makeText(DetailedActivity.this, "积分不足，不能购买该商品，继续努力吧", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        ;
    };

    private void setData(int id) {
        // TODO Auto-generated method stub
        goods = new GoodsVO(GlobalData.id.get(id), GlobalData.images.get(id),
                GlobalData.name.get(id), GlobalData.price.get(id), GlobalData.info.get(id),
                GlobalData.parentType[id], GlobalData.childType[id]);
        Log.i("data", GlobalData.name.get(0));
        Log.i("data", GlobalData.name.get(1));

        Log.i("name", goods.getName());
        Log.i("name", goods.getImages());
        name.setText(goods.getName());
        info.setText(goods.getInfo());
        price.setText(goods.getPrice() + "积分");
    }

    private void init() {
        // TODO Auto-generated method stub
        image = (ImageView) findViewById(R.id.detail_image);
        name = (TextView) findViewById(R.id.detail_name);
        info = (TextView) findViewById(R.id.detail_info);
        price = (TextView) findViewById(R.id.detail_price);
        collect = (Button) findViewById(R.id.detail_collect);
        addCar = (Button) findViewById(R.id.detail_add_car);
        TextView left = (TextView) findViewById(R.id.title_left);
        TextView center = (TextView) findViewById(R.id.title_center);
        TextView right = (TextView) findViewById(R.id.title_right);
        left.setText("返回");
        center.setText("商品详情");
        right.setText("");
        collect.setOnClickListener(onClick);
        left.setOnClickListener(onClick);
        addCar.setOnClickListener(onClick);
    }

    View.OnClickListener onClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.detail_collect:
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
                                bw.write("[Userinfo]" + userName + "\n");
                                bw.flush();

                                String[] newstr = null;
                                String str = br.readLine();
                                Log.i("tag", str);
                                newstr = str.split(",");
                                newstr[7] = getIntent().getStringExtra("goodsId");

                                bw.close();
                                br.close();
                                socket.close();
                                //tag = newstr[0].substring(4);
                                socket = new Socket("120.25.76.27", 1222);
                                br = new BufferedReader(
                                        new InputStreamReader(socket.getInputStream()));
                                bw = new BufferedWriter(
                                        new OutputStreamWriter(socket.getOutputStream()));
                                bw.write("[UpdateUinfo]" + userName + "," + newstr[1] + "," + newstr[2] + "," + newstr[3] + "," + newstr[4] + "," + newstr[5] + "," + newstr[6] + "," + newstr[7] + "\n");
                                bw.flush();
                                Log.i("Update", "[UpdateUinfo]" + userName + "," + newstr[1] + "," + newstr[2] + "," + newstr[3] + "," + newstr[4] + "," + newstr[5] + "," + newstr[6] + "," + newstr[7] + "\n");
                                String flag = br.readLine();
                                if (flag.equals("1")) {
                                    Message msg = new Message();
                                    msg.what = 1;
                                    handler.sendMessage(msg);
                                } else {
                                    Message msg = new Message();
                                    msg.what = 2;
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
                    break;
                case R.id.detail_add_car:
                    openDialog();
                    break;

                case R.id.title_left:
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    protected void openDialog() {
        // TODO Auto-generated method stub
        if (dialog == null) {
            dialog = new AlertDialog.Builder(this)
                    .setTitle("确认")
                    .setMessage("是否购买本商品？")
                    .setPositiveButton("否", null)
                    .setNegativeButton("是",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    checkScore();
                                }
                            }).create();
        }
        dialog.show();
    }

    Runnable downloadRun = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub  
            Bitmap bmp = BitmapHelper.getBitmap(goods.getImages());
            Message msg = new Message();
            msg.what = 0;
            msg.obj = bmp;
            System.out.println("000");
            handler.sendMessage(msg);
        }
    };

    private void checkScore() {
        AsyncTask<Void, String, Void> asyncTask = new AsyncTask<Void, String, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Socket socket = new Socket(HOST, PORT);
                    // 获得输入流
                    BufferedReader br1 = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    BufferedWriter bw1 = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream()));

                    bw1.write("[Userinfo]" + userName + "\n");
                    bw1.flush();
                    String details = br1.readLine();
                    Log.i(TAG, details);
                    infos = details.split(",");
                    publishProgress("" + infos[6].toString());
                    br1.close();
                    bw1.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "获取用户数据异常");
                    publishProgress("0");
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                Log.d(TAG, "获取到values[0]为：" + values[0]);
                if (Integer.parseInt(values[0]) >= Integer.parseInt(goods.getPrice())) {
                    //生成update接口需要的字符串 通过intent传给确认界面
                    infos[6] = "" + (Integer.parseInt(infos[6]) - Integer.parseInt(goods.getPrice()));
                    String updateStr = "[UpdateUinfo]";
                    updateStr+=(userName+",");
                    for (int i = 1; i < 7; i++) {
                        updateStr += (infos[i] + ",");
                    }
                    updateStr += (infos[7] + "\n");

                    Message msg = new Message();
                    msg.what = 3;
                    msg.obj = updateStr;
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = 4;
                    handler.sendMessage(msg);
                    Log.d(TAG, "积分不足");
                }


                super.onProgressUpdate(values);
            }
        };
        asyncTask.execute();
    }
}
