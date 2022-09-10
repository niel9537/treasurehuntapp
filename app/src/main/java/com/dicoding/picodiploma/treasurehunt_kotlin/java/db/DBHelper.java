package com.dicoding.picodiploma.treasurehunt_kotlin.java.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.CurrentFlow;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper (Context context) {
        super(context, "manohara.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE manoharaFlow(id INTEGER PRIMARY KEY, flow_order TEXT, post_id TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS manoharaFlow");
    }


    public boolean insertFlow (String flowOrder, String postId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("flow_order", flowOrder);
        cv.put("post_id", postId);
        return db.insert("manoharaFlow", null, cv) > 0;
    }


    public Cursor getAllFlow () {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("Select * from " + "manoharaFlow", null);
    }

    public boolean updateFlow(CurrentFlow nf, int id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("flow_order", nf.getFlowOrder());
        cv.put("post_id", nf.getPostId());
        return db.update("manoharaFlow", cv, "id" + "=" + id,
                null) > 0;
    }

    public void deleteFlow (int id) {
        SQLiteDatabase db = getReadableDatabase();
        db.delete("manoharaFlow", "id" + "=" + id, null);
    }

    public void deleteAllFlow () {
        SQLiteDatabase db = getReadableDatabase();
        db.delete("manoharaFlow", null, null);
    }

    public boolean findFlow(String flowOrder, String postId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("manoharaFlow",// Selecting Table
                new String[]{"flow_order", "post_id"},//Selecting columns want to query
                "flow_order" + " =? AND post_id "+" =?",
                new String[]{flowOrder,postId},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            //if cursor has value then in user database there is user associated with this given email so return true
            return true;
        }

        //if email does not exist return false
        return false;
        //return db.rawQuery("Select * from " + "manoharaFlow" +" where "+" flow order = "+flowOrder+" and "+" post_id = "+ postId, null) > 0;
    }

    public Cursor getPreviousFlow () {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM manoharaFlow ORDER BY id DESC LIMIT 1", null);
    }

}