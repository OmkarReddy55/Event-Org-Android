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

public class hip_organizers extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    LinearLayout containerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiphop);

        containerLayout = findViewById(R.id.containerLayout);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

// Retrieve title from intent
        String title = getIntent().getStringExtra("title");

        Log.d("NewsListActivity", "title: " + title);
        showAdminsData(title);
    }

    private void showAdminsData(String title)  {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hip_Hop");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot adminSnapshot : snapshot.getChildren()) {
                    Log.d("NewsListActivity", "adminSnapshot: " + adminSnapshot.toString());

                    final String title = adminSnapshot.child("oranization").getValue(String.class);
                    String description = adminSnapshot.child("service_Description").getValue(String.class);
                    String imageUrl = adminSnapshot.child("imageUrl").getValue(String.class);
                    String cost = adminSnapshot.child("cost").getValue(String.class);



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
                            Intent intent = new Intent(hip_organizers.this, wedding_details.class);
                            intent.putExtra("title", title);
                            intent.putExtra("description", description);
                            intent.putExtra("imageUrl", imageUrl);
                            intent.putExtra("cost", cost);
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
            Intent profileIntent = new Intent(hip_organizers.this, userHome.class);
            startActivity(profileIntent);
        } else if (itemId == R.id.action_profile) {
            // Handle profile icon click
            Intent profileIntent = new Intent(hip_organizers.this, ProfileActivity.class);

            startActivity(profileIntent);
        } else if (itemId == R.id.action_cart) {
            Intent profileIntent = new Intent(hip_organizers.this, cart.class);

            startActivity(profileIntent);
        }

        return true;
    }

}