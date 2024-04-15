package edu.northeastern.numad24sp_group4unilink.messages;
import com.google.firebase.Timestamp;
public class SharedContentModel {
    private String imageUrl; // URL of the image to display
    private String title;    // Title of the shared content
    private String description; // Description of the shared content

    public SharedContentModel() {}

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
