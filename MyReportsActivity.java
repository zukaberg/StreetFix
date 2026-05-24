package com.example.streetfix.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.streetfix.R;
import com.example.streetfix.data.model.MyReport;
import com.example.streetfix.ui.adapter.MyReportsAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyReportsActivity extends AppCompatActivity {

    private ImageButton buttonBack;
    private MaterialButton tabAll, tabNew, tabInProgress, tabResolved;
    private RecyclerView recyclerMyReports;
    private TextView textEmpty;

    private MyReportsAdapter adapter;
    private final List<MyReport> allReports = new ArrayList<>();
    private final List<MyReport> filteredReports = new ArrayList<>();

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reports);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        buttonBack = findViewById(R.id.buttonBack);
        tabAll = findViewById(R.id.tabAll);
        tabNew = findViewById(R.id.tabNew);
        tabInProgress = findViewById(R.id.tabInProgress);
        tabResolved = findViewById(R.id.tabResolved);
        recyclerMyReports = findViewById(R.id.recyclerMyReports);
        textEmpty = findViewById(R.id.textEmpty);

        buttonBack.setOnClickListener(v -> finish());

        setupRecyclerView();

        tabAll.setOnClickListener(v -> filterReports("all"));
        tabNew.setOnClickListener(v -> filterReports("new"));
        tabInProgress.setOnClickListener(v -> filterReports("in-progress"));
        tabResolved.setOnClickListener(v -> filterReports("resolved"));

        loadMyReports();
    }

    private void setupRecyclerView() {
        adapter = new MyReportsAdapter(filteredReports, report -> {
            Intent intent = new Intent(MyReportsActivity.this, ReportDetailsActivity.class);
            intent.putExtra("report_id", report.getId());
            intent.putExtra("report_title", report.getTitle());
            intent.putExtra("report_category", report.getCategory());
            intent.putExtra("report_location", report.getLocation());
            intent.putExtra("report_status", report.getStatus());
            intent.putExtra("report_supports", report.getSupports());
            intent.putExtra("report_comments", report.getComments());
            intent.putExtra("report_time", report.getTime());
            intent.putExtra("report_image", report.getImageResId());
            startActivity(intent);
        });

        recyclerMyReports.setLayoutManager(new LinearLayoutManager(this));
        recyclerMyReports.setAdapter(adapter);
    }

    private void loadMyReports() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db.collection("reports")
                .whereEqualTo("userId", currentUser.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allReports.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String id = document.getId();
                        String title = document.getString("title");
                        String category = document.getString("category");
                        String location = document.getString("location");
                        String status = document.getString("status");
                        String userPhotoUrl = document.getString("userPhotoUrl"); // NOVO

                        allReports.add(new MyReport(
                                id,
                                R.drawable.report_placeholder,
                                title != null ? title : "Untitled report",
                                category != null ? category : "Unknown",
                                location != null ? location : "Unknown location",
                                status != null ? normalizeStatus(status) : "new",
                                0,
                                0,
                                "Recently",
                                userPhotoUrl   // NOVO – 10. parametar
                        ));
                    }

                    filterReports("all");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load reports: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    updateEmptyState();
                });
    }

    private String normalizeStatus(String status) {
        String normalized = status.toLowerCase().trim();

        if (normalized.equals("pending")) {
            return "new";
        } else if (normalized.equals("in progress")) {
            return "in-progress";
        } else if (normalized.equals("resolved")) {
            return "resolved";
        } else if (normalized.equals("new")) {
            return "new";
        } else if (normalized.equals("in-progress")) {
            return "in-progress";
        } else {
            return "new";
        }
    }

    private void filterReports(String status) {
        filteredReports.clear();

        if ("all".equals(status)) {
            filteredReports.addAll(allReports);
            textEmpty.setText("No reports yet");
        } else {
            for (MyReport report : allReports) {
                if (status.equals(report.getStatus())) {
                    filteredReports.add(report);
                }
            }
            textEmpty.setText("No " + formatStatus(status) + " reports yet");
        }

        adapter.updateList(new ArrayList<>(filteredReports));
        updateEmptyState();
        updateTabs(status);
    }

    private void updateEmptyState() {
        if (filteredReports.isEmpty()) {
            textEmpty.setVisibility(View.VISIBLE);
            recyclerMyReports.setVisibility(View.GONE);
        } else {
            textEmpty.setVisibility(View.GONE);
            recyclerMyReports.setVisibility(View.VISIBLE);
        }
    }

    private void updateTabs(String selectedTab) {
        resetTabStyle(tabAll);
        resetTabStyle(tabNew);
        resetTabStyle(tabInProgress);
        resetTabStyle(tabResolved);

        if ("all".equals(selectedTab)) {
            selectTabStyle(tabAll);
        } else if ("new".equals(selectedTab)) {
            selectTabStyle(tabNew);
        } else if ("in-progress".equals(selectedTab)) {
            selectTabStyle(tabInProgress);
        } else if ("resolved".equals(selectedTab)) {
            selectTabStyle(tabResolved);
        }
    }

    private void selectTabStyle(MaterialButton button) {
        button.setBackgroundTintList(getColorStateList(R.color.primary_blue));
        button.setTextColor(getColor(R.color.white));
        button.setStrokeWidth(0);
    }

    private void resetTabStyle(MaterialButton button) {
        button.setBackgroundTintList(getColorStateList(R.color.white));
        button.setTextColor(getColor(R.color.text_secondary));
        button.setStrokeWidth(1);
        button.setStrokeColorResource(R.color.border_color);
    }

    private String formatStatus(String status) {
        switch (status) {
            case "new":
                return "new";
            case "in-progress":
                return "in progress";
            case "resolved":
                return "resolved";
            default:
                return status;
        }
    }
}