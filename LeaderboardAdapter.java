package com.example.streetfix.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.streetfix.R;
import com.example.streetfix.data.model.LeaderboardItem;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    public enum LeaderboardType {
        POINTS, REPORTS, RESOLVED
    }

    private final List<LeaderboardItem> items;
    private LeaderboardType selectedType = LeaderboardType.POINTS;

    public LeaderboardAdapter(List<LeaderboardItem> items) {
        this.items = items;
    }

    public void setSelectedType(LeaderboardType selectedType) {
        this.selectedType = selectedType;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        LeaderboardItem item = items.get(position);

        holder.textName.setText(item.getName());
        holder.imageAvatar.setImageResource(item.getAvatarResId());

        if (item.getRank() <= 3) {
            holder.textRank.setText("🏆");
            if (item.getRank() == 1) holder.textRank.setTextColor(android.graphics.Color.parseColor("#EAB308"));
            else if (item.getRank() == 2) holder.textRank.setTextColor(android.graphics.Color.parseColor("#9CA3AF"));
            else holder.textRank.setTextColor(android.graphics.Color.parseColor("#EA580C"));
        } else {
            holder.textRank.setText("#" + item.getRank());
            holder.textRank.setTextColor(android.graphics.Color.parseColor("#6B7280"));
        }

        switch (selectedType) {
            case POINTS:
                holder.textValue.setText(item.getPoints() + " pts");
                break;
            case REPORTS:
                holder.textValue.setText(item.getReports() + " reports");
                break;
            case RESOLVED:
                holder.textValue.setText(item.getResolved() + " resolved");
                break;
        }

        if (item.isCurrentUser()) {
            holder.container.setBackgroundResource(R.drawable.bg_card_highlighted);
            holder.textName.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.primary));
            holder.badgeYou.setVisibility(View.VISIBLE);
        } else {
            holder.container.setBackgroundResource(R.drawable.bg_card_white);
            holder.textName.setTextColor(android.graphics.Color.BLACK);
            holder.badgeYou.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container;
        TextView textRank, textName, textValue, badgeYou;
        ImageView imageAvatar;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.containerLeaderboardItem);
            textRank = itemView.findViewById(R.id.textRank);
            textName = itemView.findViewById(R.id.textName);
            textValue = itemView.findViewById(R.id.textValue);
            badgeYou = itemView.findViewById(R.id.badgeYou);
            imageAvatar = itemView.findViewById(R.id.imageAvatar);
        }
    }
}