package com.example.recipeexplorer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.example.recipeexplorer.database.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView listViewStates;
    private ArrayAdapter<String> statesAdapter;
    private Button cuisineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Initialize Views
        listViewStates = findViewById(R.id.listViewStates);
        cuisineButton = findViewById(R.id.boxButton);

        // Initialize ArrayAdapter for the ListView
        statesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listViewStates.setAdapter(statesAdapter);

        // Set onClickListener for Select State Button
        Button selectStateButton = findViewById(R.id.searchButton);
        selectStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listViewStates.getVisibility() == View.GONE) {
                    // Fetch states from the database
                    List<String> states = dbHelper.getAllStates();

                    // Clear previous data in the adapter
                    statesAdapter.clear();

                    // Add fetched states to the adapter
                    statesAdapter.addAll(states);

                    // Notify the adapter that the data set has changed
                    statesAdapter.notifyDataSetChanged();

                    // Make the ListView visible
                    listViewStates.setVisibility(View.VISIBLE);
                } else {
                    // Toggle the ListView visibility
                    listViewStates.setVisibility(View.GONE);
                }
            }
        });

        // Set onItemClickListener for the ListView
        listViewStates.setOnItemClickListener((parent, view, position, id) -> {
            String selectedState = statesAdapter.getItem(position);

            if (selectedState != null) {
                // Fetch the recipe for the selected state from the database
                String recipeDescription = dbHelper.getRecipeByState(selectedState);

                // Update the cuisine button text with the recipe description
                cuisineButton.setText(recipeDescription);

                // Optionally hide the ListView after selection
                listViewStates.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        dbHelper.close(); // Close the database connection when activity is destroyed
        super.onDestroy();
    }
}




