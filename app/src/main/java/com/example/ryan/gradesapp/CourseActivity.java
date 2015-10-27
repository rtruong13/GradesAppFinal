package com.example.ryan.gradesapp;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryan.gradesapp.ASyncTasks.DepartmentTask;
import com.example.ryan.gradesapp.ASyncTasks.LoadingUniversityTask;

public class CourseActivity extends AppCompatActivity {

    Spinner sSpinner;
    EditText courseNumberET;
    TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        emptyText = (TextView) findViewById(R.id.empty_item);

        SharedPreferences schoolPrefs = getSharedPreferences("data", MODE_PRIVATE);
        int schoolID = schoolPrefs.getInt("schoolID", 0);
        String schoolName = schoolPrefs.getString("schoolName", "");
        if (schoolName == "") {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && !data.getBooleanExtra("enteredData", false)) {
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences schoolPrefs = getSharedPreferences("data", MODE_PRIVATE);
        String schoolURL = schoolPrefs.getString("schoolURL", "");
        String schoolName = schoolPrefs.getString("schoolName", "");

        if (schoolName != "") {
            sSpinner = (Spinner) findViewById(R.id.courses_spinner);
            courseNumberET = (EditText) findViewById(R.id.courses_edit_text);


            //Log.d("hey", Integer.toString(schoolID));
            // Log.d("hey",schoolName);
            //Log.d("hey", schoolURL);


            //Call Department Task on Create to get list of all dpmts
            DepartmentTask departmentTask = new DepartmentTask(this);
            departmentTask.execute(schoolURL);
            departmentTask.getSpinner(sSpinner);
        }
    }

    public void courseSearch(View view) {
        String courseAbbrev = sSpinner.getSelectedItem().toString();
        String courseNum = courseNumberET.getText().toString();
        SharedPreferences schoolPrefs = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = schoolPrefs.edit();
        editor.putString("COURSE", courseAbbrev + " " + courseNum);
        editor.commit();
        int schoolID = schoolPrefs.getInt("schoolID", 0);
        LoadingUniversityTask task = new LoadingUniversityTask(this);
        task.getSchoolListView((ListView) findViewById(R.id.coursesListView));

        String url = "https://www.myedu.com/search/?search_term=" + courseAbbrev
                + "+" + courseNum + "&doctype=course&school=" + schoolID;

        task.execute("", "course", url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_course; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
