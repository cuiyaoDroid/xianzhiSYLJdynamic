package com.xianzhi.tool.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.xianzhi.tool.db.DynamicHolder;
import com.xianzhisylj.dynamic.R;



public class DynamicAdapter extends SimpleAdapter {
	// ���ڶ�̬������һ�������ļ�
	private List<Map<String, Object>> styles = null;
	private LayoutInflater inflater = null;
	
	public List<Map<String, Object>> getStyles() {
		return styles;
	}

	public void setStyles(List<Map<String, Object>> styles) {
		this.styles = styles;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View result = super.getView(position, convertView, parent);
		if(position%2==1){
			result.setBackgroundResource(R.drawable.listitem_bg_double);
		}
		Integer state=(Integer) styles.get(position).get("station");
		TextView state_txt=(TextView)result.findViewById(R.id.station_txt);
		
		if(state==2){
			int color = context.getResources().getColor(R.color.txt_green); 
			state_txt.setTextColor(color);
			state_txt.setText("����");
		}else if(state==3){
			int color = context.getResources().getColor(R.color.Red); 
			state_txt.setTextColor(color);
			state_txt.setText("����");
		}else{
			int color = context.getResources().getColor(R.color.txt_blue); 
			state_txt.setTextColor(color);
			state_txt.setText("����");
		}
		return result;
	}
	public void notify_change() {
		this.notifyDataSetChanged();
	}
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}
	private Context context;
	@SuppressWarnings("unchecked")
	public DynamicAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.context=context;
		inflater = LayoutInflater.from(context);
		styles = (List<Map<String, Object>>) data;
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if (observer != null) {
			super.unregisterDataSetObserver(observer);
		}
	}
}