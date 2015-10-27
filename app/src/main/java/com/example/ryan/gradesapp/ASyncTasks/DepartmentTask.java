package com.example.ryan.gradesapp.ASyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.ryan.gradesapp.Models.EntityModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ryan on 10/17/2015.
 */
public class DepartmentTask extends AsyncTask<String, Void, Void> {

    Spinner schoolSpinner;
    ArrayList<String> abr = new ArrayList<>();
    Context context;
    public DepartmentTask(Context c) {
        context = c;

    }

    @Override
    protected Void doInBackground(String... strings) {

        try {
            String URL = strings[0]; //https://www.myedu.com//University-of-California-Santa-Barbara/school/255
            String url = buildURL(strings[0]); //https://www.myedu.com//University-of-California-Santa-Barbara/school/255/course/by-department

            Document document = Jsoup.connect(url).get();
            Elements links = document.select("a[class*=abbreviation]"); //Search in html for these tags

            for (int i = 0; i<links.size(); i++)
            {
                abr.add(links.get(i).text()); //abbreviation "ANTH" "ART" etc...
            //    Log.d("hey", links.get(i).text());
            }
            if(abr.size()==0){
                //Set a case here for no results found. Sorry.
                Log.d("hey", "No results"); //Dominican University doesn't have a database
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, abr);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        schoolSpinner.setAdapter(spinnerAdapter);
       // schoolSpinner.setEmptyView();
    }

    ArrayAdapter<String> listArray;
    public void getArrayList(ArrayAdapter<String> array){
        listArray =  array;

    }


    private String buildURL(String URL) {
        String link = URL + "/course/by-department";//https://www.myedu.com//University-of-California-Santa-Barbara/school/255/course/by-department
        return link;
    }

    public void getSpinner(Spinner spinner){
        schoolSpinner = spinner;

    }
}
