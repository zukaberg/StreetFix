package com.example.streetfix.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.streetfix.R;
import com.example.streetfix.data.model.OnboardingItem;
import com.example.streetfix.ui.adapter.OnboardingAdapter;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 onboardingViewPager;
    private MaterialButton buttonNext;
    private LinearLayout layoutDots;
    private OnboardingAdapter onboardingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        onboardingViewPager = findViewById(R.id.onboardingViewPager);
        buttonNext = findViewById(R.id.buttonNext);
        layoutDots = findViewById(R.id.layoutDots);

        setupOnboardingItems();
        setupIndicators();
        setCurrentIndicator(0);

        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);

                if (position == onboardingAdapter.getItemCount() - 1) {
                    buttonNext.setText("Get Started");
                } else {
                    buttonNext.setText("Next");
                }
            }
        });

        buttonNext.setOnClickListener(v -> {
            if (onboardingViewPager.getCurrentItem() + 1 < onboardingAdapter.getItemCount()) {
                onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem() + 1);
            } else {
                startActivity(new Intent(OnboardingActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void setupOnboardingItems() {
        List<OnboardingItem> items = new ArrayList<>();

        items.add(new OnboardingItem(
                R.drawable.ic_onboarding_camera,
                "Report Issues",
                "Snap a photo of street problems like potholes, broken lights, or damaged sidewalks"
        ));

        items.add(new OnboardingItem(
                R.drawable.ic_onboarding_location,
                "Track Progress",
                "Follow your reports and see real-time updates as your city works to fix them"
        ));

        items.add(new OnboardingItem(
                R.drawable.ic_onboarding_award,
                "Earn Rewards",
                "Get points, badges, and recognition for making your community a better place"
        ));

        onboardingAdapter = new OnboardingAdapter(items);
        onboardingViewPager.setAdapter(onboardingAdapter);
    }

    private void setupIndicators() {
        TextView[] indicators = new TextView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new TextView(this);
            indicators[i].setBackgroundResource(R.drawable.dot_inactive);
            indicators[i].setLayoutParams(layoutParams);
            indicators[i].setWidth(8);
            indicators[i].setHeight(8);
            layoutDots.addView(indicators[i]);
        }
    }

    private void setCurrentIndicator(int index) {
        int childCount = layoutDots.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = layoutDots.getChildAt(i);
            if (i == index) {
                child.setBackgroundResource(R.drawable.dot_active);
            } else {
                child.setBackgroundResource(R.drawable.dot_inactive);
            }
        }
    }
}