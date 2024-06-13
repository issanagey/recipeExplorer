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
    public void addUser(String name, String password, byte[] profilePicture) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("password", password);
        values.put("challenge_completed", 0);
        values.put("profile_picture", profilePicture);

        long newRowId = db.insert("users", null, values);
        Log.d("Database", "New user ID: " + newRowId);
    }

    // Method to read users from the database
    public void readUsers() {
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
            long userId = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
            String userName = cursor.getString(cursor.getColumnIndexOrThrow("name"));

            // Handle the retrieved data (e.g., log it or display it)
            Log.d("DatabaseManager", "ID: " + userId + " Name: " + userName);
        }
        cursor.close();
    }

    // login verification
    // returns true if name and pw match
    public boolean LoginVerification(String name, String password){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                "id",
                "name",
                "password"
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

                // put id into login list (currently logged in)
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                loginUser(userId);

                cursor.close();
                return true;
            }
        }

        // no matches found
        cursor.close();
        return false;
    }

    // create account verification
    // returns true if account does not exist
    public boolean CreateAccountVerification(String name){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                "name"
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

            // get username
            String userName = cursor.getString(cursor.getColumnIndexOrThrow("name"));

            // check if username already exists
            if(userName.equals(name)){
                cursor.close();
                return false;
            }
        }

        // no matches found
        cursor.close();
        return true;
    }

    public void loginUser(long id) {
        // open in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete all rows in table
        db.delete("login", null, null);

        // put in new user
        ContentValues values = new ContentValues();
        values.put("id", id);

        db.insert("login", null, values);
    }

    // logout user (only from database and not views !!! always reset to Login page when calling this method)
    public void LogoutUser() {
        // open in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete all rows in table
        db.delete("login", null, null);

    }

    // returns true if there is currently a user logged in
    // allows for app to skip login page
    public boolean IsLoggedIn(){
        // check if login table is empty
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                "id"
        };

        Cursor cursor = db.query(
                "login",
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if(cursor.getCount() == 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public int GetCurrentUserID(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                "id"
        };

        Cursor cursor = db.query(
                "login",
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            cursor.close();
            return userId;
        }

        // no user currently logged in
        cursor.close();
        return -1;
    }

    public String GetUsername(int ID){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                "id",
                "name"
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

            // check id
            if(userId == ID){
                String userName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                cursor.close();
                return userName;
            }
        }

        // no user found
        cursor.close();
        return "No Match";
    }

    public com.example.recipeexplorer.model.Recipe getRecipeById(int recipeId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                "recipe_id",
                "recipe_state",
                "recipe_name",
                "recipe_steps",
                "recipe_description",
                "recipe_image"
        };

        String selection = "recipe_id = ?";
        String[] selectionArgs = { String.valueOf(recipeId) };

        Cursor cursor = db.query(
                "recipes",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        com.example.recipeexplorer.model.Recipe recipe = null;

        if (cursor != null && cursor.moveToFirst()) {
            String state = cursor.getString(cursor.getColumnIndexOrThrow("recipe_state"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("recipe_name"));
            String steps = cursor.getString(cursor.getColumnIndexOrThrow("recipe_steps"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("recipe_description"));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("recipe_image"));

            recipe = new com.example.recipeexplorer.model.Recipe(recipeId, state, name, steps, description, image);

            cursor.close();
        }

        return recipe;
    }

    public void addRecipeToCookbook(long userId, int recipeId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("recipe_id", recipeId);

        long newRowId = db.insert("cookbooks", null, values);
        Log.d("DatabaseManager", "New row ID in cookbooks: " + newRowId);
    }

    // Add for recipes, challenges, and achievements tables as needed.
}
