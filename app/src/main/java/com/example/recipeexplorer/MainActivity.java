package com.example.recipeexplorer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button searchButton = findViewById(R.id.searchButton);
        TextView boxTextView = findViewById(R.id.boxTextView);
        Button cuisineButton = findViewById(R.id.cuisineButton);
        TextView secondBoxTextView = findViewById(R.id.secondBoxTextView);

        searchButton.setOnClickListener(v -> {
            // Display a message
            boxTextView.setText("Your message here");

            // Make the Cuisine Button and Second Box TextView visible
            cuisineButton.setVisibility(View.VISIBLE);
            secondBoxTextView.setVisibility(View.VISIBLE);
        });

        cuisineButton.setOnClickListener(v -> {
            // Display another message
            secondBoxTextView.setText("Another message here");
        });
    }
}
