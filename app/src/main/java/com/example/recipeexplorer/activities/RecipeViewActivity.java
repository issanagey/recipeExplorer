package com.example.recipeexplorer.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeexplorer.R;
import com.example.recipeexplorer.database.DatabaseManager;
import com.example.recipeexplorer.model.Recipe;

public class RecipeViewActivity extends AppCompatActivity {

    private ImageButton backButton;
    private ImageButton profileButton;
    private ImageView recipeImageView;
    private TextView recipeName;
    private TextView recipeState;
    private TextView recipeDescription;
    private Button addToCookbookButton;
    private Button startChallengeButton;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_view);

        backButton = findViewById(R.id.backButton);
        profileButton = findViewById(R.id.profileButton);
        recipeImageView = findViewById(R.id.recipeImageView);
        recipeName = findViewById(R.id.recipeName);
        recipeState = findViewById(R.id.recipeState);
        recipeDescription = findViewById(R.id.recipeDescription);
        addToCookbookButton = findViewById(R.id.addToCookbookButton);
        startChallengeButton = findViewById(R.id.startChallengeButton);
        databaseManager = new DatabaseManager(this);

        // Load recipe data
        loadRecipeData();

        // Back button listener
        backButton.setOnClickListener(v -> handleBackPress());

        // Profile button listener
//        profileButton.setOnClickListener(v -> {
//            Intent intent = new Intent(RecipeViewActivity.this, ProfileActivity.class);
//            startActivity(intent);
//        });

        // Add to Cookbook button listener
        addToCookbookButton.setOnClickListener(v -> addToCookbook());

        // Start Challenge button listener
//        startChallengeButton.setOnClickListener(v -> startChallenge());
    }

    private void handleBackPress() {
        // Navigate to the previous activity
        finish();
    }

    private void loadRecipeData() {
        // Fetch recipe ID from intent extras
        Intent intent = getIntent();
        int recipeId = intent.getIntExtra("RECIPE_ID", -1);

        // Check if user is logged in
        if (!databaseManager.IsLoggedIn()) {
            // Handle case where user is not logged in
            Toast.makeText(this, "Please log in to view recipe details", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current logged-in user ID
        long userId = databaseManager.GetCurrentUserID();

        // Query the database for recipe details
        Recipe recipe = databaseManager.getRecipeById(recipeId);

        if (recipe != null) {
            // Populate UI with recipe details
            recipeState.setText(recipe.getState());
            recipeName.setText(recipe.getName());
            recipeDescription.setText(recipe.getDescription());
        } else {
            // Handle case where recipe is not found or user doesn't have access
            Toast.makeText(this, "Recipe not found or you do not have access", Toast.LENGTH_SHORT).show();
        }
    }

    private void addToCookbook() {
        // Fetch the currently logged-in user's ID
        long userId = databaseManager.GetCurrentUserID();

        // Fetch recipe ID from intent extras
        Intent intent = getIntent();
        int recipeId = intent.getIntExtra("RECIPE_ID", -1);

        // Add recipe to user's cookbook using DatabaseManager method
        databaseManager.addRecipeToCookbook(userId, recipeId);

        Toast.makeText(this, "Recipe added to cookbook", Toast.LENGTH_SHORT).show();
    }

//    private void startChallenge() {
//        // Fetch recipe ID from intent extras
//        Intent intent = getIntent();
//        int recipeId = intent.getIntExtra("RECIPE_ID", -1);
//
//        // Navigate to ChallengeActivity and pass the recipe ID
//        Intent challengeIntent = new Intent(RecipeViewActivity.this, ChallengeActivity.class);
//        challengeIntent.putExtra("RECIPE_ID", recipeId);
//        startActivity(challengeIntent);
//    }
}

