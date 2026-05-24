package com.example.streetfix.ui.activities;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.streetfix.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private ImageButton buttonBack;
    private ImageView imageAvatar;
    private MaterialButton buttonChangePhoto, buttonCancel, buttonSaveChanges;
    private TextInputEditText editFullName, editEmail, editPhone, editLocation, editBio;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    private Uri selectedImageUri = null;
    private String currentPhotoUrl = "";

    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    imageAvatar.setImageURI(uri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        buttonBack = findViewById(R.id.buttonBack);
        imageAvatar = findViewById(R.id.imageAvatar);
        buttonChangePhoto = findViewById(R.id.buttonChangePhoto);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges);

        editFullName = findViewById(R.id.editFullName);
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        editLocation = findViewById(R.id.editLocation);
        editBio = findViewById(R.id.editBio);

        buttonBack.setOnClickListener(v -> finish());
        buttonCancel.setOnClickListener(v -> finish());

        buttonChangePhoto.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        buttonSaveChanges.setOnClickListener(v -> validateAndSave());

        loadUserData();
    }

    private void loadUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String uid = currentUser.getUid();

        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String fullName = documentSnapshot.getString("fullName");
                        String email = documentSnapshot.getString("email");
                        String phone = documentSnapshot.getString("phone");
                        String location = documentSnapshot.getString("location");
                        String bio = documentSnapshot.getString("bio");
                        String photoUrl = documentSnapshot.getString("photoUrl");

                        editFullName.setText(fullName != null ? fullName : "");
                        editEmail.setText(email != null ? email : currentUser.getEmail());
                        editPhone.setText(phone != null ? phone : "");
                        editLocation.setText(location != null ? location : "");
                        editBio.setText(bio != null ? bio : "");

                        currentPhotoUrl = photoUrl != null ? photoUrl : "";

                        if (!currentPhotoUrl.isEmpty()) {
                            Glide.with(EditProfileActivity.this)
                                    .load(currentPhotoUrl)
                                    .placeholder(R.drawable.sample_avatar)
                                    .error(R.drawable.sample_avatar)
                                    .into(imageAvatar);
                        }
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load profile: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void validateAndSave() {
        String fullName = editFullName.getText() != null ? editFullName.getText().toString().trim() : "";
        String email = editEmail.getText() != null ? editEmail.getText().toString().trim() : "";
        String phone = editPhone.getText() != null ? editPhone.getText().toString().trim() : "";
        String location = editLocation.getText() != null ? editLocation.getText().toString().trim() : "";
        String bio = editBio.getText() != null ? editBio.getText().toString().trim() : "";

        if (TextUtils.isEmpty(fullName)) {
            editFullName.setError("Enter full name");
            editFullName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            editEmail.setError("Enter email");
            editEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Enter valid email");
            editEmail.requestFocus();
            return;
        }

        buttonSaveChanges.setEnabled(false);

        if (selectedImageUri != null) {
            uploadImageAndSave(fullName, email, phone, location, bio);
        } else {
            saveProfileData(fullName, email, phone, location, bio, currentPhotoUrl);
        }
    }

    private void uploadImageAndSave(String fullName, String email, String phone, String location, String bio) {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            buttonSaveChanges.setEnabled(true);
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = currentUser.getUid();
        StorageReference storageRef = storage.getReference()
                .child("profile_photos")
                .child(uid + ".jpg");

        storageRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot ->
                        storageRef.getDownloadUrl().addOnSuccessListener(uri ->
                                saveProfileData(fullName, email, phone, location, bio, uri.toString())
                        ).addOnFailureListener(e -> {
                            buttonSaveChanges.setEnabled(true);
                            Toast.makeText(this, "Failed to get image URL: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        })
                )
                .addOnFailureListener(e -> {
                    buttonSaveChanges.setEnabled(true);
                    Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void saveProfileData(String fullName, String email, String phone, String location, String bio, String photoUrl) {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            buttonSaveChanges.setEnabled(true);
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = currentUser.getUid();

        Map<String, Object> updates = new HashMap<>();
        updates.put("fullName", fullName);
        updates.put("email", email);
        updates.put("phone", phone);
        updates.put("location", location);
        updates.put("bio", bio);
        updates.put("photoUrl", photoUrl);

        db.collection("users")
                .document(uid)
                .update(updates)
                .addOnSuccessListener(unused -> {
                    buttonSaveChanges.setEnabled(true);
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    buttonSaveChanges.setEnabled(true);
                    Toast.makeText(this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}