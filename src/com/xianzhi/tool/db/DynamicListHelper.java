package com.xianzhi.tool.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DynamicListHelper extends DBHelper {
	private final static String TABLE_NAME = "Dynamic_list_order";
	public final static String ID = "_id";
	public final static String board_train_code = "board_train_code";
	public final static String start_time = "start_time";
	public final static String from_station_name = "from_station_name";
	public final static String to_station_name = "to_station_name";
	public final static String current_team = "current_team";
	public final static String driving_status = "driving_status";
	public final static String user_id = "user_id";
	public final static String job_number = "job_number";
	public final static String user_name = "user_name";
	public final static String photo = "photo";
	public final static String phone = "phone";
	public final static String department_id = "department_id";
	public final static String department_name = "department_name";
	public final static String position_id = "position_id";
	public final static String position_name = "position_name";
	public final static String team_length = "team_length";
	public final static String isFinal = "isFinal";
	public final static String isRead = "isRead";
	public final static String date = "date";
	public DynamicListHelper(Context context) {
		super(context);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		db.execSQL(getCreateSql());
	}

	private String getCreateSql() {
		String sql = "Create table IF NOT EXISTS " + TABLE_NAME + "(" + ID
				+ " integer primary key autoincrement, " + board_train_code
				+ " VARCHAR, " + start_time + " LONG, " + from_station_name
				+ " VARCHAR, " + to_station_name + " VARCHAR, " + current_team
				+ " VARCHAR, " + driving_status + " integer, " + user_id
				+ " VARCHAR, " + job_number + " VARCHAR, " + user_name
				+ " VARCHAR, " + photo + " VARCHAR, " + phone + " VARCHAR, "
				+ department_id + " integer, " + department_name + " VARCHAR, "
				+ position_id + " integer, " 
				+ date + " VARCHAR, " 
				+ position_name + " VARCHAR, "
				+ isFinal + " integer, "
				+ isRead + " integer, "
				+ team_length + " VARCHAR" + ");";
		return sql;
	}

	public long insert(DynamicListHolder content, SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		int read=0;
		if (content.getId() != -1) {
			cv.put(ID, content.getId());
			read=getRead(content.getId());
		}
		cv.put(board_train_code, content.getBoard_train_code());
		cv.put(start_time, content.getStart_time());
		cv.put(from_station_name, content.getFrom_station_name());
		cv.put(to_station_name, content.getTo_station_name());
		cv.put(current_team, content.getCurrent_team());
		cv.put(driving_status, content.getDriving_status());
		cv.put(user_id, content.getUser_ids());
		cv.put(job_number, content.getJob_number());
		cv.put(user_name, content.getUser_name());
		cv.put(photo, content.getPhoto());
		cv.put(phone, content.getPhone());
		cv.put(department_id, content.getDepartment_id());
		cv.put(department_name, content.getDepartment_name());
		cv.put(position_id, content.getPosition_id());
		cv.put(position_name, content.getPosition_name());
		cv.put(isFinal, content.getIsFinal());
		cv.put(isRead, read);
		cv.put(date, content.getDate());
		return db.replace(TABLE_NAME, null, cv);
	}

	private DynamicListHolder getDataCursor(Cursor cursor) {
		int id_column = cursor.getColumnIndex(ID);
		int board_train_code_column = cursor.getColumnIndex(board_train_code);
		int start_time_column = cursor.getColumnIndex(start_time);
		int from_station_name_column = cursor.getColumnIndex(from_station_name);
		int to_station_name_column = cursor.getColumnIndex(to_station_name);
		int current_team_column = cursor.getColumnIndex(current_team);
		int driving_status_column = cursor.getColumnIndex(driving_status);
		int user_ids_column = cursor.getColumnIndex(user_id);
		int job_number_column = cursor.getColumnIndex(job_number);
		int user_name_column = cursor.getColumnIndex(user_name);
		int photo_column = cursor.getColumnIndex(photo);
		int phone_column = cursor.getColumnIndex(phone);
		int department_id_column = cursor.getColumnIndex(department_id);
		int department_name_column = cursor.getColumnIndex(department_name);
		int position_id_column = cursor.getColumnIndex(position_id);
		int position_name_column = cursor.getColumnIndex(position_name);
		int isFinal_column = cursor.getColumnIndex(isFinal);
		int isRead_column = cursor.getColumnIndex(isRead);
		int date_column = cursor.getColumnIndex(date);

		int id = cursor.getInt(id_column);
		String board_train_code = cursor.getString(board_train_code_column);
		long start_time = cursor.getLong(start_time_column);
		String from_station_name = cursor.getString(from_station_name_column);
		String to_station_name = cursor.getString(to_station_name_column);
		String current_team = cursor.getString(current_team_column);
		int driving_status = cursor.getInt(driving_status_column);
		String user_ids = cursor.getString(user_ids_column);
		String job_number = cursor.getString(job_number_column);
		String user_name = cursor.getString(user_name_column);
		String photo = cursor.getString(photo_column);
		String phone = cursor.getString(phone_column);
		int department_id = cursor.getInt(department_id_column);
		String department_name = cursor.getString(department_name_column);
		int position_id = cursor.getInt(position_id_column);
		String position_name = cursor.getString(position_name_column);
		int isFinal = cursor.getInt(isFinal_column);
		int isRead = cursor.getInt(isRead_column);
		String date = cursor.getString(date_column);
		DynamicListHolder holder = new DynamicListHolder(id, board_train_code,
				start_time, from_station_name, to_station_name, current_team,
				driving_status, user_ids, job_number, user_name, photo, phone,
				department_id, department_name, position_id, position_name,
				position_name,isFinal,isRead,date);
		return holder;
	}

	public ArrayList<DynamicListHolder> selectData(int from, int pagesize,String str_date) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor;
		if(str_date==null){
			cursor = db.query(TABLE_NAME, null, date+"=?", new String[]{str_date}, null, null, start_time
					+ " desc limit " + from + "," + pagesize);
		}else{
			cursor = db.query(TABLE_NAME, null, date+"=?", new String[]{str_date}, null, null, start_time
				+ " desc limit " + from + "," + pagesize);
		}
		ArrayList<DynamicListHolder> holderlist = new ArrayList<DynamicListHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			DynamicListHolder holder = getDataCursor(cursor);
			holderlist.add(holder);
		}
		cursor.close();
		return holderlist;
	}
	public ArrayList<DynamicListHolder> selectDataOnWay(int from, int pagesize,String str_date) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor;
		if(str_date!=null){
			cursor = db.query(TABLE_NAME, null, driving_status+"<>1 AND "+driving_status+"<>2 AND "+ date+"="+str_date, null, null, null, start_time
						+ " desc limit " + from + "," + pagesize);
		}else{
			cursor = db.query(TABLE_NAME, null, driving_status+"<>1 AND "+driving_status+"<>2", null, null, null, start_time
					+ " desc limit " + from + "," + pagesize);
		}
		ArrayList<DynamicListHolder> holderlist = new ArrayList<DynamicListHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			DynamicListHolder holder = getDataCursor(cursor);
			holderlist.add(holder);
		}
		cursor.close();
		return holderlist;
	}
	public int getRead(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, ID+"="+id, null, null, null,null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return 0;
		}
		int isRead_column = cursor.getColumnIndex(isRead);
		int isRead = cursor.getInt(isRead_column);
		cursor.close();
		return isRead;
	}
	public int setRead(int id) {
		synchronized (lock.Lock) {
			SQLiteDatabase db = getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put(isRead, 1);
			return db.update(TABLE_NAME,cv,null, null);
		}
	}
	public ArrayList<DynamicListHolder> selectSearchData(String train_Code
			,int from, int pagesize,String str_date) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor;
		if(str_date==null){
			cursor = db.query(TABLE_NAME, null, board_train_code + " like '%"
					+ train_Code + "%'", null, null, null
					, ID + " desc limit " + from + "," + pagesize);
		}else{
			cursor = db.query(TABLE_NAME, null, board_train_code + " like '%"
				+ train_Code + "%' AND "+date+"="+str_date
				, null, null, null, ID + " desc limit " + from + "," + pagesize);
		}
		ArrayList<DynamicListHolder> holderlist = new ArrayList<DynamicListHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			DynamicListHolder holder = getDataCursor(cursor);
			holderlist.add(holder);
		}
		cursor.close();
		return holderlist;
	}
	public ArrayList<DynamicListHolder> selectSearch_date(String date) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, start_time + " like '%"
				+ date + "%'", null, null, null, ID + " desc");
		ArrayList<DynamicListHolder> holderlist = new ArrayList<DynamicListHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			DynamicListHolder holder = getDataCursor(cursor);
			holderlist.add(holder);
		}
		cursor.close();
		return holderlist;
	}
	public int delete_id(int id) {
		synchronized (lock.Lock) {
			SQLiteDatabase db = getWritableDatabase();
			return db.delete(TABLE_NAME, ID + "=" + id, null);
		}
	}

	public DynamicListHolder selectData_Id(int v_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, ID + "=?",
				new String[] { String.valueOf(v_id) }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		DynamicListHolder holder = getDataCursor(cursor);
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
