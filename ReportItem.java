package com.example.streetfix.data.model;

public class ReportItem {

    private String id;
    private int imageResId;
    private String title;
    private String category;
    private String location;
    private String status;
    private int supports;
    private int comments;
    private String time;
    private String username;

    private String imageUrl;       // URL slike PRIJAVE
    private boolean hasImage;      // flag iz Firestore-a
    private String userPhotoUrl;   // avatar korisnika

    public ReportItem(String id,
                      int imageResId,
                      String title,
                      String category,
                      String location,
                      String status,
                      int supports,
                      int comments,
                      String time,
                      String username,
                      String imageUrl,
                      boolean hasImage,
                      String userPhotoUrl) {
        this.id = id;
        this.imageResId = imageResId;
        this.title = title;
        this.category = category;
        this.location = location;
        this.status = status;
        this.supports = supports;
        this.comments = comments;
        this.time = time;
        this.username = username;
        this.imageUrl = imageUrl;
        this.hasImage = hasImage;
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getId() {
        return id;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return status;
    }

    public int getSupports() {
        return supports;
    }

    public int getComments() {
        return comments;
    }

    public String getTime() {
        return time;
    }

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean hasImage() {
        return hasImage;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }
}