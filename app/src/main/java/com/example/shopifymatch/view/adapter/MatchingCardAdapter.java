package com.example.shopifymatch.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.shopifymatch.R;
import com.example.shopifymatch.data.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MatchingCardAdapter extends RecyclerView.Adapter<MatchingCardAdapter.ViewHolder> {

    private ArrayList<Product> products;
    private ItemClickListener mClickListener;

    public MatchingCardAdapter(ArrayList<Product> products) {
        this.products = products;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.matching_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(products.get(position).getImage().getSrc()).into(holder.frontCard);

        if (products.get(position).isFaceShown()) {
            holder.frontCard.setVisibility(View.VISIBLE);
            holder.backCard.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView frontCard;
        ImageView backCard;

        ViewHolder(View itemView) {
            super(itemView);
            frontCard = itemView.findViewById(R.id.cardFront);
            backCard = itemView.findViewById(R.id.cardBack);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            // Flip card to reveal front
            frontCard.setVisibility(View.VISIBLE);
            backCard.setVisibility(View.GONE);

            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}