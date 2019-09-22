package com.example.shopifymatch.data.model;

import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("src")
    private String src;

    public String getSrc() {
        return src;
    }
}
