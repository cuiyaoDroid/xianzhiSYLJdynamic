package com.xianzhisylj.dynamic;

import java.text.ParseException;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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
import com.xianzhi.stool.T;
import com.xianzhi.tool.adapter.DynamicAdapter;
import com.xianzhi.tool.db.DBHelper;
import com.xianzhi.tool.db.DynamicHelper;
import com.xianzhi.tool.db.DynamicHolder;
import com.xianzhi.tool.db.DynamicListHelper;
import com.xianzhi.tool.db.DynamicListHolder;
import com.xianzhi.tool.db.EmployeesHelper;
import com.xianzhi.tool.db.EmployeesHolder;
import com.xianzhi.tool.db.GroupHelper;
import com.xianzhi.tool.db.GroupHolder;
import com.xianzhi.tool.db.NoteHelper;
import com.xianzhi.tool.db.NoteHolder;
import com.xianzhi.tool.db.Pager;
import com.xianzhi.tool.db.PassengerHelper;
import com.xianzhi.tool.db.PassengerHolder;
import com.xianzhi.tool.db.TrainHelper;
import com.xianzhi.tool.db.TrainHolder;
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

	private void getTrainMoreListDate() {
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().getTrainList(minId, null,
						getApplicationContext());
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
					refreshMoreParolData();
					pulldownlist.onLoadMoreComplete();
					return;
				}
			}
		};
		task.execute();
	}

	private void getTrainListDate() {
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().getTrainList(-1, null,
						getApplicationContext());
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result.startsWith(HttpJsonTool.ERROR403)) {
					gotoLoginView();
					return;
				} else if (result.startsWith(HttpJsonTool.ERROR)) {
					T.showLong(getApplicationContext(),
							result.replace(HttpJsonTool.ERROR, ""));
					return;
				} else if (result.startsWith(HttpJsonTool.SUCCESS)) {
					refreshParolData(page.curpage, "");
					pulldownlist.onRefreshComplete();
					progressBar.setVisibility(View.GONE);
					return;
				}
			}
		};
		task.execute();
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
				getTrainListDate();
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				getTrainMoreListDate();
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
		searchEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String keyword = searchEdit.getText().toString().trim();
				refreshParolData(0, keyword);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		searchEdit.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					String keyword = searchEdit.getText().toString().trim();
					refreshParolData(0, keyword);
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
				String keyword = searchEdit.getText().toString().trim();
				refreshParolData(0, keyword);
				KeyBroadTool.hideKeyboard(getApplicationContext(), searchEdit);
			}
		});
		right_btn = (ImageButton) findViewById(R.id.right_btn);
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
		refreshParolData(page.curpage, "");
		getTrainListDate();
	}

	private int minId = 0;

	@SuppressLint("SimpleDateFormat")
	private void refreshParolData(int mPage, String keyword) {
		Datalist.clear();
		DynamicListHelper helper = new DynamicListHelper(
				getApplicationContext());
		ArrayList<DynamicListHolder> holders;
		if (keyword.length() == 0) {
			holders = helper.selectData(0, (mPage + 1) * page.pagesize);
		} else {
			holders = helper.selectSearchData(keyword, 0, (mPage + 1)
					* page.pagesize);
		}
		helper.close();
		for (DynamicListHolder holder : holders) {
			addHolderData(holder);
		}
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

	private void refreshMoreParolData() {
		page.curpage++;
		DynamicListHelper helper = new DynamicListHelper(
				getApplicationContext());
		ArrayList<DynamicListHolder> holders = helper.selectData(page.curpage
				* page.pagesize, page.pagesize);
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
//		menu.add(100, 1, 1, "¼����");
//		menu.add(100, 2, 1, "�����������");
		return true;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 1) {
			DynamicHelper helper = new DynamicHelper(getApplicationContext());
			helper.clear();
			long timek7333 = 0;
			long timed8042 = 0;
			long timek2220 = 0;
			long time = System.currentTimeMillis();
			SimpleDateFormat format_today = new SimpleDateFormat("yyyy/MM/dd");
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			try {
				timed8042 = format.parse(format_today.format(time) + " 16:40")
						.getTime();
				timek7333 = format.parse(format_today.format(time) + " 18:47")
						.getTime();
				timek2220 = format.parse(format_today.format(time) + " 12:23")
						.getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DynamicHolder holder = new DynamicHolder(1, "d8042", timed8042,
					timed8042, "������", "����", "���7��4��", "��ɽ",
					DynamicHolder.daoda, 1);
			helper.insert(holder);
			holder = new DynamicHolder(2, "2220", timek2220, timek2220, "ګ��",
					"����", "���7��4��", "����", DynamicHolder.wandian, 1);
			helper.insert(holder);
			holder = new DynamicHolder(3, "k7333", timek7333, timek7333, "����",
					"�Ӽ�", "���7��4��", "����", DynamicHolder.dafa, 1);
			helper.insert(holder);
			helper.close();

			NoteHelper note_helper = new NoteHelper(getApplicationContext());
			note_helper.clear();

			NoteHolder note_holder = new NoteHolder(
					-1,
					1,
					"3080��",
					"86536Ԫ",
					"8634Ԫ",
					"��",
					"��",
					"286��",
					"��",
					"һ���������ص㹤����<br></br>1������᳹��ʵ���˸���Ҫ����ʵ���������С���<br></br>2��������ѩ������ʱ�����ݡ����Ӱ������ѩ��ȷ���ÿͳ˽���֯��ȫ��"
							+ "<br></br>3����ȡ���ڷ�����������¹ʵĽ�ѵ����Ϸٻ����е�ʵ��������μǸ���λ�Ļ���Σ���Բ���ʵ���ÿ��г����̹���涨���۷�վ�������Ҫ�����ð��칤����ȷ���г���ȫ����һʧ��"
							+ "<br></br>4��ץ��·������������ž�·�粻����ӳ��",
					"һ���������ص㹤����<br></br>1������᳹��ʵ���˸���Ҫ����ʵ���������С���<br></br>2��������ѩ������ʱ�����ݡ����Ӱ������ѩ��ȷ���ÿͳ˽���֯��ȫ��"
							+ "<br></br>3����ȡ���ڷ�����������¹ʵĽ�ѵ����Ϸٻ����е�ʵ��������μǸ���λ�Ļ���Σ���Բ���ʵ���ÿ��г����̹���涨���۷�վ�������Ҫ�����ð��칤����ȷ���г���ȫ����һʧ��"
							+ "<br></br>4��ץ��·������������ž�·�粻����ӳ��<br></br>������ʵ�ϼ��ļ�Ҫ��<br></br>1��ǿ���������������ǿ����Ѳ�ӣ����ٳ���Ա��ʵ������׼��ץ���۷�վ����ͣ���ɡ��ž�ְ������Υ�����ⷢ����",
					time + 29 * 60 * 60 * 1000, "��ʥ��", time + 29 * 60 * 60
							* 1000, "�������˶�075�ż��Ա");
			note_helper.insert(note_holder);
			note_holder = new NoteHolder(
					-1,
					2,
					"3180��",
					"96536Ԫ",
					"5123Ԫ",
					"��",
					"��",
					"423��",
					"��",
					"һ���������ص㹤����<br></br>1������᳹��ʵ���˸���Ҫ����ʵ���������С���<br></br>2��������ѩ������ʱ�����ݡ����Ӱ������ѩ��ȷ���ÿͳ˽���֯��ȫ��"
							+ "<br></br>3����ȡ���ڷ�����������¹ʵĽ�ѵ����Ϸٻ����е�ʵ��������μǸ���λ�Ļ���Σ���Բ���ʵ���ÿ��г����̹���涨���۷�վ�������Ҫ�����ð��칤����ȷ���г���ȫ����һʧ��"
							+ "<br></br>4��ץ��·������������ž�·�粻����ӳ��",
					"һ���������ص㹤����<br></br>1������᳹��ʵ���˸���Ҫ����ʵ���������С���<br></br>2��������ѩ������ʱ�����ݡ����Ӱ������ѩ��ȷ���ÿͳ˽���֯��ȫ��"
							+ "<br></br>3����ȡ���ڷ�����������¹ʵĽ�ѵ����Ϸٻ����е�ʵ��������μǸ���λ�Ļ���Σ���Բ���ʵ���ÿ��г����̹���涨���۷�վ�������Ҫ�����ð��칤����ȷ���г���ȫ����һʧ��"
							+ "<br></br>4��ץ��·������������ž�·�粻����ӳ��<br></br>������ʵ�ϼ��ļ�Ҫ��<br></br>1��ǿ���������������ǿ����Ѳ�ӣ����ٳ���Ա��ʵ������׼��ץ���۷�վ����ͣ���ɡ��ž�ְ������Υ�����ⷢ����",
					time + 29 * 60 * 60 * 1000, "��ʥ��", time + 29 * 60 * 60
							* 1000, "�������˶�078�ż��Ա");
			note_helper.insert(note_holder);
			note_holder = new NoteHolder(
					-1,
					3,
					"3334��",
					"83214Ԫ",
					"12314Ԫ",
					"��",
					"��",
					"324��",
					"��",
					"һ���������ص㹤����<br></br>1������᳹��ʵ���˸���Ҫ����ʵ���������С���<br></br>2��������ѩ������ʱ�����ݡ����Ӱ������ѩ��ȷ���ÿͳ˽���֯��ȫ��"
							+ "<br></br>3����ȡ���ڷ�����������¹ʵĽ�ѵ����Ϸٻ����е�ʵ��������μǸ���λ�Ļ���Σ���Բ���ʵ���ÿ��г����̹���涨���۷�վ�������Ҫ�����ð��칤����ȷ���г���ȫ����һʧ��"
							+ "<br></br>4��ץ��·������������ž�·�粻����ӳ��",
					"һ���������ص㹤����<br></br>1������᳹��ʵ���˸���Ҫ����ʵ���������С���<br></br>2��������ѩ������ʱ�����ݡ����Ӱ������ѩ��ȷ���ÿͳ˽���֯��ȫ��"
							+ "<br></br>3����ȡ���ڷ�����������¹ʵĽ�ѵ����Ϸٻ����е�ʵ��������μǸ���λ�Ļ���Σ���Բ���ʵ���ÿ��г����̹���涨���۷�վ�������Ҫ�����ð��칤����ȷ���г���ȫ����һʧ��"
							+ "<br></br>4��ץ��·������������ž�·�粻����ӳ��<br></br>������ʵ�ϼ��ļ�Ҫ��<br></br>1��ǿ���������������ǿ����Ѳ�ӣ����ٳ���Ա��ʵ������׼��ץ���۷�վ����ͣ���ɡ��ž�ְ������Υ�����ⷢ����",
					time + 29 * 60 * 60 * 1000, "��ʥ��", time + 29 * 60 * 60
							* 1000, "�������˶�063�ż��Ա");
			note_helper.insert(note_holder);
			note_helper.close();

			GroupHelper group_helper = new GroupHelper(getApplicationContext());
			group_helper.clear();
			GroupHolder group_holder = new GroupHolder(1, 1, "YZ344639",
					"128��", "��02", "�յ�|Ӳ��", "1,2");
			group_helper.insert(group_holder);
			group_holder = new GroupHolder(2, 1, "RW09234", "48��", "08",
					"�߼�����", "3,4");
			group_helper.insert(group_holder);
			group_holder = new GroupHolder(3, 1, "YZ344639", "128��", "09",
					"��ͨӲ��", "5");
			group_helper.insert(group_holder);
			group_holder = new GroupHolder(4, 1, "RW09234", "48��", "10",
					"�յ�|Ӳ��", "7,8");
			group_helper.insert(group_holder);

			group_holder = new GroupHolder(5, 2, "YZ344639", "128��", "��02",
					"�յ�|Ӳ��", "1,2");
			group_helper.insert(group_holder);
			group_holder = new GroupHolder(6, 2, "RW09234", "48��", "08",
					"�߼�����", "4");
			group_helper.insert(group_holder);
			group_holder = new GroupHolder(7, 2, "YZ344639", "128��", "09",
					"��ͨӲ��", "5,6");
			group_helper.insert(group_holder);
			group_holder = new GroupHolder(8, 2, "RW09234", "48��", "10",
					"�յ�|Ӳ��", "7,8");
			group_helper.insert(group_holder);

			group_holder = new GroupHolder(9, 3, "RW09234", "48��", "08",
					"�߼�����", "4");
			group_helper.insert(group_holder);
			group_holder = new GroupHolder(10, 3, "YZ344639", "128��", "��02",
					"�յ�|Ӳ��", "1,2");
			group_helper.insert(group_holder);
			group_holder = new GroupHolder(11, 3, "YZ344639", "128��", "09",
					"��ͨӲ��", "5,6");
			group_helper.insert(group_holder);
			group_holder = new GroupHolder(12, 3, "RW09234", "48��", "10",
					"�յ�|Ӳ��", "8");
			group_helper.insert(group_holder);

			group_helper.close();

			TrainHelper train_helper = new TrainHelper(getApplicationContext());
			train_helper.clear();
			TrainHolder train_holder = new TrainHolder("����,�ռ���,��ɽ,����,�߷���,����",
					"16:40,17:26,18:22,19:08,19:45,20:08", "d8042", "", "");
			train_helper.insert(train_holder);
			train_holder = new TrainHolder(
					"�������,̩��,������,�»���,̫����,����,�󰲱�,��ԭ,ũ��,����,������,��ƽ,��ԭ,����"
							+ ",������,����,����,��ɽ,����,��ʯ��,����,������,�߷���,������,����,����",
					"12:23,13:13,14:00,15:28,16:03"
							+ ",16:25,16:48,18:00,19:07,20:07,20:59,21:40,22:34,22:58,23:52,00:06,01:03,01:24,"
							+ "01:54,02:36,02:59,03:22,04:14,04:39,05:16,05:55",
					"2220", "", "");
			train_helper.insert(train_holder);
			train_holder = new TrainHolder(
					"����,����,������,�߷���,����,������,����,��ʯ��,����,��ɽ,����,������,��ƽ,������,����,����"
							+ ",�Ժ�,�ػ�,��ʯͷ,��ͼ,�Ӽ�",
					"18:47,19:14,19:50,20:13,20:36,21:21,21:45,22:13,22:41,23:09,00:14"
							+ ",00:33,02:32,03:07,04:02,05:55,07:38,09:29,09:56,10:44,11:46",
					"k7333", "", "");
			train_helper.insert(train_holder);
			train_helper.close();

			EmployeesHelper employees_helper = new EmployeesHelper(
					getApplicationContext());
			employees_helper.clear();
			EmployeesHolder employess_holder = new EmployeesHolder(-1, 1, 0,
					"tmp_user_04.png", "5438", "13814384250", "�г���", "��ʥ��",
					EmployeesHolder.TYPE_1);
			employees_helper.insert(employess_holder);
			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_01.png",
					"5438", "15814384250", "�˾���", "������", EmployeesHolder.TYPE_1);
			employees_helper.insert(employess_holder);
			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_02.png",
					"5238", "18812344250", "��������", "������",
					EmployeesHolder.TYPE_1);
			employees_helper.insert(employess_holder);
			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_03.png",
					"4238", "18314384890", "����Ա", "������", EmployeesHolder.TYPE_2);
			employees_helper.insert(employess_holder);
			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_04.png",
					"2412", "13814382425", "����Ա", "����ǿ", EmployeesHolder.TYPE_2);
			employees_helper.insert(employess_holder);
			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_01.png",
					"1523", "13692884925", "����Ա", "�¼���", EmployeesHolder.TYPE_2);
			employees_helper.insert(employess_holder);
			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_02.png",
					"5431", "15923245235", "����Ա", "����", EmployeesHolder.TYPE_2);
			employees_helper.insert(employess_holder);
			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_04.png",
					"2413", "13522314515", "����Ա", "������", EmployeesHolder.TYPE_2);
			employees_helper.insert(employess_holder);
			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_01.png",
					"4238", "18314384890", "����Ա", "������", EmployeesHolder.TYPE_2);
			employees_helper.insert(employess_holder);
			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_03.png",
					"2412", "13814382425", "����Ա", "����ǿ", EmployeesHolder.TYPE_2);
			employees_helper.insert(employess_holder);
			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_02.png",
					"1523", "13692884925", "����Ա", "�¼���", EmployeesHolder.TYPE_2);
			employees_helper.insert(employess_holder);
			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_04.png",
					"5431", "15923245235", "����Ա", "����", EmployeesHolder.TYPE_2);
			employees_helper.insert(employess_holder);
			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_01.png",
					"2413", "13522314515", "����Ա", "������", EmployeesHolder.TYPE_2);
			employees_helper.insert(employess_holder);
			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_02.png",
					"4238", "18314384890", "����Ա", "������", EmployeesHolder.TYPE_2);
			employees_helper.insert(employess_holder);
			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_03.png",
					"2412", "13814382425", "����Ա", "����ǿ", EmployeesHolder.TYPE_2);
			employees_helper.insert(employess_holder);
			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_04.png",
					"1523", "13692884925", "����Ա", "�¼���", EmployeesHolder.TYPE_2);
			employees_helper.insert(employess_holder);
			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_01.png",
					"5431", "15923245235", "����Ա", "����", EmployeesHolder.TYPE_2);
			employees_helper.insert(employess_holder);
			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_04.png",
					"2413", "13522314515", "����Ա", "������", EmployeesHolder.TYPE_2);
			employees_helper.insert(employess_holder);

			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_03.png",
					"4238", "18314384890", "�ۻ��鳤", "����", EmployeesHolder.TYPE_3);
			employees_helper.insert(employess_holder);
			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_02.png",
					"2412", "13814382425", "�ۻ�Ա", "����ϼ", EmployeesHolder.TYPE_3);
			employees_helper.insert(employess_holder);
			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_01.png",
					"1523", "13692884925", "�㲥Ա", "����", EmployeesHolder.TYPE_3);
			employees_helper.insert(employess_holder);
			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_02.png",
					"5431", "15923245235", "ֵ��Ա", "������", EmployeesHolder.TYPE_3);
			employees_helper.insert(employess_holder);
			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_04.png",
					"2413", "13522314515", "��¯�鳤", "����", EmployeesHolder.TYPE_3);
			employees_helper.insert(employess_holder);
			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_03.png",
					"5431", "15923245235", "��ˮԱ", "����", EmployeesHolder.TYPE_3);
			employees_helper.insert(employess_holder);
			employess_holder = new EmployeesHolder(-1, 1, 0, "tmp_user_01.png",
					"2413", "13522314515", "��ˮԱ", "�¶�", EmployeesHolder.TYPE_3);
			employees_helper.insert(employess_holder);
			employees_helper.close();

			PassengerHelper passenger_helper = new PassengerHelper(
					getApplicationContext());
			passenger_helper.clear();

			PassengerHolder passenger_holder = new PassengerHolder(-1, 1, 1,
					"ګ��", "342563229800603265", "����", "208", "������", "2,3");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 1, 1, "ګ��",
					"266241512790602314", "����", "208", "������", "2,6");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 1, 1, "ګ��",
					"512431123790602314", "����", "208", "������", "3,6");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 1, 1, "ګ��",
					"266341419792141414", "����", "208", "��Ȫ", "2,5");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 1, 1, "ګ��",
					"223144123490602314", "����", "208", "�ܻ���", "2,4");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 1, 1, "ګ��",
					"667456435340602314", "����", "208", "��С��", "1,10");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 1, 1, "ګ��",
					"444441419790602314", "����", "208", "������", "3,1");
			passenger_helper.insert(passenger_holder);

			passenger_holder = new PassengerHolder(-1, 1, 2, "ګ��",
					"266241512790602314", "����", "208", "������", "2,6");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 1, 2, "ګ��",
					"512431123790602314", "����", "208", "������", "3,6");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 1, 2, "ګ��",
					"266341419792141414", "����", "208", "��Ȫ", "2,5");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 1, 2, "ګ��",
					"223144123490602314", "����", "208", "�ܻ���", "2,4");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 1, 2, "ګ��",
					"667456435340602314", "����", "208", "��С��", "1,10");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 1, 2, "ګ��",
					"444441419790602314", "����", "208", "������", "3,1");
			passenger_helper.insert(passenger_holder);

			passenger_holder = new PassengerHolder(-1, 1, 3, "ګ��",
					"352623424500603265", "����", "208", "����", "3,5");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 1, 3, "ګ��",
					"998435249800603265", "����", "208", "������", "2,5");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 1, 3, "ګ��",
					"234262419800603265", "����", "208", "�Ž�", "4,10");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 1, 3, "ګ��",
					"456546419800603265", "����", "208", "�޽�", "0,0");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 1, 3, "ګ��",
					"227567566800603265", "����", "208", "�ƼҾ�", "4,11");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 1, 4, "ګ��",
					"123141129800603265", "����", "208", "������", "0,7");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 1, 4, "ګ��",
					"251234314314103265", "����", "208", "���", "1,5");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 1, 4, "ګ��",
					"251234314314103265", "����", "208", "����", "1,7");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 1, 4, "ګ��",
					"251234314314103265", "����", "208", "�ܱʳ�", "2,3");
			passenger_helper.insert(passenger_holder);

			passenger_holder = new PassengerHolder(-1, 2, 5, "ګ��",
					"342563229800603265", "����", "208", "������", "2,3");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 2, 5, "ګ��",
					"266241512790602314", "����", "208", "������", "2,6");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 2, 5, "ګ��",
					"512431123790602314", "����", "208", "������", "3,6");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 2, 5, "ګ��",
					"266341419792141414", "����", "208", "��Ȫ", "2,5");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 2, 5, "ګ��",
					"223144123490602314", "����", "208", "�ܻ���", "2,4");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 2, 5, "ګ��",
					"667456435340602314", "����", "208", "��С��", "1,10");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 2, 5, "ګ��",
					"444441419790602314", "����", "208", "������", "3,1");
			passenger_helper.insert(passenger_holder);

			passenger_holder = new PassengerHolder(-1, 1, 1, "ګ��",
					"266241512790602314", "����", "208", "������", "2,6");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 2, 6, "ګ��",
					"512431123790602314", "����", "208", "������", "3,6");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 2, 6, "ګ��",
					"266341419792141414", "����", "208", "��Ȫ", "2,5");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 2, 6, "ګ��",
					"223144123490602314", "����", "208", "�ܻ���", "2,4");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 2, 6, "ګ��",
					"667456435340602314", "����", "208", "��С��", "1,10");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 2, 6, "ګ��",
					"444441419790602314", "����", "208", "������", "3,1");
			passenger_helper.insert(passenger_holder);

			passenger_holder = new PassengerHolder(-1, 2, 7, "ګ��",
					"352623424500603265", "����", "208", "����", "3,5");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 2, 7, "ګ��",
					"998435249800603265", "����", "208", "������", "2,5");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 2, 7, "ګ��",
					"234262419800603265", "����", "208", "�Ž�", "4,10");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 2, 7, "ګ��",
					"456546419800603265", "����", "208", "�޽�", "0,0");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 2, 7, "ګ��",
					"227567566800603265", "����", "208", "�ƼҾ�", "4,11");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 1, 3, "ګ��",
					"123141129800603265", "����", "208", "������", "0,7");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 2, 8, "ګ��",
					"251234314314103265", "����", "208", "���", "1,5");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 2, 8, "ګ��",
					"251234314314103265", "����", "208", "����", "1,7");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 2, 8, "ګ��",
					"251234314314103265", "����", "208", "�ܱʳ�", "2,3");
			passenger_helper.insert(passenger_holder);

			passenger_holder = new PassengerHolder(-1, 3, 9, "����",
					"342563229800603265", "�Ӽ�", "208", "������", "2,7");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 3, 9, "����",
					"266241512790602314", "�Ӽ�", "208", "������", "2,6");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 3, 9, "����",
					"512431123790602314", "�Ӽ�", "208", "������", "3,6");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 3, 9, "����",
					"266341419792141414", "�Ӽ�", "208", "��Ȫ", "2,5");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 3, 9, "����",
					"223144123490602314", "�Ӽ�", "208", "�ܻ���", "2,4");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 3, 9, "����",
					"667456435340602314", "�Ӽ�", "208", "��С��", "1,10");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 3, 9, "����",
					"444441419790602314", "�Ӽ�", "208", "������", "3,1");
			passenger_helper.insert(passenger_holder);

			passenger_holder = new PassengerHolder(-1, 3, 10, "����",
					"266241512790602314", "�Ӽ�", "208", "������", "2,6");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 3, 10, "����",
					"512431123790602314", "�Ӽ�", "208", "������", "3,6");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 3, 10, "����",
					"266341419792141414", "�Ӽ�", "208", "��Ȫ", "2,5");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 3, 10, "����",
					"223144123490602314", "�Ӽ�", "208", "�ܻ���", "2,4");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 3, 10, "����",
					"667456435340602314", "�Ӽ�", "208", "��С��", "1,10");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 3, 10, "����",
					"444441419790602314", "�Ӽ�", "208", "������", "3,1");
			passenger_helper.insert(passenger_holder);

			passenger_holder = new PassengerHolder(-1, 3, 11, "����",
					"352623424500603265", "�Ӽ�", "208", "����", "3,5");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 3, 11, "����",
					"998435249800603265", "�Ӽ�", "208", "������", "2,5");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 3, 11, "����",
					"234262419800603265", "�Ӽ�", "208", "�Ž�", "4,10");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 3, 11, "����",
					"456546419800603265", "�Ӽ�", "208", "�޽�", "0,0");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 3, 11, "����",
					"227567566800603265", "�Ӽ�", "208", "�ƼҾ�", "4,11");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 3, 12, "����",
					"123141129800603265", "�Ӽ�", "208", "������", "0,7");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 3, 12, "����",
					"251234314314103265", "�Ӽ�", "208", "���", "1,5");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 3, 12, "����",
					"251234314314103265", "�Ӽ�", "208", "����", "1,7");
			passenger_helper.insert(passenger_holder);
			passenger_holder = new PassengerHolder(-1, 3, 12, "����",
					"251234314314103265", "�Ӽ�", "208", "�ܱʳ�", "2,3");
			passenger_helper.insert(passenger_holder);
			passenger_helper.close();

			refreshParolData(page.curpage, "");
		} else if (item.getItemId() == 2) {
			DynamicHelper helper = new DynamicHelper(getApplicationContext());
			helper.clear();
			helper.close();
		}

		return super.onContextItemSelected(item);
	}
}
