package com.xianzhisylj.dynamiclcz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.xianzhi.stool.T;
import com.xianzhi.tool.adapter.GroupInfoAdapter;
import com.xianzhi.tool.db.EmployeesHelper;
import com.xianzhi.tool.db.TrainCoachHelper;
import com.xianzhi.tool.db.TrainCoachHolder;
import com.xianzhi.webtool.HttpJsonTool;
import com.xianzhisecuritycheck.main.SecurityCheckApp;
import com.xianzhisylj.dynamiclcz.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class GroupInfoActivity extends Activity{
	private ListView group_info_list;
	private ArrayList<Map<String,Object>>groupData;
	private GroupInfoAdapter adapter;
	private int tran_id;
	private ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_info);
		groupData=new ArrayList<Map<String,Object>>();
		initContentView();
		getGroupDate();
	}
	private void initContentView(){
		tran_id=getIntent().getIntExtra("id", -1);
		if(tran_id==-1){
			return;
		}
		progressBar=(ProgressBar)findViewById(R.id.progressBar);
		group_info_list=(ListView)findViewById(R.id.group_info_list);
		adapter=new GroupInfoAdapter(getApplicationContext(), groupData, R.layout.cell_group_info
				, new String[]{"coach_no","train_code","train_type","passenger_num","real_num"}
				, new int[]{R.id.name_txt,R.id.train_code_txt,R.id.train_type_txt
				,R.id.passenger_num_txt,R.id.real_num_txt});
		group_info_list.setAdapter(adapter);
		group_info_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getApplicationContext(),CompartmentActivity.class);
				intent.putExtra("coach_no", (String)groupData.get(arg2).get("coach_no"));
				intent.putExtra("trainCode", (String)groupData.get(arg2).get("trainCode"));
				intent.putExtra("trainDate", (String)groupData.get(arg2).get("trainDate"));
				intent.putExtra("train_type", (String)groupData.get(arg2).get("train_type"));
				intent.putExtra("limit2", (Integer)groupData.get(arg2).get("limit2"));
				startActivity(intent);
			}
		});
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		RefreshGroupData();
	}
	private void getGroupDate(){
		progressBar.setVisibility(View.VISIBLE);
		group_info_list.setVisibility(View.GONE);
		AsyncTask<Void, Void, String> task =new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().getTrainCoachList(getApplicationContext(), tran_id);
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progressBar.setVisibility(View.GONE);
				group_info_list.setVisibility(View.VISIBLE);
				if(result.startsWith(HttpJsonTool.ERROR403)){
					gotoLoginView();
					return;
				}else if(result.startsWith(HttpJsonTool.ERROR)){
					T.showLong(getApplicationContext(), result.replace(HttpJsonTool.ERROR, ""));
					return;
				}else if(result.startsWith(HttpJsonTool.SUCCESS)){
					RefreshGroupData();
					return;
				}
			}
		};
		task.execute();
	}
	private void gotoLoginView() {
		SecurityCheckApp.token = "";
		try {
			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
			T.show(getApplicationContext(), "您的账号已在其他设备上登录，请重新登录", Toast.LENGTH_LONG);
			startActivity(intent);
		} catch (Exception e) {
		}
	}
	private void RefreshGroupData(){
		groupData.clear();
		TrainCoachHelper helper=new TrainCoachHelper(getApplicationContext());
		ArrayList<TrainCoachHolder>holders=helper.selectDataTrainId(tran_id);
		helper.close();
		if(holders==null){
			adapter.notifyDataSetChanged();
			return;
		}
		EmployeesHelper employess_helper=new EmployeesHelper(getApplicationContext());
		for(TrainCoachHolder holder:holders){
			Map<String,Object>data=new HashMap<String, Object>();
			data.put("train_code", holder.getTrain_no());
			data.put("passenger_num", holder.getActual()+"/"+(holder.getLimit1()+holder.getLimit2()));
			data.put("limit2", holder.getLimit2());
			data.put("coach_no", holder.getCoach_no());
			data.put("train_type", holder.getCoach_type());
			data.put("trainCode", holder.getTrainCode());
			data.put("trainDate", holder.getTrainDate());
			data.put("real_num", holder.getPassenger());
			groupData.add(data);
		}
		employess_helper.close();
		adapter.notifyDataSetChanged();
	}
}
