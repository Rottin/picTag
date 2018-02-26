package com.rottin.administrator.pictag.adapter;

import com.rottin.administrator.pictag.R;
import com.rottin.administrator.pictag.data.GlobalData;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class SortAdapter implements ExpandableListAdapter {
	Context context;

	 String[] group;
	 String[][] child;
	
	public SortAdapter(Context context) {
		super();
		this.context = context;
		//group=GlobalData.type;
		//child=GlobalData.type2;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return group.length;
	}
	
	TextView groupTxt;
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.expandable_group_item, null);
			groupTxt=(TextView) view.findViewById(R.id.expandable_group);
			view.setTag(groupTxt);
		}
		groupTxt=(TextView) view.getTag();
		groupTxt.setText(group[groupPosition]);
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return child[groupPosition].length;
	}
	
	TextView childTxt;
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.expandable_child_item, null);
			childTxt=(TextView) view.findViewById(R.id.expandable_child);
			view.setTag(childTxt);
		}
		childTxt=(TextView) view.getTag();
		childTxt.setText(child[groupPosition][childPosition]);
		return view;
	}
	
	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getCombinedChildId(long groupId, long childId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getCombinedGroupId(long groupId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

}
