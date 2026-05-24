package com.example.streetfix.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.streetfix.R;
import com.google.android.material.button.MaterialButton;

public class MapActivity extends AppCompatActivity {

    private ImageButton buttonMapFilter;
    private TextView textChangeArea;
    private MaterialButton buttonOpenReport;
    private LinearLayout navHome, navMap, navReport, navRewards, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        buttonMapFilter = findViewById(R.id.buttonMapFilter);
        textChangeArea = findViewById(R.id.textChangeArea);
        buttonOpenReport = findViewById(R.id.buttonOpenReport);

        navHome = findViewById(R.id.navHome);
        navMap = findViewById(R.id.navMap);
        navReport = findViewById(R.id.navReport);
        navRewards = findViewById(R.id.navRewards);
        navProfile = findViewById(R.id.navProfile);

        buttonMapFilter.setOnClickListener(v ->
                Toast.makeText(this, "Map filters coming next", Toast.LENGTH_SHORT).show()
        );

        textChangeArea.setOnClickListener(v ->
                Toast.makeText(this, "Area picker coming next", Toast.LENGTH_SHORT).show()
        );

        buttonOpenReport.setOnClickListener(v -> {
            startActivity(new Intent(MapActivity.this, CreateReportActivity.class));
            finish();
        });

        navHome.setOnClickListener(v -> {
            startActivity(new Intent(MapActivity.this, HomeActivity.class));
            finish();
        });

        navReport.setOnClickListener(v -> {
            startActivity(new Intent(MapActivity.this, CreateReportActivity.class));
            finish();
        });

        navRewards.setOnClickListener(v -> {
            startActivity(new Intent(MapActivity.this, RewardsActivity.class));
            finish();
        });

        navProfile.setOnClickListener(v -> {
            startActivity(new Intent(MapActivity.this, ProfileActivity.class));
            finish();
        });

        navMap.setOnClickListener(v -> {
        });
    }
}