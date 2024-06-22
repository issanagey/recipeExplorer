package com.example.recipeexplorer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeexplorer.database.DatabaseManager;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseManager dbm = new DatabaseManager(getApplicationContext());

        // cache login
        if(dbm.IsLoggedIn()){
            Main(null);
        }
        else{
            // go to login page when starting app
            Login(null);
        }

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
                    TextView textView = findViewById(R.id.user_name);
                    textView.setText("Welcome " + dbm.GetUsername(dbm.GetCurrentUserID()));
                }
                else{
                    Snackbar.make(v, "Account does not exist or Password mismatched", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Logout user
    public void Logout(View view) {
        DatabaseManager dbm = new DatabaseManager(getApplicationContext());

        dbm.LogoutUser();

        Login(null);
    }


    // Create Account page
    public void CreateAccount(View view) {
        setContentView(R.layout.create_account);

        // input references
        EditText nameInputEditText = findViewById(R.id.nameInput);
        EditText pwInputEditText = findViewById(R.id.pwInput);
        EditText confirmPwInputEditText = findViewById(R.id.confirmPwInput);

        // get button reference
        Button button = (Button) findViewById(R.id.createAccBtn);

        // set button on click listener
        button.setOnClickListener(new View.OnClickListener() {

            // verify account creation
            public void onClick(View v) {

                // get name, pw, confirm pw
                String name = nameInputEditText.getText().toString();
                String pw = pwInputEditText.getText().toString();
                String confirmPw = confirmPwInputEditText.getText().toString();

                // error checking
                if (name.isEmpty() || pw.isEmpty() || confirmPw.isEmpty()) {
                    Snackbar.make(v, "Please fill in all fields", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (!pw.equals(confirmPw)) {
                    Snackbar.make(v, "Passwords do not match", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (pw.length() < 8) {
                    Snackbar.make(v, "Password must be at least 8 characters", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (pw.contains(" ")) {
                    Snackbar.make(v, "Password cannot contain spaces", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (name.contains(" ")) {
                    Snackbar.make(v, "Name cannot contain spaces", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (name.length() < 3) {
                    Snackbar.make(v, "Name must be at least 3 characters", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // create DatabaseManager object (has all the function we need inside)
                DatabaseManager dbm = new DatabaseManager(getApplicationContext());

                // name check
                if (dbm.CreateAccountVerification(name)) {
                    // create account
                    dbm.addUser(name ,pw, "".getBytes());
                    Snackbar.make(v, "Account Created", Snackbar.LENGTH_SHORT).show();

                    // back to login page
                    Login(null);
                }
                // name already exists
                else{
                    Snackbar.make(v, "Name already taken", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Main page
    public void Main(View view) {
        setContentView(R.layout.activity_main);

        // create DatabaseManager object (has all the function we need inside)
        DatabaseManager dbm = new DatabaseManager(getApplicationContext());

        // set text (text view)
        TextView textView = findViewById(R.id.user_name);
        textView.setText("Welcome " + dbm.GetUsername(dbm.GetCurrentUserID()));

        // get profile button reference
        Button button = (Button) findViewById(R.id.profileButton);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Profile(null);

            }
        });
    }

    // Profile page
    public void Profile(View view) {
        setContentView(R.layout.profile_placeholder);

    }

    // Challenge page
//    public void Challenge(View view, int recipe_id){

    public void Challenge(View view){
        setContentView(R.layout.challenge);

        DatabaseManager dbm = new DatabaseManager(getApplicationContext());

        // get scroll view linear layout
        LinearLayout listView = findViewById(R.id.steps_list);

        // grab challenge steps from database
        String steps = dbm.GetChallengeSteps(1); // this is just a placeholder for now, value should be passed from recipe view
        String[] list_of_steps = steps.split("; ");

        // color
        Boolean even_odd = true;

        // step count
        int step_count = 1;

        // create text view for each step
        for (String step: list_of_steps) {

            // create new text view
            TextView textView = new TextView(this);

            // set id, text, size, and padding
            textView.setId(step_count);
            step_count++;
            textView.setText(step);
            textView.setTextSize(30);
            textView.setPadding(10, 10, 10, 10);

            // alternate background color
            if(even_odd)textView.setBackgroundColor(Color.GRAY);
            else textView.setBackgroundColor(Color.DKGRAY);
            even_odd = !even_odd;

            // add step to scroll view
            listView.addView(textView);
        }

        // get button reference
        Button button = (Button) findViewById(R.id.complete_button);

        // keep count on how many steps have been completed
        final int[] steps_completed_count = {0};
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                steps_completed_count[0]++;

                if(steps_completed_count[0] <= list_of_steps.length){
                    if(steps_completed_count[0] % 2 == 0) findViewById(steps_completed_count[0]).setBackgroundColor(Color.GREEN);
                    if(steps_completed_count[0] % 2 == 1) findViewById(steps_completed_count[0]).setBackgroundColor(Color.YELLOW);

                    if(steps_completed_count[0] == list_of_steps.length){
                        Button clickedButton = (Button) v;
                        clickedButton.setText("Complete Challenge");
                    }

                }

                else{
                    Main(null);
                }

            }

        });

    }


}
