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
        insertRecipe(db, "Johor", "Laksa Johor", "Laksa Johor is a unique variation of laksa from Johor, Malaysia, featuring spaghetti noodles and a spicy fish gravy.", "Step 1. Cook spaghetti until al dente; Step 2. Prepare a spicy fish gravy by blending shallots, garlic, dried chilies, shrimp paste, and tamarind pulp; Step 3. Cook the blended mixture with coconut milk, fish, and spices until fragrant; Step 4. Serve the cooked spaghetti with the spicy fish gravy; Step 5. Garnish with shredded cucumber, pineapple, and mint leaves; Step 6. Serve hot with a slice of lime and sambal.","laksa_johor_image.png");
        insertRecipe(db, "Kedah", "Ayam Pongteh Kedah", "Ayam Pongteh Kedah is a traditional Nyonya chicken stew dish from Kedah, Malaysia, known for its savory and slightly sweet flavor.", "Step 1. Marinate chicken pieces with soy sauce, palm sugar, and fermented soybean paste (tauchu); Step 2. Heat oil in a pot and sauté shallots, garlic, and ginger until fragrant; Step 3. Add the marinated chicken and cook until browned; Step 4. Pour in water and simmer until chicken is tender; Step 5. Serve hot with steamed rice; Step 6. Garnish with fried shallots and chopped cilantro.","ayam_pongteh_kedah_image.png");
        insertRecipe(db, "Kelantan", "Nasi Dagang Kelantan", "Nasi Dagang Kelantan is a traditional rice dish from Kelantan, Malaysia, served with flavorful fish curry.", "Step 1. Cook a mixture of glutinous rice and regular rice with coconut milk, ginger, and fenugreek seeds until fluffy; Step 2. Prepare fish curry by cooking fish with spices, coconut milk, and tamarind juice; Step 3. Serve the cooked rice with the fish curry; Step 4. Garnish with sliced boiled eggs, pickled vegetables, and fried shaved coconut; Step 5. Serve hot.","nasi_dagang_kelantan_image.png");
        insertRecipe(db, "Malacca", "Satay Celup Malacca", "Satay Celup Malacca is a popular hotpot dish from Malacca, Malaysia, featuring skewered ingredients cooked in a flavorful peanut sauce.", "Step 1. Prepare a boiling pot of satay sauce made from peanuts, coconut milk, spices, and chili paste; Step 2. Skewer various ingredients such as meat, seafood, and vegetables on satay sticks; Step 3. Dip the skewered ingredients into the boiling satay sauce until cooked; Step 4. Serve hot with rice cakes and additional satay sauce for dipping.","satay_celup_malacca_image.png");
        insertRecipe(db, "Sabah", "Hinava Sabah", "Hinava Sabah is a traditional Kadazandusun dish from Sabah, Malaysia, made with marinated raw fish.", "Step 1. Prepare the fish by thinly slicing fresh fish fillets; Step 2. Marinate the fish slices with lime juice, sliced shallots, grated ginger, and chopped chilies; Step 3. Let the fish marinate for a few minutes; Step 4. Garnish with sliced bitter gourd, julienned ginger, and chopped coriander; Step 5. Serve immediately as a refreshing appetizer or side dish.","hinava_sabah_image.png");
        insertRecipe(db, "Sarawak", "Sarawak Kolo Mee", "Sarawak Kolo Mee is a popular noodle dish from Sarawak, Malaysia, featuring springy egg noodles tossed in shallot oil and served with various toppings.", "Step 1. Cook egg noodles until al dente; Step 2. Toss cooked noodles in shallot oil and light soy sauce; Step 3. Top with slices of char siu (barbecued pork), minced pork, and fried shallots; Step 4. Serve hot with pickled chili and a side of clear soup.","sarawak_kolo_mee_image.png");
        insertRecipe(db, "Negeri Sembilan", "Masak Lemak Cili Api Negeri Sembilan", "Masak Lemak Cili Api Negeri Sembilan is a spicy and creamy coconut-based dish from Negeri Sembilan, Malaysia, known for its bold flavors.", "Step 1. Prepare a spice paste by blending shallots, garlic, ginger, lemongrass, and dried chilies; Step 2. Cook the spice paste with coconut milk, turmeric leaves, and tamarind juice until fragrant; Step 3. Add chicken or fish and simmer until cooked through; Step 4. Season with salt and sugar to taste; Step 5. Serve hot with steamed rice; Step 6. Garnish with sliced chili and torch ginger flower.","masak_lemak_cili_api_negeri sembilan_image.png");
        insertRecipe(db, "Pahang", "Ikan Patin Tempoyak Pahang", "Ikan Patin Tempoyak Pahang is a creamy and tangy fish curry dish from Pahang, Malaysia, made with fermented durian paste.", "Step 1. Prepare tempoyak (fermented durian paste) by mixing durian flesh with salt; Step 2. Cook the tempoyak with coconut milk, turmeric leaves, and spices until fragrant; Step 3. Add fish such as patin (catfish) and cook until fish is tender; Step 4. Add tamarind paste for sourness; Step 5. Serve hot with steamed rice; Step 6. Garnish with shredded turmeric leaves and torch ginger flower; Step 7. Serve with ulam (raw vegetables) and sambal belacan.","ikan patin_tempoyak_pahang_image.png");
        insertRecipe(db, "Penang", "Char Kway Teow Penang", "Char Kway Teow Penang is a famous stir-fried noodle dish from Penang, Malaysia, known for its smoky flavor and savory sauce.", "Step 1. Heat oil in a wok and fry chopped garlic until fragrant; Step 2. Add fresh flat rice noodles and stir-fry until slightly charred; Step 3. Push noodles to the side and add more oil; Step 4. Crack an egg into the wok and scramble; Step 5. Add prawns, cockles, and Chinese sausage (lap cheong); Step 6. Stir-fry everything together with bean sprouts, chives, and chili paste; Step 7. Season with soy sauce, fish sauce, and dark soy sauce; Step 8. Serve hot with a wedge of lime.","char_kway_teow_penang_image.png");
        insertRecipe(db, "Perak", "Ipoh Hor Fun Perak", "Ipoh Hor Fun Perak is a popular noodle soup dish from Perak, Malaysia, featuring silky rice noodles in a flavorful chicken and prawn broth.", "Step 1. Prepare the broth by simmering chicken bones, prawn shells, and aromatics such as ginger and garlic; Step 2. Strain the broth and season with soy sauce and white pepper; Step 3. Cook flat rice noodles until tender; Step 4. Serve the noodles in a bowl with shredded chicken, poached prawns, and blanched bean sprouts; Step 5. Pour hot broth over the noodles; Step 6. Garnish with sliced spring onions, fried shallots, and chopped cilantro; Step 7. Serve hot with a side of pickled green chili.","ipoh_hor_fun_perak_image.png");
        insertRecipe(db, "Perlis", "Laksa Perlis", "Laksa Perlis is a traditional fish-based noodle soup from Perlis, Malaysia, known for its tangy and spicy flavors.", "Step 1. Prepare the laksa broth by cooking fish with tamarind juice, lemongrass, and dried chilies; Step 2. Blend the cooked fish with the broth to create a smooth and flavorful base; Step 3. Cook thick rice noodles until tender; Step 4. Serve the noodles in a bowl with the hot laksa broth; Step 5. Garnish with sliced cucumber, pineapple, and mint leaves; Step 6. Serve hot with a slice of lime and sambal.","laksa_perlis_image.png");
        insertRecipe(db, "Selangor", "Satay Selangor", "Satay Selangor is a popular grilled meat skewer dish from Selangor, Malaysia, known for its rich and flavorful peanut sauce.", "Step 1. Marinate meat (chicken, beef, or lamb) with a mixture of turmeric, lemongrass, shallots, and garlic; Step 2. Skewer the marinated meat on satay sticks; Step 3. Grill the skewers over hot charcoal until cooked and slightly charred; Step 4. Prepare a peanut sauce by blending roasted peanuts, coconut milk, chili paste, and tamarind juice; Step 5. Serve the grilled satay with the peanut sauce, cucumber, onion, and rice cakes.","satay_selangor_image.png");
        insertRecipe(db, "Terengganu", "Keropok Lekor Terengganu", "Keropok Lekor Terengganu is a popular snack from Terengganu, Malaysia, made from fish paste and sago flour.", "Step 1. Prepare the fish paste by blending fish fillets with salt and sago flour; Step 2. Shape the fish paste into long cylindrical logs; Step 3. Boil the fish logs until cooked; Step 4. Slice the boiled fish logs into thin pieces; Step 5. Deep-fry the slices until crispy; Step 6. Serve hot with a sweet and spicy dipping sauce.","keropok_lekor_terengganu_image.png");
        insertRecipe(db, "Johor", "Mee Rebus Johor", "Mee Rebus Johor is a popular noodle dish from Johor, Malaysia, featuring yellow noodles in a thick and flavorful gravy.", "Step 1. Boil yellow noodles until tender; Step 2. Prepare the gravy by cooking a blend of sweet potatoes, shrimp paste, and spices; Step 3. Add prawn stock and let the gravy simmer until thickened; Step 4. Serve the noodles with the hot gravy; Step 5. Garnish with bean sprouts, hard-boiled eggs, and fried shallots; Step 6. Serve with a slice of lime and sambal.","mee_rebus_johor_image.png");
        insertRecipe(db, "Kedah", "Lemang Kedah", "Lemang Kedah is a traditional dish from Kedah, Malaysia, made of glutinous rice and coconut milk cooked in bamboo.", "Step 1. Soak glutinous rice in water for several hours; Step 2. Mix the soaked rice with coconut milk and salt; Step 3. Fill bamboo tubes with the rice mixture; Step 4. Cook the filled bamboo tubes over an open fire until the rice is fully cooked; Step 5. Serve hot with rendang or serunding.","lemang_kedah_image.png");
        insertRecipe(db, "Kelantan", "Ayam Percik Kelantan", "Ayam Percik Kelantan is a grilled chicken dish from Kelantan, Malaysia, marinated in a flavorful coconut-based sauce.", "Step 1. Marinate chicken pieces with a blend of coconut milk, turmeric, and spices; Step 2. Grill the marinated chicken over hot charcoal until cooked; Step 3. Prepare a coconut sauce by cooking a blend of coconut milk, lemongrass, and chili paste; Step 4. Serve the grilled chicken with the coconut sauce; Step 5. Garnish with chopped cilantro and fried shallots.","ayam_percik_kelantan_image.png");
        insertRecipe(db, "Malacca", "Chicken Rice Ball Malacca", "Chicken Rice Ball Malacca is a unique variation of Hainanese chicken rice from Malacca, Malaysia, featuring rice shaped into balls.", "Step 1. Cook chicken with ginger and garlic until tender; Step 2. Prepare chicken rice by cooking rice with chicken broth, garlic, and pandan leaves; Step 3. Shape the cooked rice into small balls; Step 4. Serve the rice balls with sliced chicken, cucumber, and a side of chicken broth; Step 5. Garnish with chopped cilantro and fried shallots.","chicken_rice_ball_malacca_image.png");
        insertRecipe(db, "Sabah", "Tuaran Mee Sabah", "Tuaran Mee Sabah is a popular noodle dish from Sabah, Malaysia, featuring egg noodles stir-fried with various ingredients.", "Step 1. Boil egg noodles until al dente; Step 2. Stir-fry noodles with chopped garlic, soy sauce, and oyster sauce; Step 3. Add sliced pork, prawns, and vegetables; Step 4. Continue stir-frying until everything is well combined; Step 5. Serve hot with a side of pickled chilies.","tuaran_mee_sabah_image.png");
        insertRecipe(db, "Sarawak", "Laksa Sarawak", "Laksa Sarawak is a famous noodle soup dish from Sarawak, Malaysia, known for its rich and aromatic broth.", "Step 1. Prepare the broth by simmering chicken bones and prawn shells with spices; Step 2. Strain the broth and add coconut milk; Step 3. Cook rice vermicelli until tender; Step 4. Serve the noodles with the hot broth; Step 5. Garnish with shredded chicken, prawns, and bean sprouts; Step 6. Serve with a side of sambal belacan and lime.","laksa_sarawak_image.png");
        insertRecipe(db, "Negeri Sembilan", "Rendang Negeri Sembilan", "Rendang Negeri Sembilan is a slow-cooked beef dish from Negeri Sembilan, Malaysia, known for its rich and spicy flavors.", "Step 1. Marinate beef with a blend of spices and coconut milk; Step 2. Cook the marinated beef on low heat until tender and the sauce thickens; Step 3. Serve hot with steamed rice; Step 4. Garnish with fried shallots and chopped cilantro.","rendang_negeri_sembilan_image.png");
        insertRecipe(db, "Pahang", "Nasi Kebuli Pahang", "Nasi Kebuli Pahang is a fragrant rice dish from Pahang, Malaysia, cooked with spices and served with meat.", "Step 1. Cook rice with a blend of spices, coconut milk, and chicken broth; Step 2. Prepare meat curry by cooking chicken or beef with spices and coconut milk; Step 3. Serve the rice with the meat curry; Step 4. Garnish with fried shallots and chopped cilantro.","nasi kebuli_pahang_image.png");
        insertRecipe(db, "Penang", "Penang Asam Laksa", "Penang Asam Laksa is a tangy and spicy noodle soup from Penang, Malaysia, made with tamarind and fish.", "Step 1. Prepare the broth by cooking fish with tamarind juice and spices; Step 2. Cook rice noodles until tender; Step 3. Serve the noodles with the hot broth; Step 4. Garnish with sliced cucumber, pineapple, and mint leaves; Step 5. Serve with a side of sambal and a slice of lime.","penang_asam_laksa_image.png");
        insertRecipe(db, "Perak", "Mee Kari Perak", "Mee Kari Perak is a curry noodle soup from Perak, Malaysia, featuring yellow noodles in a flavorful curry broth.", "Step 1. Cook yellow noodles until tender; Step 2. Prepare the curry broth by cooking a blend of spices with coconut milk and chicken broth; Step 3. Add chicken, tofu puffs, and fish balls to the broth; Step 4. Serve the noodles with the hot curry broth; Step 5. Garnish with bean sprouts, fried shallots, and chopped cilantro.","mee_kari_perak_image.png");
        insertRecipe(db, "Perlis", "Pulut Mempelam Perlis", "Pulut Mempelam Perlis is a traditional dessert from Perlis, Malaysia, featuring glutinous rice served with ripe mangoes.", "Step 1. Cook glutinous rice with coconut milk and salt; Step 2. Slice ripe mangoes into thin pieces; Step 3. Serve the cooked glutinous rice with the sliced mangoes; Step 4. Drizzle with additional coconut milk and sprinkle with sesame seeds.","pulut_mempelam_perlis_image.png");
        insertRecipe(db, "Selangor", "Lontong Selangor", "Lontong Selangor is a traditional dish from Selangor, Malaysia, featuring compressed rice cakes served with a coconut vegetable stew.", "Step 1. Prepare compressed rice cakes by cooking rice in banana leaves; Step 2. Cook a vegetable stew with coconut milk, turmeric, and spices; Step 3. Add vegetables such as cabbage, carrots, and tofu to the stew; Step 4. Serve the rice cakes with the hot vegetable stew; Step 5. Garnish with fried shallots and chopped cilantro.","lontong_selangor_image.png");
        insertRecipe(db, "Terengganu", "Nasi Kerabu Terengganu", "Nasi Kerabu Terengganu is a traditional rice dish from Terengganu, Malaysia, served with various herbs and grilled fish.", "Step 1. Cook rice with butterfly pea flowers to achieve a blue color; Step 2. Prepare grilled fish by marinating with turmeric and salt; Step 3. Serve the blue rice with the grilled fish; Step 4. Garnish with fresh herbs, shredded coconut, and sambal; Step 5. Serve with a side of salted egg and pickled vegetables.","nasi_kerabu_terengganu_image.png");

    }


    private void insertRecipe(SQLiteDatabase db, String state, String name, String description, String steps, String imageName) {
        ContentValues values = new ContentValues();
        values.put("recipe_state", state);
        values.put("recipe_name", name);
        values.put("recipe_description", description);
        values.put("recipe_steps", steps);
        values.put("recipe_image", imageName); // Store the image name in the database
        db.insert("recipes", null, values);

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
                    String imageNameResource = cursor.getString(cursor.getColumnIndexOrThrow("recipe_image_name"));

                    Context context = null;
                    int imageResourceId = context.getResources().getIdentifier(imageNameResource, "drawable", context.getPackageName());

                    byte[] image = new byte[0];
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

    public long addRecipe(String state, String name, String description, String steps, String imageNameResource) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("recipe_state", state);
        values.put("recipe_name", name);
        values.put("recipe_description", description);
        values.put("recipe_steps", steps);
        values.put("recipe_image_name", imageNameResource); // Store the image identifier

        long result = db.insert("recipes", null, values);
        db.close(); // Closing database connection
        return result;
    }
}
