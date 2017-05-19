package com.example.rafaelanastacioalves.desafioandroid.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rafaelanastacioalves on 28/09/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pulls implements Serializable {



    private List<Pull> pullRequestList;




    public Pulls() {
    }




    public List<Pull> getPullRequestList() {
        return pullRequestList;
    }


}

