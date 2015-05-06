package edu.mecc.jesse.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Jesse Deaton
 * Final Exam
 * May 5, 2015
 */

public class DBAdapter {

    private final Context context;
    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    private static final String TAG = "DBAdapter";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_STUDENTNUM = "studentnum";
    public static final String KEY_FAVCOLOR = "favcolor";

    public static final int COL_ROWID = 0;
    public static final int COL_NAME = 1;
    public static final int COL_STUDENTNUM = 2;
    public static final int COL_FAVCOLOR = 3;

    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_NAME, KEY_STUDENTNUM, KEY_FAVCOLOR};

    public static final String DATABASE_NAME  = "MyDB";
    public static final String DATABASE_TABLE = "mainTable";
    public static final int DATABSE_VERSION = 2;
    private static final String DATABASE_CREATE_SQL = "create table " + DATABASE_TABLE + " (" +
                                                      KEY_ROWID + " integer primary key autoincrement, " +
                                                      KEY_NAME + " text not null, " + KEY_STUDENTNUM +
                                                      " integer not null, " + KEY_FAVCOLOR + " string not null"
                                                      + ");";
    public DBAdapter (Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        myDBHelper.close();
    }

    public long insertRow(String name, int studentNum, String favColor) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_STUDENTNUM, studentNum);
        initialValues.put(KEY_FAVCOLOR, favColor);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteRow(long rowID) {
        String where = KEY_ROWID + "=" + rowID;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    public void deleteAll() {
        Cursor c = getAllRows();
        long rowID = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowID));
            }
            while (c.moveToNext());
        }
        c.close();
    }

    public Cursor getAllRows() {
        String where = null;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getRow(long rowID) {
        String where = KEY_ROWID + "=" + rowID;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);

        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public boolean updateRow(long rowID, String name, int studenNum, String favColor) {
        String where = KEY_ROWID + "=" + rowID;

        ContentValues newValues = new ContentValues();
        newValues.put(KEY_NAME, name);
        newValues.put(KEY_STUDENTNUM, studenNum);
        newValues.put(KEY_FAVCOLOR, favColor);

        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABSE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading applications's database from version " + oldVersion + " to "
                       + newVersion + ", which will destroy all old data!");
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            onCreate(_db);
        }
    }

}
