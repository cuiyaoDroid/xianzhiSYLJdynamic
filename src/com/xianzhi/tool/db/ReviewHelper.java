package com.xianzhi.tool.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ReviewHelper extends DBHelper {
	private final static String TABLE_NAME = "reviewlist_order";
	public final static String ID = "_id";
	public final static String TRAIN_ID = "train_id";
	public final static String USER_ID = "user_id";
	public final static String USER_NAME = "user_name";
	public final static String USER_PHONE = "user_phone";
	public final static String POSITION_NAME = "position_name";
	public final static String ROLE_NAMES = "role_names";
	public final static String MESSAGE = "message";
	public final static String MSG_STATUS = "msg_status";
	public final static String MSG_TYPE = "msg_type";
	public final static String CREATE_TIME = "create_time";
	
	
	public ReviewHelper(Context context) {
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
				+ USER_ID + " integer, "
				+ USER_NAME + " VARCHAR, "
				+ USER_PHONE + " VARCHAR, "
				+ POSITION_NAME + " VARCHAR, "
				+ ROLE_NAMES + " VARCHAR, "
				+ MESSAGE + " VARCHAR, "
				+ MSG_STATUS + " integer, "
				+ MSG_TYPE + " integer, "
				+ CREATE_TIME + " LONG"
				+ ");";
		return sql;
	}

	public long insert(ReviewHolder content, SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		if (content.getId() != -1) {
			cv.put(ID, content.getId());
		}
		cv.put(TRAIN_ID, content.getTrain_id());
		cv.put(USER_ID, content.getUser_id());
		cv.put(USER_NAME, content.getUser_name());
		cv.put(USER_PHONE, content.getUser_phone());
		cv.put(POSITION_NAME, content.getPosition_name());
		cv.put(ROLE_NAMES, content.getRole_names());
		cv.put(MESSAGE, content.getMessage());
		cv.put(MSG_STATUS, content.getMsg_status());
		cv.put(MSG_TYPE, content.getMsg_type());
		cv.put(CREATE_TIME, content.getCreate_time());
		return db.replace(TABLE_NAME, null, cv);
	}

	private ReviewHolder getDataCursor(Cursor cursor) {
		int id_column = cursor.getColumnIndex(ID);
		int train_id_column = cursor.getColumnIndex(TRAIN_ID);
		int user_id_column = cursor.getColumnIndex(USER_ID);
		int user_name_column = cursor.getColumnIndex(USER_NAME);
		int user_phone_column = cursor.getColumnIndex(USER_PHONE);
		int position_name_column = cursor.getColumnIndex(POSITION_NAME);
		int role_names_column = cursor.getColumnIndex(ROLE_NAMES);
		int message_column = cursor.getColumnIndex(MESSAGE);
		int msg_status_column = cursor.getColumnIndex(MSG_STATUS);
		int msg_type_column = cursor.getColumnIndex(MSG_TYPE);
		int create_time_column = cursor.getColumnIndex(CREATE_TIME);

		int id = cursor.getInt(id_column);
		int train_id = cursor.getInt(train_id_column);
		int user_id = cursor.getInt(user_id_column);
		String user_name = cursor.getString(user_name_column);
		String user_phone = cursor.getString(user_phone_column);
		String position_name = cursor.getString(position_name_column);
		String role_names = cursor.getString(role_names_column);
		String message = cursor.getString(message_column);
		int msg_status = cursor.getInt(msg_status_column);
		int msg_type = cursor.getInt(msg_type_column);
		long create_time = cursor.getLong(create_time_column);
		
		ReviewHolder holder = new ReviewHolder(id, train_id, user_id, user_name, user_phone, position_name
				, role_names, message, msg_status, msg_type, create_time);
		return holder;
	}

	public ArrayList<ReviewHolder> selectData(int trainId) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, TRAIN_ID+"=?"
				, new String[]{String.valueOf(trainId)}, null, null, ID
				+ " desc");
		ArrayList<ReviewHolder> holderlist = new ArrayList<ReviewHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			ReviewHolder holder = getDataCursor(cursor);
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
	public int delete_Trainid(int train_id) {
		synchronized (lock.Lock) {
			SQLiteDatabase db = getWritableDatabase();
			return db.delete(TABLE_NAME, TRAIN_ID + "=" + train_id, null);
		}
	}

	public ReviewHolder selectData_Id(int v_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, ID + "=?",
				new String[] { String.valueOf(v_id) }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		ReviewHolder holder = getDataCursor(cursor);
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
