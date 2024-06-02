package com.example.recipeexplorer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseManager {
    private DatabaseHelper dbHelper;

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Method to add a user to the database
    public void addUser(String name, String email, String password, byte[] profilePicture) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        values.put("profile_picture", profilePicture);

        long newRowId = db.insert("users", null, values);
        Log.d("Database", "New user ID: " + newRowId);
    }

    // Method to read users from the database
    public void readUsers() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                "id",
                "name",
                "email",
                "password",
                "profile_picture"
        };

        Cursor cursor = db.query(
                "users",
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            long userId = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
            String userName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String userEmail = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String userPassword = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            byte[] userProfilePicture = cursor.getBlob(cursor.getColumnIndexOrThrow("profile_picture"));

            // Handle the retrieved data (e.g., log it or display it)
            Log.d("Database", "ID: " + userId + " Name: " + userName + " Email: " + userEmail);
        }
        cursor.close();
    }

    public boolean LoginVerification(String name, String password){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                "name",
                "password",
        };

        Cursor cursor = db.query(
                "users",
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {

            // get username & pw
            String userName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String userPassword = cursor.getString(cursor.getColumnIndexOrThrow("password"));

            // check if username & pw match
            if(userName.equals(name) && userPassword.equals(password)){
                cursor.close();
                return true;
            }
        }

        // no matches found
        cursor.close();
        return false;
    }


    // Add for recipes, challenges, and achievements tables as needed.
}
