package com.example.streetfix.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.streetfix.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateReportActivity extends AppCompatActivity {

    private ImageButton buttonBack, buttonRemoveImage;
    private FrameLayout layoutPhotoPicker;
    private LinearLayout layoutUploadPlaceholder, navHome, navMap, navReport, navRewards, navProfile;
    private ImageView imagePreview;
    private Spinner spinnerCategory;
    private TextInputEditText editTitle, editDescription, editLocation;
    private TextView textChooseOnMap;
    private MaterialButton buttonSubmitReport;

    private Uri selectedImageUri;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // GALERIJA
    private final ActivityResultLauncher<String> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    imagePreview.setImageURI(uri);
                    imagePreview.setVisibility(View.VISIBLE);
                    layoutUploadPlaceholder.setVisibility(View.GONE);
                    buttonRemoveImage.setVisibility(View.VISIBLE);
                }
            });

    // KAMERA (preview → spremi u cache → Uri)
    private final ActivityResultLauncher<Void> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), bitmap -> {
                if (bitmap != null) {
                    try {
                        File cacheDir = new File(getCacheDir(), "images");
                        if (!cacheDir.exists()) cacheDir.mkdirs();
                        File file = new File(cacheDir, "captured_" + System.currentTimeMillis() + ".jpg");

                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();

                        Uri uri = FileProvider.getUriForFile(
                                this,
                                getPackageName() + ".fileprovider",
                                file
                        );

                        selectedImageUri = uri;
                        imagePreview.setImageBitmap(bitmap);
                        imagePreview.setVisibility(View.VISIBLE);
                        layoutUploadPlaceholder.setVisibility(View.GONE);
                        buttonRemoveImage.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        Toast.makeText(this, "Failed to save photo", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        buttonBack = findViewById(R.id.buttonBack);
        buttonRemoveImage = findViewById(R.id.buttonRemoveImage);
        layoutPhotoPicker = findViewById(R.id.layoutPhotoPicker);
        layoutUploadPlaceholder = findViewById(R.id.layoutUploadPlaceholder);
        imagePreview = findViewById(R.id.imagePreview);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        editLocation = findViewById(R.id.editLocation);
        textChooseOnMap = findViewById(R.id.textChooseOnMap);
        buttonSubmitReport = findViewById(R.id.buttonSubmitReport);

        navHome = findViewById(R.id.navHome);
        navMap = findViewById(R.id.navMap);
        navReport = findViewById(R.id.navReport);
        navRewards = findViewById(R.id.navRewards);
        navProfile = findViewById(R.id.navProfile);

        setupCategorySpinner();

        buttonBack.setOnClickListener(v -> finish());

        layoutPhotoPicker.setOnClickListener(v -> {
            String[] options = {"Take photo", "Choose from gallery"};

            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Add photo")
                    .setItems(options, (dialog, which) -> {
                        if (which == 0) {
                            cameraLauncher.launch(null);
                        } else {
                            galleryLauncher.launch("image/*");
                        }
                    })
                    .show();
        });

        buttonRemoveImage.setOnClickListener(v -> {
            selectedImageUri = null;
            imagePreview.setImageDrawable(null);
            imagePreview.setVisibility(View.GONE);
            layoutUploadPlaceholder.setVisibility(View.VISIBLE);
            buttonRemoveImage.setVisibility(View.GONE);
        });

        textChooseOnMap.setOnClickListener(v ->
                Toast.makeText(this, R.string.map_picker_coming_next, Toast.LENGTH_SHORT).show()
        );

        navHome.setOnClickListener(v -> {
            startActivity(new Intent(CreateReportActivity.this, HomeActivity.class));
            finish();
        });

        navMap.setOnClickListener(v -> {
            startActivity(new Intent(CreateReportActivity.this, MapActivity.class));
            finish();
        });

        navReport.setOnClickListener(v -> { });

        navRewards.setOnClickListener(v -> {
            startActivity(new Intent(CreateReportActivity.this, RewardsActivity.class));
            finish();
        });

        navProfile.setOnClickListener(v -> {
            startActivity(new Intent(CreateReportActivity.this, ProfileActivity.class));
            finish();
        });

        buttonSubmitReport.setOnClickListener(v -> submitReport());
    }

    private void setupCategorySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.report_categories,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void submitReport() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, R.string.you_must_be_logged_in, Toast.LENGTH_SHORT).show();
            return;
        }

        String title = editTitle.getText() != null ? editTitle.getText().toString().trim() : "";
        String description = editDescription.getText() != null ? editDescription.getText().toString().trim() : "";
        String location = editLocation.getText() != null ? editLocation.getText().toString().trim() : "";
        String selectedCategoryLabel = spinnerCategory.getSelectedItem() != null
                ? spinnerCategory.getSelectedItem().toString()
                : "";

        String categoryKey = mapCategoryLabelToKey(selectedCategoryLabel);

        if (title.isEmpty()) {
            editTitle.setError(getString(R.string.title_required));
            editTitle.requestFocus();
            return;
        }

        if (categoryKey.isEmpty()) {
            Toast.makeText(this, R.string.please_select_category, Toast.LENGTH_SHORT).show();
            return;
        }

        if (description.isEmpty()) {
            editDescription.setError(getString(R.string.description_required));
            editDescription.requestFocus();
            return;
        }

        if (location.isEmpty()) {
            editLocation.setError(getString(R.string.location_required));
            editLocation.requestFocus();
            return;
        }

        buttonSubmitReport.setEnabled(false);

        String uid = currentUser.getUid();

        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    String fullName = documentSnapshot.getString("fullName");
                    String photoUrl = documentSnapshot.getString("photoUrl");

                    if (fullName == null || fullName.trim().isEmpty()) {
                        fullName = currentUser.getEmail() != null ? currentUser.getEmail() : "Anonymous";
                    }

                    Map<String, Object> report = new HashMap<>();
                    report.put("title", title);
                    report.put("description", description);
                    report.put("location", location);
                    report.put("category", categoryKey);
                    report.put("status", "new");
                    report.put("userId", uid);
                    report.put("userEmail", currentUser.getEmail());
                    report.put("userName", fullName);
                    report.put("userPhotoUrl", photoUrl);
                    report.put("createdAt", FieldValue.serverTimestamp());
                    report.put("supports", 0L);
                    report.put("comments", 0L);

                    if (selectedImageUri == null) {
                        report.put("hasImage", false);

                        db.collection("reports")
                                .add(report)
                                .addOnSuccessListener(documentReference -> {
                                    buttonSubmitReport.setEnabled(true);
                                    Toast.makeText(this, R.string.report_submitted, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(CreateReportActivity.this, HomeActivity.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    buttonSubmitReport.setEnabled(true);
                                    Toast.makeText(this, getString(R.string.failed_to_submit_report) + e.getMessage(), Toast.LENGTH_LONG).show();
                                });

                    } else {
                        report.put("hasImage", true);

                        StorageReference storageRef = FirebaseStorage.getInstance()
                                .getReference()
                                .child("report_images/" + System.currentTimeMillis() + "_" + uid + ".jpg");

                        storageRef.putFile(selectedImageUri)
                                .continueWithTask(task -> {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    return storageRef.getDownloadUrl();
                                })
                                .addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();
                                    report.put("imageUrl", imageUrl);

                                    db.collection("reports")
                                            .add(report)
                                            .addOnSuccessListener(documentReference -> {
                                                buttonSubmitReport.setEnabled(true);
                                                Toast.makeText(this, R.string.report_submitted, Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(CreateReportActivity.this, HomeActivity.class));
                                                finish();
                                            })
                                            .addOnFailureListener(e -> {
                                                buttonSubmitReport.setEnabled(true);
                                                Toast.makeText(this, getString(R.string.failed_to_submit_report) + e.getMessage(), Toast.LENGTH_LONG).show();
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    buttonSubmitReport.setEnabled(true);
                                    Toast.makeText(this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    buttonSubmitReport.setEnabled(true);
                    Toast.makeText(this, getString(R.string.failed_to_load_user_profile) + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private String mapCategoryLabelToKey(String categoryLabel) {
        if (categoryLabel == null) return "";

        String value = categoryLabel.toLowerCase(Locale.ROOT).trim();

        if (value.equals(getString(R.string.select_category).toLowerCase(Locale.ROOT).trim())) return "";
        if (value.equals("potholes") || value.equals("rupe na cesti")) return "potholes";
        if (value.equals("street lighting") || value.equals("ulična rasvjeta") || value.equals("ulicna rasvjeta")) return "street_lighting";
        if (value.equals("trash") || value.equals("otpad")) return "trash";
        if (value.equals("sidewalks") || value.equals("trotoari")) return "sidewalks";
        if (value.equals("traffic signs") || value.equals("saobraćajni znakovi") || value.equals("saobracajni znakovi")) return "traffic_signs";
        if (value.equals("graffiti") || value.equals("grafiti")) return "graffiti";
        if (value.equals("other") || value.equals("ostalo")) return "other";

        return "";
    }
}