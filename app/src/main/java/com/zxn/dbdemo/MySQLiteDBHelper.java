package com.zxn.dbdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by zxn on 2019/2/24.
 */
public class MySQLiteDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TodayStepDB.db";
    public static final String TABLE_NAME = "TodayStepData";
    private static final String PRIMARY_KEY = "_id";
    public static final String TODAY = "today";
    public static final String DATE = "date";
    public static final String STEP = "step";
    private static final int VERSION = 1;

    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TODAY + " TEXT, "
            + DATE + " long, "
            + STEP + " long);";

    private String tag = "MySQLiteDBHelper";
    private String TAG = "MySQLiteDBHelper";

    /**
     * 创建数据库的构造方法
     * @param context 应用程序上下文
     * name 数据库的名字
     * factory 查询数据库的游标工厂一般情况下用sdk默认的
     * version 数据库的版本一般大于0
     */
    public MySQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    public MySQLiteDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(tag, "onCreate.....");
        //创建一个数据库
        db.execSQL("create table person (personid integer primary key autoincrement ,name varchar(30) ,step int)");
        Log.e(TAG, SQL_CREATE_TABLE);
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(tag,"onUpgrade*******");
        //增加一列
        // db.execSQL("alter table person add phone varchar(13) null");
        //deleteTable();
        //onCreate(db);
    }

    public static MySQLiteDBHelper factory(Context context) {
        return new MySQLiteDBHelper(context);
    }
}
