package com.xianzhi.tool.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.example.web.image.ImageDownloader;
import com.xianzhi.webtool.HttpJsonTool;
import com.xianzhisylj.dynamic.R;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class GridListAdapter extends SimpleAdapter {
	// 用于动态的载入一个界面文件
	private List<Map<String, Object>> styles = null;

	public List<Map<String, Object>> getStyles() {
		return styles;
	}

	public void setStyles(List<Map<String, Object>> styles) {
		this.styles = styles;
	}

	private Bitmap getImageFromAssetsFile(String fileName) {
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;

	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View result = super.getView(position, convertView, parent);
		ImageView head_img = (ImageView) result.findViewById(R.id.head_img);
		downloader.download(HttpJsonTool.ServerUrl+(String)styles.get(position).get("img"), head_img);
		TextView name = (TextView) result.findViewById(R.id.name_txt);
		name.setText(Html.fromHtml((String)styles.get(position).get("name")));
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
	private ImageDownloader downloader;
	@SuppressWarnings("unchecked")
	public GridListAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.context = context;
		downloader=new ImageDownloader(context, 0);
		styles = (List<Map<String, Object>>) data;
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if (observer != null) {
			super.unregisterDataSetObserver(observer);
		}
	}
}
