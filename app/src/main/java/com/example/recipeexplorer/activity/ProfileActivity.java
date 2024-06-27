package com.example.recipeexplorer.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeexplorer.R;
import com.example.recipeexplorer.database.DatabaseManager;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {
    private ImageView myImage;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        DatabaseManager dbm = new DatabaseManager(getApplicationContext());
        myImage = findViewById(R.id.user_avatar);
        TextView userName = findViewById(R.id.user_name);
        TextView recipesTried = findViewById(R.id.stat_recipes_tried);
        TextView achievementEarned = findViewById(R.id.achievements_stat);
        myImage.setImageBitmap(dbm.GetUserProfilePicture(dbm.GetCurrentUserID()));
        userName.setText(dbm.GetUsername(dbm.GetCurrentUserID()));
        Integer Num = dbm.GetUserRecipesTried(dbm.GetCurrentUserID());
        recipesTried.setText(Integer.toString(Num));
        if (Num <= 0) {
            achievementEarned.setText("0");
        } else if (Num <= 3) {
            achievementEarned.setText("1");
        } else if (Num <= 6) {
            achievementEarned.setText("2");
        } else {
            achievementEarned.setText("3");
        }

        Button editProfileButton = findViewById(R.id.btn_edit_profile);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    // Edit Profile Activity
    public static class EditProfileActivity extends AppCompatActivity {
        private ImageView myImage;
        private Uri imageUri;
        private static final int PICK_IMAGE_REQUEST = 1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.editprofile);
            DatabaseManager dbm = new DatabaseManager(getApplicationContext());
            myImage = findViewById(R.id.edit_user_avatar);
            myImage.setImageBitmap(dbm.GetUserProfilePicture(dbm.GetCurrentUserID()));

            // Set onClickListeners for buttons
            Button changePictureButton = findViewById(R.id.btn_change_picture);
            changePictureButton.setOnClickListener(v -> onChangePictureClicked());

            Button saveChangesButton = findViewById(R.id.btn_save_changes);
            saveChangesButton.setOnClickListener(v -> onSaveChangesClicked());

            Button cancelButton = findViewById(R.id.btn_cancel);
            cancelButton.setOnClickListener(v -> onCancelClicked());
        }

        public void onChangePictureClicked() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    myImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void onSaveChangesClicked() {
            EditText usernameEditText = findViewById(R.id.edit_user_name);
            EditText currentPasswordEditText = findViewById(R.id.edit_current_password);
            EditText newPasswordEditText = findViewById(R.id.edit_new_password);
            EditText confirmPasswordEditText = findViewById(R.id.edit_confirm_password);
            ImageView changeProfilePicture = findViewById(R.id.edit_user_avatar);

            String newUsername = usernameEditText.getText().toString().trim();
            String currentPassword = currentPasswordEditText.getText().toString().trim();
            String newPassword = newPasswordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            Drawable drawable = changeProfilePicture.getDrawable();
            boolean profilePictureChanged = drawable != null;

            boolean usernameChanged = !newUsername.isEmpty();
            boolean passwordChanged = !newPassword.isEmpty();

            if (!usernameChanged && !passwordChanged && !profilePictureChanged) {
                Snackbar.make(findViewById(android.R.id.content), "No changes made", Snackbar.LENGTH_SHORT).show();
                return;
            }

            DatabaseManager dbm = new DatabaseManager(getApplicationContext());

            if (passwordChanged) {
                if (currentPassword.isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter current password", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    Snackbar.make(findViewById(android.R.id.content), "Passwords do not match", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (newPassword.length() < 8) {
                    Snackbar.make(findViewById(android.R.id.content), "Password must be at least 8 characters", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (newPassword.contains(" ")) {
                    Snackbar.make(findViewById(android.R.id.content), "Password cannot contain spaces", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (!dbm.verifyPassword(currentPassword)) {
                    Snackbar.make(findViewById(android.R.id.content), "Current password is incorrect", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }

            if (usernameChanged) {
                if (newUsername.contains(" ")) {
                    Snackbar.make(findViewById(android.R.id.content), "Name cannot contain spaces", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (newUsername.length() < 3) {
                    Snackbar.make(findViewById(android.R.id.content), "Name must be at least 3 characters", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }

            boolean updateSuccess = false;

            if (usernameChanged) {
                updateSuccess = dbm.updateUsername(newUsername) || updateSuccess;
            }

            if (passwordChanged) {
                updateSuccess = dbm.updatePassword(newPassword) || updateSuccess;
            }

            if (profilePictureChanged) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                dbm.updateProfilePicture(bitmap);
                updateSuccess = true;  // Assuming the profile picture update always succeeds
            }

            if (updateSuccess) {
                Snackbar.make(findViewById(android.R.id.content), "Profile updated successfully", Snackbar.LENGTH_SHORT).show();
                finish();  // Finish the activity and return to the profile view
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Failed to update profile", Snackbar.LENGTH_SHORT).show();
            }
        }

        public void onCancelClicked() {
            finish();  // Finish the activity and return to the profile view
        }
    }
}

