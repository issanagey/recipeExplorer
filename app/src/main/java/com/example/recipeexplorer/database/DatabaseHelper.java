package com.example.recipeexplorer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.recipeexplorer.model.Recipe;
import java.util.ArrayList;
import java.util.List;

import com.example.recipeexplorer.models.Recipe;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "app_database.db";

    // Table create statements
    private static final String CREATE_TABLE_USERS = "CREATE TABLE users (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT," +
            "password TEXT," +
            "challenge_completed INTEGER," +
            "profile_picture BLOB" +
            ")";

    private static final String CREATE_TABLE_LOGIN = "CREATE TABLE login (" +
            "id INTEGER PRIMARY KEY" +
            ")";

    private static final String CREATE_TABLE_RECIPES = "CREATE TABLE recipes (" +
            "recipe_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "recipe_state TEXT," +
            "recipe_name TEXT," +
            "recipe_steps TEXT," +
            "recipe_description TEXT," +
            "recipe_image BLOB" +
            ")";

    private static final String CREATE_TABLE_COOKBOOKS = "CREATE TABLE cookbooks (" +
            "user_id INTEGER," +
            "recipe_id INTEGER," +
            "PRIMARY KEY (user_id, recipe_id)" +
            ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating required tables
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_LOGIN);
        db.execSQL(CREATE_TABLE_RECIPES);
        db.execSQL(CREATE_TABLE_COOKBOOKS);

        // Inserting some dummy data for users and recipes
        insertDummyData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if they exist
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS login");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS cookbooks");

        // Create tables again
        onCreate(db);
    }

    // Method to insert dummy data for users and recipes
    private void insertDummyData(SQLiteDatabase db) {
        // Insert dummy users
        ContentValues values = new ContentValues();
        values.put("name", "admin");
        values.put("password", "admin");
        values.put("challenge_completed", 0);
        db.insert("users", null, values);

        values.clear();
        values.put("name", "test");
        values.put("password", "12345678");
        values.put("challenge_completed", 0);
        db.insert("users", null, values);

        // Insert dummy recipes
        values.clear();
        values.put("recipe_state", "Johor");
        values.put("recipe_name", "Laksa Johor");
        values.put("recipe_steps", "Step 1. Cook spaghetti until al dente; Step 2. Prepare a spicy fish gravy by blending shallots, garlic, dried chilies, shrimp paste, and tamarind pulp; Step 3. Cook the blended mixture with coconut milk, fish, and spices until fragrant; Step 4. Serve the cooked spaghetti with the spicy fish gravy; Step 5. Garnish with shredded cucumber, pineapple, and mint leaves; Step 6. Serve hot with a slice of lime and sambal.");
        values.put("recipe_description", "Laksa Johor is a unique variation of laksa from Johor, Malaysia, featuring spaghetti noodles and a spicy fish gravy.");
        db.insert("recipes", null, values);

        values.clear();
        values.put("recipe_state", "Kedah");
        values.put("recipe_name", "Ayam Pongteh Kedah");
        values.put("recipe_steps", "Step 1. Marinate chicken pieces with soy sauce, palm sugar, and fermented soybean paste (tauchu); Step 2. Heat oil in a pot and sauté shallots, garlic, and ginger until fragrant; Step 3. Add the marinated chicken and cook until browned; Step 4. Pour in water and simmer until chicken is tender; Step 5. Serve hot with steamed rice; Step 6. Garnish with fried shallots and chopped cilantro.");
        values.put("recipe_description", "Ayam Pongteh Kedah is a traditional Nyonya chicken stew dish from Kedah, Malaysia, known for its savory and slightly sweet flavor.");
        db.insert("recipes", null, values);

        // Insert more recipes similarly for other states...
    }

    // Method to fetch all states from the recipes table
    public List<String> getAllStates() {
        List<String> states = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT DISTINCT recipe_state FROM recipes";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int recipeStateIndex = cursor.getColumnIndex("recipe_state");
            if (recipeStateIndex >= 0) {
                do {
                    String state = cursor.getString(recipeStateIndex);
                    states.add(state);
                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        return states;
    }

    // Method to fetch all recipes by state
    public List<Recipe> getAllRecipesByState(String state) {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {"recipe_name", "recipe_description"};
        String selection = "recipe_state = ?";
        String[] selectionArgs = { state };

        Cursor cursor = db.query("recipes", columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            int recipeNameIndex = cursor.getColumnIndex("recipe_name");
            int recipeDescriptionIndex = cursor.getColumnIndex("recipe_description");
            if (recipeNameIndex >= 0 && recipeDescriptionIndex >= 0) {
                do {
                    String recipeName = cursor.getString(recipeNameIndex);
                    String recipeDescription = cursor.getString(recipeDescriptionIndex);
                    recipes.add(new Recipe(recipeName, recipeDescription));
                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        return recipes;
}
}
