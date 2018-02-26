package com.rottin.administrator.pictag.activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.rottin.administrator.pictag.R;
import com.rottin.administrator.pictag.adapter.AffirmAdapter;
import com.rottin.administrator.pictag.data.BitmapHelper;
import com.rottin.administrator.pictag.data.GlobalData;
import com.rottin.administrator.pictag.vo.AddressVO;
import com.rottin.administrator.pictag.vo.GoodsVO;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AffirmActivity extends Activity implements OnClickListener {

    @SuppressWarnings("unused")
    private static final String TAG = "AffirmActivity";
    final private static String HOST = "120.25.76.27";
    final private static int PORT = 1222;
    ListView lv;
    ImageView image;
    TextView gname;
    Button button;
    TextView sumTxt;
    RelativeLayout layout;
    TextView name;
    TextView tel;
    TextView addr;
    TextView hintText;
    TextView textView;
    AffirmAdapter adapter;
    private AddressVO address;
    private GoodsVO goods;
    private int id;
    private String userName, updateStr;
    private String[] newstr = new String[8];
    private AlertDialog ad = null;
    private String tele, addre;


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
                    hintText.setVisibility(View.GONE);
                    name.setText(userName);
                    tel.setText(newstr[2]);
                    addr.setText(newstr[4]);
                    break;
                case 2:
                    if (msg.obj.equals("1")) {
                        Toast.makeText(AffirmActivity.this, "修改成功", Toast.LENGTH_SHORT);
                        tel.setText(tele);
                        addr.setText(addre);
                    } else {
                        Toast.makeText(AffirmActivity.this, "修改失败", Toast.LENGTH_SHORT);
                    }
                    break;
            }
        }

        ;
    };

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affirm);
        init();
        Intent intent = getIntent();
        id = intent.getExtras().getInt("goodsId1");
        userName = intent.getStringExtra("userName");
        updateStr = intent.getStringExtra("updateStr");
        Log.i("id", "" + id);
        Log.d(TAG, "updateStr为："+updateStr);
        setData(id);
//		list = (ArrayList<CarVO>) intent.getSerializableExtra("carInfo");
//		adapter = new AffirmAdapter(this, list);
//		lv.setAdapter(adapter);
        //int sum = 0;
//		for (CarVO vo : list) {
//			sum += Integer.parseInt(vo.getGoods().getPrice()) * vo.getNum();
//		}

        //sumTxt.setText("�ϼ�:" + goods.getPrice());
        new Thread(downloadRun).start();
        changeAddress();

    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        changeAddress();
    }


    private void setData(int id) {
        // TODO Auto-generated method stub
        goods = new GoodsVO(GlobalData.id.get(id), GlobalData.images.get(id),
                GlobalData.name.get(id), GlobalData.price.get(id), GlobalData.info.get(id),
                GlobalData.parentType[id], GlobalData.childType[id]);
        image.setImageResource(goods.getImage());
        gname.setText(goods.getName());
        sumTxt.setText(goods.getPrice() + "分");

    }

    private void changeAddress() {
        // TODO Auto-generated method stub
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


                    String str = br.readLine();
                    Log.i("tag", str);
                    newstr = str.split(",");

                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = newstr;
                    handler.sendMessage(msg);

                    bw.close();
                    br.close();
                    socket.close();

                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();


//		boolean flag = true;
//		for (AddressVO vo : GlobalData.addresslist) {
//			if (vo.isDefault()) {
//				address = vo;
//				flag = false;
//				break;
//			}
//		}
//		if (flag && GlobalData.addresslist.size() > 0) {
//			int index = GlobalData.addresslist.size() - 1;
//			// Log.w(TAG,"---->>"+index+"----->>"+GlobalData.addressList.size());
//			address = GlobalData.addresslist.get(index);
//		}
//		if (GlobalData.addresslist.size() > 0) {
//			hintText.setVisibility(View.GONE);
//			name.setText(address.getName());
//			tel.setText(address.getTel());
//			addr.setText(address.getAddress());
//		} else {
//			hintText.setVisibility(View.VISIBLE);
//		}

    }

    private void init() {
        // TODO Auto-generated method stub
        TextView left = (TextView) findViewById(R.id.title_left);
        TextView center = (TextView) findViewById(R.id.title_center);
        TextView right = (TextView) findViewById(R.id.title_right);
        left.setText("返回");
        center.setText("确认订单");
        right.setText("");

        layout = (RelativeLayout) findViewById(R.id.affirm_address);
        button = (Button) findViewById(R.id.affirm_submit);
        sumTxt = (TextView) findViewById(R.id.textView1);
        name = (TextView) findViewById(R.id.affirm_name);
        tel = (TextView) findViewById(R.id.affirm_tel);
        addr = (TextView) findViewById(R.id.affirm_addr);
        hintText = (TextView) findViewById(R.id.affirm_hint);
        left.setOnClickListener(this);
        layout.setOnClickListener(this);
        button.setOnClickListener(this);
        image = (ImageView) findViewById(R.id.detail_image);
        gname = (TextView) findViewById(R.id.detail_name);
        textView = (TextView) findViewById(R.id.textView1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.affirm_address:// 更改收货地址
                //startActivity(new Intent(this, AddressActivity.class));
                addDialog();
                break;

            case R.id.title_left:
                finish();
                break;
            case R.id.affirm_submit:// 提交订单
                //更新积分
                updateScore();
                // 跳转
                Intent intent = new Intent(this, DoneActivity.class);
                intent.putExtra("username", userName);
                startActivity(intent);
                // 关闭页面
                finish();
                break;
        }
    }

    protected void addDialog() {
        if (ad == null) {
            final View view = LayoutInflater.from(this).inflate(
                    R.layout.addr_dialog_item, null);
            final TextView e_name = (TextView) view
                    .findViewById(R.id.addr_dialog_item_name);
            final EditText e_tel = (EditText) view
                    .findViewById(R.id.addr_dialog_item_tel);
            final EditText e_addr = (EditText) view
                    .findViewById(R.id.addr_dialog_item_addr);
            e_name.setText(userName);
            e_tel.setText(tel.getText().toString());
            e_addr.setText(addr.getText().toString());
            ad = new AlertDialog.Builder(this)
                    .setTitle("更改收货地址ַ")
                    .setView(view)
                    .setPositiveButton("更改",
                            new DialogInterface.OnClickListener() {// ���һ��ȷ�ϰ�ť
                                @Override
                                // ȷ�ϰ�ť�ĵ���¼�
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub


                                    tele = e_tel.getText().toString()
                                            .trim();
                                    addre = e_addr.getText().toString()
                                            .trim();
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
                                                bw.write("[UpdateUinfo]" + userName + "," + newstr[1] + "," + tele + "," + newstr[3] + "," + addre + "," + newstr[5] + "," + newstr[6] + "," + newstr[7] + "\n");
                                                bw.flush();


                                                Log.i("tag", "[UpdateUinfo]" + userName + "," + newstr[1] + "," + tele + "," + newstr[3] + "," + addre + "," + newstr[5] + "," + newstr[6] + "," + newstr[7] + "\n");


                                                Message msg = new Message();
                                                msg.what = 2;
                                                msg.obj = br.readLine();
                                                handler.sendMessage(msg);

                                                bw.close();
                                                br.close();
                                                socket.close();

                                            } catch (UnknownHostException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();


                                    e_name.setText("");
                                    e_tel.setText("");
                                    e_addr.setText("");

                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    e_name.setText("");
                                    e_tel.setText("");
                                    e_addr.setText("");

                                }
                            })
                    .create();

        }
        ad.show();
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

    private void updateScore() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(HOST, PORT);
                    // 获得输入流
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    BufferedWriter bw = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream()));

                    bw.write(updateStr);
                    bw.flush();
                    String result = br.readLine();
                    Log.d(TAG, "修改信息后服务器返回字符：" + result);
                    br.close();
                    bw.close();
                    socket.close();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        Log.d(TAG, "开始修改积分信息...");
    }

}
