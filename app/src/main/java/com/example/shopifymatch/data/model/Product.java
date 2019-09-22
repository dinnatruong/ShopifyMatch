package com.example.shopifymatch.data.model;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("id")
    private String id;

    @SerializedName("image")
    private Image image;

    private boolean isFaceShown;

    public Product(Product product) {
        this.id = product.id;
        this.image = product.image;
        this.isFaceShown = product.isFaceShown;
    }

    public String getId() {
        return id;
    }

    public Image getImage() {
        return image;
    }

    public boolean isFaceShown() {
        return isFaceShown;
    }

    public void setFaceShown(boolean faceShown) {
        isFaceShown = faceShown;
    }
}
