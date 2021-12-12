package com.example.shopandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static  final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "contactDb";

    public static final String TABLE_CONTACTS = "contatcs";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "brand";
    public static final String KEY_COST = "cost";

    public static  final String TABLE_USERS = "Users";
    public static  final String KEY_IDUSER = "_id";
    public static  final String KEY_LOGIN = "Login";
    public static  final String KEY_PASSWORD = "Password";
    public static  final String KEY_ROLE = "Role";

    public DBHelper(@Nullable Context context) {super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_CONTACTS + "(" + KEY_ID + " integer primary key," + KEY_NAME + " text, " + KEY_COST+ " text)");
        db.execSQL("create table "+ TABLE_USERS+"("+KEY_IDUSER+" integer primary key,"+KEY_LOGIN + " text,"+KEY_PASSWORD+" text,"+KEY_ROLE+" REAL);");
        db.execSQL("INSERT INTO "+TABLE_USERS+ " ("+KEY_LOGIN + ", "+KEY_PASSWORD+", "+KEY_ROLE+") VALUES ('admin','admin',1);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_CONTACTS);
        db.execSQL("drop table if exists " + TABLE_USERS);
        onCreate(db);
    }
}
