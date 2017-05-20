package com.example.rafaelanastacioalves.desafioandroid.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Repos {
    private List<Repo> items;

    public Repos() {
        super();
    }

    public List<Repo> getRepoList() {
        return items;
    }

    public List<Integer> getRepoOwnersIDList() {
        List<Integer> list = new ArrayList<Integer>();
        for (Repo repo :
                items) {
            list.add(repo.getOwner().getId());
        }
        return list;
    }
    
    public void setRepoList(List<Repo> repos) {
        this.items = repos;
    }

    public void setRepoOwnersPicture(HashMap picturesHash){
        for (Repo repo:
                items) {
            int idKey = repo.getOwner().getId();
            repo.setPictureFile((String)picturesHash.get(idKey));

        }
    }
}
