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

import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by rafaelanastacioalves on 08/07/17.
 * This loader will be responsible for loading all the list. He will have to return the list until
 * the page it is asked. If it is already loaded, or if the page loaded is higher than the page
 * asked, the loader returns more than asked - until the page he has.
 */
public class ReposAsyncTaskLoader extends AsyncTaskLoader<List<Repo>> {

    // these are the most important variable that reflects what we have as "cache"
    private static List<Repo> mRepoList;
    private static Integer loadedPage;

    private int askedPage;

    public ReposAsyncTaskLoader(Context context) {
        //TODO add askedPage = 1 here?
        super(context);
    }

    /**
     * Constructor in case we need load more. The loader will load until the page specified.
     *  @param context       The context
     * @param page          The page until which we want the loader to load.
     */
    public ReposAsyncTaskLoader(Context context, int page) {
        super(context);
        Timber.i("askedPage seted : " + page);
        askedPage = page;
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


}
