package com.example.githubuserapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.githubuserapp.model.User;

import java.util.ArrayList;

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

public class UserHelper {
    public static final String DATABASE_TABLE = TABLE_NAME;
    public static DatabaseHelper databaseHelper;
    private static UserHelper INSTANCE;

    private static SQLiteDatabase database;


    public UserHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static UserHelper getInstance(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new UserHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close(){
        databaseHelper.close();
        if (database.isOpen()){
            database.close();
        }
    }
    public Cursor queryById(String id) {
        database = databaseHelper.getReadableDatabase();
        return database.query(DATABASE_TABLE, null
                , _ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryByUsername( String username){
        database = databaseHelper.getReadableDatabase();
        String[] projection = {USERNAME};
        return database.query(DATABASE_TABLE, projection,
                USERNAME + "=?",
                new String[]{username},
                null,
                null,
                null);
    }
    public long insert(User user){
        database = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(_ID, user.getId());
        contentValues.put(USERNAME, user.getUsername());
        contentValues.put(NAME, user.getName());
        contentValues.put(AVATAR_URL, user.getPhoto());
        contentValues.put(FOLLOWER, user.getFollower());
        contentValues.put(FOLLOWING, user.getFollowing());
        contentValues.put(REPOSITORY, user.getRepository());
        contentValues.put(LOCATION, user.getLocation());
        contentValues.put(COMPANY, user.getCompany());
        contentValues.put(HTML_URL, user.getHtmlUrl());

        return  database.insert(DATABASE_TABLE, null, contentValues);
    }

    public int delete(String id){
        database = databaseHelper.getWritableDatabase();
        return database.delete(DATABASE_TABLE, _ID + " =?", new String[]{id});
    }

    public int deleteUsername(String id){
        database = databaseHelper.getWritableDatabase();
        return database.delete(DATABASE_TABLE, USERNAME + " =?", new String[]{id});
    }

    public ArrayList<User> getDataUser(){
        ArrayList<User> userGithubList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE,null,
                null,
                null,
                null,
                null,
                USERNAME + " ASC",
                null);
        cursor.moveToFirst();
        User userGithub;
        if (cursor.getCount() > 0){
            do {
                userGithub = new User();
                userGithub.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(USERNAME)));
                userGithub.setName(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
                userGithub.setPhoto(cursor.getString(cursor.getColumnIndexOrThrow(AVATAR_URL)));
                userGithub.setFollower(cursor.getString(cursor.getColumnIndexOrThrow(FOLLOWER)));
                userGithub.setFollowing(cursor.getString(cursor.getColumnIndexOrThrow(FOLLOWING)));
                userGithub.setRepository(cursor.getString(cursor.getColumnIndexOrThrow(REPOSITORY)));
                userGithub.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(LOCATION)));
                userGithub.setCompany(cursor.getString(cursor.getColumnIndexOrThrow(COMPANY)));
                userGithub.setHtmlUrl(cursor.getString(cursor.getColumnIndexOrThrow(HTML_URL)));

                userGithubList.add(userGithub);
                cursor.moveToNext();
            }while (!cursor.isAfterLast());
        }cursor.close();
        return userGithubList;
    }

}
