package com.xianzhi.tool.db;

import java.util.ArrayList;

import com.xianzhi.stool.L;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SeatInfoHelper extends DBHelper {
	private final static String TABLE_NAME = "seatinfo_order";
	public final static String ID = "_id";
	public final static String TICKET_TYPE = "ticket_type";
	public final static String COACH_NO = "coach_no";
	public final static String SALE_MODE = "sale_mode";
	public final static String TO_TELE_CODE = "to_tele_code";
	public final static String OFFICE_NO = "office_no";
	public final static String BOARD_TRAIN_CODE = "board_train_code";
	public final static String SEAT_TYPE_CODE = "seat_type_code";
	public final static String SEAT_NO = "seat_no";
	public final static String FROM_STATION_NAME = "from_station_name";
	public final static String STATISTICS_DATE = "statistics_date";
	public final static String TO_STATION_NAME = "to_station_name";
	public final static String TICKET_SOURCE = "ticket_source";
	public final static String FROM_TELE_CODE = "from_tele_code";
	public final static String TRAIN_DATE = "train_date";
	public final static String WINDOW_NO = "window_no";
	public final static String TICKET_PRICE = "ticket_price";
	public final static String TICKET_NO = "ticket_no";
	public final static String INNER_CODE = "inner_code";
	public final static String TRAIN_NO = "train_no";
	
	
	public SeatInfoHelper(Context context) {
		super(context);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		db.execSQL(getCreateSql());
	}

	private String getCreateSql() {
		String sql = "Create table IF NOT EXISTS " + TABLE_NAME + "(" 
				+ ID + " integer primary key autoincrement, " 
				+ TICKET_TYPE + " integer, " 
				+ COACH_NO + " VARCHAR, " 
				+ SALE_MODE + " VARCHAR, " 
				+ TO_TELE_CODE + " VARCHAR, " 
				+ OFFICE_NO + " VARCHAR, "
				+ BOARD_TRAIN_CODE+ " VARCHAR, " 
				+ SEAT_TYPE_CODE + " VARCHAR, "
				+ SEAT_NO + " VARCHAR, "
				+ FROM_STATION_NAME + " VARCHAR, " 
				+ STATISTICS_DATE + " VARCHAR, " 
				+ TO_STATION_NAME + " VARCHAR, " 
				+ TICKET_SOURCE + " VARCHAR, " 
				+ FROM_TELE_CODE + " VARCHAR, " 
				+ TRAIN_DATE + " VARCHAR, "
				+ WINDOW_NO+ " integer, " 
				+ TICKET_PRICE + " FLOAT, "
				+ TICKET_NO + " VARCHAR, "
				+ INNER_CODE + " VARCHAR, " 
				+ TRAIN_NO + " VARCHAR" 
				+ ");";
		return sql;
	}

	public long insert(SeatInfoHolder content, SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		cv.put(TICKET_TYPE, content.getTicket_type());
		cv.put(TRAIN_NO, content.getTrain_no());
		cv.put(COACH_NO, content.getCoach_no());
		cv.put(SALE_MODE, content.getSale_mode());
		cv.put(TO_TELE_CODE, content.getTo_tele_code());
		cv.put(OFFICE_NO, content.getOffice_no());
		cv.put(BOARD_TRAIN_CODE, content.getBoard_train_code());
		cv.put(SEAT_TYPE_CODE, content.getSeat_type_code());
		cv.put(SEAT_NO, content.getSeat_no());
		cv.put(FROM_STATION_NAME, content.getFrom_station_name());
		cv.put(STATISTICS_DATE, content.getStatistics_date());
		cv.put(TO_STATION_NAME, content.getTo_station_name());
		cv.put(TICKET_SOURCE, content.getTicket_source());
		cv.put(FROM_TELE_CODE, content.getFrom_tele_code());
		cv.put(TRAIN_DATE, content.getTrain_date());
		cv.put(WINDOW_NO, content.getWindow_no());
		cv.put(TICKET_PRICE, content.getTicket_price());
		cv.put(TICKET_NO, content.getTicket_no());
		cv.put(INNER_CODE, content.getInner_code());
		cv.put(TRAIN_NO, content.getTrain_no());
		
		return db.replace(TABLE_NAME, null, cv);
	}
	
	private SeatInfoHolder getDataCursor(Cursor cursor) {
		int ticket_type_column = cursor.getColumnIndex(TICKET_TYPE);
		int coach_no_column = cursor.getColumnIndex(COACH_NO);
		int sale_mode_column = cursor.getColumnIndex(SALE_MODE);
		int to_tele_code_column = cursor.getColumnIndex(TO_TELE_CODE);
		int office_no_column = cursor.getColumnIndex(OFFICE_NO);
		int board_train_code_column = cursor.getColumnIndex(BOARD_TRAIN_CODE);
		int seat_type_code_column = cursor.getColumnIndex(SEAT_TYPE_CODE);
		int seat_no_column = cursor.getColumnIndex(SEAT_NO);
		int from_station_name_column = cursor.getColumnIndex(FROM_STATION_NAME);
		int statistics_date_column = cursor.getColumnIndex(STATISTICS_DATE);
		int to_station_name_column = cursor.getColumnIndex(TO_STATION_NAME);
		int ticket_source_column = cursor.getColumnIndex(TICKET_SOURCE);
		int from_tele_code_column = cursor.getColumnIndex(FROM_TELE_CODE);
		int train_date_column = cursor.getColumnIndex(TRAIN_DATE);
		int window_no_column = cursor.getColumnIndex(WINDOW_NO);
		int ticket_price_column = cursor.getColumnIndex(TICKET_PRICE);
		int ticket_no_column = cursor.getColumnIndex(TICKET_NO);
		int inner_code_column = cursor.getColumnIndex(INNER_CODE);
		int train_no_column = cursor.getColumnIndex(TRAIN_NO);

		int ticket_type = cursor.getInt(ticket_type_column);
		String coach_no = cursor.getString(coach_no_column);
		String sale_mode = cursor.getString(sale_mode_column);
		String to_tele_code = cursor.getString(to_tele_code_column);
		String office_no = cursor.getString(office_no_column);
		String board_train_code = cursor.getString(board_train_code_column);
		String seat_type_code = cursor.getString(seat_type_code_column);
		String seat_no = cursor.getString(seat_no_column);
		String from_station_name = cursor.getString(from_station_name_column);
		String statistics_date = cursor.getString(statistics_date_column);
		String to_station_name = cursor.getString(to_station_name_column);
		String ticket_source = cursor.getString(ticket_source_column);
		String from_tele_code = cursor.getString(from_tele_code_column);
		String train_date = cursor.getString(train_date_column);
		int window_no = cursor.getInt(window_no_column);
		float ticket_price = cursor.getFloat(ticket_price_column);
		String ticket_no = cursor.getString(ticket_no_column);
		String inner_code = cursor.getString(inner_code_column);
		String train_no = cursor.getString(train_no_column);

		SeatInfoHolder holder = new SeatInfoHolder(ticket_type, coach_no, sale_mode
				, to_tele_code, office_no, board_train_code, seat_type_code, seat_no
				, from_station_name, statistics_date, to_station_name, ticket_source
				, from_tele_code, train_date, window_no, ticket_price, ticket_no
				, inner_code, train_no);

		return holder;
	}
	public int delete_id(int id) {
		synchronized (lock.Lock) {
			SQLiteDatabase db = getWritableDatabase();
			return db.delete(TABLE_NAME, ID + "=" + id, null);
		}
	}

	public int delete_trainCode(String trainCode,String trainDate,String coachNo) {
		synchronized (lock.Lock) {
			SQLiteDatabase db = getWritableDatabase();
			return db.delete(TABLE_NAME, BOARD_TRAIN_CODE + "=? AND "+TRAIN_DATE+"=? AND "+COACH_NO+"=?"
					, new String[] { trainCode,trainDate,coachNo});
		}
	}

	public ArrayList<SeatInfoHolder> selectData_trainCode(String trainCode,String trainDate
			,String coachNo,String seat_no) {
		SQLiteDatabase db = this.getReadableDatabase();
		L.i("trainCode "+trainCode+" trainDate "+trainDate+" coachNo "+coachNo+" seat_no "+seat_no);
		Cursor cursor = db.query(TABLE_NAME, null, 
				BOARD_TRAIN_CODE + "=? AND "+TRAIN_DATE+"=? AND "+COACH_NO+"=? AND "+SEAT_NO+"=?",
				new String[] { trainCode,trainDate,coachNo,seat_no}, null, null, null);
		
		ArrayList<SeatInfoHolder>holderlist=new ArrayList<SeatInfoHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			SeatInfoHolder holder = getDataCursor(cursor);
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
