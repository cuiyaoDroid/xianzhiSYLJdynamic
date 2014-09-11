package com.xianzhi.tool.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TrainCoachHelper extends DBHelper {
	private final static String TABLE_NAME = "trainCoach_order";
	public final static String ID = "_id";
	public final static String COACH_NO = "coach_no";
	public final static String TRAIN_NO = "train_no";
	public final static String COACH_TYPE = "coach_type";
	public final static String LIMIT1 = "limit1";
	public final static String TRAINDATE = "trainDate";
	public final static String TRAINCODE = "trainCode";
	public final static String TRAIN_ID = "train_id";

	public TrainCoachHelper(Context context) {
		super(context);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		db.execSQL(getCreateSql());
	}

	private String getCreateSql() {
		String sql = "Create table IF NOT EXISTS " + TABLE_NAME + "(" + ID
				+ " integer primary key autoincrement, " + COACH_NO
				+ " VARCHAR, " + TRAIN_NO + " VARCHAR, " + COACH_TYPE
				+ " VARCHAR, " + LIMIT1 + " integer, " + TRAINDATE
				+ " VARCHAR, " + TRAIN_ID + " integer, " + TRAINCODE
				+ " VARCHAR" + ");";
		return sql;
	}

	public long insert(TrainCoachHolder content, SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		cv.put(COACH_NO, content.getCoach_no());
		cv.put(TRAIN_NO, content.getTrain_no());
		cv.put(COACH_TYPE, content.getCoach_type());
		cv.put(LIMIT1, content.getLimit1());
		cv.put(TRAINDATE, content.getTrainDate());
		cv.put(TRAINCODE, content.getTrainCode());
		cv.put(TRAIN_ID, content.getTrain_id());
		return db.replace(TABLE_NAME, null, cv);
	}

	private TrainCoachHolder getDataCursor(Cursor cursor) {
		int coach_no_column = cursor.getColumnIndex(COACH_NO);
		int train_no_column = cursor.getColumnIndex(TRAIN_NO);
		int coach_type_column = cursor.getColumnIndex(COACH_TYPE);
		int limit1_column = cursor.getColumnIndex(LIMIT1);
		int trainDate_column = cursor.getColumnIndex(TRAINDATE);
		int trainCode_column = cursor.getColumnIndex(TRAINCODE);
		int train_id_column = cursor.getColumnIndex(TRAIN_ID);

		String coach_no = cursor.getString(coach_no_column);
		String train_no = cursor.getString(train_no_column);
		String coach_type = cursor.getString(coach_type_column);
		int limit1 = cursor.getInt(limit1_column);
		String trainDate = cursor.getString(trainDate_column);
		String trainCode = cursor.getString(trainCode_column);
		int train_id = cursor.getInt(train_id_column);

		TrainCoachHolder holder = new TrainCoachHolder(coach_no, train_no,
				coach_type, limit1, trainDate, trainCode, train_id);

		return holder;
	}

	public int delete_id(int id) {
		synchronized (lock.Lock) {
			SQLiteDatabase db = getWritableDatabase();
			return db.delete(TABLE_NAME, ID + "=" + id, null);
		}
	}

	public int delete_train_id(int train_id) {
		synchronized (lock.Lock) {
			SQLiteDatabase db = getWritableDatabase();
			return db.delete(TABLE_NAME, TRAIN_ID + "=" + train_id, null);
		}
	}

	public TrainCoachHolder selectData_Id(int v_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, ID + "=?",
				new String[] { String.valueOf(v_id) }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		TrainCoachHolder holder = getDataCursor(cursor);
		cursor.close();
		return holder;
	}
	public ArrayList<TrainCoachHolder> selectDataTrainId(int trainId) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, TRAIN_ID + "=" + trainId, null, null, null, ID
				+ " desc");
		ArrayList<TrainCoachHolder> holderlist = new ArrayList<TrainCoachHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			TrainCoachHolder holder = getDataCursor(cursor);
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
