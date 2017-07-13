package com.example.rafaelanastacioalves.desafioandroid.repolist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.rafaelanastacioalves.desafioandroid.R;
import com.example.rafaelanastacioalves.desafioandroid.RecyclerViewClickListener;
import com.example.rafaelanastacioalves.desafioandroid.entities.Repo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by rafaelanastacioalves on 24/05/17.
 */
public class RepoListAdapter extends RecyclerView.Adapter<RepoViewHolder> {
    private RecyclerViewClickListener recyclerViewClickListener;
    private List<Repo> items = new ArrayList<>();

    private Context mContext;

    public RepoListAdapter(Context context) {
        mContext = context;
    }



    public void setRecyclerViewClickListener(RecyclerViewClickListener aRVC) {
        this.recyclerViewClickListener = aRVC;
    }

    public List<Repo> getItems() {
        return this.items;
    }



    @Override
    public RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RepoViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repo_viewholder, parent, false), recyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(RepoViewHolder holder, int position) {
        Repo aRepoW = getItems().get(position);
        ((RepoViewHolder) holder).bind(aRepoW, mContext);
    }


    public void setItems(List<Repo> items){
        this.items = items;
        notifyDataSetChanged();


    }

    @Override
    public int getItemCount() {
        return getItems().size();
    }
}


