package com.xianzhisylj.dynamic;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.xianzhi.tool.db.MapHolder;
import com.xianzhialarm.listener.OnTabActivityResultListener;
import com.xianzhisecuritycheck.main.AddnotifiUserListActivity;
import com.xianzhisecuritycheck.main.SecurityDetailActivity;
import com.xianzhisecuritycheck.main.SecurityMainActivity;
import com.xianzhisecuritycheck.main.addNewInforActivity;

public class SecuityInfoActivity extends TabActivity {
	private static TabHost m_tabHost;
	public static int id=-1;
	public static MapHolder holder;
	private static Animation slide_left_out;
	private static Animation slide_right_in;
	private static Animation slide_right_out;
	private static Animation slide_left_in;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_secuity_info);
		slide_left_out=AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
		slide_right_in=AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
		slide_right_out=AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
		slide_left_in=AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
		init();
		Log.i("SecuityInfoActivity","onCreate");
	}

	public static String mTextviewArray[] = { "车次信息", "编组情况", "重点工作", "乘务记录" };

	@SuppressWarnings("rawtypes")
	public static Class mTabClassArray[] = { SecurityMainActivity.class,
			SecurityDetailActivity.class, addNewInforActivity.class,
			AddnotifiUserListActivity.class };
	public static int currentTab=-1;
	private void init() {
		m_tabHost = getTabHost();
		int count = mTabClassArray.length;
		for (int i = 0; i < count; i++) {
			TabSpec tabSpec = m_tabHost.newTabSpec(mTextviewArray[i])
					.setIndicator(mTextviewArray[i])
					.setContent(getTabItemIntent(i));
			m_tabHost.addTab(tabSpec);
		}
		m_tabHost.setCurrentTabByTag(mTextviewArray[0]);
	}
	public static void setCurrentTabWithAnim(int now, int next, String tag) {
		// 这个方法是关键，用来判断动画滑动的方向
		if (now < next) {
			m_tabHost.getCurrentView().startAnimation(slide_left_out);
			m_tabHost.setCurrentTabByTag(tag);
			m_tabHost.getCurrentView().startAnimation(slide_right_in);
		} else {
			m_tabHost.getCurrentView().startAnimation(slide_right_out);
			m_tabHost.setCurrentTabByTag(tag);
			m_tabHost.getCurrentView().startAnimation(slide_left_in);
		}
		currentTab=m_tabHost.getCurrentTab();
	}
	private Intent getTabItemIntent(int index) {
		Intent intent = new Intent(this, mTabClassArray[index]);
		return intent;
	}
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        Activity subActivity = getLocalActivityManager().getCurrentActivity();  
        if (subActivity instanceof OnTabActivityResultListener) {  
            OnTabActivityResultListener listener = (OnTabActivityResultListener) subActivity;  
            listener.onTabActivityResult(requestCode, resultCode, data);  
        }  
        super.onActivityResult(requestCode, resultCode, data);  
    }  
}