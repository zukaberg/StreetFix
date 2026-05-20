package com.example.streetfix.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.streetfix.R;
import com.google.android.material.button.MaterialButton;

public class SignUpActivity extends AppCompatActivity {

    private MaterialButton buttonCreateAccount;
    private TextView textSignIn;
    private CheckBox checkTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        textSignIn = findViewById(R.id.textSignIn);
        checkTerms = findViewById(R.id.checkTerms);

        buttonCreateAccount.setOnClickListener(v -> {
            if (!checkTerms.isChecked()) {
                Toast.makeText(this, "You must accept the terms first", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(SignUpActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        });

        textSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}