package com.example.shopifymatch.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductsResponse {

    @SerializedName("products")
    private ArrayList<Product> products;

    public ArrayList<Product> getProducts() {
        return products;
    }
}
