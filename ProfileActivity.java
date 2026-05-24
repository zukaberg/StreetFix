package com.example.streetfix.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.streetfix.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private ImageButton buttonSettings;
    private MaterialButton buttonEditProfile;

    private LinearLayout layoutMyReports, layoutAchievements, layoutLeaderboard, layoutSettings;
    private LinearLayout navHome, navMap, navReport, navRewards, navProfile;

    private ImageView imageAvatar;                 // NOVO: profilna slika

    private TextView textUserName, textLocation, textJoined;
    private TextView textReportsCount, textResolvedCount, textPoints, textLevel;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        buttonSettings = findViewById(R.id.buttonSettings);
        buttonEditProfile = findViewById(R.id.buttonEditProfile);

        layoutMyReports = findViewById(R.id.layoutMyReports);
        layoutAchievements = findViewById(R.id.layoutAchievements);
        layoutLeaderboard = findViewById(R.id.layoutLeaderboard);
        layoutSettings = findViewById(R.id.layoutSettings);

        navHome = findViewById(R.id.navHome);
        navMap = findViewById(R.id.navMap);
        navReport = findViewById(R.id.navReport);
        navRewards = findViewById(R.id.navRewards);
        navProfile = findViewById(R.id.navProfile);

        imageAvatar = findViewById(R.id.imageAvatar);   // NOVO

        textUserName = findViewById(R.id.textUserName);
        textLocation = findViewById(R.id.textLocation);
        textJoined = findViewById(R.id.textJoined);
        textReportsCount = findViewById(R.id.textReportsCount);
        textResolvedCount = findViewById(R.id.textResolvedCount);
        textPoints = findViewById(R.id.textPoints);
        textLevel = findViewById(R.id.textLevel);

        buttonSettings.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, SettingsActivity.class)));

        buttonEditProfile.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class)));

        layoutMyReports.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, MyReportsActivity.class)));

        layoutAchievements.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, AchievementsActivity.class)));

        layoutLeaderboard.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, LeaderboardActivity.class)));

        layoutSettings.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, SettingsActivity.class)));

        navHome.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            finish();
        });

        navMap.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, MapActivity.class));
            finish();
        });

        navReport.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, CreateReportActivity.class));
            finish();
        });

        navRewards.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, RewardsActivity.class));
            finish();
        });

        navProfile.setOnClickListener(v -> { });

        loadUserProfile();
    }

    private void loadUserProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            goToLogin();
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
                        Timestamp createdAt = documentSnapshot.getTimestamp("createdAt");

                        Long reportsCount = documentSnapshot.getLong("reportsCount");
                        Long resolvedCount = documentSnapshot.getLong("resolvedCount");
                        Long points = documentSnapshot.getLong("points");
                        Long level = documentSnapshot.getLong("level");

                        // NOVO: URL profilne
                        String photoUrl = documentSnapshot.getString("photoUrl");

                        textUserName.setText(fullName != null && !fullName.isEmpty() ? fullName : "User");
                        textLocation.setText(email != null && !email.isEmpty() ? email : "No email");

                        textReportsCount.setText(String.valueOf(reportsCount != null ? reportsCount : 0));
                        textResolvedCount.setText(String.valueOf(resolvedCount != null ? resolvedCount : 0));
                        textPoints.setText(String.valueOf(points != null ? points : 0));
                        textLevel.setText("Level " + (level != null ? level : 1));

                        if (createdAt != null) {
                            SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
                            textJoined.setText("Joined " + sdf.format(createdAt.toDate()));
                        } else {
                            textJoined.setText("Joined recently");
                        }

                        // UČITAVANJE PROFILNE SLIKE
                        if (photoUrl != null && !photoUrl.isEmpty()) {
                            Glide.with(ProfileActivity.this)
                                    .load(photoUrl)
                                    .placeholder(R.drawable.ic_user_placeholder)
                                    .error(R.drawable.ic_user_placeholder)
                                    .circleCrop()
                                    .into(imageAvatar);
                        } else {
                            imageAvatar.setImageResource(R.drawable.ic_user_placeholder);
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, "User profile not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(ProfileActivity.this, "Failed to load profile: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void goToLogin() {
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}