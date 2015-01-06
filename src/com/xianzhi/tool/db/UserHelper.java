package com.xianzhi.tool.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserHelper extends DBHelper {
	private final static String TABLE_NAME = "Duser_list_order";
	public final static String ID = "_id";
	public final static String job_number = "job_number";
	public final static String user_name = "user_name";
	public final static String photo = "photo";
	public final static String phone = "phone";
	public final static String department_id = "department_id";
	public final static String department_name = "department_name";
	public final static String position_id = "position_id";
	public final static String position_name = "position_name";
	public UserHelper(Context context) {
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
				+ job_number + " VARCHAR, " 
				+ user_name + " VARCHAR, " 
				+ photo + " VARCHAR, " 
				+ phone + " VARCHAR, "
				+ department_id + " integer, " 
				+ department_name + " VARCHAR, "
				+ position_id + " integer, " 
				+ position_name + " VARCHAR);";
		return sql;
	}
	public long insert(UserHolder content, SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		if (content.getId() != -1) {
			cv.put(ID, content.getId());
		}
		cv.put(job_number, content.getJob_number());
		cv.put(user_name, content.getUser_name());
		cv.put(photo, content.getPhoto());
		cv.put(phone, content.getPhone());
		cv.put(department_id, content.getDepartment_id());
		cv.put(department_name, content.getDepartment_name());
		cv.put(position_id, content.getPosition_id());
		cv.put(position_name, content.getPosition_name());
		return db.replace(TABLE_NAME, null, cv);
	}

	private UserHolder getDataCursor(Cursor cursor) {
		int id_column = cursor.getColumnIndex(ID);
		int job_number_column = cursor.getColumnIndex(job_number);
		int user_name_column = cursor.getColumnIndex(user_name);
		int photo_column = cursor.getColumnIndex(photo);
		int phone_column = cursor.getColumnIndex(phone);
		int department_id_column = cursor.getColumnIndex(department_id);
		int department_name_column = cursor.getColumnIndex(department_name);
		int position_id_column = cursor.getColumnIndex(position_id);
		int position_name_column = cursor.getColumnIndex(position_name);

		int id = cursor.getInt(id_column);
		String job_number = cursor.getString(job_number_column);
		String user_name = cursor.getString(user_name_column);
		String photo = cursor.getString(photo_column);
		String phone = cursor.getString(phone_column);
		int department_id = cursor.getInt(department_id_column);
		String department_name = cursor.getString(department_name_column);
		int position_id = cursor.getInt(position_id_column);
		String position_name = cursor.getString(position_name_column);
		UserHolder holder = new UserHolder(id, job_number, user_name, photo, phone, department_id, department_name, position_id, position_name);
		return holder;
	}
	public int delete_id(int id) {
		synchronized (lock.Lock) {
			SQLiteDatabase db = getWritableDatabase();
			return db.delete(TABLE_NAME, ID + "=" + id, null);
		}
	}
	public UserHolder selectData_Id(int v_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, ID + "=?",
				new String[] { String.valueOf(v_id) }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		UserHolder holder = getDataCursor(cursor);
		cursor.close();
		return holder;
	}
	public void clear() {
		synchronized (lock.Lock) {
			SQLiteDatabase db = this.getReadableDatabase();
			String dropsql = " DROP TABLE IF EXISTS " + TABLE_NAME;
			db.execSQL(dropsql);
			db.execSQL(getCreateSql());
		}
	}
}
