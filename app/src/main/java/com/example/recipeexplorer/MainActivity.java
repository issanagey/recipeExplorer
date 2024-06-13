package com.example.recipeexplorer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeexplorer.database.DatabaseManager;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private DatabaseManager dbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbm = new DatabaseManager(getApplicationContext());

        // Check if a user is already logged in
        if (dbm.IsLoggedIn()) {
            // Go to main activity
            Main();
        } else {
            // Go to login page
            Login();
        }
    }

    // Login page
    public void Login() {
        setContentView(R.layout.login);

        // Input references
        EditText nameInputEditText = findViewById(R.id.nameInput);
        EditText pwInputEditText = findViewById(R.id.pwInput);

        // Get button reference
        Button button = findViewById(R.id.loginButton);

        // Set button on click listener
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Get name and password
                String name = nameInputEditText.getText().toString();
                String pw = pwInputEditText.getText().toString();

                // Verify login
                if (dbm.LoginVerification(name, pw)) {
                    Main();
                } else {
                    Snackbar.make(v, "Account does not exist or Password mismatched", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Main activity
    public void Main() {
        setContentView(R.layout.activity_main);

        // Display the logged-in user's name
        TextView textView = findViewById(R.id.user_name);
        textView.setText("Welcome " + dbm.GetUsername(dbm.GetCurrentUserID()));

        // Set logout button listener
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Logout();
            }
        });
    }

    // Logout user
    public void Logout() {
        dbm.LogoutUser();
        Login();
    }

    // Create Account page
    public void CreateAccount(View view) {
        setContentView(R.layout.create_account);

        // Input references
        EditText nameInputEditText = findViewById(R.id.nameInput);
        EditText pwInputEditText = findViewById(R.id.pwInput);

        // Get button reference
        Button button = findViewById(R.id.createAccBtn);

        // Set button on click listener
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Get name and password
                String name = nameInputEditText.getText().toString();
                String pw = pwInputEditText.getText().toString();

                // Verify if the account can be created
                if (dbm.CreateAccountVerification(name)) {
                    // Add new user to the database
                    dbm.addUser(name, pw, null); // Assuming no profile picture
                    Snackbar.make(v, "Account created successfully", Snackbar.LENGTH_SHORT).show();
                    Login();
                } else {
                    Snackbar.make(v, "Account already exists", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}

