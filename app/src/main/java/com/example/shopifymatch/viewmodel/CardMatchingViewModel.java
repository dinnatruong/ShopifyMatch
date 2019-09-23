package com.example.shopifymatch.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Handler;
import android.util.Log;

import com.example.shopifymatch.data.model.Product;
import com.example.shopifymatch.data.repository.ProductRepository;

import java.util.ArrayList;

public class CardMatchingViewModel extends ViewModel {
    private ProductRepository productRepository;
    private ArrayList<Integer> selectedCards;
    private LiveData<ArrayList<Product>> productCards;
    private LiveData<Integer> pairsFound;
    private LiveData<Integer> pairsLeft;

    public CardMatchingViewModel() {
        productRepository = new ProductRepository();
        selectedCards = new ArrayList<>();
        pairsFound = new MutableLiveData<>();
        pairsLeft = new MutableLiveData<>();
        productCards = productRepository.getCardProducts();
        ((MutableLiveData<Integer>) pairsFound).setValue(0);
        ((MutableLiveData<Integer>) pairsLeft).setValue(10);
    }

    public LiveData<ArrayList<Product>> getCardProducts() {
        return productCards;
    }

    public LiveData<Integer> getPairsFound() {
        return pairsFound;
    }

    public LiveData<Integer> getPairsLeft() {
        return pairsLeft;
    }

    public void selectCard(int index) {
        ArrayList<Product> currentDeck = productCards.getValue();

        if (selectedCards.contains(index) || currentDeck.get(index).isFaceShown()) {
            return;
        }

        selectedCards.add(index);
        currentDeck.get(index).setFaceShown(true);
        ((MutableLiveData<ArrayList<Product>>) productCards).setValue(currentDeck);

        checkMatch();
        checkGameOver();
    }

    private void checkMatch() {
        if (selectedCards.size() == 2) {

            final ArrayList<Product> currentDeck = productCards.getValue();
            final int firstSelectedCardIndex = selectedCards.get(0);
            final int secondSelectedCardIndex = selectedCards.get(1);

            // Compare selected cards
            if (currentDeck.get(firstSelectedCardIndex).getId().equals(currentDeck.get(secondSelectedCardIndex).getId())) {

                // Increase number of pairs found
                if (pairsFound.getValue() != null) {
                    int currentPairsFound = pairsFound.getValue();
                    currentPairsFound++;
                    ((MutableLiveData<Integer>) pairsFound).setValue(currentPairsFound);
                }

                // Decrease number of pairs left
                if (pairsLeft.getValue() != null) {
                    int currentPairsLeft = pairsLeft.getValue();
                    currentPairsLeft--;
                    ((MutableLiveData<Integer>) pairsLeft).setValue(currentPairsLeft);
                }

            } else {

                // Flip cards if pairs do not match
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        currentDeck.get(firstSelectedCardIndex).setFaceShown(false);
                        currentDeck.get(secondSelectedCardIndex).setFaceShown(false);
                        ((MutableLiveData<ArrayList<Product>>) productCards).setValue(currentDeck);
                    }
                }, 400);
            }

            selectedCards.clear();
        }
    }

    private void checkGameOver() {
        if (pairsLeft.getValue() != null && pairsLeft.getValue() == 0) {
            Log.d("GAME OVER", "GAME OVER");
        }
    }

    public void restartCards() {
        // Get new deck of cards and reset scores
        productCards = productRepository.getCardProducts();
        ((MutableLiveData<Integer>) pairsFound).setValue(0);
        ((MutableLiveData<Integer>) pairsLeft).setValue(10);
    }
}
