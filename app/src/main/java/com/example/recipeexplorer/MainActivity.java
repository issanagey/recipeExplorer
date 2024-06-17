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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Initialize Views
        ListView listViewStates = findViewById(R.id.listViewStates);
        Button selectStateButton = findViewById(R.id.searchButton);

        // Initialize ArrayAdapter for the ListView
        ArrayAdapter<String> statesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listViewStates.setAdapter(statesAdapter);

        // Set onClickListener for Select State Button
        selectStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch states from the database
                List<String> states = dbHelper.getAllStates();

                // Clear previous data in the adapter
                statesAdapter.clear();

                // Add fetched states to the adapter
                statesAdapter.addAll(states);

                // Notify the adapter that the data set has changed
                statesAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onDestroy() {
        dbHelper.close(); // Close the database connection when activity is destroyed
        super.onDestroy();
    }
}

