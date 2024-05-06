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

public class org_home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private PopupMenu popupMenu;
    private ImageView hamburgerIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_home);

        // ImageView references
        ImageView imageView1 = findViewById(R.id.imageView1);
        ImageView imageView2 = findViewById(R.id.imageView2);
        ImageView imageView3 = findViewById(R.id.imageView3);
        ImageView imageView4 = findViewById(R.id.imageView4);


        // Hamburger menu icon reference
        hamburgerIcon = findViewById(R.id.hamburgerIcon);

        // Set click listener for hamburger icon
        hamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // Set click listeners for the ImageView elements
        setImageViewClickListener(imageView1, weddingupload.class);
        setImageViewClickListener(imageView2, dance_upload.class);
        setImageViewClickListener(imageView3, weddingupload.class);





        // TextView references
        TextView textView1 = findViewById(R.id.textView1);
        TextView textView2 = findViewById(R.id.textView2);
        TextView textView3 = findViewById(R.id.textView3);
        TextView textView4 = findViewById(R.id.textView4);
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
                Intent profileIntent = new Intent(org_home.this, usertype.class);
                startActivity(profileIntent);
                // Handle menu item 1 click
                break;
            case R.id.action_cart:
                Intent cartIntent = new Intent(org_home.this, cartdetails.class);
                startActivity(cartIntent);
                // Handle menu item 2 click
                break;
            // Add more cases for other menu items as needed

        }
    }


    // Helper method to set click listeners for ImageViews
    private void setImageViewClickListener(ImageView imageView, final Class<?> destinationClass) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click for the imageView (e.g., redirect to the specified class)
                Intent intent = new Intent(org_home.this, destinationClass);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_home) {
            // Redirect to admin_home activity
            Intent profileIntent = new Intent(org_home.this, admin_home.class);
            startActivity(profileIntent);
        } else if (itemId == R.id.action_profile) {
            // Redirect to ProfileActivity
            Intent profileIntent = new Intent(org_home.this, ProfileActivity.class);
            startActivity(profileIntent);
        } else if (itemId == R.id.action_cart) {
            // Redirect to cartdetails activity
            Intent cartIntent = new Intent(org_home.this, cartdetails.class);
            startActivity(cartIntent);
        }

        return true;
    }
}
