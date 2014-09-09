package com.xianzhisylj.dynamic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.xianzhi.tool.adapter.GroupInfoAdapter;
import com.xianzhi.tool.db.EmployeesHelper;
import com.xianzhi.tool.db.EmployeesHolder;
import com.xianzhi.tool.db.GroupHelper;
import com.xianzhi.tool.db.GroupHolder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class GroupInfoActivity extends Activity{
	private ListView group_info_list;
	private ArrayList<Map<String,Object>>groupData;
	private GroupInfoAdapter adapter;
	private int tran_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_info);
		groupData=new ArrayList<Map<String,Object>>();
		initContentView();
	}
	private void initContentView(){
		tran_id=getIntent().getIntExtra("id", -1);
		if(tran_id==-1){
			return;
		}
		group_info_list=(ListView)findViewById(R.id.group_info_list);
		adapter=new GroupInfoAdapter(getApplicationContext(), groupData, R.layout.cell_group_info
				, new String[]{"name","train_code","train_type","passenger_num"}
				, new int[]{R.id.name_txt,R.id.train_code_txt,R.id.train_type_txt,R.id.passenger_num_txt});
		group_info_list.setAdapter(adapter);
		group_info_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getApplicationContext(),CompartmentActivity.class);
				intent.putExtra("em_names", (String)groupData.get(arg2).get("em_names"));
				intent.putExtra("name", (String)groupData.get(arg2).get("name"));
				intent.putExtra("id", (Integer)groupData.get(arg2).get("id"));
				intent.putExtra("dynamic_id", tran_id);
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
	private void RefreshGroupData(){
		groupData.clear();
		GroupHelper helper=new GroupHelper(getApplicationContext());
		ArrayList<GroupHolder>holders=helper.selectData(tran_id);
		helper.close();
		if(holders==null){
			adapter.notifyDataSetChanged();
			return;
		}
		EmployeesHelper employess_helper=new EmployeesHelper(getApplicationContext());
		for(GroupHolder holder:holders){
			Map<String,Object>data=new HashMap<String, Object>();
			data.put("id", holder.getId());
			data.put("crew_group_id", holder.getCrew_group_id());
			data.put("train_code", holder.getTrain_code());
			data.put("passenger_num", holder.getPassenger_num());
			data.put("name", holder.getName());
			data.put("train_type", holder.getTrain_type());
			String[] ids=holder.getCrew_em_ids().split(",");
			String name="";
			String head_img="";
			String tel="";
			for(String id:ids){
				int i_id=Integer.parseInt(id);
				EmployeesHolder employess_holder=employess_helper.selectData_Id(i_id);
				name+=employess_holder.getName()+",";
				head_img+=employess_holder.getHead_img()+",";
				tel+=employess_holder.getPhone_num()+",";
			}
			data.put("em_names", name);
			data.put("em_head_imgs", head_img);
			data.put("em_tels", tel);
			groupData.add(data);
		}
		employess_helper.close();
		adapter.notifyDataSetChanged();
	}
}
