package com.example.rafaelanastacioalves.desafioandroid.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rafaelanastacioalves on 28/09/2015.
 */
@SuppressWarnings({"DefaultFileTemplate", "unused"})
public class Pull implements Serializable {


    @SerializedName("title")
    private String title;

    @SerializedName("user")
    private PullUser pullUser;

    @SerializedName("body")
    private String body;

    @SerializedName("html_url")
    private String pullUrl;


    public Pull() {
    }


    public String getTitle() {
        return title;
    }


    public String getBody() {
        return body;
    }


    public PullUser getPullUser() {
        return pullUser;
    }

    public String getPullUrl() {
        return pullUrl;
    }
}

