package com.example.recipeexplorer.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeexplorer.MainActivity;
import com.example.recipeexplorer.R;
import com.example.recipeexplorer.adapters.RecipeAdapter;
import com.example.recipeexplorer.database.DatabaseManager;
import com.example.recipeexplorer.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class CookbookActivity extends AppCompatActivity {

    private ListView listViewRecipes;
    private RecipeAdapter recipeAdapter;


    private DatabaseManager db = new DatabaseManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cook_book);

        Button mainButton = findViewById(R.id.backButton);
        mainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CookbookActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        // initialise list
        listViewRecipes = findViewById(R.id.listViewRecipes);

        // Initialize custom adapter for the ListView
        recipeAdapter = new RecipeAdapter(this, new ArrayList<>());
        listViewRecipes.setAdapter(recipeAdapter);


        // fetch recipes from database
        List<Recipe> recipes = db.GetRecipeListFromCookBook(db.GetCurrentUserID());

        // Check if recipes list is not empty
        if (!recipes.isEmpty()) {
            // Clear previous data in the adapter
            recipeAdapter.clear();

            // Add fetched recipes to the adapter
            recipeAdapter.addAll(recipes);

            // Notify the adapter that the data set has changed
            recipeAdapter.notifyDataSetChanged();
        }

        listViewRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected recipe from the adapter
                Recipe selectedRecipe = recipeAdapter.getItem(position);

                // Prepare intent to start RecipeViewActivity
                Intent intent = new Intent(CookbookActivity.this, RecipeViewActivity.class);
                intent.putExtra("RECIPE_ID", selectedRecipe.getId()); // Use "RECIPE_ID" here

                // Start the activity
                startActivity(intent);
            }
        });
    }

}
