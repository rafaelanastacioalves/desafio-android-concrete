package com.example.rafaelanastacioalves.desafioandroid.listeners;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Adapted by rafaelanastacioalves on 25/05/17
 * From: <a href="https://gist.github.com/ssinss/e06f12ef66c51252563e">
 * https://gist.github.com/ssinss/e06f12ef66c51252563e</a>
 */
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    @SuppressWarnings("FieldCanBeLocal")
    private final int visibleThreshold = 8;
    private final LinearLayoutManager mLinearLayoutManager;
    private int previousTotal = 0;
    private boolean loading = true;
    private int current_page = 1;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = mLinearLayoutManager.getItemCount();
        int firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading
                && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            current_page++;
            onLoadMore(current_page);

            loading = true;
        }
    }

    public void setCurrentPage(int current_page) {
        this.current_page = current_page;
    }

    /**
     * Callback to Load More
     *
     * @param current_page The new current page that requires loading
     */
    public abstract void onLoadMore(int current_page);
}
