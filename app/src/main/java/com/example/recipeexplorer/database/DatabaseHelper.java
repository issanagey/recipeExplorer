package com.example.recipeexplorer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import com.example.recipeexplorer.models.Recipe;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "app_database.db";

    // Users table create statement
    private static final String CREATE_TABLE_USERS = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, password TEXT, challenge_completed INTEGER, profile_picture BLOB)";

    // Login table create statement (cache current user to skip login)
    private static final String CREATE_TABLE_LOGIN = "CREATE TABLE login (id INTEGER PRIMARY KEY)";

    // Recipes table create statement
    private static final String CREATE_TABLE_RECIPES = "CREATE TABLE recipes (recipe_id INTEGER PRIMARY KEY AUTOINCREMENT, recipe_state TEXT , recipe_name TEXT, recipe_steps TEXT, recipe_description TEXT, recipe_image BLOB)";

    // Cookbook table create statement
    private static final String CREATE_TABLE_COOKBOOKS = "CREATE TABLE cookbooks (user_id INTEGER , recipe_id INTEGER)";

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

        // dummy data
        // users
        // admin
        ContentValues values = new ContentValues();
        values.put("name", "admin");
        values.put("password", "admin");
        values.put("challenge_completed", 0);
        db.insert("users", null, values);

        Bitmap emptyBitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);

        // Optionally, fill the Bitmap with a black color
        Canvas canvas = new Canvas(emptyBitmap);
        canvas.drawColor(Color.BLACK);

        // Convert the Bitmap to a byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        emptyBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        // Put the byte array into ContentValues
        values.put("profile_picture", byteArray);

        db.insert("users", null, values);

        // test
        values = new ContentValues();
        values.put("name", "test");
        values.put("password", "12345678");
        values.put("challenge_completed", 0);
        db.insert("users", null, values);

        // Insert recipes for all states
        insertRecipe(db, "Johor", "Laksa Johor", "Laksa Johor is a unique variation of laksa from Johor, Malaysia, featuring spaghetti noodles and a spicy fish gravy.", "Step 1. Cook spaghetti until al dente; Step 2. Prepare a spicy fish gravy by blending shallots, garlic, dried chilies, shrimp paste, and tamarind pulp; Step 3. Cook the blended mixture with coconut milk, fish, and spices until fragrant; Step 4. Serve the cooked spaghetti with the spicy fish gravy; Step 5. Garnish with shredded cucumber, pineapple, and mint leaves; Step 6. Serve hot with a slice of lime and sambal.");
        insertRecipe(db, "Kedah", "Ayam Pongteh Kedah", "Ayam Pongteh Kedah is a traditional Nyonya chicken stew dish from Kedah, Malaysia, known for its savory and slightly sweet flavor.", "Step 1. Marinate chicken pieces with soy sauce, palm sugar, and fermented soybean paste (tauchu); Step 2. Heat oil in a pot and sauté shallots, garlic, and ginger until fragrant; Step 3. Add the marinated chicken and cook until browned; Step 4. Pour in water and simmer until chicken is tender; Step 5. Serve hot with steamed rice; Step 6. Garnish with fried shallots and chopped cilantro.");
        insertRecipe(db, "Kelantan", "Nasi Dagang Kelantan", "Nasi Dagang Kelantan is a traditional rice dish from Kelantan, Malaysia, served with flavorful fish curry.", "Step 1. Cook a mixture of glutinous rice and regular rice with coconut milk, ginger, and fenugreek seeds until fluffy; Step 2. Prepare fish curry by cooking fish with spices, coconut milk, and tamarind juice; Step 3. Serve the cooked rice with the fish curry; Step 4. Garnish with sliced boiled eggs, pickled vegetables, and fried shaved coconut; Step 5. Serve hot.");
        insertRecipe(db, "Malacca", "Satay Celup Malacca", "Satay Celup Malacca is a popular hotpot dish from Malacca, Malaysia, featuring skewered ingredients cooked in a flavorful peanut sauce.", "Step 1. Prepare a boiling pot of satay sauce made from peanuts, coconut milk, spices, and chili paste; Step 2. Skewer various ingredients such as meat, seafood, and vegetables on satay sticks; Step 3. Dip the skewered ingredients into the boiling satay sauce until cooked; Step 4. Serve hot with rice cakes and additional satay sauce for dipping.");
        insertRecipe(db, "Sabah", "Hinava Sabah", "Hinava Sabah is a traditional Kadazandusun dish from Sabah, Malaysia, made with marinated raw fish.", "Step 1. Prepare the fish by thinly slicing fresh fish fillets; Step 2. Marinate the fish slices with lime juice, sliced shallots, grated ginger, and chopped chilies; Step 3. Let the fish marinate for a few minutes; Step 4. Garnish with sliced bitter gourd, julienned ginger, and chopped coriander; Step 5. Serve immediately as a refreshing appetizer or side dish.");
        insertRecipe(db, "Sarawak", "Sarawak Kolo Mee", "Sarawak Kolo Mee is a popular noodle dish from Sarawak, Malaysia, featuring springy egg noodles tossed in shallot oil and served with various toppings.", "Step 1. Cook egg noodles until al dente; Step 2. Toss cooked noodles in shallot oil and light soy sauce; Step 3. Top with slices of char siu (barbecued pork), minced pork, and fried shallots; Step 4. Serve hot with pickled chili and a side of clear soup.");
        insertRecipe(db, "Negeri Sembilan", "Masak Lemak Cili Api Negeri Sembilan", "Masak Lemak Cili Api Negeri Sembilan is a spicy and creamy coconut-based dish from Negeri Sembilan, Malaysia, known for its bold flavors.", "Step 1. Prepare a spice paste by blending shallots, garlic, ginger, lemongrass, and dried chilies; Step 2. Cook the spice paste with coconut milk, turmeric leaves, and tamarind juice until fragrant; Step 3. Add chicken or fish and simmer until cooked through; Step 4. Season with salt and sugar to taste; Step 5. Serve hot with steamed rice; Step 6. Garnish with sliced chili and torch ginger flower.");
        insertRecipe(db, "Pahang", "Ikan Patin Tempoyak Pahang", "Ikan Patin Tempoyak Pahang is a creamy and tangy fish curry dish from Pahang, Malaysia, made with fermented durian paste.", "Step 1. Prepare tempoyak (fermented durian paste) by mixing durian flesh with salt; Step 2. Cook the tempoyak with coconut milk, turmeric leaves, and spices until fragrant; Step 3. Add fish such as patin (catfish) and cook until fish is tender; Step 4. Add tamarind paste for sourness; Step 5. Serve hot with steamed rice; Step 6. Garnish with shredded turmeric leaves and torch ginger flower; Step 7. Serve with ulam (raw vegetables) and sambal belacan.");
        insertRecipe(db, "Penang", "Char Kway Teow Penang", "Char Kway Teow Penang is a famous stir-fried noodle dish from Penang, Malaysia, known for its smoky flavor and savory sauce.", "Step 1. Heat oil in a wok and fry chopped garlic until fragrant; Step 2. Add fresh flat rice noodles and stir-fry until slightly charred; Step 3. Push noodles to the side and add more oil; Step 4. Crack an egg into the wok and scramble; Step 5. Add prawns, cockles, and Chinese sausage (lap cheong); Step 6. Stir-fry everything together with bean sprouts, chives, and chili paste; Step 7. Season with soy sauce, fish sauce, and dark soy sauce; Step 8. Serve hot with a wedge of lime.");
        insertRecipe(db, "Perak", "Ipoh Hor Fun Perak", "Ipoh Hor Fun Perak is a popular noodle soup dish from Perak, Malaysia, featuring silky rice noodles in a flavorful chicken and prawn broth.", "Step 1. Prepare the broth by simmering chicken bones, prawn shells, and aromatics such as ginger and garlic; Step 2. Strain the broth and season with soy sauce and white pepper; Step 3. Cook flat rice noodles until tender; Step 4. Serve the noodles in a bowl with shredded chicken, poached prawns, and blanched bean sprouts; Step 5. Pour hot broth over the noodles; Step 6. Garnish with sliced spring onions, fried shallots, and chopped cilantro; Step 7. Serve hot with a side of pickled green chili.");
        insertRecipe(db, "Perlis", "Laksa Perlis", "Laksa Perlis is a traditional fish-based noodle soup from Perlis, Malaysia, known for its tangy and spicy flavors.", "Step 1. Prepare the laksa broth by cooking fish with tamarind juice, lemongrass, and dried chilies; Step 2. Blend the cooked fish with the broth to create a smooth and flavorful base; Step 3. Cook thick rice noodles until tender; Step 4. Serve the noodles in a bowl with the hot laksa broth; Step 5. Garnish with sliced cucumber, pineapple, and mint leaves; Step 6. Serve hot with a slice of lime and sambal.");
        insertRecipe(db, "Selangor", "Satay Selangor", "Satay Selangor is a popular grilled meat skewer dish from Selangor, Malaysia, known for its rich and flavorful peanut sauce.", "Step 1. Marinate meat (chicken, beef, or lamb) with a mixture of turmeric, lemongrass, shallots, and garlic; Step 2. Skewer the marinated meat on satay sticks; Step 3. Grill the skewers over hot charcoal until cooked and slightly charred; Step 4. Prepare a peanut sauce by blending roasted peanuts, coconut milk, chili paste, and tamarind juice; Step 5. Serve the grilled satay with the peanut sauce, cucumber, onion, and rice cakes.");
        insertRecipe(db, "Terengganu", "Keropok Lekor Terengganu", "Keropok Lekor Terengganu is a popular snack from Terengganu, Malaysia, made from fish paste and sago flour.", "Step 1. Prepare the fish paste by blending fish fillets with salt and sago flour; Step 2. Shape the fish paste into long cylindrical logs; Step 3. Boil the fish logs until cooked; Step 4. Slice the boiled fish logs into thin pieces; Step 5. Deep-fry the slices until crispy; Step 6. Serve hot with a sweet and spicy dipping sauce.");
    }

    private void insertRecipe(SQLiteDatabase db, String state, String name, String description, String steps) {
        ContentValues values = new ContentValues();
        values.put("recipe_state", state);
        values.put("recipe_name", name);
        values.put("recipe_description", description);
        values.put("recipe_steps", steps);

        Bitmap emptyBitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);

        // Optionally, fill the Bitmap with a black color
        Canvas canvas = new Canvas(emptyBitmap);
        canvas.drawColor(Color.BLACK);

        // Convert the Bitmap to a byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        emptyBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        // Put the byte array into ContentValues
        values.put("recipe_image", byteArray);

        db.insert("recipes", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS login");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS cookbooks");

        // Create tables again
        onCreate(db);
    }

    public List<String> getAllStates() {
        List<String> states = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, "recipes", new String[]{"recipe_state"}, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String state = cursor.getString(cursor.getColumnIndexOrThrow("recipe_state"));
                if (!states.contains(state)) {
                    states.add(state);
                    Log.d("DatabaseHelper", "Added state: " + state);
                }
            } while (cursor.moveToNext());
        } else {
            Log.d("DatabaseHelper", "No states found in the database.");
        }
        cursor.close();
        return states;
    }

    public List<Recipe> getRecipesByState(String state) {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("recipes", null, "recipe_state=?", new String[]{state}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                try {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("recipe_id"));
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

    public void addRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("recipe_state", recipe.getState());
        values.put("recipe_name", recipe.getTitle());
        values.put("recipe_description", recipe.getDescription());
        values.put("recipe_steps", recipe.getSteps());
        values.put("recipe_image", recipe.getImage());
        db.insert("recipes", null, values);
    }
}
