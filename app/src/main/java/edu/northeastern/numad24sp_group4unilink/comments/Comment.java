package edu.northeastern.numad24sp_group4unilink.comments;

import java.util.Date;

public class Comment {
    private String userId;
    private String authorEmail;
    private String comment;
    private Date postedOn;

    public Comment() {
        // Default constructor required for Firestore
    }

    public Comment(String userId, String userEmail, String comment, Date postedDate) {
        this.userId = userId;
        this.authorEmail = userEmail;
        this.comment = comment;
        this.postedOn = postedDate;
    }

    // Add getters and setters
    // ...
}