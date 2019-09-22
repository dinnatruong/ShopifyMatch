package com.example.shopifymatch.data.repository;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private Retrofit retrofit;
    private String BASE_URL = "https://shopicruit.myshopify.com";

    public ShopifyService getShopifyService() {

        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(ShopifyService.class);
    }
}
