package com.example.signuploginrealtime;


public class wedding {

    private String oranization;
    private String cost;
    private String Service_Description;
    private String imageUrl;


    // Default constructor required for Firebase
    public wedding() {
    }

    public wedding( String oranization,String Service_Description, String cost, String imageUrl) {

        this.oranization = oranization;
        this.Service_Description = Service_Description;
        this.cost = cost;
        this.imageUrl = imageUrl;

    }



    public String getService_Description() {
        return Service_Description;
    }

    public void setService_Description(String Service_Description) {
        this.Service_Description = Service_Description;
    }

    public String getoranization() {
        return oranization;
    }

    public void setoranization(String oranization) {
        this.oranization = oranization;
    }
    public String getcost() {
        return cost;
    }

    public void setcost(String cost) {
        this.cost = cost;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}