package com.dev.stdev.njuskalonovosti.database;

import android.database.sqlite.SQLiteDatabase;

public abstract class AbstractTable {

	protected final SQLiteDatabase db;

	public AbstractTable(SQLiteDatabase database) {
		this.db = database;
	}

	public abstract void createTable();

	public abstract void dropTable();
}
