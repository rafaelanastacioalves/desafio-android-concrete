package com.example.rafaelanastacioalves.desafioandroid.pulllist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafaelanastacioalves.desafioandroid.R;
import com.example.rafaelanastacioalves.desafioandroid.RecyclerViewClickListener;
import com.example.rafaelanastacioalves.desafioandroid.entities.Pull;
import com.example.rafaelanastacioalves.desafioandroid.repolist.RepoListActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * A fragment representing a single Repo detail screen.
 * This fragment is either contained in a {@link RepoListActivity}
 * in two-pane mode (on tablets) or a {@link RepoDetailActivity}
 * on handsets.
 */
public class RepoDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Pull>>, RecyclerViewClickListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_CREATOR = "creator_arg";
    public static final String ARG_REPOSITORY = "repository_arg";
    private static final int PULL_LIST_LOADER = 11;

    private static final String CREATOR_KEY_LOADER = "creator_key_loader";

    private static final String REPOSITORY_KEY_LOADER = "repository_key_loader";
    private final RecyclerViewClickListener clickListener = this;
    @BindView(R.id.pulls_list_recycler_view)
    RecyclerView mPullsListRecyclerView;
    private PullsListAdapter mPullsListAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RepoDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String mCreatorString = getArguments().getString(ARG_CREATOR);
        String mRepositoryString = getArguments().getString(ARG_REPOSITORY);

        Bundle bundle = new Bundle();
        bundle.putString(CREATOR_KEY_LOADER, mCreatorString);
        bundle.putString(REPOSITORY_KEY_LOADER, mRepositoryString);

        getLoaderManager().initLoader(PULL_LIST_LOADER, bundle, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pulls_list_fragment, container, false);
        ButterKnife.bind(this, rootView);

        setupRecyclerView(mPullsListRecyclerView);
        return rootView;
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        if (mPullsListAdapter == null) {
            mPullsListAdapter = new PullsListAdapter(getContext());
        }
        mPullsListAdapter.setRecyclerViewClickListener(clickListener);
        recyclerView.setAdapter(mPullsListAdapter);
    }


    @Override
    public Loader<List<Pull>> onCreateLoader(int id, Bundle args) {
        String creator = args.getString(CREATOR_KEY_LOADER);
        String repository = args.getString(REPOSITORY_KEY_LOADER);
        return new PullListAsyncTaskLoader(getContext(), repository, creator);
    }

    @Override
    public void onLoadFinished(Loader<List<Pull>> loader, List<Pull> data) {
        if (loader instanceof PullListAsyncTaskLoader) {
            if (data != null) {
                mPullsListAdapter.setItems(data);
            } else {
                mPullsListAdapter.setItems(null);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Pull>> loader) {
        Timber.i("onLoaderReset");

    }

    @Override
    public void onClick(View view, int position) {

        Pull aPull = (Pull) view.getTag();
        Timber.i("Url: " + Uri.parse(aPull.getPullUrl()));
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(aPull.getPullUrl()));
        startActivity(browserIntent);
    }

}
