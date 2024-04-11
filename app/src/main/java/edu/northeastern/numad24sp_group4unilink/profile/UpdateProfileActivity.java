package edu.northeastern.numad24sp_group4unilink.profile;

import static edu.northeastern.numad24sp_group4unilink.Login.mAuth;
import static edu.northeastern.numad24sp_group4unilink.profile.ProfileActivity.currentUserDocId;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.northeastern.numad24sp_group4unilink.BaseActivity;
import edu.northeastern.numad24sp_group4unilink.R;
import edu.northeastern.numad24sp_group4unilink.databinding.ActivityProfileBinding;

public class UpdateProfileActivity extends BaseActivity {

    private String firstname, lastname, about, gender, level;
    private FirebaseFirestore userDB;
    private FirebaseUser currentUser;

    private EditText editTextFirstName, editTextLastName, editTextAbout;
    private RadioGroup editTextGender, editTextLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primaryDarkColor)));

            userDB = FirebaseFirestore.getInstance();

            currentUser = mAuth.getCurrentUser();

            populateIntentData();

        Button saveDetails = findViewById(R.id.saveDetailsId);

        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileDetails();
                finish();
            }
        });

        // handles the back button press with a confirmation dialog
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
                builder.setTitle("Unsaved changes").setMessage("Are you sure you want to dismiss?").setCancelable(false);
                builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    finish();
                });
                builder.setNegativeButton(android.R.string.no, null);
                builder.show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

    }

    private void populateIntentData() {

        Intent intent = getIntent();
        firstname = intent.getStringExtra("firstName");
        lastname = intent.getStringExtra("lastName");
        about = intent.getStringExtra("about");
        gender = intent.getStringExtra("gender");
        level = intent.getStringExtra("level");

        Log.d("Update Profile", "First name:" + firstname);

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextAbout = findViewById(R.id.editTextAbout);
        editTextGender = findViewById(R.id.editTextGender);
        editTextLevel = findViewById(R.id.editTextLevel);

        editTextFirstName.setText(firstname);
        editTextLastName.setText(lastname);
        editTextAbout.setText(about);

        if("Male".equals(gender)){
            editTextGender.check(R.id.radioMaleId);
        } else if ("Female".equals(gender)){
            editTextGender.check(R.id.radioFemaleId);
        }

        if("Male".equals(gender)){
            editTextGender.check(R.id.radioMaleId);
        } else if ("Female".equals(gender)){
            editTextGender.check(R.id.radioFemaleId);
        }
        switch (level){
            case "Graduate": editTextLevel.check(R.id.radioGradId);
                break;
            case "Undergraduate": editTextLevel.check(R.id.radioUnderGradId);
                break;
            case "Other": editTextLevel.check(R.id.radioOtherLevelId);
                break;
            default:
                break;
        }

    }

    private void saveProfileDetails() {
        
        String fName = editTextFirstName.getText().toString();
        String lName = editTextLastName.getText().toString();
        String about = editTextAbout.getText().toString();

        int selectedGenderId = editTextGender.getCheckedRadioButtonId();
        String gender = "";

        if(selectedGenderId != -1){
            RadioButton selectedGenderBtn = findViewById(selectedGenderId);
                gender = selectedGenderBtn.getText().toString();
        }

        int selectedLevelId = editTextLevel.getCheckedRadioButtonId();
        String level = "";

        if(selectedLevelId != -1){
            RadioButton selectedLevelBtn = findViewById(selectedLevelId);
            level = selectedLevelBtn.getText().toString();
        }

        if (currentUser != null) {
            DocumentReference userDoc = userDB.collection("users").document(currentUserDocId);

            Map<String, Object> userDetails = new HashMap<>();
            userDetails.put("firstName", fName);
            userDetails.put("lastName", lName);
            userDetails.put("about", about);
            userDetails.put("gender", gender);
            userDetails.put("level", level);

            userDoc.update(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("Firebase", "Details updated:Success");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.w("Firebase", "Value updated:Success" + e);
                }
            });

        }
    }


}