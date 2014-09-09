package com.xianzhisylj.dynamic;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.xianzhi.stool.DensityUtil;
import com.xianzhi.tool.adapter.GridSeatListAdapter;
import com.xianzhi.tool.db.PassengerHelper;
import com.xianzhi.tool.db.PassengerHolder;

public class CompartmentActivity extends Activity {
	private GridView gridview_1;
	private GridView gridview_2;
	private ArrayList<HashMap<String, Object>> grid1_Data;
	private GridSeatListAdapter grid1_Adapter;
	private ArrayList<HashMap<String, Object>> grid2_Data;
	private GridSeatListAdapter grid2_Adapter;
	private String[] letters = new String[] { "A", "B", "C", "D", "E" };

	private TextView titleTxt;
	private int id;
	private int dynamic_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compartments);
		grid1_Data = new ArrayList<HashMap<String, Object>>();
		grid2_Data = new ArrayList<HashMap<String, Object>>();
		initContentView();
	}

	private void initContentView() {
		gridview_1 = (GridView) findViewById(R.id.gridview_1);
		gridview_2 = (GridView) findViewById(R.id.gridview_2);
		titleTxt = (TextView) findViewById(R.id.title_txt);
		Intent intent = getIntent();
		id = intent.getIntExtra("id", -1);
		dynamic_id=intent.getIntExtra("dynamic_id", -1);
		titleTxt.setText(intent.getStringExtra("name") + "车厢\u3000乘务员 "
				+ intent.getStringExtra("em_names").replaceAll(",", " "));
		initGridView();
	}
	private float startX;
	private float startY;
	private void OnTouch(MotionEvent event){
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
				if (pointX-startX > 100) {
					finish();
				}
			}
		default:
			break;
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		OnTouch(event);
		return super.onTouchEvent(event);
	}
	private void initGridView() {
		grid1_Adapter = new GridSeatListAdapter(this, grid1_Data,
				R.layout.cell_compartment, new String[] { "letter" },
				new int[] { R.id.seat_num });
		gridview_1.setAdapter(grid1_Adapter);
		gridview_1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				HashMap<String,Object>map=grid1_Data.get(position);
				if((Boolean)map.get("full")){
					initEditTypePopupWindow(arg1,map);
				}
			}
		});
		OnTouchListener listener=new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				OnTouch(event);
				return false;
			}
		};
		gridview_1.setOnTouchListener(listener);
		
		grid2_Adapter = new GridSeatListAdapter(this, grid2_Data,
				R.layout.cell_compartment, new String[] { "letter" },
				new int[] { R.id.seat_num });
		gridview_2.setAdapter(grid2_Adapter);
		gridview_2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				HashMap<String,Object>map=grid2_Data.get(position);
				if((Boolean)map.get("full")){
					initEditTypePopupWindow(arg1,map);
				}
			}
		});
		gridview_2.setOnTouchListener(listener);
	}
	String[][] seat=new String[5][12];
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(id==-1||dynamic_id==-1){
			return;
		}
		refreshDBData();
		refreshCellToGrid_1();
		refreshCellToGrid_2();
	}
	private void refreshDBData(){
		PassengerHelper helper=new PassengerHelper(getApplicationContext());
		ArrayList<PassengerHolder> holders=helper.selectDataGroup(dynamic_id, id);
		for(PassengerHolder holder:holders){
			String[] seats=holder.getSeat().split(",");
			if(seats.length<2){
				continue;
			}
			if(Integer.parseInt(seats[0])>4||Integer.parseInt(seats[1])>11){
				continue;
			}
			seat[Integer.parseInt(seats[0])][Integer.parseInt(seats[1])]=holder.getName()+","
					+holder.getPersonal_code()+","+holder.getStart_station()
					+","+holder.getArrive_station()+","+holder.getTick_price();
		}
		helper.close();
	}
	private void refreshCellToGrid_1() {
		grid1_Data.clear();
		for (int i = 0; i < 36; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("letter",
					String.format("%02d" + letters[(4 - i / 12)], (i % 12 + 1)));
			String str_seat=seat[4 - i / 12][i % 12];
			if(str_seat!=null){
				String[] arr_seat=str_seat.split(",");
				map.put("full", true);
				if(arr_seat.length<5){
					map.put("full", false);
					continue;
				}
				map.put("name", arr_seat[0]);
				map.put("personal_code", arr_seat[1]);
				map.put("start_station", arr_seat[2]);
				map.put("arrive_station", arr_seat[3]);
				map.put("tick_price", arr_seat[4]);
			}else{
				map.put("full", false);
			}
			grid1_Data.add(map);
		}
		grid1_Adapter.notifyDataSetChanged();
	}

	private void refreshCellToGrid_2() {
		grid2_Data.clear();
		for (int i = 0; i < 24; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("letter",
					String.format("%02d" + letters[(1 - i / 12)], (i % 12 + 1)));
			String str_seat=seat[1 - i / 12][i % 12];
			if(str_seat!=null){
				String[] arr_seat=str_seat.split(",");
				map.put("full", true);
				if(arr_seat.length<5){
					map.put("full", false);
					continue;
				}
				map.put("name", arr_seat[0]);
				map.put("personal_code", arr_seat[1]);
				map.put("start_station", arr_seat[2]);
				map.put("arrive_station", arr_seat[3]);
				map.put("tick_price", arr_seat[4]);
			}else{
				map.put("full", false);
			}
			
			grid2_Data.add(map);
		}
		grid2_Adapter.notifyDataSetChanged();
	}
	
	
	private PopupWindow EditTypePop = null;

	private void initEditTypePopupWindow(View view, HashMap<String, Object> hashMap) {
		View popunwindwow = getLayoutInflater().inflate(
				R.layout.popup_passenger_info, null);
		TextView name_txt=(TextView)popunwindwow.findViewById(R.id.name_txt);
		TextView personal_code_txt=(TextView)popunwindwow.findViewById(R.id.personal_code_txt);
		TextView start_station_txt=(TextView)popunwindwow.findViewById(R.id.start_station_txt);
		TextView arrive_station_txt=(TextView)popunwindwow.findViewById(R.id.arrive_station_txt);
		TextView tick_price_txt=(TextView)popunwindwow.findViewById(R.id.tick_price_txt);
		name_txt.setText("姓名: "+(String) hashMap.get("name"));
		Log.i("personal_code", (String) hashMap.get("personal_code"));
		personal_code_txt.setText("身份证: "+(String) hashMap.get("personal_code"));
		start_station_txt.setText("始发站: "+(String) hashMap.get("start_station"));
		arrive_station_txt.setText("终点站: "+(String) hashMap.get("arrive_station"));
		tick_price_txt.setText("票价: "+(String) hashMap.get("tick_price"));
		
		EditTypePop = new PopupWindow(popunwindwow, DensityUtil.dip2px(getApplicationContext(), 200),
				DensityUtil.dip2px(getApplicationContext(), 100));
		EditTypePop.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
			}
		});
		EditTypePop.setFocusable(true);
		EditTypePop.setAnimationStyle(R.style.AnimationFade);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		EditTypePop.setBackgroundDrawable(dw);
		EditTypePop.setOutsideTouchable(true);
		showPopWindow(view);
	}

	private void showPopWindow(View view) {
		if (EditTypePop == null) {
			return;
		}
		if (!EditTypePop.isShowing()) {
			EditTypePop.showAsDropDown(view,0,-view.getHeight());
		}
	}
}