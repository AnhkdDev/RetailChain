package model;

import java.sql.Timestamp;

public class Notification {

    private int notificationID;
    private String title;
    private String message;
    private boolean isRead;
    private Timestamp createdAt;
    private Integer userID; // Nullable to match schema

    // Default constructor
    public Notification() {
    }

    // Parameterized constructor
    public Notification(int notificationID, String title, String message, boolean isRead, Timestamp createdAt, Integer userID) {
        this.notificationID = notificationID;
        this.title = title;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.userID = userID;
    }

    // Getters and Setters
    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }
}