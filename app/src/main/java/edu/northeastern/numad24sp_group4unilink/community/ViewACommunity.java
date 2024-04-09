package edu.northeastern.numad24sp_group4unilink.community;

import android.os.Bundle;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad24sp_group4unilink.R;

public class ViewACommunity extends AppCompatActivity {

    private Spinner spinner;
    private FirebaseFirestore db;
    private CollectionReference postsRef;
    private String commTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_acommunity);
        spinner = findViewById(R.id.spinner);
        db = FirebaseFirestore.getInstance();

        // Get the commTag from the intent
        commTag = getIntent().getStringExtra("commTag");
        TextView userEmailTextView = findViewById(R.id.textView4);
        userEmailTextView.setText("Posts in : " + commTag);


        // Initialize Firestore reference to posts collection
        postsRef = db.collection("posts");

        // Populate spinner with posts based on the commTag
        populateSpinnerWithPosts();
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

}