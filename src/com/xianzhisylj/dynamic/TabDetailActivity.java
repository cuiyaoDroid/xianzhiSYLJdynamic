package com.xianzhisylj.dynamic;

import java.util.Calendar;
import java.util.Locale;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.xianzhi.stool.Constant.ConValue;
import com.xianzhi.tool.db.DynamicHelper;
import com.xianzhi.tool.db.DynamicHolder;
import com.xianzhi.tool.db.TrainHelper;
import com.xianzhi.tool.db.TrainHolder;

@SuppressWarnings("deprecation")
public class TabDetailActivity extends TabActivity {

	// private final static String TAG = "CustomTabActivity2";

	private TabHost m_tabHost;

	private RadioGroup m_radioGroup;
	public static Bitmap my_head_pic = null;
	public static String UserId = null;
	public static String token = "";
	private LinearLayout train_info_layout;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_detail_tab);
		train_info_layout = (LinearLayout) findViewById(R.id.train_info_layout);
		refreshTrainInfo();
		init();
	}

	private void refreshTrainInfo() {
		train_info_layout.removeAllViews();

		int id = getIntent().getIntExtra("id", -1);
		if (id == -1) {
			return;
		}
		DynamicHelper Dhelper = new DynamicHelper(getApplicationContext());
		DynamicHolder Dholder = Dhelper.selectData_Id(id);
		Dhelper.close();
		if (Dholder == null) {
			return;
		}
		TrainHelper helper = new TrainHelper(getApplicationContext());
		TrainHolder holder = helper.selectData_Name(Dholder.getTrain_number());
		helper.close();
		String[] stations = holder.getPlace_group().split(",");
		String[] times = holder.getTime_group().split(",");
		if (stations.length != times.length) {
			Toast.makeText(getApplicationContext(), "车次数据错误", Toast.LENGTH_LONG)
					.show();
			return;
		}

		long[] l_times = new long[times.length];
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTimeInMillis(Dholder.getStart_time());
		int s = 0;
		long cur_time = System.currentTimeMillis();
		for (int i = 0; i < times.length; i++) {
			String[] time = times[i].split(":");
			calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
			calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
			l_times[i] = calendar.getTimeInMillis();
			if (i != 0) {
				if (l_times[i - 1] > l_times[i]) {
					calendar.set(Calendar.DAY_OF_MONTH,
							calendar.get(Calendar.DAY_OF_MONTH) + 1);
					l_times[i] = calendar.getTimeInMillis();
				}
			}
			if (cur_time > l_times[i]) {
				s++;
			}
		}

		int i = 0;
		for (String station : stations) {
			View cell;
			TextView time_txt;
			TextView locatetxt;
			if (i == 0) {
				cell = getLayoutInflater().inflate(R.layout.cell_train_start,
						null);
				time_txt = (TextView) cell.findViewById(R.id.time_txt);
				locatetxt = (TextView) cell.findViewById(R.id.locate_txt);
				if (i < s) {
					time_txt.setTextColor(Color.GREEN);
					locatetxt.setTextColor(Color.GREEN);
				}
			} else if (i == stations.length - 1) {
				cell = getLayoutInflater().inflate(R.layout.cell_train_end,
						null);
				time_txt = (TextView) cell.findViewById(R.id.time_txt);
				locatetxt = (TextView) cell.findViewById(R.id.locate_txt);
				if (i <= s) {
					time_txt.setTextColor(Color.GREEN);
					locatetxt.setTextColor(Color.GREEN);
				}
			} else {
				cell = getLayoutInflater().inflate(R.layout.cell_train_middle,
						null);
				time_txt = (TextView) cell.findViewById(R.id.time_txt);
				locatetxt = (TextView) cell.findViewById(R.id.locate_txt);
				if (i <= s) {
					time_txt.setTextColor(Color.GREEN);
					locatetxt.setTextColor(Color.GREEN);
					if (i == s) {
						ImageView tran = (ImageView) cell
								.findViewById(R.id.train_icon);
						tran.setVisibility(View.VISIBLE);
					}
				}

			}
			locatetxt.setText(station);
			time_txt.setText(times[i]);
			train_info_layout.addView(cell);
			i++;
		}

	}
	int currentTab;
	private void init() {

		m_tabHost = getTabHost();

		int count = ConValue.mTabClassArray.length;
		for (int i = 0; i < count; i++) {
			TabSpec tabSpec = m_tabHost.newTabSpec(ConValue.mTextviewArray[i])
					.setIndicator(ConValue.mTextviewArray[i])
					.setContent(getTabItemIntent(i));
			m_tabHost.addTab(tabSpec);
		}

		m_radioGroup = (RadioGroup) findViewById(R.id.main_radiogroup);
		m_radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				currentTab=m_tabHost.getCurrentTab();
				switch (checkedId) {
				case R.id.RadioButton0:
					setCurrentTabWithAnim(currentTab, 0,
							ConValue.mTextviewArray[0]);

					break;
				case R.id.RadioButton1:
					setCurrentTabWithAnim(currentTab, 1,
							ConValue.mTextviewArray[1]);
					break;
				case R.id.RadioButton2:
					setCurrentTabWithAnim(currentTab, 2,
							ConValue.mTextviewArray[2]);
					break;
				case R.id.RadioButton3:
					setCurrentTabWithAnim(currentTab, 3,
							ConValue.mTextviewArray[3]);
					break;
				case R.id.RadioButton4:
					setCurrentTabWithAnim(currentTab, 4,
							ConValue.mTextviewArray[4]);
					break;
				case R.id.RadioButton5:
					setCurrentTabWithAnim(currentTab, 5,
							ConValue.mTextviewArray[5]);
					break;
				}
			}
		});

		((RadioButton) findViewById(R.id.RadioButton0)).toggle();
	}

	private void setCurrentTabWithAnim(int now, int next, String tag) {
		// 这个方法是关键，用来判断动画滑动的方向
		if (now > next) {
			m_tabHost.getCurrentView().startAnimation(
					AnimationUtils.loadAnimation(this, R.anim.push_bottom_out));
			m_tabHost.setCurrentTabByTag(tag);
			m_tabHost.getCurrentView().startAnimation(
					AnimationUtils.loadAnimation(this, R.anim.push_top_in));
		} else {
			m_tabHost.getCurrentView().startAnimation(
					AnimationUtils.loadAnimation(this, R.anim.push_top_out));
			m_tabHost.setCurrentTabByTag(tag);
			m_tabHost.getCurrentView().startAnimation(
					AnimationUtils.loadAnimation(this, R.anim.push_bottom_in));
		}
	}

	private Intent getTabItemIntent(int index) {
		int id = getIntent().getIntExtra("id", -1);
		Intent intent = new Intent(this, ConValue.mTabClassArray[index]);
		intent.putExtra("id", id);
		Log.i("TabDetailActivity", "Intent");
		return intent;
	}
}
