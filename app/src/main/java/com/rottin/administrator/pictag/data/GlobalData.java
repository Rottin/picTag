package com.rottin.administrator.pictag.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.rottin.administrator.pictag.R;
import com.rottin.administrator.pictag.vo.AddressVO;
import com.rottin.administrator.pictag.vo.GoodsVO;



public class GlobalData {

	public static List<GoodsVO> goodsList = new ArrayList<GoodsVO>();
	public static Set<Integer> collectlist = new HashSet<Integer>();
	public static List<AddressVO> addresslist = new ArrayList<AddressVO>();
	public static String a[][] = null;
	///-----------------------------------------------------------------------------------
	public static String[][] newstr  = {{"9876543321","532524143","5453","53","435","8"},{"12345678","23456789","3","456","5","6"},{"123456787","234567896","35","442","55","66"},{"1234567885","23456755589","3","583","5","6"},{"1234567895","23456789","3","786","5","6"}};
	public static int[] goal = new int[100];
	public static int[] pid = new int[100];
	public static int pidlength;
	public static ArrayList<String> name = new ArrayList<String>();
	public static ArrayList<String> images = new ArrayList<String>();
	public static ArrayList<String> price = new ArrayList<String>();
	public static ArrayList<String> picid = new ArrayList<String>();
	public static ArrayList<String> info = new ArrayList<String>();
	public static int[] number = new int[100];
	public static String[] childType = new String[] { "2", "2", "1", "1", "1",
			"1", "2", "4", "2", "1", "1", "1", "1", "1", "2" };
	public static int[] image = new int[] { R.drawable.mp4, R.drawable.bag,
			R.drawable.bqz, R.drawable.bt, R.drawable.ibm,
			R.drawable.nokia3310, R.drawable.dd, R.drawable.dv, R.drawable.eh,
			R.drawable.mp4, R.drawable.hqz, R.drawable.hs, R.drawable.watch,
			R.drawable.wj, R.drawable.xk };
	public static String[] parentType = new String[] { "1", "2", "3", "4", "5",
			"6", "3", "1", "7", "3", "3", "5", "7", "8", "8" };
	public static ArrayList<String> id = new ArrayList<String>();
	

}
