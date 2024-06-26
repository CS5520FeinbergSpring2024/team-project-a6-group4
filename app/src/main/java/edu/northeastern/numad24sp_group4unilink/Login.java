package edu.northeastern.numad24sp_group4unilink;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class Login extends AppCompatActivity {
    public static FirebaseAuth mAuth;
    private String email, password;
    private EditText emailText, passwordText;
    private ProgressBar progressBar;
    public static FirebaseUser loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primaryDarkColor)));

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        emailText = findViewById(R.id.enterEmailId);
        passwordText = findViewById(R.id.enterPasswordId);
        progressBar = findViewById(R.id.loginProgressBarId);
        final Button loginButton = findViewById(R.id.buttonLogin);
        final TextView registerNow = findViewById(R.id.registerNow);

        loginButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            email = emailText.getText().toString().trim();
            password = passwordText.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Login.this, "Enter both email and password.", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(Login.this, task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in successful
                            Log.d("Firebase", "signInWithEmail:success");
                            Toast.makeText(Login.this, "Sign in is successful.",
                                    Toast.LENGTH_SHORT).show();
                            loggedInUser = mAuth.getCurrentUser();

                            homepage(loggedInUser.getEmail(), loggedInUser.getUid());

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Firebase", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Incorrect credentials.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        registerNow.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Register.class);
            //intent.putExtra("username", usernameTxt);
            startActivity(intent);
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            loggedInUser = currentUser;
            homepage(loggedInUser.getEmail(), loggedInUser.getUid());
        }
    }

    public void homepage(String userEmail, String userId) {
        Intent intent = new Intent(Login.this, MainActivity.class);
        intent.putExtra("userEmail", userEmail); // Pass the user's email address to CreatePost activity
        intent.putExtra("userID", userId);
        intent.putExtra("EVENTS_TYPE","ALL_EVENTS");
        startActivity(intent);
        finish();
    }

}
