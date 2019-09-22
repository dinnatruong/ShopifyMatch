package com.example.shopifymatch.data.repository;

import com.example.shopifymatch.data.model.ProductsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ShopifyService {

    @GET("/admin/products.json")
    Call<ProductsResponse> getProducts(@Query("page") int page, @Query("access_token") String accessToken);
}
