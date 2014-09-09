package com.xianzhi.tool.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class EmployeesHelper extends DBHelper {
	private final static String TABLE_NAME = "Employees_order";
	public final static String ID = "id";
	public final static String CREW_GROUP_ID = "crew_group_id";
	public final static String GROUP_ID = "group_id";
	public final static String HEAD_IMG = "head_img";
	public final static String PERSONAL_CODE = "personal_code";
	public final static String PHONE_NUM = "phone_num";
	public final static String POSITION = "position";
	public final static String NAME = "name";
	public final static String TYPE = "type";
	public EmployeesHelper(Context context) {
		super(context);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "Create table IF NOT EXISTS " + TABLE_NAME + "(" 
				+ ID + " integer primary key autoincrement, " 
				+ CREW_GROUP_ID + " integer, " 
				+ GROUP_ID + " integer, " 
				+ HEAD_IMG + " VARCHAR, " 
				+ PERSONAL_CODE + " VARCHAR, " 
				+ PHONE_NUM + " VARCHAR, "
				+ POSITION+ " VARCHAR, " 
				+ NAME + " VARCHAR, "
				+ TYPE + " integer"
				+ ");";
		db.execSQL(sql);
	}

	public long insert(EmployeesHolder content) {
		synchronized (lock.Lock) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			if(content.getId()!=-1){
				cv.put(ID, content.getId());
			}
			cv.put(CREW_GROUP_ID, content.getCrew_group_id());
			cv.put(GROUP_ID, content.getGroup_id());
			cv.put(HEAD_IMG, content.getHead_img());
			cv.put(PERSONAL_CODE, content.getPersonal_code());
			cv.put(PHONE_NUM, content.getPhone_num());
			cv.put(POSITION, content.getPosition());
			cv.put(NAME, content.getName());
			cv.put(TYPE, content.getType());
			return db.replace(TABLE_NAME, null, cv);
		}
	}
	private EmployeesHolder getDataCursor(Cursor cursor){
		int id_column = cursor.getColumnIndex(ID);
		int crew_group_id_column = cursor.getColumnIndex(CREW_GROUP_ID);
		int group_id_column = cursor.getColumnIndex(GROUP_ID);
		int head_img_column = cursor.getColumnIndex(HEAD_IMG);
		int personal_code_column = cursor.getColumnIndex(PERSONAL_CODE);
		int phone_num_column = cursor.getColumnIndex(PHONE_NUM);
		int position_column = cursor.getColumnIndex(POSITION);
		int name_column = cursor.getColumnIndex(NAME);
		int type_column = cursor.getColumnIndex(TYPE);
		
		int id = cursor.getInt(id_column);
		int crew_group_id = cursor.getInt(crew_group_id_column);
		int group_id = cursor.getInt(group_id_column);
		String head_img = cursor.getString(head_img_column);
		String personal_code = cursor.getString(personal_code_column);
		String phone_num = cursor.getString(phone_num_column);
		String position = cursor.getString(position_column);
		String name = cursor.getString(name_column);
		int type = cursor.getInt(type_column);
		
		
		EmployeesHolder holder = new EmployeesHolder(id, crew_group_id, group_id, head_img
				, personal_code, phone_num, position, name, type);
		return holder;
	}
	public ArrayList<EmployeesHolder> selectData(int from,int pagesize) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null
				,null, null, null, ID + " desc limit "
						+ from + "," + pagesize);
		ArrayList<EmployeesHolder> holderlist = new ArrayList<EmployeesHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			EmployeesHolder holder=getDataCursor(cursor);
			holderlist.add(holder);
		}
		cursor.close();
		return holderlist;
	}
	public ArrayList<EmployeesHolder> selectData_CrewId(int CrewId,int type) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, CREW_GROUP_ID+"=? AND "+TYPE+"=?"
				,new String[]{String.valueOf(CrewId),String.valueOf(type)}, null, null, ID + " desc");
		ArrayList<EmployeesHolder> holderlist = new ArrayList<EmployeesHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			EmployeesHolder holder=getDataCursor(cursor);
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
	
	public EmployeesHolder selectData_Id(int v_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, ID + "=?",
				new String[] { String.valueOf(v_id) }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		EmployeesHolder holder=getDataCursor(cursor);
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
					+ GROUP_ID + " integer, " 
					+ HEAD_IMG + " VARCHAR, " 
					+ PERSONAL_CODE + " VARCHAR, " 
					+ PHONE_NUM + " VARCHAR, "
					+ POSITION+ " VARCHAR, " 
					+ NAME + " VARCHAR, "
					+ TYPE + " integer"
					+ ");";

			db.execSQL(sql);
		}
	}
}
