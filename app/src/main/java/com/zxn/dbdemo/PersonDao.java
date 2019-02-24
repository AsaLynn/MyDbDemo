package com.zxn.dbdemo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxn on 2019/2/24.
 */
public class PersonDao implements IDBHelper{

    private MySQLiteDBHelper myDBHelper;
    public String tag = "PersonDao.class";

    //在new出来的时候就实现myDBHelper初始化
    public PersonDao(Context context) {
        myDBHelper = new MySQLiteDBHelper(context);
    }

    //增加
    public void addPerson(String name, String phone) {
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        //先判断数据库是否可用
        if (database.isOpen()) {
            //执行插入操作
            //database.execSQL("insert into person (name,phone) values('"+name+"','"+phone+"')");

            //推荐如下写法
            database.execSQL("insert into person (name,phone) values(?,?)", new Object[]{name, phone});
            database.close();
        }
    }

    //查找
    public boolean findPerson(String phone) {
        boolean result = false;
        SQLiteDatabase database = myDBHelper.getReadableDatabase();
        if (database.isOpen()) {
            //database.execSQL("select * from phone='"+phone+"'");
            Cursor cursor = database.rawQuery("select * from person where phone=?", new String[]{phone});
            if (cursor.moveToFirst()) {//游标是否移动到下一行,如果是,那说明有数据返回
                Log.d(tag, "count:" + cursor.getColumnCount());
                int nameIndex = cursor.getColumnIndex("name");
                Log.d(tag, "name:" + cursor.getString(nameIndex));
                cursor.close();
                result = true;
            } else {
                result = false;

            }
            database.close();
        }
        return result;
    }

    //删除一条数据
    public void deletePerson(String phone) {
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if (database.isOpen()) {
            database.execSQL("delete from person where phone=?", new Object[]{phone});
        }
        database.close();
    }

    //更新一条数据
    public void updatePerson(String phone, String newName, String newPhone) {
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if (database.isOpen()) {
            database.execSQL("update person set name=?,phone=? where phone=?", new Object[]{newName, newPhone, phone});
        }
        database.close();
    }

    //查找所有person
    public List<PersonInfo> findAllPerson(){
        List<PersonInfo> personList = new ArrayList<PersonInfo>();
        SQLiteDatabase database = myDBHelper.getReadableDatabase();
        if(database.isOpen()){
            Cursor cursor = database.rawQuery("select * from person ", null);
            while(cursor.moveToNext()){
                int nameIndex = cursor.getColumnIndex("name");
                int phoneIndex = cursor.getColumnIndex("phone");
                String name = cursor.getString(nameIndex);
                String phone = cursor.getString(phoneIndex);

                PersonInfo  person = new PersonInfo(name,phone);
                Log.d(tag,person.toString());

                personList.add(person);
            }

        }
        database.close();
        return personList;
    }

    @Override
    public void update() {
//        db.execSQL("update lxrData set number=?,introduce=? where name=?",
//                new Object[] { lxr.getNumber(), lxr.getIntroduce(), lxr.getName() });

        SQLiteDatabase writableDatabase = myDBHelper.getWritableDatabase();

        //update(String table, ContentValues values, String whereClause, String[] whereArgs)
        //writableDatabase.update()

    }
}
