package com.example.streetfix.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.streetfix.R;
import com.example.streetfix.data.model.ReportItem;
import com.example.streetfix.ui.adapter.ReportAdapter;


import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerReports;
    private LinearLayout navHome, navMap, navReport, navRewards, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerReports = findViewById(R.id.recyclerReports);
        navReport = findViewById(R.id.navReport);
        navProfile = findViewById(R.id.navProfile);
        navRewards = findViewById(R.id.navRewards);
        navMap = findViewById(R.id.navMap);

        recyclerReports.setLayoutManager(new LinearLayoutManager(this));
        recyclerReports.setAdapter(new ReportAdapter(getSampleReports()));

        navReport.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CreateReportActivity.class);
            startActivity(intent);
        });

        navProfile.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        });

        navRewards.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, RewardsActivity.class));
            finish();
        });

        navMap.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, MapActivity.class));
            finish();
        });
    }

    private List<ReportItem> getSampleReports() {
        List<ReportItem> reports = new ArrayList<>();

        reports.add(new ReportItem(
                R.drawable.sample_report_placeholder,
                "Large pothole on Main Street causing traffic issues",
                "Potholes",
                "Main St & 5th Ave",
                "In Progress",
                142,
                28,
                "2h ago",
                "Sarah Chen"
        ));

        reports.add(new ReportItem(
                R.drawable.sample_report_placeholder,
                "Broken street light creating safety hazard",
                "Street Lighting",
                "Oak Avenue",
                "Verified",
                89,
                15,
                "5h ago",
                "Mike Johnson"
        ));

        reports.add(new ReportItem(
                R.drawable.sample_report_placeholder,
                "Cracked sidewalk needs repair before someone trips",
                "Sidewalks",
                "Pine Street",
                "New",
                67,
                12,
                "1d ago",
                "Emily Rodriguez"
        ));

        reports.add(new ReportItem(
                R.drawable.sample_report_placeholder,
                "Overflowing trash bin attracting pests",
                "Trash",
                "Elm Park",
                "Resolved",
                203,
                45,
                "2d ago",
                "David Kim"
        ));

        reports.add(new ReportItem(
                R.drawable.sample_report_placeholder,
                "Faded stop sign barely visible at intersection",
                "Traffic Signs",
                "Cedar Rd & Maple Dr",
                "Verified",
                156,
                31,
                "3d ago",
                "Lisa Thompson"
        ));

        return reports;
    }
}