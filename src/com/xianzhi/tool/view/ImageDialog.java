package com.xianzhi.tool.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.example.web.image.ImageDownloader;
import com.xianzhisylj.dynamic.R;


public class ImageDialog extends AlertDialog {
	private String path;
	private ImageDownloader loader;
	public ImageDialog(Context context, int theme,String path,ImageDownloader loader) {
		super(context, theme);
		this.path=path;
		this.loader=loader;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		dismiss();
		return super.onTouchEvent(event);
	}
	public ImageDialog(Context context) {
		super(context);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_img);
		ImageView image=(ImageView)findViewById(R.id.image);
		loader.download(path, image);
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
}