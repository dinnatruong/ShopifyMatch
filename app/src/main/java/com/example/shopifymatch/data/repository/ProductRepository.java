package com.example.shopifymatch.data.repository;

import android.arch.lifecycle.MutableLiveData;

import com.example.shopifymatch.data.model.Product;
import com.example.shopifymatch.data.model.ProductsResponse;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
    private ArrayList<Product> products = new ArrayList<>();
    private MutableLiveData<ArrayList<Product>> cardProducts = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Product>> getCardProducts() {
        ShopifyService shopifyService = new RetrofitClient().getShopifyService();

        final Call<ProductsResponse> productsCall = shopifyService.getProducts(1, "c32313df0d0ef512ca64d5b336a0d7c6");
        productsCall.enqueue(new Callback<ProductsResponse>() {
            @Override
            public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {
                if (response.isSuccessful()) {

                    ProductsResponse productsResponse = response.body();

                    if (productsResponse.getProducts() != null) {
                        products = productsResponse.getProducts();
                        ArrayList<Product> cards = new ArrayList<>();
                        Collections.shuffle(products);

                        // Add pairs of cards to deck face down
                        for (int i = 0; i < 10; i++) {
                            products.get(i).setFaceShown(false);
                            cards.add(new Product(products.get(i)));
                            cards.add(new Product(products.get(i)));
                        }

                        Collections.shuffle(cards);
                        cardProducts.setValue(cards);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductsResponse> call, Throwable t) {

            }
        });

        return cardProducts;
    }
}
