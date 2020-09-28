package com.myapp.riddle.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    private SQLiteDatabase sqLiteDatabase;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
        getReadableDatabase();
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase mydb) {
        System.out.println("hello");
        createtable(mydb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void createtable(SQLiteDatabase mydb)
    {
        String query="create table if not exists riddles(id integer PRIMARY KEY,question text NOT NULL,answer text NOT NULL);";
        mydb.execSQL(query);
        Log.i(TAG, "SQLITE:Riddles table created");
        query="create table if not exists userinfo(id integer PRIMARY KEY,name text,start integer,level integer,score integer,pic integer);";
        mydb.execSQL(query);
        Log.i(TAG,"SQLITE:User table created");

    }


    public void insertdata()
    {
        sqLiteDatabase=this.getWritableDatabase();
        try {
            String query = "insert into userinfo values(1,'',0,1,0,0);";
            sqLiteDatabase.execSQL(query);
            Log.i(TAG,"SQLITE:User table initialised");
        }
        catch (Exception e){
            Log.e(TAG, "Error while initialising user table" );;}
    }

    public void insertrows(int id,String question,String answer)
    {
        sqLiteDatabase=this.getWritableDatabase();
        String query="insert into riddles values("+id+",\""+question+"\",\""+answer+"\");";
        sqLiteDatabase.execSQL(query);
        Log.i(TAG, "Riddles: "+id+" inserted");
    }

    public Cursor getdata(int  id)
    {
        sqLiteDatabase=this.getReadableDatabase();
        Cursor res =  sqLiteDatabase.rawQuery("select * from riddles where id="+id,null);
        res.moveToFirst();
        return res;
    }


    public String getfromdb(String column)
    {
        sqLiteDatabase=getReadableDatabase();
        String query="select "+column+" from userinfo";
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public void updateuserinfo(String column,int value)
    {
        sqLiteDatabase=this.getWritableDatabase();
        String query="update userinfo set "+column+"="+value+" where id=1;";
        sqLiteDatabase.execSQL(query);
        System.out.println(column+" updated to "+value);
    }


    public void updateusername(String column,String name)
    {
        sqLiteDatabase=this.getWritableDatabase();
        String query="update userinfo set "+column+"='"+name+"' where id=1;";
        sqLiteDatabase.execSQL(query);
       Log.i(TAG,column+" updated to "+name);
    }


    public boolean checkfornewuser()
    {
        sqLiteDatabase=getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select start from userinfo",null);
        cursor.moveToFirst();
        if(cursor.getInt(0)==0)
            return true;
        else
            return false;

    }

}
