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
        Login(null);

    }

    // Login page
    public void Login(View view){
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
                if (dbm.LoginVerification(name,pw)) {
                    setContentView(R.layout.activity_main);
                }
                else{
                    Snackbar.make(v, "Account does not exist or Password mismatched", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

}
