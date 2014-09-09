package com.xianzhi.tool.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TrainHelper extends DBHelper {
	public final static String ID = "id";
	private final static String TABLE_NAME = "Train_order";
	public final static String PLACE_GROUP = "place_group";
	public final static String TIME_GROUP = "time_group";
	public final static String NAME = "name";
	public final static String HOLE_TIME = "hole_time";
	public final static String TYPE = "type";
	public TrainHelper(Context context) {
		super(context);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "Create table IF NOT EXISTS " + TABLE_NAME + "(" 
				+ ID + " integer primary key autoincrement, " 
				+ NAME + " VARCHAR, " 
				+ PLACE_GROUP + " VARCHAR, " 
				+ TIME_GROUP + " VARCHAR, " 
				+ HOLE_TIME + " VARCHAR, " 
				+ TYPE + " VARCHAR" 
				+ ");";
		db.execSQL(sql);
	}

	public long insert(TrainHolder content) {
		synchronized (lock.Lock) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put(PLACE_GROUP, content.getPlace_group());
			cv.put(TIME_GROUP, content.getTime_group());
			cv.put(NAME, content.getName());
			cv.put(HOLE_TIME, content.getHole_time());
			cv.put(TYPE, content.getType());
			return db.replace(TABLE_NAME, null, cv);
		}
	}
	private TrainHolder getDataCursor(Cursor cursor){
		int place_group_column = cursor.getColumnIndex(PLACE_GROUP);
		int time_group_column = cursor.getColumnIndex(TIME_GROUP);
		int name_column = cursor.getColumnIndex(NAME);
		int hole_time_column = cursor.getColumnIndex(HOLE_TIME);
		int type_column = cursor.getColumnIndex(TYPE);
		
		String place_group = cursor.getString(place_group_column);
		String time_group = cursor.getString(time_group_column);
		String name = cursor.getString(name_column);
		String hole_time = cursor.getString(hole_time_column);
		String type = cursor.getString(type_column);
		
		TrainHolder holder = new TrainHolder(place_group, time_group, name, hole_time, type);
		return holder;
	}
	public ArrayList<TrainHolder> selectTrain(String name) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, NAME+"=?",new String[]{name}, null, null, null);
		ArrayList<TrainHolder> holderlist = new ArrayList<TrainHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			TrainHolder holder=getDataCursor(cursor);
			holderlist.add(holder);
		}
		cursor.close();
		return holderlist;
	}
	public int delete_id(String id){
		synchronized (lock.Lock) {
			SQLiteDatabase db = getWritableDatabase();
			return db.delete(TABLE_NAME,  NAME + "=" + id, null);
		}
	}
	
	public TrainHolder selectData_Name(String name) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, NAME + "=?",
				new String[] { name }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		TrainHolder holder=getDataCursor(cursor);
		cursor.close();
		return holder;
	}
	public void clear() {
		synchronized (lock.Lock) {
			SQLiteDatabase db = this.getReadableDatabase();
			String dropsql = " DROP TABLE IF EXISTS " + TABLE_NAME;
			db.execSQL(dropsql);
			String sql = "Create table IF NOT EXISTS " + TABLE_NAME + "(" 
					+ ID + " integer primary key autoincrement, " 
					+ NAME + " VARCHAR, " 
					+ PLACE_GROUP + " VARCHAR, " 
					+ TIME_GROUP + " VARCHAR, " 
					+ HOLE_TIME + " VARCHAR, " 
					+ TYPE + " VARCHAR" 
					+ ");";

			db.execSQL(sql);
		}
	}
}
