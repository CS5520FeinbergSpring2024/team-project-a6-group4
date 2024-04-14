package edu.northeastern.numad24sp_group4unilink.posts;

import edu.northeastern.numad24sp_group4unilink.comments.Comment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edu.northeastern.numad24sp_group4unilink.R;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class ViewPostActivity extends AppCompatActivity {

    private TextView eventTitle, eventDesc, editTextDate, editTextTime, eventlocation, eventTag;
    private ImageView eventImage;
    private Button Attend, Comment;
    private Spinner attendeesSpinner,commentsSpinner;
    String postId, userId, userEmail;
    FirebaseFirestore db;
    CollectionReference communitiesRef;
    String selectedCommunity;
    Map<String, String> userIdToEmailMap, commentIdtoComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_post);

        // Get the post ID from the intent
        postId = getIntent().getStringExtra("postId");
        userId = getIntent().getStringExtra("userId");
        userEmail = getIntent().getStringExtra("userEmail");

        eventTitle=findViewById(R.id.eventTitle);
        eventDesc=findViewById(R.id.eventDesc);
        editTextTime = findViewById(R.id.editTextTime);
        editTextDate = findViewById(R.id.editTextDate);
        eventlocation = findViewById(R.id.eventlocation);
        eventImage = findViewById(R.id.eventImage);
        eventTag=findViewById(R.id.eventTag);
        Attend= findViewById(R.id.Attend);
        Comment= findViewById(R.id.Comment);
        attendeesSpinner= findViewById(R.id.attendees);
        commentsSpinner= findViewById(R.id.comments);
        db = FirebaseFirestore.getInstance();

        communitiesRef = db.collection("communities");

        // Trigger function to populate UI with Firestore data
        populateFieldsFromFirestore();
        populateSpinnerUsers();
        populateSpinnerComments();

        // Assuming you have already initialized eventlocation TextView
        eventlocation.setOnClickListener(v -> {
            String locationQuery = eventlocation.getText().toString();
            Log.d("loc to open:", " is "+locationQuery);
            if (!locationQuery.isEmpty()) {
                // Create a Uri for the Google Maps search query
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(locationQuery));

                // Create an Intent to launch Google Maps with the search query
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps"); // Specify package to ensure Maps app is used
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    // Handle the case where Google Maps app is not installed
                    Toast.makeText(this, "Google Maps app not installed.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Handle the case where location query is empty
                Toast.makeText(this, "Location not available.", Toast.LENGTH_SHORT).show();
            }
        });

        Attend.setOnClickListener(v -> {
            // Check if the current user is already attending the post
            checkIfAttendingPost();
        });

        Comment.setOnClickListener(v -> {
            showCommentDialog();
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }


    private void populateFieldsFromFirestore() {

        db.collection("posts").document(postId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                Log.e("Firestore", "Snapshot:" +document);
                if (document.exists()) {
                    // Populate UI fields with Firestore data
                    eventTitle.setText(document.getString("title"));
                    eventDesc.setText(document.getString("description"));
                    eventlocation.setText(document.getString("location"));

                    //event Date - 2
                    // Get timestamp from Firestore
                    Timestamp timestamp = document.getTimestamp("eventDate");
                    if (timestamp != null) {
                        // Convert timestamp to Date
                        Date date = timestamp.toDate();
                        // Format date and time
                        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        String dateString = sdfDate.format(date);
                        String timeString = sdfTime.format(date);
                        // Set date and time in EditText fields
                        editTextDate.setText(dateString);
                        editTextTime.setText(timeString);
                    }

                    //tag
                    eventTag.setText(document.getString("tag"));

                    String imageUrl=document.getString("picture");
                    if (imageUrl!=null) {
                        Log.e("Image stored", "url: "+imageUrl);
                        //picture
                        Picasso.get()
                                .load(imageUrl)
                                .placeholder(R.drawable.event) // Optional placeholder image
                                .error(R.drawable.event) // Optional error image
                                .into(eventImage);
                    }
                }
            } else {
                // Handle errors while fetching data
                Log.e("Firestore", "Error getting sent post from DB ");
            }
        });
    }

    private void checkIfAttendingPost() {
        //THIS FUNCTION LITERALLY ADDS THE LOGGED IN USER TO THE EVEMNT"S LIST OF ATTENDEES
        // Reference to the post document in Firestore
        CollectionReference postsRef = db.collection("posts");
        postsRef.document(postId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Get the current attendees array
                    @SuppressWarnings("unchecked")
                    List<String> attendees = (List<String>) document.get("attendees");
                    if (attendees == null) {
                        attendees = new ArrayList<>();
                    }

                    // Check if the current user is already attending the post
                    if (!attendees.contains(userId)) {
                        // Add the current user to the attendees array
                        attendees.add(userId);

                        // Update the attendees array in Firestore
                        postsRef.document(postId).update("attendees", attendees).addOnSuccessListener(aVoid -> {
                            // Attendee added successfully
                            Toast.makeText(this, "You are now attending the event.", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e -> {
                            // Failed to update attendees array
                            Toast.makeText(this, "Failed to attend the event. Please try again.", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        // User is already attending the post
                        Toast.makeText(this, "You are already attending this event.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Post document doesn't exist
                    Toast.makeText(this, "Event not found.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Error getting post document
                Toast.makeText(this, "Error getting post information. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateSpinnerUsers() {
        userIdToEmailMap = new HashMap<>();
        List<String> userIdsL=new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        attendeesSpinner.setAdapter(adapter);
        db.collection("posts").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String documentId = document.getId();
                    if (documentId.equals(postId)) {
                        List<String> users = (List<String>) document.get("attendees");
                        if (users != null) {
                            userIdsL.addAll(users);
                        }
                        break;
                    }
                }
                Log.v( "ids", "user ids is " + userIdsL );

                // Query the users collection to get names for each user ID
                for (String user : userIdsL) {
                    db.collection("users").whereEqualTo("userID", user).get().addOnCompleteListener(userTask -> {
                        if (userTask.isSuccessful()) {
                            Log.v("ids-name success", "user ids is " + user + " doc"+userTask.getResult());
                            for (QueryDocumentSnapshot userDoc : userTask.getResult()) {
                                String name = userDoc.getString("email");
                                //sO here im only taking the id and email right, email form user, you have to take all attributes like photo when making recycler view for more info if you want

                                //TAKE MORE THAN JUST EMAIL, save as a user and use a RECYCLER VIEW HERE
                                Log.v("ids-name",name);
                                if (name != null && !name.isEmpty()) {
                                    // Save the mapping of userID to name for later use
                                    userIdToEmailMap.put(user, name);
                                    adapter.add(name);
                                    Log.v("ids-name", "user ids is " + user + " name: " + name);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            // Handle failure to retrieve user's name
                            Log.e("populateSpinnerUsers", "Error getting user's name: " + userTask.getException());
                        }
                    });
                }
                Log.v( "ids", "user emails is " + userIdToEmailMap );


            } else {
                // Handle failure to retrieve users
                Toast.makeText(getApplicationContext(), "Failed to retrieve users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateSpinnerComments() {
        commentIdtoComment = new HashMap<>();
        List<String> commentIdsL=new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        commentsSpinner.setAdapter(adapter);
        db.collection("posts").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String documentId = document.getId();
                    if (documentId.equals(postId)) {
                        List<String> comm = (List<String>) document.get("comments");
                        if (comm != null) {
                            commentIdsL.addAll(comm);
                        }
                        break;
                    }
                }
                Log.v( "c ids", "comments ids is " + commentIdsL );

                // Query the users collection to get names for each user ID
                for (String commentId : commentIdsL) {
                    db.collection("comments").document(commentId).get().addOnCompleteListener(commentTask -> {
                        if (commentTask.isSuccessful()) {
                            DocumentSnapshot commentDoc = commentTask.getResult();
                            if (commentDoc.exists()) {
                                String commentText = commentDoc.getString("comment");
                                String author = commentDoc.getString("userEmail");
                                if (commentText != null && !commentText.isEmpty()) {
                                    String commentDisplay = author + " : " + commentText;
                                    //USE RECYCLER VIEW HERFE INSTEAD

                                    commentIdtoComment.put(commentId, commentDisplay);
                                    adapter.add(commentDisplay);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            // Handle failure to retrieve comment
                            Log.e("populateSpinnerComm", "Error getting comment: " + commentTask.getException());
                        }
                    });
                }
                Log.v( "ids", "comm emails is " + commentIdtoComment );


            } else {
                // Handle failure to retrieve users
                Toast.makeText(getApplicationContext(), "Failed to retrieve users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCommentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_comment, null);
        EditText editTextComment = dialogView.findViewById(R.id.editTextComment);

        builder.setView(dialogView)
                .setTitle("Post a Comment")
                .setPositiveButton("Post", (dialog, which) -> {
                    String commentText = editTextComment.getText().toString().trim();
                    if (!commentText.isEmpty()) {
                        postComment(commentText);
                    } else {
                        Toast.makeText(this, "Please enter a comment.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void postComment(String commentText) {
        // Create a new comment document in Firestore
        // Create a Map to represent the comment data
        Map<String, Object> commentData = new HashMap<>();
        commentData.put("userId", userId);
        commentData.put("userEmail", userEmail);
        commentData.put("comment", commentText);
        commentData.put("postedDate", new Date());

        // Add the comment data to Firestore
        db.collection("comments").add(commentData)
                .addOnSuccessListener(documentReference -> {
                    String commentId = documentReference.getId(); // Get the ID of the newly created comment

                    // Update the corresponding post document with the comment ID
                    db.collection("posts").document(postId)
                            .update("comments", FieldValue.arrayUnion(commentId))
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Comment posted successfully.", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Failed to update post with comment ID.", Toast.LENGTH_SHORT).show();
                                Log.e("UpdatePost", "Error updating post with comment ID: " + e.getMessage());
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to post comment. Please try again.", Toast.LENGTH_SHORT).show();
                    Log.e("PostComment", "Error posting comment: " + e.getMessage());
                });

    }



}