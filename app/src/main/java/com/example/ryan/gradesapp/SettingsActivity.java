package com.example.ryan.gradesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    TextView schoolName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        schoolName = (TextView)findViewById(R.id.school_text);
        SharedPreferences schoolPrefs = getSharedPreferences("data", MODE_PRIVATE);
        String schoolName = schoolPrefs.getString("schoolName", "");
        this.schoolName.setText(schoolName);
        this.schoolName.setTextColor(Color.parseColor("#000000"));

    }

    public void pickClick(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();

    }
}
