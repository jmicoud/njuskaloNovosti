package com.dev.stdev.njuskalonovosti.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dev.stdev.njuskalonovosti.App;

public class DatabaseClass extends SQLiteOpenHelper {

	//Database Version
	private static final int DATABASE_VERSION = 1;
	//Database Name
	private static final String DATABASE_NAME = "stanovi.db";

	private static SQLiteDatabase database = null;

	public DatabaseClass(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (AbstractTable table : databaseTables(db))
			table.createTable();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		for (AbstractTable table : databaseTables(db))
			table.dropTable();

		// Creating tables again
		onCreate(db);
	}

	private AbstractTable[] databaseTables(SQLiteDatabase db) {
		return new AbstractTable[]{
				new TableAlarm(db),
				new TableSearch(db),
				new TableFlat(db),
		};
	}

	public static SQLiteDatabase getDatabase() {
		if (database == null) {
			DatabaseClass databaseClass = new DatabaseClass(App.getApplication());
			database = databaseClass.getWritableDatabase();
		}
		return database;
	}
}



