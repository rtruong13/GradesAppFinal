package com.example.ryan.gradesapp.Params;

import android.widget.ImageView;

import com.example.ryan.gradesapp.Adapters.DistributionAdapter;
import com.example.ryan.gradesapp.Interfaces.getList;

/**
 * Created by Ryan on 10/20/2015.
 */

//Created so that you can create an instance of this class in Activity. Pass it to async task and fill it up. It automatically changes the values when you change in
    //async task.
public class LoadingURLTaskParam{
    public String url;
    public DistributionAdapter pastYearAdapter;
    public getList getListInterface;
    public ImageView avgDistribution;
}
