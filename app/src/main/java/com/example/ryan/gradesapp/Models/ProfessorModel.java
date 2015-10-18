package com.example.ryan.gradesapp.Models;

/**
 * Created by Ryan on 10/16/2015.
 */
public class ProfessorModel extends EntityModel {
    String profName;
    int profId;

    public ProfessorModel(String url,String pName, int id) {
        super("professor", url);
        profName = pName;
        profId = id;
    }
}
