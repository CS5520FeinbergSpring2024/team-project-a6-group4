package edu.northeastern.numad24sp_group4unilink.posts;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.northeastern.numad24sp_group4unilink.R;

public class ViewPostActivity extends AppCompatActivity {

    private TextView eventTitle, eventDesc, editTextDate, editTextTime, eventlocation, eventTag;
    private ImageView eventImage;
    private Button Attend, Comment;
    String postId, userId;
    FirebaseFirestore db;
    CollectionReference communitiesRef;
    String selectedCommunity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_post);

        // Get the post ID from the intent
        postId = getIntent().getStringExtra("postId");
        userId = getIntent().getStringExtra("userId");

        eventTitle=findViewById(R.id.eventTitle);
        eventDesc=findViewById(R.id.eventDesc);
        editTextTime = findViewById(R.id.editTextTime);
        editTextDate = findViewById(R.id.editTextDate);
        eventlocation = findViewById(R.id.eventlocation);
        eventImage = findViewById(R.id.eventImage);
        eventTag=findViewById(R.id.eventTag);
        Attend= findViewById(R.id.Attend);
        Comment= findViewById(R.id.Comment);

        db = FirebaseFirestore.getInstance();

        communitiesRef = db.collection("communities");

        // Trigger function to populate UI with Firestore data
        populateFieldsFromFirestore();

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
                                .error(R.drawable.error) // Optional error image
                                .into(eventImage);
                    }
                }
            } else {
                // Handle errors while fetching data
                Log.e("Firestore", "Error getting sent post from DB ");
            }
        });
    }

}