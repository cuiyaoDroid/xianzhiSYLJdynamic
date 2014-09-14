package com.xianzhisylj.dynamic;

import java.util.ArrayList;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.xianzhi.stool.L;
import com.xianzhi.stool.T;
import com.xianzhi.tool.db.DynamicListHelper;
import com.xianzhi.tool.db.ReviewHolder;
import com.xianzhi.tool.db.TrainDynamicHelper;
import com.xianzhi.tool.db.TrainDynamicHolder;
import com.xianzhi.webtool.HttpJsonTool;

@SuppressWarnings("deprecation")
public class TabDetailActivity extends TabActivity {

	// private final static String TAG = "CustomTabActivity2";

	private TabHost m_tabHost;

	private RadioGroup m_radioGroup;
	public static Bitmap my_head_pic = null;
	public static String UserId = null;
	public static String token = "";
	private LinearLayout train_info_layout;
	public static String mTextviewArray[] = { "车次信息", "编组情况", "重点工作", "乘务记录",
			"提交报告", "审阅记录" };
	private boolean ismySelf=false;
	@SuppressWarnings("rawtypes")
	private ProgressBar progressBar;
	private Class mTabClassArray[];
	int isRead=0;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_detail_tab);
		train_info_layout = (LinearLayout) findViewById(R.id.train_info_layout);
		progressBar =  (ProgressBar) findViewById(R.id.progressBar);
		train_id = getIntent().getIntExtra("id", -1);
		user_id = getIntent().getIntExtra("user_id", -1);
		ismySelf=user_id==HttpJsonTool.userId;
		isRead = getIntent().getIntExtra("isRead", -1);
		isFinal = getIntent().getIntExtra("isFinal", -1);
		if (train_id == -1) {
			return;
		}
		HttpJsonTool.train_id=train_id;
		if(isRead!=1&&!ismySelf&&HttpJsonTool.reviewTrain==1){
			sendReview();
		}
		refreshTrainInfo();
		getTrainDynamic();
		init();
	}
	
	private void getTrainDynamic(){
		progressBar.setVisibility(View.VISIBLE);
		AsyncTask<Void, Void, String> task =new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().getTrainDynamic(getApplicationContext(), train_id);
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progressBar.setVisibility(View.GONE);
				if(result==null){
					return;
				}
				if (result.startsWith(HttpJsonTool.ERROR)) {
					T.showLong(getApplicationContext(),
							result.replace(HttpJsonTool.ERROR, ""));
					return;
				} else if (result.startsWith(HttpJsonTool.SUCCESS)) {
					refreshTrainDynamic();
					return;
				}
			}
		};
		task.execute();
	}
	private void sendReview(){
		AsyncTask<Void, Void, String> task =new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				
				ReviewHolder holder=new ReviewHolder(-1, train_id, HttpJsonTool.userId
						, "", "", "", ""
						, "", 1, 1, -1);
				return HttpJsonTool.getInstance().saveReview(getApplicationContext(), holder);
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(result==null){
					return;
				}
				if(result.startsWith(HttpJsonTool.SUCCESS)){
					DynamicListHelper helper=new DynamicListHelper(getApplicationContext());
					helper.setRead(train_id);
					helper.close();
				}
			}
		};
		task.execute();
	}
	private int train_id;
	private int user_id=-1;
	private int isFinal=-1;
	private void refreshTrainInfo() {
		
		if(ismySelf&&isFinal==0){
			mTabClassArray = new Class[]{ TrainInfoActivity.class,
					GroupInfoActivity.class,
					ImportWorkEditActivity.class,// ImportWorkInfoActivity.class,
					NoteEditActivity.class, commitActivity.class,
					ReviewInfoActivity.class };
			findViewById(R.id.RadioButton4).setVisibility(View.VISIBLE);
		}else{
			mTabClassArray = new Class[]{ TrainInfoActivity.class,
					GroupInfoActivity.class,
					ImportWorkInfoActivity.class,
					NoteInfoActivity.class, commitActivity.class,
					ReviewInfoActivity.class };
			findViewById(R.id.RadioButton4).setVisibility(View.GONE);
		}
	}
	private void refreshTrainDynamic(){
		train_info_layout.removeAllViews();
		TrainDynamicHelper helper=new TrainDynamicHelper(getApplicationContext());
		ArrayList<TrainDynamicHolder> holders=helper.selectDataTrainId(train_id);
		helper.close();
		int i=0;
		for(TrainDynamicHolder holder:holders){
			View cell;
			TextView time_txt;
			TextView locatetxt;
			L.i(holder.getSTATION_NAME());
			if (i == 0) {
				cell = getLayoutInflater().inflate(R.layout.cell_train_start,
						null);
				time_txt = (TextView) cell.findViewById(R.id.time_txt);
				locatetxt = (TextView) cell.findViewById(R.id.locate_txt);
				if (holder.getPASSED()==1||holder.getCUR()==1) {
					time_txt.setTextColor(Color.GREEN);
					locatetxt.setTextColor(Color.GREEN);
				}
			} else if (i == holders.size() - 1) {
				cell = getLayoutInflater().inflate(R.layout.cell_train_end,
						null);
				time_txt = (TextView) cell.findViewById(R.id.time_txt);
				locatetxt = (TextView) cell.findViewById(R.id.locate_txt);
				if (holder.getPASSED()==1||holder.getCUR()==1) {
					time_txt.setTextColor(Color.GREEN);
					locatetxt.setTextColor(Color.GREEN);
				}
			} else {
				cell = getLayoutInflater().inflate(R.layout.cell_train_middle,
						null);
				time_txt = (TextView) cell.findViewById(R.id.time_txt);
				locatetxt = (TextView) cell.findViewById(R.id.locate_txt);
				if (holder.getPASSED()==1||holder.getCUR()==1) {
					time_txt.setTextColor(Color.GREEN);
					locatetxt.setTextColor(Color.GREEN);
					if (holder.getCUR()==1) {
						ImageView tran = (ImageView) cell
								.findViewById(R.id.train_icon);
						tran.setVisibility(View.VISIBLE);
					}
				}

			}
			locatetxt.setText(holder.getSTATION_NAME());
			time_txt.setText(holder.getTIME_ARRIVAL());
			train_info_layout.addView(cell);
			i++;
		}
		
	}
	int currentTab;

	private void init() {

		m_tabHost = getTabHost();

		int count = mTabClassArray.length;
		for (int i = 0; i < count; i++) {
			TabSpec tabSpec = m_tabHost.newTabSpec(mTextviewArray[i])
					.setIndicator(mTextviewArray[i])
					.setContent(getTabItemIntent(i));
			m_tabHost.addTab(tabSpec);
		}

		m_radioGroup = (RadioGroup) findViewById(R.id.main_radiogroup);
		m_radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				currentTab = m_tabHost.getCurrentTab();
				switch (checkedId) {
				case R.id.RadioButton0:
					setCurrentTabWithAnim(currentTab, 0,
							mTextviewArray[0]);

					break;
				case R.id.RadioButton1:
					setCurrentTabWithAnim(currentTab, 1,
							mTextviewArray[1]);
					break;
				case R.id.RadioButton2:
					setCurrentTabWithAnim(currentTab, 2,
							mTextviewArray[2]);
					break;
				case R.id.RadioButton3:
					setCurrentTabWithAnim(currentTab, 3,
							mTextviewArray[3]);
					break;
				case R.id.RadioButton4:
					setCurrentTabWithAnim(currentTab, 4,
							mTextviewArray[4]);
					break;
				case R.id.RadioButton5:
					setCurrentTabWithAnim(currentTab, 5,
							mTextviewArray[5]);
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
		Intent intent = new Intent(this, mTabClassArray[index]);
		intent.putExtra("id", id);
		intent.putExtra("ismySelf", ismySelf);
		Log.i("TabDetailActivity", "Intent");
		return intent;
	}
}
