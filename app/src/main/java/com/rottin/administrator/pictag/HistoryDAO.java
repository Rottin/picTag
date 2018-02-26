package com.rottin.administrator.pictag;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.rottin.administrator.pictag.domain.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */

public class HistoryDAO {
    private DbOpenHelper helper;
    private SimpleDateFormat format;
    final static private String TAG = "HistoryDAO";

    public HistoryDAO(Context context) {
        helper = new DbOpenHelper(context);
    }

    private String getTimeStr() {
        format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String timeStr = format.format(curDate);
        Log.d(TAG, "Saved time:" + timeStr);
        return timeStr;
    }

    public void insert1(String picid, String t1, String t2, String t3, String t4, String uid) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String curTime = getTimeStr();
        db.execSQL("INSERT INTO history1 (time, picid," +
                "t1 , t2, t3 , t4  , uid) " +
                " values(?,?,?,?,?,?,?)", new Object[]{curTime, picid, t1, t2, t3, t4, uid});
        db.close();
    }

    public void insert2(String picid, String t1, String t2, String t3, String t4, String selected, String uid) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String curTime = getTimeStr();
        db.execSQL("INSERT INTO history2 (time , picid ," +
                "t1 , t2 , t3 , t4 , selected , uid) " +
                " values(?,?,?,?,?,?,?,?)", new Object[]{curTime, picid, t1, t2, t3, t4, selected, uid});
        db.close();
    }

    public void insert3(String picid1, String picid2, String picid3, String picid4, String t, String selected, String uid) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String curTime = getTimeStr();
        db.execSQL("INSERT INTO history3 (time, picid1 ," +
                "picid2, picid3, picid4, t, selected , uid) " +
                " values(?,?,?,?,?,?,?,?)", new Object[]{curTime, picid1, picid2, picid3, picid4, t, selected, uid});
        db.close();
    }

    public List<Exercise1> findAll1(String uid) {
        List<Exercise1> exercise1s = new ArrayList<Exercise1>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, time, picid, t1, t2, t3, t4 FROM history1 WHERE uid = '"+uid+"'", null);
        while (cursor.moveToNext()) {
            Exercise1 exercise1 = new Exercise1();
            exercise1.setId(cursor.getInt(0));
            exercise1.setTime(cursor.getString(1));
            exercise1.setPicid(cursor.getString(2));
            exercise1.setT1(cursor.getString(3));
            exercise1.setT2(cursor.getString(4));
            exercise1.setT3(cursor.getString(5));
            exercise1.setT4(cursor.getString(6));
            exercise1s.add(exercise1);
        }
        cursor.close();
        db.close();
        return exercise1s;
    }

    public List<Exercise2> findAll2(String uid) {
        List<Exercise2> exercise2s = new ArrayList<Exercise2>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, time, picid, t1, t2, t3, t4, selected FROM history2 WHERE uid = '"+uid+"'", null);
        while (cursor.moveToNext()) {
            Exercise2 exercise2 = new Exercise2();
            exercise2.setId(cursor.getInt(0));
            exercise2.setTime(cursor.getString(1));
            exercise2.setPicid(cursor.getString(2));
            exercise2.setT1(cursor.getString(3));
            exercise2.setT2(cursor.getString(4));
            exercise2.setT3(cursor.getString(5));
            exercise2.setT4(cursor.getString(6));
            exercise2.setSelected(cursor.getString(7));
            exercise2s.add(exercise2);
        }
        cursor.close();
        db.close();
        return exercise2s;
    }

    public List<Exercise3> findAll3(String uid) {
        List<Exercise3> exercise3s = new ArrayList<Exercise3>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, time, picid1, picid2, picid3, picid4, t, selected FROM history3  WHERE uid = '"+uid+"'", null);
        while (cursor.moveToNext()) {
            Exercise3 exercise3 = new Exercise3();
            exercise3.setId(cursor.getInt(0));
            exercise3.setTime(cursor.getString(1));
            exercise3.setPicid1(cursor.getString(2));
            exercise3.setPicid2(cursor.getString(3));
            exercise3.setPicid3(cursor.getString(4));
            exercise3.setPicid4(cursor.getString(5));
            exercise3.setT(cursor.getString(6));
            exercise3.setSelected(cursor.getString(7));
            exercise3s.add(exercise3);
        }
        cursor.close();
        db.close();
        return exercise3s;
    }

    public Exercise1 getInfoById1(String id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        int idNum = Integer.parseInt(id);
        Cursor cursor = db.rawQuery("SELECT _id, time, picid, t1, t2, t3, t4 FROM history1 WHERE _id=? ", new String[]{id});
        cursor.moveToNext();
        Exercise1 exercise1 = new Exercise1();
        exercise1.setId(idNum);
        exercise1.setTime(cursor.getString(1));
        exercise1.setPicid(cursor.getString(2));
        exercise1.setT1(cursor.getString(3));
        exercise1.setT2(cursor.getString(4));
        exercise1.setT3(cursor.getString(5));
        exercise1.setT4(cursor.getString(6));
        return exercise1;
    }

    public Exercise2 getInfoById2(String id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        int idNum = Integer.parseInt(id);
        Cursor cursor = db.rawQuery("SELECT _id, time, picid, t1, t2, t3, t4, selected FROM history2 WHERE _id=? ", new String[]{id});
        cursor.moveToNext();
        Exercise2 exercise2 = new Exercise2();
        exercise2.setId(idNum);
        exercise2.setTime(cursor.getString(1));
        exercise2.setPicid(cursor.getString(2));
        exercise2.setT1(cursor.getString(3));
        exercise2.setT2(cursor.getString(4));
        exercise2.setT3(cursor.getString(5));
        exercise2.setT4(cursor.getString(6));
        exercise2.setSelected(cursor.getString(7));
        return exercise2;
    }
    public Exercise3 getInfoById3(String id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        int idNum = Integer.parseInt(id);
        Cursor cursor = db.rawQuery("SELECT _id, time, picid1, picid2, picid3, picid4, t, selected FROM history3 WHERE _id=? ", new String[]{id});
        cursor.moveToNext();
        Exercise3 exercise3 = new Exercise3();
        exercise3.setId(idNum);
        exercise3.setTime(cursor.getString(1));
        exercise3.setPicid1(cursor.getString(2));
        exercise3.setPicid2(cursor.getString(3));
        exercise3.setPicid3(cursor.getString(4));
        exercise3.setPicid4(cursor.getString(5));
        exercise3.setT(cursor.getString(6));
        exercise3.setSelected(cursor.getString(7));
        return exercise3;
    }
    public void deleteById(String id, String type){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM history"+type+" WHERE _id=? ", new String[]{id});
        db.close();
    }
    public void deleteAll(){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM history1 ");
        db.execSQL("DELETE FROM history2 ");
        db.execSQL("DELETE FROM history3 ");
        db.close();
    }
}
