package com.example.streetfix.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.streetfix.R;
import com.example.streetfix.data.model.AchievementItem;

import java.util.List;

public class AchievementsAdapter extends RecyclerView.Adapter<AchievementsAdapter.AchievementViewHolder> {

    private final List<AchievementItem> achievements;

    public AchievementsAdapter(List<AchievementItem> achievements) {
        this.achievements = achievements;
    }

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_achievement, parent, false);
        return new AchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        AchievementItem item = achievements.get(position);

        holder.iconAchievement.setImageResource(item.getIconResId());
        holder.textTitle.setText(item.getTitle());
        holder.textDescription.setText(item.getDescription());

        holder.iconContainer.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.getContext(), item.getBgColorResId())
        );

        if (item.isUnlocked()) {
            holder.iconCheck.setVisibility(View.VISIBLE);
            holder.layoutProgress.setVisibility(View.GONE);
            holder.itemView.setAlpha(1f);
        } else {
            holder.iconCheck.setVisibility(View.GONE);
            holder.layoutProgress.setVisibility(View.VISIBLE);
            holder.itemView.setAlpha(0.6f);

            holder.textProgress.setText(item.getProgress() + "/" + item.getTarget());
            holder.progressBar.setMax(item.getTarget());
            holder.progressBar.setProgress(item.getProgress());
        }
    }

    @Override
    public int getItemCount() {
        return achievements != null ? achievements.size() : 0;
    }

    static class AchievementViewHolder extends RecyclerView.ViewHolder {
        LinearLayout iconContainer;
        ImageView iconAchievement, iconCheck;
        TextView textTitle, textDescription, textProgress;
        ProgressBar progressBar;
        LinearLayout layoutProgress;

        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);

            iconContainer = itemView.findViewById(R.id.iconContainer);
            iconAchievement = itemView.findViewById(R.id.iconAchievement);
            iconCheck = itemView.findViewById(R.id.iconCheck);
            textTitle = itemView.findViewById(R.id.textAchievementTitle);
            textDescription = itemView.findViewById(R.id.textAchievementDescription);
            textProgress = itemView.findViewById(R.id.textProgress);
            progressBar = itemView.findViewById(R.id.progressAchievement);
            layoutProgress = itemView.findViewById(R.id.layoutProgress);
        }
    }
}