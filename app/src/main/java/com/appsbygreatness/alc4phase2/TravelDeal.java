package com.appsbygreatness.alc4phase2;

import java.io.Serializable;

public class TravelDeal implements Serializable {

    private String id;
    private String title;
    private String price;
    private String description;
    private String imageUrl;
    private String imageName;

    TravelDeal(){}

    public TravelDeal(String title, String price, String description, String imageUrl, String imageName) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.imageName = imageName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
