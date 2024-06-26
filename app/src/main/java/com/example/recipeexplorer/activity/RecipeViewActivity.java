package com.example.recipeexplorer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeexplorer.R;
import com.example.recipeexplorer.database.DatabaseManager;
import com.example.recipeexplorer.models.Recipe;

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
        setContentView(R.layout.activity_recipe_view);

        // Initialize views
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
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeViewActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Add to Cookbook button listener
        addToCookbookButton.setOnClickListener(v -> addToCookbook());

        // Start Challenge button listener
        startChallengeButton.setOnClickListener(v -> startChallenge());
    }

    private void handleBackPress() {
        // Navigate to the previous activity
        finish();
    }

    private void loadRecipeData() {
        // Fetch recipe ID from intent extras
        Intent intent = getIntent();
        int recipeId = intent.getIntExtra("RECIPE_ID", -1);

        // Query the database for recipe details
        Recipe recipe = databaseManager.getRecipeById(recipeId);

        if (recipe != null) {
            // Populate UI with recipe details
            recipeState.setText(recipe.getState());
            recipeName.setText(recipe.getTitle());
            recipeDescription.setText(recipe.getDescription());
        } else {
            // Handle case where recipe is not found or user doesn't have access
            Log.e("RecipeViewActivity", "Recipe not found in database for ID: " + recipeId);
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

    private void startChallenge() {
        // Fetch recipe ID from intent extras
        Intent intent = getIntent();
        int recipeId = intent.getIntExtra("RECIPE_ID", -1);

        // Fetch recipe steps (if available) using DatabaseManager
        String recipeSteps = databaseManager.getRecipeSteps(recipeId);

        // Fetch recipe title (already fetched in loadRecipeData())

        // Check if recipeSteps or recipeTitle is null
        if (recipeSteps == null || recipeName.getText() == null) {
            // Handle case where steps or title are null or not found
            Toast.makeText(this, "Recipe steps or title not found", Toast.LENGTH_SHORT).show();
            return;
        }

        String recipeTitle = recipeName.getText().toString();

        // Prepare intent to start ChallengeActivity
        Intent challengeIntent = new Intent(RecipeViewActivity.this, ChallengeActivity.class);
        challengeIntent.putExtra("recipe_id", recipeId);
        challengeIntent.putExtra("recipe_steps", recipeSteps);
        challengeIntent.putExtra("recipe_title", recipeTitle);
        startActivity(challengeIntent);
    }
}
