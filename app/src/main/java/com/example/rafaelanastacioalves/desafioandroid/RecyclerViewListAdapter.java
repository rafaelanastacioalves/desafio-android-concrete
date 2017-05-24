package com.example.rafaelanastacioalves.desafioandroid;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public abstract class RecyclerViewListAdapter<V, R, R1> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<R> items = new ArrayList<>();
    private RecyclerViewClickListener clickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return onCreateItemViewHolder(viewGroup, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindItemViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return getItems().size();
    }

    protected abstract RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup viewGroup, int viewType);

    protected abstract void onBindItemViewHolder(final RecyclerView.ViewHolder holder, int position);

    private boolean hasItems() {
        return items.size() > 0;
    }

    public List<R> getItems() {
        return items;
    }

    public void setItems(List<R> items) {
        for(int i=0; i<items.size(); i++){
            this.items.add(items.get(i));
        }
    }

    public RecyclerViewClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(RecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
