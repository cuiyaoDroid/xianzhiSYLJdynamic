package com.xianzhi.tool.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.xianzhi.tool.db.DynamicListHelper;
import com.xianzhisylj.dynamiclcz.R;
import com.xianzhisylj.dynamiclcz.addNewDynamicActivity;



public class DynamicAdapter extends SimpleAdapter {
	// 用于动态的载入一个界面文件
	private List<Map<String, Object>> styles = null;
	private LayoutInflater inflater = null;
	
	public List<Map<String, Object>> getStyles() {
		return styles;
	}

	public void setStyles(List<Map<String, Object>> styles) {
		this.styles = styles;
	}
	public interface clickCallBack{
		void onClick(int position);
	}
	
	public clickCallBack getCallback() {
		return callback;
	}

	public void setCallback(clickCallBack callback) {
		this.callback = callback;
	}
	private clickCallBack callback;
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View result = super.getView(position, convertView, parent);
		result.setBackgroundResource(position%2==1?R.drawable.listitem_bg_double:R.drawable.listitem_bg_single);
		Integer state=(Integer) styles.get(position).get("station");
		Integer isFinal=(Integer) styles.get(position).get("isFinal");
		TextView state_txt=(TextView)result.findViewById(R.id.station_txt);
		String user_name=(String) styles.get(position).get(DynamicListHelper.user_name);
		TextView owner_txt=(TextView)result.findViewById(R.id.owner_txt);
		TextView users_txt=(TextView)result.findViewById(R.id.users_txt);
		users_txt.setVisibility(user_name.trim().length()==0?View.GONE:View.VISIBLE);
		int index=user_name.indexOf(",");
		if(index==-1){
			users_txt.setText(user_name);
		}else{
			users_txt.setText(user_name.substring(0,index)+"...");
		}
		
		owner_txt.setText(isFinal==1?"已提交":Html.fromHtml("<u>确认</u>"));
		owner_txt.setEnabled(isFinal!=1);
		owner_txt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(callback!=null){
					callback.onClick(position);
				}
			}
		});
		int color;
		switch (state) {
		case 1:
			color = context.getResources().getColor(R.color.txt_gray); 
			state_txt.setTextColor(color);
			state_txt.setText("待发");
			break;
		case 2:
			color = context.getResources().getColor(R.color.txt_darkgreen); 
			state_txt.setTextColor(color);
			state_txt.setText("到达");
			break;
		case 3:
			color = context.getResources().getColor(R.color.Red); 
			state_txt.setTextColor(color);
			state_txt.setText("晚点");
			break;
		case 4:
			color = context.getResources().getColor(R.color.txt_blue); 
			state_txt.setTextColor(color);
			state_txt.setText("提前");
			break;
		case 5:
			color = context.getResources().getColor(R.color.txt_green); 
			state_txt.setTextColor(color);
			state_txt.setText("正点");
			break;
		default:
			color = context.getResources().getColor(R.color.txt_green); 
			state_txt.setTextColor(color);
			state_txt.setText("正点");
			break;
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
