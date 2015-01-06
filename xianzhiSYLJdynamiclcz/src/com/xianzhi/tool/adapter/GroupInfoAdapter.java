package com.xianzhi.tool.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class GroupInfoAdapter extends SimpleAdapter {
	// 用于动态的载入一个界面文件
	private List<Map<String, Object>> styles = null;

	public List<Map<String, Object>> getStyles() {
		return styles;
	}

	public void setStyles(List<Map<String, Object>> styles) {
		this.styles = styles;
	}

	private Context context;

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
		/*
		RelativeLayout re_img_1 = (RelativeLayout) result
				.findViewById(R.id.re_img_1);
		ImageView head_img_1 = (ImageView) result.findViewById(R.id.head_img_1);
		TextView name_txt_1 = (TextView) result.findViewById(R.id.name_txt_1);
		RelativeLayout re_img_2 = (RelativeLayout) result
				.findViewById(R.id.re_img_2);
		String names = (String) styles.get(position).get("em_names");
		String head_imgs = (String) styles.get(position).get("em_head_imgs");
		String tels = (String) styles.get(position).get("em_tels");
		String[] em_names = names.split(",");
		String[] em_head_imgs = head_imgs.split(",");
		final String[] em_tels = tels.split(",");
		if (em_names.length > 0 && em_head_imgs.length > 0
				&& em_tels.length > 0) {
			Bitmap bit = getImageFromAssetsFile(em_head_imgs[0]);
			head_img_1.setImageBitmap(bit);
			name_txt_1.setText(em_names[0]);
			if (em_names.length > 1 && em_head_imgs.length > 1
					&& em_tels.length > 1) {
				re_img_2.setVisibility(View.VISIBLE);
				ImageView head_img_2 = (ImageView) result
						.findViewById(R.id.head_img_2);
				TextView name_txt_2 = (TextView) result
						.findViewById(R.id.name_txt_2);
				Bitmap bit_2 = getImageFromAssetsFile(em_head_imgs[1]);
				head_img_2.setImageBitmap(bit_2);
				name_txt_2.setText(em_names[1]);
				re_img_2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						callTellNum(em_tels[1]);
					}
				});
			} else {
				re_img_2.setVisibility(View.INVISIBLE);
			}
			re_img_1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					callTellNum(em_tels[0]);
				}
			});
		}*/
		return result;
	}

	private void callTellNum(String num) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
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
	public GroupInfoAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		styles = (List<Map<String, Object>>) data;
		this.context = context;
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if (observer != null) {
			super.unregisterDataSetObserver(observer);
		}
	}
}
