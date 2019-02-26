package com.zxn.dbdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_insert)
    Button btnInsert;
    @BindView(R.id.btn_update)
    Button btnUpdate;
    @BindView(R.id.btn_select)
    Button btnSelect;
    private MySQLiteDBHelper dbHelper;
    private SQLiteDatabase writableDatabase;
    private String tag = this.getClass().getSimpleName();
    private String TAG = this.getClass().getSimpleName();
    private SQLiteDatabase readableDatabase;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {

                        onCreateDb();

                    }
                });
    }

    private void onCreateDb() {
        dbHelper = MySQLiteDBHelper.factory(this);
        writableDatabase = dbHelper.getWritableDatabase();
        readableDatabase = dbHelper.getReadableDatabase();
    }

    @OnClick({R.id.btn_insert, R.id.btn_update, R.id.btn_update2, R.id.btn_select, R.id.btn_select_one, R.id.btn_select_single})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_insert:
                //insert(String table, String nullColumnHack, ContentValues values)
                for (int i = 0; i < 10; i++) {
                    //String table, String nullColumnHack, ContentValues values
                    ContentValues values = new ContentValues();
                    values.put(MySQLiteDBHelper.TODAY, "2019" + i);
                    values.put(MySQLiteDBHelper.DATE, 0);
                    values.put(MySQLiteDBHelper.STEP, 10 + i);
                    //values.put(MySQLiteDBHelper.TODAY,"");
                    //返回新添记录的行号，与主键id无
                    long line = writableDatabase.insert(MySQLiteDBHelper.TABLE_NAME, null, values);
                    Log.i(tag, "onViewClicked--line: " + line);
                }
                break;
            case R.id.btn_update:
                update();
                break;
            case R.id.btn_update2:
                update2();
                break;
            case R.id.btn_select:
                //rawQuery(String sql, String[] selectionArgs)
                String sql = "select * from " + MySQLiteDBHelper.TABLE_NAME;
                String[] selectionArgs = {};
                Cursor cursor = readableDatabase.rawQuery(sql, selectionArgs);
                while (cursor.moveToNext()) {
                    //String string = cursor.getString(0);
                    //Log.i(TAG, "onViewClicked: " + string);
                    //String string1 = cursor.getString(1);
                    //Log.i(TAG, "onViewClicked: " + string1);
                    String[] columnNames = cursor.getColumnNames();

                    for (String name : columnNames) {
                        int columnIndex = cursor.getColumnIndex(name);
                        String value = cursor.getString(columnIndex);
                        Log.i(TAG, "onViewClicked: " + name + "-->" + value);
                    }
                }
                break;
            case R.id.btn_select_one:
                selectOne();
                break;
            case R.id.btn_select_single:
                selectSingle();
                break;

        }
    }

    private void update2() {
        String sql = "update TodayStepData set step = 500 where _id=(select _id from TodayStepData where today = 20190 order by _id desc limit 1)";
        writableDatabase.execSQL(sql);
    }

    private void update() {

         /*String sql = "select * from " + MySQLiteDBHelper.TABLE_NAME;
                String[] selectionArgs = {};
                Cursor cursor = readableDatabase.rawQuery(sql, selectionArgs);*/

        //update(String table, ContentValues values, String whereClause, String[] whereArgs)+
        ContentValues values = new ContentValues();
        //values.put(MySQLiteDBHelper.TODAY, "2019" + 10);
        //values.put(MySQLiteDBHelper.DATE, 10);
        values.put(MySQLiteDBHelper.STEP, 100);
        String whereClause = "today = ?";
        String[] whereArgs = {"20190"};
        writableDatabase.update(MySQLiteDBHelper.TABLE_NAME, values, whereClause, whereArgs);
    }

    private void selectSingle() {
//        query(String table, String[] columns, String selection,
//                String[] selectionArgs, String groupBy, String having,
//                String orderBy, String limit)
        String orderBy = "_id desc";
        String limit = "1";
        String selection = "today = ?";
        String[] selectionArgs = {"20190"};
        Cursor cursor
                = readableDatabase
                .query(MySQLiteDBHelper.TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        orderBy,
                        limit);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("_id");
            String idValue = cursor.getString(columnIndex);
            int stepIndex = cursor.getColumnIndex("step");
            String stepValue = cursor.getString(stepIndex);
            Log.i(TAG, "onViewClicked: step-->" + stepValue);
            Log.i(TAG, "onViewClicked: idValue-->" + idValue);
        }

        /*while (cursor.moveToNext()) {
            String[] columnNames = cursor.getColumnNames();
            for (String name : columnNames) {
                int columnIndex = cursor.getColumnIndex(name);
                String value = cursor.getString(columnIndex);
                Log.i(TAG, "onViewClicked: " + name + "-->" + value);
            }
        }*/
    }

    private void selectOne() {
        //query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
        String selection = "today = ?";
        String[] selectionArgs = {"20190"};
        Cursor cursor
                = readableDatabase
                .query(MySQLiteDBHelper.TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
        while (cursor.moveToNext()) {
            String[] columnNames = cursor.getColumnNames();
            for (String name : columnNames) {
                int columnIndex = cursor.getColumnIndex(name);
                String value = cursor.getString(columnIndex);
                Log.i(TAG, "onViewClicked: " + name + "-->" + value);
            }
        }
    }


}
