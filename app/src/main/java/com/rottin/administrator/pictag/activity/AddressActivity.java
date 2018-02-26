package com.rottin.administrator.pictag.activity;

import com.rottin.administrator.pictag.R;
import com.rottin.administrator.pictag.adapter.AddressAdapter;
import com.rottin.administrator.pictag.data.GlobalData;
import com.rottin.administrator.pictag.vo.AddressVO;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

@SuppressLint("InflateParams")
public class AddressActivity extends Activity {

	private static final int ITEM_DEL = 0;
	private static final int ITEM_UPDATE = 1;
	ListView lv;
	AddressAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		init();
		adapter = new AddressAdapter(this, GlobalData.addresslist);
		lv.setAdapter(adapter);
		registerForContextMenu(lv);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.setHeaderTitle("操作");
		menu.add(0, ITEM_DEL, 0, "修改");
		menu.add(0, ITEM_UPDATE, 1, "删除");
		menu.setHeaderIcon(android.R.drawable.ic_dialog_dialer);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case ITEM_DEL:
			Intent intent = new Intent(AddressActivity.this,
					UpdateAddressActivity.class);
			intent.putExtra("addressIndex", info.position);
			startActivityForResult(intent, 1);
			break;
		case ITEM_UPDATE:
			GlobalData.addresslist.remove(info.position);
			break;
		}
		adapter.notifyDataSetChanged();
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {// 说明是UpdataAddressActivity返回
			if (resultCode == RESULT_OK) {
				adapter.notifyDataSetChanged();
			}
		}
	}

	private void init() {
		// TODO Auto-generated method stub
		TextView left = (TextView) findViewById(R.id.title_left);
		TextView center = (TextView) findViewById(R.id.title_center);
		TextView right = (TextView) findViewById(R.id.title_right);
		left.setText("返回");
		center.setText("收货地址ַ");
		right.setText("添加");
		left.setOnClickListener(onClick);
		right.setOnClickListener(onClick);
		lv = (ListView) findViewById(R.id.address_lv);
	}

	View.OnClickListener onClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.title_left:
				finish();
				
				break;

			case R.id.title_right:
				addDialog();
				break;
			}
		}
	};
	/**
	 * ��ӶԻ���
	 */
	private AlertDialog ad = null;

	protected void addDialog() {
		if (ad == null) {
			final View view = LayoutInflater.from(this).inflate(
					R.layout.addr_dialog_item, null);
			final EditText e_name = (EditText) view
					.findViewById(R.id.addr_dialog_item_name);
			final EditText e_tel = (EditText) view
					.findViewById(R.id.addr_dialog_item_tel);
			final EditText e_addr = (EditText) view
					.findViewById(R.id.addr_dialog_item_addr);
			ad = new AlertDialog.Builder(this)
					.setTitle("添加收货地址ַ")
					.setView(view)
					.setPositiveButton("添加",
							new DialogInterface.OnClickListener() {// ���һ��ȷ�ϰ�ť
								@Override
								// ȷ�ϰ�ť�ĵ���¼�
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									// ��ȡView�����е�����е�ֵ

									String name = e_name.getText().toString()
											.trim();
									String tel = e_tel.getText().toString()
											.trim();
									String addr = e_addr.getText().toString()
											.trim();

									adapter.notifyDataSetChanged();
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

}
