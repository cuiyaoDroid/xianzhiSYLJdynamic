package com.xianzhi.tool.adapter;

import java.util.List;
import java.util.Map;

import com.xianzhisylj.dynamiclcz.R;



import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableContractAdapter extends BaseExpandableListAdapter {
	private List<List<Map<String, Object>>> children;
	private List<Map<String, Object>> group;
	private String[] childFrom, groupFrom;
	private int[] childTo, groupTo;
	private int clayout, glayout;
	private LayoutInflater inflater;
	private Context context;

	public ExpandableContractAdapter(Context context, List<Map<String, Object>> group,
			int glayout, String[] groupFrom, int[] groupTo,
			List<List<Map<String, Object>>> children, int clayout,
			String[] childFrom, int[] childTo) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.group = group;
		this.glayout = glayout;
		this.groupFrom = groupFrom;
		this.groupTo = groupTo;
		this.children = children;
		this.clayout = clayout;
		this.childFrom = childFrom;
		this.childTo = childTo;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return children.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View cv;
		if (convertView == null)
			cv = newChildView(parent);
		else
			cv = convertView;
		TextView tv = (TextView) cv.findViewById(childTo[0]);
		String content = (String) children.get(groupPosition)
				.get(childPosition).get(childFrom[0]);
		tv.setText(content);
		TextView se_tv = (TextView) cv.findViewById(childTo[1]);
		String se_content = (String) children.get(groupPosition)
				.get(childPosition).get(childFrom[1]);
		se_tv.setText(se_content);
		CheckBox check = (CheckBox) cv.findViewById(childTo[2]);
		Boolean se_check = (Boolean) children.get(groupPosition)
				.get(childPosition).get(childFrom[2]);
		check.setChecked(se_check);
		return cv;
	}

	private View newChildView(ViewGroup parent) {
		// TODO Auto-generated method stub
		return inflater.inflate(clayout, parent, false);
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return children.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return group.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return group.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View gv;
		if (convertView == null)
			gv = newGroupView(parent);
		else
			gv = convertView;
		TextView tv = (TextView) gv.findViewById(groupTo[0]);
		String content = (String) group.get(groupPosition).get(groupFrom[0]);
		tv.setText(content);
		ImageView mgroupimage=(ImageView)gv.findViewById(R.id.expandsimageView);
        if(!isExpanded){
             mgroupimage.setImageResource(R.drawable.bg_contact_item);
         }else{
             mgroupimage.setImageResource(R.drawable.bg_contact_item_on);                    
         }
		return gv;
	}

	private View newGroupView(ViewGroup parent) {
		// TODO Auto-generated method stub
		return inflater.inflate(glayout, parent, false);
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if (observer != null) {
			super.unregisterDataSetObserver(observer);
		}
	}
}