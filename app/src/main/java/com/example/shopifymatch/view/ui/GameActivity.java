package com.example.shopifymatch.view.ui;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

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

                if (integer != null && integer == 0) {
                    showWinnerDialog();
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, final int position) {
        cardMatchingViewModel.selectCard(position);
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
                showHelpDialog();
                break;
        }
        return true;
    }

    private void showWinnerDialog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_winner, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        dialogView.findViewById(R.id.playAgainBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardMatchingViewModel.restartCards();
                alertDialog.dismiss();
            }
        });

        dialogView.findViewById(R.id.homeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        alertDialog.show();
    }

    private void showHelpDialog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_help, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        dialogView.findViewById(R.id.closeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
}
