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

    // these are the most important variable that reflects what we have as "cache"
    private static List<Repo> mRepoList;
    private static Integer loadedPage;

    private int askedPage;

    public ReposAsyncTaskLoader(Context context) {
        //TODO add askedPage = 1 here?
        super(context);
    }

    /**
     * Constructor in case we need load more. So we specify the status and new loadedPage to load
     *
     * @param context       The context
     * @param page          The loadedPage the loader should be counting at
     * @param isLoadingMore Flag to inform that it is supposed to load more on loading callback
     */
    public ReposAsyncTaskLoader(Context context, int page, Boolean isLoadingMore) {
        super(context);
        Timber.i("new AsyncTaskLoader + loadingMore: " + isLoadingMore);
        Timber.i("askedPage seted : " + page);
        askedPage = page;
        LOAD_MORE.set(isLoadingMore);
    }

    /**
     * @return the current loadedPage the loader is able to load
     */
    public int getPage() {
        return loadedPage;
    }

    @Override
    protected void onStartLoading() {
        //TODO Adapt so we take into consideration when we have list AND loaded page together and
        // what position was started externally (less then the loaded? More?)
        Timber.i("onStartLoading");
        if (mRepoList == null || loadedPage == null) {
            if (LOAD_MORE.get()) {
                Timber.w("LOAD_MORE is set true! Shouldn't happen with null list...");
            }

            // if we have no list, we reset state
            mRepoList = null;
            loadedPage = 0;
            askedPage =1;

            // and ask to load
            forceLoad();

        } else if (loadedPage<askedPage) {
            // if we still have pages to load
            Timber.i("loadedPage: " + loadedPage);
            forceLoad();
        } else {
            if(loadedPage>askedPage){
                Timber.w("loaded page (" + loadedPage + ") higher than the asked page (" +askedPage + ")!" );
            }
            Timber.i("we already have the result!");
            deliverResult(mRepoList);

        }

    }


    @Override
    public List<Repo> loadInBackground() {
        Timber.i("ReposAsyncTaskLoader loadInBackground");


        for (int page = loadedPage+1; page <= askedPage; page++){


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

                    loadedPage = page;

                } else {
                    //TODO add error management here
                    Timber.e(response.message());
                }

            } catch (IOException e) {
                //TODO add error management here
                e.printStackTrace();
            }





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
