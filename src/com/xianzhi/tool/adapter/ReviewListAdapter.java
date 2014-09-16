package com.xianzhi.tool.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.DataSetObserver;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.xianzhi.tool.db.ReviewHelper;
import com.xianzhisylj.dynamic.R;


public class ReviewListAdapter extends SimpleAdapter {
	// 用于动态的载入一个界面文件
	private List<Map<String, Object>> styles = null;

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
		TextView content_txt = (TextView)result.findViewById(R.id.content_txt);
		String create_time=(String) styles.get(position).get(ReviewHelper.CREATE_TIME);
		String role_names=(String) styles.get(position).get(ReviewHelper.POSITION_NAME);
		String user_name=(String) styles.get(position).get(ReviewHelper.USER_NAME);
		int msg_type=(Integer) styles.get(position).get(ReviewHelper.MSG_TYPE);
		String message=(String) styles.get(position).get(ReviewHelper.MESSAGE);
		String str_submit=create_time+"\u3000"+role_names+" <font color=#006AE2>" 
				+ user_name + "</font>"+"\u3000";
		switch (msg_type) {
		case 1:
			str_submit+="已阅。";
			break;
		case 2:
			str_submit+=message;
			break;
		case 3:
			str_submit+="提交本次乘务报告的最终版本。";
			break;
		default:
			break;
		}
		
		content_txt.setText(Html.fromHtml(str_submit));
		
		
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
	public ReviewListAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		styles = (List<Map<String, Object>>) data;
		this.context=context;
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if (observer != null) {
			super.unregisterDataSetObserver(observer);
		}
	}
}
