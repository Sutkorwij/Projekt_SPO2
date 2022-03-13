package com.example.projekt_test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "user.db";
    public static final String TABLE_NAME = "user_table";
    public static final String ID = "ID";
    public static final String COL_1 = "NAME";
    public static final String COL_2 = "SEX";
    public static final String COL_3 = "AGE";
    public static final String COL_4 = "HEIGHT";
    public static final String COL_5 = "WEIGHT";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                                                      +COL_1+" TEXT," + COL_2+ " TEXT,"
                                                      +COL_3+" INT," + COL_4+ " INT,"
                                                      +COL_5+" DOUBLE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String sex, int age, int height, double weight){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, name);
        contentValues.put(COL_2, sex);
        contentValues.put(COL_3, age);
        contentValues.put(COL_4, height);
        contentValues.put(COL_5, weight);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public boolean updateData(String id, String name, String sex, int age, int height, double weight){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, name);
        contentValues.put(COL_2, sex);
        contentValues.put(COL_3, age);
        contentValues.put(COL_4, height);
        contentValues.put(COL_5, weight);
        long result = db.update(TABLE_NAME, contentValues, "ID = ?", new String[] {id});
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Integer deleteData (String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",  new String[] {id});
    }

    public void deleteAllData (){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = this.getAllData();
        res.getCount();
        StringBuffer buffer = new StringBuffer();
        res.moveToLast();
        int last_ID = Integer.parseInt(res.getString(0));
        for(int i = 1; i<last_ID+1;i++){
            this.deleteData(String.valueOf(i));
        }
    }
}