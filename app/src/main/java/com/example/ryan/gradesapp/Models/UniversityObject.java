package com.example.ryan.gradesapp.Models;

/**
 * Created by Ryan on 10/16/2015.
 */
public class UniversityObject extends EntityModel {
    String name;
    int uniID;


    public UniversityObject(int id, String n, String url)
    {
        super("school", url);
        uniID = id;
        name = n;

    }

    public String getURL(){
        return url;
    }

    @Override
    public String getName(){

        return name;
    }

    public int getUniID(){

        return uniID;
    }
}
