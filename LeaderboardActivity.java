package com.example.streetfix.ui.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.streetfix.R;
import com.example.streetfix.data.model.LeaderboardItem;
import com.example.streetfix.ui.adapter.LeaderboardAdapter;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView recyclerLeaderboard;
    private TextView tabPoints, tabReports, tabResolved;
    private LeaderboardAdapter adapter;
    private final List<LeaderboardItem> leaderboardList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        ImageView btnBack = findViewById(R.id.btnBack);
        recyclerLeaderboard = findViewById(R.id.recyclerLeaderboard);
        tabPoints = findViewById(R.id.tabPoints);
        tabReports = findViewById(R.id.tabReports);
        tabResolved = findViewById(R.id.tabResolved);

        btnBack.setOnClickListener(v -> finish());

        tabPoints.setText(getString(R.string.points));
        tabReports.setText(getString(R.string.reports));
        tabResolved.setText(getString(R.string.resolved));

        setupData();

        adapter = new LeaderboardAdapter(leaderboardList);
        recyclerLeaderboard.setLayoutManager(new LinearLayoutManager(this));
        recyclerLeaderboard.setAdapter(adapter);

        selectTab(LeaderboardAdapter.LeaderboardType.POINTS);

        tabPoints.setOnClickListener(v -> selectTab(LeaderboardAdapter.LeaderboardType.POINTS));
        tabReports.setOnClickListener(v -> selectTab(LeaderboardAdapter.LeaderboardType.REPORTS));
        tabResolved.setOnClickListener(v -> selectTab(LeaderboardAdapter.LeaderboardType.RESOLVED));
    }

    private void setupData() {
        leaderboardList.add(new LeaderboardItem(1, "Alex Martinez", R.drawable.ic_profile_placeholder, 2450, 87, 45, false));
        leaderboardList.add(new LeaderboardItem(2, "Jordan Lee", R.drawable.ic_profile_placeholder, 2180, 72, 38, false));
        leaderboardList.add(new LeaderboardItem(3, "Taylor Kim", R.drawable.ic_profile_placeholder, 1920, 65, 32, false));
        leaderboardList.add(new LeaderboardItem(4, "Morgan Chen", R.drawable.ic_profile_placeholder, 1750, 58, 28, false));
        leaderboardList.add(new LeaderboardItem(5, "Casey Rivera", R.drawable.ic_profile_placeholder, 1620, 54, 25, false));
        leaderboardList.add(new LeaderboardItem(
                12,
                getString(R.string.you_user_label),
                R.drawable.ic_profile_placeholder,
                650,
                12,
                5,
                true
        ));
    }

    private void selectTab(LeaderboardAdapter.LeaderboardType type) {
        adapter.setSelectedType(type);

        resetTabs();

        if (type == LeaderboardAdapter.LeaderboardType.POINTS) {
            tabPoints.setBackgroundResource(R.drawable.bg_tab_selected);
            tabPoints.setTextColor(getColor(android.R.color.white));
        } else if (type == LeaderboardAdapter.LeaderboardType.REPORTS) {
            tabReports.setBackgroundResource(R.drawable.bg_tab_selected);
            tabReports.setTextColor(getColor(android.R.color.white));
        } else {
            tabResolved.setBackgroundResource(R.drawable.bg_tab_selected);
            tabResolved.setTextColor(getColor(android.R.color.white));
        }
    }

    private void resetTabs() {
        tabPoints.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        tabReports.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        tabResolved.setBackgroundColor(android.graphics.Color.TRANSPARENT);

        tabPoints.setTextColor(android.graphics.Color.parseColor("#64748B"));
        tabReports.setTextColor(android.graphics.Color.parseColor("#64748B"));
        tabResolved.setTextColor(android.graphics.Color.parseColor("#64748B"));
    }
}