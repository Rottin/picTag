package com.rottin.administrator.pictag.adapter;

import java.util.List;
import com.rottin.administrator.pictag.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class AffirmAdapter extends BaseAdapter {

	Context context;
	
	public AffirmAdapter(Context context) {
		super();
		this.context = context;
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
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		Helper h;
		if(view ==null ){
			h = new Helper();
			view = LayoutInflater.from(context).inflate(R.layout.affirm_list_item, null);
			h.image = (ImageView) view.findViewById(R.id.affirm_list_item_image);
			h.name = (TextView) view.findViewById(R.id.affirm_list_item_name);
			h.price = (TextView) view.findViewById(R.id.affirm_list_item_price);
			h.num = (TextView) view.findViewById(R.id.affirm_list_item_num);
			view.setTag(h);
				
		}
		h = (Helper) view.getTag();
		return view;
	}
	class Helper{
		ImageView image;
		TextView name;
		TextView price;
		TextView num;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

}
