package com.kc.notesmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DatabaseHelper  extends SQLiteOpenHelper {

    public  static final String DATABSE_NAME="NOTES.db";
    public  static final String TABLE_NAME="Notesfile";
    public  static final String COL_ID="ID";
    public  static final String COL_IMAGE="IMAGE";
    public  static final String COL_TITLE="TITLE";
    public  static final String COL_SUBJECT="SUBJECT";
    public  static final String COL_LOCATION="LOCATION";
    public  static final String COL_DATE="DATE";
    public  static final String COL_TIME="TIME";
    public  static final String COL_NOTES="NOTES";
    ByteArrayOutputStream arrayOutputStream;
    ByteArrayOutputStream arrayOutputStream2;
    private byte[] imageinbyte;
    private byte[] imageinbyte2;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABSE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Notesfile(ID INTEGER PRIMARY KEY AUTOINCREMENT,IMAGE BLOB,TITLE TEXT,SUBJECT TEXT,LOCATION TEXT," +
                "DATE TEXT,TIME TEXT,NOTES TEXT )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }


    public  boolean insertdata(Bitmap image,String title, String subject, String notes, String location, String date, String time)
    {
        SQLiteDatabase db=this.getWritableDatabase();

       Bitmap imageToStoreBitmmap=image;
       arrayOutputStream =new ByteArrayOutputStream();
        imageToStoreBitmmap.compress(Bitmap.CompressFormat.JPEG,100,arrayOutputStream);
        imageinbyte=arrayOutputStream.toByteArray();

        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_IMAGE,imageinbyte);
        contentValues.put(COL_TITLE,title);
        contentValues.put(COL_SUBJECT,subject);
        contentValues.put(COL_NOTES,notes);
        contentValues.put(COL_LOCATION,location);
        contentValues.put(COL_DATE,date);
        contentValues.put(COL_TIME,time);

        long l=db.insert(TABLE_NAME,null,contentValues);
        if(l==-1)
        {
            return  false;
        }
        else
        {
            return true;

        }
    }
   public  Cursor readdta()
   {
       SQLiteDatabase db=this.getReadableDatabase();
       Cursor cursor=db.rawQuery("select * from Notesfile",null);
       return cursor;
       }



    public  boolean upadtedata(String ID,Bitmap image,String title, String subject,String notes,String location,String date,String time) {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues contentValues = new ContentValues();
        Bitmap imageToStoreBitmmap=image;
        arrayOutputStream2 =new ByteArrayOutputStream();
        imageToStoreBitmmap.compress(Bitmap.CompressFormat.JPEG,100,arrayOutputStream2);
        imageinbyte2=arrayOutputStream2.toByteArray();


        contentValues.put(COL_ID, ID);
        contentValues.put(COL_IMAGE,imageinbyte2);
        contentValues.put(COL_TITLE, title);
        contentValues.put(COL_SUBJECT, subject);
        contentValues.put(COL_NOTES, notes);
        contentValues.put(COL_LOCATION, location);
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_TIME, time);

        db.update(TABLE_NAME,contentValues,"ID=?",new String[]{ID});
        return  true;
    }

    public long deletedata(String id)
    {
        SQLiteDatabase db=this.getReadableDatabase();

        return db.delete(TABLE_NAME,"ID=?",new String[]{id});


    }

}
