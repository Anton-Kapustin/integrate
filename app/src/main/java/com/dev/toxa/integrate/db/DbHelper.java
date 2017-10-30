package com.dev.toxa.integrate.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.dev.toxa.integrate.LoggingNameClass;
import com.dev.toxa.integrate.db.DbReaderContract.dbEntry;
import com.dev.toxa.integrate.db.DbReaderContract.settingsEntry;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

//=================================Переменные==============================
    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";
    private static final String TEXT_TYPE = " TEXT";
    private static final String DATABASE_NAME = "IntegrateDB";
    private static final String INTAGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_DATA =   "CREATE TABLE " + dbEntry.TABLE_NAME + " (" +
            dbEntry._ID + " INTEGER PRIMARY KEY," +
            dbEntry.SERVER_NAME + TEXT_TYPE + COMMA_SEP +
            dbEntry.IP_ADDRESS + TEXT_TYPE + COMMA_SEP + dbEntry.DISTR_NAME + TEXT_TYPE + COMMA_SEP +
            dbEntry.FAVORITE + INTAGER_TYPE + COMMA_SEP + dbEntry.LAST_USE + TEXT_TYPE + " )";

    private static final String SQL_CREATE_SETTINGS = "CREATE TABLE " + settingsEntry.TABLE_NAME + " (" +
            settingsEntry.NOTIFY_ENABLED + INTAGER_TYPE + " )";
//=========================================================================

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        sqLiteDatabase.execSQL(SQL_CREATE_DATA);
        sqLiteDatabase.execSQL(SQL_CREATE_SETTINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addToBaseServer(String IP, String serverName, String distr) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        SQLiteDatabase db = this.getWritableDatabase();
        String[] selectColumns = {dbEntry.IP_ADDRESS};
        Cursor cursor = db.query(dbEntry.TABLE_NAME, selectColumns, dbEntry.IP_ADDRESS + " LIKE " + IP, null, null, null, null);
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbEntry.IP_ADDRESS, IP);
        contentValues.put(dbEntry.SERVER_NAME, serverName);
        contentValues.put(dbEntry.DISTR_NAME, distr);
        if (cursor.getCount() == 0) {
            db.insert(dbEntry.TABLE_NAME, null, contentValues);
        } else {
            db.update(dbEntry.TABLE_NAME, contentValues, null, null);
        }
        db.close();
    }

    public void addToBaseNotifyEnabled(boolean enable) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        int notifyEnabled = 0;
        if (enable) {
            notifyEnabled = 1;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(settingsEntry.TABLE_NAME, null, null, null, null, null, null);
        ContentValues contentValues = new ContentValues();
        contentValues.put(settingsEntry.NOTIFY_ENABLED, notifyEnabled);
        if (cursor.getCount() == 0) {
            db.insert(settingsEntry.TABLE_NAME, null, contentValues);
        } else {
            db.update(settingsEntry.TABLE_NAME, contentValues, null, null);
        }
        db.close();

    }

    public boolean getNotifyEnabled() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(settingsEntry.TABLE_NAME, null, null, null, null, null, null);
        int enable = 0;
        if (cursor.moveToFirst()) {
            enable = cursor.getInt(cursor.getColumnIndex(settingsEntry.NOTIFY_ENABLED));
        }
        Log.d(LOG_TAG, "notify is " + enable);
        if (enable == 0) {
            return false;
        } else {
            return true;
        }
    }
}
