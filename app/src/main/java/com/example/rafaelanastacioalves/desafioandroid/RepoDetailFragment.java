package com.example.rafaelanastacioalves.desafioandroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafaelanastacioalves.desafioandroid.entities.Pull;
import com.example.rafaelanastacioalves.desafioandroid.retrofit.GithubClient;
import com.example.rafaelanastacioalves.desafioandroid.retrofit.ServiceGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * A fragment representing a single Repo detail screen.
 * This fragment is either contained in a {@link RepoListActivity}
 * in two-pane mode (on tablets) or a {@link RepoDetailActivity}
 * on handsets.
 */
public class RepoDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Pull>>, RecyclerViewClickListener  {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_CREATOR = "creator_arg";
    public static final String ARG_REPOSITORY = "repository_arg";
    private static final int PULL_LIST_LOADER = 11;

    /**
     * The dummy content this fragment is presenting.
     */
    String mCreatorString;
    public static final String CREATOR_KEY_LOADER = "creator_key_loader";

    String mRepositoryString;
    public static final String REPOSITORY_KEY_LOADER = "repository_key_loader";


    private LoaderManager.LoaderCallbacks<List<Pull>> callback = RepoDetailFragment.this;
    PullsListAdapter mPullsListAdapter;


    @BindView(R.id.pulls_list_recycler_view)
    RecyclerView mPullsListRecyclerView;

    private RecyclerViewClickListener clickListener = this;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RepoDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the dummy content specified by the fragment
        // arguments. In a real-world scenario, use a Loader
        // to load content from a content provider.
        mCreatorString = getArguments().getString(ARG_CREATOR);
        mRepositoryString = getArguments().getString(ARG_REPOSITORY);

        Bundle bundle = new Bundle();
        bundle.putString(CREATOR_KEY_LOADER,mCreatorString);
        bundle.putString(REPOSITORY_KEY_LOADER,mRepositoryString);

        getLoaderManager().initLoader(PULL_LIST_LOADER, bundle, this);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pulls_list_fragment, container, false);
        ButterKnife.bind(this,rootView);

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
        return new PullListAsyncTaskLoader(getContext(), repository,creator);
    }

    @Override
    public void onLoadFinished(Loader<List<Pull>> loader, List<Pull> data) {
        if(loader instanceof  PullListAsyncTaskLoader) {
            mPullsListAdapter.setItems(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Pull>> loader) {
        Timber.i("onLoaderReset");

    }

    @Override
    public void onClick(View view, int position) {

        Pull aPull = (Pull)view.getTag();
        Timber.i("Url: " +  Uri.parse(aPull.getPullUrl()) );
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(aPull.getPullUrl()));
        startActivity(browserIntent);
    }

    private static class PullListAsyncTaskLoader extends AsyncTaskLoader<List<Pull>> {
        private static List<Pull> mPullList;

        private static String mRepository;
        private static String mCreator;


        public PullListAsyncTaskLoader(Context context, String repository, String creator) {
            super(context);
            mRepository = repository;
            mCreator = creator                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        ;

        }


        @Override
        protected void onStartLoading() {

            Timber.i("onStartLoading");

            // we reset every time we start loading. Caching logic decisions later..
            mPullList = null;
            forceLoad();
            super.onStartLoading();
        }

        @Override
        public List<Pull> loadInBackground() {
            GithubClient githubClient = ServiceGenerator.createService(GithubClient.class);
            Call<ArrayList<Pull>> call = githubClient.getPulls(mCreator, mRepository);

            try {
                Response<ArrayList<Pull>> response = call.execute();

                if (response.isSuccessful()) {
                    Timber.i("response Successful");
                    ArrayList<Pull> pulls = response.body();
                    mPullList = pulls;
                    return mPullList;
                } else {
                    Timber.e(response.message(), null);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return mPullList;
        }
    }
}
