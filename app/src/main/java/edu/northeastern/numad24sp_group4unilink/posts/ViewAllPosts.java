package edu.northeastern.numad24sp_group4unilink.posts;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad24sp_group4unilink.R;

public class ViewAllPosts extends AppCompatActivity {
    private Spinner selectPostSpinner;
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private String userId, userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_all_posts);
        userEmail = getIntent().getStringExtra("userEmail");
        userId=getIntent().getStringExtra("userId");
        TextView userEmailTextView = findViewById(R.id.textView3);
        userEmailTextView.setText("Select which post to view: as User : " + userEmail);

        selectPostSpinner = findViewById(R.id.selectPost);

        // Populate spinner with post titles
        populatePostTitlesSpinner();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void populatePostTitlesSpinner() {
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("posts");
        //all posts
        CollectionReference postsRef = eventsRef;
        postsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<String> postTitles = new ArrayList<>();
            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                // Assuming 'title' is the attribute in your posts collection
                String title = document.getString("title");
                if (title != null && !title.isEmpty() && !postTitles.contains(title)) {
                    postTitles.add(title);
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
}