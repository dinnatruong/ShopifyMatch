package com.example.shopifymatch.view.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.shopifymatch.R;
import com.example.shopifymatch.data.model.Product;
import com.example.shopifymatch.view.adapter.MatchingCardAdapter;
import com.example.shopifymatch.viewmodel.CardMatchingViewModel;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements MatchingCardAdapter.ItemClickListener {

    MatchingCardAdapter adapter;
    CardMatchingViewModel cardMatchingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Initialize grid layout
        final RecyclerView recyclerView = findViewById(R.id.cardGrid);
        int numberOfColumns = 4;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        cardMatchingViewModel = ViewModelProviders.of(this).get(CardMatchingViewModel.class);

        // Set up observer for cards
        cardMatchingViewModel.getCardProducts().observe(this, new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Product> products) {

                adapter = new MatchingCardAdapter(products);
                adapter.setClickListener(GameActivity.this);
                recyclerView.setAdapter(adapter);
            }
        });

        // Set up observer for number of pairs found
        cardMatchingViewModel.getPairsFound().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                TextView numPairsFound = findViewById(R.id.numPairsFound);
                numPairsFound.setText(String.valueOf(integer));
            }
        });

        // Set up observer for number of pairs left
        cardMatchingViewModel.getPairsLeft().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                TextView numPairsLeft = findViewById(R.id.numPairsLeft);
                numPairsLeft.setText(String.valueOf(integer));
            }
        });
    }

    @Override
    public void onItemClick(View view, final int position) {

        if (cardMatchingViewModel.isOnSecondCardSelected()) {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    cardMatchingViewModel.selectCard(position);
                }
            }, 350);
        } else {
            cardMatchingViewModel.selectCard(position);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.action_reset:
                cardMatchingViewModel.restartCards();
                break;

            case R.id.action_help:
                break;
        }
        return true;
    }
}
