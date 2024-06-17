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
            // Display the word "Penang"
            boxTextView.setText(getString(R.string.penang));

            // Make the Cuisine Button and Second Box TextView visible with a fade-in animation
            cuisineButton.setVisibility(View.VISIBLE);
            cuisineButton.setAlpha(0f);
            cuisineButton.animate().alpha(1f).setDuration(300);

            secondBoxTextView.setVisibility(View.VISIBLE);
            secondBoxTextView.setAlpha(0f);
            secondBoxTextView.animate().alpha(1f).setDuration(300);
        });

        cuisineButton.setOnClickListener(v -> {
            // Display the word "Laksa"
            secondBoxTextView.setText(getString(R.string.laksa));
        });
    }
}

