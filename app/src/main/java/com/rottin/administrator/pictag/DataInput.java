package com.rottin.administrator.pictag;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class DataInput {
	
	
	private Handler handle = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				System.out.println("111");
				Bitmap bmp=(Bitmap)msg.obj;
				
				break;

			}
		};
	};
	private static  String infor= null;
	
	public static  String[][] init(){
		String[][] newstr = null;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Socket socket;
	    		try {
	    			socket = new Socket("120.25.76.27", 1222);

	    	       BufferedReader br = new BufferedReader(
	    	                new InputStreamReader(socket.getInputStream()));
	    	        BufferedWriter bw = new BufferedWriter(
	    	                new OutputStreamWriter(socket.getOutputStream()));
	    	        bw.write("[Market]\n");
	    	        bw.flush();
	    	        infor = br.readLine().substring(10);
	      
	        	int n = Integer.parseInt(infor);
    	        
    	        String[][] newstr = new String[n][6];
    	        int i = 0;
    	        
    	      
					//br.readLine();
	    	        while((infor=br.readLine())!=null) {
						Log.i("msg", infor);
						newstr[i] = infor.split(",");
						i++;

					}
				}
	    		catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	    
			}
		}).start();
		
		return newstr;
		
	    	
	}
	
	
}
