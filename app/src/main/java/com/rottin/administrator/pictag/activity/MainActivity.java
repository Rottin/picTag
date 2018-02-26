package com.rottin.administrator.pictag.activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LruCache;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.ActivityCompat;

import com.nineoldandroids.view.ViewHelper;
import com.rottin.administrator.pictag.DragLayout;
import com.rottin.administrator.pictag.R;


public class MainActivity extends AppCompatActivity {

    private String mUsername, info1;
    private static int i = 0;
    private DragLayout dl;
    private ListView lv;
    private String infos[];
    private String[] good;
    private ImageView iv_icon, img_guess;
    private TextView broadcast_text, tabBar_text, slide_username,grade;
    private Button button_left, button_right, button_begin;
    private ProgressBar mProgressBar, guessProgressbar;
    final private static String HOST = "120.25.76.27", TAG = "MainActivity";
    final private static int PORT = 1222;
    private String[] guessArray = new String[4];
    private List<Bitmap> guessPics = new ArrayList<Bitmap>();
    private String url;
    private int guessPicNum = 0, currentGuessPic = 0;
    private ImageView iv_addpic;
    private Toolbar toolbar;
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private AlertDialog dialog;
    private Bitmap bmp = null;
    private int page = 1;
    private Bitmap nbmp;
    //创建Cache
    private LruCache<String, Bitmap> mCaches;
    private String price;
    private String goal;
    private String goods;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    if(bmp!=null)
                    bmp.recycle();
                    bmp = guessPics.get(0);
                    img_guess.setImageBitmap(bmp);
                    guessProgressbar.setVisibility(View.GONE);
                    Log.d(TAG, "底部图片加载完成");
                    break;
                case 1:
                    img_guess.setImageBitmap((Bitmap) msg.obj);

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermission();
        initDragLayout();
        initView();
        getDetails();
        initGuessPics();
    }


    /******动态申请权限*******/

    private void initPermission() {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            int j = ContextCompat.checkSelfPermission(this, permissions[1]);
            if (i != PackageManager.PERMISSION_GRANTED || j != PackageManager.PERMISSION_GRANTED) {
                showDialogTipUserRequestPermission();
            }
        }
    }

    private void showDialogTipUserRequestPermission() {

        new AlertDialog.Builder(this)
                .setTitle("相机或存储权限不可用")
                .setMessage("请允许软件使用相机与存储权限以保证所有功能的正常运行")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
                    }
                }).setCancelable(false).show();
    }

    // 开始提交请求权限
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }

    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        showDialogTipUserGoToAppSettting();
                    } else
                        finish();
                } else {
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showDialogTipUserGoToAppSettting() {

        dialog = new AlertDialog.Builder(this)
                .setTitle("手动开启权限")
                .setMessage("请在 应用设置-权限 中，开启PiaTag申请使用的权限")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, 123);
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting();
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /*****动态申请权限部分结束*****/


    private void initDragLayout() {
        dl = (DragLayout) findViewById(R.id.dl);
        dl.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onOpen() {
                lv.smoothScrollToPosition(new Random().nextInt(30));
            }

            @Override
            public void onClose() {
                shake();
            }

            @Override
            public void onDrag(float percent) {
                ViewHelper.setAlpha(iv_icon, 1 - percent);
            }
        });
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#009688"));
        }
        mUsername = getIntent().getExtras().get("username").toString();
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        img_guess = (ImageView) findViewById(R.id.img_guess);
        img_guess.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                intent.putExtra("picId", guessArray[currentGuessPic].substring(5));
                intent.putExtra("username", mUsername);
                startActivity(intent);
            }
        });
//        LinearLayout ads = (LinearLayout) findViewById(R.id.ads);
//        Snackbar.make(ads , "测试", Snackbar.LENGTH_LONG)
//                .show();
        grade = (TextView)findViewById(R.id.main_grade);
        broadcast_text = (TextView) findViewById(R.id.broadcast_text);
        broadcast_text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/5/29 跳转积分商城
//				Intent intent = new Intent(MainActivity.this, )
            }
        });
        tabBar_text = (TextView) findViewById(R.id.main_tabBarText);
        tabBar_text.setText(mUsername);
        slide_username = (TextView) findViewById(R.id.slide_username);
        slide_username.setText(mUsername);
        button_begin = (Button) findViewById(R.id.button_begin);
        //button_history = (Button) findViewById(R.id.button_history);
        //button_upload = (Button) findViewById(R.id.button_upload);
        button_left = (Button) findViewById(R.id.button_left);
        button_right = (Button) findViewById(R.id.button_right);
        button_begin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FirstExcerciseActivity.class);
                intent.putExtra("count1", 0);
                intent.putExtra("username", mUsername);
                intent.putExtra("page",1);
                startActivity(intent);
                if(bmp!=null&&!bmp.isRecycled()){
                    bmp.recycle() ;
                    bmp=null;
                    System.gc();
                }
                finish();
            }
        });
        //// TODO: 2017/7/3 button_left right的点击方法
        button_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentGuessPic == 0)
                    Snackbar.make(v, "前面已经没有图片了", Snackbar.LENGTH_SHORT).show();
                else if (currentGuessPic > 0) {
                    currentGuessPic--;
                    Bitmap bitmap = getBitmapFromCache(""+currentGuessPic);
                    if(bitmap == null){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = new Message();
                                msg.what = 1;
                                bmp = getUrlImage(currentGuessPic);
                                addBitmapToCache(""+currentGuessPic,bmp);
                                msg.obj = bmp;
                                handler.sendMessage(msg);
                            }
                        }).start();
                        ///new NewsAsyncTask(imageView,url).execute(url);
                    }else{
                        img_guess.setImageBitmap(bitmap);
                    }

                }
            }
        });
        button_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentGuessPic == guessPicNum - 1) {
                    Snackbar.make(v, "后面已经没有图片了", Snackbar.LENGTH_SHORT).show();
                    dialog();
                }
                else if (currentGuessPic < guessPicNum - 1) {
                    currentGuessPic++;
                    Bitmap bitmap = getBitmapFromCache(""+currentGuessPic);
                    if(bitmap == null){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = new Message();
                                msg.what = 1;
                                bmp = getUrlImage(currentGuessPic);
                                addBitmapToCache(""+currentGuessPic,bmp);
                                msg.obj = bmp;
                                handler.sendMessage(msg);
                            }
                        }).start();
                        ///new NewsAsyncTask(imageView,url).execute(url);
                    }else{
                        img_guess.setImageBitmap(bitmap);
                    }
                }
            }
        });

        mProgressBar = (ProgressBar) findViewById(R.id.main_progressbar);
        guessProgressbar = (ProgressBar) findViewById(R.id.main_guess_progressbar);

        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                R.layout.item_text, new String[]{"个人信息", "积分商城",
                "历史记录", "反馈", "关于", "注销",
        }));
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                //Toast.makeText(getApplicationContext(), "click " + position, Toast.LENGTH_SHORT).show();
                //跳转个人信息界面
                if (position == 0) {
                    Intent intent = new Intent(MainActivity.this, UserDetailActivity.class);
                    intent.putExtra("username", mUsername);
                    intent.putExtra("goal",goal);
                    startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(MainActivity.this, GoodsActivity.class);
                    intent.putExtra("username", mUsername);
                    startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                    intent.putExtra("username", mUsername);
                    startActivity(intent);
                } else if (position == 3) {
                    Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
                    intent.putExtra("username", mUsername);
                    startActivity(intent);
                } else if (position == 4) {
                    Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                    intent.putExtra("username", mUsername);
                    startActivity(intent);
                } else if (position == 5) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
            }
        });
        iv_icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dl.open();
            }
        });
        //上传图片控件初始化
        iv_addpic = (ImageView) findViewById(R.id.iv_add);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        iv_addpic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openOptionsMenu();
            }
        });
    }

    //在右上角打开菜单列表
    @Override
    public void openOptionsMenu() {
        final View toolbar = findViewById(R.id.toolbar_main);//填入你的toolbar的ID
        if (toolbar instanceof Toolbar) {
            ((Toolbar) toolbar).showOverflowMenu();
        } else {
            super.openOptionsMenu();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.uploadpic_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent = new Intent(MainActivity.this, PreviewPicActivity.class);
        //noinspection SimplifiableIfStatement
//        if (id == R.id.useCamera) {
//            intent.putExtra("TYPE", 1);//调用相机 takePhoto()
//            startActivity(intent);
//            return true;
//        } else if (id == R.id.fromGallery) {
        if (id == R.id.fromGallery) {
            intent.putExtra("TYPE", 2);//调用系统相册选取 pickPhoto()
            intent.putExtra("username", mUsername);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shake() {
        iv_icon.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    //增加到缓存
    public void addBitmapToCache(String url, Bitmap bitmap) {
        if(getBitmapFromCache(url)==null){
            mCaches.put(url, bitmap);
//            bitmap.recycle();
//            bmp = null;
//            System.gc();
        }
    }
    //从缓存中获取数据
    public Bitmap getBitmapFromCache(String url){
        return mCaches.get(url);

    }

    boolean flag = true;
    private void initGuessPics() {
        guessProgressbar.setVisibility(View.VISIBLE);
        currentGuessPic = 0;
        //创建缓存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory/4;
        mCaches = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                // 每次存入时调用
                return value.getByteCount();
            }
        };
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                getGuessArray();

                Message msg = new Message();
                msg.what = 1;
                bmp = getUrlImage(currentGuessPic);
                addBitmapToCache(""+currentGuessPic,bmp);
                msg.obj = bmp;
                handler.sendMessage(msg);
//                guessPics.add(getUrlImage(currentGuessPic));
//                Message msg = new Message();
//                msg.what = 100;
//                handler.sendMessage(msg);
//                Log.d(TAG, "guessPic数组获取完成");
            }
        });
        try {
            t1.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t1.start();
    }

    public void getGuessArray() {
        Socket socket;
        try {
            socket = new Socket("120.25.76.27", 1222);
            // 获得输入流
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
            bw.write("[GetGuessPic]\n");
            bw.flush();
            String guessPicStr = br.readLine();
            Log.i(TAG, "字符串：" + guessPicStr);
            guessPicNum = Integer.parseInt(guessPicStr.substring(8));
            for (int i = 0; i < guessPicNum; i++) {
                guessArray[i] = br.readLine();
                Log.d(TAG, "guessArray:" + guessArray[i]);
            }
            socket.close();
        } catch (UnknownHostException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (NullPointerException e) {
            Message msg = new Message();
            msg.what = 0x122;
            msg.obj = "网络连接超时！";
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public Bitmap getUrlImage(int num) {
        Bitmap bmp = null;
        Socket socket;
        try {
            socket = new Socket("120.25.76.27", 1222);
            // 获得输入流
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
            bw.write("[GetPic]" + guessArray[num].substring(5) + ",p\n");
            bw.flush();

            url = br.readLine();
            Log.d(TAG, "URL:" + url);
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
                return nbmp;
            }
            is.close();
        } catch (UnknownHostException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (NullPointerException e) {
            Message msg = new Message();
            msg.what = 0x122;
            msg.obj = "网络连接超时！";
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return bmp;
    }

    private void getDetails() {

        AsyncTask<Void, String, Void> read = new AsyncTask<Void, String, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                //Socket socket = new Socket();
                Message msg = new Message();
                info1 = "0";

                try {
                    //socket.connect(new InetSocketAddress(HOST, PORT));
                    Socket socket = new Socket(HOST,PORT);
                    // 获得输入流
                    BufferedReader br1 = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    BufferedWriter bw1 = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream()));

                    bw1.write("[Userinfo]" + mUsername + "\n");
                    bw1.flush();
                    String details = br1.readLine();
                    Log.i("details",details);
                    infos = details.split(",");
                    br1.close();
                    bw1.close();
                    socket.close();

                    //socket.connect(new InetSocketAddress(HOST, PORT));
                    socket = new Socket(HOST, PORT);
                    // 获得输入流
                    br1 = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    bw1 = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream()));

                    bw1.write("[GetOneStuff]" + infos[7] + "\n");
                    bw1.flush();
                    goods = br1.readLine();
                    if(goods == null){
                        price = "100";
                        goal = "无";
                    }else{
                        good = goods.split(",");
                        price = good[0];
                        goal = good[1];
                    }

//                    if(price == null){
//                        price = "100";
//                    }
                    //Log.i("price",price);
                    br1.close();
                    bw1.close();
                    socket.close();
                    info1 = "1";
                    publishProgress(info1);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "获取用户数据异常");
                    info1 = "0";
                    publishProgress(info1);
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                if (info1.equals("1")) {
                    // TODO: 2017/7/6 更新progressbar
                    Log.d(TAG, "更新goal,mark为" + infos[6]);
                    if(Integer.parseInt(infos[6])<0){
                        showDefaultDialog();
                    }
                    mProgressBar.setProgress(100*Integer.parseInt(infos[6])/Integer.parseInt(price));
                    grade.setText(infos[6]+"/"+price);
                } else {
                    Toast.makeText(MainActivity.this, "获取用户信息失败，请检查网络", Toast.LENGTH_SHORT).show();
                }
                super.onProgressUpdate(values);
            }
        };
        read.execute();
    }

    private void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("更换一组图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initGuessPics();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    private void showDefaultDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("对不起，您的账号已被封禁。");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    private long firstTime=0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
            if (System.currentTimeMillis()-firstTime>2000){
                Toast.makeText(MainActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                firstTime=System.currentTimeMillis();
            }else{
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
