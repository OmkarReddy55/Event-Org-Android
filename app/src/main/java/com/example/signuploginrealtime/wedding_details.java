package com.example.signuploginrealtime;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class wedding_details extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView bookNameTextView, authorNameTextView, copiesAvailableTextView, selectedDateTextView;
    private ImageView bookCoverImageView, hamburgerIcon;
    private Button reserveButton;
    private StorageReference storageReference;
    private DatabaseReference cartReference;
    private FirebaseUser currentUser;

    private Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wedding_details);

        storageReference = FirebaseStorage.getInstance().getReference().child("cart");
        cartReference = FirebaseDatabase.getInstance().getReference("cart");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Initialize views
        bookCoverImageView = findViewById(R.id.bookCoverImageView);
        bookNameTextView = findViewById(R.id.oranization);
        authorNameTextView = findViewById(R.id.cost);
        copiesAvailableTextView = findViewById(R.id.service_Description);
        selectedDateTextView = findViewById(R.id.dateSelectionTextView);
        reserveButton = findViewById(R.id.reserveButton);
        hamburgerIcon = findViewById(R.id.hamburgerIcon);

        // Set click listener for hamburger icon
        hamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu();
            }
        });

        // Set bottom navigation view listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // Get intent data
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("imageUrl");
        String cost = intent.getStringExtra("cost");
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");

        // Display data in views
        bookNameTextView.setText(title);
        authorNameTextView.setText("$" + cost);
        copiesAvailableTextView.setText(description);
        Picasso.get().load(imageUrl).into(bookCoverImageView);

        // Set click listener for reserve button
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDate != null) {
                    addToCart(title, cost, imageUrl);
                } else {
                    Toast.makeText(wedding_details.this, "Please select a date first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set click listener for date selection text view
        selectedDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Initially disable reserve button
        reserveButton.setEnabled(false);
    }

    private void addToCart(String title, String cost, String imageUrl) {
        String selectedDateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.getTime());

        // Create a CartItem object
        cart cartItem = new cart(title, cost, imageUrl, selectedDateString);

        // Add the cart item to the database under the current user's ID
        DatabaseReference userCartReference = cartReference.child(currentUser.getUid()).child(title);
        userCartReference.setValue(cartItem);

        // Display a toast message
        Toast.makeText(wedding_details.this, "Added to cart successfully", Toast.LENGTH_SHORT).show();
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedDate = Calendar.getInstance();
                selectedDate.set(Calendar.YEAR, year);
                selectedDate.set(Calendar.MONTH, monthOfYear);
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                selectedDateTextView.setText(sdf.format(selectedDate.getTime()));

                // Enable reserve button after selecting a date
                reserveButton.setEnabled(true);
            }
        };

        new DatePickerDialog(wedding_details.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showPopupMenu() {
        // Creating a popup menu
        PopupMenu popupMenu = new PopupMenu(this, hamburgerIcon);
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
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(wedding_details.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.action_home:
                Intent homeIntent = new Intent(wedding_details.this, userHome.class);
                startActivity(homeIntent);
                break;
            case R.id.action_cart:
                Intent cartIntent = new Intent(wedding_details.this, cartdetails.class);
                startActivity(cartIntent);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_home) {
            Intent homeIntent = new Intent(wedding_details.this, userHome.class);
            startActivity(homeIntent);
        } else if (itemId == R.id.action_profile) {
            Intent profileIntent = new Intent(wedding_details.this, ProfileActivity.class);
            startActivity(profileIntent);
        } else if (itemId == R.id.action_cart) {
            Intent cartIntent = new Intent(wedding_details.this, cartdetails.class);
            startActivity(cartIntent);
        }

        return true;
    }
}
