package com.example.recipeexplorer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

                String name = nameInputEditText.getText().toString();
                String pw = pwInputEditText.getText().toString();

                // login successful
                if (name.equals("admin") && pw.equals("admin")) {
                    Snackbar.make(v, "Login successful", Snackbar.LENGTH_SHORT).show();
                    setContentView(R.layout.activity_main);
                }
                else{
                    Snackbar.make(v, "Login Failed", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }


}
