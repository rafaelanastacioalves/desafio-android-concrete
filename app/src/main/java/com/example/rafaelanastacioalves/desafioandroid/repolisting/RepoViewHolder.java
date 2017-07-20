package com.example.rafaelanastacioalves.desafioandroid.repolisting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rafaelanastacioalves.desafioandroid.R;
import com.example.rafaelanastacioalves.desafioandroid.entities.Repo;
import com.example.rafaelanastacioalves.desafioandroid.listeners.RecyclerViewClickListener;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

public class RepoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    LinearLayout container_linear_layout;
    RecyclerViewClickListener aRecyclerViewListener;


    TextView title_text_view;
    TextView description_text_view;
    TextView number_forks_text_view;
    TextView number_stars_text_view;
    TextView owner_name_text_view;


    CircularImageView repo_owner_imageview;


    public RepoViewHolder(View itemView, RecyclerViewClickListener clickListener) {
        super(itemView);
        this.aRecyclerViewListener = clickListener;
        container_linear_layout = (LinearLayout) itemView.findViewById(R.id.repo_linear_layout_container);

        title_text_view = (TextView) itemView.findViewById(R.id.repo_text_view_title);
        description_text_view = (TextView) itemView.findViewById(R.id.repo_text_view_description);
        owner_name_text_view = (TextView) itemView.findViewById(R.id.repo_textview_owner_name);

        number_stars_text_view = (TextView) itemView.findViewById(R.id.repo_textview_number_stars);
        number_forks_text_view = (TextView) itemView.findViewById(R.id.repo_textview_number_forks);


        repo_owner_imageview = (CircularImageView) itemView.findViewById(R.id.repo_textview_owner_photo);

        container_linear_layout.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    public void bind(Repo aRepo, Context context) {
        title_text_view.setText(aRepo.getName());
        title_text_view.setTag(aRepo);

        description_text_view.setText(aRepo.getDescription());

        number_forks_text_view.setText(Integer.toString(aRepo.getForks()));

        number_stars_text_view.setText(Integer.toString(aRepo.getStargazersCount()));

        owner_name_text_view.setText(aRepo.getOwner().getLogin());

        Picasso.with(context)
                .load(aRepo.getOwner().getAvatarUrl())
                .resize(150, 150)
                .centerInside()
                .placeholder(R.drawable.placeholder_user)
                .into(repo_owner_imageview);
        repo_owner_imageview.setContentDescription(aRepo.getName());


        container_linear_layout.setTag(aRepo);
        container_linear_layout.setContentDescription(aRepo.getName());

    }

    @Override
    public void onClick(View v) {
        aRecyclerViewListener.onClick(v, getAdapterPosition());

    }
}
