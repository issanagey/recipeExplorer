package com.example.recipeexplorer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeexplorer.MainActivity;
import com.example.recipeexplorer.R;
import com.example.recipeexplorer.adapters.RecipeAdapter;
import com.example.recipeexplorer.database.DatabaseHelper;
import com.example.recipeexplorer.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Spinner spinnerStates;
    private ListView listViewRecipes;
    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Initialize Views
        spinnerStates = findViewById(R.id.spinnerStates);
        listViewRecipes = findViewById(R.id.listViewRecipes);

        // Load states into Spinner
        List<String> states = dbHelper.getAllStates();
        ArrayAdapter<String> statesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, states);
        statesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStates.setAdapter(statesAdapter);

        // Initialize custom adapter for the ListView
        recipeAdapter = new RecipeAdapter(this, new ArrayList<>());
        listViewRecipes.setAdapter(recipeAdapter);

        // Set onClickListener for Select State Button
        Button selectStateButton = findViewById(R.id.searchButton);
        selectStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedState = spinnerStates.getSelectedItem().toString();
                // Fetch recipes from the database
                List<Recipe> recipes = dbHelper.getRecipesByState(selectedState);

                // Check if recipes list is not empty
                if (!recipes.isEmpty()) {
                    // Clear previous data in the adapter
                    recipeAdapter.clear();

                    // Add fetched recipes to the adapter
                    recipeAdapter.addAll(recipes);

                    // Notify the adapter that the data set has changed
                    recipeAdapter.notifyDataSetChanged();
                } else {
                    // Handle case where no recipes were found
                    Toast.makeText(RecipeActivity.this, "No recipes found for state: " + selectedState, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set onClickListener for Return Button
        ImageButton returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for Profile Button
        ImageButton profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for Add to Cookbook Button
        Button addToCookbookButton = findViewById(R.id.addToCookbookButton);
        addToCookbookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedState = spinnerStates.getSelectedItem().toString();
                // Implement logic to add current state's ID and user ID to the cookbook table
                // Example: dbHelper.addToCookbook(selectedState, userId);
                // Replace `userId` with the actual user ID from your session or database.
            }
        });

        // Set onClickListener for Start Challenge Button
        Button startChallengeButton = findViewById(R.id.startChallengeButton);
        startChallengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for selected item in the ListView
                int position = listViewRecipes.getCheckedItemPosition();
                if (position != AdapterView.INVALID_POSITION) {
                    // Get the selected recipe from the adapter
                    Recipe selectedRecipe = recipeAdapter.getItem(position);

                    // Prepare intent to start ChallengeActivity
                    Intent intent = new Intent(RecipeActivity.this, ChallengeActivity.class);

                    // Pass recipe details to ChallengeActivity
                    intent.putExtra("recipe_id", selectedRecipe.getId());
                    intent.putExtra("recipe_title", selectedRecipe.getTitle());
                    intent.putExtra("recipe_steps", selectedRecipe.getSteps());

                    // Start the activity
                    startActivity(intent);
                } else {
                    // Handle the case where no item is selected
                    Toast.makeText(RecipeActivity.this, "Please select a recipe", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set item click listener for ListView to enable selection
        listViewRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toggle selection on click
                listViewRecipes.setItemChecked(position, true);
                recipeAdapter.setSelectedPosition(position); // Update selected position in adapter
            }
        });
    }

    @Override
    protected void onDestroy() {
        dbHelper.close(); // Close the database connection when activity is destroyed
        super.onDestroy();
    }
}
