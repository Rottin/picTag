package com.rottin.administrator.pictag.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.rottin.administrator.pictag.activity.GoodsActivity;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class DataInput {
	
	
//	private Handler handle = new Handler() {
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 0:
//				System.out.println("111");
//				Bitmap bmp=(Bitmap)msg.obj;
//				
//				break;
//
//			}
//		};
//	};
	private static  String infor= null;
	static int n;
	
	public static  void init(){
		
//		GlobalData.id.add("1");
//		GlobalData.name.add("�����ߣ�aigo�� MP5���벥����U303 8GB ������ĻMP3 ��ɫ");
//		GlobalData.price.add("450");
//		GlobalData.info.add("����CPUΪ���ļܹ���Rknano��һ����ڵ͹���ARM�������ĵ͹��ġ������ܵĶ�ý��оƬ�����������ȵ�ƽ̨�ܹ�������Ӳ����������ʹ��Rknano�����ںܵ͵�Ƶ���½�����Ƶ���빤�����Ӷ���󽵵ͻ��������Ĺ��ġ�ŷ��Q9ӵ�г���10Сʱ�����ֲ�������ʱ�䡣ǿ���������������������ǿ����ȫ���Ż�����Ƶ����ϵͳ�ͻطŵ�·�������ߴ�85DB����Ⱥ�����CD���������С�");
//		GlobalData.images.add("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png");
//		
//		GlobalData.id.add("2");
//		GlobalData.name.add("ѩ��2014�¿�Ů��OLְҵ����� Ů�����б�����8305 �װ�ɫ");
//		GlobalData.price.add("1450");
//		GlobalData.info.add("���ֹ�Ƥ�������ҡ���տ�ֹ� ���Ͽ�����ʮ��רע���������������������Ʒ�ƻ����299��20Ԫ�룬��499��50Ԫȯ����899��100Ԫȯ��ѧԺ�糱������ر����๦�ܴ�ɫС����ţƤС�������� ˫�� ���� б�� ������һ������ŵ�Ʒ�����������ڹ�� ���� ���κγ��ϳ������裡");
//		GlobalData.images.add("http://imgstore04.cdn.sogou.com/app/a/100520024/877e990117d6a7ebc68f46c5e76fc47a");
		
		
		String[][] newstr = null;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Socket socket;
	    		try {
	    			socket = new Socket("120.25.76.27", 1222);
	    			
	    	        // ���������
	    	       BufferedReader br = new BufferedReader(
	    	                new InputStreamReader(socket.getInputStream()));
	    	        BufferedWriter bw = new BufferedWriter(
	    	                new OutputStreamWriter(socket.getOutputStream()));
	    	        bw.write("[Market]\n");
	    	        bw.flush();
	    	        
	    	        infor = br.readLine().substring(10);
	    	        Log.i("tah", infor);
	    	       
	    	      

	      
	        	n = Integer.parseInt(infor);
    	        
    	        String[][] newstr = new String[n][6];
    	        int i = 0;
    	        
    	      
					//br.readLine();
	    	        while((infor=br.readLine())!=null){
	    	        	Log.i("msg", infor);
	    	        	newstr[i] = infor.split(",");
	                	i++;
	    	        	
	    	        }
	    	        
	    	        for(int a = 0;a<n;a++){
	    	        	GlobalData.id.add(newstr[a][0].substring(7));
	    	        	Log.i("id", GlobalData.id.get(a));
	    	        }
	    	        for(int x = 0;x<n;x++){
	    	        	GlobalData.images.add(newstr[x][1]);
	    	        	Log.i("images", GlobalData.images.get(x));
	    	        }
	    	        for(int x = 0;x<n;x++){
	    	        	GlobalData.name.add(newstr[x][2]);
	    	        	Log.i("name", GlobalData.name.get(x));
	    	        }
	    	        for(int x = 0;x<n;x++){
	    	        	GlobalData.price.add(newstr[x][3]);
	    	        	Log.i("price", GlobalData.price.get(x));
	    	        }
	    	        for(int x = 0;x<n;x++){
	    	        	GlobalData.info.add(newstr[x][4]);
	    	        	Log.i("info", GlobalData.info.get(x));
	    	        }

	    	        br.close();
	    	        bw.close();
	    	        socket.close();
	    	       // a= newstr;
	    	       
	    	        for(int x = 0;x<n;x++){
	    	        	 socket = new Socket("120.25.76.27", 1222);
	 	    			
	 	    	        // ���������
	 	    	       br = new BufferedReader(
	 	    	                new InputStreamReader(socket.getInputStream()));
	 	    	        bw = new BufferedWriter(
	 	    	                new OutputStreamWriter(socket.getOutputStream()));
	    	        	bw.write("[GetPic]"+GlobalData.images.get(x)+",s\n");
	    	        	Log.i("tah", GlobalData.images.get(x));
	    	        	bw.flush();
	    	        	GlobalData.images.set(x, "http://www.bi-home.cn/software/stuff/"+br.readLine());
	    	        	Log.i("tah", GlobalData.images.get(x));
	    	        	br.close();
	    	        	bw.close();
	    	        	socket.close();
	    	        }
				} 
//	    		catch(NullPointerException e) {
//					Message msg = new Message();
//		        	msg.what = 0x122;
//		        	msg.obj  = "�������ӳ�ʱ����";
//				}
	    		
	    		
	    		catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	        
//	            Handler handler = new Handler();
//	            Message msg = new Message();
//	            msg.obj = a;
//	            handler.sendMessage(msg);
	        
	  
//	    for(int o = 0;o<a.length;o++){
//	    	for(int p = 0; p<a[o].length;p++){
//	    		Log.i("tag", a[o][p]);
//	    	}
//	    }
//	    		GlobalData.images.set(0,"https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png");
//	    		GlobalData.images.set(1,"https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png");
//	    		GlobalData.images.set(2,"https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png");
//	    		GlobalData.images.set(3,"https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png");
			}
		}).start();
	}
	
	
}
