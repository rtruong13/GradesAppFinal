package com.example.ryan.gradesapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.ryan.gradesapp.ASyncTasks.LoadingURLTask;
import com.example.ryan.gradesapp.Adapters.DistributionAdapter;
import com.example.ryan.gradesapp.Interfaces.getList;
import com.example.ryan.gradesapp.Models.ProfessorModel;
import com.example.ryan.gradesapp.Params.LoadingURLTaskParam;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchedCourseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, getList {
    ArrayList<ProfessorModel>allProfessor;

    ImageView avgDistImg;
    LinearLayout linearLayout;
    ListView distListView;
    Spinner gSpinner;
    DistributionAdapter adapter;
    String[] select = {"Professors from past year","All Professors"};
    ArrayList<ProfessorModel> pastYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_course);

        gSpinner = (Spinner)findViewById(R.id.grade_spinner);
        linearLayout = (LinearLayout) findViewById(R.id.linlayout);
        avgDistImg = (ImageView) findViewById(R.id.average_distribution_img);
        distListView = (ListView)findViewById(R.id.distribution_list_view);

        linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));// Set background to match myedu's white background color

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, select);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        gSpinner.setAdapter(spinnerAdapter);
        gSpinner.setOnItemSelectedListener(this);


        SharedPreferences schoolPrefs = getSharedPreferences("data", MODE_PRIVATE);
        String courseURL = schoolPrefs.getString("courseURL", "");
        String label = schoolPrefs.getString("COURSE", "Distributions");
        setTitle(label);//Set the toolbar title

        //Create adapter
        adapter = new DistributionAdapter(this, new ArrayList<ProfessorModel>());
        distListView.setAdapter(adapter);

        //Create object with adapter and URL variables
        LoadingURLTaskParam param = new LoadingURLTaskParam();
        param.avgDistribution = (ImageView) findViewById(R.id.average_distribution_img);
        param.pastYearAdapter = adapter;
        param.url = courseURL;
        param.getListInterface= this;
        LoadingURLTask loadingTask = new LoadingURLTask(this);
        loadingTask.execute(param); //Passed param


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(i == 0){
            //show half list by using adapter
            adapter.changeAllProfessors(pastYear);
        }
        else{
            //show half listby using adapter
            adapter.changeAllProfessors(allProfessor);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void loadFullList(ArrayList<ProfessorModel> professorModels) {
        allProfessor= new ArrayList<>(professorModels);
        pastYear = new ArrayList<>();
        for (ProfessorModel profModel :professorModels) {
            if (profModel.getPastYear() == true) {
                pastYear.add(profModel);
            }
        }
    }
}
