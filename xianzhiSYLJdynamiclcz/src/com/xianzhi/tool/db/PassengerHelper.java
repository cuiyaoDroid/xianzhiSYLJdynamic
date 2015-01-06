package com.xianzhi.tool.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PassengerHelper extends DBHelper {
	private final static String TABLE_NAME = "Passenger_order";
	public final static String ID = "id";
	public final static String DYNAMIC_ID = "dynamic_id";
	public final static String GROUP_ID = "group_id";
	public final static String START_STATION = "start_station";
	public final static String PERSONAL_CODE = "personal_code";
	public final static String ARRIVE_STATION = "arrive_station";
	public final static String TICK_PRICE = "tick_price";
	public final static String NAME = "name";
	public final static String SEAT = "seat";

	public PassengerHelper(Context context) {
		super(context);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "Create table IF NOT EXISTS " + TABLE_NAME + "(" + ID
				+ " integer primary key autoincrement, " + DYNAMIC_ID
				+ " integer, " + GROUP_ID + " integer, " + START_STATION
				+ " VARCHAR, " + PERSONAL_CODE + " VARCHAR, " + ARRIVE_STATION
				+ " VARCHAR, " + SEAT + " VARCHAR, " + TICK_PRICE
				+ " VARCHAR, " + NAME + " VARCHAR" + ");";
		db.execSQL(sql);
	}

	public long insert(PassengerHolder content) {
		synchronized (lock.Lock) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			if (content.getId() != -1) {
				cv.put(ID, content.getId());
			}
			cv.put(DYNAMIC_ID, content.getDynamic_id());
			cv.put(GROUP_ID, content.getGroup_id());
			cv.put(START_STATION, content.getStart_station());
			cv.put(PERSONAL_CODE, content.getPersonal_code());
			cv.put(ARRIVE_STATION, content.getArrive_station());
			cv.put(TICK_PRICE, content.getTick_price());
			cv.put(NAME, content.getName());
			cv.put(SEAT, content.getSeat());
			return db.replace(TABLE_NAME, null, cv);
		}
	}

	private PassengerHolder getDataCursor(Cursor cursor) {
		int id_column = cursor.getColumnIndex(ID);
		int dynamic_id_column = cursor.getColumnIndex(DYNAMIC_ID);
		int group_id_column = cursor.getColumnIndex(GROUP_ID);
		int start_station_column = cursor.getColumnIndex(START_STATION);
		int personal_code_column = cursor.getColumnIndex(PERSONAL_CODE);
		int arrive_station_column = cursor.getColumnIndex(ARRIVE_STATION);
		int tick_price_column = cursor.getColumnIndex(TICK_PRICE);
		int name_column = cursor.getColumnIndex(NAME);
		int seat_column = cursor.getColumnIndex(SEAT);

		int id = cursor.getInt(id_column);
		int dynamic_id = cursor.getInt(dynamic_id_column);
		int group_id = cursor.getInt(group_id_column);
		String start_station = cursor.getString(start_station_column);
		String personal_code = cursor.getString(personal_code_column);
		String arrive_station = cursor.getString(arrive_station_column);
		String tick_price = cursor.getString(tick_price_column);
		String name = cursor.getString(name_column);
		String seat = cursor.getString(seat_column);

		PassengerHolder holder = new PassengerHolder(id, dynamic_id, group_id,
				start_station, personal_code, arrive_station, tick_price, name,
				seat);
		return holder;
	}

	public ArrayList<PassengerHolder> selectData(int from, int pagesize) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, ID
				+ " desc limit " + from + "," + pagesize);
		ArrayList<PassengerHolder> holderlist = new ArrayList<PassengerHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			PassengerHolder holder = getDataCursor(cursor);
			holderlist.add(holder);
		}
		cursor.close();
		return holderlist;
	}
	public ArrayList<PassengerHolder> selectDataGroup(int dynamic_id, int group_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, DYNAMIC_ID + "=? AND "+GROUP_ID + "=?"
				, new String[] { String.valueOf(dynamic_id) ,String.valueOf(group_id)}, null, null, ID
				+ " desc");
		ArrayList<PassengerHolder> holderlist = new ArrayList<PassengerHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			PassengerHolder holder = getDataCursor(cursor);
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

	public PassengerHolder selectData_Id(int v_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, ID + "=?",
				new String[] { String.valueOf(v_id) }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		PassengerHolder holder = getDataCursor(cursor);
		cursor.close();
		return holder;
	}

	public void clear() {
		synchronized (lock.Lock) {
			SQLiteDatabase db = this.getReadableDatabase();
			String dropsql = " DROP TABLE IF EXISTS " + TABLE_NAME;
			db.execSQL(dropsql);
			String sql = "Create table IF NOT EXISTS " + TABLE_NAME + "(" + ID
					+ " integer primary key autoincrement, " + DYNAMIC_ID
					+ " integer, " + GROUP_ID + " integer, " + START_STATION
					+ " VARCHAR, " + PERSONAL_CODE + " VARCHAR, "
					+ ARRIVE_STATION + " VARCHAR, " + SEAT + " VARCHAR, "
					+ TICK_PRICE + " VARCHAR, " + NAME + " VARCHAR" + ");";

			db.execSQL(sql);
		}
	}
}
