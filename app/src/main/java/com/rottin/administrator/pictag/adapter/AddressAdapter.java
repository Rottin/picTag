package com.rottin.administrator.pictag.adapter;

import java.util.List;
import com.rottin.administrator.pictag.R;
import com.rottin.administrator.pictag.vo.AddressVO;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class AddressAdapter extends BaseAdapter {

	Context context;
	List<AddressVO> list;

	public AddressAdapter(Context context, List<AddressVO> list) {
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
	public View getView(final int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		Helper h;
		if (view == null) {
			h = new Helper();
			view = LayoutInflater.from(context).inflate(R.layout.address_item,null);
			h.isDefault = (RadioButton) view.findViewById(R.id.address_item_bt);
			h.name = (TextView) view.findViewById(R.id.address_item_name);
			h.tel = (TextView) view.findViewById(R.id.address_item_phone);
			h.addr = (TextView) view.findViewById(R.id.address_item_addr);
//			h.index = position;
			view.setTag(h);
		}
		h = (Helper) view.getTag();
		h.isDefault.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (AddressVO vo : list) {
					vo.setDefault(false);
				}
//				Log.i("AddressOutif", "----->>" + position);
				list.get(position).setDefault(true);
				AddressAdapter.this.notifyDataSetChanged();
			}
		});
		h.isDefault.setChecked(list.get(position).isDefault());
		h.name.setText("姓名：" + list.get(position).getName());
		h.tel.setText("电话：" + list.get(position).getTel());
		h.addr.setText("地址：" + list.get(position).getAddress());
		return view;
	}

	class Helper {
		int index;
		RadioButton isDefault;
		TextView name;
		TextView tel;
		TextView addr;
	}

}
