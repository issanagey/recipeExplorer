package com.example.recipeexplorer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    private static final String CREATE_TABLE_COOKBOOKS = "CREATE TABLE cookbooks (user_id INTEGER PRIMARY KEY, recipe_id INTEGER)";

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

        // test
        values = new ContentValues();
        values.put("name", "test");
        values.put("password", "12345678");
        values.put("challenge_completed", 0);
        db.insert("users", null, values);

        // recipes
        // Johor - Laksa Johor
        values = new ContentValues();
        values.put("recipe_state", "Johor");
        values.put("recipe_name", "Laksa Johor");
        values.put("recipe_steps", "Step 1. Cook spaghetti until al dente; Step 2. Prepare a spicy fish gravy by blending shallots, garlic, dried chilies, shrimp paste, and tamarind pulp; Step 3. Cook the blended mixture with coconut milk, fish, and spices until fragrant; Step 4. Serve the cooked spaghetti with the spicy fish gravy; Step 5. Garnish with shredded cucumber, pineapple, and mint leaves; Step 6. Serve hot with a slice of lime and sambal.");
        values.put("recipe_description", "Laksa Johor is a unique variation of laksa from Johor, Malaysia, featuring spaghetti noodles and a spicy fish gravy.");
        db.insert("recipes", null, values);

        // Kedah - Ayam Pongteh
        values = new ContentValues();
        values.put("recipe_state", "Kedah");
        values.put("recipe_name", "Ayam Pongteh Kedah");
        values.put("recipe_steps", "Step 1. Marinate chicken pieces with soy sauce, palm sugar, and fermented soybean paste (tauchu); Step 2. Heat oil in a pot and sauté shallots, garlic, and ginger until fragrant; Step 3. Add the marinated chicken and cook until browned; Step 4. Pour in water and simmer until chicken is tender; Step 5. Serve hot with steamed rice; Step 6. Garnish with fried shallots and chopped cilantro.");
        values.put("recipe_description", "Ayam Pongteh Kedah is a traditional Nyonya chicken stew dish from Kedah, Malaysia, known for its savory and slightly sweet flavor.");
        db.insert("recipes", null, values);

        // Kelantan - Nasi Dagang
        values = new ContentValues();
        values.put("recipe_state", "Kelantan");
        values.put("recipe_name", "Nasi Dagang Kelantan");
        values.put("recipe_steps", "Step 1. Cook a mixture of glutinous rice and regular rice with coconut milk, ginger, and fenugreek seeds until fluffy; Step 2. Prepare fish curry by cooking fish with spices, coconut milk, and tamarind juice; Step 3. Serve the cooked rice with the fish curry; Step 4. Garnish with sliced boiled eggs, pickled vegetables, and fried shaved coconut; Step 5. Serve hot.");
        values.put("recipe_description", "Nasi Dagang Kelantan is a traditional rice dish from Kelantan, Malaysia, served with flavorful fish curry.");
        db.insert("recipes", null, values);

        // Malacca - Satay Celup
        values = new ContentValues();
        values.put("recipe_state", "Malacca");
        values.put("recipe_name", "Satay Celup Malacca");
        values.put("recipe_steps", "Step 1. Prepare a boiling pot of satay sauce made from peanuts, coconut milk, spices, and chili paste; Step 2. Skewer various ingredients such as meat, seafood, and vegetables on satay sticks; Step 3. Dip the skewered ingredients into the boiling satay sauce until cooked; Step 4. Serve hot with rice cakes and additional satay sauce for dipping.");
        values.put("recipe_description", "Satay Celup Malacca is a popular hotpot dish from Malacca, Malaysia, featuring skewered ingredients cooked in a flavorful peanut sauce.");
        db.insert("recipes", null, values);

        // Sabah - Hinava
        values = new ContentValues();
        values.put("recipe_state", "Sabah");
        values.put("recipe_name", "Hinava Sabah");
        values.put("recipe_steps", "Step 1. Prepare the fish by thinly slicing fresh fish fillets; Step 2. Marinate the fish slices with lime juice, sliced shallots, grated ginger, and chopped chilies; Step 3. Let the fish marinate for a few minutes; Step 4. Garnish with sliced bitter gourd, julienned ginger, and chopped coriander; Step 5. Serve immediately as a refreshing appetizer or side dish.");
        values.put("recipe_description", "Hinava Sabah is a traditional Kadazandusun dish from Sabah, Malaysia, made with marinated raw fish.");
        db.insert("recipes", null, values);

        // Sarawak - Sarawak Kolo Mee
        values = new ContentValues();
        values.put("recipe_state", "Sarawak");
        values.put("recipe_name", "Sarawak Kolo Mee");
        values.put("recipe_steps", "Step 1. Cook egg noodles until al dente; Step 2. Toss cooked noodles in shallot oil and light soy sauce; Step 3. Top with slices of char siu (barbecued pork), minced pork, and fried shallots; Step 4. Serve hot with pickled chili and a side of clear soup.");
        values.put("recipe_description", "Sarawak Kolo Mee is a popular noodle dish from Sarawak, Malaysia, featuring springy egg noodles tossed in shallot oil and served with various toppings.");
        db.insert("recipes", null, values);

        // Negeri Sembilan - Masak Lemak Cili Api
        values = new ContentValues();
        values.put("recipe_state", "Negeri Sembilan");
        values.put("recipe_name", "Masak Lemak Cili Api Negeri Sembilan");
        values.put("recipe_steps", "Step 1. Prepare a spice paste by blending shallots, garlic, ginger, lemongrass, and dried chilies; Step 2. Cook the spice paste with coconut milk, turmeric leaves, and tamarind juice until fragrant; Step 3. Add chicken or fish and simmer until cooked through; Step 4. Season with salt and sugar to taste; Step 5. Serve hot with steamed rice; Step 6. Garnish with sliced chili and torch ginger flower.");
        values.put("recipe_description", "Masak Lemak Cili Api Negeri Sembilan is a spicy and creamy coconut-based dish from Negeri Sembilan, Malaysia, known for its bold flavors.");
        db.insert("recipes", null, values);

        // Pahang - Ikan Patin Tempoyak
        values = new ContentValues();
        values.put("recipe_state", "Pahang");
        values.put("recipe_name", "Ikan Patin Tempoyak Pahang");
        values.put("recipe_steps", "Step 1. Prepare tempoyak (fermented durian paste) by mixing durian flesh with salt; Step 2. Cook the tempoyak with coconut milk, turmeric leaves, and spices until fragrant; Step 3. Add fish such as patin (catfish) and cook until fish is tender; Step 4. Add tamarind paste for sourness; Step 5. Serve hot with steamed rice; Step 6. Garnish with shredded turmeric leaves and torch ginger flower; Step 7. Serve with ulam (raw vegetables) and sambal belacan.");
        values.put("recipe_description", "Ikan Patin Tempoyak Pahang is a creamy and tangy fish curry dish from Pahang, Malaysia, made with fermented durian paste.");
        db.insert("recipes", null, values);

        // Penang - Char Kway Teow
        values = new ContentValues();
        values.put("recipe_state", "Penang");
        values.put("recipe_name", "Char Kway Teow Penang");
        values.put("recipe_steps", "Step 1. Heat oil in a wok and fry chopped garlic until fragrant; Step 2. Add fresh flat rice noodles and stir-fry until slightly charred; Step 3. Push noodles to the side and add more oil; Step 4. Crack an egg into the wok and scramble; Step 5. Add prawns, cockles, and Chinese sausage (lap cheong); Step 6. Stir-fry everything together with bean sprouts, chives, and chili paste; Step 7. Season with soy sauce, fish sauce, and dark soy sauce; Step 8. Serve hot garnished with chopped spring onions and a lime wedge.");
        values.put("recipe_description", "Char Kway Teow Penang is a famous stir-fried noodle dish from Penang, Malaysia, known for its smoky flavor and savory sauce.");
        db.insert("recipes", null, values);

        // Perak - Ipoh Hor Fun
        values = new ContentValues();
        values.put("recipe_state", "Perak");
        values.put("recipe_name", "Ipoh Hor Fun Perak");
        values.put("recipe_steps", "Step 1. Blanch flat rice noodles in hot water until cooked; Step 2. Prepare chicken and prawn broth by simmering chicken bones, prawn shells, and aromatics; Step 3. Cook shredded chicken and prawns separately; Step 4. Serve the blanched noodles in the hot broth; Step 5. Top with cooked chicken, prawns, and choy sum (Chinese mustard greens); Step 6. Garnish with chopped spring onions and fried shallots; Step 7. Serve hot with chili sauce and calamansi lime.");
        values.put("recipe_description", "Ipoh Hor Fun Perak is a comforting noodle soup dish from Perak, Malaysia, featuring silky smooth rice noodles in a flavorful broth.");
        db.insert("recipes", null, values);

        // Perlis - Ayam Kicap Madu
        values = new ContentValues();
        values.put("recipe_state", "Perlis");
        values.put("recipe_name", "Ayam Kicap Madu Perlis");
        values.put("recipe_steps", "Step 1. Marinate chicken pieces with soy sauce, honey, and ginger paste; Step 2. Heat oil in a pan and fry the marinated chicken until golden brown; Step 3. Remove chicken from the pan and set aside; Step 4. In the same pan, sauté sliced onions and garlic until caramelized; Step 5. Return the fried chicken to the pan and toss with the onions and garlic; Step 6. Serve hot with steamed rice; Step 7. Garnish with chopped spring onions.");
        values.put("recipe_description", "Ayam Kicap Madu Perlis is a sweet and savory honey soy chicken dish from Perlis, Malaysia, known for its tender and flavorful meat.");
        db.insert("recipes", null, values);

        // Selangor - Lontong
        values = new ContentValues();
        values.put("recipe_state", "Selangor");
        values.put("recipe_name", "Lontong Selangor");
        values.put("recipe_steps", "Step 1. Prepare rice cakes (lontong) by boiling rice wrapped in banana leaves until cooked; Step 2. Prepare a spicy coconut gravy by blending shallots, garlic, ginger, dried chilies, lemongrass, and coconut milk; Step 3. Cook the blended mixture until fragrant and thickened; Step 4. Serve the rice cakes with the spicy coconut gravy; Step 5. Garnish with sliced boiled eggs, fried tofu, and crispy tempeh; Step 6. Serve hot with sambal and fried anchovies.");
        values.put("recipe_description", "Lontong Selangor is a traditional Malaysian dish from Selangor, consisting of rice cakes served with a spicy coconut gravy.");
        db.insert("recipes", null, values);

        // Terengganu - Keropok Lekor
        values = new ContentValues();
        values.put("recipe_state", "Terengganu");
        values.put("recipe_name", "Keropok Lekor Terengganu");
        values.put("recipe_steps", "Step 1. Mix fish paste with sago flour, salt, and water to form a dough; Step 2. Shape the dough into long sausage-like shapes; Step 3. Boil the shaped dough in hot water until they float to the surface; Step 4. Remove from water and drain excess moisture; Step 5. Deep fry the boiled keropok until golden brown and crispy; Step 6. Serve hot with chili sauce or sweet and sour sauce; Step 7. Enjoy as a snack or appetizer.");
        values.put("recipe_description", "Keropok Lekor Terengganu is a traditional fish snack from Terengganu, Malaysia, known for its chewy texture and savory flavor.");
        db.insert("recipes", null, values);

        // Kuala Lumpur - Roti Canai
        values = new ContentValues();
        values.put("recipe_state", "Kuala Lumpur");
        values.put("recipe_name", "Roti Canai Kuala Lumpur");
        values.put("recipe_steps", "Step 1. Prepare dough by mixing flour, water, salt, and ghee until smooth; Step 2. Divide the dough into balls and let them rest; Step 3. Flatten each ball into a thin sheet and stretch it out; Step 4. Fold and coil the stretched dough into a round shape; Step 5. Fry the dough on a hot griddle until golden and crispy on both sides; Step 6. Serve hot with dhal curry or chicken curry.");
        values.put("recipe_description", "Roti Canai Kuala Lumpur is a classic Malaysian flatbread known for its flaky layers and served with flavorful curry.");
        db.insert("recipes", null, values);

        // Labuan - Ikan Masak Asam Pedas
        values = new ContentValues();
        values.put("recipe_state", "Labuan");
        values.put("recipe_name", "Ikan Masak Asam Pedas Labuan");
        values.put("recipe_steps", "Step 1. Prepare the fish by cleaning and cutting into steaks; Step 2. Blend shallots, garlic, ginger, dried chilies, and tamarind paste to make the spice paste; Step 3. Heat oil in a pan and sauté the spice paste until fragrant; Step 4. Add water, tamarind juice, tomatoes, and okra; Step 5. Simmer until the vegetables are cooked; Step 6. Add fish steaks and cook until fish is tender; Step 7. Season with salt and sugar to taste; Step 8. Garnish with chopped cilantro; Step 9. Serve hot with steamed rice.");
        values.put("recipe_description", "Ikan Masak Asam Pedas Labuan is a tangy and spicy fish dish from Labuan, Malaysia, known for its sour and savory flavors.");
        db.insert("recipes", null, values);

        // Putrajaya - Ayam Masak Merah
        values = new ContentValues();
        values.put("recipe_state", "Putrajaya");
        values.put("recipe_name", "Ayam Masak Merah Putrajaya");
        values.put("recipe_steps", "Step 1. Marinate chicken pieces with turmeric powder, salt, and lime juice; Step 2. Heat oil in a pan and fry the marinated chicken until golden brown; Step 3. Remove chicken from the pan and set aside; Step 4. In the same pan, sauté blended shallots, garlic, ginger, and chili paste until fragrant; Step 5. Add tomato puree, chili sauce, sugar, and salt; Step 6. Simmer until the sauce thickens; Step 7. Return the fried chicken to the pan and coat with the sauce; Step 8. Garnish with sliced onions and cilantro; Step 9. Serve hot with steamed rice.");
        values.put("recipe_description", "Ayam Masak Merah Putrajaya is a spicy tomato-based chicken dish from Putrajaya, Malaysia, known for its vibrant red color and bold flavors.");
        db.insert("recipes", null, values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if they exist
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS login");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS cookbook");

        // Create tables again
        onCreate(db);
    }
}
