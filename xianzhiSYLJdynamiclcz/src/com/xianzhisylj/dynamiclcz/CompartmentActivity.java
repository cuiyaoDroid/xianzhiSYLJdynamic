package com.xianzhisylj.dynamiclcz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import android.widget.HorizontalScrollView;
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
	private GridView gridview_3;
	private GridView gridview_4;
	private ArrayList<HashMap<String, Object>> grid1_Data;
	private SimpleAdapter grid1_Adapter;
	private ArrayList<HashMap<String, Object>> grid2_Data;
	private GridSeatListAdapter grid2_Adapter;
	private ArrayList<HashMap<String, Object>> grid3_Data;
	private SimpleAdapter grid3_Adapter;
	private ArrayList<HashMap<String, Object>> grid4_Data;
	private GridSeatListAdapter grid4_Adapter;
	
	private String[] letters = new String[] { "A", "B", "C", "D", "F" };
	private String[] letters2 = new String[] { "A", "C", "D", "F" };
	private int[] letters3 = new int[] { 1, 2, 3};
	private int[] letters4 = new int[] { 1, 3};
	private TextView titleTxt;
	private String coach_no;
	private String trainCode;
	private String trainDate;
	private String train_type;
	private int limit2;
	private ProgressDialog progreeDialog;
	private HorizontalScrollView horizontalScrollView1;
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
		limit2 = intent.getIntExtra("limit2",0);
		
		initProgressDialog();
		initCount();
		grid1_Data = new ArrayList<HashMap<String, Object>>();
		grid2_Data = new ArrayList<HashMap<String, Object>>();
		grid3_Data = new ArrayList<HashMap<String, Object>>();
		grid4_Data = new ArrayList<HashMap<String, Object>>();
		initContentView();
		getSeatData();
	}
	private void initCount(){
		if(train_type.endsWith("YW")){
			seat_topX=3;
		}else if(train_type.endsWith("RW")){
			seat_topX=2;
		}else{
			seat_topX=2;
		}
		
		if(train_type.endsWith("RZ2")){
			seat_bottomX=3;
		}else if(train_type.endsWith("RZ1")){
			seat_bottomX=2;
		}else{
			seat_bottomX=3;
		}
		seat_x=seat_topX+seat_bottomX;
		if(train_type.endsWith("RZ")||train_type.endsWith("YZ")){
			seat_y=120/5;
		}else if(train_type.endsWith("YW")){
			seat_y=22;
		}else if(train_type.endsWith("RW")){
			seat_y=36;
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
					refreshCellToGrid_3();
					refreshCellToGrid_4();
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
		horizontalScrollView1 = (HorizontalScrollView) findViewById(R.id.horizontalScrollView1);
		gridview_1 = (GridView) findViewById(R.id.gridview_1);
		gridview_1.setNumColumns(seat_y);
		
		int itemWidth = DensityUtil.dip2px(getApplicationContext(), 55)*seat_y;  
		if(train_type.endsWith("RW")){
			itemWidth = DensityUtil.dip2px(getApplicationContext(), 55)*seat_y/2;  
		}
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(  
        		itemWidth, LinearLayout.LayoutParams.WRAP_CONTENT);  
        gridview_1.setLayoutParams(params);  
		gridview_2 = (GridView) findViewById(R.id.gridview_2);
		gridview_2.setNumColumns(seat_y);
		gridview_2.setLayoutParams(params); 
		gridview_3 = (GridView) findViewById(R.id.gridview_3);
		gridview_3.setNumColumns(seat_y);
		gridview_3.setLayoutParams(params); 
		if(limit2==0){
			gridview_3.setVisibility(View.GONE);
		}
		gridview_4 = (GridView) findViewById(R.id.gridview_4);
		gridview_4.setNumColumns(seat_y);
		gridview_4.setLayoutParams(params); 
		if(limit2==0){
			gridview_4.setVisibility(View.GONE);
		}
		
		if(train_type.endsWith("RW")){
			gridview_1.setNumColumns(seat_y/2);
			gridview_3.setNumColumns(seat_y/2);
		}
		titleTxt = (TextView) findViewById(R.id.title_txt);
		titleTxt.setText("编组："+coach_no);
		initGridView();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
//		OnTouch(event);
		return super.onTouchEvent(event);
	}

	private void initGridView() {
		if(train_type.endsWith("RW")||train_type.endsWith("YW")){
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
		if(train_type.endsWith("RW")||train_type.endsWith("YW")){
			grid3_Adapter = new GridBedAdapter(this, grid3_Data,
					R.layout.cell_compartment, new String[] { "letter" },
					new int[] { R.id.seat_num });
		}else{
			grid3_Adapter = new GridSeatListAdapter(this, grid3_Data,
					R.layout.cell_compartment, new String[] { "letter" },
					new int[] { R.id.seat_num });
		}
		gridview_3.setAdapter(grid3_Adapter);
		gridview_3.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				HashMap<String, Object> map = grid3_Data.get(position);
				String letter=(String) map.get("letter");
				if ((Boolean) map.get("full")) {
					initEditTypePopupWindow(arg1, letter);
				}
			}
		});

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
		
		grid4_Adapter = new GridSeatListAdapter(this, grid4_Data,
				R.layout.cell_compartment, new String[] { "letter" },
				new int[] { R.id.seat_num });
		gridview_4.setAdapter(grid4_Adapter);
		gridview_4.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				HashMap<String, Object> map = grid4_Data.get(position);
				String letter=(String) map.get("letter");
				if ((Boolean) map.get("full")) {
					initEditTypePopupWindow(arg1, letter);
				}
			}
		});
		
		gridview_1.post(scrollViewRunable);
	}
	private int seat_topX=2;
	private int seat_bottomX=3;
	private int seat_x=seat_topX+seat_bottomX;
	private int seat_y=19;
	Runnable scrollViewRunable = new Runnable() {  
	    @Override  
	    public void run() {  
	    	horizontalScrollView1.scrollTo(0, 0);
	    }  
	  };  
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		refreshDBData();
		refreshCellToGrid_1();
		refreshCellToGrid_2();
		refreshCellToGrid_3();
		refreshCellToGrid_4();
	}


	private void refreshCellToGrid_1() {
		grid1_Data.clear();
		int start_num=0;
		if(limit2>0){
			start_num+=1000;
		}
//		Iterator<String> mIterator=hashSet.iterator();
//		if(mIterator.hasNext()){
//			start_num=1000*Integer.parseInt(mIterator.next().substring(0, 1));
//		}
			
		for (int i = 0; i < seat_y*seat_topX; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			String letter="";
			if(train_type.endsWith("RZ")||train_type.endsWith("YZ")){
				letter=String.format("%04d", (i % seat_y)*5+(i/seat_y+1)+start_num);
			}else if(train_type.endsWith("SRZ")||train_type.endsWith("SYZ")){
				letter=String.format("%04d", (i % seat_y)*5+(i/seat_y+1)+start_num);
			}else if(train_type.endsWith("RZ2")){
				letter=String.format("%03d" + letters[(seat_x-1 - i / seat_y)], (i % seat_y + 1)+start_num/10);
			}else if(train_type.endsWith("RZ1")){
				letter=String.format("%03d" + letters2[(seat_x-1 - i / seat_y)], (i % seat_y + 1)+start_num/10);
			}else if(train_type.endsWith("YW")||train_type.endsWith("SYW")){
				letter=String.format("%03d"+""+letters3[i/seat_y], (i % seat_y + 1)+start_num/10);
			}else if(train_type.endsWith("RW")||train_type.endsWith("SRW")){
				if(i/seat_y==0&&i%2==1){
					continue;
				}else if(i/seat_y==1&&i%2==0){
					continue;
				}
				letter=String.format("%03d"+""+letters4[i/seat_y], (i % seat_y + 1+start_num/10));
			}else if(train_type.endsWith("SRZ")||train_type.endsWith("SYZ")){
				letter=String.format("%04d", (i % seat_y)*5+(i/seat_y+1)+start_num);
			}else {
				letter=String.format("%04d", (i % seat_y)*5+(i/seat_y+1)+start_num);
			}
			map.put("letter",letter);
			map.put("full", hashSet.contains(letter));
			grid1_Data.add(map);
		}
		grid1_Adapter.notifyDataSetChanged();
	}

	private void refreshCellToGrid_2() {
		grid2_Data.clear();
		if(train_type.endsWith("YW")||train_type.endsWith("RW")||train_type.endsWith("SYW")||train_type.endsWith("SRW")){
			return;
		}
		int start_num=0;
		if(limit2>0){
			start_num+=1000;
		}
//		Iterator<String> mIterator=hashSet.iterator();
//		if(mIterator.hasNext()){
//			start_num=1000*Integer.parseInt(mIterator.next().substring(0, 1));
//		}
			
		for (int i = 0; i < seat_y*seat_bottomX; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			String letter="";
			if(train_type.endsWith("RZ")||train_type.endsWith("YZ")){
				letter=String.format("%04d", (i % seat_y)*5+(i/seat_y+seat_topX+1)+start_num);
			}else if(train_type.endsWith("RZ2")){
				letter=String.format("%03d" + letters[(seat_bottomX-1 - i / seat_y)], (i % seat_y + 1)+start_num/10);
			}else if(train_type.endsWith("RZ1")){
				letter=String.format("%03d" + letters2[(seat_bottomX-1 - i / seat_y)], (i % seat_y + 1)+start_num/10);
			}else if(train_type.endsWith("SRZ")||train_type.endsWith("SYZ")){
				letter=String.format("%04d", (i % seat_y)*5+(i/seat_y+seat_topX+1)+start_num);
			}else {
				letter=String.format("%04d", (i % seat_y)*5+(i/seat_y+seat_topX+1)+start_num);
			}
			
			map.put("letter",letter);
			map.put("full", hashSet.contains(letter));
			grid2_Data.add(map);
		}
		grid2_Adapter.notifyDataSetChanged();
	}
	private void refreshCellToGrid_3() {
		grid3_Data.clear();
		if(limit2==0){
			return;
		}
		int start_num=0;
//		Iterator<String> mIterator=hashSet.iterator();
//		if(mIterator.hasNext()){
//			start_num=1000*Integer.parseInt(mIterator.next().substring(0, 1))+1000;
//		}
		if(limit2>0){
			start_num+=2000;
		}
		for (int i = 0; i < seat_y*seat_topX; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			String letter="";
			if(train_type.endsWith("RZ")||train_type.endsWith("YZ")){
				letter=String.format("%04d", (i % seat_y)*5+(i/seat_y+1)+start_num);
			}else if(train_type.endsWith("SRZ")||train_type.endsWith("SYZ")){
				letter=String.format("%04d", (i % seat_y)*5+(i/seat_y+1)+start_num);
			}else if(train_type.endsWith("RZ2")){
				letter=String.format("%03d" + letters[(seat_x-1 - i / seat_y)], (i % seat_y + 1)+start_num/10);
			}else if(train_type.endsWith("RZ1")){
				letter=String.format("%03d" + letters2[(seat_x-1 - i / seat_y)], (i % seat_y + 1)+start_num/10);
			}else if(train_type.endsWith("YW")||train_type.endsWith("SYW")){
				letter=String.format("%03d"+""+letters3[i/seat_y], (i % seat_y + 1)+start_num/10);
			}else if(train_type.endsWith("RW")||train_type.endsWith("SRW")){
				if(i/seat_y==0&&i%2==1){
					continue;
				}else if(i/seat_y==1&&i%2==0){
					continue;
				}
				letter=String.format("%03d"+""+letters4[i/seat_y], (i % seat_y + 1)+start_num/10);
			}else if(train_type.endsWith("SRZ")||train_type.endsWith("SYZ")){
				letter=String.format("%04d", (i % seat_y)*5+(i/seat_y+1)+start_num);
			}else {
				letter=String.format("%04d", (i % seat_y)*5+(i/seat_y+1)+start_num);
			}
			map.put("letter",letter);
			map.put("full", hashSet.contains(letter));
			grid3_Data.add(map);
		}
		grid3_Adapter.notifyDataSetChanged();
	}
	private void refreshCellToGrid_4() {
		grid4_Data.clear();
		if(limit2==0){
			return;
		}
		if(train_type.endsWith("YW")||train_type.endsWith("RW")||train_type.endsWith("SYW")||train_type.endsWith("SRW")){
			return;
		}
		int start_num=0;
		//Iterator<String> mIterator=hashSet.iterator();
//		if(mIterator.hasNext()){
//			start_num=1000*Integer.parseInt(mIterator.next().substring(0, 1))+1000;
//		}
		if(limit2>0){
			start_num+=2000;
		}
		for (int i = 0; i < seat_y*seat_bottomX; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			String letter="";
			if(train_type.endsWith("RZ")||train_type.endsWith("YZ")){
				letter=String.format("%04d", (i % seat_y)*5+(i/seat_y+seat_topX+1)+start_num);
			}else if(train_type.endsWith("RZ2")){
				letter=String.format("%03d" + letters[(seat_bottomX-1 - i / seat_y)], (i % seat_y + 1)+start_num/10);
			}else if(train_type.endsWith("RZ1")){
				letter=String.format("%03d" + letters2[(seat_bottomX-1 - i / seat_y)], (i % seat_y + 1)+start_num/10);
			}else if(train_type.endsWith("SRZ")||train_type.endsWith("SYZ")){
				letter=String.format("%04d", (i % seat_y)*5+(i/seat_y+seat_topX+1)+start_num);
			}else {
				letter=String.format("%04d", (i % seat_y)*5+(i/seat_y+seat_topX+1)+start_num);
			}
			
			map.put("letter",letter);
			map.put("full", hashSet.contains(letter));
			grid4_Data.add(map);
		}
		grid4_Adapter.notifyDataSetChanged();
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
			content+=holder.toString()+((i==holders.size()-1)?"":"\r\n----------------------------------------\r\n");
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