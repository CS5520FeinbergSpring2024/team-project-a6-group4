package edu.northeastern.numad24sp_group4unilink.messages;


public class UserModel {
    private String firstName; // User's first name
    private String email;     // User's email address

    // Default constructor required for Firestore data fetching
    public UserModel() {}

    // Getters and setters for the fields
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
