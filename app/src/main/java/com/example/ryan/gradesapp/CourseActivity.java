package com.example.ryan.gradesapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class CourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        SharedPreferences schoolPrefs = getSharedPreferences("data", MODE_PRIVATE);
        int schoolID = schoolPrefs.getInt("schoolID", 0);
        String schoolName = schoolPrefs.getString("schoolName", "");
        String schoolURL = schoolPrefs.getString("schoolURL", "");
        //Log.d("hey", Integer.toString(schoolID));
       // Log.d("hey",schoolName);
        //Log.d("hey", schoolURL);


        //Call Department Task on Create to get list of all dpmts
        DepartmentTask departmentTask = new DepartmentTask();
        departmentTask.execute(schoolURL);
    }
}
