package edu.northeastern.numad24sp_group4unilink.messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import edu.northeastern.numad24sp_group4unilink.R;

public class SharedContentAdapter extends FirestoreRecyclerAdapter<SharedContentModel, SharedContentAdapter.SharedContentViewHolder> {

    public SharedContentAdapter(@NonNull FirestoreRecyclerOptions<SharedContentModel> options) {
        super(options); // Pass options to the superclass
    }

    @Override
    protected void onBindViewHolder(@NonNull SharedContentViewHolder holder, int position, @NonNull SharedContentModel model) {
        // Set the title and description of the shared content
        holder.titleView.setText(model.getTitle());
        holder.descriptionView.setText(model.getDescription());
        // Load the image using Glide
        Glide.with(holder.imageView.getContext()).load(model.getImageUrl()).into(holder.imageView);
    }

    @NonNull
    @Override
    public SharedContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout and create a new ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shared_content, parent, false);
        return new SharedContentViewHolder(view);
    }

    static class SharedContentViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView; // ImageView for the content image
        TextView titleView; // TextView for the content title
        TextView descriptionView; // TextView for the content description

        public SharedContentViewHolder(View itemView) {
            super(itemView);
            // Initialize the views
            imageView = itemView.findViewById(R.id.imageView_shared_content);
            titleView = itemView.findViewById(R.id.textView_shared_title);
            descriptionView = itemView.findViewById(R.id.textView_shared_description);
        }
    }
}
