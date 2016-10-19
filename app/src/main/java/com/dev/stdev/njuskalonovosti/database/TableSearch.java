package com.dev.stdev.njuskalonovosti.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dev.stdev.njuskalonovosti.models.SearchClass;

import java.util.ArrayList;
import java.util.List;

public class TableSearch extends AbstractTable {
	private final String TABLE_SEARCH = "pretrage";
	private final String COLUMN_GENID = "generalid";
	private final String COLUMN_SEARCH = "pretrage";
	private final String COLUMN_TYPE = "tip";

	public TableSearch(SQLiteDatabase database) {
		super(database);
	}

	@Override
	public void createTable() {
		db.execSQL("CREATE TABLE " + TABLE_SEARCH + "(" + COLUMN_GENID + " TEXT," + COLUMN_SEARCH + " TEXT," + COLUMN_TYPE + " TEXT" + ");");
	}

	@Override
	public void dropTable() {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH);
	}

	public void addSearch(SearchClass pretraga) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_GENID, pretraga.getGeneralId());
		values.put(COLUMN_SEARCH, pretraga.getSearch());
		values.put(COLUMN_TYPE, pretraga.getType());

		// Inserting Row
		db.insert(TABLE_SEARCH, null, values);
	}

	// Deleting pretrage on pretraga
	public void deleteSearch(String search) {
		db.delete(TABLE_SEARCH, COLUMN_SEARCH + "='" + search + "'", null);
	}

	// Deleting pretrage on pretraga
	public void deleteSearchByGenId(String generalid) {
		db.delete(TABLE_SEARCH, COLUMN_GENID + "='" + generalid + "'", null);
	}

	// Getting All pretrage
	public List<SearchClass> getSearchByGenID(String genid) {
		List<SearchClass> prdataList = new ArrayList<>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_SEARCH + " WHERE " + COLUMN_GENID + "='" + genid + "'";

		Cursor cursor = null;
		try {
			cursor = db.rawQuery(selectQuery, null);
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					SearchClass prt = new SearchClass();
					prt.setGeneralId(cursor.getString(0));
					prt.setSearch(cursor.getString(1));
					prt.setType(cursor.getString(2));

					// Adding contact to list
					prdataList.add(prt);
				} while (cursor.moveToNext());
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}

		// return apartments list
		return prdataList;
	}

	// Getting All pretrage
	public List<SearchClass> getSearchBySearch(String search) {
		List<SearchClass> prdataList = new ArrayList<>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_SEARCH + " WHERE " + COLUMN_SEARCH + "='" + search + "'";

		Cursor cursor = null;
		try {
			cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					SearchClass prt = new SearchClass();
					prt.setGeneralId(cursor.getString(0));
					prt.setSearch(cursor.getString(1));
					prt.setType(cursor.getString(2));

					// Adding contact to list
					prdataList.add(prt);
				} while (cursor.moveToNext());
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		// return apartments list
		return prdataList;
	}

	// Getting All pretrage
	public List<SearchClass> getAllSearch() {
		List<SearchClass> prdataList = new ArrayList<>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_SEARCH;

		Cursor cursor = null;
		try {
			cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					SearchClass prt = new SearchClass();
					prt.setGeneralId(cursor.getString(0));
					prt.setSearch(cursor.getString(1));
					prt.setType(cursor.getString(2));

					// Adding contact to list
					prdataList.add(prt);
				} while (cursor.moveToNext());
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		// return apartments list
		return prdataList;
	}

	public boolean isSearchTableEmpty() {
		String count = "SELECT count(*) FROM " + TABLE_SEARCH;
		Cursor mcursor = null;
		int icount = 0;
		try {
			mcursor = db.rawQuery(count, null);
			if (mcursor.moveToFirst())
				icount = mcursor.getInt(0);
		} finally {
			if (mcursor != null) {
				mcursor.close();
			}
		}
		return icount <= 0;
	}

}
