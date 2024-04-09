package edu.northeastern.numad24sp_group4unilink;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.northeastern.numad24sp_group4unilink.community.CreateCommunityActivity;
import edu.northeastern.numad24sp_group4unilink.community.ViewCommunities;
import edu.northeastern.numad24sp_group4unilink.posts.CreatePost;
import edu.northeastern.numad24sp_group4unilink.posts.DeleteAnyOfYourPosts;
import edu.northeastern.numad24sp_group4unilink.community.DeleteSomeCommunity;
import edu.northeastern.numad24sp_group4unilink.posts.EditAllPosts;
import edu.northeastern.numad24sp_group4unilink.posts.ViewAllPosts;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Retrieve user's email address from intent
        String userEmail = getIntent().getStringExtra("userEmail");
        if (userEmail != null) {
            // Update UI with user's email address (e.g., set text in a TextView)
            TextView userEmailTextView = findViewById(R.id.textView2);
            userEmailTextView.setText("Hello : " + userEmail);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void launchPostCreationActivity(View view) {
        Intent intent = new Intent(this, CreatePost.class);
        String userEmail = getIntent().getStringExtra("userEmail");
        String userId= getIntent().getStringExtra("userId");
        intent.putExtra("userEmail", userEmail); // Pass the user's email address to CreatePost activity
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    public void launchEditPostActivity(View view) {
        Intent intent = new Intent(this, EditAllPosts.class);
        String userEmail = getIntent().getStringExtra("userEmail");
        String userId= getIntent().getStringExtra("userId");
        intent.putExtra("userEmail", userEmail); // Pass the user's email address to CreatePost activity
        intent.putExtra("userId", userId);
        startActivity(intent);
    }
    public void launchViewPostActivity(View view) {
        Intent intent = new Intent(this, ViewAllPosts.class);
        String userEmail = getIntent().getStringExtra("userEmail");
        String userId= getIntent().getStringExtra("userId");
        intent.putExtra("userEmail", userEmail); // Pass the user's email address to CreatePost activity
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    public void launchDeletePostActivity(View view) {
        Intent intent = new Intent(this, DeleteAnyOfYourPosts.class);
        String userEmail = getIntent().getStringExtra("userEmail");
        String userId= getIntent().getStringExtra("userId");
        intent.putExtra("userEmail", userEmail); // Pass the user's email address to CreatePost activity
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    public void launchViewCommunityActivity(View view) {
        Intent intent = new Intent(this, ViewCommunities.class);
        String userEmail = getIntent().getStringExtra("userEmail");
        String userId= getIntent().getStringExtra("userId");
        intent.putExtra("userEmail", userEmail); // Pass the user's email address to CreatePost activity
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    public void launchCreateCommunityActivity(View view) {
        Intent intent = new Intent(this, CreateCommunityActivity.class);
        String userEmail = getIntent().getStringExtra("userEmail");
        String userId= getIntent().getStringExtra("userId");
        intent.putExtra("userEmail", userEmail); // Pass the user's email address to CreatePost activity
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    public void launchDeleteCommunityActivity(View view){
        Intent intent = new Intent(this, DeleteSomeCommunity.class);
        String userEmail = getIntent().getStringExtra("userEmail");
        String userId= getIntent().getStringExtra("userId");
        intent.putExtra("userEmail", userEmail); // Pass the user's email address to CreatePost activity
        intent.putExtra("userId", userId);
        startActivity(intent);
    }


}