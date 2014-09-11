package com.xianzhisylj.dynamic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.xianzhi.tool.adapter.GridListAdapter;
import com.xianzhi.tool.db.DynamicHelper;
import com.xianzhi.tool.db.DynamicHolder;
import com.xianzhi.tool.db.DynamicListHelper;
import com.xianzhi.tool.db.DynamicListHolder;
import com.xianzhi.tool.db.EmployeesHelper;
import com.xianzhi.tool.db.EmployeesHolder;
import com.xianzhi.tool.view.MyGridView;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		grid1_Data = new ArrayList<HashMap<String, Object>>();
		grid2_Data = new ArrayList<HashMap<String, Object>>();
		grid3_Data = new ArrayList<HashMap<String, Object>>();
		initContentView();
	}

	@SuppressLint("SimpleDateFormat")
	private void initContentView() {
		setContentView(R.layout.activity_train_info);
		detailTitleTxt = (TextView) findViewById(R.id.detail_title_txt);
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
		DynamicListHolder holder = helper.selectData_Id(train_id);
		helper.close();
		// crew_id = holder.getWatch_group_id();
		String str_checker = "<font color=#006AE2>"
				+ holder.getBoard_train_code() + "</font>" + "\u3000"
				+ holder.getFrom_station_name() + "━━"
				+ holder.getTo_station_name() + " | 发车时间:"
				+ format.format(holder.getStart_time()) + "\u3000当值班组:"
				+ holder.getCurrent_team();
		detailTitleTxt.setText(Html.fromHtml(str_checker));
		initGridView();
		refreshCellToGrid_1(holder);
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
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num));
		startActivity(intent);
	}

	private void refreshCellToGrid_1(DynamicListHolder holder) {
		grid1_Data.clear();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name",
				holder.getPosition_name() + " <font color=#006AE2>"
						+ holder.getUser_name() + "</font>");
		map.put("tel", holder.getPhone());
		map.put("img", holder.getPhoto());
		grid1_Data.add(map);
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
