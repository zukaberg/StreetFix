package com.example.streetfix.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.streetfix.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ReportDetailsActivity extends AppCompatActivity {

    private ImageButton buttonBack;
    private ImageView imageReport;
    private ImageView imageUserAvatar;

    private TextView textStatus, textCategory, textTitle, textLocation,
            textUsername, textCreatedAt, textDescription;
    private MaterialButton buttonSupport, buttonComments, buttonShare;
    private TextInputEditText editComment;

    private FirebaseFirestore db;
    private String reportId;
    private String currentUserId;

    private int supportCount = 0;
    private int commentsCount = 0;
    private boolean isSupported = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);

        db = FirebaseFirestore.getInstance();

        // Uzmi trenutnog korisnika
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        }

        buttonBack = findViewById(R.id.buttonBack);
        imageReport = findViewById(R.id.imageReport);
        imageUserAvatar = findViewById(R.id.imageUserAvatar);

        textStatus = findViewById(R.id.textStatus);
        textCategory = findViewById(R.id.textCategory);
        textTitle = findViewById(R.id.textTitle);
        textLocation = findViewById(R.id.textLocation);
        textUsername = findViewById(R.id.textUsername);
        textCreatedAt = findViewById(R.id.textCreatedAt);
        textDescription = findViewById(R.id.textDescription);

        buttonSupport = findViewById(R.id.buttonSupport);
        buttonComments = findViewById(R.id.buttonComments);
        buttonShare = findViewById(R.id.buttonShare);
        editComment = findViewById(R.id.editComment);

        buttonBack.setOnClickListener(v -> finish());

        reportId = getIntent().getStringExtra("report_id");

        if (reportId == null || reportId.trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.error_report_id_missing), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        imageReport.setImageResource(R.drawable.report_placeholder);

        loadReportDetails();
        loadSupportState();

        buttonSupport.setOnClickListener(v -> {
            if (currentUserId == null || currentUserId.isEmpty()) {
                Toast.makeText(this, getString(R.string.error_login_required), Toast.LENGTH_SHORT).show();
                return;
            }
            toggleSupport();
        });

        buttonComments.setOnClickListener(v ->
                Toast.makeText(this, getString(R.string.comments_coming_soon), Toast.LENGTH_SHORT).show()
        );

        buttonShare.setOnClickListener(v -> shareReport());
    }

    private void loadReportDetails() {
        db.collection("reports")
                .document(reportId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        Toast.makeText(this, getString(R.string.error_report_not_found), Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }

                    String title       = documentSnapshot.getString("title");
                    String category    = documentSnapshot.getString("category");
                    String location    = documentSnapshot.getString("location");
                    String status      = documentSnapshot.getString("status");
                    String username    = documentSnapshot.getString("userName");
                    String description = documentSnapshot.getString("description");
                    String imageUrl    = documentSnapshot.getString("imageUrl");
                    Boolean hasImage   = documentSnapshot.getBoolean("hasImage");
                    String userPhotoUrl = documentSnapshot.getString("userPhotoUrl");

                    Long supports = documentSnapshot.getLong("supportCount");
                    Long comments = documentSnapshot.getLong("comments");
                    Timestamp createdAt = documentSnapshot.getTimestamp("createdAt");

                    supportCount  = supports != null ? supports.intValue() : 0;
                    commentsCount = comments != null ? comments.intValue() : 0;

                    textTitle.setText(title != null ? title : getString(R.string.label_report));
                    textCategory.setText(category != null ? category : getString(R.string.label_category));
                    textLocation.setText(location != null ? location : getString(R.string.unknown_location));
                    textUsername.setText(username != null ? username : getString(R.string.anonymous));
                    textCreatedAt.setText(formatTimeAgo(createdAt));
                    textDescription.setText(description != null ? description : getString(R.string.no_description));

                    updateSupportButtonUi();
                    buttonComments.setText(getString(R.string.comments_count, commentsCount));
                    updateStatusUI(normalizeStatus(status));

                    if (hasImage != null && hasImage && imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(ReportDetailsActivity.this)
                                .load(imageUrl)
                                .placeholder(R.drawable.report_placeholder)
                                .error(R.drawable.report_placeholder)
                                .centerCrop()
                                .into(imageReport);
                    } else {
                        imageReport.setImageResource(R.drawable.report_placeholder);
                    }

                    if (userPhotoUrl != null && !userPhotoUrl.isEmpty()) {
                        Glide.with(ReportDetailsActivity.this)
                                .load(userPhotoUrl)
                                .placeholder(R.drawable.ic_user_nav)
                                .error(R.drawable.ic_user_nav)
                                .circleCrop()
                                .into(imageUserAvatar);
                    } else {
                        imageUserAvatar.setImageResource(R.drawable.ic_user_nav);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, getString(R.string.error_load_failed, e.getMessage()), Toast.LENGTH_LONG).show()
                );
    }

    // Provjeri je li korisnik već lajkovao ovu objavu
    private void loadSupportState() {
        if (currentUserId == null || currentUserId.isEmpty()) return;

        db.collection("reports")
                .document(reportId)
                .collection("supports")
                .document(currentUserId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    isSupported = documentSnapshot.exists();
                    updateSupportButtonUi();
                });
    }

    // Toggle — jedan korisnik može jednom lajkovati jednu objavu
    private void toggleSupport() {
        DocumentReference reportRef = db.collection("reports").document(reportId);
        DocumentReference supportRef = reportRef.collection("supports").document(currentUserId);

        db.runTransaction(transaction -> {
                    DocumentSnapshot reportSnap  = transaction.get(reportRef);
                    DocumentSnapshot supportSnap = transaction.get(supportRef);

                    Long currentCount = reportSnap.getLong("supportCount");
                    long count = currentCount != null ? currentCount : 0;

                    if (supportSnap.exists()) {
                        // Već lajkovao — ukloni lajk
                        transaction.delete(supportRef);
                        transaction.update(reportRef, "supportCount", Math.max(0, count - 1));
                        isSupported  = false;
                        supportCount = (int) Math.max(0, count - 1);
                    } else {
                        // Nije lajkovao — dodaj lajk
                        Map<String, Object> data = new HashMap<>();
                        data.put("userId", currentUserId);
                        data.put("createdAt", FieldValue.serverTimestamp());
                        transaction.set(supportRef, data);
                        transaction.update(reportRef, "supportCount", count + 1);
                        isSupported  = true;
                        supportCount = (int) (count + 1);
                    }

                    return null;
                }).addOnSuccessListener(unused -> updateSupportButtonUi())
                .addOnFailureListener(e ->
                        Toast.makeText(this, getString(R.string.error_support_failed), Toast.LENGTH_SHORT).show()
                );
    }

    // Ažurira UI button bez mijenjanja logike
    private void updateSupportButtonUi() {
        buttonSupport.setActivated(isSupported);
        buttonSupport.setText(getString(R.string.supports_count, supportCount));
    }

    private void updateStatusUI(String status) {
        switch (status) {
            case "in-progress":
                textStatus.setText(getString(R.string.status_in_progress));
                textStatus.setBackgroundResource(R.drawable.bg_status_in_progress);
                textStatus.setTextColor(getColor(R.color.status_progress_text));
                break;
            case "resolved":
                textStatus.setText(getString(R.string.status_resolved));
                textStatus.setBackgroundResource(R.drawable.bg_status_resolved);
                textStatus.setTextColor(getColor(R.color.status_resolved_text));
                break;
            default:
                textStatus.setText(getString(R.string.status_new));
                textStatus.setBackgroundResource(R.drawable.bg_status_new);
                textStatus.setTextColor(getColor(R.color.status_new_text));
                break;
        }
    }

    private String normalizeStatus(String status) {
        if (status == null) return "new";

        switch (status.toLowerCase().trim()) {
            case "in progress":
            case "in-progress":
            case "verified":
                return "in-progress";
            case "resolved":
                return "resolved";
            default:
                return "new";
        }
    }

    private void shareReport() {
        String title    = textTitle.getText().toString();
        String location = textLocation.getText().toString();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, title + "\n" + location);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_report)));
    }

    private String formatTimeAgo(Timestamp timestamp) {
        if (timestamp == null) return getString(R.string.time_recently);

        long diff    = System.currentTimeMillis() - timestamp.toDate().getTime();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        long hours   = TimeUnit.MILLISECONDS.toHours(diff);
        long days    = TimeUnit.MILLISECONDS.toDays(diff);

        if (minutes < 1)  return getString(R.string.time_just_now);
        if (minutes < 60) return getString(R.string.time_minutes_ago, minutes);
        if (hours   < 24) return getString(R.string.time_hours_ago, hours);
        if (days    < 7)  return getString(R.string.time_days_ago, days);
        return getString(R.string.time_weeks_ago, days / 7);
    }
}