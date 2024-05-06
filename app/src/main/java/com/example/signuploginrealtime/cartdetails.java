package com.example.signuploginrealtime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class cartdetails extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, PaymentResultListener {

    LinearLayout containerLayout;
    Button payButton;

    private PopupMenu popupMenu;
    private ImageView hamburgerIcon;
    ArrayList<cart> cartItemsList = new ArrayList<>();

    TextView totalAmountTextView;
    double totalAmount = 0.0; // Variable to store the total amount
    private static final String CART_ITEMS_KEY = "cart_items";
    String selectedDateString;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartdetails);
        Checkout.preload(getApplicationContext());

        containerLayout = findViewById(R.id.containerLayout);
        payButton = findViewById(R.id.payButton);
        totalAmountTextView = findViewById(R.id.amount);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // Hamburger menu icon reference
        hamburgerIcon = findViewById(R.id.hamburgerIcon);

        // Set click listener for hamburger icon
        hamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu();
            }
        });

        // Initialize cartItems list
//        cartItems = new ArrayList<>();

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();

        // Retrieve title and selected date from intent
        String title = getIntent().getStringExtra("title");
        selectedDateString = getIntent().getStringExtra("selectedDate"); // Retrieve the selected date

        Log.d("NewsListActivity", "title: " + title);
        showAdminsData(title);
    }

    private void showAdminsData(String title) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("cart").child(auth.getCurrentUser().getUid());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                for (com.google.firebase.database.DataSnapshot adminSnapshot : snapshot.getChildren()) {
                    Log.d("NewsListActivity", "adminSnapshot: " + adminSnapshot.toString());

                    final String itemTitle = adminSnapshot.child("title").getValue(String.class);
                    String date = adminSnapshot.child("selectedDateString").getValue(String.class);
                    String imageUrl = adminSnapshot.child("imageUrl").getValue(String.class);
                    String cost = adminSnapshot.child("cost").getValue(String.class);

                    cart cartItem = new cart();
                    cartItem.settitle(itemTitle);
                    cartItem.setImageUrl(imageUrl);
                    cartItem.setcost(cost);
                    cartItem.setSelectedDateString(date);

                    // Add the cartItem to the ArrayList
                    cartItemsList.add(cartItem);

                    // Add the current item to the list

                    // Update the total amount
                    totalAmount += Double.parseDouble(cost);

                    // Inflate a new CardView layout for each item
                    View cardViewLayout = getLayoutInflater().inflate(R.layout.item_cart, null);

                    // Find Views inside the CardView layout
                    ImageView imageView = cardViewLayout.findViewById(R.id.imageViewNews);
                    TextView titleTextView = cardViewLayout.findViewById(R.id.textViewHeading);
                    TextView dateTextView = cardViewLayout.findViewById(R.id.textViewDate);
                    TextView titleText = cardViewLayout.findViewById(R.id.textView);
                    ImageView deleteBtn = cardViewLayout.findViewById(R.id.deleteIV);

                    // Load the image using Picasso
                    Picasso.get().load(imageUrl).into(imageView);

                    // Set item title and cost to TextViews
                    titleTextView.setText(itemTitle);
                    titleText.setText("$" + cost);
                    dateTextView.setText(date);

                    // Set onClickListener for the CardView
                    cardViewLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Pass the selected itemTitle to Details activity
                            Intent intent = new Intent(cartdetails.this, wedding_details.class);
                            startActivity(intent);
                        }
                    });

                    deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Remove item from UI list
                            containerLayout.removeView(cardViewLayout);

                            // Remove item from Firebase database
                            adminSnapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Item removed successfully from Firebase database
                                    // Update total amount
                                    totalAmount -= Double.parseDouble(cost);
                                    totalAmountTextView.setText(String.format(" Amount: $%.2f", totalAmount));
                                    Toast.makeText(cartdetails.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Failed to remove item from Firebase database
                                    Toast.makeText(cartdetails.this, "Failed to delete item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                    // Add the inflated CardView to the container layout
                    containerLayout.addView(cardViewLayout);
                }

                // Update the total amount TextView
                totalAmountTextView.setText(String.format(" Amount: $%.2f", totalAmount));

                // Set onClickListener for the Pay Now button
                payButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startPayment();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
                Log.e("NewsListActivity", "Error retrieving data: " + error.getMessage());
            }
        });
    }

//    private List<cart> fetchCartItemsFromSharedPreferences() {
//        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//        String cartItemsJson = sharedPreferences.getString("cartItems", "");
//
//        Gson gson = new Gson();
//        Type type = new TypeToken<List<cart>>() {}.getType();
//        return gson.fromJson(cartItemsJson, type);
//    }

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
                Intent profileIntent = new Intent(cartdetails.this, usertype.class);
                startActivity(profileIntent);
                // Handle menu item 1 click
                break;

            case R.id.action_home:
                Intent homeIntent = new Intent(cartdetails.this, userHome.class);
                startActivity(homeIntent);
                // Handle menu item 2 click
                break;

            case R.id.action_cart:
                Intent cartIntent = new Intent(cartdetails.this, cartdetails.class);
                startActivity(cartIntent);
                // Handle menu item 2 click
                break;
        }
    }

    private void startPayment() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            Checkout checkout = new Checkout();
            checkout.setKeyID("rzp_test_josUbBiaFul7oK");
            checkout.setImage(R.drawable.logo);

            final Activity activity = this;

            try {
                JSONObject options = new JSONObject();
                options.put("name", "Event-Org");
                options.put("description", "Reference No. #123456");
                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                options.put("theme.color", "#3399cc");
                options.put("currency", "INR");
                // Calculate the total amount dynamically from cartItems
                int totalAmountInPaise = (int) (totalAmount * 100);
                // Convert to paise
                options.put("amount", String.valueOf(totalAmountInPaise));
                options.put("prefill.email", "reddyomkar55@gmail.com");
                options.put("prefill.contact", "7995201194");
                JSONObject retryObj = new JSONObject();
                retryObj.put("enabled", true);
                retryObj.put("max_count", 4);
                options.put("retry", retryObj);

                checkout.open(activity, options);

            } catch (Exception e) {
                Log.e("TAG", "Error in starting Razorpay Checkout", e);
            }
        } else {
            // User not authenticated, handle accordingly
            showToast("User not authenticated");
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        try {
            Log.d("ONSUCCESS", "onPaymentSuccess: " + s);

            // Add order details to Firebase
            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
                addOrderDetailsToFirebase(userId);
                clearCartInFirebase(); // Add this line to clear the cart in Firebase
            }

            // Clear the cart after placing the order
            clearCart();

            // Show success message
            showToast("Payment successful");

            // Redirect to userHome
            Intent homeIntent = new Intent(cartdetails.this, userHome.class);
            startActivity(homeIntent);
        } catch (Exception e) {
            // Handle any exceptions that occur
            Log.e("TAG", "Exception in onPaymentSuccess: " + e.getMessage(), e);
            showToast("An error occurred: " + e.getMessage());
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.d("ONERROR", "onPaymentError: " + s);

        // Show error message
        showToast("Payment failed: " + s);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_home) {
            Intent profileIntent = new Intent(cartdetails.this, userHome.class);
            startActivity(profileIntent);
        } else if (itemId == R.id.action_profile) {
            Intent profileIntent = new Intent(cartdetails.this, ProfileActivity.class);
            startActivity(profileIntent);
        } else if (itemId == R.id.action_cart) {
            Intent profileIntent = new Intent(cartdetails.this, cartdetails.class);
            startActivity(profileIntent);
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Checkout.RZP_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Payment successful, handled in onPaymentSuccess
            } else if (resultCode == Checkout.PAYMENT_CANCELED) {
                // Payment canceled by the user
                // Handle accordingly
                showToast("Payment canceled");
            } else if (resultCode == Checkout.TLS_ERROR) {
                // Payment failed due to an error
                // Handle accordingly
                showToast("Payment failed due to TLS error");
            }
        }
    }

    private void addOrderDetailsToFirebase(String userId) {
        DatabaseReference ordersReference = FirebaseDatabase.getInstance().getReference("myOrders");

        List<cart> cartItems = cartItemsList;

        for (cart cartItem : cartItems) {
            String itemTitle = cartItem.gettitle();
            String imageUrl = cartItem.getImageUrl();
            String cost = String.valueOf(cartItem.getcost());

            // Create a unique key for each order
            String orderId = ordersReference.push().getKey();

            // Create a map with order details including user ID
            Map<String, Object> orderDetails = new HashMap<>();
            orderDetails.put("userId", userId);
            orderDetails.put("itemTitle", itemTitle);
            orderDetails.put("imageUrl", imageUrl);
            orderDetails.put("cost", cost);

            // Add the order details to the "myorders" node
            ordersReference.child(itemTitle).child(orderId).setValue(orderDetails)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Data saved successfully
                            Toast.makeText(cartdetails.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed to save data
                            Toast.makeText(cartdetails.this, "Failed to place order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void clearCartInFirebase() {
        DatabaseReference cartReference = FirebaseDatabase.getInstance().getReference("cart");

        for (cart cartItem : cartItemsList) {
            // Find the corresponding item in the "cart" node and remove it
            cartReference.orderByChild("cart").equalTo(cartItem.gettitle())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                            for (com.google.firebase.database.DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                snapshot.getRef().removeValue(); // Remove the item from Firebase
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle onCancelled if needed
                            Log.e("cartdetails", "Error removing item from cart: " + databaseError.getMessage());
                        }
                    });
        }
    }

    private void clearCart() {
        // Add logic to clear the cart in Firebase or locally
        // Clear the containerLayout or reset totalAmount if needed
        containerLayout.removeAllViews();
        totalAmount = 0.0;
        totalAmountTextView.setText(String.format(" Total Amount: $%.2f", totalAmount));

        // Redirect to userHome
        Intent homeIntent = new Intent(cartdetails.this, userHome.class);
        startActivity(homeIntent);
    }
}