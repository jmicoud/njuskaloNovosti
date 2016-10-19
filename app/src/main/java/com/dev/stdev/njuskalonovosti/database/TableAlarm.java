package com.dev.stdev.njuskalonovosti.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dev.stdev.njuskalonovosti.models.AlarmClass;

import java.util.ArrayList;
import java.util.List;

public class TableAlarm extends AbstractTable {

	private final String TABLE_NAME = "alarm";
	private final String COLUMN_GENID = "generalid";
	private final String COLUMN_INTERVAL = "interval";

	public TableAlarm(SQLiteDatabase database) {
		super(database);
	}

	@Override
	public void createTable() {
		db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COLUMN_GENID + " TEXT," + COLUMN_INTERVAL + " TEXT" + ");");
	}

	@Override
	public void dropTable() {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	}

	public void addAlarm(AlarmClass alarm) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_GENID, alarm.getGeneralid());
		values.put(COLUMN_INTERVAL, alarm.getInterval());

		// Inserting Row
		db.insert(TABLE_NAME, null, values);
	}

	/**
	 * Deleting alarm
	 */
	public void deleteAlarm(String generalid) {
		db.delete(TABLE_NAME, COLUMN_GENID + "=" + generalid, null);
	}

	/**
	 * Getting All Alarms
	 *
	 * @return
	 */
	public List<AlarmClass> getAllAlarms() {
		List<AlarmClass> alList = new ArrayList<>();
		// Select All Query

		//Log.d("get","get apatment at"+generalid);
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;

		Cursor cursor = null;
		try {
			cursor = db.rawQuery(selectQuery, null);
			//Log.d("get","getapatmentafter");
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					AlarmClass ac = new AlarmClass();
					ac.setGeneralid(cursor.getString(0));
					ac.setInterval(cursor.getString(1));
					//ac.setAlarmid(cursor.getString(2));

					// Adding contact to list
					alList.add(ac);
				} while (cursor.moveToNext());
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		// return apartments list
		return alList;
	}


}
