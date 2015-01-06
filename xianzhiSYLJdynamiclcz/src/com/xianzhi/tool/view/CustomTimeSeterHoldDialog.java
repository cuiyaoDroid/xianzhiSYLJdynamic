package com.xianzhi.tool.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xianzhi.tool.view.wheel.WheelView;
import com.xianzhialarm.listener.TimeDialogListener;
import com.xianzhisylj.dynamiclcz.R;

/**
 * <p>
 * Title: CustomDialog
 * </p>
 * <p>
 * Description:自定义Dialog（参数传入Dialog样式文件，Dialog布局文件）
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * 
 * @author archie
 * @version 1.0
 */
public class CustomTimeSeterHoldDialog extends Dialog {
	private int layoutRes;// 布局文件
	private Context context;
	private long milltime;
	public CustomTimeSeterHoldDialog(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * 自定义布局的构造方法
	 * 
	 * @param context
	 * @param resLayout
	 */
	public CustomTimeSeterHoldDialog(Context context, int resLayout) {
		super(context);
		this.context = context;
		this.layoutRes = resLayout;
	}

	/**
	 * 自定义主题及布局的构造方法
	 * 
	 * @param context
	 * @param theme
	 * @param resLayout
	 */
	public CustomTimeSeterHoldDialog(Context context, int theme, int resLayout,long milltime) {
		super(context, theme);
		this.context = context;
		this.layoutRes = resLayout;
		this.milltime=milltime;
	}
	private TimeDialogListener listener;
	public void setTimeDialogListener(TimeDialogListener listener){
		this.listener=listener;
	}
	private Calendar calendar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(layoutRes);
		final WheelView hours = (WheelView) findViewById(R.id.hour);
		NumericWheelAdapter hourAdapter = new NumericWheelAdapter(context, 0,
				23, "%02d时");
		hourAdapter.setItemResource(R.layout.wheel_text_item);
		hourAdapter.setItemTextResource(R.id.text);
		hours.setViewAdapter(hourAdapter);
		hours.setCyclic(true);

		final WheelView mins = (WheelView) findViewById(R.id.mins);
		NumericWheelAdapter minAdapter = new NumericWheelAdapter(context, 0,
				59, "%02d");
		minAdapter.setItemResource(R.layout.wheel_text_item);
		minAdapter.setItemTextResource(R.id.text);
		mins.setViewAdapter(minAdapter);
		mins.setCyclic(true);

		// set current time
		calendar = Calendar.getInstance(Locale.CHINA);
		if(milltime!=0){
			calendar.setTimeInMillis(milltime);
		}
		hours.setCurrentItem(calendar.get(Calendar.HOUR_OF_DAY));
		mins.setCurrentItem(calendar.get(Calendar.MINUTE));

		final WheelView day = (WheelView) findViewById(R.id.day);
		final DayArrayAdapter adapter = new DayArrayAdapter(context, calendar);
		day.setViewAdapter(adapter);
		day.setCurrentItem(adapter.getItemsCount() / 2);
		final Button setBtn = (Button) findViewById(R.id.set_btn);
		setBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				calendar.set(Calendar.HOUR_OF_DAY,hours.getCurrentItem());
				calendar.set(Calendar.MINUTE,mins.getCurrentItem());
				calendar.set(Calendar.SECOND, 0);
				adapter.getItemcalendarText(day.getCurrentItem());
				if(listener!=null){
					listener.getTimeInMill(calendar.getTimeInMillis());
				}
				dismiss();
			}
		});
		final Button cancalBtn = (Button) findViewById(R.id.cancal_btn);
		cancalBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
	}

	/**
	 * Day adapter
	 * 
	 */
	private class DayArrayAdapter extends AbstractWheelTextAdapter {
		// Count of days to be shown
		private final int daysCount = 366;

		// Calendar
		Calendar ad_calendar;

		/**
		 * Constructor
		 */
		protected DayArrayAdapter(Context context, Calendar calendar) {
			super(context, R.layout.wheel_text_time_week, NO_RESOURCE);
			this.ad_calendar = calendar;

			setItemTextResource(R.id.time2_monthday);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			int day = -daysCount / 2 + index;
			Calendar newCalendar = (Calendar) ad_calendar.clone();
			newCalendar.add(Calendar.DAY_OF_YEAR, day);

			View view = super.getItem(index, cachedView, parent);
			TextView weekday = (TextView) view.findViewById(R.id.time2_weekday);
			DateFormat format = new SimpleDateFormat("EEE");
			weekday.setText(format.format(newCalendar.getTime()));
			TextView monthday = (TextView) view
					.findViewById(R.id.time2_monthday);
			monthday.setTextColor(0xFF111111);
			if (day == 0) {
				monthday.setTextColor(0xFF0000F0);
			}
			DateFormat formatdata = new SimpleDateFormat("MM月dd日");
			monthday.setText(formatdata.format(newCalendar.getTime()));
			return view;
		}
		@Override
		public int getItemsCount() {
			return daysCount + 1;
		}
		protected CharSequence getItemcalendarText(int index) {
			int day = -daysCount / 2 + index;
			calendar.add(Calendar.DAY_OF_YEAR, day);
			DateFormat format = new SimpleDateFormat("MM月dd日 EEE");
			return format.format(calendar.getTime());
		}
		@Override
		protected CharSequence getItemText(int index) {
			// TODO Auto-generated method stub
			return "";
		}
	}
}