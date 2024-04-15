package edu.northeastern.numad24sp_group4unilink.messages;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import edu.northeastern.numad24sp_group4unilink.R;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.UserViewHolder> {
    // List to hold UserModel data
    private final List<UserModel> userList;

    // Constructor
    public SearchUserAdapter(List<UserModel> userList) {
        this.userList = userList;
    }

    // Create new views for each item
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_user_recycler_view, parent, false);
        return new UserViewHolder(view);
    }

    // Replace the contents of a view
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserModel currentUser = userList.get(position);
        holder.firstNameText.setText(currentUser.getFirstName()); // Set the user's first name
        holder.emailText.setText(currentUser.getEmail()); // Set the user's email address
    }

    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return userList.size();
    }

    // Provide a reference to the views for each data item
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView firstNameText; // TextView for user's first name
        TextView emailText; // TextView for user's email address

        public UserViewHolder(View itemView) {
            super(itemView);
            firstNameText = itemView.findViewById(R.id.firstName_text);
            emailText = itemView.findViewById(R.id.email_text);
        }
    }
}
