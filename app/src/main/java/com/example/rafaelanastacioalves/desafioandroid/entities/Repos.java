package com.example.rafaelanastacioalves.desafioandroid.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Repos implements Serializable {
    @SerializedName("items")
    private List<Repo> repos;

    public Repos() {
        super();
    }

    public List<Repo> getRepoList() {
        return repos;
    }

    public List<Integer> getRepoOwnersIDList() {
        List<Integer> list = new ArrayList<Integer>();
        for (Repo repo :
                repos) {
            list.add(repo.getOwner().getId());
        }
        return list;
    }
    
    public void setRepoList(List<Repo> repos) {
        this.repos   = repos;
    }

    public void setRepoOwnersPicture(HashMap picturesHash){
        for (Repo repo:
             repos) {
            int idKey = repo.getOwner().getId();
            repo.setPictureFile((String)picturesHash.get(idKey));

        }
    }
}
