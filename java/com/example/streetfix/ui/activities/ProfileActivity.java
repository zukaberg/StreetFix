package com.example.streetfix.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.streetfix.R;
import com.google.android.material.button.MaterialButton;

public class ProfileActivity extends AppCompatActivity {

    private ImageButton buttonSettings;
    private MaterialButton buttonEditProfile;
    private LinearLayout menuMyReports, menuAchievements, menuLeaderboard, menuSettings;
    private LinearLayout navHome, navMap, navReport, navRewards, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        buttonSettings = findViewById(R.id.buttonSettings);
        buttonEditProfile = findViewById(R.id.buttonEditProfile);

        menuMyReports = findViewById(R.id.menuMyReports);
        menuAchievements = findViewById(R.id.menuAchievements);
        menuLeaderboard = findViewById(R.id.menuLeaderboard);
        menuSettings = findViewById(R.id.menuSettings);

        navHome = findViewById(R.id.navHome);
        navMap = findViewById(R.id.navMap);
        navReport = findViewById(R.id.navReport);
        navRewards = findViewById(R.id.navRewards);
        navProfile = findViewById(R.id.navProfile);


        buttonSettings.setOnClickListener(v ->
                Toast.makeText(this, "Settings screen next", Toast.LENGTH_SHORT).show()
        );

        buttonEditProfile.setOnClickListener(v ->
                Toast.makeText(this, "Edit profile screen next", Toast.LENGTH_SHORT).show()
        );

        menuMyReports.setOnClickListener(v ->
                Toast.makeText(this, "My Reports screen next", Toast.LENGTH_SHORT).show()
        );

        menuAchievements.setOnClickListener(v ->
                Toast.makeText(this, "Achievements screen next", Toast.LENGTH_SHORT).show()
        );

        menuLeaderboard.setOnClickListener(v ->
                Toast.makeText(this, "Leaderboard screen next", Toast.LENGTH_SHORT).show()
        );

        menuSettings.setOnClickListener(v ->
                Toast.makeText(this, "Settings screen next", Toast.LENGTH_SHORT).show()
        );

        navHome.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            finish();
        });

        navReport.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, CreateReportActivity.class));
            finish();
        });

        navMap.setOnClickListener(v ->
                Toast.makeText(this, "Map screen next", Toast.LENGTH_SHORT).show()
        );

        navRewards.setOnClickListener(v ->
                Toast.makeText(this, "Rewards screen next", Toast.LENGTH_SHORT).show()
        );

        navProfile.setOnClickListener(v -> {
        });

        navRewards.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, RewardsActivity.class));
            finish();
        });

        navMap.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, MapActivity.class));
            finish();
        });
    }
}