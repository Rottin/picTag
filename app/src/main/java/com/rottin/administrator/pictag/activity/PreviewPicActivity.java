package com.rottin.administrator.pictag.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rottin.administrator.pictag.R;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URI;
import java.sql.Time;
import java.sql.Timestamp;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.handle;
import static android.R.attr.tag;

public class PreviewPicActivity extends AppCompatActivity {

    private static final String TAG = "PreviewActivity";
    private ImageView img;
    private Button btn;
    private TextView back;
    private int TYPE;
    private EditText edit1, edit2, edit3, edit4;
    private String result, mUsername;
    private ProgressBar progressBar;
    private int flag = 0;

    /***
     * 使用照相机拍照获取图片
     */
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    /***
     * 使用相册中的图片
     */
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2;

    /***
     * 从Intent获取图片路径的KEY
     */
    public static final String KEY_PHOTO_PATH = "photo_path";
    private Uri photoUri;
    private String picPath;
    private Intent lastIntent;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x600:
                    Toast.makeText(PreviewPicActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 0x601:
                    Toast.makeText(PreviewPicActivity.this, "没有有效输入！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_pic);
        initView();
        lastIntent = getIntent();
        TYPE = lastIntent.getExtras().getInt("TYPE");
        mUsername = lastIntent.getExtras().getString("username");
        if (TYPE == 1) {
            takePhoto();
            Log.d(TAG, "调用takePhoto");
        } else if (TYPE == 2) {
            pickPhoto();
            Log.d(TAG, "调用pickPhoto");
        } else
            Toast.makeText(PreviewPicActivity.this, "出现错误，请重试", Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        back = (TextView) findViewById(R.id.preview_return);
        btn = (Button) findViewById(R.id.preview_uploadbtn);
        img = (ImageView) findViewById(R.id.preview_img);
        edit1 = (EditText) findViewById(R.id.editText1);
        edit2 = (EditText) findViewById(R.id.editText2);
        edit3 = (EditText) findViewById(R.id.editText3);
        edit4 = (EditText) findViewById(R.id.editText4);
        progressBar = (ProgressBar) findViewById(R.id.preview_progressbar);
        progressBar.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "sending...");
                sendPic();
            }
        });
    }

    private void sendPic() {
        Log.d(TAG, "开始发送");
        progressBar.setVisibility(View.VISIBLE);
        //压缩图片  保存在"/mnt/sdcard/picTag/"+filename 目录下
        File oriFile = new File(picPath);
        try {
            saveFile(getimage(picPath), "/storage/emulated/0/DCIM/picTag/", oriFile.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        final File file = new File("/storage/emulated/0/DCIM/picTag/" + oriFile.getName());
        if (file.exists() && file.isFile() && (file.getName().endsWith(".jpg") || file.getName().endsWith(".png") || file.getName().endsWith(".jpeg"))) {
            Log.d(TAG, "文件格式正确,文件名为：" + file.getName());
        } else {
            Log.d(TAG, "文件格式错误,文件名为：" + file.getName());
            Toast.makeText(PreviewPicActivity.this, "图片格式错误，请重新选择或拍摄", Toast.LENGTH_SHORT).show();
        }
        AsyncTask<Void, String, Void> read = new AsyncTask<Void, String, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Socket socket = new Socket("120.25.76.27", 1222);
                    FileInputStream fis = new FileInputStream(file);
                    OutputStream out = socket.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    String sendStr = "[SendPic]";
                    int length = 0;
                    while ((len = fis.read(buffer)) != -1) {
                        length += len;
                    }
                    Log.d(TAG, "长度为：" + length);
                    sendStr += (length + ",p\n");
                    writer.write(sendStr);
                    writer.flush();
                    Log.d(TAG, "已经发送sendStr");
                    fis = new FileInputStream(file);
                    while ((len = fis.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    out.flush();
                    Log.d(TAG, "发送图片完成");
                    result = reader.readLine();
                    publishProgress(result);
                    Log.d(TAG, "获取到返回值：" + result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                Log.d(TAG, "处理返回数据...");
                //上传失败
                if (result.toString().equals("0")) {
                    Toast.makeText(PreviewPicActivity.this, "图片上传失败，网络或服务器错误！", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "图片上传失败，网络或服务器错误！");
                    finish();
                } else {
                    //上传成功 result为picid
                    //打标签
                    send();
                }

                super.onProgressUpdate(values);
            }
        };
        read.execute();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            doPhoto(requestCode, data);
            Log.i(TAG, "最终选择的图片=" + picPath);
            if (picPath == null || picPath.equals("")) {
                img.setImageResource(R.drawable.ic_error);
                Toast.makeText(this, "选择图片文件出错，请重试", Toast.LENGTH_LONG).show();
                finish();
            } else {
//                Bitmap bm = BitmapFactory.decodeFile(picPath);
                img.setImageBitmap(getimage(picPath));
            }
        }
    }

    /*******拍摄/选取照片代码**********/
    private void takePhoto() {
        Log.d("Main", "takePhoto");
        //执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
            ContentValues values = new ContentValues();
            photoUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
            /**-----------------*/
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
            Log.d("Main", "takePhoto调用完成");
        } else {
            Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
    }

    private void doPhoto(int requestCode, Intent data) {
        if (requestCode == SELECT_PIC_BY_PICK_PHOTO)  //从相册取图片，有些手机有异常情况，请注意
        {
            if (data == null) {
                Toast.makeText(this, "选择图片文件出错，请重试", Toast.LENGTH_LONG).show();
                img.setImageResource(R.drawable.ic_error);
                finish();
                return;
            }
            photoUri = data.getData();
            if (photoUri == null) {
                Toast.makeText(this, "选择图片文件出错，请重试", Toast.LENGTH_LONG).show();
                img.setImageResource(R.drawable.ic_error);
                return;
            }
        }
        String[] pojo = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(photoUri, pojo, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
            cursor.moveToFirst();
            picPath = cursor.getString(columnIndex);
//            cursor.close();
        }
        Log.i(TAG, "imagePath = " + picPath);
        if (picPath != null && (picPath.endsWith(".png") || picPath.endsWith(".PNG") || picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))) {
            lastIntent.putExtra(KEY_PHOTO_PATH, picPath);
            setResult(Activity.RESULT_OK, lastIntent);
//            finish();
        } else {
            Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
        }
    }

    /************选取照片部分结束********************/

    //压缩图片
    public static Bitmap getimage(String srcPath) {

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;

        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static File saveFile(Bitmap bm, String path, String fileName) throws IOException {
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path, fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;
    }

    public void send() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Log.d(TAG, "Sending tag...");
                    Socket socket1;
                    socket1 = new Socket("120.25.76.27", 1222);
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(socket1.getOutputStream()));
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(socket1.getInputStream()));
                    String sendstr = "[Submit]" + mUsername;
                    if (edit1.getText().toString().trim().length() != 0)
                        sendstr += "," + result + "," + edit1.getText().toString();
                    if (edit2.getText().toString().trim().length() != 0)
                        sendstr += "," + result + "," + edit2.getText().toString();
                    if (edit3.getText().toString().trim().length() != 0)
                        sendstr += "," + result + "," + edit3.getText().toString();
                    if (edit4.getText().toString().trim().length() != 0)
                        sendstr += "," + result + "," + edit4.getText().toString();
                    if (sendstr == "[Submit]" + mUsername) {
                        Log.d(TAG, "没有有效输入");
                        Message msg = new Message();
                        msg.what=0x601;
                        handler.sendMessage(msg);
                        return;
                    } else
                        sendstr += "\n";
                    Log.d(TAG, "打标签：" + sendstr);
                    //writer.write("[Submit]"+"username"+", "+"picid"+","+edit1.getText().toString()+"picid"+","+edit2.getText().toString()+"\n");
                    writer.write(sendstr);
                    writer.flush();
                    writer.close();
                    socket1.close();
                    Log.d(TAG, "打标签成功");
                    Message msg = new Message();
                    msg.what=0x600;
                    handler.sendMessage(msg);
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
}
