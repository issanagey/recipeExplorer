package com.example.recipeexplorer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeexplorer.database.DatabaseManager;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // go to login page when starting app
        setContentView(R.layout.login);

        // input references
        EditText nameInputEditText = findViewById(R.id.nameInput);
        EditText pwInputEditText = findViewById(R.id.pwInput);

        // get button reference
        Button button = (Button) findViewById(R.id.loginButton);

        // set button on click listener
        button.setOnClickListener(new View.OnClickListener() {

            // verify login
            public void onClick(View v) {

                // get name and pw
                String name = nameInputEditText.getText().toString();
                String pw = pwInputEditText.getText().toString();

                // create DatabaseManager object (has all the function we need inside)
                DatabaseManager dbm = new DatabaseManager(getApplicationContext());

                // login successful
                if (dbm.LoginVerification(name, pw)) {
                    Snackbar.make(v, "Login successful", Snackbar.LENGTH_SHORT).show();
                    setContentView(R.layout.activity_main);
                }
                else{
                    Snackbar.make(v, "Login Failed", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    // Create new account (login)
    public void CreateAccButtonClick(View view) {
        setContentView(R.layout.create_account);
    }

    // Redirect to login page (from create account page)
    public void RedirectToLogin(View view){
        setContentView(R.layout.login);
    }

}
