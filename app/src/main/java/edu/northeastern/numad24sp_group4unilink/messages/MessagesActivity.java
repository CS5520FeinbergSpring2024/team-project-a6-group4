package edu.northeastern.numad24sp_group4unilink.messages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telecom.Call;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import edu.northeastern.numad24sp_group4unilink.BaseActivity;
import edu.northeastern.numad24sp_group4unilink.R;
import edu.northeastern.numad24sp_group4unilink.databinding.ActivityMessagesBinding;
import edu.northeastern.numad24sp_group4unilink.utils.AndroidUtil;
import edu.northeastern.numad24sp_group4unilink.utils.FirebaseUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.callback.Callback;

public class MessagesActivity extends BaseActivity {

    UserModel otherUser;
    String messagesId;
    MessagesModel messagesModel;
    MessagesRecyclerAdapter adapter;

    EditText messageInput;
    ImageButton sendMessageBtn;
    ImageButton backBtn;
    TextView otherUsername;
    RecyclerView recyclerView;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        messagesId = FirebaseUtil.getMessagesId(FirebaseUtil.currentUserId(), otherUser.getUserId());

        messageInput = findViewById(R.id.chat_message_input);
        sendMessageBtn = findViewById(R.id.message_send_btn);
        backBtn = findViewById(R.id.back_btn);
        otherUsername = findViewById(R.id.other_username);
        recyclerView = findViewById(R.id.chat_recycler_view);
        imageView = findViewById(R.id.profile_pic_image_view);

        backBtn.setOnClickListener((v) -> {
            onBackPressed();
        });
        otherUsername.setText(otherUser.getFirstname());

        sendMessageBtn.setOnClickListener((v -> {
            String message = messageInput.getText().toString().trim();
            if (message.isEmpty())
                return;
            sendMessageToUser(message);
        }));

        getOrCreateMessagesModel();
        setupMessagesRecyclerView();
    }

    void setupMessagesRecyclerView() {
        Query query = FirebaseUtil.getMessagesMessageReference(messagesId)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<MessageModel> options = new FirestoreRecyclerOptions.Builder<MessageModel>()
                .setQuery(query, MessageModel.class).build();

        adapter = new MessagesRecyclerAdapter(options, getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    void sendMessageToUser(String message) {
        messagesModel.setLastMessageTimestamp(Timestamp.now());
        messagesModel.setLastMessageSenderId(FirebaseUtil.currentUserId());
        messagesModel.setLastMessage(message);
        FirebaseUtil.getMessagesReference(messagesId).set(messagesModel);

        MessageModel messageModel = new MessageModel(message, FirebaseUtil.currentUserId(), Timestamp.now());
        FirebaseUtil.getMessagesReference(messagesId).collection(message).add(messageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            messageInput.setText("");
                        }
                    }
                });
    }

    void getOrCreateMessagesModel() {
        FirebaseUtil.getMessagesReference(messagesId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                messagesModel = task.getResult().toObject(MessagesModel.class);
                if (messagesModel == null) {
                    messagesModel = new MessagesModel(
                            messagesId,
                            Arrays.asList(FirebaseUtil.currentUserId(), otherUser.getUserId()),
                            Timestamp.now(),
                            ""
                    );
                    FirebaseUtil.getMessagesReference(messagesId).set(messagesModel);
                }
            }
        });
    }

}
