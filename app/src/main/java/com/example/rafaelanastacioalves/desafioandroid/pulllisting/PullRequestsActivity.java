package com.example.rafaelanastacioalves.desafioandroid.pulllisting;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.rafaelanastacioalves.desafioandroid.R;
import com.example.rafaelanastacioalves.desafioandroid.repolisting.RepoListActivity;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * An activity representing a single Repo detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RepoListActivity}.
 */
public class PullRequestsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pull_list_activity);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getIntent().getStringExtra(PullRequestsFragment.ARG_REPOSITORY));

        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Timber.i("PullRequestsFragment ARG CREATOR: " + getIntent().getStringExtra(PullRequestsFragment.ARG_CREATOR));
            Bundle arguments = new Bundle();
            arguments.putString(PullRequestsFragment.ARG_CREATOR,
                    getIntent().getStringExtra(PullRequestsFragment.ARG_CREATOR));

            Timber.i("PullRequestsFragment ARG REPOSITORY: " + getIntent().getStringExtra(PullRequestsFragment.ARG_REPOSITORY));
            arguments.putString(PullRequestsFragment.ARG_REPOSITORY,
                    getIntent().getStringExtra(PullRequestsFragment.ARG_REPOSITORY));
            PullRequestsFragment fragment = new PullRequestsFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.repo_detail_container, fragment)
                    .commit();
        }

    }

}
