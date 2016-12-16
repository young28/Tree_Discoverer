package com.example.pinatala.tree_discoverer.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by YouYang on 28/11/16.
 */

public class TreeDatabaseOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tree";

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";

    public static final String KEY_TREE_ID = "tree_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_TREE_PHOTO_NAME = "tree_photo";
    public static final String KEY_LEAF_PHOTO_NAME = "leaf_photo";
    public static final String KEY_TYPE_NAME = "type_name";

    public static final String TREE_TABLE_NAME = "tree_info";

    public static final String KEY_TREE_TYPE_ID = "tree_type_id";
    //public static final String KEY_TYPE_NAME = "type_name";
    public static final String TYPE_TABLE_NAME = "tree_type";

    public static final String KEY_COMMENT_ID = "comment_id";
    public static final String KEY_NUM_LIKES = "num_likes";
    public static final String KEY_NUM_DISLIKES = "num_dislikes";
    public static final String KEY_OPINIONS = "opinions";
    public static final String COMMENT_TABLE_NAME = "comment";

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_STEPS = "steps";
    public static final String KEY_POINTS = "points";
    public static final String KEY_PROFILE = "profile";
    public static final String USER_TABLE_NAME = "user";


    //now the database is without foreign key function, first test with tree table.
    public static final String CREATE_TABLE_TREE = "CREATE TABLE IF NOT EXISTS " + TREE_TABLE_NAME +
            " ("
            + KEY_TREE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TREE_PHOTO_NAME + " TEXT, "
            + KEY_LEAF_PHOTO_NAME + " TEXT, "  + KEY_DATE + " TEXT, " + KEY_LATITUDE + " REAL, "
            + KEY_LONGITUDE + " REAL, " + KEY_TYPE_NAME + " TEXT);";



    public static final String CREATE_TABLE_TREE_TYPE = "CREATE TABLE IF NOT EXISTS " + TYPE_TABLE_NAME +
            " (" + KEY_TREE_TYPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TYPE_NAME + " TEXT);";

    public static final String CREATE_TABLE_COMMENT = "CREATE TABLE IF NOT EXISTS " + COMMENT_TABLE_NAME +
            " (" + KEY_COMMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NUM_LIKES + " INTEGER, " +
            KEY_NUM_DISLIKES + " INTEGER, " + KEY_OPINIONS + " TEXT);";

    public static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME +
            " (" + KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_STEPS + " INTEGER, " +
            KEY_POINTS + " INTEGER, " + KEY_PROFILE + " BLOB);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TREE);
        db.execSQL(CREATE_TABLE_TREE_TYPE);
        db.execSQL(CREATE_TABLE_COMMENT);
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public TreeDatabaseOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public TreeDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public TreeDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}


