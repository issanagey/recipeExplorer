package com.example.recipeexplorer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "app_database.db";

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_RECIPES = "recipes";
    private static final String TABLE_CHALLENGES = "challenges";
    private static final String TABLE_ACHIEVEMENTS = "achievements";

    // Users Table Columns
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_PROFILE_PICTURE = "profile_picture";

    // Users table create statement
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT,"
            + COLUMN_USER_PASSWORD + " TEXT,"
            + COLUMN_USER_PROFILE_PICTURE + " BLOB" + ")";

    // Recipes table create statement (currently empty)
    private static final String CREATE_TABLE_RECIPES = "CREATE TABLE " + TABLE_RECIPES + "("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT" + ")";

    // Challenges table create statement (currently empty)
    private static final String CREATE_TABLE_CHALLENGES = "CREATE TABLE " + TABLE_CHALLENGES + "("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT" + ")";

    // Achievements table create statement (currently empty)
    private static final String CREATE_TABLE_ACHIEVEMENTS = "CREATE TABLE " + TABLE_ACHIEVEMENTS + "("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating required tables
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_RECIPES);
        db.execSQL(CREATE_TABLE_CHALLENGES);
        db.execSQL(CREATE_TABLE_ACHIEVEMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHALLENGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACHIEVEMENTS);

        // Create tables again
        onCreate(db);
    }

    //dsdsdsds
}
