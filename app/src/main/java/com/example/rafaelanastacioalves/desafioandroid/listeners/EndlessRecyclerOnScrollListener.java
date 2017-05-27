package com.example.rafaelanastacioalves.desafioandroid.listeners;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Adapted by rafaelanastacioalves on 25/05/17
 * From: <a href="https://gist.github.com/ssinss/e06f12ef66c51252563e">
 *     https://gist.github.com/ssinss/e06f12ef66c51252563e</a>
 */
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    /**
     * A constructor so we can initialize with custom current page
     * @param linearLayoutManager
     * @param initialPage
     */
    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager, int initialPage) {
        this(linearLayoutManager);
        current_page = initialPage;
    }
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

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
     * @param current_page The new current page that requires loading
     */
    public abstract void onLoadMore(int current_page);
}
