package com.xianzhi.tool.view;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xianzhi.stool.DensityUtil;
import com.xianzhi.tool.adapter.popEditTypeAdapter;
import com.xianzhisylj.dynamiclcz.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;


public class MenuPopupWindow extends PopupWindow {
	private View mMenuView;
	private popEditTypeAdapter popadapter;
	private List<Map<String, String>> popData;
	public MenuPopupWindow(Context context,OnItemClickListener itemsOnClick,String[] menuitem) {
		super(context);
		popData = new ArrayList<Map<String, String>>();
		for(String item:menuitem){
			Map<String,String>data_1=new HashMap<String, String>();
			data_1.put("text", item);
			popData.add(data_1);
		}
		popadapter = new popEditTypeAdapter(context, popData,
				R.layout.cell_poplist, new String[] { "text" },
				new int[] { R.id.autoedit_text });
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popupwin_reply_type, null);
		ListView list = (ListView)mMenuView.findViewById(R.id.reply_type_listView);
		list.setAdapter(popadapter);
		list.setOnItemClickListener(itemsOnClick);
		this.setContentView(mMenuView);
		this.setWidth(DensityUtil.dip2px(context, 80));
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setBackgroundDrawable(new BitmapDrawable());
		this.setOutsideTouchable(true);
	}
}
