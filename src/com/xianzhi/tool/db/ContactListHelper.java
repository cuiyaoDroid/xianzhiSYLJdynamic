package com.xianzhi.tool.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ContactListHelper extends DBHelper {
	private final static String TABLE_NAME = "contactlist_order";
	public final static String ID = "id";
	public final static String NAME = "name";
	public final static String POSITION = "position";
	public final static String DEPTID = "deptId";
	public final static String SORT = "sort";

	public ContactListHelper(Context context) {
		super(context);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "Create table IF NOT EXISTS " + TABLE_NAME + "(" + ID
				+ " integer primary key autoincrement, " + NAME + " VARCHAR, "
				+ DEPTID + " integer, " + SORT + " integer, " + POSITION
				+ " VARCHAR" + ");";

		db.execSQL(sql);
	}

	public long insert(ContactListHolder content) {
		synchronized (lock.Lock) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put(ID, content.getId());
			cv.put(NAME, content.getName());
			cv.put(POSITION, content.getPosition());
			cv.put(DEPTID, content.getDeptId());
			cv.put(SORT, content.getSort());
			long row = db.replace(TABLE_NAME, null, cv);
			return row;
		}
	}

	public long autoidinsert(ContactListHolder content) {
		synchronized (lock.Lock) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put(NAME, content.getName());
			cv.put(POSITION, content.getPosition());
			cv.put(DEPTID, content.getDeptId());
			cv.put(SORT, content.getSort());
			long row = db.replace(TABLE_NAME, null, cv);
			return row;
		}
	}


	public ArrayList<ContactListHolder> selectData(String content) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, DEPTID + "<>0 AND(" + NAME
				+ " like '%" + content + "%' OR " + POSITION + " like '%"
				+ content + "%')", null, null, null, ID + " asc");
		ArrayList<ContactListHolder> holderlist = new ArrayList<ContactListHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			int id_column = cursor.getColumnIndex(ID);
			int name_column = cursor.getColumnIndex(NAME);
			int position_column = cursor.getColumnIndex(POSITION);
			int depid_column = cursor.getColumnIndex(DEPTID);
			int sort_column = cursor.getColumnIndex(SORT);
			int id = cursor.getInt(id_column);
			String name = cursor.getString(name_column);
			String position = cursor.getString(position_column);
			int depid = cursor.getInt(depid_column);
			int sort = cursor.getInt(sort_column);
			ContactListHolder holder = new ContactListHolder(id, name,
					position, depid,sort);
			holderlist.add(holder);
		}
		cursor.close();
		return holderlist;
	}

	public ContactListHolder selectData_id(int vid) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, ID + "=?",
				new String[] { String.valueOf(vid) }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		int id_column = cursor.getColumnIndex(ID);
		int name_column = cursor.getColumnIndex(NAME);
		int position_column = cursor.getColumnIndex(POSITION);
		int depid_column = cursor.getColumnIndex(DEPTID);
		int sort_column = cursor.getColumnIndex(SORT);
		int id = cursor.getInt(id_column);
		String name = cursor.getString(name_column);
		String position = cursor.getString(position_column);
		int depid = cursor.getInt(depid_column);
		int sort = cursor.getInt(sort_column);
		ContactListHolder holder = new ContactListHolder(id, name,
				position, depid,sort);

		cursor.close();
		return holder;
	}

	public ArrayList<ContactListHolder> selectDepIdData(int depId) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db
				.query(TABLE_NAME, null, DEPTID + "=?",
						new String[] { String.valueOf(depId) }, null, null, SORT
								+ " desc");
		ArrayList<ContactListHolder> holderlist = new ArrayList<ContactListHolder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			int id_column = cursor.getColumnIndex(ID);
			int name_column = cursor.getColumnIndex(NAME);
			int position_column = cursor.getColumnIndex(POSITION);
			int depid_column = cursor.getColumnIndex(DEPTID);
			int sort_column = cursor.getColumnIndex(SORT);
			int id = cursor.getInt(id_column);
			String name = cursor.getString(name_column);
			String position = cursor.getString(position_column);
			int depid = cursor.getInt(depid_column);
			int sort = cursor.getInt(sort_column);
			ContactListHolder holder = new ContactListHolder(id, name,
					position, depid,sort);
			holderlist.add(holder);
		}
		cursor.close();
		return holderlist;
	}

	public void clear() {
		synchronized (lock.Lock) {
			SQLiteDatabase db = this.getReadableDatabase();
			String dropsql = " DROP TABLE IF EXISTS " + TABLE_NAME;
			db.execSQL(dropsql);
			String sql = "Create table IF NOT EXISTS " + TABLE_NAME + "(" + ID
					+ " integer primary key autoincrement, " + NAME + " VARCHAR, "
					+ DEPTID + " integer, " + SORT + " integer, " + POSITION
					+ " VARCHAR" + ");";

			db.execSQL(sql);
		}
	}
}
