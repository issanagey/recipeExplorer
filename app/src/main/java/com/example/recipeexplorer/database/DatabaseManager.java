// Updated DatabaseManager class
package com.example.recipeexplorer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import com.example.recipeexplorer.models.Recipe;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private final DatabaseHelper dbHelper;
    private Context context;

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
                "id", // Ensure to retrieve the ID column
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

    public Bitmap GetUserProfilePicture(int ID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                "id",
                "profile_picture"
        };

        Cursor cursor = db.query(
                "users",
                projection,
                "id = ?",
                new String[]{String.valueOf(ID)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            byte[] blob = cursor.getBlob(cursor.getColumnIndexOrThrow("profile_picture"));
            cursor.close();

            if (blob != null) {
                return BitmapFactory.decodeByteArray(blob, 0, blob.length);
            } else {
                // If the user is found but the profile picture is null, return a black bitmap
                Bitmap blackBitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(blackBitmap);
                canvas.drawColor(Color.BLACK); // Set the bitmap color to black
                return blackBitmap;
            }
        }

        // If no profile picture is found, or in case of any issue, return a default/empty Bitmap
        if (cursor != null) {
            cursor.close();
        }
        Bitmap emptyBitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(emptyBitmap);
        canvas.drawColor(Color.WHITE); // or use Color.TRANSPARENT for a transparent background
        return emptyBitmap;
    }


    public Integer GetUserRecipesTried(int ID){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                "id",
                "challenge_completed"
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
                Integer recipesTried = Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("challenge_completed")));
                cursor.close();
                return recipesTried;
            }
        }

        // no recipes tried found
        cursor.close();
        return 999999999;
    }

    public boolean verifyPassword(String currentPassword) {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT id FROM users WHERE password = ?", new String[]{currentPassword});
            return cursor != null && cursor.moveToFirst();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public boolean updateUsername(String newUsername) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", newUsername);

        int rowsAffected = db.update("users", values, "id = ?", new String[]{String.valueOf(GetCurrentUserID())});
        return rowsAffected > 0;
    }

    public boolean updatePassword(String newPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword);

        int rowsAffected = db.update("users", values, "id = ?", new String[]{String.valueOf(GetCurrentUserID())});
        return rowsAffected > 0;
    }

    public void updateProfilePicture(Bitmap newProfilePicture) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Convert Bitmap to byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        newProfilePicture.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        // Put byte array into ContentValues
        values.put("profile_picture", byteArray);

        // Update the user's profile picture in the database
        int rowsAffected = db.update("users", values, "id = ?", new String[]{String.valueOf(GetCurrentUserID())});


    }

    public String GetChallengeSteps(int recipeId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                "recipe_id",
                "recipe_steps"
        };

        Cursor cursor = db.query(
                "recipes",
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {

            Integer current_recipeID = cursor.getInt(cursor.getColumnIndexOrThrow("recipe_id"));
            // check id
            if(current_recipeID == recipeId){
                String steps = cursor.getString(cursor.getColumnIndexOrThrow("recipe_steps"));
                cursor.close();
                return steps;
            }
        }

        // no user found
        cursor.close();
        return "No Match";
    }

    public void AddChallengeCompleted(Integer user_id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // first, we need to get the current challenge completed value
        String[] projection = {"challenge_completed"};

        Cursor cursor = db.query(
                "users",
                new String[] {"challenge_completed"},
                "id = ?",
                new String[] {String.valueOf(user_id)},
                null,
                null,
                null
        );

        cursor.moveToFirst();
        int challenge_completed = cursor.getInt(cursor.getColumnIndexOrThrow("challenge_completed"));

        // increment challenge completed value by 1
        challenge_completed++;
        Log.d("MyApp", Integer.toString(challenge_completed));

        // New value for one column
        ContentValues values = new ContentValues();
        values.put("challenge_completed", challenge_completed);

        // update challenge completed value
        db.update("users",values, "id = ?", new String[] {String.valueOf(user_id)});

    }

    public com.example.recipeexplorer.models.Recipe getRecipeById(int recipeId) {
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

        com.example.recipeexplorer.models.Recipe recipe = null;

        if (cursor != null && cursor.moveToFirst()) {
            String state = cursor.getString(cursor.getColumnIndexOrThrow("recipe_state"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("recipe_name"));
            String steps = cursor.getString(cursor.getColumnIndexOrThrow("recipe_steps"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("recipe_description"));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("recipe_image"));

            recipe = new com.example.recipeexplorer.models.Recipe(recipeId, state, name, steps, description, image);

            cursor.close();
        }

        return recipe;
    }

    public void addRecipeToCookbook(long userId, int recipeId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("recipe_id", recipeId);
        values.put("completed", 0);

        long newRowId = db.insert("cookbooks", null, values);
    }

    public String getRecipeSteps(int recipeId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                "recipe_id",
                "recipe_steps"
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

        String steps = null;

        if (cursor != null && cursor.moveToFirst()) {
            steps = cursor.getString(cursor.getColumnIndexOrThrow("recipe_steps"));
            cursor.close();
        }

        return steps;
    }

    public String GetRecipeName(int recipeId){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                "recipe_id",
                "recipe_name"
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

        String name = null;

        if (cursor != null && cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow("recipe_name"));
            cursor.close();
        }

        return name;
    }

    public List<Recipe> GetRecipeListFromCookBook(int user_id) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Recipe> recipes = new ArrayList<>();


        // get cursor of recipe id
        Cursor cursor = db.query("cookbooks", new String[]{"recipe_id"}, "user_id=?", new String[]{String.valueOf(user_id)}, null, null, null, null);

        // loop through cursor and add recipe to list
        while (cursor.moveToNext()){
            recipes.add(GetRecipeByID(cursor.getInt(cursor.getColumnIndexOrThrow("recipe_id"))));
        }

        cursor.close();

        return recipes;
    }

    public Recipe GetRecipeByID(int recipe_id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("recipes", null, "recipe_id=?", new String[]{String.valueOf(recipe_id)}, null, null, null, null);

        cursor.moveToFirst();
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("recipe_id"));
        String recipeState = cursor.getString(cursor.getColumnIndexOrThrow("recipe_state"));
        String title = cursor.getString(cursor.getColumnIndexOrThrow("recipe_name"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("recipe_description"));
        String steps = cursor.getString(cursor.getColumnIndexOrThrow("recipe_steps"));
        byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("recipe_image"));

        Recipe recipe = new Recipe(id, recipeState, title, description, steps, image);
        cursor.close();

        return recipe;
    }

    // Function to return an array of recipe IDs based on user ID
    public int[] getRecipeIdsByUserId(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT recipe_id FROM save WHERE user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        int[] recipeIds = new int[cursor.getCount()];
        int index = 0;

        if (cursor.moveToFirst()) {
            do {
                recipeIds[index++] = cursor.getInt(cursor.getColumnIndex("recipe_id"));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return recipeIds;
    }

    public List<Recipe> getRecipesByState(String state) {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("recipes", null, "recipe_state=?", new String[]{state}, null, null, null);

        // recipes in coockbook should not be included
        int[] idInCookbook = getRecipeIdsByUserId(GetCurrentUserID());

        if (cursor.moveToFirst()) {
            do {
                try {
                    // check conflict id
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("recipe_id"));
                    for (int i = 0; i < idInCookbook.length; i++) {
                        if (idInCookbook[i] == id) {
                            continue;
                        }
                    }

                    String recipeState = cursor.getString(cursor.getColumnIndexOrThrow("recipe_state"));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("recipe_name"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("recipe_description"));
                    String steps = cursor.getString(cursor.getColumnIndexOrThrow("recipe_steps"));
                    byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("recipe_image"));

                    Recipe recipe = new Recipe(id, recipeState, title, description, steps, image);
                    recipes.add(recipe);
                } catch (IllegalArgumentException e) {
                    // Handle the case where a column name is not found
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recipes;
    }

}