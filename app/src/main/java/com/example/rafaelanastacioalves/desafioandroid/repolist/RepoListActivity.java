package com.example.rafaelanastacioalves.desafioandroid.repolist;

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
import com.example.rafaelanastacioalves.desafioandroid.RecyclerViewClickListener;
import com.example.rafaelanastacioalves.desafioandroid.RecyclerViewListAdapter;
import com.example.rafaelanastacioalves.desafioandroid.pulllist.RepoDetailActivity;
import com.example.rafaelanastacioalves.desafioandroid.pulllist.RepoDetailFragment;
import com.example.rafaelanastacioalves.desafioandroid.entities.Repo;
import com.example.rafaelanastacioalves.desafioandroid.listeners.EndlessRecyclerOnScrollListener;

import java.util.List;

import timber.log.Timber;

/**
 * An activity representing a list of Repos. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RepoDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RepoListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Repo>>, RecyclerViewClickListener {

    private static final int REPOS_LOADER_ID = 1;
    private static final String PAGE_KEY = "PAGE_KEY";
    private static final String LOAD_MORE_KEY = "LOAD_MORE_KEY";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private final LoaderManager.LoaderCallbacks<List<Repo>> mCallback = RepoListActivity.this;
    @SuppressWarnings("FieldCanBeLocal")
    private final int repoListLoaderId = 10;
    protected RepoListAdapter mRepoListAdapter;
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    private final RecyclerViewClickListener mClickListener = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag("LifeCycles");
        Timber.i("onCreate Activity");

        setContentView(R.layout.activity_repo_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.repo_list);
        setupRecyclerView(mRecyclerView);

        if (findViewById(R.id.repo_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        getSupportLoaderManager().initLoader(repoListLoaderId, null, mCallback);
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
                bundle.putBoolean(LOAD_MORE_KEY, true);
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
            Boolean isLoadingMore = args.getBoolean(LOAD_MORE_KEY, false);
            Timber.i("onCreateLoader: isLoadingMore: " + isLoadingMore);
            return new ReposAsyncTaskLoader(this, page, isLoadingMore);

        } else {
            return new ReposAsyncTaskLoader(this);
        }


    }

    @Override
    public void onLoadFinished(Loader<List<Repo>> loader, List<Repo> data) {

        if (loader instanceof ReposAsyncTaskLoader) {
            int current_page = ((ReposAsyncTaskLoader) loader).getPage();
            mEndlessRecyclerOnScrollListener.setCurrentPage(current_page);

            if (data == null ){
                //noinspection StatementWithEmptyBody
                if (current_page > 1){
                    // we don't put anything into adapter
                    //TODO add any error managing for pagination loading


                }else{
                    // if we're first time loading data
                    mRepoListAdapter.setItems(null);
                }
            }else{
                // if data is different from null, we put it into loader
                mRepoListAdapter.setItems(data);
            }
        }


    }

    @Override
    public void onLoaderReset(Loader<List<Repo>> loader) {
        Timber.i("onLoaderReset");
        mRepoListAdapter.setItems(null);

    }

    @Override
    public void onClick(View view, int position) {
        Repo repo = mRepoListAdapter.getItems().get(position);

        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(RepoDetailFragment.ARG_CREATOR, repo.getOwner().getLogin());
            arguments.putString(RepoDetailFragment.ARG_REPOSITORY, repo.getName());
            RepoDetailFragment fragment = new RepoDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.repo_detail_container, fragment)
                    .commit();
        } else {
            Intent i = new Intent(this,RepoDetailActivity.class);
            i.putExtra(RepoDetailFragment.ARG_CREATOR, repo.getOwner().getLogin() );
            i.putExtra(RepoDetailFragment.ARG_REPOSITORY, repo.getName() );
            startActivity(i);
        }


    }


    public RecyclerViewListAdapter getAdapter() {
        return mRepoListAdapter;
    }
}
