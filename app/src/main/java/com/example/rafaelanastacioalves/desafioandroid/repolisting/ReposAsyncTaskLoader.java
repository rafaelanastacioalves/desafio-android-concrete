package com.example.rafaelanastacioalves.desafioandroid.repolisting;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.rafaelanastacioalves.desafioandroid.entities.Repo;
import com.example.rafaelanastacioalves.desafioandroid.entities.Repos;
import com.example.rafaelanastacioalves.desafioandroid.retrofit.GithubClient;
import com.example.rafaelanastacioalves.desafioandroid.retrofit.ServiceGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by rafaelanastacioalves on 08/07/17.
 */
public class ReposAsyncTaskLoader extends AsyncTaskLoader<List<Repo>> {
    private static final AtomicBoolean LOAD_MORE = new AtomicBoolean(false);
    private static List<Repo> mRepoList;
    private static int page = 1;

    public ReposAsyncTaskLoader(Context context) {
        super(context);
    }

    /**
     * Constructor in case we need load more. So we specify the status and new page to load
     *
     * @param context       The context
     * @param page          The page the loader should be counting at
     * @param isLoadingMore Flag to inform that it is supposed to load more on loading callback
     */
    public ReposAsyncTaskLoader(Context context, int page, Boolean isLoadingMore) {
        super(context);
        Timber.i("new AsyncTaskLoader + loadingMore: " + isLoadingMore);
        ReposAsyncTaskLoader.page = page;
        LOAD_MORE.set(isLoadingMore);
    }

    /**
     * @return the current page the loader is able to load
     */
    public int getPage() {
        return page;
    }

    @Override
    protected void onStartLoading() {

        Timber.i("onStartLoading");
        if (mRepoList == null) {
            if (LOAD_MORE.get()) {
                Timber.w("LOAD_MORE is set true! Shouldn't happen with null list...");
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
                if (mRepoList == null) {
                    mRepoList = new ArrayList<Repo>(repos.getRepoList());
                } else {
                    mRepoList.addAll(repos.getRepoList());
                }
                return mRepoList;
            } else {
                Timber.e(response.message());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return mRepoList;
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        Timber.i("onStopLoading");
        // if we were loading more, we now set status as false (not loading more)
        if (LOAD_MORE.get()) {
            LOAD_MORE.set(false);
        }
    }
}
