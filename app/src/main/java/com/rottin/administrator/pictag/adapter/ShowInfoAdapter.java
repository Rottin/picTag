package com.rottin.administrator.pictag.adapter;

import java.util.List;

import com.rottin.administrator.pictag.R;
import com.rottin.administrator.pictag.vo.GoodsVO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class ShowInfoAdapter extends BaseAdapter {
	Context context;
	List<GoodsVO> list;

	public ShowInfoAdapter(Context context, List<GoodsVO> list) {
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
	public Object getItem(int arg0) {
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
		if (view == null) {
			h = new Helper();
			view = LayoutInflater.from(context).inflate(
					R.layout.show_info_list_item, null);
			h.image = (ImageView) view.findViewById(R.id.show_info_image_item);
			h.name = (TextView) view.findViewById(R.id.show_info_name_item);
			h.price = (TextView) view.findViewById(R.id.show_info_price_item);
			view.setTag(h);
		}
		h = (Helper) view.getTag();
		h.image.setImageResource(list.get(position).getImage());
		h.name.setText(list.get(position).getName());
		h.price.setText(list.get(position).getPrice()+"����");
		return view;
	}

	class Helper {
		ImageView image;
		TextView name;
		TextView price;
	}

}
