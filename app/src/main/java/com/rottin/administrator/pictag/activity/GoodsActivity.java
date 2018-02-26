package com.rottin.administrator.pictag.activity;

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
import java.util.ArrayList;
import java.util.HashMap;

import com.rottin.administrator.pictag.R;
import com.rottin.administrator.pictag.adapter.GoodsAdapter;
import com.rottin.administrator.pictag.adapter.ImageLoader;
import com.rottin.administrator.pictag.data.BitmapHelper;
import com.rottin.administrator.pictag.data.DataInput;
import com.rottin.administrator.pictag.data.GlobalData;
import com.rottin.administrator.pictag.vo.GoodsVO;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.LocaleDisplayNames;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class GoodsActivity extends Activity {

    GridView gv;
    ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    HashMap<String, Object> map = null;
    ProgressBar progressBar;
    private String result;
    private boolean flag = true;

    GoodsAdapter adapter;
    String username;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    System.out.println("111");

                    break;
                case 1:
                    Log.d("goods", "获取信息完毕");
                    progressBar.setVisibility(View.GONE);
                    break;

            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        username = getIntent().getStringExtra("username");
        progressBar = (ProgressBar) findViewById(R.id.goods_progressbar);
        progressBar.setVisibility(View.VISIBLE);
        TextView left = (TextView) findViewById(R.id.title_left);
        left.setText("返回");
        left.setOnClickListener(onClick);
        TextView center = (TextView) findViewById(R.id.title_center);
        center.setText("积分商城");
        TextView right = (TextView) findViewById(R.id.title_right);
        right.setVisibility(View.INVISIBLE);

        GlobalData.id.clear();
        GlobalData.images.clear();
        GlobalData.info.clear();
        GlobalData.price.clear();

        AsyncTask<Void,String, Void> asyncTask = new AsyncTask<Void, String, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                initData();

                Log.d("async", "initData finished");
                result = "1";
                try {
                    Thread.sleep(3200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                while (flag){
//                    Thread
//                }
                publishProgress(result);
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                Log.d("async", "result:"+result);
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                if(result.equals("1")){
                    initAdapter();
                }

                super.onProgressUpdate(values);
            }
        };
        asyncTask.execute();
    }

    private void initAdapter(){
        Log.d("goods", "initAdapter");
        String[][] a = {{"a", "b", "c", "d", "e", "f"}, {"1", "2", "3", "4", "5", "6"}};
        Log.i("a", "" + a.length);
        //GlobalData.getInput();
//		Log.i("global", GlobalData.id.get(0));
        init();
        gv = (GridView) findViewById(R.id.goods_grid_list);
        adapter = new GoodsAdapter(GoodsActivity.this, GlobalData.goodsList);
        gv.setAdapter(adapter);
        Log.d("goods", "setAdapter");
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(GoodsActivity.this, DetailedActivity.class);
                intent.putExtra("goodsId", GlobalData.goodsList.get(arg2).getId());
                Log.i("newid", "" + GlobalData.goodsList.get(arg2).getId());
                intent.putExtra("username", username);
                startActivity(intent);

                finish();
            }
        });
    }
    private void init() {
        // TODO Auto-generated method stub
        for (int i = 0; i < GlobalData.id.size(); i++) {
            GlobalData.goodsList.add(new GoodsVO(GlobalData.id.get(i),
                    GlobalData.images.get(i), GlobalData.name.get(i),
                    GlobalData.price.get(i), GlobalData.info.get(i),
                    GlobalData.parentType[i], GlobalData.childType[i]));
        }
    }

    View.OnClickListener onClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.title_left:
                    finish();
                    break;
                default:
                    break;
            }
        }
    };


    private static String infor = null;
    static int n;

    public void initData() {


        String[][] newstr = null;
        /*AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                Socket socket;
                try {
                    socket = new Socket("120.25.76.27", 1222);
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    BufferedWriter bw = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream()));
                    bw.write("[Market]\n");
                    bw.flush();

                    infor = br.readLine().substring(10);
                    Log.i("tah", infor);


                    n = Integer.parseInt(infor);

                    String[][] newstr = new String[n][6];
                    int i = 0;


                    //br.readLine();
                    while ((infor = br.readLine()) != null) {
                        Log.i("msg", infor);
                        newstr[i] = infor.split(",");
                        i++;

                    }

                    for (int a = 0; a < n; a++) {
                        GlobalData.id.add(newstr[a][0].substring(7));
                        Log.i("id", GlobalData.id.get(a));
                    }
                    for (int x = 0; x < n; x++) {
                        GlobalData.images.add(newstr[x][1]);
                        Log.i("images", GlobalData.images.get(x));
                    }
                    for (int x = 0; x < n; x++) {
                        GlobalData.name.add(newstr[x][2]);
                        Log.i("name", GlobalData.name.get(x));
                    }
                    for (int x = 0; x < n; x++) {
                        GlobalData.price.add(newstr[x][3]);
                        Log.i("price", GlobalData.price.get(x));
                    }
                    for (int x = 0; x < n; x++) {
                        GlobalData.info.add(newstr[x][4]);
                        Log.i("info", GlobalData.info.get(x));
                    }

                    br.close();
                    bw.close();
                    socket.close();
                    // a= newstr;

                    for (int x = 0; x < n; x++) {
                        socket = new Socket("120.25.76.27", 1222);
                        br = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));
                        bw = new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream()));
                        bw.write("[GetPic]" + GlobalData.images.get(x) + ",s\n");
                        Log.i("tah", GlobalData.images.get(x));
                        bw.flush();
                        GlobalData.images.set(x, "http://www.bi-home.cn/software/stuff/" + br.readLine());
                        Log.i("tah", GlobalData.images.get(x));
                        br.close();
                        bw.close();
                        socket.close();
                        Message msg = new Message();
                        msg.what = 0;
                        handler.sendMessage(msg);
                        publishProgress(1);
                    }
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    publishProgress(0);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    publishProgress(0);
                }
            }

            @Override
            protected void onProgressUpdate(Object[] values) {
                super.onProgressUpdate(values);
            }
        };
        asyncTask.execute();*/

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Socket socket;
                try {
                    socket = new Socket("120.25.76.27", 1222);
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    BufferedWriter bw = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream()));
                    bw.write("[Market]\n");
                    bw.flush();

                    infor = br.readLine().substring(10);
                    Log.i("tah", infor);


                    n = Integer.parseInt(infor);

                    String[][] newstr = new String[n][6];
                    int i = 0;


                    //br.readLine();
                    while ((infor = br.readLine()) != null) {
                        Log.i("msg", infor);
                        newstr[i] = infor.split(",");
                        i++;

                    }

                    for (int a = 0; a < n; a++) {
                        GlobalData.id.add(newstr[a][0].substring(7));
                        Log.i("id", GlobalData.id.get(a));
                    }
                    for (int x = 0; x < n; x++) {
                        GlobalData.images.add(newstr[x][1]);
                        Log.i("images", GlobalData.images.get(x));
                    }
                    for (int x = 0; x < n; x++) {
                        GlobalData.name.add(newstr[x][2]);
                        Log.i("name", GlobalData.name.get(x));
                    }
                    for (int x = 0; x < n; x++) {
                        GlobalData.price.add(newstr[x][3]);
                        Log.i("price", GlobalData.price.get(x));
                    }
                    for (int x = 0; x < n; x++) {
                        GlobalData.info.add(newstr[x][4]);
                        Log.i("info", GlobalData.info.get(x));
                    }

                    br.close();
                    bw.close();
                    socket.close();
                    // a= newstr;

                    for (int x = 0; x < n; x++) {
                        socket = new Socket("120.25.76.27", 1222);
                        br = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));
                        bw = new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream()));
                        bw.write("[GetPic]" + GlobalData.images.get(x) + ",s\n");
                        Log.i("tah", GlobalData.images.get(x));
                        bw.flush();
                        GlobalData.images.set(x, "http://www.bi-home.cn/software/stuff/" + br.readLine());
                        Log.i("tah", GlobalData.images.get(x));
                        br.close();
                        bw.close();
                        socket.close();
                        Message msg = new Message();
                        msg.what = 0;
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
