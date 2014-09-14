package com.xianzhi.tool.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TrainDynamicHelper extends DBHelper {
	private final static String TABLE_NAME = "train_dynamic_order";
	public final static String ID = "_id";
	public final static String TRAIN_ID = "train_id";
	public final static String STATION_NAME = "STATION_NAME";
	public final static String STATION_NO = "STATION_NO";
	public final static String TIME_ARRIVAL = "TIME_ARRIVAL";
	public final static String TIME_ARRIVAL_TD = "TIME_ARRIVAL_TD";
	public final static String TIME_DEPART = "TIME_DEPART";
	public final static String TIME_DEPART_TD = "TIME_DEPART_TD";
	public final static String CUR = "CUR";
	public final static String ON_TIME = "ON_TIME";
	public final static String PASSED = "PASSED";
	public final static String PRE_PASSED = "PRE_PASSED";
	public final static String NEXT_PASSED = "NEXT_PASSED";
	
	
	public TrainDynamicHelper(Context context) {
		super(context);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		db.execSQL(getCreateSql());
	}

	private String getCreateSql() {
		String sql = "Create table IF NOT EXISTS " + TABLE_NAME + "(" + ID
				+ " integer primary key autoincrement, "
				+ TRAIN_ID + " integer, "
				+ STATION_NAME + " VARCHAR, " 
				+ STATION_NO + " VARCHAR, "
				+ TIME_ARRIVAL + " VARCHAR, "
				+ TIME_ARRIVAL_TD + " VARCHAR, "
				+ TIME_DEPART + " VARCHAR, "
				+ TIME_DEPART_TD + " VARCHAR, "
				+ CUR + " integer, "
				+ ON_TIME + " integer, "
				+ PASSED + " integer, "
				+ PRE_PASSED + " integer, "
				+ NEXT_PASSED + " integer"
				+ ");";
		return sql;
	}

	public long insert(TrainDynamicHolder content, SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		if(content.getId()!=-1){
			cv.put(ID, content.getId());
		}
		cv.put(TRAIN_ID, content.getTrain_id());
		cv.put(STATION_NAME, content.getSTATION_NAME());
		cv.put(STATION_NO, content.getSTATION_NO());
		cv.put(TIME_ARRIVAL, content.getTIME_ARRIVAL());
		cv.put(TIME_ARRIVAL_TD, content.getTIME_ARRIVAL_TD());
		cv.put(TIME_DEPART, content.getTIME_DEPART());
		cv.put(TIME_DEPART_TD, content.getTIME_DEPART_TD());
		cv.put(CUR, content.getCUR());
		cv.put(ON_TIME, content.getON_TIME());
		cv.put(PASSED, content.getPASSED());
		cv.put(NEXT_PASSED, content.getNEXT_PASSED());
		cv.put(PRE_PASSED, content.getPRE_PASSED());
		return db.replace(TABLE_NAME, null, cv);
	}

	private TrainDynamicHolder getDataCursor(Cursor cursor) {
		int ID_column = cursor.getColumnIndex(ID);
		int TRAIN_ID_column = cursor.getColumnIndex(TRAIN_ID);
		int STATION_NAME_column = cursor.getColumnIndex(STATION_NAME);
		int STATION_NO_column = cursor.getColumnIndex(STATION_NO);
		int TIME_ARRIVAL_column = cursor.getColumnIndex(TIME_ARRIVAL);
		int TIME_ARRIVAL_TD_column = cursor.getColumnIndex(TIME_ARRIVAL_TD);
		int TIME_DEPART_column = cursor.getColumnIndex(TIME_DEPART);
		int TIME_DEPART_TD_column = cursor.getColumnIndex(TIME_DEPART_TD);
		int CUR_column = cursor.getColumnIndex(CUR);
		int ON_TIME_column = cursor.getColumnIndex(ON_TIME);
		int PASSED_column = cursor.getColumnIndex(PASSED);
		int NEXT_PASSED_column = cursor.getColumnIndex(NEXT_PASSED);
		int PRE_PASSED_column = cursor.getColumnIndex(PRE_PASSED);

		int id = cursor.getInt(ID_column);
		int train_id = cursor.getInt(TRAIN_ID_column);
		String STATION_NAME = cursor.getString(STATION_NAME_column);
		String STATION_NO = cursor.getString(STATION_NO_column);
		String TIME_ARRIVAL = cursor.getString(TIME_ARRIVAL_column);
		String TIME_ARRIVAL_TD = cursor.getString(TIME_ARRIVAL_TD_column);
		String TIME_DEPART = cursor.getString(TIME_DEPART_column);
		String TIME_DEPART_TD = cursor.getString(TIME_DEPART_TD_column);
		int CUR = cursor.getInt(CUR_column);
		int ON_TIME = cursor.getInt(ON_TIME_column);
		int PASSED = cursor.getInt(PASSED_column);
		int NEXT_PASSED = cursor.getInt(NEXT_PASSED_column);
		int PRE_PASSED = cursor.getInt(PRE_PASSED_column);

		TrainDynamicHolder holder = new TrainDynamicHolder(id,train_id, STATION_NAME
				, STATION_NO, TIME_ARRIVAL, TIME_ARRIVAL_TD, TIME_DEPART
				, TIME_DEPART_TD, CUR, ON_TIME, PASSED, PRE_PASSED, NEXT_PASSED);

		return holder;
	}

	public int delete_id(int id) {
		synchronized (lock.Lock) {
			SQLiteDatabase db = getWritableDatabase();
			return db.delete(TABLE_NAME, ID + "=" + id, null);
		}
	}

	public int delete_train_id(int train_id) {
		synchronized (lock.Lock) {
			SQLiteDatabase db = getWritableDatabase();
			return db.delete(TABLE_NAME, TRAIN_ID + "=" + train_id, null);
		}
	}

	public TrainDynamicHolder selectData_Id(int v_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, ID + "=?",
				new String[] { String.valueOf(v_id) }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		TrainDynamicHolder holder = getDataCursor(cursor);
		cursor.close();
		return holder;
	}
	public ArrayList<TrainDynamicHolder> selectDataTrainId(int trainId) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, TRAIN_ID + "=" + trainId, null, null, null, ID
				+ " desc");
		ArrayList<TrainDynamicHolder> holderlist = new ArrayList<TrainDynamicHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			TrainDynamicHolder holder = getDataCursor(cursor);
			holderlist.add(holder);
		}
		cursor.close();
		return holderlist;
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
