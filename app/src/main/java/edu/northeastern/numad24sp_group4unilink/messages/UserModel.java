package edu.northeastern.numad24sp_group4unilink.messages;

import com.google.firebase.Timestamp;

public class UserModel {
    private String email;
    private String firstname;
    private String userId;
    private String fcmToken;

    public UserModel() {
    }

    public UserModel(String email, String firstname, String userId) {
        this.email = email;
        this.firstname = firstname;
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
