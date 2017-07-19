package com.example.rafaelanastacioalves.desafioandroid.pulllisting;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rafaelanastacioalves.desafioandroid.R;
import com.example.rafaelanastacioalves.desafioandroid.entities.Pull;
import com.example.rafaelanastacioalves.desafioandroid.listeners.RecyclerViewClickListener;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by rafaelanastacioalves on 19/07/17.
 */

class PullViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView pullTextViewDescription;
    private final TextView pullTextViewTitle;
    private final TextView pullTexViewUserName;
    private final RecyclerViewClickListener aRecyclerViewListener;

    private final LinearLayout pullLinearLayoutContainer;

    private final CircularImageView circularImageView;

    public PullViewHolder(View itemView, RecyclerViewClickListener clickListener) {
        super(itemView);
        this.aRecyclerViewListener = clickListener;
        pullTextViewDescription = (TextView) itemView.findViewById(R.id.pull_text_view_description);
        pullTextViewTitle = (TextView) itemView.findViewById(R.id.pull_textview_title);
        pullTexViewUserName = (TextView) itemView.findViewById(R.id.pull_textview_owner_username);
        pullLinearLayoutContainer = (LinearLayout) itemView.findViewById(R.id.pull_linear_layout_container);

        circularImageView = (CircularImageView) itemView.findViewById(R.id.repo_textview_owner_photo);

        pullLinearLayoutContainer.setOnClickListener(this);
    }

    public void bind(Pull aPull, Context context) {
        pullTextViewDescription.setText(aPull.getBody());
        pullLinearLayoutContainer.setContentDescription("Pull Request number " + (getAdapterPosition() + 1));

        pullTextViewTitle.setText(aPull.getTitle());
        pullTexViewUserName.setText(aPull.getPullUser().getLogin());
        pullTexViewUserName.setHint(aPull.getPullUser().getLogin() + getAdapterPosition());


        Picasso.with(context)
                .load(aPull.getPullUser().getAvatarUrl())
                .resize(150, 150)
                .centerInside()
                .placeholder(R.drawable.placeholder_user)
                .into(circularImageView);

        pullLinearLayoutContainer.setTag(aPull);

    }


    @Override
    public void onClick(View v) {
        aRecyclerViewListener.onClick(v, getAdapterPosition());

    }
}


