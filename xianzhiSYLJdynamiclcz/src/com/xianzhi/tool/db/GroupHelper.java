package com.xianzhi.tool.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GroupHelper extends DBHelper {
	private final static String TABLE_NAME = "Group_order";
	public final static String ID = "id";
	public final static String CREW_GROUP_ID = "crew_group_id";
	public final static String TRAIN_CODE = "train_code";
	public final static String PASSENGER_NUM = "passenger_num";
	public final static String NAME = "name";
	public final static String TRAIN_TYPE = "train_type";
	public final static String CREW_EM_IDS = "crew_em_ids";
	public GroupHelper(Context context) {
		super(context);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "Create table IF NOT EXISTS " + TABLE_NAME + "(" 
				+ ID + " integer primary key autoincrement, " 
				+ CREW_GROUP_ID + " integer, " 
				+ TRAIN_CODE + " VARCHAR, " 
				+ PASSENGER_NUM + " VARCHAR, " 
				+ TRAIN_TYPE + " VARCHAR, " 
				+ CREW_EM_IDS + " VARCHAR, " 
				+ NAME + " VARCHAR"
				+ ");";
		db.execSQL(sql);
	}

	public long insert(GroupHolder content) {
		synchronized (lock.Lock) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			if(content.getId()!=-1){
				cv.put(ID, content.getId());
			}
			cv.put(CREW_GROUP_ID, content.getCrew_group_id());
			cv.put(TRAIN_CODE, content.getTrain_code());
			cv.put(PASSENGER_NUM, content.getPassenger_num());
			cv.put(NAME, content.getName());
			cv.put(TRAIN_TYPE, content.getTrain_type());
			cv.put(CREW_EM_IDS, content.getCrew_em_ids());
			return db.replace(TABLE_NAME, null, cv);
		}
	}
	private GroupHolder getDataCursor(Cursor cursor){
		int id_column = cursor.getColumnIndex(ID);
		int crew_group_id_column = cursor.getColumnIndex(CREW_GROUP_ID);
		int train_code_column = cursor.getColumnIndex(TRAIN_CODE);
		int passenger_num_column = cursor.getColumnIndex(PASSENGER_NUM);
		int name_column = cursor.getColumnIndex(NAME);
		int train_type_column = cursor.getColumnIndex(TRAIN_TYPE);
		int crew_em_ids_column = cursor.getColumnIndex(CREW_EM_IDS);
		
		int id = cursor.getInt(id_column);
		int crew_group_id = cursor.getInt(crew_group_id_column);
		String train_code = cursor.getString(train_code_column);
		String passenger_num = cursor.getString(passenger_num_column);
		String name = cursor.getString(name_column);
		String train_type = cursor.getString(train_type_column);
		String crew_em_ids = cursor.getString(crew_em_ids_column);
		
		GroupHolder holder = new GroupHolder(id, crew_group_id, train_code, passenger_num
				, name, train_type,crew_em_ids);
		return holder;
	}
	public ArrayList<GroupHolder> selectData(int v_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, CREW_GROUP_ID + "=?"
				,new String[] { String.valueOf(v_id) }, null, null, ID + " desc");
		ArrayList<GroupHolder> holderlist = new ArrayList<GroupHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			GroupHolder holder=getDataCursor(cursor);
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
	
	public GroupHolder selectData_Id(int v_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, ID + "=?",
				new String[] { String.valueOf(v_id) }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		GroupHolder holder=getDataCursor(cursor);
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
					+ CREW_GROUP_ID + " integer, " 
					+ TRAIN_CODE + " VARCHAR, " 
					+ PASSENGER_NUM + " VARCHAR, " 
					+ TRAIN_TYPE + " VARCHAR, " 
					+ CREW_EM_IDS + " VARCHAR, " 
					+ NAME + " VARCHAR"
					+ ");";

			db.execSQL(sql);
		}
	}
}
