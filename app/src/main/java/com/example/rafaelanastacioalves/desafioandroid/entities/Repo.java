package com.example.rafaelanastacioalves.desafioandroid.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rafaelanastacioalves on 28/09/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Repo implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("stargazers_count")
    private int stargazers_count;

    @SerializedName("forks")
    private int forks;

    @SerializedName("owner")
    private RepoOwner owner;

    @SerializedName("pulls_url")
    private String pullsUrl;
    private String pictureFile;

    public Repo() {
    }

    public Repo(String text) {
        this.name = text;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setText(String text) {
        this.name = text;
    }

    public int getForks() {
        return forks;
    }

    public RepoOwner getOwner() {
        return owner;
    }

    public int getStargazersCount() {
        return stargazers_count;
    }

    public void setPictureFile(String picture) {
        this.pictureFile = picture;
    }
}

