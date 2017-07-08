package com.example.rafaelanastacioalves.desafioandroid.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rafaelanastacioalves on 4/16/16.
 */

@SuppressWarnings({"CanBeFinal", "unused"})
class GitHubUser implements Serializable {
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

    public String getAvatarUrl() {
        return avatarUrl;
    }


}
