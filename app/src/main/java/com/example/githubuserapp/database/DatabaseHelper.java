package com.example.githubuserapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static android.provider.BaseColumns._ID;
import static com.example.githubuserapp.database.DatabaseContract.UserColumns.AVATAR_URL;
import static com.example.githubuserapp.database.DatabaseContract.UserColumns.COMPANY;
import static com.example.githubuserapp.database.DatabaseContract.UserColumns.FOLLOWER;
import static com.example.githubuserapp.database.DatabaseContract.UserColumns.FOLLOWING;
import static com.example.githubuserapp.database.DatabaseContract.UserColumns.HTML_URL;
import static com.example.githubuserapp.database.DatabaseContract.UserColumns.LOCATION;
import static com.example.githubuserapp.database.DatabaseContract.UserColumns.NAME;
import static com.example.githubuserapp.database.DatabaseContract.UserColumns.REPOSITORY;
import static com.example.githubuserapp.database.DatabaseContract.UserColumns.TABLE_NAME;
import static com.example.githubuserapp.database.DatabaseContract.UserColumns.USERNAME;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "dbusergithub";
    private static int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String SQL_CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    USERNAME + " TEXT, " +
                    NAME + " TEXT, " +
                    AVATAR_URL + " TEXT, " +
                    FOLLOWER + " TEXT, " +
                    FOLLOWING + " TEXT, " +
                    REPOSITORY + " TEXT, " +
                    COMPANY + " TEXT, " +
                    LOCATION + " TEXT, " +
                    HTML_URL + " TEXT"+
                    ")";



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
