package com.example.rafaelanastacioalves.desafioandroid.retrofit;

import com.example.rafaelanastacioalves.desafioandroid.entities.Pull;
import com.example.rafaelanastacioalves.desafioandroid.entities.Repos;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by rafaelalves on 19/05/17.
 */

@SuppressWarnings("ALL")
public interface GithubClient {


    // === Repositories === //
    @GET("/search/repositories?q=language:Java")
    Call<Repos> getRepos(
            @Query("sort") String sort,
            @Query("page") int page
    );
//    void getRepo(@Query("q") String q, @Query("sort") String sort, @Query("page") int page, Callback<Repos> response);


    @GET("/repos/{creator}/{repository}/pulls")
    Call<ArrayList<Pull>> getPulls(
            @Path("creator") String creator,
            @Path("repository") String repository
    );
//    void getPulls(@Path("creator") String creator, @Path("repository") String repository, Callback<ArrayList<Pull>> response);




}
