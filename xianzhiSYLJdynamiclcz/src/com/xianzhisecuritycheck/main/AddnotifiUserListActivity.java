package com.xianzhisecuritycheck.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xianzhi.stool.KeyBroadTool;
import com.xianzhi.tool.adapter.ExpandableContractAdapter;
import com.xianzhi.tool.adapter.SearchListAdapter;
import com.xianzhi.tool.db.ContactListHelper;
import com.xianzhi.tool.db.ContactListHolder;
import com.xianzhi.tool.db.MapHolder;
import com.xianzhi.webtool.HttpJsonTool;
import com.xianzhisylj.dynamiclcz.R;


public class AddnotifiUserListActivity extends Activity implements
		OnClickListener {
	private ImageButton goback_btn;
	private ImageButton commitBtn;
	private List<Map<String, Object>> group;
	private List<List<Map<String, Object>>> children;
	private ExpandableContractAdapter adapter;
	private ExpandableListView expandableListView;
	private LinearLayout userList;
	private ListView searchList;
	private SearchListAdapter searchAdapter;
	private List<Map<String, Object>> searchdata;
	private EditText searchEdit;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_to_view);
		goback_btn = (ImageButton) findViewById(R.id.goback_btn);
		goback_btn.setOnClickListener(this);
		commitBtn = (ImageButton) findViewById(R.id.right_btn);
		commitBtn.setOnClickListener(this);
		userList = (LinearLayout) findViewById(R.id.user_list);
		users = new HashMap<Integer,String>();
		initProgressDialog();
		initExpandableView();
		initsearchView();
		SharedPreferences userInfo = getSharedPreferences("user_info", 0);
		boolean isfirst=userInfo.getBoolean("first", true);
		if(isfirst){
			refreshUserList();
		}
	}
	private ProgressDialog progressDialog;
	private void initProgressDialog() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("");
		progressDialog.setMessage("正在为您读取联系人列表，由于数据量较大，请耐心等待...");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(false);
	}
	private void refreshUserList() {
		progressDialog.show();
		AsyncTask<Void, Void, Void> async = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				HttpJsonTool json_tool = HttpJsonTool.getInstance();
				json_tool.checkOutDepartmentContact(getApplicationContext());
				json_tool.checkOutUserContext(getApplicationContext());
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progressDialog.dismiss();
				if (HttpJsonTool.state403) {
					Toast.makeText(getApplicationContext(), "账号在其他设备上登录，请重新登录", Toast.LENGTH_LONG).show();
					HttpJsonTool.state403=false;
					gotoLoginView();
					return;
				}
				refreshData();
				SharedPreferences userInfo = getSharedPreferences("user_info", 0);
				userInfo.edit().putBoolean("first", false).commit();
			}
		};
		async.execute();
	}
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		menu.add(100, 1, 1, "刷新联系人列表");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 1) {
			refreshUserList();
		}
		return super.onContextItemSelected(item);
	}
	private void initsearchView() {
		searchdata = new ArrayList<Map<String, Object>>();
		searchList = (ListView) findViewById(R.id.searchlist);
		searchAdapter = new SearchListAdapter(getApplicationContext(),
				searchdata, R.layout.cell_expandable_child, new String[] {
						"name", "position", "check" }, new int[] {
						R.id.child_name, R.id.child_job, R.id.check });
		searchList.setAdapter(searchAdapter);
		searchEdit = (EditText) findViewById(R.id.search_edit);
		searchEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s.length() == 0) {
					searchList.setVisibility(View.GONE);
					expandableListView.setVisibility(View.VISIBLE);
				} else {
					searchList.setVisibility(View.VISIBLE);
					expandableListView.setVisibility(View.GONE);
					refreshsearchData();
				}
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
		searchList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				CheckBox check = (CheckBox) arg1.findViewById(R.id.check);
				boolean ifcheck = !check.isChecked();
				check.setChecked(ifcheck);
				Map<String, Object> list = searchdata.get(arg2);
				list.put("check", ifcheck);
				int group = -1;
				int id=(Integer) list.get("id");
				String content = (String) list.get("name") + " " + group + " " + 0;
				if (ifcheck) {
					users.put(id,content);
				} else {
					users.remove(id);
				}
				refreshUserView();
			}
		});
		ImageButton settingBtn = (ImageButton) findViewById(R.id.setting_btn);
		settingBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				KeyBroadTool.hideKeyboard(getApplicationContext(), searchEdit);
			}
		});
	}

	private void initExpandableView() {
		group = new ArrayList<Map<String, Object>>();
		children = new ArrayList<List<Map<String, Object>>>();
		adapter = new ExpandableContractAdapter(this, group,
				R.layout.cell_expandable_group, new String[] { "name" },
				new int[] { R.id.group_name }, children,
				R.layout.cell_expandable_child, new String[] { "name",
						"position", "check" }, new int[] { R.id.child_name,
						R.id.child_job, R.id.check });
		expandableListView = (ExpandableListView) findViewById(R.id.list);
		expandableListView.setAdapter(adapter);
		expandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long arg) {

				/*
				 * Toast.makeText( AddnotifiUserListActivity.this, "你点击了" +
				 * adapter.getChild(groupPosition, childPosition),
				 * Toast.LENGTH_SHORT).show();
				 */
				Map<String, Object> list = (Map<String, Object>) adapter
						.getChild(groupPosition, childPosition);
				CheckBox check = (CheckBox) v.findViewById(R.id.check);
				boolean ifcheck = !check.isChecked();
				check.setChecked(ifcheck);
				list.put("check", ifcheck);
				int id=(Integer) list.get("id");
				String content = (String) list.get("name") + " " + groupPosition + " "
						+ childPosition;
				if (ifcheck) {
					users.put(id,content);
				} else {
					users.remove(id);
				}
				refreshUserView();
				return false;
			}
		});
	}

	private Map<Integer,String> users;

	private void refreshUserView() {
		userList.removeAllViews();
		Iterator<Integer> userIterator=users.keySet().iterator();
		while (userIterator.hasNext()) {
			final int vid=userIterator.next();
			View view = getLayoutInflater().inflate(R.layout.cell_usertop_list,
					null);
			TextView txt = (TextView) view.findViewById(R.id.name_txt);
			final String[] user = users.get(vid).split(" ");
			txt.setText(user[0]);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (Integer.parseInt(user[1]) != -1) {
						((Map<String, Object>) adapter.getChild(
								Integer.parseInt(user[1]),
								Integer.parseInt(user[2]))).put("check", false);
					}
					searchEdit.setText("");
					users.remove(vid);
					refreshUserView();
					adapter.notifyDataSetChanged();
				}
			});
			userList.addView(view);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshData();
	}

	private void refreshData() {
		group.clear();
		children.clear();
		ContactListHelper helper = new ContactListHelper(
				getApplicationContext());
		ArrayList<ContactListHolder> holders = helper.selectDepIdData(0);
		for (ContactListHolder holder : holders) {
			Map<String, Object> c_group = new HashMap<String, Object>();
			c_group.put("name", holder.getName());
			c_group.put("id", holder.getId());
			group.add(c_group);
			ArrayList<ContactListHolder> childholders = helper
					.selectDepIdData(holder.getId());
			List<Map<String, Object>> childlist = new ArrayList<Map<String, Object>>();
			for (ContactListHolder childholder : childholders) {
				Map<String, Object> c_child = new HashMap<String, Object>();
				c_child.put("name", childholder.getName());
				c_child.put("position", childholder.getPosition());
				c_child.put("id", childholder.getId());
				c_child.put("check", false);
				childlist.add(c_child);
			}
			children.add(childlist);
		}
		helper.close();
		adapter.notifyDataSetChanged();
	}

	private void refreshsearchData() {
		searchdata.clear();
		ContactListHelper helper = new ContactListHelper(
				getApplicationContext());
		ArrayList<ContactListHolder> holders = helper.selectData(searchEdit
				.getText().toString().trim());
		helper.close();
		for (ContactListHolder holder : holders) {
			Map<String, Object> c_child = new HashMap<String, Object>();
			c_child.put("name", holder.getName());
			c_child.put("position", holder.getPosition());
			c_child.put("id", holder.getId());
			c_child.put("check", false);
			searchdata.add(c_child);
		}
		searchAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.goback_btn:
			finish();
			break;
		case R.id.right_btn:
			Intent intent = new Intent(AddnotifiUserListActivity.this,
					addNewInforActivity.class);
			MapHolder holder=new MapHolder(users);
			Bundle bundle=new Bundle();
			bundle.putSerializable("users", holder);
			intent.putExtras(bundle);
			setResult(addNewInforActivity.AddnotifiUser, intent);
			finish();
		default:
			break;
		}
	}
}