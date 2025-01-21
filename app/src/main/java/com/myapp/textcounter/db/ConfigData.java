package com.myapp.textcounter.db;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConfigData extends SQLiteOpenHelper {

    // データーベースのバージョン
    private static final int DATABASE_VERSION = 1;
    // データーベース名
    private static final String DATABASE_NAME = "ConfigData.db";
    private static final String _ID = "_id";
    private static final String TABLE_NAME = "themeModeData";
    private static final String APP_THEMES_FLAGS = "flag";
       /*
    private static final String APP_THEMES_ID = "checkd";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " TEXT PRIMARY KEY," + APP_THEMES_FLAGS + " TEXT," + APP_THEMES_ID + " INTEGER DEFAULT 3)";
    */

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " TEXT PRIMARY KEY," + APP_THEMES_FLAGS + " TEXT)";
    private static final String TABLE_NAME2 = "watcherModeData";
    private static final String WATCHER_MODE = "value";
    private static final String TEXTBOX_WATCHER_MODE = "CREATE TABLE " + TABLE_NAME2 + " (" +
            _ID + " TEXT PRIMARY KEY," + WATCHER_MODE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public ConfigData(Context context) {super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);//テーマ用
        db.execSQL(TEXTBOX_WATCHER_MODE);//Watcher用
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(TEXTBOX_WATCHER_MODE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}