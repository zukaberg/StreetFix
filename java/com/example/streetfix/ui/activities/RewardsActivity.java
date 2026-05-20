package com.example.streetfix.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.streetfix.R;

public class RewardsActivity extends AppCompatActivity {

    private ImageButton buttonNotifications;
    private Button buttonTabAchievements, buttonTabLeaderboard;
    private LinearLayout layoutAchievementsSection, layoutLeaderboardSection;
    private TextView textViewAllAchievements, textViewAllLeaderboard;
    private LinearLayout navHome, navMap, navReport, navRewards, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        buttonNotifications = findViewById(R.id.buttonNotifications);
        buttonTabAchievements = findViewById(R.id.buttonTabAchievements);
        buttonTabLeaderboard = findViewById(R.id.buttonTabLeaderboard);
        layoutAchievementsSection = findViewById(R.id.layoutAchievementsSection);
        layoutLeaderboardSection = findViewById(R.id.layoutLeaderboardSection);
        textViewAllAchievements = findViewById(R.id.textViewAllAchievements);
        textViewAllLeaderboard = findViewById(R.id.textViewAllLeaderboard);

        navHome = findViewById(R.id.navHome);
        navMap = findViewById(R.id.navMap);
        navReport = findViewById(R.id.navReport);
        navRewards = findViewById(R.id.navRewards);
        navProfile = findViewById(R.id.navProfile);

        buttonNotifications.setOnClickListener(v ->
                Toast.makeText(this, "Notifications next", Toast.LENGTH_SHORT).show()
        );

        textViewAllAchievements.setOnClickListener(v ->
                Toast.makeText(this, "Achievements screen next", Toast.LENGTH_SHORT).show()
        );

        textViewAllLeaderboard.setOnClickListener(v ->
                Toast.makeText(this, "Leaderboard screen next", Toast.LENGTH_SHORT).show()
        );

        buttonTabAchievements.setOnClickListener(v -> showAchievementsTab());
        buttonTabLeaderboard.setOnClickListener(v -> showLeaderboardTab());

        navHome.setOnClickListener(v -> {
            startActivity(new Intent(RewardsActivity.this, HomeActivity.class));
            finish();
        });

        navReport.setOnClickListener(v -> {
            startActivity(new Intent(RewardsActivity.this, CreateReportActivity.class));
            finish();
        });

        navProfile.setOnClickListener(v -> {
            startActivity(new Intent(RewardsActivity.this, ProfileActivity.class));
            finish();
        });

        navMap.setOnClickListener(v -> {
            startActivity(new Intent(RewardsActivity.this, MapActivity.class));
            finish();
        });

        navRewards.setOnClickListener(v -> {
        });

        showAchievementsTab();
    }

    private void showAchievementsTab() {
        layoutAchievementsSection.setVisibility(android.view.View.VISIBLE);
        layoutLeaderboardSection.setVisibility(android.view.View.GONE);

        buttonTabAchievements.setBackgroundResource(R.drawable.bg_tab_selected);
        buttonTabAchievements.setTextColor(getColor(android.R.color.white));

        buttonTabLeaderboard.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        buttonTabLeaderboard.setTextColor(android.graphics.Color.parseColor("#64748B"));
    }

    private void showLeaderboardTab() {
        layoutAchievementsSection.setVisibility(android.view.View.GONE);
        layoutLeaderboardSection.setVisibility(android.view.View.VISIBLE);

        buttonTabLeaderboard.setBackgroundResource(R.drawable.bg_tab_selected);
        buttonTabLeaderboard.setTextColor(getColor(android.R.color.white));

        buttonTabAchievements.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        buttonTabAchievements.setTextColor(android.graphics.Color.parseColor("#64748B"));
    }
}