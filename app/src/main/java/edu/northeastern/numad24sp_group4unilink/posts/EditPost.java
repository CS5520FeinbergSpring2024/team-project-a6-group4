package edu.northeastern.numad24sp_group4unilink.posts;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.squareup.picasso.Picasso;

import edu.northeastern.numad24sp_group4unilink.R;

public class EditPost extends AppCompatActivity {
    private EditText eventTitleEditText, eventDescEditText, eventLocationEditText, editTextTime, editTextDate;
    private Spinner eventTagSpinner;
    private ImageView eventImageView;

    private TextView postIdTextView;
    private Button editPostButton;
    String postId, userId;
    FirebaseFirestore db;
    CollectionReference communitiesRef;
    String selectedCommunity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_post);

        // Get the post ID from the intent
        postId = getIntent().getStringExtra("postId");
        userId = getIntent().getStringExtra("userId");
        // Find the TextView for displaying the post ID
        postIdTextView = findViewById(R.id.textView);

        // Display the post ID in the TextView
        postIdTextView.setText("EDIT POST : " + postId);

        eventTitleEditText=findViewById(R.id.eventTitle);
        eventDescEditText=findViewById(R.id.eventDesc);
        editTextTime = findViewById(R.id.editTextTime);
        editTextDate = findViewById(R.id.editTextDate);
        eventLocationEditText = findViewById(R.id.eventlocation);
        eventImageView = findViewById(R.id.eventImage);
        editPostButton= findViewById(R.id.editPostButton);

        db = FirebaseFirestore.getInstance();

        communitiesRef = db.collection("communities");
        setUpSpinnerOptions();


        // Trigger function to populate UI with Firestore data
        populateFieldsFromFirestore();




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setUpSpinnerOptions(){
        eventTagSpinner = findViewById(R.id.eventTag);

        communitiesRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<String> tagList = new ArrayList<>();
            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                String tag = document.getString("tag");
                if (tag != null && !tag.isEmpty() && !tagList.contains(tag)) {
                    tagList.add(tag);
                }
            }
            // Populate the spinner with the tagList data
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tagList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            eventTagSpinner.setAdapter(adapter);
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Error getting tags: ", e);
        });

        // Set up the OnItemSelectedListener for the spinner
        eventTagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected tag name
                selectedCommunity = (String) parentView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case where nothing is selected (if needed)
                selectedCommunity="";
            }
        });
    }

    private void populateFieldsFromFirestore() {

        db.collection("posts").document(postId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                Log.e("Firestore", "Snapshot:" +document);
                if (document.exists()) {
                    // Populate UI fields with Firestore data
                    eventTitleEditText.setText(document.getString("title"));
                    eventDescEditText.setText(document.getString("description"));
                    eventLocationEditText.setText(document.getString("location"));

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
                    // Set selected value in spinner
                    setSpinnerSelection( document.getString("tag"));

                    String imageUrl=document.getString("picture");
                    if (imageUrl!=null) {
                        Log.e("Image stored", "url: "+imageUrl);
                        //picture
                        Picasso.get()
                                .load(imageUrl)
                                .placeholder(R.drawable.event) // Optional placeholder image
                                .error(R.drawable.error) // Optional error image
                                .into(eventImageView);
                    }
                }
            } else {
                // Handle errors while fetching data
                Log.e("Firestore", "Error getting sent post from DB ");
            }
        });
    }

    private void setSpinnerSelection( String valueToSelect) {
        // Code to set selected value in spinner
        if (eventTagSpinner != null && valueToSelect != null) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) eventTagSpinner.getAdapter();
            if (adapter != null) {
                int position = adapter.getPosition(valueToSelect);
                eventTagSpinner.setSelection(position);
            }
        }
    }
}