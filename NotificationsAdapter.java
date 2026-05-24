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
import com.example.streetfix.data.model.NotificationItem;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {

    private final List<NotificationItem> notifications;

    public NotificationsAdapter(List<NotificationItem> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationItem item = notifications.get(position);

        holder.textTitle.setText(item.getTitle());
        holder.textMessage.setText(item.getMessage());
        holder.textTime.setText(item.getTime());
        holder.imageIcon.setImageResource(item.getIconResId());

        holder.iconContainer.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.getContext(), item.getIconBgColorResId())
        );

        if (item.isUnread()) {
            holder.unreadDot.setVisibility(View.VISIBLE);
            holder.container.setBackgroundResource(R.drawable.bg_notification_unread);
        } else {
            holder.unreadDot.setVisibility(View.GONE);
            holder.container.setBackgroundResource(R.drawable.bg_card_white);
        }
    }

    @Override
    public int getItemCount() {
        return notifications != null ? notifications.size() : 0;
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container, iconContainer;
        ImageView imageIcon;
        TextView textTitle, textMessage, textTime;
        View unreadDot;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.containerNotification);
            iconContainer = itemView.findViewById(R.id.iconContainer);
            imageIcon = itemView.findViewById(R.id.imageNotificationIcon);
            textTitle = itemView.findViewById(R.id.textNotificationTitle);
            textMessage = itemView.findViewById(R.id.textNotificationMessage);
            textTime = itemView.findViewById(R.id.textNotificationTime);
            unreadDot = itemView.findViewById(R.id.unreadDot);
        }
    }
}