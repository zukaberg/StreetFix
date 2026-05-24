package com.example.streetfix.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.streetfix.R;
import com.example.streetfix.data.model.ReportItem;
import com.example.streetfix.ui.adapter.ReportAdapter;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerReports;
    private LinearLayout navHome, navMap, navReport, navRewards, navProfile;
    private ImageView buttonNotifications;
    private ChipGroup chipGroupCategories;
    private EditText editSearch;

    private ReportAdapter reportAdapter;
    private final List<ReportItem> allReports = new ArrayList<>();
    private final List<ReportItem> filteredReports = new ArrayList<>();

    private String selectedCategoryKey = "all";
    private String searchQuery = "";

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();

        recyclerReports = findViewById(R.id.recyclerReports);
        navHome = findViewById(R.id.navHome);
        navReport = findViewById(R.id.navReport);
        navProfile = findViewById(R.id.navProfile);
        navRewards = findViewById(R.id.navRewards);
        navMap = findViewById(R.id.navMap);
        buttonNotifications = findViewById(R.id.buttonNotifications);
        chipGroupCategories = findViewById(R.id.chipGroupCategories);
        editSearch = findViewById(R.id.editSearch);

        recyclerReports.setLayoutManager(new LinearLayoutManager(this));

        reportAdapter = new ReportAdapter(filteredReports, report -> {
            Intent intent = new Intent(HomeActivity.this, ReportDetailsActivity.class);
            intent.putExtra("report_id", report.getId());
            startActivity(intent);
        });

        recyclerReports.setAdapter(reportAdapter);

        setupFilters();
        setupNavigation();
        loadAllReports();
    }

    private void setupFilters() {
        chipGroupCategories.setOnCheckedChangeListener((group, checkedId) -> {
            selectedCategoryKey = mapChipIdToCategoryKey(checkedId);
            applyFilters();
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                searchQuery = s != null ? s.toString().trim() : "";
                applyFilters();
            }
        });
    }

    private String mapChipIdToCategoryKey(int checkedId) {
        if (checkedId == R.id.chipPotholes) return "potholes";
        if (checkedId == R.id.chipLighting) return "street_lighting";
        if (checkedId == R.id.chipTrash) return "trash";
        if (checkedId == R.id.chipSidewalks) return "sidewalks";
        if (checkedId == R.id.chipTrafficSigns) return "traffic_signs";
        return "all";
    }

    private void setupNavigation() {
        buttonNotifications.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, NotificationsActivity.class))
        );

        navHome.setOnClickListener(v -> { });

        navReport.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, CreateReportActivity.class))
        );

        navProfile.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class))
        );

        navRewards.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, RewardsActivity.class));
            finish();
        });

        navMap.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, MapActivity.class));
            finish();
        });
    }

    private void loadAllReports() {
        db.collection("reports")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allReports.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String title = document.getString("title");
                        String category = document.getString("category");
                        String location = document.getString("location");
                        String status = document.getString("status");
                        String username = document.getString("userName");
                        Long supports = document.getLong("supports");
                        Long comments = document.getLong("comments");
                        Timestamp createdAt = document.getTimestamp("createdAt");

                        String imageUrl = document.getString("imageUrl");          // NOVO
                        Boolean hasImage = document.getBoolean("hasImage");       // NOVO
                        String userPhotoUrl = document.getString("userPhotoUrl"); // avatar

                        allReports.add(new ReportItem(
                                document.getId(),
                                R.drawable.sample_report_placeholder,
                                title != null ? title : "Untitled report",
                                category != null ? category : "Unknown",
                                location != null ? location : "Unknown location",
                                normalizeStatus(status),
                                supports != null ? supports.intValue() : 0,
                                comments != null ? comments.intValue() : 0,
                                formatTimeAgo(createdAt),
                                username != null ? username : "Anonymous",
                                imageUrl,
                                hasImage != null && hasImage,
                                userPhotoUrl
                        ));
                    }

                    applyFilters();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, getString(R.string.failed_to_load_reports) + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }

    private void applyFilters() {
        filteredReports.clear();

        String normalizedSearch = searchQuery.toLowerCase(Locale.ROOT).trim();

        for (ReportItem report : allReports) {
            String reportCategoryKey = report.getCategory() != null
                    ? report.getCategory().toLowerCase(Locale.ROOT).trim()
                    : "";

            String title = safeLower(report.getTitle());
            String location = safeLower(report.getLocation());
            String username = safeLower(report.getUsername());

            boolean matchesCategory = selectedCategoryKey.equals("all")
                    || selectedCategoryKey.equals(reportCategoryKey);

            boolean matchesSearch = normalizedSearch.isEmpty()
                    || title.contains(normalizedSearch)
                    || reportCategoryKey.contains(normalizedSearch)
                    || location.contains(normalizedSearch)
                    || username.contains(normalizedSearch);

            if (matchesCategory && matchesSearch) {
                filteredReports.add(report);
            }
        }

        reportAdapter.notifyDataSetChanged();
    }

    private String normalizeStatus(String status) {
        if (status == null) return "new";

        String normalized = status.toLowerCase(Locale.ROOT).trim();

        switch (normalized) {
            case "new":
            case "pending":
                return "new";
            case "in progress":
            case "in-progress":
            case "verified":
                return "in progress";
            case "resolved":
                return "resolved";
            default:
                return "new";
        }
    }

    private String formatTimeAgo(Timestamp timestamp) {
        if (timestamp == null) return getString(R.string.recently);

        long now = System.currentTimeMillis();
        long time = timestamp.toDate().getTime();
        long diff = now - time;

        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        long days = TimeUnit.MILLISECONDS.toDays(diff);

        if (minutes < 1) return getString(R.string.just_now);
        if (minutes < 60) return minutes + "m ago";
        if (hours < 24) return hours + "h ago";
        if (days < 7) return days + "d ago";
        return (days / 7) + "w ago";
    }

    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT).trim();
    }
}

