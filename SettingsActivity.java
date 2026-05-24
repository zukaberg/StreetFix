package com.example.streetfix.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.example.streetfix.R;
import com.example.streetfix.utils.LanguageHelper;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private ImageButton buttonBack;
    private LinearLayout itemNotifications, itemPrivacy, itemLanguage, itemDarkMode, itemHelp, itemAbout, itemLogout;
    private SwitchCompat switchDarkMode;
    private ImageView imageThemeIcon;
    private TextView textLanguageValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        buttonBack = findViewById(R.id.buttonBack);
        itemNotifications = findViewById(R.id.itemNotifications);
        itemPrivacy = findViewById(R.id.itemPrivacy);
        itemLanguage = findViewById(R.id.itemLanguage);
        itemDarkMode = findViewById(R.id.itemDarkMode);
        itemHelp = findViewById(R.id.itemHelp);
        itemAbout = findViewById(R.id.itemAbout);
        itemLogout = findViewById(R.id.itemLogout);

        switchDarkMode = findViewById(R.id.switchDarkMode);
        imageThemeIcon = findViewById(R.id.imageThemeIcon);
        textLanguageValue = findViewById(R.id.textLanguageValue);

        buttonBack.setOnClickListener(v -> finish());

        updateLanguageValue();

        boolean isDarkMode =
                AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;

        switchDarkMode.setChecked(isDarkMode);
        updateThemeIcon(isDarkMode);

        itemNotifications.setOnClickListener(v ->
                Toast.makeText(this, getString(R.string.notifications), Toast.LENGTH_SHORT).show()
        );

        itemPrivacy.setOnClickListener(v ->
                Toast.makeText(this, getString(R.string.privacy_security), Toast.LENGTH_SHORT).show()
        );

        itemLanguage.setOnClickListener(v -> showLanguageDialog());

        itemHelp.setOnClickListener(v ->
                Toast.makeText(this, getString(R.string.help_center), Toast.LENGTH_SHORT).show()
        );

        itemAbout.setOnClickListener(v ->
                Toast.makeText(this, getString(R.string.about), Toast.LENGTH_SHORT).show()
        );

        itemDarkMode.setOnClickListener(v -> switchDarkMode.toggle());

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateThemeIcon(isChecked);
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        itemLogout.setOnClickListener(v -> showLogoutDialog());
    }

    private void showLanguageDialog() {
        final String[] languageLabels = {
                getString(R.string.english),
                getString(R.string.bosnian)
        };

        final String[] languageTags = {"en", "bs"};

        String currentLanguage = LanguageHelper.getCurrentLanguageTag();
        int checkedItem = currentLanguage.startsWith("bs") ? 1 : 0;

        new AlertDialog.Builder(this)
                .setTitle(R.string.language)
                .setSingleChoiceItems(languageLabels, checkedItem, (dialog, which) -> {
                    LanguageHelper.setAppLanguage(languageTags[which]);
                    dialog.dismiss();
                    recreate();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void updateLanguageValue() {
        String currentLanguage = LanguageHelper.getCurrentLanguageTag();

        if (currentLanguage.startsWith("bs")) {
            textLanguageValue.setText(getString(R.string.bosnian));
        } else {
            textLanguageValue.setText(getString(R.string.english));
        }
    }

    private void updateThemeIcon(boolean isDarkMode) {
        imageThemeIcon.setImageResource(isDarkMode ? R.drawable.ic_moon : R.drawable.ic_sun);
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.log_out)
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton(R.string.log_out, (dialog, which) -> logoutUser())
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}