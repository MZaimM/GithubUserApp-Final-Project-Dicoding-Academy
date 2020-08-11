package com.example.githubuserapp.database;

import android.provider.BaseColumns;

public class DatabaseContract {
    public static final class UserColumns implements BaseColumns{
        public static  String TABLE_NAME = "user";
        public static String USERNAME = "username";
        public static String NAME = "name";
        public static String AVATAR_URL = "avatar_url";
        public static String FOLLOWER = "follower";
        public static String FOLLOWING ="following";
        public static String REPOSITORY = "repository";
        public static String COMPANY = "company";
        public static String LOCATION = "location";
        public static String HTML_URL = "html_url";
    }
}
