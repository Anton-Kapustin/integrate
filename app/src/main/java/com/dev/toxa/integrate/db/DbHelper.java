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

import java.util.HashMap;
import java.util.Map;

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
    private static final String LIKE = " LIKE ";
    private static final String AND = " AND ";
    private static final String OR = " OR ";
    private static final String QUOTES = "\"";
    private static final String SQL_CREATE_DATA =   "CREATE TABLE " +
            dbEntry.TABLE_NAME + " (" +
            dbEntry._ID + " INTEGER PRIMARY KEY," +
            dbEntry.SERVER_NAME + TEXT_TYPE + COMMA_SEP +
            dbEntry.IP_ADDRESS + TEXT_TYPE + COMMA_SEP +
            dbEntry.MAC_ADDRESS + TEXT_TYPE + COMMA_SEP +
            dbEntry.DISTR_NAME + TEXT_TYPE + COMMA_SEP +
            dbEntry.FAVORITE + INTAGER_TYPE + COMMA_SEP +
            dbEntry.IN_USE + INTAGER_TYPE + " )";

    private static final String SQL_CREATE_SETTINGS = "CREATE TABLE " +
            settingsEntry.TABLE_NAME + " (" +
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

    public int addToBaseServer(String IP, String serverName, String macAdress, String distr) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        SQLiteDatabase db = this.getWritableDatabase();
        String[] selectColumns = {dbEntry._ID, dbEntry.IP_ADDRESS};
        String selection = dbEntry.IP_ADDRESS + LIKE + QUOTES + IP + QUOTES + AND + dbEntry.MAC_ADDRESS + LIKE + QUOTES + macAdress + QUOTES;
        Log.d(LOG_TAG, "selection: " + selection);
        Cursor cursor = db.query(dbEntry.TABLE_NAME, selectColumns, selection, null, null, null, null);
        cursor.moveToFirst();
        int ID  = 0;
        ContentValues contentValues = new ContentValues();
        Log.d(LOG_TAG, "cursor count: " + cursor.getCount());
        if (cursor.getCount() == 0) {
            contentValues.put(dbEntry.IP_ADDRESS, IP);
            contentValues.put(dbEntry.SERVER_NAME, serverName);
            contentValues.put(dbEntry.MAC_ADDRESS, macAdress);
            contentValues.put(dbEntry.DISTR_NAME, distr);
            contentValues.put(dbEntry.FAVORITE, 0);
            contentValues.put(dbEntry.IN_USE, 0);
            db.insert(dbEntry.TABLE_NAME, null, contentValues);
            ID = 1;
        } else {
            contentValues.put(dbEntry.SERVER_NAME, serverName);
            db.update(dbEntry.TABLE_NAME, contentValues, null, null);
            ID = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        Log.d(LOG_TAG, "ID from db: " + ID);
        return ID;
    }

    public Map getServerData(int serverID) {
        Map serverData = new HashMap();

        SQLiteDatabase db = this.getWritableDatabase();
        String [] columns = {dbEntry._ID, dbEntry.IP_ADDRESS, dbEntry.SERVER_NAME, dbEntry.DISTR_NAME, dbEntry.FAVORITE,
                dbEntry.MAC_ADDRESS, dbEntry.IN_USE};
        String selection = dbEntry._ID + LIKE + serverID;
        Log.d(LOG_TAG, "selection: " + selection);
        Cursor cursor = db.query(dbEntry.TABLE_NAME, columns,selection,null,null,null,null);
        cursor.moveToFirst();
        if (cursor.getCount() != 0) {
            serverData.put("IP", cursor.getString(cursor.getColumnIndex(dbEntry.IP_ADDRESS)));
            serverData.put("serverName", cursor.getString(cursor.getColumnIndex(dbEntry.SERVER_NAME)));
            serverData.put("distr", cursor.getString(cursor.getColumnIndex(dbEntry.DISTR_NAME)));
            serverData.put("favorite", cursor.getInt(cursor.getColumnIndex(dbEntry.FAVORITE)));
            serverData.put("macAddress", cursor.getString(cursor.getColumnIndex(dbEntry.MAC_ADDRESS)));
            Log.d(LOG_TAG, "IP from db: " + cursor.getString(cursor.getColumnIndex(dbEntry.IP_ADDRESS)));
            ContentValues contentValues = new ContentValues();
            contentValues.put(dbEntry.IN_USE, 1);
            db.update(dbEntry.TABLE_NAME, contentValues, "_id" + LIKE +
                    cursor.getInt(cursor.getColumnIndex(dbEntry._ID)), null);
        } else {
            serverData.put("IP", "0");
        }
        cursor.close();
        db.close();
        return serverData;
    }

    public void changeFavorite(boolean isFavorite, String IP, String macAddress) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {dbEntry._ID, dbEntry.FAVORITE};
        String selection = dbEntry.IP_ADDRESS + LIKE + QUOTES + IP + QUOTES + AND +
                dbEntry.MAC_ADDRESS + LIKE + QUOTES + macAddress + QUOTES;
        Cursor cursor = db.query(dbEntry.TABLE_NAME, columns, selection,  null, null, null, null );
        if (cursor.moveToFirst()) {
            if (cursor.getCount() != 0) {
                ContentValues contentValues = new ContentValues();
                if (isFavorite) {
                    contentValues.put(dbEntry.FAVORITE, 1);
                } else {
                    contentValues.put(dbEntry.FAVORITE, 0);
                }
                Log.d(LOG_TAG, "ID: " + cursor.getInt(cursor.getColumnIndex(dbEntry._ID)));
                db.update(dbEntry.TABLE_NAME, contentValues, "_id" + LIKE +
                        cursor.getInt(cursor.getColumnIndex(dbEntry._ID)), null);
            }
        }
        cursor.close();
        db.close();
    }

    public void changeInUse(boolean isINUse, String IP, String macAddress) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {dbEntry._ID, dbEntry.IN_USE};
        String selection = dbEntry.IP_ADDRESS + LIKE + QUOTES + IP + QUOTES + AND +
                dbEntry.MAC_ADDRESS + LIKE + QUOTES + macAddress + QUOTES;
        Cursor cursor = db.query(dbEntry.TABLE_NAME, columns, selection,  null, null, null, null );
        if (cursor.moveToFirst()) {
            if (cursor.getCount() != 0) {
                ContentValues contentValues = new ContentValues();
                if (isINUse) {
                    contentValues.put(dbEntry.IN_USE, 1);
                } else {
                    contentValues.put(dbEntry.IN_USE, 0);
                }
                Log.d(LOG_TAG, "ID: " + cursor.getInt(cursor.getColumnIndex(dbEntry._ID)));
                db.update(dbEntry.TABLE_NAME, contentValues, "_id" + LIKE +
                        cursor.getInt(cursor.getColumnIndex(dbEntry._ID)), null);
            }
        }
        cursor.close();
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
        cursor.close();
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
        cursor.close();
        Log.d(LOG_TAG, "notify is " + enable);
        if (enable == 0) {
            return false;
        } else {
            return true;
        }
    }
}
