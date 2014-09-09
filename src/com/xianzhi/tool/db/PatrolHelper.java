package com.xianzhi.tool.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PatrolHelper extends DBHelper {
	private final static String TABLE_NAME = "Patrol_order";
	public final static String ID = "id";
	public final static String CONTENT = "content";
	public final static String IMG = "img";
	public final static String VIDEODATA = "videoData";
	public final static String COMPLETE_TIME = "completeTime";
	public final static String TOUSERIDS = "toUserIds";
	public final static String CREAT_TIME = "creatTime";
	public final static String USERNAME = "userName";
	public final static String RESPOSIBLE_PERSONNAME = "responsiblePersonName";
	public final static String RESULT = "result";
	public final static String USERID="userId";
	
	public final static String TYPE_ID="typeId";
	public final static String TYPE_NAME="type_name";
	public final static String RESULT_TYPE_ID="result_typeId";
	public final static String RESULT_TYPE_NAME="result_typeId_name";		
	public PatrolHelper(Context context) {
		super(context);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "Create table IF NOT EXISTS " + TABLE_NAME + "(" 
				+ ID + " integer primary key autoincrement, " 
				+ CONTENT + " VARCHAR, " 
				+ IMG + " VARCHAR, " 
				+ VIDEODATA + " VARCHAR, " 
				+ USERID + " integer, " 
				+ COMPLETE_TIME + " LONG, "
				+ TOUSERIDS+ " VARCHAR, " 
				+ CREAT_TIME + " LONG, " 
				+ USERNAME + " VARCHAR, " 
				+ RESPOSIBLE_PERSONNAME + " VARCHAR, " 
				+ TYPE_ID+ " integer, " 
				+ TYPE_NAME + " VARCHAR, " 
				+ RESULT_TYPE_ID + " integer, " 
				+ RESULT_TYPE_NAME + " VARCHAR, " 
				+ RESULT + " VARCHAR" 
				+ ");";

		db.execSQL(sql);
	}

	public long insert(PatrolHolder content) {
		synchronized (lock.Lock) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			if(content.getId()!=-1){
				cv.put(ID, content.getId());
			}
			cv.put(CONTENT, content.getContent());
			cv.put(IMG, content.getImg());
			cv.put(VIDEODATA, content.getVideoData());
			cv.put(COMPLETE_TIME, content.getCompleteTime());
			cv.put(TOUSERIDS, content.getToUserIds());
			cv.put(CREAT_TIME, content.getCreatTime());
			cv.put(USERNAME, content.getUserName());
			cv.put(RESPOSIBLE_PERSONNAME, content.getResponsiblePersonName());
			cv.put(RESULT, content.getResult());
			cv.put(USERID, content.getUserId());
			cv.put(TYPE_ID, content.getTypeId());
			cv.put(TYPE_NAME, content.getType_name());
			cv.put(RESULT_TYPE_ID, content.getResult_typeId());
			cv.put(RESULT_TYPE_NAME, content.getResult_typeId_name());
			return db.replace(TABLE_NAME, null, cv);
		}
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		String sql = " DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);
	}
	private PatrolHolder getDataCursor(Cursor cursor){
		int id_column = cursor.getColumnIndex(ID);
		int content_column = cursor.getColumnIndex(CONTENT);
		int img_column = cursor.getColumnIndex(IMG);
		int videodata_column = cursor.getColumnIndex(VIDEODATA);
		int complete_time_column = cursor.getColumnIndex(COMPLETE_TIME);
		int touserids_column = cursor.getColumnIndex(TOUSERIDS);
		int creat_time_column = cursor.getColumnIndex(CREAT_TIME);
		int username_column = cursor.getColumnIndex(USERNAME);
		int resposible_personname_column = cursor.getColumnIndex(RESPOSIBLE_PERSONNAME);
		int result_column = cursor.getColumnIndex(RESULT);
		int userId_column = cursor.getColumnIndex(USERID);
		int type_id_column = cursor.getColumnIndex(TYPE_ID);
		int type_name_column = cursor.getColumnIndex(TYPE_NAME);
		int result_type_id_column = cursor.getColumnIndex(RESULT_TYPE_ID);
		int result_type_name_column = cursor.getColumnIndex(RESULT_TYPE_NAME);
		
		int id = cursor.getInt(id_column);
		String content = cursor.getString(content_column);
		String img = cursor.getString(img_column);
		String videodata = cursor.getString(videodata_column);
		long complete_time = cursor.getLong(complete_time_column);
		String touserids = cursor.getString(touserids_column);
		long creat_time = cursor.getLong(creat_time_column);
		String username = cursor.getString(username_column);
		String resposible_personname = cursor.getString(resposible_personname_column);
		String result = cursor.getString(result_column);
		int userId = cursor.getInt(userId_column);
		int type_id = cursor.getInt(type_id_column);
		String type_name = cursor.getString(type_name_column);
		int result_type_id = cursor.getInt(result_type_id_column);
		String result_type_name = cursor.getString(result_type_name_column);
		
		PatrolHolder holder = new PatrolHolder(id, content, img, videodata, complete_time, touserids, creat_time, username, resposible_personname, result, userId
				, type_id, type_name, result_type_id, result_type_name);
		return holder;
	}
	public ArrayList<PatrolHolder> selectData(int userId,int from,int pagesize) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null
				,null, null, null, ID + " desc limit "
						+ from + "," + pagesize);
		ArrayList<PatrolHolder> holderlist = new ArrayList<PatrolHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			PatrolHolder holder=getDataCursor(cursor);
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
	
	public PatrolHolder selectData_Id(int v_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, ID + "=?",
				new String[] { String.valueOf(v_id) }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		PatrolHolder holder=getDataCursor(cursor);
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
					+ CONTENT + " VARCHAR, " 
					+ IMG + " VARCHAR, " 
					+ VIDEODATA + " VARCHAR, " 
					+ USERID + " integer, " 
					+ COMPLETE_TIME + " LONG, "
					+ TOUSERIDS+ " VARCHAR, " 
					+ CREAT_TIME + " LONG, " 
					+ USERNAME + " VARCHAR, " 
					+ RESPOSIBLE_PERSONNAME + " VARCHAR, " 
					+ TYPE_ID+ " integer, " 
					+ TYPE_NAME + " VARCHAR, " 
					+ RESULT_TYPE_ID + " integer, " 
					+ RESULT_TYPE_NAME + " VARCHAR, " 
					+ RESULT + " VARCHAR" 
					+ ");";

			db.execSQL(sql);
		}
	}
}
