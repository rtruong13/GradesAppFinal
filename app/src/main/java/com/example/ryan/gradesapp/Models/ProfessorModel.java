package com.example.ryan.gradesapp.Models;

/**
 * Created by Ryan on 10/16/2015.
 */
public class ProfessorModel extends EntityModel {
    public String profName;
    public String gradeDistributionURL;
    public Boolean pastYear;

    public ProfessorModel(String url, String pName, Boolean pYear) {
        super("professor", url);
        profName = pName;
        gradeDistributionURL = url;
        pastYear = pYear;

    }

    @Override
    public String getName() {
        return profName;
    }

    public Boolean getPastYear(){
        return pastYear;
    }

}
