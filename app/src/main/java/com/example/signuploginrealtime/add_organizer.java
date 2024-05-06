package com.example.signuploginrealtime;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class add_organizer extends AppCompatActivity {

    EditText signupfirstname, signupEmail, signuplastname, signupPassword, signupbussiness;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_oraganizer);

        // Initialize FirebaseDatabase and DatabaseReference
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("organizers");

        signupfirstname = findViewById(R.id.signup_firstname);
        signuplastname = findViewById(R.id.signup_lastname);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupbussiness = findViewById(R.id.signup_bussiness);
        signupButton = findViewById(R.id.signup_button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get values from EditText fields
                String firstname = signupfirstname.getText().toString();
                String lasttname = signuplastname.getText().toString();
                String email = signupEmail.getText().toString();
                String password = signupPassword.getText().toString();
                String bussiness = signupbussiness.getText().toString();

                // Save organizer details to Firebase Realtime Database
                Addorganizer helperClass = new Addorganizer(firstname, lasttname, email, password, bussiness);
                reference.push().setValue(helperClass);

                Toast.makeText(add_organizer.this, "Organizer added successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(add_organizer.this, admin_home.class);
                startActivity(intent);
            }
        });
    }
}
