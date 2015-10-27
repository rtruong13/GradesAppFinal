package com.example.ryan.gradesapp.Models;

import android.widget.ListView;

/**
 * Created by Ryan on 10/16/2015.
 */
public class CourseModel extends EntityModel {
    String courseName;
    String atUniv;
    String courseURL;
    int courseID;

    public CourseModel(String url,String pName, String univ, int id) {
        super("course", "");
        atUniv=univ;
        courseName = pName;
        courseURL = url;
        courseID = id;
    }

    @Override
    public String getName() {
        return courseName;
    }

    public String getURL() {
        return courseURL;
    }

    public int getCourseID(){
        return courseID;

    }

}
