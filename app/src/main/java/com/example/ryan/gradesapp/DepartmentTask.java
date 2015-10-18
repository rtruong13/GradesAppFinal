package com.example.ryan.gradesapp;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ryan.gradesapp.Models.EntityModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ryan on 10/17/2015.
 */
public class DepartmentTask extends AsyncTask<String, Void, ArrayList<EntityModel>> {
    @Override
    protected ArrayList<EntityModel> doInBackground(String... strings) {

        try {
            String URL = strings[0]; //Grabs the school URL i.e. https://www.myedu.com//University-of-California-Santa-Barbara/school/255
            String url = buildURL(strings[0]); //creates an url i.e. https://www.myedu.com/search/?search_term=barbara&doctype=school
            //Log.d("hey", url);

            Document document = Jsoup.connect(url).get();
            Elements links = document.select("a[class*=abbreviation" + "]"); //Search in html for these tags


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }





    private String buildURL(String URL) {
        String link = URL + "/course/by-department";
        return link;
    }
}
