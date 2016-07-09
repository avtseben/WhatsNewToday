package ru.alexandertsebenko.whatsnewtoday.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WntSqliteHelper extends SQLiteOpenHelper{

    public static final String DB_NAME = "wnt";
    public static final int DB_VERISON = 1;

    public static final String WNT_TABLE = "WNT";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_WNT_DATE = "WNT_DATE";
    public static final String COLUMN_WNT_TEXT = "WNT_TEXT";

    public WntSqliteHelper(Context context) {
        super(context,DB_NAME,null,DB_VERISON);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + WNT_TABLE + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_WNT_DATE + " TEXT, "
                + COLUMN_WNT_TEXT + " TEXT);");
        Log.d("DBHepler_log", "onCreate");
    }
    public void insertNew(SQLiteDatabase db, String date, String text) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WNT_DATE, date);
        values.put(COLUMN_WNT_TEXT, text);
        db.insert(WNT_TABLE,null,values);
    }
    public void updateTextByDate(SQLiteDatabase db, String date, String text) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WNT_TEXT, text);
        db.update(WNT_TABLE,
                values,
                COLUMN_WNT_DATE + " = ?", new String[] {date});
    }
    public String findNoteByDate(SQLiteDatabase db, String date) {
/*        Cursor cursor = db.query(WNT_TABLE,
                new String [] {COLUMN_WNT_TEXT},
                COLUMN_WNT_DATE,
                new String[]{date},
                null,null,null);*/
        Cursor cursor = db.query(WNT_TABLE,
                new String [] {COLUMN_WNT_TEXT},
                null,null,null,null,null);
        String result = cursor.getString(0);
        cursor.close();
        db.close();
        return result;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
