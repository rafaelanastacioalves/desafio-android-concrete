package com.example.rafaelanastacioalves.desafioandroid;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.rafaelanastacioalves.desafioandroid.repolist.RepoListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by rafaelanastacioalves on 6/6/17.
 */


@RunWith(AndroidJUnit4.class)
public class ScreenTransitionTest {
    private final int POSITION = 1;

    @Rule
    public ActivityTestRule<RepoListActivity> mRepoListTestRule = new ActivityTestRule<RepoListActivity>(RepoListActivity.class);

    @Test
    public void clickAnyRepoListItemShowsPullList(){
//        onView(ViewMatchers.withId(R.id.repo_list)).perform(RecyclerViewActions.actionOnItemAtPosition(POSITION,click()));
//        onView(ViewMatchers.withId(R.id.pulls_list_recycler_view));
    }
}
