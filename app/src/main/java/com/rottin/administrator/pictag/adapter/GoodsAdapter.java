package com.rottin.administrator.pictag.adapter;

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
import java.util.HashMap;
import java.util.List;

import com.rottin.administrator.pictag.R;
import com.rottin.administrator.pictag.adapter.GoodsAdapter.Helper;
import com.rottin.administrator.pictag.data.BitmapHelper;
import com.rottin.administrator.pictag.data.GlobalData;
import com.rottin.administrator.pictag.vo.GoodsVO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class GoodsAdapter extends BaseAdapter {
	private Context context;
	private List<GoodsVO> list;
	private ImageLoader mImageLoader;
	private HashMap<Integer,View> viewMap = new HashMap<Integer, View>();
	String url = "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png";
	
	public GoodsAdapter(Context context, List<GoodsVO> list) {
		super();
		this.context = context;
		this.list = list;
		mImageLoader = new ImageLoader();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		// TODO Auto-generated method stub
		Helper h;
		if (viewMap.containsKey(position) || viewMap.get(position) == null) {
			h = new Helper();
			v = LayoutInflater.from(context).inflate(R.layout.goods_grid_item,
					null);
			h.image = (ImageView) v.findViewById(R.id.goods_item_image);
			h.price = (TextView) v.findViewById(R.id.goods_item_price);
			v.setTag(h);
			viewMap.put(position, v);
		}else{
			v = viewMap.get(position);
			h = (Helper) v.getTag();
		}
		h = (Helper) v.getTag();
		h.image.setBackgroundResource(R.drawable.ic_launcher);
		h.price.setText(list.get(position).getPrice() + "积分");
		mImageLoader.showImageByAsyncTask(h.image, list.get(position).getImages());
		
		return v;
	}

	class Helper {
		ImageView image;
		TextView price;
	}

}
