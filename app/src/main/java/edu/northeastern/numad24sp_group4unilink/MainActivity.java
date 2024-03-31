package edu.northeastern.numad24sp_group4unilink;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.northeastern.numad24sp_group4unilink.events.EventsActivity;
import edu.northeastern.numad24sp_group4unilink.groups.GroupsActivity;
import edu.northeastern.numad24sp_group4unilink.profile.ProfileActivity;

public class MainActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                // Handle home action
                openHome();
            } else if (itemId == R.id.events) {
                // Handle events action
                openEvents();
            } else if (itemId == R.id.fab) {
                // Handle add action
                openAdd();
            } else if (itemId == R.id.groups) {
                // Handle groups action
                openGroups();
            } else if (itemId == R.id.profile) {
                // Handle profile action
                openProfile();
            }
            return true; // return true to indicate you've handled the selection
        });

        FloatingActionButton fab = findViewById(R.id.addButon);
        fab.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(MainActivity.this, view);
            popup.getMenuInflater().inflate(R.menu.add_menu, popup.getMenu());

            // Setting the onClick Listener for the menu items
            popup.setOnMenuItemClickListener(item -> {

                int itemId = item.getItemId();
                if (itemId == R.id.addPost) {
                    openHome();
                    return true;
                } else if (itemId == R.id.addEvent) {
                    // Handle events action
                    openEvents();
                    return true;
                } else if (itemId == R.id.addGroup) {
                    // Handle add action
                    openGroups();
                    return true;
                }else{
                    return false;
                }

            });

            // Showing the popup menu
            popup.show();
        });



    }

    public void openHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openEvents(){
        Intent intent = new Intent(this, EventsActivity.class);
        startActivity(intent);
    }

    public void openGroups(){
        Intent intent = new Intent(this, GroupsActivity.class);
        startActivity(intent);
    }

    public void openAdd(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void openProfile(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}