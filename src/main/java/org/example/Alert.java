package org.example;

public class Alert {
    // Attributes
    public int alertType;
    public String heading;
    public String description;
    public String url;
    public String imageURL;
    public String postedBy = "c55bc56a-232c-46a4-9778-7f0d41690aa2";
    public int priceInCents;

    public Alert(int alertType, String heading, String description, String url, String imageUrl, int priceInCents) {
        this.alertType = alertType;
        this.heading = heading;
        this.description = description;
        this.url = url;
        this.imageURL = imageUrl;
        this.priceInCents = priceInCents;
    }

    public Alert(){}
}