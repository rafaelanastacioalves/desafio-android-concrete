package com.example.rafaelanastacioalves.desafioandroid.entities;

/**
 * Created by rafaelanastacioalves on 28/09/2015.
 */
@SuppressWarnings({"unused", "DefaultFileTemplate"})
public class Repo {

    private String name;

    private String description;

    private int stargazers_count;

    private int forks;

    private RepoOwner owner;

    private String pullsUrl;
    @SuppressWarnings("FieldCanBeLocal")
    private String pictureFile;

    public Repo() {
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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

}

