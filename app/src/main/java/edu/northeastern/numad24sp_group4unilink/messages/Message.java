package edu.northeastern.numad24sp_group4unilink.messages;

import com.google.firebase.Timestamp;

public class Message {
    private String senderId;
    private String receiverId;
    private Timestamp timestamp;
    private String sharedContentId;
    private String sharedContentType;
    private transient String imageUrl;
    private transient String title;
    private transient String description;

    public Message() {
    }

    // Constructor with all fields
    public Message(String senderId, String receiverId, Timestamp timestamp,
                   String sharedContentId, String sharedContentType) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.timestamp = timestamp;
        this.sharedContentId = sharedContentId;
        this.sharedContentType = sharedContentType;
    }

    // Getters and Setters
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getSharedContentId() {
        return sharedContentId;
    }

    public void setSharedContentId(String sharedContentId) {
        this.sharedContentId = sharedContentId;
    }

    public String getSharedContentType() {
        return sharedContentType;
    }

    public void setSharedContentType(String sharedContentType) {
        this.sharedContentType = sharedContentType;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


