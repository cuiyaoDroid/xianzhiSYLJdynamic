package com.xianzhi.tool.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class KeyworkAndRecordHelper extends DBHelper {
	private final static String TABLE_NAME = "KeyworkAndRecord_order";
	public final static String ID = "train_id";
	public final static String CONTEXT = "context";
	public final static String PASSENGER_CNT = "passenger_cnt";
	public final static String PACKET_CNT = "packet_cnt";
	public final static String PASSENGER_RCPT = "passenger_rcpt";
	public final static String CATERING_RCPT = "catering_rcpt";
	public final static String PASSENGER_MISS = "passenger_miss";
	public final static String RECEIPTS_MISS = "receipts_miss";
	public final static String WORKER_MISS = "worker_miss";
	public final static String NOTES = "notes";
	
	
	public KeyworkAndRecordHelper(Context context) {
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
				+ CONTEXT + " VARCHAR, " 
				+ PASSENGER_CNT + " FLOAT, " 
				+ PACKET_CNT + " FLOAT, " 
				+ PASSENGER_RCPT + " FLOAT, " 
				+ CATERING_RCPT + " FLOAT, "
				+ PASSENGER_MISS+ " FLOAT, " 
				+ RECEIPTS_MISS + " FLOAT, "
				+ WORKER_MISS + " FLOAT ,"
				+ NOTES + " VARCHAR"
				+ ");";
		return sql;
	}
	public long insert(KeyworkAndRecordHolder content) {
		synchronized (lock.Lock) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			if(content.getTrain_id()!=-1){
				cv.put(ID, content.getTrain_id());
			}
			cv.put(CONTEXT,content.getContext());
			cv.put(PASSENGER_CNT,content.getPassenger_cnt());
			cv.put(PACKET_CNT,content.getPacket_cnt());
			cv.put(PASSENGER_RCPT,content.getPassenger_rcpt());
			cv.put(CATERING_RCPT,content.getCatering_rcpt());
			cv.put(PASSENGER_MISS,content.getPassenger_miss());
			cv.put(RECEIPTS_MISS,content.getReceipts_miss());
			cv.put(WORKER_MISS,content.getWorker_miss());
			cv.put(NOTES,content.getNotes());
			
			return db.replace(TABLE_NAME, null, cv);
		}
	}
	
	
	private KeyworkAndRecordHolder getDataCursor(Cursor cursor){
		int id_column = cursor.getColumnIndex(ID);
		int context_column = cursor.getColumnIndex(CONTEXT);
		int passenger_cnt_column = cursor.getColumnIndex(PASSENGER_CNT);
		int packet_cnt_column = cursor.getColumnIndex(PACKET_CNT);
		int passenger_rcpt_column = cursor.getColumnIndex(PASSENGER_RCPT);
		int catering_rcpt_column = cursor.getColumnIndex(CATERING_RCPT);
		int passenger_miss_column = cursor.getColumnIndex(PASSENGER_MISS);
		int receipts_miss_column = cursor.getColumnIndex(RECEIPTS_MISS);
		int worker_miss_column = cursor.getColumnIndex(WORKER_MISS);
		int notes_column = cursor.getColumnIndex(NOTES);
		
		int train_id = cursor.getInt(id_column);
		String context = cursor.getString(context_column);
		float passenger_cnt = cursor.getFloat(passenger_cnt_column);
		float packet_cnt = cursor.getFloat(packet_cnt_column);
		float passenger_rcpt = cursor.getFloat(passenger_rcpt_column);
		float catering_rcpt = cursor.getFloat(catering_rcpt_column);
		float passenger_miss = cursor.getFloat(passenger_miss_column);
		float receipts_miss = cursor.getFloat(receipts_miss_column);
		float worker_miss = cursor.getFloat(worker_miss_column);
		String notes = cursor.getString(notes_column);
		
		KeyworkAndRecordHolder holder = new KeyworkAndRecordHolder(train_id, context, passenger_cnt, packet_cnt
				, passenger_rcpt, catering_rcpt, passenger_miss, receipts_miss, worker_miss,notes);
		
		
		return holder;
	}
	public int delete_id(int id){
		synchronized (lock.Lock) {
			SQLiteDatabase db = getWritableDatabase();
			return db.delete(TABLE_NAME,  ID + "=" + id, null);
		}
	}
	
	public KeyworkAndRecordHolder selectData_Id(int v_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, ID + "=?",
				new String[] { String.valueOf(v_id) }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		KeyworkAndRecordHolder holder=getDataCursor(cursor);
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
