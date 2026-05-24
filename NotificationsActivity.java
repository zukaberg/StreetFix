package com.example.streetfix.ui.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.streetfix.R;
import com.example.streetfix.data.model.NotificationItem;
import com.example.streetfix.ui.adapter.NotificationsAdapter;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView recyclerNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        ImageView btnBack = findViewById(R.id.btnBack);
        recyclerNotifications = findViewById(R.id.recyclerNotifications);

        btnBack.setOnClickListener(v -> finish());

        recyclerNotifications.setLayoutManager(new LinearLayoutManager(this));
        recyclerNotifications.setAdapter(new NotificationsAdapter(getNotifications()));
    }

    private List<NotificationItem> getNotifications() {
        List<NotificationItem> list = new ArrayList<>();

        list.add(new NotificationItem(
                "1",
                "resolved",
                R.drawable.ic_check_circle,
                R.color.achievement_green_bg,
                "Issue Resolved",
                "Your report \"Large pothole on Main Street\" has been marked as resolved",
                "2h ago",
                true
        ));

        list.add(new NotificationItem(
                "2",
                "support",
                R.drawable.ic_heart,
                R.color.achievement_red_bg,
                "New Support",
                "Mike Johnson and 12 others supported your report",
                "5h ago",
                true
        ));

        list.add(new NotificationItem(
                "3",
                "comment",
                R.drawable.ic_message,
                R.color.achievement_blue_bg,
                "New Comment",
                "Emily Rodriguez commented: \"City crew was here this morning\"",
                "1d ago",
                false
        ));

        list.add(new NotificationItem(
                "4",
                "achievement",
                R.drawable.ic_trophy,
                R.color.achievement_yellow_bg,
                "Achievement Unlocked",
                "You earned the \"Neighborhood Helper\" badge!",
                "2d ago",
                false
        ));

        list.add(new NotificationItem(
                "5",
                "comment",
                R.drawable.ic_message,
                R.color.achievement_blue_bg,
                "New Comment",
                "Jordan Lee commented on your report about broken street light",
                "3d ago",
                false
        ));

        list.add(new NotificationItem(
                "6",
                "support",
                R.drawable.ic_heart,
                R.color.achievement_red_bg,
                "New Support",
                "Sarah Martinez supported your report",
                "4d ago",
                false
        ));

        return list;
    }
}