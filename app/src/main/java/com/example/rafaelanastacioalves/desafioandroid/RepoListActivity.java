package com.example.rafaelanastacioalves.desafioandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rafaelanastacioalves.desafioandroid.dummy.DummyContent;
import com.example.rafaelanastacioalves.desafioandroid.entities.Repo;
import com.example.rafaelanastacioalves.desafioandroid.entities.Repos;
import com.example.rafaelanastacioalves.desafioandroid.listeners.EndlessRecyclerOnScrollListener;
import com.example.rafaelanastacioalves.desafioandroid.retrofit.GithubClient;
import com.example.rafaelanastacioalves.desafioandroid.retrofit.ServiceGenerator;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Response;
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
    private LoaderManager.LoaderCallbacks<List<Repo>> callback = RepoListActivity.this;
    private final int repoListLoaderId = 10;
    private RecyclerView mRecyclerView;
    RepoListAdapter mRepoListAdapter;
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    private RecyclerViewClickListener clickListener = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag("LifeCycles");
        Timber.i("onCreate Activity");

        setContentView(R.layout.activity_repo_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());



        mRecyclerView = (RecyclerView) findViewById(R.id.repo_list);
        setupRecyclerView((RecyclerView) mRecyclerView);

        if (findViewById(R.id.repo_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        getSupportLoaderManager().initLoader(repoListLoaderId, null, callback);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        if (mRepoListAdapter == null) {
            mRepoListAdapter = new RepoListAdapter(this);
        }
        mRepoListAdapter.setRecyclerViewClickListener(clickListener);
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
                getSupportLoaderManager().restartLoader(REPOS_LOADER_ID, bundle, callback);
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
            Timber.i("onCreateLooader: isLoadingMore: " + isLoadingMore);
            return new ReposAsyncTaskLoader(this, page, isLoadingMore);

        } else {
            return new ReposAsyncTaskLoader(this);
        }


    }

    @Override
    public void onLoadFinished(Loader<List<Repo>> loader, List<Repo> data) {
//        DummyContent.populateWithStringArray(data);
//        setupRecyclerView((RecyclerView) mRecyclerView);
        //TODO setar o adapter de forma que sete uma vez no começo e aqui só atualize os itens e
        // dê um notify
        if (loader instanceof ReposAsyncTaskLoader) {
            int current_page = ((ReposAsyncTaskLoader) loader).getPage();
            mEndlessRecyclerOnScrollListener.setCurrentPage(current_page);
            mRepoListAdapter.setItems(data);

        }


    }

    public void resetData(){

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


    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.repo_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(RepoDetailFragment.ARG_CREATOR, holder.mItem.id);
                        RepoDetailFragment fragment = new RepoDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.repo_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, RepoDetailActivity.class);
                        intent.putExtra(RepoDetailFragment.ARG_CREATOR, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

    private static class ReposAsyncTaskLoader extends AsyncTaskLoader<List<Repo>> {
        private static List<Repo> mRepoList;
        private static int page = 1;
        private static AtomicBoolean LOAD_MORE = new AtomicBoolean(false);

        public ReposAsyncTaskLoader(Context context) {
            super(context);
        }

        /**
         * @return the current page the loader is able to load
         */
        public int getPage() {
            return page;
        }

        /**
         * Constructor in case we need load more. So we specify the status and new page to load
         *
         * @param context
         * @param page
         * @param isLoadingMore
         */
        public ReposAsyncTaskLoader(Context context, int page, Boolean isLoadingMore) {
            super(context);
            Timber.i("new AsyncTaskLoader + loadingMore: " + isLoadingMore);
            this.page = page;
            this.LOAD_MORE.set(isLoadingMore);
        }


        @Override
        protected void onStartLoading() {

            Timber.i("onStartLoading");
            if (mRepoList == null) {
                if (LOAD_MORE.get()) {
                    Timber.w("LOAD_MORE está como true! Não deveria com lista nula...");
                }
                // if we have no list, we start from page 1
                page = 1;
                forceLoad();
            } else if (LOAD_MORE.get()) {
                Timber.i("page: " + page);
                forceLoad();
            } else {
                Timber.i("we already have the result!");
                deliverResult(mRepoList);
            }

            super.onStartLoading();
        }

        @Override
        protected void onStopLoading() {
            super.onStopLoading();
            Timber.i("onStopLoading");
            // if we were loading more, we now set status as false (not loading more)
            if (LOAD_MORE.get()){
                LOAD_MORE.set(false);
            }
        }

        @Override
        public List<Repo> loadInBackground() {
            Timber.i("ReposAsyncTaskLoader loadInBackground");
            GithubClient githubClient = ServiceGenerator.createService(GithubClient.class);
            Call<Repos> call = githubClient.getRepos("language:Java",
                    "starts",
                    page
            );

            try {
                Response<Repos> response = call.execute();

                if (response.isSuccessful()) {
                    Timber.i("response Successful");
                    Repos repos = response.body();
                    mRepoList = repos.getRepoList();
                    return mRepoList;
                } else {
                    Timber.e(response.message(), null);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return mRepoList;
        }
    }
}
