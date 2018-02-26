package com.rottin.administrator.pictag.activity;

import com.rottin.administrator.pictag.R;
import com.rottin.administrator.pictag.data.GlobalData;
import com.rottin.administrator.pictag.vo.AddressVO;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UpdateAddressActivity extends Activity {
	private int index;
	private EditText ename;
	private EditText etel;
	private EditText eaddr;
	private Button submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_address);
		Intent intent = getIntent();
		index = intent.getExtras().getInt("addressIndex");
		data = GlobalData.addresslist.get(index);
		init();
		
		ename.setText(data.getName());
		etel.setText(data.getTel());
		eaddr.setText(data.getAddress());
	}

	private void init() {
		// TODO Auto-generated method stub
		TextView left = (TextView) findViewById(R.id.title_left);
		TextView center = (TextView) findViewById(R.id.title_center);
		TextView right = (TextView) findViewById(R.id.title_right);
		left.setText("返回");
		center.setText("修改收货地址ַ");
		right.setText("");
		left.setOnClickListener(onClick);
		ename = (EditText) findViewById(R.id.update_address_name);
		etel = (EditText) findViewById(R.id.update_address_tel);
		eaddr = (EditText) findViewById(R.id.update_address_address);
		submit = (Button) findViewById(R.id.update_address_submit);
		submit.setOnClickListener(onClick);

	}

	View.OnClickListener onClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.update_address_submit:
				String name = ename.getText().toString().trim();
				String tel = etel.getText().toString().trim();
				String addr = eaddr.getText().toString().trim();
				data.setName(name);
				data.setAddress(addr);
				data.setTel(tel);
				
				// GlobalData.addressList.set(index, object);
				setResult(RESULT_OK);
				break;
			}
			finish();
		}

	};

	private AddressVO data;
}
//	TextView left, center, right;
//	Button submit;
//	private EditText name;
//	private int location;
//	private EditText tel;
//	private EditText address;
//	private AddressVO addressvo;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_update_address);
//		init();
//
//		// ��ȡ��һ��Activity���ݹ���������
//		Intent intent = getIntent();
//		location = intent.getExtras().getInt("addressId");
//		//
//		AddressVO vo=(AddressVO) intent.getExtras().get("address");
//		// ���ݴ��ݹ�����List ���±��ҵ��û���Ϣ
//		addressvo = GlobalData.addresslist.get(location);
//		left.setOnClickListener(onClick);
//		submit = (Button) findViewById(R.id.update_address_submit);
//		submit.setOnClickListener(onClick);
//		
//		name = (EditText) findViewById(R.id.update_address_name);
//		tel = (EditText) findViewById(R.id.update_address_tel);
//		address = (EditText) findViewById(R.id.update_address_address);
//		
//		name.setText(addressvo.getName());
//		tel.setText(addressvo.getTel());
//		address.setText(addressvo.getAddress());
//	}
//
//	View.OnClickListener onClick = new View.OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			switch (v.getId()) {
//			case R.id.title_left:
//				finish();
//				break;
//
//			default:
//				String uname = name.getText().toString().trim();
//				String utel = tel.getText().toString().trim();
//				String uaddress = address.getText().toString().trim();
//				TextView hint_txt;
//				hint_txt = (TextView) findViewById(R.id.update_address_hint_name);
//				if (uname.equals("")) {
//					hint_txt.setText("��������Ϊ��");
//					return;
//				} else {
//					hint_txt.setText("");
//				}
//				hint_txt = (TextView) findViewById(R.id.update_address_hint_tel);
//				if (utel.equals("")) {
//					hint_txt.setText("�绰����Ϊ��");
//					return;
//				} else {
//					hint_txt.setText("");
//				}
//				hint_txt = (TextView) findViewById(R.id.update_address_address);
//				if (uaddress.equals("")) {
//					hint_txt.setText("��ַ����Ϊ��");
//					return;
//				} else {
//					hint_txt.setText("");
//				}
//
//				GlobalData.addresslist.set(location,new AddressVO(addressvo.getName(),
//						addressvo.getTel(),addressvo.getAddress()));
//				Toast.makeText(UpdateAddressActivity.this, "�޸ĳɹ�",
//						Toast.LENGTH_SHORT).show();
//				finish();
//				break;
//			}
//
//		}
//	};
//
//	private void init() {
//		// TODO Auto-generated method stub
//		left = (TextView) findViewById(R.id.title_left);
//		center = (TextView) findViewById(R.id.title_center);
//		right = (TextView) findViewById(R.id.title_right);
//		left.setText("");
//		center.setText("�޸ĵ�ַ");
//		right.setText("");
//	}
//}
