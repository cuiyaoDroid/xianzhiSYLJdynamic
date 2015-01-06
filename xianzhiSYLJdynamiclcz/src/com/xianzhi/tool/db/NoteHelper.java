package com.xianzhi.tool.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NoteHelper extends DBHelper {
	private final static String TABLE_NAME = "Note_order";
	public final static String ID = "id";
	public final static String DYNAMIC_ID = "dynamic_id";
	public final static String PASSENGER_NUM = "passenger_num";
	public final static String TICKET_ORDER = "ticket_order";
	public final static String DINING_ORDER = "dining_order";
	public final static String PASSENGER_DEAD_NUM = "passenger_dead_num";
	public final static String EMPOYEE_DEAD_NUM = "empoyee_dead_num";
	public final static String PACKAGE_NUM = "package_num";
	public final static String TICK_LOST = "tick_lost";
	public final static String NOTEPAD = "Notepad";
	public final static String IMPORT_INFO = "import_info";
	public final static String SUBMIT_TIME = "submit_time";
	public final static String SUBMIT_NAME = "submit_name";
	public final static String APPROVAL_TIME = "approval_time";
	public final static String APPROVAL_NAME = "approval_name";
	public NoteHelper(Context context) {
		super(context);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "Create table IF NOT EXISTS " + TABLE_NAME + "(" 
				+ ID + " integer primary key autoincrement, " 
				+ DYNAMIC_ID + " integer, " 
				+ PASSENGER_NUM + " VARCHAR, " 
				+ TICKET_ORDER + " VARCHAR, "
				+ DINING_ORDER+ " VARCHAR, " 
				+ PASSENGER_DEAD_NUM + " VARCHAR, "
				+ EMPOYEE_DEAD_NUM + " VARCHAR, " 
				+ PACKAGE_NUM + " VARCHAR, "
				+ TICK_LOST+ " VARCHAR, "
				+ NOTEPAD + " VARCHAR, "
				+ SUBMIT_TIME + " LONG, "
				+ SUBMIT_NAME + " VARCHAR, "
				+ APPROVAL_TIME + " LONG, "
				+ APPROVAL_NAME + " VARCHAR, "
				+ IMPORT_INFO + " VARCHAR"
				+ ");";
		db.execSQL(sql);
	}

	public long insert(NoteHolder content) {
		synchronized (lock.Lock) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			if(content.getId()!=-1){
				cv.put(ID, content.getId());
			}
			cv.put(DYNAMIC_ID, content.getDynamic_id());
			cv.put(PASSENGER_NUM, content.getPassenger_num());
			cv.put(TICKET_ORDER, content.getTicket_order());
			cv.put(DINING_ORDER, content.getDining_order());
			cv.put(PASSENGER_DEAD_NUM, content.getPassenger_dead_num());
			cv.put(EMPOYEE_DEAD_NUM, content.getEmpoyee_dead_num());
			cv.put(PACKAGE_NUM, content.getPackage_num());
			cv.put(TICK_LOST, content.getTick_lost());
			cv.put(NOTEPAD, content.getNotepad());
			cv.put(IMPORT_INFO, content.getImport_info());
			cv.put(SUBMIT_TIME, content.getSubmit_time());
			cv.put(SUBMIT_NAME, content.getSubmit_name());
			cv.put(APPROVAL_TIME, content.getApproval_time());
			cv.put(APPROVAL_NAME, content.getApproval_name());
			
			return db.replace(TABLE_NAME, null, cv);
		}
	}
	private NoteHolder getDataCursor(Cursor cursor){
		int id_column = cursor.getColumnIndex(ID);
		int dynamic_id_column = cursor.getColumnIndex(DYNAMIC_ID);
		int passenger_num_column = cursor.getColumnIndex(PASSENGER_NUM);
		int ticket_order_column = cursor.getColumnIndex(TICKET_ORDER);
		int dining_order_column = cursor.getColumnIndex(DINING_ORDER);
		int passenger_dead_num_column = cursor.getColumnIndex(PASSENGER_DEAD_NUM);
		int empoyee_dead_num_column = cursor.getColumnIndex(EMPOYEE_DEAD_NUM);
		int package_num_column = cursor.getColumnIndex(PACKAGE_NUM);
		int tick_lost_column = cursor.getColumnIndex(TICK_LOST);
		int notepad_column = cursor.getColumnIndex(NOTEPAD);
		int import_info_column = cursor.getColumnIndex(IMPORT_INFO);
		int submit_time_column = cursor.getColumnIndex(SUBMIT_TIME);
		int submit_name_column = cursor.getColumnIndex(SUBMIT_NAME);
		int approval_time_column = cursor.getColumnIndex(APPROVAL_TIME);
		int approval_name_column = cursor.getColumnIndex(APPROVAL_NAME);
		
		int id = cursor.getInt(id_column);
		int dynamic_id = cursor.getInt(dynamic_id_column);
		String passenger_num = cursor.getString(passenger_num_column);
		String ticket_order = cursor.getString(ticket_order_column);
		String dining_order = cursor.getString(dining_order_column);
		String passenger_dead_num = cursor.getString(passenger_dead_num_column);
		String empoyee_dead_num = cursor.getString(empoyee_dead_num_column);
		String package_num = cursor.getString(package_num_column);
		String tick_lost = cursor.getString(tick_lost_column);
		String notepad = cursor.getString(notepad_column);
		String import_info = cursor.getString(import_info_column);
		long submit_time = cursor.getLong(submit_time_column);
		String submit_name = cursor.getString(submit_name_column);
		long approval_time = cursor.getLong(approval_time_column);
		String approval_name = cursor.getString(approval_name_column);
		
		NoteHolder holder = new NoteHolder(id, dynamic_id, passenger_num, ticket_order
				, dining_order, passenger_dead_num, empoyee_dead_num, package_num
				, tick_lost, notepad, import_info, submit_time, submit_name, approval_time, approval_name);
		return holder;
	}
	public ArrayList<NoteHolder> selectData(int from,int pagesize) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null
				,null, null, null, ID + " desc limit "
						+ from + "," + pagesize);
		ArrayList<NoteHolder> holderlist = new ArrayList<NoteHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			NoteHolder holder=getDataCursor(cursor);
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
	
	public NoteHolder selectData_dynamicId(int v_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, DYNAMIC_ID + "=?",
				new String[] { String.valueOf(v_id) }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		NoteHolder holder=getDataCursor(cursor);
		cursor.close();
		return holder;
	}
	public NoteHolder selectData_Id(int v_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, ID + "=?",
				new String[] { String.valueOf(v_id) }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		NoteHolder holder=getDataCursor(cursor);
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
					+ DYNAMIC_ID + " integer, " 
					+ PASSENGER_NUM + " VARCHAR, " 
					+ TICKET_ORDER + " VARCHAR, "
					+ DINING_ORDER+ " VARCHAR, " 
					+ PASSENGER_DEAD_NUM + " VARCHAR, "
					+ EMPOYEE_DEAD_NUM + " VARCHAR, " 
					+ PACKAGE_NUM + " VARCHAR, "
					+ TICK_LOST+ " VARCHAR, "
					+ NOTEPAD + " VARCHAR, "
					+ SUBMIT_TIME + " LONG, "
					+ SUBMIT_NAME + " VARCHAR, "
					+ APPROVAL_TIME + " LONG, "
					+ APPROVAL_NAME + " VARCHAR, "
					+ IMPORT_INFO + " VARCHAR"
					+ ");";

			db.execSQL(sql);
		}
	}
}
