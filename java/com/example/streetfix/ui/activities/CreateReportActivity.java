package com.example.streetfix.ui.activities;

import android.content.Intent;
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

import com.example.streetfix.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

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

    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    imagePreview.setImageURI(uri);
                    imagePreview.setVisibility(View.VISIBLE);
                    layoutUploadPlaceholder.setVisibility(View.GONE);
                    buttonRemoveImage.setVisibility(View.VISIBLE);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);

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
        navProfile = findViewById(R.id.navProfile);
        navRewards = findViewById(R.id.navRewards);
        navMap = findViewById(R.id.navMap);

        setupCategorySpinner();

        buttonBack.setOnClickListener(v -> finish());

        layoutPhotoPicker.setOnClickListener(v -> {
            if (selectedImageUri == null) {
                imagePickerLauncher.launch("image/*");
            }
        });

        buttonRemoveImage.setOnClickListener(v -> {
            selectedImageUri = null;
            imagePreview.setImageDrawable(null);
            imagePreview.setVisibility(View.GONE);
            layoutUploadPlaceholder.setVisibility(View.VISIBLE);
            buttonRemoveImage.setVisibility(View.GONE);
        });

        textChooseOnMap.setOnClickListener(v ->
                Toast.makeText(this, "Map picker coming next", Toast.LENGTH_SHORT).show()
        );

        navHome.setOnClickListener(v -> {
            startActivity(new Intent(CreateReportActivity.this, HomeActivity.class));
            finish();
        });

        navProfile.setOnClickListener(v -> {
            startActivity(new Intent(CreateReportActivity.this, ProfileActivity.class));
        });

        navRewards.setOnClickListener(v -> {
            startActivity(new Intent(CreateReportActivity.this, RewardsActivity.class));
            finish();
        });

        navMap.setOnClickListener(v -> {
            startActivity(new Intent(CreateReportActivity.this, MapActivity.class));
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
        String title = editTitle.getText() != null ? editTitle.getText().toString().trim() : "";
        String description = editDescription.getText() != null ? editDescription.getText().toString().trim() : "";
        String location = editLocation.getText() != null ? editLocation.getText().toString().trim() : "";
        String category = spinnerCategory.getSelectedItem() != null
                ? spinnerCategory.getSelectedItem().toString()
                : "";

        if (title.isEmpty()) {
            editTitle.setError("Title is required");
            return;
        }

        if (category.equals("Select a category") || category.isEmpty()) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }

        if (description.isEmpty()) {
            editDescription.setError("Description is required");
            return;
        }

        if (location.isEmpty()) {
            editLocation.setError("Location is required");
            return;
        }

        Toast.makeText(this, "Report submitted", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(CreateReportActivity.this, HomeActivity.class));
        finish();
    }
}