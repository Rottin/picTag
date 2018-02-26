package com.rottin.administrator.pictag.activity;

import java.util.ArrayList;
import java.util.List;
import com.rottin.administrator.pictag.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class Test extends Activity {
	int[] image = new int[] { R.drawable.bag, R.drawable.bag, R.drawable.bqz,
			R.drawable.bt, R.drawable.bt, R.drawable.dv, R.drawable.ibm,
			R.drawable.nokia3310, R.drawable.dv, R.drawable.bag, R.drawable.ibm,
			R.drawable.nokia3310 };
	String[] price = new String[] { "$450", "$1450", "$150", "$45", "$45",
			"$999", "$6645", "$450", "$999", "$1450", "$6645", "$450" };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods);
		GridView gv=(GridView) findViewById(R.id.goods_grid_list);
		MyAdatper adapter=new MyAdatper(this,getData());
		gv.setAdapter(adapter);
	}
	private List<GoodsVO> getData(){
		List<GoodsVO> list =new ArrayList<GoodsVO>();
		for(int i=0;i<image.length;i++){
			list.add(new GoodsVO(image[i],price[i]));
		}
		return list;
	}
	class GoodsVO{
		int image;
		String price;
		public GoodsVO(int image, String price) {
			super();
			this.image = image;
			this.price = price;
		}
		public int getImage() {
			return image;
		}
		public void setImage(int image) {
			this.image = image;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		
	}
	@SuppressLint("ViewHolder")
	class MyAdatper extends BaseAdapter{
		Context context;
		List<GoodsVO> list;
		public MyAdatper(Context context, List<GoodsVO> list) {
			super();
			this.context = context;
			this.list = list;
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
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v=LayoutInflater.from(context).inflate(R.layout.goods_grid_item, null);
			ImageView iv=(ImageView) findViewById(R.id.goods_item_image);
			TextView tv=(TextView) findViewById(R.id.goods_item_price);
			iv.setImageResource(list.get(position).getImage());
			tv.setText(list.get(position).getPrice());
			return v;
		}
	}

}
