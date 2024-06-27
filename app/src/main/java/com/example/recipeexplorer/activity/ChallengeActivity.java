package com.example.recipeexplorer.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeexplorer.MainActivity;
import com.example.recipeexplorer.R;
import com.example.recipeexplorer.database.DatabaseManager;

public class ChallengeActivity extends AppCompatActivity {

    private LinearLayout listView;
    private Button completeButton;
    private int stepsCompletedCount = 0;
    private String[] listOfSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challenge);

        // Initialize views
        listView = findViewById(R.id.steps_list);
        completeButton = findViewById(R.id.complete_button);

        DatabaseManager dbm = new DatabaseManager(getApplicationContext());

        // Retrieve recipe details from intent extras
        int recipeId = getIntent().getIntExtra("recipe_id", -1); // Replace with correct type if needed
        String recipeTitle = getIntent().getStringExtra("recipe_title");
        String steps = getIntent().getStringExtra("recipe_steps");

        // Check if steps or title is null
        if (steps == null || recipeTitle == null) {
            // Handle case where steps or title are null or not found
            Toast.makeText(this, "Recipe steps or title not found", Toast.LENGTH_SHORT).show();
            finish(); // Finish the activity if steps or title are not available
            return;
        }

        // Set title of the activity
        setTitle(recipeTitle);

        // Split the steps into an array
        listOfSteps = steps.split(";");

        // Display all steps initially
        displayAllSteps();

        // Highlight the first step initially
        updateStepHighlight(0);

        // Set onClickListener for Complete Button
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepsCompletedCount++;

                if (stepsCompletedCount < listOfSteps.length) {
                    // Update UI to highlight next step
                    updateStepHighlight(stepsCompletedCount);
                } else if (stepsCompletedCount == listOfSteps.length) {
                    // Handle challenge completion
                    dbm.AddChallengeCompleted(dbm.GetCurrentUserID());
                    completeButton.setText("Challenge Completed");
                    completeButton.setEnabled(false); // Disable button after completion

                    // Optionally, you can navigate back to MainActivity after a delay
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Intent intent = new Intent(ChallengeActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();  // Finish the activity and return to the main activity
                                }
                            },
                            2000 // Delay in milliseconds before navigating back
                    );
                }
            }
        });

        // Set onClickListener for Back Button
        Button backButton = findViewById(R.id.main_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set onClickListener for Profile Button
        Button profileButton = findViewById(R.id.profile_button);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChallengeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    // Method to display all steps initially
    private void displayAllSteps() {
        for (int i = 0; i < listOfSteps.length; i++) {
            TextView textView = new TextView(this);
            textView.setText(listOfSteps[i]);
            textView.setTextSize(20);
            textView.setPadding(10, 10, 10, 10);

            // Set background color (initially all dim)
            textView.setBackgroundColor(Color.LTGRAY);
            listView.addView(textView);
        }
    }

    // Method to update step highlight based on completion count
    private void updateStepHighlight(int stepIndex) {
        if (stepIndex >= 0 && stepIndex < listOfSteps.length) {
            // Get the TextView at stepIndex
            TextView textView = (TextView) listView.getChildAt(stepIndex);

            // Update background color to indicate completion progress
            textView.setBackgroundColor(Color.GREEN); // Or any other highlight color
        }
    }
}
