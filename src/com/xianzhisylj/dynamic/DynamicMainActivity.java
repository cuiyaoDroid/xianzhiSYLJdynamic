package com.xianzhisylj.dynamic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.xianzhi.stool.KeyBroadTool;
import com.xianzhi.stool.L;
import com.xianzhi.stool.T;
import com.xianzhi.tool.adapter.DynamicAdapter;
import com.xianzhi.tool.db.DBHelper;
import com.xianzhi.tool.db.DynamicListHelper;
import com.xianzhi.tool.db.DynamicListHolder;
import com.xianzhi.tool.db.Pager;
import com.xianzhi.tool.view.PullDownListView;
import com.xianzhi.tool.view.PullDownListView.OnRefreshListioner;
import com.xianzhi.webtool.HttpJsonTool;
import com.xianzhisecuritycheck.main.SecurityCheckApp;

public class DynamicMainActivity extends Activity {
	private PullDownListView pulldownlist;
	private List<Map<String, Object>> Datalist;
	private DynamicAdapter adapter;
	private Pager page;
	private EditText searchEdit;
	private ImageButton settingBtn;
	private ProgressBar progressBar;
	private ImageButton right_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dynamic_main);
		upGradeDBifnessage();
		page = new Pager(0, HttpJsonTool.PAGE_SIZE);
		initContentView();
	}

	private void getTrainMoreListDate(final String keyword) {
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().getTrainList(minId, keyword,
						getApplicationContext(),false);
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
	private void getTrainListDate(final int page,final String keyword,final boolean ifclear) {
		TrainListDatetask = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().getTrainList(-1, keyword,
						getApplicationContext(),ifclear);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progressBar.setVisibility(View.GONE);
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
			T.show(getApplicationContext(), "您的账号已在其他设备上登录，请重新登录",
					Toast.LENGTH_LONG);
			startActivity(intent);
		} catch (Exception e) {
		}
	}

	private void upGradeDBifnessage() {
		DBHelper helper = new DBHelper(getApplicationContext());
		helper.close();
	}

	private void initContentView() {
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
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
		// progressBar = (ProgressBar) findViewById(R.id.progressBar);
		pulldownlist = (PullDownListView) findViewById(R.id.pulldownlist);
		pulldownlist.setRefreshListioner(new OnRefreshListioner() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				String keyword = searchEdit.getText().toString().trim();
				page = new Pager(0, HttpJsonTool.PAGE_SIZE);
				if(keyword.length()==0){
					getTrainListDate(0,null,true);
				}else{
					getTrainListDate(0,keyword,true);
				}
				
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				String keyword = searchEdit.getText().toString().trim();
				if(keyword.length()==0){
					getTrainMoreListDate(null);
				}else{
					getTrainMoreListDate(keyword);
				}
				
			}
		});
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
				int user_id = (Integer) Datalist.get(position - 1).get(
						"user_id");
				intent.putExtra("id", id);
				intent.putExtra("user_id", user_id);
				int isRead = (Integer) Datalist.get(position - 1).get("isRead");
				intent.putExtra("isRead", isRead);
				int isFinal = (Integer) Datalist.get(position - 1).get(
						"isFinal");
				intent.putExtra("isFinal", isFinal);
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
		searchEdit = (EditText) findViewById(R.id.search_edit);
		searchEdit.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					page = new Pager(0, HttpJsonTool.PAGE_SIZE);
					String keyword = searchEdit.getText().toString().trim();
					progressBar.setVisibility(View.VISIBLE);
					getTrainListDate(0,keyword,false);
					KeyBroadTool.hideKeyboard(getApplicationContext(),
							searchEdit);
				}
				return false;

			}
		});

		settingBtn = (ImageButton) findViewById(R.id.setting_btn);
		settingBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				page = new Pager(0, HttpJsonTool.PAGE_SIZE);
				String keyword = searchEdit.getText().toString().trim();
				progressBar.setVisibility(View.VISIBLE);
				getTrainListDate(0,keyword,false);
				KeyBroadTool.hideKeyboard(getApplicationContext(), searchEdit);
			}
		});
		right_btn = (ImageButton) findViewById(R.id.right_btn);
		right_btn.setVisibility(HttpJsonTool.manageTrain==1?View.VISIBLE:View.GONE);
		right_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DynamicMainActivity.this,
						addNewDynamicActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		progressBar.setVisibility(View.VISIBLE);
		refreshParolData(page.curpage, null);
		getTrainListDate(page.curpage,null,false);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(TrainListDatetask!=null){
			TrainListDatetask.cancel(true);
		}
		searchEdit.setText("");
	}
	private int minId = 0;

	@SuppressLint("SimpleDateFormat")
	private void refreshParolData(int mPage, String keyword) {
		Datalist.clear();
		DynamicListHelper helper = new DynamicListHelper(
				getApplicationContext());
		ArrayList<DynamicListHolder> holders;
		if (keyword==null) {
			holders = helper.selectData(0, (mPage + 1) * page.pagesize);
		} else {
			holders = helper.selectSearchData(keyword, 0, (mPage + 1)
					* page.pagesize);
		}
		helper.close();
		for (DynamicListHolder holder : holders) {
			addHolderData(holder);
		}
		L.i(holders.size()+"   "+page.pagesize);
		pulldownlist.setMore(holders.size() == page.pagesize);
		adapter.notifyDataSetChanged();
	}

	@SuppressLint("SimpleDateFormat")
	private void addHolderData(DynamicListHolder holder) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", holder.getId());
		data.put("train_number", holder.getBoard_train_code());
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		data.put("start_time", format.format(holder.getStart_time()));
		data.put("start_position", holder.getFrom_station_name());
		data.put("arrive_position", holder.getTo_station_name());
		data.put("watch_group", holder.getCurrent_team());
		data.put("Interval_position", "");
		data.put("station", holder.getDriving_status());
		data.put("user_id", holder.getUser_id());
		data.put("isRead", holder.getIsRead());
		data.put("isFinal", holder.getIsFinal());
		Datalist.add(data);
		minId = holder.getId();
	}

	private void refreshMoreParolData(String keyword) {
		page.curpage++;
		DynamicListHelper helper = new DynamicListHelper(
				getApplicationContext());
		ArrayList<DynamicListHolder> holders;
		if (keyword==null) {
			holders = helper.selectData(page.curpage* page.pagesize, page.pagesize);
		} else {
			holders = helper.selectSearchData(keyword, page.curpage* page.pagesize, page.pagesize);
		}
		helper.close();
		for (DynamicListHolder holder : holders) {
			addHolderData(holder);
		}
		pulldownlist.setMore(holders.size() == page.pagesize);
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
//		menu.add(100, 1, 1, "录数据");
//		menu.add(100, 2, 1, "清除缓存数据");
		return true;
	}

}
