package com.xianzhi.tool.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.xianzhisylj.dynamic.R;

public class GridSeatListAdapter extends SimpleAdapter {
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
		boolean full=(Boolean) styles.get(position).get("full");
		ImageView img=(ImageView)result.findViewById(R.id.seat_img);
		TextView set_txt=(TextView)result.findViewById(R.id.seat_num);
		img.setImageResource(full?R.drawable.img_seat_full:R.drawable.img_seat_empty);
//		set_txt.setVisibility(full?View.VISIBLE:View.VISIBLE);
		set_txt.setTextColor(full?context.getResources().getColor(R.color.seat_text_full)
				:context.getResources().getColor(R.color.seat_text));
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
	public GridSeatListAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.context = context;
		styles = (List<Map<String, Object>>) data;
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if (observer != null) {
			super.unregisterDataSetObserver(observer);
		}
	}
}
