package com.xianzhi.tool.view;


import com.xianzhisylj.dynamiclcz.R;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.PopupWindow;


public class SelectPicPopupWindow extends PopupWindow {


	private ImageButton camera_btn, photo_btn,cancel_btn,video_btn;
	private View mMenuView;

	public SelectPicPopupWindow(Context context,OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popwin_menu, null);
		video_btn = (ImageButton) mMenuView.findViewById(R.id.video_btn);
		camera_btn = (ImageButton) mMenuView.findViewById(R.id.camera_btn);
		photo_btn = (ImageButton) mMenuView.findViewById(R.id.photo_btn);
		cancel_btn = (ImageButton) mMenuView.findViewById(R.id.cancel_btn);
		
		//…Ë÷√∞¥≈•º‡Ã˝
		video_btn.setOnClickListener(itemsOnClick);
		camera_btn.setOnClickListener(itemsOnClick);
		photo_btn.setOnClickListener(itemsOnClick);
		cancel_btn.setOnClickListener(itemsOnClick);
		this.setContentView(mMenuView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setAnimationStyle(R.style.AnimBottom);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		this.setBackgroundDrawable(dw);
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}				
				return true;
			}
		});

	}

}
