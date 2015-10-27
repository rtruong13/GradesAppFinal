package com.example.ryan.gradesapp.ASyncTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ryan.gradesapp.CourseActivity;
import com.example.ryan.gradesapp.Models.CourseModel;
import com.example.ryan.gradesapp.Models.EntityModel;
import com.example.ryan.gradesapp.Models.ProfessorModel;
import com.example.ryan.gradesapp.Models.UniversityObject;
import com.example.ryan.gradesapp.SearchedCourseActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ryan on 10/16/2015.
 */
public class LoadingUniversityTask extends AsyncTask<String, Void, ArrayList<EntityModel>> {
    Activity context;
    ListView listView;
    String whatToSearchFor;

    public LoadingUniversityTask(Activity c) {
        context = c;

    }

    @Override
    protected ArrayList<EntityModel> doInBackground(String... strings) { //array of string objects
        // Connect to the web site
        try {
            whatToSearchFor = strings[1]; //Grabs either professor, school, etc..
            //Log.d("BUILDING URL", strings[0]); //Returns what you searched for in EditText i.e. Santa Barbara

            String url = "";
            if (strings.length <= 2)
                url = buildURL(strings[0], whatToSearchFor); //creates an url i.e. https://www.myedu.com/search/?search_term=barbara&doctype=school
            else
                url = strings[2];

            Document document = Jsoup.connect(url).get();
            Elements links = document.select("li[class*=" + whatToSearchFor + "]"); //Search in html for these tags
            ArrayList<EntityModel> models = ParseElements(links, whatToSearchFor);//Passes an array list of found tags
            //and returns a ArrayList of the found results using whatToSearchFor

            return models;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<EntityModel> ParseElements(Elements elements, String type)//Returns ArrayList of entities
    {
        ArrayList<EntityModel> retValue = null;

     /*   if (type.equals("professor")) {
            retValue = new ArrayList<EntityModel>();
            for (int i = 0; i < elements.size(); i++) {
                retValue.add(parseProfessor(elements.get(i)));
            }
        } */
        if (type.equals("school")) {
            retValue = new ArrayList<EntityModel>();
            for (int i = 0; i < elements.size(); i++) {
                retValue.add(parseSchool(elements.get(i))); //Stores a Uni Object with name, id, and link
            }

        } else if (type.equals("course")) {
            retValue = new ArrayList<EntityModel>();
            for (int i = 0; i < elements.size(); i++) {
                retValue.add(parseCourse(elements.get(i)));
            }
        }
        return retValue;
    }

    //We don't actually use this method.
  /*  private ProfessorModel parseProfessor(org.jsoup.nodes.Element element) {
        String name = element.select("p[class]").get(0).data();
        org.jsoup.nodes.Element link = element.select("a[href]").get(0);
        String urlLink = "https://www.myedu.com/" + link.attr("href");
        int id = GetId(urlLink);
        return new ProfessorModel(urlLink, name, id, urlLink);
    }*/

    private CourseModel parseCourse(org.jsoup.nodes.Element element) {
        org.jsoup.nodes.Element link1 = element.select("a[href]").get(0);
        String name = link1.text();
        org.jsoup.nodes.Element link = element.select("a[href]").get(0);
        String urlLink = "https://www.myedu.com/" + link.attr("href");
        int id = GetId(urlLink);
        String univ = element.select("p[class*=subtitle]").get(0).text();
        return new CourseModel(urlLink, name, univ, id);
    }

    private UniversityObject parseSchool(org.jsoup.nodes.Element element) {
        org.jsoup.nodes.Element link = element.select("a[href]").get(0);
        String name = link.text(); //Returns i.e. Santa Barbara Business College
        //  Log.d("name", name);
        // Log.d("attr", link.attr("href")); //Returns i.e. /Santa-Barbara-Business-College/school/6335
        String urlLink = "https://www.myedu.com/" + link.attr("href"); //returns i.e. https://www.myedu.com/Santa-Barbara-Business-College/school/6335/
        int id = GetId(urlLink);  //id of the school found i.e.6335
        // Log.d("urlLinkID",Integer.toString(id));
        //Log.d("urlLink", urlLink);
        return new UniversityObject(id, name, urlLink); //Returns Uni with name and id, link
    }

    private String buildURL(String search, String whatToSearchFor) {
        String link = "https://www.myedu.com/search/?search_term=";
        search = search.replace(" ", "+"); //
        link = link + search + "&doctype=" + whatToSearchFor; //provides exact url
        return link;
    }

    private int GetId(String url) {
        String id = url.substring(url.lastIndexOf('/') + 1);
        int intID = Integer.valueOf(id);
        return intID;
    }

    @Override
    protected void onPostExecute(ArrayList<EntityModel> objects) { //Paramter is found results
        createList(objects);
    }

    private void createList(ArrayList<EntityModel> objects) {

        if (whatToSearchFor == "school") {
            final ArrayList<UniversityObject> uniObj = getUniList(objects);
            final ArrayList<String> schoolNames = new ArrayList<>();

            for (EntityModel uniModel : objects) {
                schoolNames.add(uniModel.getName());
            }
            if (objects.size() == 0) {
                schoolNames.add("Could not find school name. Please enter another.");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, schoolNames);
                listView.setAdapter(adapter);
            } else {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, schoolNames);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        final String title = ((TextView) view).getText().toString();
                        builder.setTitle(title);

                        final UniversityObject object = uniObj.get(i);

                        builder.setMessage("Are you sure you want to save?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences schoolPrefs = context.getSharedPreferences("data", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = schoolPrefs.edit();
                                editor.putString("schoolName", title);
                                editor.putInt("schoolID", object.getUniID());
                                editor.putString("schoolURL", object.getURL());
                                editor.commit();
                                Intent intent = new Intent();
                                intent.putExtra("enteredData", true);
                                context.setResult(200, intent);
                                context.finish();

                            }
                        });
                        builder.setNegativeButton("No, chose another", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
            }
        } else if (whatToSearchFor == "course") {
            final ArrayList<CourseModel> courseObj = getCourseList(objects);
            final ArrayList<String> courseNames = new ArrayList<>();

            for (EntityModel courseModel : objects) {
                courseNames.add(courseModel.getName());
            }
            if (objects.size() == 0) {
                courseNames.add("No courses found.");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, courseNames);
                listView.setAdapter(adapter);
            }else {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, courseNames);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        final CourseModel object = courseObj.get(i);

                        SharedPreferences schoolPrefs = context.getSharedPreferences("data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = schoolPrefs.edit();
                        editor.putString("courseURL", object.getURL());
 //                       Log.d("hey",object.getURL()); https://www.myedu.com//ANTH-111-The-Anthropology-of-Food/course/s/1081705
   //                     Log.d("hey", object.getName()); ANTH 111 The Anthropology of Food
     //                   Log.d("hey", Integer.toString(object.getCourseID())); 1081705


                        editor.commit();

                        Intent intent = new Intent(context, SearchedCourseActivity.class);
                        context.startActivity(intent);
                    }
                });
            }
        }
    }

    //Takes list of EntityModels and separates into another list of UniModels and returns it
    private ArrayList<UniversityObject> getUniList(ArrayList<EntityModel> obj) {
        ArrayList<UniversityObject> uniList = new ArrayList<>();
        for (EntityModel entityModel : obj) {
            if (entityModel.nameOfCategory == "school") {
                UniversityObject uniObject;
                uniObject = (UniversityObject) entityModel;
                uniList.add(uniObject);
            }
        }
        return uniList;
    }

    //Takes list of EntityModels and separates into another list of CourseModels and returns it
    private ArrayList<CourseModel> getCourseList(ArrayList<EntityModel> obj) {
        ArrayList<CourseModel> courseList = new ArrayList<>();
        for (EntityModel entityModel : obj) {
            if (entityModel.nameOfCategory == "course") {
                CourseModel courseObject;
                courseObject = (CourseModel) entityModel;
                courseList.add(courseObject);
            }
        }
        return courseList;
    }


    public void getSchoolListView(ListView listview) {
        listView = listview;

    }


}
