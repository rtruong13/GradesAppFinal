package com.example.ryan.gradesapp.ASyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.ryan.gradesapp.Models.ProfessorModel;
import com.example.ryan.gradesapp.Params.LoadingURLTaskParam;
import com.example.ryan.gradesapp.SearchedCourseActivity;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Ryan on 10/20/2015.
 */
public class LoadingURLTask extends AsyncTask<LoadingURLTaskParam, Void, ArrayList<ProfessorModel>> {

    LoadingURLTaskParam param;
    WeakReference<Context> context;
    String avgImageURL;

    public LoadingURLTask(Context c) {
        context = new WeakReference<Context>(c); //Why use Weak Reference? Because if Activity is pressed back while
        // this is still loading, it causes a memory leak. This AsyncTask will still be run.
        // Attach Weak Ref to destroy it when activity is destroyed.

    }

    @Override
    protected ArrayList<ProfessorModel> doInBackground(LoadingURLTaskParam... strings) {

        ArrayList<ProfessorModel> listOfModels = new ArrayList<>();
        try {
            param = strings[0]; //Param object instantiated
            Document document = Jsoup.connect(param.url).get(); //param.url is the actual url

             avgImageURL = getAverageDist(document);  //Gets a single avg grade dist image URL


            /*Grab everything with name prof, then grab everything with tag tbody then loop inside toboy and grab
            everything with tag img. Use .text to grab inside =" " and attr to grab all with attr*/
            Elements profs = document.select("table[class=profs]");
            Elements classes = profs.get(0).select("tbody[class=\"list\"]");
            Elements aTag = profs.get(0).select("tbody[data-name]");

            for (int i = 0; i < classes.size(); i++) {

                Elements content = classes.get(i).select("div[class=content");
                Elements imgs = content.get(0).select("img[lsrc]");

                Elements gradeCount = content.get(0).select("p[class=grade-count]");
                String profGrade = aTag.get(i).attr("data-name") + "                 " + gradeCount.select("p[class=grade-count]").get(0).text();
                String pastYear = aTag.get(i).attr("data-past_year");
                Boolean pYear;
                if (pastYear.equals("1")) {
                    pYear = true;
                } else
                    pYear = false;
                listOfModels.add(new ProfessorModel(imgs.get(0).attr("lsrc"), profGrade, pYear));

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return listOfModels;
    }

    @Override
    protected void onPostExecute(ArrayList<ProfessorModel> result) {
        if (context.get() != null){
            Picasso.with(context.get())
                    .load(avgImageURL)
                    .into(param.avgDistribution);
        }

        param.getListInterface.loadFullList(result);
        ArrayList<ProfessorModel> pastYearList = new ArrayList<>();
         /*Now remove elements that are not within the past year*/
        for (ProfessorModel profModel : result) {
            if (profModel.getPastYear() == true) {
                pastYearList.add(profModel);
            }
        }

        param.pastYearAdapter.changeAllProfessors(pastYearList); //So after you grab all the data from server, after you call execute.
        // This will give the adapter in main all the objects we got from runInBackground
       // param.pastYearAdapter.notifyDataSetChanged();//Notifies the attached observers that the underlying data has been changed and any View reflecting the data set should refresh itself.

    }

    //Grab the average distribution image, separate from all the professors
    private String getAverageDist(Document doc){
        Elements image = doc.select("div[class=image]");
        Element avgImg = image.select("img[src]").get(0);
        String imgUrl = avgImg.attr("src");
        return imgUrl;
    }
}
