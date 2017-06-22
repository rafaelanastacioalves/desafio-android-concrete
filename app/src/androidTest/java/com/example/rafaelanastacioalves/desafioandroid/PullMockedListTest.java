package com.example.rafaelanastacioalves.desafioandroid;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
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
import timber.log.Timber;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

/**
 * Created by rafaelanastacioalves on 6/6/17.
 */


@RunWith(AndroidJUnit4.class)
public class PullMockedListTest {
    private String fileNameRepoListOKRespone = "repo_list_ok_response.json";
    private String fileNamePullsListOKRespone = "pulls_list_ok_response.json";

    @Rule
    public IntentsTestRule<RepoDetailActivity> mRepoListTestRule = new IntentsTestRule<RepoDetailActivity>(RepoDetailActivity.class, true, false);

    private final static MockWebServer server  = new MockWebServer();;

    @Before
    public void setUp() throws Exception {
        try {
            server.start(0);

        }catch (IllegalStateException e){
            // alrealdy started: nothing to do
        }
        InstrumentationRegistry.registerInstance(InstrumentationRegistry.getInstrumentation(),new Bundle());
        Constants.API_BASE_URL = server.url("/").toString();
        Timber.i("setUp with API_BASE_URL: " + Constants.API_BASE_URL);


//        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

    }
    @After
    public void tearDown() throws Exception {
        Timber.i("tearDown");
        server.shutdown();
        //server = null;
    }



    @Test
    public void pullListTest() throws IOException {
        Timber.i("pullListTest");

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(RestServiceTestHelper.getStringFromFile(
                        InstrumentationRegistry.getInstrumentation().getContext()
                        ,fileNamePullsListOKRespone)
                )
        );


        Intent intent = new Intent();
        intent.putExtra(RepoDetailFragment.ARG_CREATOR, "elastic");
        intent.putExtra(RepoDetailFragment.ARG_REPOSITORY, "elasticsearch");
        mRepoListTestRule.launchActivity(intent);


        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

        onView(allOf(withId(R.id.pull_textview_title), withText("Update synonym-tokenfilter.asciidoc"))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.pull_text_view_description), withText(containsString("1) Correct a typo that refers to the 'graph_synonyms' filter by the name of the")))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.pull_textview_owner_username), withText("elasticsearcher"), withHint("elasticsearcher1"))).check(matches(isDisplayed()));


    }

    @Test
    public void clickTest() throws IOException {
        Timber.i("ClickTest");

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(RestServiceTestHelper.getStringFromFile(
                        InstrumentationRegistry.getInstrumentation().getContext()
                        ,fileNamePullsListOKRespone)
                )
        );




        Intent intent = new Intent();
        intent.putExtra(RepoDetailFragment.ARG_CREATOR, "elastic");
        intent.putExtra(RepoDetailFragment.ARG_REPOSITORY, "elasticsearch");
        mRepoListTestRule.launchActivity(intent);
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));




        String urlString = "https://github.com/elastic/elasticsearch/pull/25280";
//        Intents.init();


        onView(allOf(withId(R.id.pull_linear_layout_container), withContentDescription("Pull Request number " + 1))).perform(click());

        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse(urlString))));

//        Intents.release();
    }



}
