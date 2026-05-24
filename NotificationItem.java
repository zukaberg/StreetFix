package com.example.streetfix.data.model;

public class NotificationItem {
    private final String id;
    private final String type;
    private final int iconResId;
    private final int iconBgColorResId;
    private final String title;
    private final String message;
    private final String time;
    private final boolean unread;

    public NotificationItem(String id, String type, int iconResId, int iconBgColorResId,
                            String title, String message, String time, boolean unread) {
        this.id = id;
        this.type = type;
        this.iconResId = iconResId;
        this.iconBgColorResId = iconBgColorResId;
        this.title = title;
        this.message = message;
        this.time = time;
        this.unread = unread;
    }

    public String getId() { return id; }
    public String getType() { return type; }
    public int getIconResId() { return iconResId; }
    public int getIconBgColorResId() { return iconBgColorResId; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getTime() { return time; }
    public boolean isUnread() { return unread; }
}