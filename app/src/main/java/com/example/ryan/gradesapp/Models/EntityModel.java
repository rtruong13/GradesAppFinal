package com.example.ryan.gradesapp.Models;

/**
 * Created by Ryan on 10/16/2015.
 */
public class EntityModel {
    public String nameOfCategory;
    public String url;

    public EntityModel(String nameOfEntity, String url)
    {
        nameOfCategory =nameOfEntity;
        this.url =url;
    }


}