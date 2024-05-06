package com.example.signuploginrealtime;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    LinearLayout containerLayout;
    Button payButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        containerLayout = findViewById(R.id.linearLayout);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // Retrieve title from intent
        String name = getIntent().getStringExtra("name");

        Log.d("NewsListActivity", "name: " + name);
        showAdminsData(name);


    }

    private void showAdminsData(String name) {

        TextView titleTextView = findViewById(R.id.textViewHeading);
        TextView titleText = findViewById(R.id.textView);
        TextView titleText2 = findViewById(R.id.textView2);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot adminSnapshot : snapshot.getChildren()) {
                    Log.d("NewsListActivity", "adminSnapshot: " + adminSnapshot.toString());

                    final String name = adminSnapshot.child("name").getValue(String.class);
                    String email = adminSnapshot.child("email").getValue(String.class);
                    String password = adminSnapshot.child("password").getValue(String.class);

                    Log.d("NewsListActivity", "name: " + name);
                    Log.d("NewsListActivity", "password: " + password);



                    // Find Views inside the CardView layout
                    // Set shopName and shopAddress to TextViews
                    titleTextView.setText(name);
                    titleText.setText( email);
                    titleText2.setText(password);

                    // Set onClickListener for the CardView

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
                Log.e("NewsListActivity", "Error retrieving data: " + error.getMessage());
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_home) {
            Intent profileIntent = new Intent(ProfileActivity.this, userHome.class);
            startActivity(profileIntent);
        } else if (itemId == R.id.action_profile) {
            // Handle profile icon click
            Intent profileIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        } else if (itemId == R.id.action_cart) {
            Intent profileIntent = new Intent(ProfileActivity.this, cartdetails.class);
            startActivity(profileIntent);
        }

        return true;
    }
}
