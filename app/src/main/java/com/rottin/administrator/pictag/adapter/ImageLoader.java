package com.rottin.administrator.pictag.adapter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import com.rottin.administrator.pictag.Util;

public class ImageLoader {
	
	private ImageView mImageView;
	private String mUrl;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(mImageView.getTag().equals(mUrl)){
				mImageView.setImageBitmap((Bitmap) msg.obj);
			}
		
		};
	};
	//创建Cache
	private LruCache<String, Bitmap> mCaches;

	public ImageLoader() {
		// TODO Auto-generated constructor stub
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory/4;
		mCaches = new LruCache<String, Bitmap>(cacheSize){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// 每次存入时调用
				return value.getByteCount();
			}
		};
	}

	//增加到缓存
	public void addBitmapToCache(String url, Bitmap bitmap) {
		if(getBitmapFromCache(url)==null){
			mCaches.put(url, bitmap);
		}
	}
	//从缓存中获取数据
	public Bitmap getBitmapFromCache(String url){
		return mCaches.get(url);

	}
	
	public void showImageByThread(ImageView imageView,final String url){
		
		mImageView = imageView;
		mUrl = url;
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				Bitmap bitmap = getBitmapFromURL(url);
				Message message = Message.obtain();
				message.obj = bitmap;
				handler.sendMessage(message);
			}
		}.start();
		
	}
	
	public Bitmap getBitmapFromURL(String urlString){
		Bitmap bitmap;
		InputStream is = null;
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			is = new BufferedInputStream(connection.getInputStream());
			bitmap = BitmapFactory.decodeStream(is);
			connection.disconnect();
			return bitmap;
			
		}
		catch (java.io.IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void showImageByAsyncTask(ImageView imageView ,String url){
		//从缓存中取出图片
		Bitmap bitmap = getBitmapFromCache(url);
		if(bitmap == null){
			new NewsAsyncTask(imageView,url).execute(url);
		}else{
			imageView.setImageBitmap(bitmap);
		}

	}
	
	private class NewsAsyncTask extends AsyncTask<String, Void, Bitmap>{

		private String mUrl;
		private ImageView mImageView;
		
		public NewsAsyncTask(ImageView imageView,String url) {
			// TODO Auto-generated constructor stub
			mImageView =imageView;
			mUrl = url;
		}
		
		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
//			return getBitmapFromURL(params[0]);
			//从网络获取图片
			Bitmap bitmap = getBitmapFromURL(params[0]);
			if(bitmap != null){
				addBitmapToCache(params[0], bitmap);
			}
			return bitmap;
		}
		
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			// TODO Auto-generated method stub
			super.onPostExecute(bitmap);
			
				mImageView.setImageBitmap(bitmap);
			
		}
	}
}
