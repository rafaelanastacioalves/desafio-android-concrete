package com.example.rafaelanastacioalves.desafioandroid.repolisting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.rafaelanastacioalves.desafioandroid.R;
import com.example.rafaelanastacioalves.desafioandroid.entities.Repo;
import com.example.rafaelanastacioalves.desafioandroid.listeners.EndlessRecyclerOnScrollListener;
import com.example.rafaelanastacioalves.desafioandroid.listeners.RecyclerViewClickListener;
import com.example.rafaelanastacioalves.desafioandroid.pulllisting.PullRequestsActivity;
import com.example.rafaelanastacioalves.desafioandroid.pulllisting.PullRequestsFragment;

import java.util.List;

import timber.log.Timber;

/**
 * An activity representing a list of Repos. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PullRequestsActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RepoListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Repo>>, RecyclerViewClickListener {

    private static final int REPOS_LOADER_ID = 1;
    private static final String PAGE_KEY = "PAGE_KEY";
    private static int lastFirstVisiblePosition;
    private final LoaderManager.LoaderCallbacks<List<Repo>> mCallback = RepoListActivity.this;
    @SuppressWarnings("FieldCanBeLocal")
    private final int repoListLoaderId = 10;
    private final RecyclerViewClickListener mClickListener = this;
    protected RepoListAdapter mRepoListAdapter;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag("LifeCycles");
        Timber.i("onCreate Activity");

        setContentView(R.layout.repo_list_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        mRecyclerView = (RecyclerView) findViewById(R.id.repo_list);
        setupRecyclerView(mRecyclerView);

        if (findViewById(R.id.repo_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        if (getSupportLoaderManager().getLoader(repoListLoaderId) == null) {
            getSupportLoaderManager().initLoader(repoListLoaderId, null, mCallback);
        } else {
            getSupportLoaderManager().restartLoader(repoListLoaderId, null, mCallback);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        lastFirstVisiblePosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        if (mRepoListAdapter == null) {
            mRepoListAdapter = new RepoListAdapter(this);
        }
        mRepoListAdapter.setRecyclerViewClickListener(mClickListener);
        recyclerView.setAdapter(mRepoListAdapter);


        mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(
                (LinearLayoutManager) recyclerView.getLayoutManager()
        ) {
            @Override
            public void onLoadMore(int currentPage) {
                Timber.i("onLoadMore: current page " + currentPage);
                //TODO
                Bundle bundle = new Bundle();
                bundle.putInt(PAGE_KEY, currentPage);
                Timber.i("Asking to restart loader...");
                getSupportLoaderManager().restartLoader(REPOS_LOADER_ID, bundle, mCallback);
            }
        };
        recyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
    }


    @Override
    public Loader<List<Repo>> onCreateLoader(int id, Bundle args) {
        Timber.i("onCreateLoader");
        if (args != null) {
            int page = args.getInt(PAGE_KEY, 1);
            return new ReposAsyncTaskLoader(this, page);

        } else {
            return new ReposAsyncTaskLoader(this);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPosition(lastFirstVisiblePosition);

    }

    @Override
    public void onLoadFinished(Loader<List<Repo>> loader, List<Repo> data) {

        if (loader instanceof ReposAsyncTaskLoader) {
            int current_page = ((ReposAsyncTaskLoader) loader).getPage();
            mEndlessRecyclerOnScrollListener.setCurrentPage(current_page);

            if (data == null) {
                //noinspection StatementWithEmptyBody
                if (current_page > 1) {
                    // we don't put anything into adapter
                    //TODO add any error managing for pagination loading


                } else {
                    // if we're first time loading data
                    mRepoListAdapter.setItems(null);
                }
            } else {
                // if data is different from null, we put it into loader
                mRepoListAdapter.setItems(data);
            }
        }


    }

    @Override
    public void onLoaderReset(Loader<List<Repo>> loader) {
        //Timber.i("onLoaderReset");
        // We do nothing here, as we don't want invalidate our data
    }

    @Override
    public void onClick(View view, int position) {
        Repo repo = mRepoListAdapter.getItems().get(position);

        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(PullRequestsFragment.ARG_CREATOR, repo.getOwner().getLogin());
            arguments.putString(PullRequestsFragment.ARG_REPOSITORY, repo.getName());
            PullRequestsFragment fragment = new PullRequestsFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.repo_detail_container, fragment)
                    .commit();
        } else {
            Intent i = new Intent(this, PullRequestsActivity.class);
            i.putExtra(PullRequestsFragment.ARG_CREATOR, repo.getOwner().getLogin());
            i.putExtra(PullRequestsFragment.ARG_REPOSITORY, repo.getName());
            startActivity(i);
        }


    }


    public RepoListAdapter getAdapter() {
        return mRepoListAdapter;
    }
}
