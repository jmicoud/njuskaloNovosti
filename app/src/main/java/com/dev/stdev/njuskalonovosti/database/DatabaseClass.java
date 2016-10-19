package com.dev.stdev.njuskalonovosti.database;

//import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dev.stdev.njuskalonovosti.classes.AlarmClass;
import com.dev.stdev.njuskalonovosti.classes.FlatAdvertismentClass;
import com.dev.stdev.njuskalonovosti.classes.SearchClass;

import java.util.ArrayList;
import java.util.List;



public class DatabaseClass extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;
    //Database Name
    private static final String DATABASE_NAME = "stanovi";
    //Database table
    private static final String TABLE_ALARMI = "alarm";
    private static final String TABLE_PRETRAGE = "pretrage";
    private static final String TABLE_NOVI_STANOVI = "novistanovi";

    //Database columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_GENID = "generalid";
    private static final String COLUMN_LINK = "link";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRIZE = "prize";
    private static final String COLUMN_DATETM = "datetm";
    private static final String COLUMN_PRETRAGE = "pretrage";
    private static final String COLUMN_TIP = "tip";
    private static final String COLUMN_INTERVAL = "interval";
    //private static final String COLUMN_ALARM_ID = "interval";


    public DatabaseClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ALARMI_TABLE = "CREATE TABLE " + TABLE_ALARMI + "(" + COLUMN_GENID + " TEXT," + COLUMN_INTERVAL + " TEXT" + ");";
        db.execSQL(CREATE_ALARMI_TABLE);

        String CREATE_APARTMENTS_TABLE = "CREATE TABLE " + TABLE_NOVI_STANOVI + "(" + COLUMN_GENID + " TEXT," + COLUMN_ID + " TEXT," + COLUMN_LINK + " TEXT," + COLUMN_DESCRIPTION + " TEXT," + COLUMN_PRIZE + " TEXT," + COLUMN_DATETM + " TEXT" + ");";
        db.execSQL(CREATE_APARTMENTS_TABLE);

        String CREATE_PRETRAGE_TABLE = "CREATE TABLE " + TABLE_PRETRAGE + "(" + COLUMN_GENID + " TEXT," + COLUMN_PRETRAGE + " TEXT," + COLUMN_TIP + " TEXT" + ");";
        db.execSQL(CREATE_PRETRAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOVI_STANOVI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRETRAGE);

        // Creating tables again
        onCreate(db);
    }


    public void addFlat(FlatAdvertismentClass flat, String generalId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GENID, generalId);
        values.put(COLUMN_ID, flat.getId());
        values.put(COLUMN_LINK, flat.getLink());
        values.put(COLUMN_DESCRIPTION, flat.getDescription());
        values.put(COLUMN_PRIZE, flat.getPrize());
        values.put(COLUMN_DATETM, flat.getDtm());
        // Inserting Row
        db.insert(TABLE_NOVI_STANOVI, null, values);
        db.close(); // Closing database connection
    }


    public void addSearch(SearchClass pretraga) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GENID, pretraga.getGeneralId());
        values.put(COLUMN_PRETRAGE, pretraga.getSearch());
        values.put(COLUMN_TIP, pretraga.getType());

        // Inserting Row
        db.insert(TABLE_PRETRAGE, null, values);
        db.close(); // Closing database connection
    }


    public void addAlarm(AlarmClass alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GENID, alarm.getGeneralid());
        values.put(COLUMN_INTERVAL, alarm.getInterval());

        // Inserting Row
        db.insert(TABLE_ALARMI, null, values);
        db.close(); // Closing database connection
    }


    // Deleting alarm
    public void deleteAlarm(String generalid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARMI, COLUMN_GENID + "=" + generalid, null);
        db.close();
    }

    // Deleting apartment
    public void deleteFlat(String generalid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOVI_STANOVI, COLUMN_GENID + "=" + generalid, null);
        db.close();
    }

    // Deleting pretrage on pretraga
    public void deleteSearch(String pretraga) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRETRAGE, COLUMN_PRETRAGE + "='" + pretraga + "'", null);
        db.close();
    }

    // Deleting pretrage on pretraga
    public void deleteSearchByGenId(String generalid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRETRAGE, COLUMN_GENID + "='" + generalid + "'", null);
        db.close();
    }


    // Getting All Apartments by generalid
    public List<FlatAdvertismentClass> getAllApartments(String generalid) {
        List<FlatAdvertismentClass> fldataList = new ArrayList<>();
        // Select All Query

        //Log.d("get","get apatment at"+generalid);
        String selectQuery = "SELECT * FROM " + TABLE_NOVI_STANOVI + " WHERE " + COLUMN_GENID + "=" + generalid;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

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

        cursor.close();
        db.close();
        // return apartments list
        return fldataList;
    }


    // Getting All Alarms
    public List<AlarmClass> getAllAlarms() {
        List<AlarmClass> alList = new ArrayList<>();
        // Select All Query

        //Log.d("get","get apatment at"+generalid);
        String selectQuery = "SELECT  * FROM " + TABLE_ALARMI;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

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

        cursor.close();
        db.close();
        // return apartments list
        return alList;
    }






    // Getting All pretrage
    public List<SearchClass> getSearchByGenID(String genid) {
        List<SearchClass> prdataList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PRETRAGE + " WHERE " + COLUMN_GENID + "='" + genid + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

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

        cursor.close();
        db.close();
        // return apartments list
        return prdataList;
    }

    // Getting All pretrage
    public List<SearchClass> getSearchBySearch(String search) {
        List<SearchClass> prdataList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PRETRAGE + " WHERE " + COLUMN_PRETRAGE + "='" + search + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

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

        cursor.close();
        db.close();
        // return apartments list
        return prdataList;
    }

    // Getting All pretrage
    public List<SearchClass> getAllSearch() {
        List<SearchClass> prdataList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PRETRAGE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

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

        cursor.close();
        db.close();
        // return apartments list
        return prdataList;
    }



    boolean isApartmentsTableEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_NOVI_STANOVI;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        mcursor.close();
        db.close();
        return icount <= 0;
    }

    public boolean isPretrageTableEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_PRETRAGE;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        mcursor.close();
        db.close();
        return icount <= 0;
    }


}



