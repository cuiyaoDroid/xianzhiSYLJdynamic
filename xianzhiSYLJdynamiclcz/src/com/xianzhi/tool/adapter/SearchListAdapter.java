package com.xianzhi.tool.adapter;

import java.util.List;
import java.util.Map;

import com.xianzhisylj.dynamiclcz.R;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;



public class SearchListAdapter extends SimpleAdapter {
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
		if (result != null) {
			inflater.inflate(R.layout.cell_expandable_child, null);
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

	@SuppressWarnings("unchecked")
	public SearchListAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
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
