package com.example.signuploginrealtime;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

public class dance extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener {

    LinearLayout containerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub1);

        containerLayout = findViewById(R.id.containerLayout);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


        showAdminsData();
    }

    private void showAdminsData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("dance");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot adminSnapshot : snapshot.getChildren()) {
                    Log.d("NewsListActivity", "adminSnapshot: " + adminSnapshot.toString());

                    final String title = adminSnapshot.child("oranization").getValue(String.class);
                    String description = adminSnapshot.child("description").getValue(String.class);
                    String imageUrl = adminSnapshot.child("imageUrl").getValue(String.class);

                    Log.d("NewsListActivity", "title: " + title);
                    Log.d("NewsListActivity", "description: " + description);
                    Log.d("NewsListActivity", "imageUrl: " + imageUrl);

                    // Inflate the CardView layout
                    View cardViewLayout = getLayoutInflater().inflate(R.layout.item_org, null);

                    // Find Views inside the CardView layout
                    ImageView ImageView = cardViewLayout.findViewById(R.id.imageViewNews);
                    TextView titleTextView = cardViewLayout.findViewById(R.id.textViewHeading);


                    // Load the image using Picasso
                    Picasso.get().load(imageUrl).into(ImageView);

                    // Set shopName and shopAddress to TextViews
                    titleTextView.setText(title);

                    // Set onClickListener for the CardView
                    cardViewLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Pass the selected shopName to Covers activity
                            Intent intent = new Intent(dance.this, hip_organizers.class);
                            intent.putExtra("title", title);
                            startActivity(intent);
                        }
                    });

                    // Add the inflated CardView to the container layout
                    containerLayout.addView(cardViewLayout);
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
            Intent profileIntent = new Intent(dance.this, userHome.class);
            startActivity(profileIntent);
        } else if (itemId == R.id.action_profile) {
            // Handle profile icon click
            Intent profileIntent = new Intent(dance.this, ProfileActivity.class);

            startActivity(profileIntent);
        } else if (itemId == R.id.action_cart) {
            Intent profileIntent = new Intent(dance.this, cart.class);

            startActivity(profileIntent);
        }

        return true;
    }

}