package edu.northeastern.numad24sp_group4unilink.messages;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad24sp_group4unilink.R;

public class SearchUserActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SearchUserAdapter adapter;
    private List<UserModel> userList = new ArrayList<>();
    private EditText searchInput;
    private ImageButton searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        RecyclerView recyclerView = findViewById(R.id.search_user_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchUserAdapter(userList);
        recyclerView.setAdapter(adapter);

        searchInput = findViewById(R.id.search_username_input);
        Button searchButton = findViewById(R.id.search_user_btn);

        searchButton.setOnClickListener(v -> performSearch(searchInput.getText().toString()));
    }

    private void performSearch(String searchTerm) {
        Query query = FirebaseFirestore.getInstance().collection("users")
                .orderBy("firstName") // assuming "firstName" is the field you want to search by
                .startAt(searchTerm).endAt(searchTerm + "\uf8ff"); // search for terms starting with the input

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            userList.clear();
            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                UserModel user = snapshot.toObject(UserModel.class);
                userList.add(user);
            }
            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            // Handle errors, possibly update the UI to indicate a failed search
        });
    }
}
