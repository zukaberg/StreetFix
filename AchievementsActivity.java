package com.example.streetfix.ui.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.streetfix.R;
import com.example.streetfix.data.model.AchievementItem;
import com.example.streetfix.ui.adapter.AchievementsAdapter;

import java.util.ArrayList;
import java.util.List;

public class AchievementsActivity extends AppCompatActivity {

    private RecyclerView recyclerAchievements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        ImageView btnBack = findViewById(R.id.btnBack);
        recyclerAchievements = findViewById(R.id.recyclerAchievements);

        btnBack.setOnClickListener(v -> finish());

        recyclerAchievements.setLayoutManager(new LinearLayoutManager(this));
        recyclerAchievements.setAdapter(new AchievementsAdapter(getAchievements()));
    }

    private List<AchievementItem> getAchievements() {
        List<AchievementItem> list = new ArrayList<>();

        list.add(new AchievementItem(
                R.drawable.ic_star,
                getString(R.string.achievement_first_report),
                getString(R.string.achievement_first_report_desc),
                true,
                1,
                1,
                R.color.achievement_yellow_bg
        ));

        list.add(new AchievementItem(
                R.drawable.ic_zap,
                getString(R.string.achievement_5_reports),
                getString(R.string.achievement_5_reports_desc),
                true,
                5,
                5,
                R.color.achievement_blue_bg
        ));

        list.add(new AchievementItem(
                R.drawable.ic_trophy,
                getString(R.string.achievement_10_resolved),
                getString(R.string.achievement_10_resolved_desc),
                true,
                10,
                10,
                R.color.achievement_green_bg
        ));

        list.add(new AchievementItem(
                R.drawable.ic_heart,
                getString(R.string.achievement_neighborhood_helper),
                getString(R.string.achievement_neighborhood_helper_desc),
                true,
                100,
                100,
                R.color.achievement_red_bg
        ));

        list.add(new AchievementItem(
                R.drawable.ic_target,
                getString(R.string.achievement_25_reports),
                getString(R.string.achievement_25_reports_desc),
                false,
                12,
                25,
                R.color.achievement_purple_bg
        ));

        list.add(new AchievementItem(
                R.drawable.ic_award,
                getString(R.string.achievement_city_hero),
                getString(R.string.achievement_city_hero_desc),
                false,
                650,
                1000,
                R.color.achievement_orange_bg
        ));

        return list;
    }
}