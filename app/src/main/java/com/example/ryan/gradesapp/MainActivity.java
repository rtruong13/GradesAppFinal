package com.example.ryan.gradesapp;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ryan.gradesapp.ASyncTasks.LoadingUniversityTask;
import com.example.ryan.gradesapp.Interfaces.OnLoadUniListCompleted;
import com.example.ryan.gradesapp.Models.UniversityObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnLoadUniListCompleted{

    EditText schoolET;
    String sData = "data";
    ListView schoolListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences schoolPrefs = getSharedPreferences(sData, MODE_PRIVATE);
        schoolET = (EditText)findViewById(R.id.school_name_et);
        schoolListView = (ListView) findViewById(R.id.school_list_view);


        String school;
        school = schoolPrefs.getString("schoolName", "");

        schoolET.setText(school.toString());
    }

    public void searchClick(View view) {
        String name = schoolET.getText().toString();

        LoadingUniversityTask downloadTask = new LoadingUniversityTask(this);
        downloadTask.execute(name, "school");
        downloadTask.getSchoolListView(schoolListView);  //Set the listview in another thread.


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("enteredData", false);
        setResult(200, intent);
        super.onBackPressed();

    }

    //Here we get back the ArrayList of uni objects and load adapter with it
    @Override
    public void onLoadUniListCompleted(ArrayList<UniversityObject> uniObject) {

    }
}
