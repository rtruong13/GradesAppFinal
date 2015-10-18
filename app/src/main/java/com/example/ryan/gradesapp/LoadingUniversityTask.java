package com.example.ryan.gradesapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Entity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryan.gradesapp.Models.EntityModel;
import com.example.ryan.gradesapp.Models.ProfessorModel;
import com.example.ryan.gradesapp.Models.UniversityObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan on 10/16/2015.
 */
public class LoadingUniversityTask extends AsyncTask<String, Void, ArrayList<EntityModel>> {
    Context context;
    ArrayList<EntityModel> copyModel;
    ListView schoolListView;
    SharedPreferences schoolPrefs;

    public LoadingUniversityTask(Context c) {
        context = c;

    }

    @Override
    protected ArrayList<EntityModel> doInBackground(String... strings) { //array of string objects
// Connect to the web site
        try {
            String whatToSearchFor = strings[1]; //Grabs either professor, school, etc..
            //Log.d("BUILDING URL", strings[0]); //Returns what you searched for in EditText i.e. Santa Barbara

            String url = buildURL(strings[0], whatToSearchFor); //creates an url i.e. https://www.myedu.com/search/?search_term=barbara&doctype=school
            //Log.d("BUILDING URL", url);

            Document document = Jsoup.connect(url).get();

            Elements links = document.select("li[class*=" + whatToSearchFor + "]"); //Search in html for these tags


            ArrayList<EntityModel> models = ParseElements(links, whatToSearchFor);//Passes an array list of found tags
            //and returns a ArrayList of the found results using whatToSearchFor

            copyModel = models;
            return models;

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Get the html document title
        //title = document.title();
        return null;
    }

    private ArrayList<EntityModel> ParseElements(Elements elements, String type)//Returns ArrayList of entities
    {
        ArrayList<EntityModel> retValue = null;

        if (type.equals("professor")) {
            retValue = new ArrayList<EntityModel>();
            for (int i = 0; i < elements.size(); i++) {
                retValue.add(parseProfessor(elements.get(i)));
            }
        } else if (type.equals("school")) {
            retValue = new ArrayList<EntityModel>();
            for (int i = 0; i < elements.size(); i++) {
                retValue.add(parseSchool(elements.get(i))); //Stores a Uni Object with name, id, and link

            }

        }

        return retValue;
    }

    private ProfessorModel parseProfessor(org.jsoup.nodes.Element element) {
        String name = element.select("p[class]").get(0).data();
        org.jsoup.nodes.Element link = element.select("a[href]").get(0);
        String urlLink = "https://www.myedu.com/" + link.attr("href");
        int id = GetId(urlLink);

        return new ProfessorModel(urlLink, name, id);
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
        createUniList();
    }

    private void createUniList(){

        final ArrayList<UniversityObject> uniObj = getUniList();
        final ArrayList<String> schoolNames = new ArrayList<>();




        for (UniversityObject uniModel : uniObj) {
            schoolNames.add(uniModel.getName());
        }
        if(uniObj.size() == 0){
            schoolNames.add("Could not find school name. Please enter another.");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, schoolNames);
            schoolListView.setAdapter(adapter);
        }
        else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, schoolNames);
            schoolListView.setAdapter(adapter);

            schoolListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


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
                            // TODO Auto-generated method stub

                            SharedPreferences schoolPrefs = context.getSharedPreferences("data", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = schoolPrefs.edit();
                            editor.putString("schoolName", title);
                            editor.putInt("schoolID", object.getUniID());
                            editor.putString("schoolURL", object.getURL());

                            editor.commit();

                            Intent intent = new Intent (context, CourseActivity.class);
                            context.startActivity(intent);

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
    }

    private ArrayList<UniversityObject> getUniList() {
        ArrayList<UniversityObject> uniList = new ArrayList<>();
        for (EntityModel entityModel : copyModel) {
            if (entityModel.nameOfCategory == "school") {
                UniversityObject uniObject;
                uniObject = (UniversityObject) entityModel;
                uniList.add(uniObject);
            }
        }
        return uniList;
    }

    public void getSchoolListView(ListView listview) {
        schoolListView = listview;

    }


}
