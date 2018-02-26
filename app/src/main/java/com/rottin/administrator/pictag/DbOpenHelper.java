package com.rottin.administrator.pictag;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2017/8/14.
 */

public class DbOpenHelper extends SQLiteOpenHelper{

    public DbOpenHelper(Context context) {
        super(context, "historyDB1", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table history1 (_id integer primary key autoincrement, time varchar(32), picid varchar(32)," +
                "t1 varchar(32), t2 varchar(32), t3 varchar(32), t4 varchar(32) , uid varchar(32) )");
        db.execSQL("create table history2 (_id integer primary key autoincrement, time varchar(32), picid varchar(32)," +
                "t1 varchar(32), t2 varchar(32), t3 varchar(32), t4 varchar(32), selected varchar(5) , uid varchar(32) )");
        db.execSQL("create table history3 (_id integer primary key autoincrement, time varchar(32), picid1 varchar(32)," +
                "picid2 varchar(32), picid3 varchar(32), picid4 varchar(32), t varchar(32), selected varchar(5) , uid varchar(32) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        Log.i("old",""+oldVersion);
//        Log.i("new",""+newVersion);
//        if(newVersion > oldVersion){
//            db.execSQL("ALTER TABLE history1 ADD uid varchar(32) default 'null';");
//            db.execSQL("ALTER TABLE history2 ADD uid varchar(32) default 'null';");
//            db.execSQL("ALTER TABLE history3 ADD uid varchar(32) default 'null';");
//        }


    }
}
