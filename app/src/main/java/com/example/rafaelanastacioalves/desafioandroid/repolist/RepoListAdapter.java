package com.example.rafaelanastacioalves.desafioandroid.repolist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.rafaelanastacioalves.desafioandroid.R;
import com.example.rafaelanastacioalves.desafioandroid.RecyclerViewClickListener;
import com.example.rafaelanastacioalves.desafioandroid.RecyclerViewListAdapter;
import com.example.rafaelanastacioalves.desafioandroid.entities.Repo;


/**
 * Created by rafaelanastacioalves on 24/05/17.
 */
public class RepoListAdapter extends RecyclerViewListAdapter<RecyclerView.ViewHolder, Repo, RecyclerViewClickListener> {
    private RecyclerViewClickListener recyclerViewClickListener;
    private Context mContext;

    public RepoListAdapter(Context context) {
        mContext = context;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup viewGroup, int viewType) {
        return new RepoViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.repo_viewholder, viewGroup, false), recyclerViewClickListener);
    }


    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        Repo aRepoW = getItems().get(position);
        ((RepoViewHolder) holder).bind(aRepoW, mContext);
    }

    public void setRecyclerViewClickListener(RecyclerViewClickListener aRVC) {
        this.recyclerViewClickListener = aRVC;
    }


}


