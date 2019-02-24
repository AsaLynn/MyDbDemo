package com.zxn.dbdemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        dbHelper = MySQLiteDBHelper.factory(getApplicationContext());
        writableDatabase = dbHelper.getWritableDatabase();
        readableDatabase = dbHelper.getReadableDatabase();

    }

    @OnClick({R.id.btn_insert, R.id.btn_update, R.id.btn_select})
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
                //update(String table, ContentValues values, String whereClause, String[] whereArgs)+
                ContentValues values = new ContentValues();
                values.put(MySQLiteDBHelper.TODAY, "2019" + 10);
                values.put(MySQLiteDBHelper.DATE, 10);
                values.put(MySQLiteDBHelper.STEP, 100);
                String whereClause = "";
                String[] whereArgs = {};
                //writableDatabase.update(MySQLiteDBHelper.TABLE_NAME, values, );
                break;
            case R.id.btn_select:
                //rawQuery(String sql, String[] selectionArgs)
                String sql = "";
                String[] selectionArgs = {};
                Cursor cursor = readableDatabase.rawQuery(sql, selectionArgs);
                while (cursor.moveToNext()) {
                    String string = cursor.getString(0);
                    Log.i(TAG, "onViewClicked: " + string);
                }
                break;
        }
    }
}
