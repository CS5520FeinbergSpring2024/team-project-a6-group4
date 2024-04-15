package edu.northeastern.numad24sp_group4unilink.messages;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import edu.northeastern.numad24sp_group4unilink.BaseActivity;
import edu.northeastern.numad24sp_group4unilink.R;

public class MessagesActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private SharedContentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages); // Set the layout for the activity
        recyclerView = findViewById(R.id.recycler_view_shared_content); // Find the RecyclerView
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = FirebaseFirestore.getInstance()
                .collection("messages")
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<SharedContentModel> options = new FirestoreRecyclerOptions.Builder<SharedContentModel>()
                .setQuery(query, SharedContentModel.class)
                .build();

        adapter = new SharedContentAdapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}