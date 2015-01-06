package com.xianzhisylj.dynamiclcz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.xianzhi.stool.L;
import com.xianzhi.stool.T;
import com.xianzhi.tool.adapter.GridListAdapter;
import com.xianzhi.tool.db.DynamicListHelper;
import com.xianzhi.tool.db.DynamicListHolder;
import com.xianzhi.tool.db.EmployeesHelper;
import com.xianzhi.tool.db.EmployeesHolder;
import com.xianzhi.tool.db.UserHelper;
import com.xianzhi.tool.db.UserHolder;
import com.xianzhi.tool.view.MyGridView;
import com.xianzhi.webtool.HttpJsonTool;
import com.xianzhisecuritycheck.main.SecurityCheckApp;

public class TrainInfoActivity extends Activity {
	private TextView detailTitleTxt;
	private int train_id = -1;
	private int crew_id = -1;
	private MyGridView gridview_1;
	private MyGridView gridview_2;
	private MyGridView gridview_3;
	private ArrayList<HashMap<String, Object>> grid1_Data;
	private GridListAdapter grid1_Adapter;
	private ArrayList<HashMap<String, Object>> grid2_Data;
	private GridListAdapter grid2_Adapter;
	private ArrayList<HashMap<String, Object>> grid3_Data;
	private GridListAdapter grid3_Adapter;
	private TextView cancal_txt;
	private ProgressDialog progreeDialog;

	private void initProgressDialog() {
		progreeDialog = new ProgressDialog(this);
		progreeDialog.setTitle("");
		progreeDialog.setMessage("正在取消认领...");
		progreeDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		grid1_Data = new ArrayList<HashMap<String, Object>>();
		grid2_Data = new ArrayList<HashMap<String, Object>>();
		grid3_Data = new ArrayList<HashMap<String, Object>>();
		initProgressDialog();
		initContentView();
	}

	private void cancalTrainConductor() {
		progreeDialog.show();
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().clearTrainConductor(
						getApplicationContext(), train_id);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progreeDialog.dismiss();
				if (result.startsWith(HttpJsonTool.ERROR403)) {
					gotoLoginView();
					return;
				} else if (result.startsWith(HttpJsonTool.ERROR)) {
					T.show(getApplicationContext(),
							result.replace(HttpJsonTool.ERROR, ""),
							Toast.LENGTH_LONG);
				} else if (result.startsWith(HttpJsonTool.SUCCESS)) {
					T.show(getApplicationContext(), "取消成功", Toast.LENGTH_LONG);
					finish();
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
			T.show(getApplicationContext(), "您的账号已在其他设备上登录，请重新登录",
					Toast.LENGTH_LONG);
			startActivity(intent);
		} catch (Exception e) {
		}
	}

	@SuppressLint("SimpleDateFormat")
	private void initContentView() {
		setContentView(R.layout.activity_train_info);
		detailTitleTxt = (TextView) findViewById(R.id.detail_title_txt);
		final boolean isMyself = getIntent().getBooleanExtra("ismySelf", false);
		int isFinal = getIntent().getIntExtra("isfinal", 0);
		cancal_txt = (TextView) findViewById(R.id.cancal_txt);
		cancal_txt.setClickable(true);
		cancal_txt.setText(Html
				.fromHtml("<font color=#006AE2><u>取消认领</u></font>"));

		gridview_1 = (MyGridView) findViewById(R.id.gridview_1);
		gridview_2 = (MyGridView) findViewById(R.id.gridview_2);
		gridview_3 = (MyGridView) findViewById(R.id.gridview_3);
		train_id = getIntent().getIntExtra("id", -1);
		if (train_id == -1) {
			return;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		DynamicListHelper helper = new DynamicListHelper(
				getApplicationContext());
		final DynamicListHolder holder = helper.selectData_Id(train_id);
		helper.close();
		if(holder==null){
			finish();
			return;
		}
		// crew_id = holder.getWatch_group_id();
		String str_checker = "<font color=#006AE2>"
				+ holder.getBoard_train_code() + "</font>" + "\u3000"
				+ holder.getFrom_station_name() + "━━"
				+ holder.getTo_station_name() + " | 发车时间:"
				+ format.format(holder.getStart_time()) + "\u3000"
				+ (holder.getCurrent_team().trim().length()>0 ? "当值班组:" : "未被认领")
				+ holder.getCurrent_team();
		detailTitleTxt.setText(Html.fromHtml(str_checker));
		L.i("========isFinal===="+isFinal);
		cancal_txt.setVisibility(isFinal != 1? View.VISIBLE : View.GONE);

		cancal_txt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isMyself) {
					AlertDialog.Builder builder = new Builder(
							TrainInfoActivity.this);
					builder.setMessage("确认取消认领吗？");
					builder.setTitle("提示");
					builder.setPositiveButton("确认",
							new Dialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									cancalTrainConductor();
								}
							});
					builder.setNegativeButton("取消",
							new Dialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					builder.create().show();
				}else{
					Intent intent = new Intent(getApplicationContext(),
							addNewDynamicActivity.class);
					intent.putExtra("id", train_id);
					intent.putExtra("isfirst", holder.getUser_name().length()==0);
					intent.putExtra(DynamicListHelper.board_train_code, holder.getBoard_train_code());
					intent.putExtra(DynamicListHelper.start_time, holder.getStart_time());
					startActivity(intent);
					finish();
				}
			}
		});

		if (!isMyself) {
			//gridview_1.setVisibility(View.GONE);
			cancal_txt.setText(Html
					.fromHtml("<font color=#006AE2><u>确认</u></font>"));
		} 
		if(holder.getUser_name().trim().length()>0){
			initGridView();
			getUserInfo(holder);
		}else{
			gridview_1.setVisibility(View.GONE);
		}
	}
	private void getUserInfo(final DynamicListHolder holder){
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().getUserList(
						getApplicationContext(), train_id);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progreeDialog.dismiss();
				if (result.startsWith(HttpJsonTool.ERROR403)) {
					gotoLoginView();
					return;
				} else if (result.startsWith(HttpJsonTool.ERROR)) {
					T.show(getApplicationContext(),
							result.replace(HttpJsonTool.ERROR, ""),
							Toast.LENGTH_LONG);
				} else if (result.startsWith(HttpJsonTool.SUCCESS)) {
				}
				refreshCellToGrid_1(holder);
			}

		};
		task.execute();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// refreshCellToGrid_1();
		// refreshCellToGrid_2();
		// refreshCellToGrid_3();
	}

	private void initGridView() {
		grid1_Adapter = new GridListAdapter(this, grid1_Data,
				R.layout.cell_main_per_view, new String[] {}, new int[] {});
		gridview_1.setAdapter(grid1_Adapter);
		gridview_1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				callTellNum((String) (grid1_Data.get(position).get("tel")));
			}
		});
		// grid2_Adapter = new GridListAdapter(this, grid2_Data,
		// R.layout.cell_small_per_view, new String[] { },
		// new int[] { });
		// gridview_2.setAdapter(grid2_Adapter);
		// gridview_2.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int position,
		// long arg3) {
		// // TODO Auto-generated method stub
		// callTellNum((String)(grid2_Data.get(position).get("tel")));
		// }
		// });
		// grid3_Adapter = new GridListAdapter(this, grid3_Data,
		// R.layout.cell_small_per_view, new String[] { },
		// new int[] { });
		// gridview_3.setAdapter(grid3_Adapter);
		// gridview_3.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int position,
		// long arg3) {
		// // TODO Auto-generated method stub
		// callTellNum((String)(grid3_Data.get(position).get("tel")));
		// }
		// });
	}

	private void callTellNum(String num) {
		L.i("tel:" + num);
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num));
		startActivity(intent);
	}

	private void refreshCellToGrid_1(DynamicListHolder holder) {
		grid1_Data.clear();
		String user_ids=holder.getUser_ids();
		String userIds[]=user_ids.trim().split(",");
		UserHelper helper=new UserHelper(getApplicationContext());
		for(String user_id:userIds){
			if(user_id.length()==0){
				continue;
			}
			UserHolder userholder=helper.selectData_Id(Integer.parseInt(user_id));
			if(userholder==null){
				continue;
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", userholder.getPosition_name()
					+ "<br></br><font color=#006AE2>" + userholder.getUser_name()
					+ "</font>");
			map.put("tel", userholder.getPhone());
			map.put("img", userholder.getPhoto());
			grid1_Data.add(map);
		}
		helper.close();	
		grid1_Adapter.notifyDataSetChanged();
	}

	private void refreshCellToGrid_2() {
		grid2_Data.clear();
		EmployeesHelper helper = new EmployeesHelper(getApplicationContext());
		ArrayList<EmployeesHolder> holders = helper.selectData_CrewId(crew_id,
				EmployeesHolder.TYPE_2);
		helper.close();
		for (EmployeesHolder holder : holders) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", holder.getName());
			map.put("tel", holder.getPhone_num());
			map.put("img", holder.getHead_img());
			grid2_Data.add(map);
		}
		grid2_Adapter.notifyDataSetChanged();
	}

	private void refreshCellToGrid_3() {
		grid3_Data.clear();
		EmployeesHelper helper = new EmployeesHelper(getApplicationContext());
		ArrayList<EmployeesHolder> holders = helper.selectData_CrewId(crew_id,
				EmployeesHolder.TYPE_3);
		helper.close();
		for (EmployeesHolder holder : holders) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name",
					holder.getPosition() + "<br></br>" + holder.getName());
			map.put("tel", holder.getPhone_num());
			map.put("img", holder.getHead_img());
			grid3_Data.add(map);
		}
		grid3_Adapter.notifyDataSetChanged();
	}

}
