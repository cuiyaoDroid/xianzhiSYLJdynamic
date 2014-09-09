package com.xianzhisecuritycheck.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xianzhi.tool.adapter.MyAdapter;
import com.xianzhi.tool.db.Pager;
import com.xianzhi.tool.db.PatrolHelper;
import com.xianzhi.tool.db.PatrolHolder;
import com.xianzhi.tool.view.PullDownListView;
import com.xianzhi.tool.view.PullDownListView.OnRefreshListioner;
import com.xianzhi.webtool.HttpJsonTool;
import com.xianzhisylj.dynamic.R;

public class SecurityMainActivity extends Activity implements OnClickListener {
	private PullDownListView pulldownlist;
	private List<Map<String, Object>> Datalist;
	private MyAdapter adapter;
	private Pager page;
	private static final int PAGE_SIZE = 15;
	private ImageButton gobackBtn;
	private ImageButton addNewBtn;
	private ProgressBar progressBar;
	private boolean isAutoRefresh = true;
	private TextView timeTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_security_main);
		gobackBtn = (ImageButton) findViewById(R.id.goback_btn);
		gobackBtn.setOnClickListener(this);
		addNewBtn = (ImageButton) findViewById(R.id.right_btn);
		addNewBtn.setOnClickListener(this);
		initContentView();
		page = new Pager(0, PAGE_SIZE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		menu.add(100, 1, 1, "清除缓存数据");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 1) {
			PatrolHelper helper = new PatrolHelper(getApplicationContext());
			helper.clear();
			helper.close();
			isAutoRefresh = true;
			refreshPatrolList();
		}
		return super.onContextItemSelected(item);
	}

	private void refreshPatrolList() {
		if (isAutoRefresh) {
			progressBar.setVisibility(View.VISIBLE);
		}
		AsyncTask<Void, Void, Void> async = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				HttpJsonTool json_tool = HttpJsonTool.getInstance();
				json_tool.checkOutPatrolList(getApplicationContext());
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (isAutoRefresh) {
					progressBar.setVisibility(View.GONE);
					isAutoRefresh = false;
				}
				pulldownlist.onRefreshComplete();
				if (HttpJsonTool.state403) {
					Toast.makeText(getApplicationContext(), "账号在其他设备上登录，请重新登录",
							Toast.LENGTH_LONG).show();
					HttpJsonTool.state403 = false;
					gotoLoginView();
					return;
				}
				refreshParolData(page.curpage);
			}
		};
		async.execute();
	}

	private void refreshMorePatrolList() {
		AsyncTask<Void, Void, Void> async = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				HttpJsonTool json_tool = HttpJsonTool.getInstance();
				json_tool
						.checkOutPatrolMoreList(minId, getApplicationContext());
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (HttpJsonTool.state403) {
					HttpJsonTool.state403 = false;
					gotoLoginView();
					return;
				}
				pulldownlist.onLoadMoreComplete();
				refreshMoreParolData();
			}
		};
		async.execute();
	}

	private int minId = -1;

	private void gotoLoginView() {
		try {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			ComponentName componentName = new ComponentName(
					"com.xianzhi.rail.cc", "com.xianzhi.rail.cc.LogoActivity");
			intent.setComponent(componentName);
			startActivity(intent);
			overridePendingTransition(0, 0);
			finish();
		} catch (Exception e) {

		}
	}

	@SuppressLint("SimpleDateFormat")
	private void refreshParolData(int mPage) {
		Datalist.clear();
		PatrolHelper helper = new PatrolHelper(getApplicationContext());
		ArrayList<PatrolHolder> holders = helper.selectData(
				SecurityCheckApp.userId, 0, (mPage + 1) * page.pagesize);
		helper.close();
		for (PatrolHolder holder : holders) {
			addParolData(holder);
		}
		pulldownlist.setMore(holders.size() == page.pagesize);
		adapter.notifyDataSetChanged();
	}

	@SuppressLint("SimpleDateFormat")
	private void addParolData(PatrolHolder holder) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", holder.getId());
		data.put("content", "<font color=#2E79C3>[" + holder.getType_name()
				+ "] </font>" + holder.getContent().replaceAll("\n", "<br />"));
		data.put("img", holder.getImg());
		data.put("userName", "责任人:" + holder.getResponsiblePersonName().trim());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		data.put("completeTime", format.format(holder.getCompleteTime()) + " 前");
		data.put("result_typeName", holder.getResult_typeId_name());
		Datalist.add(data);
		minId = holder.getId();
	}

	private void refreshMoreParolData() {
		page.curpage++;
		PatrolHelper helper = new PatrolHelper(getApplicationContext());
		ArrayList<PatrolHolder> holders = helper.selectData(
				SecurityCheckApp.userId, page.curpage * page.pagesize,
				page.pagesize);
		helper.close();
		for (PatrolHolder holder : holders) {
			addParolData(holder);
		}
		pulldownlist.setMore(holders.size() == page.pagesize);
		adapter.notifyDataSetChanged();
	}

	private void initContentView() {
		Datalist = new ArrayList<Map<String, Object>>();
		ListView receiveList = (ListView) findViewById(R.id.receive_list);
		adapter = new MyAdapter(getApplicationContext(), Datalist,
				R.layout.cell_securitycheck_list, new String[] { "userName",
						"completeTime" }, new int[] { R.id.checker_txt,
						R.id.time_txt });
		receiveList.setAdapter(adapter);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		pulldownlist = (PullDownListView) findViewById(R.id.pulldownlist);
		pulldownlist.setRefreshListioner(new OnRefreshListioner() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				page = new Pager(0, PAGE_SIZE);
				isAutoRefresh = false;
				refreshPatrolList();
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				refreshMorePatrolList();
			}
		});
		receiveList = pulldownlist.mListView;
		pulldownlist.setMore(true);

		receiveList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				int id = (Integer) Datalist.get(position - 1).get("id");
				Intent intent = new Intent(getApplicationContext(),
						SecurityDetailActivity.class);
				intent.putExtra("id", id);
				startActivityForResult(intent, Activity.RESULT_OK);
			}
		});
		receiveList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				return false;
			}
		});
//		initPlainContent();
	}

	/*private LinearLayout pain_page;
	private TasksCompletedView mTasksView;
	private int mTotalProgress;
	private int mCurrentProgress;
	private float startX;
	private float startY;
	private Calendar calendar;
	private ImageView prev_btn;
	private ImageView next_btn;
	private TextView tasks_txt;
	private ProgressBar progressCanderBar;
	private Map<String, String> calendar_Data;

	@SuppressLint("SimpleDateFormat")
	private void initPlainContent() {
		calendar_Data = new HashMap<String, String>();
		pain_page = (LinearLayout) findViewById(R.id.pain_page);
		final SimpleDateFormat interfaceformat = new SimpleDateFormat("yyyyMM");
		pain_page.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = event.getX();
					startY = event.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:

					float pointX = event.getX();
					float pointY = event.getY();

					if (Math.abs(startX - pointX) > Math.abs(startY - pointY)) {
						if (Math.abs(startX - pointX) > 50 && calendar != null) {
							if (startX - pointX > 0) {
								calendar.set(Calendar.MONTH,
										calendar.get(Calendar.MONTH) + 1);
							} else {
								calendar.set(Calendar.MONTH,
										calendar.get(Calendar.MONTH) - 1);
							}
							long l_time = calendar.getTimeInMillis();
							SimpleDateFormat format = new SimpleDateFormat(
									"yyyy年MM月");
							timeTxt.setText(format.format(l_time));
							refreshVariable(interfaceformat.format(calendar
									.getTimeInMillis()));
						}
					}
					break;
				default:
					break;
				}
				return true;
			}
		});
		timeTxt = (TextView) findViewById(R.id.time_txt);
		calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		long l_time = calendar.getTimeInMillis();
		final SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
		timeTxt.setText(format.format(l_time));
		prev_btn = (ImageView) findViewById(R.id.prev_btn);
		prev_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
				long l_time = calendar.getTimeInMillis();
				timeTxt.setText(format.format(l_time));
				refreshVariable(interfaceformat.format(calendar
						.getTimeInMillis()));
			}
		});
		next_btn = (ImageView) findViewById(R.id.next_btn);
		next_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
				long l_time = calendar.getTimeInMillis();
				timeTxt.setText(format.format(l_time));
				refreshVariable(interfaceformat.format(calendar
						.getTimeInMillis()));
			}
		});
		tasks_txt = (TextView) findViewById(R.id.tasks_txt);
		progressCanderBar = (ProgressBar) findViewById(R.id.progressCanderBar);
		mTasksView = (TasksCompletedView) findViewById(R.id.tasks_view);
		refreshVariable(interfaceformat.format(calendar.getTimeInMillis()));
	}

	AsyncTask<Void, Void, String> refreshVariable;

	private void refreshVariable(final String interfaceTime) {
		if (refreshVariable != null) {
			refreshVariable.cancel(true);
		}
		String calData = calendar_Data.get(interfaceTime);
		if (calData != null) {
			tasks_txt.setVisibility(View.VISIBLE);
			mTasksView.setVisibility(View.VISIBLE);
			String[] tasks = calData.split(",");
			if (tasks.length < 2) {
				return;
			}
			float num = Float.parseFloat(tasks[1]) / Float.parseFloat(tasks[0])
					* 100;
			BigDecimal b = new BigDecimal(num);
			float f_num = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			String str_tasks = "计划完成<font color=#FF9000>" + tasks[0]
					+ "</font>件<br />实际完成<font color=#2E79C3>" + tasks[1]
					+ "</font>件" + "<br />完成率<font color=#FF0000>" + f_num
					+ "%</font>";
			tasks_txt.setText(Html.fromHtml(str_tasks));
			mTasksView.setTotalProgress(Integer.parseInt(tasks[0]));
			mTotalProgress = Integer.parseInt(tasks[1]);
			mCurrentProgress = 0;
			new Thread(new ProgressRunable()).start();
			return;
		}

		progressCanderBar.setVisibility(View.VISIBLE);
		tasks_txt.setVisibility(View.GONE);
		mTasksView.setVisibility(View.GONE);
		refreshVariable = new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().getTaskStats(interfaceTime);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progressCanderBar.setVisibility(View.GONE);
				if (HttpJsonTool.state403) {
					Toast.makeText(getApplicationContext(), "账号在其他设备上登录，请重新登录",
							Toast.LENGTH_LONG).show();
					HttpJsonTool.state403 = false;
					gotoLoginView();
					return;
				}
				if (result.length() == 0) {
					tasks_txt.setVisibility(View.VISIBLE);
					tasks_txt.setText("\u3000暂无数据");
					return;
				}

				tasks_txt.setVisibility(View.VISIBLE);
				mTasksView.setVisibility(View.VISIBLE);
				String[] tasks = result.split(",");
				if (tasks.length < 2) {
					return;
				}
				calendar_Data.put(interfaceTime, result);
				float num = Float.parseFloat(tasks[1])
						/ Float.parseFloat(tasks[0]) * 100;
				BigDecimal b = new BigDecimal(num);
				float f_num = b.setScale(2, BigDecimal.ROUND_HALF_UP)
						.floatValue();
				String str_tasks = "计划完成<font color=#FF9000>" + tasks[0]
						+ "</font>件<br />实际完成<font color=#2E79C3>" + tasks[1]
						+ "</font>件" + "<br />完成率<font color=#FF0000>" + f_num
						+ "%</font>";
				tasks_txt.setText(Html.fromHtml(str_tasks));
				mTasksView.setTotalProgress(Integer.parseInt(tasks[0]));
				mTotalProgress = Integer.parseInt(tasks[1]);
				mCurrentProgress = 0;
				new Thread(new ProgressRunable()).start();
			}
		};
		refreshVariable.execute();
	}

	class ProgressRunable implements Runnable {
		@Override
		public void run() {
			while (mCurrentProgress < mTotalProgress) {
				mCurrentProgress += 1;
				mTasksView.setProgress(mCurrentProgress);
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
*/
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isAutoRefresh = true;
		refreshPatrolList();
		refreshParolData(page.curpage);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.goback_btn:
			finish();
			break;
		case R.id.right_btn:
			Intent intent = new Intent(getApplicationContext(),
					addNewInforActivity.class);
			startActivityForResult(intent, Activity.RESULT_OK);
			break;
		default:
			break;
		}
	}
}
