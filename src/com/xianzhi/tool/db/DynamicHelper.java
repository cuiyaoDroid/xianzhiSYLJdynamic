package com.xianzhi.tool.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DynamicHelper extends DBHelper {
	private final static String TABLE_NAME = "Dynamic_order";
	public final static String ID = "id";
	public final static String TRAIN_NUMBER = "train_number";
	public final static String START_TIME = "start_time";
	public final static String ARRIVE_TIME = "arrive_time";
	public final static String START_POSITION = "start_position";
	public final static String ARRIVE_POSITION = "arrive_position";
	public final static String WATCH_GROUP = "watch_group";
	public final static String INTERVAL_POSITION = "Interval_position";
	public final static String STATION = "station";
	public final static String WATCH_GROUP_ID = "watch_group_id";
	public DynamicHelper(Context context) {
		super(context);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		db.execSQL(getCreateSql());
	}
	private String getCreateSql(){
		String sql = "Create table IF NOT EXISTS " + TABLE_NAME + "(" 
				+ ID + " integer primary key autoincrement, " 
				+ TRAIN_NUMBER + " VARCHAR, " 
				+ START_TIME + " LONG, " 
				+ ARRIVE_TIME + " LONG, " 
				+ START_POSITION + " VARCHAR, " 
				+ ARRIVE_POSITION + " VARCHAR, "
				+ WATCH_GROUP+ " VARCHAR, " 
				+ INTERVAL_POSITION + " VARCHAR, "
				+ WATCH_GROUP_ID + " integer, "
				+ STATION + " VARCHAR" 
				+ ");";
		return sql;
	}
	public long insert(DynamicHolder content) {
		synchronized (lock.Lock) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			if(content.getId()!=-1){
				cv.put(ID, content.getId());
			}
			cv.put(TRAIN_NUMBER, content.getTrain_number());
			cv.put(START_TIME, content.getStart_time());
			cv.put(ARRIVE_TIME, content.getArrive_time());
			cv.put(START_POSITION, content.getStart_position());
			cv.put(ARRIVE_POSITION, content.getArrive_position());
			cv.put(WATCH_GROUP, content.getWatch_group());
			cv.put(INTERVAL_POSITION, content.getInterval_position());
			cv.put(WATCH_GROUP_ID, content.getWatch_group_id());
			cv.put(STATION, content.getStation());
			return db.replace(TABLE_NAME, null, cv);
		}
	}
	private DynamicHolder getDataCursor(Cursor cursor){
		int id_column = cursor.getColumnIndex(ID);
		int train_number_column = cursor.getColumnIndex(TRAIN_NUMBER);
		int start_time_column = cursor.getColumnIndex(START_TIME);
		int arrive_time_column = cursor.getColumnIndex(ARRIVE_TIME);
		int start_position_column = cursor.getColumnIndex(START_POSITION);
		int arrive_position_column = cursor.getColumnIndex(ARRIVE_POSITION);
		int watch_group_column = cursor.getColumnIndex(WATCH_GROUP);
		int interval_position_column = cursor.getColumnIndex(INTERVAL_POSITION);
		int station_column = cursor.getColumnIndex(STATION);
		int watch_group_id_column = cursor.getColumnIndex(WATCH_GROUP_ID);
		
		int id = cursor.getInt(id_column);
		String train_number = cursor.getString(train_number_column);
		long start_time = cursor.getLong(start_time_column);
		long arrive_time = cursor.getLong(arrive_time_column);
		String start_position = cursor.getString(start_position_column);
		String arrive_position = cursor.getString(arrive_position_column);
		String watch_group = cursor.getString(watch_group_column);
		String interval_position = cursor.getString(interval_position_column);
		String station = cursor.getString(station_column);
		int watch_group_id = cursor.getInt(watch_group_id_column);
		DynamicHolder holder = new DynamicHolder(id, train_number, start_time, arrive_time, start_position
				, arrive_position, watch_group, interval_position, station,watch_group_id);
		return holder;
	}
	public ArrayList<DynamicHolder> selectData(int from,int pagesize) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null
				,null, null, null, ARRIVE_TIME + " desc limit "
						+ from + "," + pagesize);
		ArrayList<DynamicHolder> holderlist = new ArrayList<DynamicHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			DynamicHolder holder=getDataCursor(cursor);
			holderlist.add(holder);
		}
		cursor.close();
		return holderlist;
	}
	public ArrayList<DynamicHolder> selectSearchData(String num) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, TRAIN_NUMBER
				+ " like '%" + num + "%'"
				,null, null, null, ARRIVE_TIME + " desc");
		ArrayList<DynamicHolder> holderlist = new ArrayList<DynamicHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			DynamicHolder holder=getDataCursor(cursor);
			holderlist.add(holder);
		}
		cursor.close();
		return holderlist;
	}
	public int delete_id(int id){
		synchronized (lock.Lock) {
			SQLiteDatabase db = getWritableDatabase();
			return db.delete(TABLE_NAME,  ID + "=" + id, null);
		}
	}
	
	public DynamicHolder selectData_Id(int v_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, ID + "=?",
				new String[] { String.valueOf(v_id) }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		DynamicHolder holder=getDataCursor(cursor);
		cursor.close();
		return holder;
	}
	public void clear() {
		synchronized (lock.Lock) {
			SQLiteDatabase db = this.getWritableDatabase();
			String dropsql = " DROP TABLE IF EXISTS " + TABLE_NAME;
			db.execSQL(dropsql);
			db.execSQL(getCreateSql());
		}
	}
}
