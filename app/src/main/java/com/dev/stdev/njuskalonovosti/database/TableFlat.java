package com.dev.stdev.njuskalonovosti.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dev.stdev.njuskalonovosti.models.FlatAdvertismentClass;

import java.util.ArrayList;
import java.util.List;

public class TableFlat extends AbstractTable {

	private final String TABLE_NEW_FLATS = "novistanovi";
	private final String COLUMN_ID = "id";
	private final String COLUMN_GENID = "generalid";
	private final String COLUMN_LINK = "link";
	private final String COLUMN_DESCRIPTION = "description";
	private final String COLUMN_PRIZE = "prize";
	private final String COLUMN_DATETM = "datetm";

	public TableFlat(SQLiteDatabase database) {
		super(database);
	}

	@Override
	public void createTable() {
		String CREATE_FLATS_TABLE = "CREATE TABLE " + TABLE_NEW_FLATS +
				"(" + COLUMN_GENID + " TEXT," + COLUMN_ID + " TEXT," + COLUMN_LINK + " TEXT,"
				+ COLUMN_DESCRIPTION + " TEXT," + COLUMN_PRIZE + " TEXT," + COLUMN_DATETM + " TEXT" + ");";
		db.execSQL(CREATE_FLATS_TABLE);
	}

	@Override
	public void dropTable() {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEW_FLATS);
	}

	public void addFlat(FlatAdvertismentClass flat, String generalId) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_GENID, generalId);
		values.put(COLUMN_ID, flat.getId());
		values.put(COLUMN_LINK, flat.getLink());
		values.put(COLUMN_DESCRIPTION, flat.getDescription());
		values.put(COLUMN_PRIZE, flat.getPrize());
		values.put(COLUMN_DATETM, flat.getDtm());
		// Inserting Row
		db.insert(TABLE_NEW_FLATS, null, values);
	}

	// Deleting apartment
	public void deleteFlat(String generalid) {
		db.delete(TABLE_NEW_FLATS, COLUMN_GENID + "=" + generalid, null);
	}

	// Getting All Apartments by generalid
	public List<FlatAdvertismentClass> getAllApartments(String generalid) {
		List<FlatAdvertismentClass> fldataList = new ArrayList<>();
		// Select All Query

		//Log.d("get","get apatment at"+generalid);
		String selectQuery = "SELECT * FROM " + TABLE_NEW_FLATS + " WHERE " + COLUMN_GENID + "=" + generalid;

		Cursor cursor = null;
		try {
			cursor = db.rawQuery(selectQuery, null);

			//Log.d("get","getapatmentafter");
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					FlatAdvertismentClass fld = new FlatAdvertismentClass();
					fld.setId(cursor.getString(1));
					fld.setLink(cursor.getString(2));
					fld.setDescription(cursor.getString(3));
					fld.setPrize(cursor.getString(4));
					fld.setDtm(cursor.getString(5));
					// Adding contact to list
					fldataList.add(fld);
				} while (cursor.moveToNext());
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		// return apartments list
		return fldataList;
	}

	    /*boolean isApartmentsTableEmpty() {
	    SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_NEW_FLATS;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        mcursor.close();
        db.close();
        return icount <= 0;
    }*/

}
