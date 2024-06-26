package com.example.recipeexplorer;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeexplorer.database.DatabaseHelper;
import com.example.recipeexplorer.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Spinner spinnerStates;
    private ListView listViewRecipes;
    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                List<Recipe> recipes = dbHelper.getAllRecipesByState(selectedState);

                // Clear previous data in the adapter
                recipeAdapter.clear();

                // Add fetched recipes to the adapter
                recipeAdapter.addAll(recipes);

                // Notify the adapter that the data set has changed
                recipeAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onDestroy() {
        dbHelper.close(); // Close the database connection when activity is destroyed
        super.onDestroy();
}
}
