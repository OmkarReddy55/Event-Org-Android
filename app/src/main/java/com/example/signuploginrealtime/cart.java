package com.example.signuploginrealtime;

import android.content.Context;
import android.widget.Toast;

public class cart {

    private String title;
    private String cost;
    private String Service_Description;
    private String imageUrl;
    private String selectedDateString; // New field for selected date

    // Default constructor required for Firebase
    public cart(String itemTitle, String imageUrl, double v, String selectedDateString) {
    }

    public cart(String title, String cost, String imageUrl, String selectedDateString) {
        this.title = title;
        this.cost = cost;
        this.imageUrl = imageUrl;
        this.selectedDateString = selectedDateString;
    }

    public cart() {

    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public String getcost() {
        return cost;
    }

    public void setcost(String cost) {
        this.cost = cost;
    }

    public String getService_Description() {
        return Service_Description;
    }

    public void setService_Description(String service_Description) {
        Service_Description = service_Description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSelectedDateString() {
        return selectedDateString;
    }

    public void setSelectedDateString(String selectedDateString) {
        this.selectedDateString = selectedDateString;
    }

    // Method to display a toast message
    public void showToast(Context context) {
        Toast.makeText(context, "Added to cart successfully", Toast.LENGTH_SHORT).show();
    }
}
