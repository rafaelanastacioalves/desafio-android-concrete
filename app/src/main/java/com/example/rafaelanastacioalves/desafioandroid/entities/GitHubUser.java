package com.example.rafaelanastacioalves.desafioandroid.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rafaelanastacioalves on 4/16/16.
 */

public class GitHubUser implements Serializable {
    @SerializedName("login")
    private String login;



    @SerializedName("id")
    private int id;

    @SerializedName("avatar_url")
    private String avatarUrl;


    private String pictureStringData;

    public String getLogin() {
        return login;
    }


    public String getPictureStringData() {
        return pictureStringData;
    }

    public void setPictureStringData(String pictureStringData) {
        this.pictureStringData = pictureStringData;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }


    public int getId() {
        return id;
    }
}
