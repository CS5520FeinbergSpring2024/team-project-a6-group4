package edu.northeastern.numad24sp_group4unilink.posts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.numad24sp_group4unilink.MainActivity;
import edu.northeastern.numad24sp_group4unilink.R;

public class DeleteAnyOfYourPosts extends AppCompatActivity {

    private Spinner selectPostSpinner;
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private String userId, userEmail;
    private Map<String, String> postTitlesAndIds;
    private Button startDeleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delete_any_of_your_posts);

        userEmail = getIntent().getStringExtra("userEmail");
        userId=getIntent().getStringExtra("userId");
        TextView userEmailTextView = findViewById(R.id.textView3);
        userEmailTextView.setText("Select which post to delete: as User : " + userEmail+" id:"+userId);

        selectPostSpinner = findViewById(R.id.selectPost);
        startDeleteButton= findViewById(R.id.buttonDeletePost);
        postTitlesAndIds = new HashMap<>();

        // Populate spinner with post titles
        populatePostTitlesSpinner();

        // Set an OnClickListener for the startEditButton
        startDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected title and corresponding ID
                String selectedTitle = (String) selectPostSpinner.getSelectedItem();
                String selectedId = postTitlesAndIds.get(selectedTitle);
                // Delete the post from the database using the selected ID
                showConfirmationDialog(selectedId);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //to find only the logged in user's posts, can be used in profile/my posts so he/she can edit it
    private void populatePostTitlesSpinner() {
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("posts");
        //all posts
        CollectionReference postsRef = eventsRef;
        Log.e("userId", "here:"+userId);
//        Log.e("userId in db?", "here:"+userId);
        postsRef.whereEqualTo("authorId", userId).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<String> postTitles = new ArrayList<>();
            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                // Assuming 'title' is the attribute in your posts collection
                String title = document.getString("title");
                String postId = document.getId();
                if (title != null && !title.isEmpty() && !postTitles.contains(title)) {
                    postTitles.add(title);
                    // Add title and ID to the map
                    postTitlesAndIds.put(title, postId);
                }
            }
            // Populate the spinner with post titles
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, postTitles);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selectPostSpinner.setAdapter(adapter);
        }).addOnFailureListener(e -> {
            // Handle failure to retrieve posts
            Log.e("Firestore", "Error getting posts: ", e);
        });
    }

    private void showConfirmationDialog(String selectedId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this post?");

        // Add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Yes button
                deletePost(selectedId);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked No button
                dialog.dismiss();
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void deletePost(String postId) {
        // Get the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Reference to the posts collection
        CollectionReference postsRef = db.collection("posts");

        // Delete the document with the specified post ID
        postsRef.document(postId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Post deleted successfully
                        Toast.makeText(getApplicationContext(), "Post deleted successfully", Toast.LENGTH_SHORT).show();
                        // Optionally, navigate to another activity or refresh the UI
                        // Navigate back to the main activity
                        Intent intent = new Intent(DeleteAnyOfYourPosts.this, MainActivity.class);
                        intent.putExtra("userEmail", userEmail); // Pass the user's email address to CreatePost activity
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                        finish(); // Optional, to finish the current activity
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure to delete the post
                        Log.e("Firestore", "Error deleting post: " + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Failed to delete post", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}