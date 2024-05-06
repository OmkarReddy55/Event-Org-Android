package com.example.signuploginrealtime;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class userHome extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private PopupMenu popupMenu;
    private ImageView hamburgerIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        // ImageView references
        ImageView imageView1 = findViewById(R.id.imageView1);
        ImageView imageView2 = findViewById(R.id.imageView2);
        ImageView imageView3 = findViewById(R.id.imageView3);
        ImageView imageView4 = findViewById(R.id.imageView4);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // Set click listeners for the ImageView elements
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click for imageView1 (e.g., redirect to sub1 activity)
                redirectTosub1();
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click for imageView2 (e.g., redirect to dance activity)
                Intent intent = new Intent(userHome.this, dance.class);
                startActivity(intent);
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click for imageView3 (e.g., redirect to weddingupload activity)
//                Intent intent = new Intent(userHome.this, weddingupload.class);
//                startActivity(intent);
            }
        });

        // Hamburger menu icon reference
        hamburgerIcon = findViewById(R.id.hamburgerIcon);

        // Set click listener for hamburger icon
        hamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu();
            }
        });

        // TextView references
        TextView textView1 = findViewById(R.id.textView1);
        TextView textView2 = findViewById(R.id.textView2);
        TextView textView3 = findViewById(R.id.textView3);
        TextView textView4 = findViewById(R.id.textView4);
    }

    private void redirectTosub1() {
        // Create an Intent to start sub1 activity
        Intent intent = new Intent(this, sub1.class);
        startActivity(intent);
    }

    private void showPopupMenu() {
        // Creating a popup menu
        popupMenu = new PopupMenu(this, hamburgerIcon);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        // Set a listener for menu item clicks
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                handlePopupMenuItemClick(item);
                return true;
            }
        });

        // Show the popup menu
        popupMenu.show();
    }

    private void handlePopupMenuItemClick(MenuItem item) {
        // Handle the clicks on popup menu items
        switch (item.getItemId()) {
            case R.id.logout:
                Intent profileIntent = new Intent(userHome.this, usertype.class);
                startActivity(profileIntent);
                // Handle menu item 1 click
                break;
            case R.id.action_cart:
                Intent cartIntent = new Intent(userHome.this, cartdetails.class);
                startActivity(cartIntent);
                // Handle menu item 2 click
                break;
            // Add more cases for other menu items as needed

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_home) {
            // Handle the click on the home icon (e.g., navigate to userHome activity)
            Intent profileIntent = new Intent(userHome.this, userHome.class);
            startActivity(profileIntent);
        } else if (itemId == R.id.action_profile) {
            // Handle profile icon click (e.g., navigate to ProfileActivity)
            Intent profileIntent = new Intent(userHome.this, ProfileActivity.class);
            startActivity(profileIntent);
        } else if (itemId == R.id.action_cart) {
            // Handle cart icon click (e.g., navigate to cartdetails activity)
            Intent profileIntent = new Intent(userHome.this, cartdetails.class);
            startActivity(profileIntent);
        }

        return true;
    }
}