package com.example.dark.prod_family_project.Models;

/**
 * Created by abd on 01-May-18.
 */

public class RatingModel {

    String name,rate,id;

    public String getId() {
        return id;
    }

    public RatingModel(String id, String name, String rate) {
        this.id = id;
        this.name = name;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public String getRate() {
        return rate;
    }
}
