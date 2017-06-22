package com.example.rafaelanastacioalves.desafioandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.rafaelanastacioalves.desafioandroid.constants.Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by rafaelanastacioalves on 6/6/17.
 */


@RunWith(AndroidJUnit4.class)
public class RepoMockedListTest {
    private String fileNameRepoListOKRespone = "repo_list_ok_response.json";
    private String fileNamePullsListOKRespone = "pulls_list_ok_response.json";
    private final int POSITION = 1;

    @Rule
    public ActivityTestRule<RepoListActivity> mRepoListTestRule = new ActivityTestRule<RepoListActivity>(RepoListActivity.class, true, false);

    private MockWebServer server;


    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
        InstrumentationRegistry.registerInstance(InstrumentationRegistry.getInstrumentation(),new Bundle());
        Constants.API_BASE_URL = server.url("/").toString();

    }

//    @Test
//    public void clickAnyRepoListItemShowsPullList(){
//        onView(withId(R.id.repo_list)).perform(RecyclerViewActions.actionOnItemAtPosition(POSITION,click()));
//        onView(withId(R.id.pulls_list_recycler_view));
//    }

    @Test
    public void JSONValuesFulfill() throws IOException {
        server.enqueue(new MockResponse()
        .setResponseCode(200)
        .setBody(RestServiceTestHelper.getStringFromFile(
                InstrumentationRegistry.getInstrumentation().getContext()
        ,fileNameRepoListOKRespone)
        )
        );


        Intent intent = new Intent();
        mRepoListTestRule.launchActivity(intent);

        onView(allOf(withId(R.id.repo_text_view_title), withText("elasticsearch"))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.repo_text_view_description), withText("Open Source, Distributed, RESTful Search Engine"))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.repo_textview_number_forks), withText("8177"))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.repo_textview_number_stars), withText("23226"))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.repo_textview_owner_photo),withContentDescription("elasticsearch"))).check(matches(isDisplayed()));


    }

    @Test
    public void screenTransition() throws IOException {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(RestServiceTestHelper.getStringFromFile(
                        InstrumentationRegistry.getInstrumentation().getContext()
                        ,fileNameRepoListOKRespone)
                )
        );

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(RestServiceTestHelper.getStringFromFile(
                        InstrumentationRegistry.getInstrumentation().getContext()
                        ,fileNamePullsListOKRespone)
                )
        );


        Intent intent = new Intent();
        mRepoListTestRule.launchActivity(intent);

        onView(allOf(withId(R.id.repo_linear_layout_container), withContentDescription("elasticsearch")))
                .perform(click());
        onView(withId(R.id.pull_list_fragment)).check(matches(isDisplayed()));
    }


    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

}
