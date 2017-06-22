package com.example.rafaelanastacioalves.desafioandroid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rafaelanastacioalves.desafioandroid.entities.Pull;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;


/**
 * Created by rafaelanastacioalves on 10/03/16.
 */
public class PullsListAdapter extends RecyclerViewListAdapter<RecyclerView.ViewHolder, Pull, RecyclerViewClickListener> {
    private final Context mContext;
    private RecyclerViewClickListener recyclerViewClickListener;

    public PullsListAdapter(Context context) { mContext = context; }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup viewGroup, int viewType) {
        return new PullViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pull_viewholder, viewGroup, false), recyclerViewClickListener);
    }


    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        Pull aPull = getItems().get(position);
        ((PullViewHolder) holder).bind(aPull,mContext);
    }

    public void setRecyclerViewClickListener(RecyclerViewClickListener aRVC) {
        this.recyclerViewClickListener = aRVC;
    }


}


class PullViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView pullTextViewDescription;
    TextView pullTextViewTitle;
    TextView pullTexViewtUserName;
    RecyclerViewClickListener aRecyclerViewListener;

    LinearLayout pullLinearLayoutContainer;

    CircularImageView circularImageView;

    public PullViewHolder(View itemView, RecyclerViewClickListener clickListener) {
        super(itemView);
        this.aRecyclerViewListener = clickListener;
        pullTextViewDescription = (TextView) itemView.findViewById(R.id.pull_text_view_description);
        pullTextViewTitle = (TextView) itemView.findViewById(R.id.pull_textview_title);
        pullTexViewtUserName = (TextView) itemView.findViewById(R.id.pull_textview_owner_username);
        pullLinearLayoutContainer = (LinearLayout) itemView.findViewById(R.id.pull_linear_layout_container);

        circularImageView = (CircularImageView) itemView.findViewById(R.id.repo_textview_owner_photo);

        pullLinearLayoutContainer.setOnClickListener(this);
    }

    public void bind(Pull aPull, Context context) {
        pullTextViewDescription.setText(aPull.getBody());
        pullLinearLayoutContainer.setContentDescription("Pull Request number " + (getAdapterPosition() + 1));

        pullTextViewTitle.setText(aPull.getTitle());
        pullTexViewtUserName.setText(aPull.getPullUser().getLogin());
        pullTexViewtUserName.setHint(aPull.getPullUser().getLogin()+getAdapterPosition());


        Picasso.with(context)
                .load(aPull.getPullUser().getAvatarUrl())
                .resize(50, 50)
                .placeholder(R.drawable.placeholder_user)
                .into(circularImageView);

        pullLinearLayoutContainer.setTag(aPull);

    }


    @Override
    public void onClick(View v) {
        aRecyclerViewListener.onClick(v, getAdapterPosition());

    }
}

