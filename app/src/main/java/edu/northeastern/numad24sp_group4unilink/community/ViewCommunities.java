package edu.northeastern.numad24sp_group4unilink.community;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.numad24sp_group4unilink.R;

public class ViewCommunities extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference commRef;
    private String userId, userEmail;
    private Map<String, community> commIdsandComm;
    private Spinner selectCommSpinner;
    private Button buttonViewComm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_communities);
        userEmail = getIntent().getStringExtra("userEmail");
        userId=getIntent().getStringExtra("userId");

        selectCommSpinner = findViewById(R.id.selectComm);
        buttonViewComm= findViewById(R.id.buttonViewComm);
        commIdsandComm = new HashMap<>();
        populateCommSpinner();

        // Set click listener for the view community button
        buttonViewComm.setOnClickListener(v -> onViewCommunityClicked());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void populateCommSpinner() {
        db = FirebaseFirestore.getInstance();
        commRef = db.collection("communities");

        commRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<String> commTitles = new ArrayList<>();
            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                // Assuming 'tag' is the attribute in your communities collection
                String tag = document.getString("tag");
                String commId = document.getId();
                // Assuming 'users' is an array of strings in Firestore
                List<String> users = document.contains("users") ? (List<String>) document.get("users") : new ArrayList<>();
                String pictureUrl = document.getString("picture");

                // Create a community object
                community comm = new community(commId, tag, users.toArray(new String[0]), pictureUrl);

                if (tag != null && !tag.isEmpty() && !commTitles.contains(tag)) {
                    commTitles.add(tag);
                    // Add title and community object to the map
                    commIdsandComm.put(tag, comm);
                }
            }
            // Populate the spinner with community tags
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, commTitles);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selectCommSpinner.setAdapter(adapter);
        }).addOnFailureListener(e -> {
            // Handle failure to retrieve communities
            Log.e("Firestore", "Error getting communities: ", e);
        });
    }

    private void onViewCommunityClicked() {
        // Get the selected community title and corresponding community object
        String selectedCommTitle = (String) selectCommSpinner.getSelectedItem();
        community selectedComm = commIdsandComm.get(selectedCommTitle);
        if (selectedComm != null) {
            // Log the community values
            Log.d("Selected Community ID", selectedComm.getId());
            Log.d("Selected Community Tag", selectedComm.getTag());
            Log.d("Selected Community Users", Arrays.toString(selectedComm.getUsers()));
            Log.d("Selected Community Picture URL", selectedComm.getPictureUrl());

            // Start ViewACommunityActivity with necessary data in intent
            Intent intent = new Intent(ViewCommunities.this, ViewACommunity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("userEmail", userEmail);
            intent.putExtra("commId", selectedComm.getId());
            intent.putExtra("commTag", selectedComm.getTag());
            startActivity(intent);
        } else {
            // Handle case where selected community is null
            Log.e("Error", "Selected community is null");
        }
    }

}