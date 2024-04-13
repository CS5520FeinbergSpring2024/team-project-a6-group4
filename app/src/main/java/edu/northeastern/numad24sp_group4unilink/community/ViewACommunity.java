package edu.northeastern.numad24sp_group4unilink.community;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.numad24sp_group4unilink.R;

public class ViewACommunity extends AppCompatActivity {

    private Spinner spinner;
    private Spinner spinnerUsers;
    private FirebaseFirestore db;
    private CollectionReference postsRef;
    private String commTag;
    Map<String, String> userIdToEmailMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_acommunity);
        spinner = findViewById(R.id.spinner);
        spinnerUsers= findViewById(R.id.spinnerUsers);
        db = FirebaseFirestore.getInstance();

        // Get the commTag from the intent
        commTag = getIntent().getStringExtra("commTag");
        TextView userEmailTextView = findViewById(R.id.textView4);
        userEmailTextView.setText("Posts in : " + commTag);


        // Initialize Firestore reference to posts collection
        postsRef = db.collection("posts");

        // Populate spinner with posts based on the commTag
        populateSpinnerWithPosts();
        populateSpinnerUsers();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void populateSpinnerWithPosts() {
        postsRef.whereEqualTo("tag", commTag).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> postTitles = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String title = document.getString("title");
                    if (title != null && !title.isEmpty()) {
                        postTitles.add(title);
                        //fetches the right posts for each community, now all the fileds have to be extracted and displayed using recycler view similar to title!
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, postTitles);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            } else {
                // Handle failure to retrieve posts
                Toast.makeText(getApplicationContext(), "Failed to retrieve posts", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void populateSpinnerUsers() {
//        db.collection("communities").whereEqualTo("tag", commTag).get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                List<String> usersList = new ArrayList<>();
//                for (QueryDocumentSnapshot document : task.getResult()) {
//                    List<String> users = (List<String>) document.get("users");
//                    if (users != null) {
//                        usersList.addAll(users);
//                    }
//                }
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                        android.R.layout.simple_spinner_item, usersList);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spinnerUsers.setAdapter(adapter);
//            } else {
//                // Handle failure to retrieve users
//                Toast.makeText(getApplicationContext(), "Failed to retrieve users", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void populateSpinnerUsers() {
        userIdToEmailMap = new HashMap<>();
        List<String> userIdsL=new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUsers.setAdapter(adapter);
        db.collection("communities").whereEqualTo("tag", commTag).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    List<String> users = (List<String>) document.get("users");
                    if (users != null) {
                        userIdsL.addAll(users);
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



}