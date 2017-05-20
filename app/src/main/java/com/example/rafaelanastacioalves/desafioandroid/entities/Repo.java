package com.example.rafaelanastacioalves.desafioandroid.entities;

/**
 * Created by rafaelanastacioalves on 28/09/2015.
 */
public class Repo {

    private String name;

    private String description;

    private int stargazers_count;

    private int forks;

    private RepoOwner owner;

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

