package com.xianzhisylj.dynamiclcz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xianzhi.stool.L;
import com.xianzhi.stool.T;
import com.xianzhi.tool.adapter.DynamicAdapter;
import com.xianzhi.tool.adapter.DynamicAdapter.clickCallBack;
import com.xianzhi.tool.db.DBHelper;
import com.xianzhi.tool.db.DynamicListHelper;
import com.xianzhi.tool.db.DynamicListHolder;
import com.xianzhi.tool.db.Pager;
import com.xianzhi.tool.view.MenuPopupWindow;
import com.xianzhi.tool.view.PullDownListView;
import com.xianzhi.tool.view.PullDownListView.OnRefreshListioner;
import com.xianzhi.tool.view.PullDownListView.filtCallBack;
import com.xianzhi.webtool.HttpJsonTool;
import com.xianzhisecuritycheck.main.SecurityCheckApp;

public class DynamicMainActivity extends Activity implements OnClickListener  {
	private PullDownListView pulldownlist;
	private List<Map<String, Object>> Datalist;
	private DynamicAdapter adapter;
	private Pager page;
	private ProgressBar progressBar;
	private ImageButton right_btn;
	private ImageButton goback_btn;
	
	
	private ImageButton show_btn;
	private ImageButton addnew_btn;
	private ImageButton search_btn;
	private RelativeLayout title;
	private LinearLayout blue_line;
	
	private Calendar calendar;
	private long time;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	private String date;
	private ProgressBar progressBar2;
	private long maxStartTime=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		L.i("DynamicMainActivity onCreate");
		setContentView(R.layout.activity_dynamic_main);
		
		setTime(0);
		page = new Pager(0, HttpJsonTool.PAGE_SIZE);
		initContentView();
		progressBar.setVisibility(View.VISIBLE);
		progressBar2.setVisibility(View.VISIBLE);
		
	}
	private String today;
	private void setTime(int day) {
		if (day == 0 || calendar == null) {
			calendar = Calendar.getInstance(Locale.CHINA);
			time = System.currentTimeMillis();
			calendar.setTimeInMillis(time);
			today = format.format(time);
		}
		calendar.add(Calendar.DAY_OF_YEAR, day);
		time = calendar.getTimeInMillis();
		date = format.format(time);
	}

	MenuPopupWindow popupWin;

	private void showEditMENU(View view) {
		if (popupWin == null) {
			popupWin = new MenuPopupWindow(getApplicationContext(),
					itemsOnClick, new String[] { "�½�", "����"/* ,"ͳ�Ʒ���" */});
		}
		popupWin.showAsDropDown(view);
	}

	private OnItemClickListener itemsOnClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			switch (arg2) {
			case 0:
				Intent intent = new Intent(getApplicationContext(),
						addNewDynamicActivity.class);
				startActivityForResult(intent, Activity.RESULT_OK);
				if (popupWin != null) {
					popupWin.dismiss();
				}
				break;
			case 1:
				Intent searchIntent = new Intent(getApplicationContext(),
						DynamicMainSearchActivity.class);
				searchIntent.putExtra("date", date);
				startActivityForResult(searchIntent, Activity.RESULT_OK);
				if (popupWin != null) {
					popupWin.dismiss();
				}
				break;
			default:
				break;
			}
		}
	};
	
	private void getTrainMoreListDate(final String keyword) {
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				return HttpJsonTool.getInstance().getTrainList(minId, keyword,
						getApplicationContext(), false, date,-1,format.format(maxStartTime));
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result.startsWith(HttpJsonTool.ERROR)) {
					T.showLong(getApplicationContext(),
							result.replace(HttpJsonTool.ERROR, ""));
					return;
				} else if (result.startsWith(HttpJsonTool.SUCCESS)) {
					pulldownlist.onLoadMoreComplete();
					refreshMoreParolData(keyword);
					return;
				}
			}
		};
		task.execute();
	}

	AsyncTask<Void, Void, String> TrainListDatetask = null;

	private void getTrainListDate(final int page, final String keyword,
			final boolean ifclear, final boolean login) {
		if (TrainListDatetask != null) {
			TrainListDatetask.cancel(true);
		}
		TrainListDatetask = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().getTrainList(-1, keyword,
						getApplicationContext(), ifclear, date,-1,null);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progressBar.setVisibility(View.GONE);
				progressBar2.setVisibility(View.GONE);
				pulldownlist.setVisibility(View.VISIBLE);
				if (result.startsWith(HttpJsonTool.ERROR403)) {
					gotoLoginView();
					return;
				} else if (result.startsWith(HttpJsonTool.ERROR)) {
					T.showLong(getApplicationContext(),
							result.replace(HttpJsonTool.ERROR, ""));
					return;
				} else if (result.startsWith(HttpJsonTool.SUCCESS)) {
					pulldownlist.onRefreshComplete();
					refreshParolData(page, keyword);
					return;
				}
			}
		};
		TrainListDatetask.execute();
	}

	private void gotoLoginView() {
		SecurityCheckApp.token = "";
		try {
			Intent intent = new Intent(getApplicationContext(),
					LoginActivity.class);
			T.show(getApplicationContext(), "�����˺����������豸�ϵ�¼�������µ�¼",
					Toast.LENGTH_LONG);
			startActivity(intent);
		} catch (Exception e) {
		}
	}

	private void upGradeDBifnessage() {
		DBHelper helper = new DBHelper(getApplicationContext());
		helper.close();
	}

	private filtCallBack callback = new filtCallBack() {

		@Override
		public void filt(boolean left) {
			// TODO Auto-generated method stub
			if (TrainListDatetask != null) {
				TrainListDatetask.cancel(true);
			}
			setTime(left ? 1 : -1);
			// progressBar.setVisibility(View.VISIBLE);
			page = new Pager(0, HttpJsonTool.PAGE_SIZE);
			Animation anim = AnimationUtils.loadAnimation(
					getApplicationContext(), left ? R.anim.slide_left_out
							: R.anim.slide_right_out);
			pulldownlist.setAnimation(anim);
			anim.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					int size = refreshParolData(page.curpage, null);
					if (size == HttpJsonTool.PAGE_SIZE) {
						progressBar2.setVisibility(View.GONE);
						pulldownlist.setVisibility(View.VISIBLE);
						pulldownlist.clearAnimation();
						pulldownlist.setAnimation(AnimationUtils
								.loadAnimation(getApplicationContext(),
										android.R.anim.fade_in));
					} else {
						getTrainListDate(0, null, false, false);
					}
				}
			});
			progressBar2.setVisibility(View.VISIBLE);
			pulldownlist.setVisibility(View.GONE);

		}
	};

	private void initContentView() {
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
		Datalist = new ArrayList<Map<String, Object>>();
		ListView dynamicList = (ListView) findViewById(R.id.dynamic_list);
		adapter = new DynamicAdapter(getApplicationContext(), Datalist,
				R.layout.cell_dynamic_list, new String[] { "train_number",
						"start_time", "start_position", "arrive_position",
						"watch_group", "Interval_position" }, new int[] {
						R.id.train_number_txt, R.id.start_time_txt,
						R.id.start_position_txt, R.id.arrive_position_txt,
						R.id.watch_group_txt });
		dynamicList.setAdapter(adapter);
		adapter.setCallback(new clickCallBack() {

			@Override
			public void onClick(int position) {
				// TODO Auto-generated method stub
				String trainCode = (String) Datalist.get(position).get(
						"train_number");
				long start_time = (Long) Datalist.get(position).get(
						"start_time_long");
				int id = (Integer) Datalist.get(position).get("id");
				Intent intent = new Intent(getApplicationContext(),
						addNewDynamicActivity.class);
				String usernames = (String) Datalist.get(position).get(DynamicListHelper.user_name);
				intent.putExtra("isfirst", usernames.length()==0);
				intent.putExtra("id", id);
				intent.putExtra(DynamicListHelper.board_train_code, trainCode);
				intent.putExtra(DynamicListHelper.start_time, start_time);
				
				startActivity(intent);
			}
		});
		// progressBar = (ProgressBar) findViewById(R.id.progressBar);
		pulldownlist = (PullDownListView) findViewById(R.id.pulldownlist);
		pulldownlist.setRefreshListioner(new OnRefreshListioner() {
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				page = new Pager(0, HttpJsonTool.PAGE_SIZE);
				getTrainListDate(0, null, true, false);
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				getTrainMoreListDate(null);
			}
		});
		pulldownlist.setCallback(callback);
		dynamicList = pulldownlist.mListView;
		pulldownlist.setMore(true);
		dynamicList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						TabDetailActivity.class);
				int id = (Integer) Datalist.get(position - 1).get("id");
				String user_id = (String) Datalist.get(position - 1).get(
						"user_id");
				intent.putExtra("id", id);
				intent.putExtra("user_id", user_id);
				int isRead = (Integer) Datalist.get(position - 1).get("isRead");
				intent.putExtra("isRead", isRead);
				int isFinal = (Integer) Datalist.get(position - 1).get(
						"isFinal");
				intent.putExtra("isFinal", isFinal);
				int status = (Integer) Datalist.get(position - 1).get(
						"station");
				intent.putExtra("status", status);
				Log.i("id", "" + id);
				startActivityForResult(intent, Activity.RESULT_OK);
			}
		});
		dynamicList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		right_btn = (ImageButton) findViewById(R.id.right_btn);
		right_btn.setOnClickListener(this);
		goback_btn = (ImageButton) findViewById(R.id.goback_btn);
		goback_btn.setOnClickListener(this);
		show_btn = (ImageButton) findViewById(R.id.show_btn);
		show_btn.setOnClickListener(this);
		addnew_btn = (ImageButton) findViewById(R.id.addnew_btn);
		addnew_btn.setOnClickListener(this);
		search_btn = (ImageButton) findViewById(R.id.search_btn);
		search_btn.setOnClickListener(this);
		title = (RelativeLayout) findViewById(R.id.title);
		blue_line = (LinearLayout) findViewById(R.id.blue_line);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// progressBar.setVisibility(View.VISIBLE);
		refreshParolData(page.curpage, null);
		getTrainListDate(page.curpage, null, true, true);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (TrainListDatetask != null) {
			TrainListDatetask.cancel(true);
		}
		progressBar2.setVisibility(View.GONE);
		pulldownlist.setVisibility(View.VISIBLE);
	}

	private int minId = 0;

	@SuppressLint("SimpleDateFormat")
	private int refreshParolData(int mPage, String keyword) {
		Datalist.clear();
		DynamicListHelper helper = new DynamicListHelper(
				getApplicationContext());
		ArrayList<DynamicListHolder> holders;
		if (keyword == null) {
			if(today.equals(date)){
				holders = helper.selectDataOnWay(0, (mPage + 1) * page.pagesize,date);
			}else{
				holders = helper.selectData(0, (mPage + 1) * page.pagesize,date);
			}
		} else {
			holders = helper.selectSearchData(keyword, 0, (mPage + 1)
					* page.pagesize, date);
		}
		helper.close();
		for (DynamicListHolder holder : holders) {
			addHolderData(holder);
		}
		L.i(holders.size() + "   " + page.pagesize);
		pulldownlist.setMore(holders.size() >0);
		adapter.notifyDataSetChanged();
		return holders.size();
	}

	@SuppressLint("SimpleDateFormat")
	private void addHolderData(DynamicListHolder holder) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", holder.getId());
		data.put("train_number", holder.getBoard_train_code());
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		data.put("start_time", format.format(holder.getStart_time()));
		data.put("start_time_long", holder.getStart_time());
		data.put("start_position", holder.getFrom_station_name());
		data.put("arrive_position", holder.getTo_station_name());
		data.put("watch_group", holder.getCurrent_team());
		data.put("Interval_position", "");
		data.put("station", holder.getDriving_status());
		data.put("user_id", holder.getUser_ids());
		data.put("isRead", holder.getIsRead());
		data.put("isFinal", holder.getIsFinal());
		data.put(DynamicListHelper.user_name, holder.getUser_name());
		Datalist.add(data);
		minId = holder.getId();
		maxStartTime=holder.getStart_time();
	}

	private void refreshMoreParolData(String keyword) {
		page.curpage++;
		DynamicListHelper helper = new DynamicListHelper(
				getApplicationContext());
		ArrayList<DynamicListHolder> holders;
		if (keyword == null) {
			if(today.equals(date)){
				holders = helper.selectDataOnWay(page.curpage * page.pagesize,
						page.pagesize,date);
			}else{
				holders = helper.selectData(page.curpage * page.pagesize,
						page.pagesize,date);
			}
		} else {
			holders = helper.selectSearchData(keyword, page.curpage
					* page.pagesize, page.pagesize, date);
		}
		helper.close();
		for (DynamicListHolder holder : holders) {
			addHolderData(holder);
		}
		pulldownlist.setMore(holders.size() >0);
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(Menu.NONE, Menu.FIRST + 1, 5, "�½�");
		menu.add(Menu.NONE, Menu.FIRST + 2, 5, "����");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			Intent intent = new Intent(getApplicationContext(),
					addNewDynamicActivity.class);
			intent.putExtra("isfirst", true);
			startActivityForResult(intent, Activity.RESULT_OK);
			break;
		case Menu.FIRST + 2:
			Intent searchIntent = new Intent(getApplicationContext(),
					DynamicMainSearchActivity.class);
			searchIntent.putExtra("date", date);
			startActivityForResult(searchIntent, Activity.RESULT_OK);
			break;
		}
		return false;
	}
	private boolean titleisshow=false;
	private void toggleTitle(){
		titleisshow=!titleisshow;
		title.setVisibility(titleisshow?View.VISIBLE:View.GONE);
		blue_line.setVisibility(titleisshow?View.GONE:View.VISIBLE);
		show_btn.setImageResource(titleisshow?R.drawable.btn_top_hide:R.drawable.btn_top_show);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.right_btn:
			callback.filt(true);
			break;
		case R.id.goback_btn:
			callback.filt(false);
			break;
		case R.id.show_btn:
			toggleTitle();
			break;
		case R.id.addnew_btn:
			Intent intent = new Intent(getApplicationContext(),
					addNewDynamicActivity.class);
			intent.putExtra("isfirst", true);
			startActivityForResult(intent, Activity.RESULT_OK);
			break;
		case R.id.search_btn:
			Intent searchIntent = new Intent(getApplicationContext(),
					DynamicMainSearchActivity.class);
			searchIntent.putExtra("date", date);
			startActivityForResult(searchIntent, Activity.RESULT_OK);
			break;

		default:
			break;
		}
	}
}
