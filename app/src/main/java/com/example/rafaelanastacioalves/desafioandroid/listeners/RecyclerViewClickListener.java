package com.example.rafaelanastacioalves.desafioandroid.listeners;

import android.view.View;

/**
 * Created by rafaelanastacioalves on 10/03/16.
 */
public interface RecyclerViewClickListener {

    /**
     * Callback method to be invoked when a item in a
     * RecyclerView is clicked
     *
     * @param view     The view within the RecyclerView.Adapter
     * @param position The position of the view in the adapter
     */
    public void onClick(View view, int position);
}