package com.example.streetfix.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.streetfix.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private MaterialButton buttonCreateAccount;
    private TextView textSignIn;
    private CheckBox checkTerms;
    private TextInputEditText editFullName, editEmail, editPassword;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        textSignIn = findViewById(R.id.textSignIn);
        checkTerms = findViewById(R.id.checkTerms);

        editFullName = findViewById(R.id.editFullName);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);

        buttonCreateAccount.setOnClickListener(v -> registerUser());

        textSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser() {
        String fullName = editFullName.getText() != null ? editFullName.getText().toString().trim() : "";
        String email = editEmail.getText() != null ? editEmail.getText().toString().trim() : "";
        String password = editPassword.getText() != null ? editPassword.getText().toString().trim() : "";

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

        if (TextUtils.isEmpty(password)) {
            editPassword.setError("Enter password");
            editPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editPassword.setError("Password must be at least 6 characters");
            editPassword.requestFocus();
            return;
        }

        if (!checkTerms.isChecked()) {
            Toast.makeText(this, "You must accept the terms first", Toast.LENGTH_SHORT).show();
            return;
        }

        buttonCreateAccount.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, authTask -> {
                    if (authTask.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        if (firebaseUser == null) {
                            buttonCreateAccount.setEnabled(true);
                            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String uid = firebaseUser.getUid();

                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("fullName", fullName);
                        userMap.put("email", email);
                        userMap.put("username", "");
                        userMap.put("points", 0);
                        userMap.put("level", 1);
                        userMap.put("reportsCount", 0);
                        userMap.put("resolvedCount", 0);
                        userMap.put("profileImage", "");
                        userMap.put("createdAt", Timestamp.now());

                        db.collection("users")
                                .document(uid)
                                .set(userMap)
                                .addOnSuccessListener(unused -> {
                                    buttonCreateAccount.setEnabled(true);
                                    Toast.makeText(SignUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    buttonCreateAccount.setEnabled(true);
                                    Toast.makeText(SignUpActivity.this,
                                            "User created but Firestore save failed: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                });

                    } else {
                        buttonCreateAccount.setEnabled(true);
                        Toast.makeText(SignUpActivity.this,
                                authTask.getException() != null ? authTask.getException().getMessage() : "Registration failed",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}