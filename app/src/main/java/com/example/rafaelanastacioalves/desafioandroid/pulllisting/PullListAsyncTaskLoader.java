package com.example.rafaelanastacioalves.desafioandroid.pulllisting;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.rafaelanastacioalves.desafioandroid.entities.Pull;
import com.example.rafaelanastacioalves.desafioandroid.retrofit.GithubClient;
import com.example.rafaelanastacioalves.desafioandroid.retrofit.ServiceGenerator;
import com.orhanobut.hawk.Hawk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by rafaelanastacioalves on 12/07/17.
 */

@SuppressWarnings("DefaultFileTemplate")
class PullListAsyncTaskLoader extends AsyncTaskLoader<List<Pull>> {
    private static final long mExpirationTimeLong = 1000 * 30; // in milliseconds
    private static String mRepository;
    private static String mCreator;
    private final String mPersistenceKey;
    private final String mLastUpdatedForPullRequestKey;
    private List<Pull> mPullList;


    public PullListAsyncTaskLoader(Context context, String repository, String creator) {
        super(context);
        mRepository = repository;
        mCreator = creator;
        mPersistenceKey = mCreator + "-" + mRepository;
        mLastUpdatedForPullRequestKey = mCreator + "-" + mRepository + "-" + "mLastUpdate";

    }


    @Override
    protected void onStartLoading() {

        Timber.i("onStartLoading");

        mPullList = Hawk.get(mPersistenceKey);
        Date mLastUpdate = Hawk.get(mLastUpdatedForPullRequestKey);

        long timeElapsed = Utils.getTimeDeltaUntilNowFrom(mLastUpdate);

        // if we don't have any list or if we passed the expiration time
        if (mPullList == null || timeElapsed > mExpirationTimeLong) {
            forceLoad();
        } else {
            Timber.i("we already have the result!");
            deliverResult(mPullList);
        }
    }


    @Override
    public List<Pull> loadInBackground() {
        GithubClient githubClient = ServiceGenerator.createService(GithubClient.class);
        Call<ArrayList<Pull>> call = githubClient.getPulls(mCreator, mRepository);

        try {
            Response<ArrayList<Pull>> response = call.execute();

            if (response.isSuccessful()) {
                Timber.i("response Successful");
                mPullList = response.body();
                Hawk.put(mPersistenceKey, mPullList);
                Hawk.put(mLastUpdatedForPullRequestKey, new Date());
                return mPullList;
            } else {
                //TODO add more error management
                Timber.e(response.message());
                // we try to show the last result
                mPullList = Hawk.get(mPersistenceKey);
                return mPullList;

            }

        } catch (IOException e) {
            //TODO add more error management
            e.printStackTrace();
        }

        return mPullList;
    }
}
