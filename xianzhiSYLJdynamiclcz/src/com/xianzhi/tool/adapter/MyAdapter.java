package com.xianzhi.tool.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.web.image.ImageDownloader;
import com.xianzhi.webtool.HttpJsonTool;
import com.xianzhisylj.dynamiclcz.R;


public class MyAdapter extends SimpleAdapter {
	// 用于动态的载入一个界面文件
	private List<Map<String, Object>> styles = null;
	private LayoutInflater inflater = null;

	public List<Map<String, Object>> getStyles() {
		return styles;
	}

	public void setStyles(List<Map<String, Object>> styles) {
		this.styles = styles;
	}
	private ImageDownloader imgeDownloader;
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View result = super.getView(position, convertView, parent);
		ImageView img=(ImageView)result.findViewById(R.id.image_content);
		String imgpath=(String) styles.get(position).get("img");
		TextView content_txt=(TextView)result.findViewById(R.id.content_txt);
		content_txt.setText(Html.fromHtml((String) styles.get(position).get("content")));
		TextView result_type_txt=(TextView)result.findViewById(R.id.result_type_txt);
		String result_typeName=(String) styles.get(position).get("result_typeName");
		if(result_typeName.equals("问题")){
			int color = context.getResources().getColor(R.color.Red);
			result_type_txt.setTextColor(color);
		}else if(result_typeName.equals("表扬")){
			int color = context.getResources().getColor(R.color.dark_orange);
			result_type_txt.setTextColor(color);
		}else{
			int color = context.getResources().getColor(R.color.green);
			result_type_txt.setTextColor(color);
		}
		result_type_txt.setText(result_typeName);
		
		if(imgpath.length()>0){
			String path=HttpJsonTool.ServerUrl+"/s"+(imgpath.split(";")[0]);
			imgeDownloader.download(path, img);
		}else{
			img.setImageResource(R.drawable.img_default);
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
	public MyAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		inflater = LayoutInflater.from(context);
		styles = (List<Map<String, Object>>) data;
		imgeDownloader=new ImageDownloader(context,0);
		this.context=context;
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if (observer != null) {
			super.unregisterDataSetObserver(observer);
		}
	}
}
