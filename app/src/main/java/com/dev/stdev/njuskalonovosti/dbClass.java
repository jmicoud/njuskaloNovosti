package com.dev.stdev.njuskalonovosti;

//import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.R.attr.id;


/**
 * Created by IW568 on 9/29/2016.
 */

public class dbClass extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;
    //Database Name
    private static final String DATABASE_NAME = "stanovi";
    //Database table
    private static final String TABLE_NOVI_STANOVI = "novistanovi";
    //Database columns
    private static final String COLUMN_ID =   "id";
    private static final String COLUMN_LINK =  "link";
    private static final String COLUMN_DESCRIPTION =  "description";
    private static final String COLUMN_PRIZE =  "prize";
    private static final String COLUMN_DATETM =  "datetm";


    public dbClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE ="CREATE TABLE"+TABLE_NOVI_STANOVI +"("+COLUMN_ID +"TEXT,"+COLUMN_LINK +"TEXT,"+COLUMN_DESCRIPTION +"TEXT" + COLUMN_PRIZE +"TEXT" + COLUMN_DATETM +"TEXT" +")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOVI_STANOVI);
    // Creating tables again
        onCreate(db);
    }


    public void addApartment(flatData flat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, flat.getId());
        values.put(COLUMN_LINK, flat.getLink());
        values.put(COLUMN_DESCRIPTION, flat.getDescription());
        values.put(COLUMN_PRIZE, flat.getPrize());
        values.put(COLUMN_DATETM, flat.getDtm());
        // Inserting Row
        db.insert(TABLE_NOVI_STANOVI, null, values);
        db.close(); // Closing database connection
    }


    // Deleting single contact
    public void deleteAllApartments() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOVI_STANOVI,null,null);
        db.close();
    }

}



