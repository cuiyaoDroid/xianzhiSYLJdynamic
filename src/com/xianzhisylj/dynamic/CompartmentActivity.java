package com.xianzhisylj.dynamic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xianzhi.stool.DensityUtil;
import com.xianzhi.stool.T;
import com.xianzhi.tool.adapter.GridBedAdapter;
import com.xianzhi.tool.adapter.GridSeatListAdapter;
import com.xianzhi.tool.db.SeatInfoHelper;
import com.xianzhi.tool.db.SeatInfoHolder;
import com.xianzhi.webtool.HttpJsonTool;
import com.xianzhisecuritycheck.main.SecurityCheckApp;

public class CompartmentActivity extends Activity {
	private GridView gridview_1;
	private GridView gridview_2;
	private ArrayList<HashMap<String, Object>> grid1_Data;
	private SimpleAdapter grid1_Adapter;
	private ArrayList<HashMap<String, Object>> grid2_Data;
	private GridSeatListAdapter grid2_Adapter;
	private String[] letters = new String[] { "A", "B", "C", "D", "F" };
	private String[] letters2 = new String[] { "A", "C", "D", "F" };
	private TextView titleTxt;
	private String coach_no;
	private String trainCode;
	private String trainDate;
	private String train_type;
	private ProgressDialog progreeDialog;
	private void initProgressDialog() {
		progreeDialog = new ProgressDialog(this);
		progreeDialog.setTitle("");
		progreeDialog.setMessage("正在获得票务信息，请稍等...");
		progreeDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compartments);
		hashSet=new HashSet<String>();
		Intent intent = getIntent();
		coach_no = intent.getStringExtra("coach_no");
		trainCode = intent.getStringExtra("trainCode");
		trainDate = intent.getStringExtra("trainDate");
		train_type = intent.getStringExtra("train_type");
		initProgressDialog();
		initCount();
		grid1_Data = new ArrayList<HashMap<String, Object>>();
		grid2_Data = new ArrayList<HashMap<String, Object>>();
		initContentView();
		getSeatData();
	}
	private void initCount(){
		if(train_type.equals("YW")){
			seat_topX=3;
		}else if(train_type.equals("RW")){
			seat_topX=2;
		}else{
			seat_topX=2;
		}
		
		if(train_type.equals("RZ2")){
			seat_bottomX=3;
		}else if(train_type.equals("RZ1")){
			seat_bottomX=2;
		}else{
			seat_bottomX=3;
		}
		seat_x=seat_topX+seat_bottomX;
		if(train_type.equals("RZ")||train_type.equals("YZ")){
			seat_y=120/5;
		}else if(train_type.equals("YW")){
			seat_y=22;
		}else{
			seat_y=19;
		}
	}
	private Set<String>hashSet;
	private void getSeatData(){
		progreeDialog.show();
		hashSet.clear();
		
		AsyncTask<Void, Void, String> task =new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().getSeatInfo(getApplicationContext()
						, trainCode, trainDate, coach_no,hashSet);
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progreeDialog.dismiss();
				if(result.startsWith(HttpJsonTool.ERROR403)){
					gotoLoginView();
					return;
				}else if(result.startsWith(HttpJsonTool.ERROR)){
					T.showLong(getApplicationContext(), result.replace(HttpJsonTool.ERROR, ""));
					return;
				}else if(result.startsWith(HttpJsonTool.SUCCESS)){
//					RefreshGroupData();
					refreshCellToGrid_1();
					refreshCellToGrid_2();
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
	private void initContentView() {
		gridview_1 = (GridView) findViewById(R.id.gridview_1);
		gridview_1.setNumColumns(seat_y);
		int itemWidth = DensityUtil.dip2px(getApplicationContext(), 55)*seat_y;  
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(  
        		itemWidth, LinearLayout.LayoutParams.WRAP_CONTENT);  
        gridview_1.setLayoutParams(params);  
		gridview_2 = (GridView) findViewById(R.id.gridview_2);
		gridview_2.setNumColumns(seat_y);
		gridview_2.setLayoutParams(params); 
		titleTxt = (TextView) findViewById(R.id.title_txt);
		titleTxt.setText("编组："+coach_no);
		initGridView();
	}

	private float startX;
	private float startY;

	private void OnTouch(MotionEvent event) {
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
				if (pointX - startX > 100) {
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
//		OnTouch(event);
		return super.onTouchEvent(event);
	}

	private void initGridView() {
		if(train_type.equals("RW")||train_type.equals("YW")){
			grid1_Adapter = new GridBedAdapter(this, grid1_Data,
					R.layout.cell_compartment, new String[] { "letter" },
					new int[] { R.id.seat_num });
		}else{
			grid1_Adapter = new GridSeatListAdapter(this, grid1_Data,
					R.layout.cell_compartment, new String[] { "letter" },
					new int[] { R.id.seat_num });
		}
		gridview_1.setAdapter(grid1_Adapter);
		gridview_1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				HashMap<String, Object> map = grid1_Data.get(position);
				String letter=(String) map.get("letter");
				if ((Boolean) map.get("full")) {
					initEditTypePopupWindow(arg1, letter);
				}
			}
		});
		OnTouchListener listener = new OnTouchListener() {

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
				HashMap<String, Object> map = grid2_Data.get(position);
				String letter=(String) map.get("letter");
				if ((Boolean) map.get("full")) {
					initEditTypePopupWindow(arg1, letter);
				}
			}
		});
		gridview_2.setOnTouchListener(listener);
	}
	private int seat_topX=2;
	private int seat_bottomX=3;
	private int seat_x=seat_topX+seat_bottomX;
	private int seat_y=19;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		refreshDBData();
		refreshCellToGrid_1();
		refreshCellToGrid_2();
	}


	private void refreshCellToGrid_1() {
		grid1_Data.clear();
		for (int i = 0; i < seat_y*seat_topX; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			String letter="";
			if(train_type.equals("RZ")||train_type.equals("YZ")){
				letter=String.format("%04d", (i % seat_y)*5+(i/seat_y+1));
			}else if(train_type.equals("RZ2")){
				letter=String.format("%03d" + letters[(seat_x-1 - i / seat_y)], (i % seat_y + 1));
			}else if(train_type.equals("RZ1")){
				letter=String.format("%03d" + letters2[(seat_x-1 - i / seat_y)], (i % seat_y + 1));
			}else if(train_type.equals("YW")||train_type.equals("RW")||train_type.equals("SYW")||train_type.equals("SRW")){
				letter=String.format("%04d", (i % seat_y)*seat_topX+(i/seat_y+1));
			}else if(train_type.equals("SRZ")||train_type.equals("SYZ")){
				letter=String.format("%04d", (i % seat_y)*5+(i/seat_y+1)+1000);
			}else {
				letter=String.format("%03d" + letters[(seat_x-1 - i / seat_y)], (i % seat_y + 1));
			}
			map.put("letter",letter);
			map.put("full", hashSet.contains(letter));
			grid1_Data.add(map);
		}
		grid1_Adapter.notifyDataSetChanged();
	}

	private void refreshCellToGrid_2() {
		grid2_Data.clear();
		if(train_type.equals("YW")||train_type.equals("RW")||train_type.equals("SYW")||train_type.equals("SRW")){
			return;
		}
		for (int i = 0; i < seat_y*seat_bottomX; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			String letter="";
			if(train_type.equals("RZ")||train_type.equals("YZ")){
				letter=String.format("%04d", (i % seat_y)*5+(i/seat_y+seat_topX+1));
			}else if(train_type.equals("RZ2")){
				letter=String.format("%03d" + letters[(seat_bottomX-1 - i / seat_y)], (i % seat_y + 1));
			}else if(train_type.equals("RZ1")){
				letter=String.format("%03d" + letters2[(seat_bottomX-1 - i / seat_y)], (i % seat_y + 1));
			}else if(train_type.equals("SRZ")||train_type.equals("SYZ")){
				letter=String.format("%04d", (i % seat_y)*5+(i/seat_y+seat_topX+1)+1000);
			}else {
				letter=String.format("%03d" + letters[(seat_bottomX-1 - i / seat_y)], (i % seat_y + 1));
			}
			
			map.put("letter",letter);
			map.put("full", hashSet.contains(letter));
			grid2_Data.add(map);
		}
		grid2_Adapter.notifyDataSetChanged();
	}

	private PopupWindow EditTypePop = null;

	private void initEditTypePopupWindow(View view,
			String letter) {
		View popunwindwow = getLayoutInflater().inflate(
				R.layout.popup_passenger_info, null);
		SeatInfoHelper helper=new SeatInfoHelper(getApplicationContext());
		ArrayList<SeatInfoHolder>holders=helper.selectData_trainCode(trainCode, trainDate, coach_no, letter);
		helper.close();
		String content="";
		int i=0;
		for(SeatInfoHolder holder:holders){
			content+=holder.toString()+((i==holders.size()-1)?"":"\r\n");
			i++;
		}
		TextView content_txt = (TextView) popunwindwow.findViewById(R.id.content_txt);
		content_txt.setText(content);
		EditTypePop = new PopupWindow(popunwindwow, DensityUtil.dip2px(
				getApplicationContext(), 200), DensityUtil.dip2px(
				getApplicationContext(), 100));
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
			EditTypePop.showAsDropDown(view, 0, -view.getHeight());
		}
	}
}