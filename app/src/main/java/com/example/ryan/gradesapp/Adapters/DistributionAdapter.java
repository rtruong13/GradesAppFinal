package com.example.ryan.gradesapp.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ryan.gradesapp.Models.ProfessorModel;
import com.example.ryan.gradesapp.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan on 10/18/2015.
 */
public class DistributionAdapter extends ArrayAdapter<String> {

    Context context;
    ArrayList<ProfessorModel> profModel;

    public DistributionAdapter(Context context, ArrayList<ProfessorModel> model) {
        super(context, R.layout.list_items_distribution);
        this.context = context;
        this.profModel = model;


    }

    public void changeAllProfessors(List<ProfessorModel> result) {
        if(result == null) return;
        profModel= new ArrayList<>(result);
        notifyDataSetChanged();
    }

    class  MyViewHolder {
        ImageView myImage;
        TextView myDescription;


        MyViewHolder(View v) {
            myImage = (ImageView) v.findViewById(R.id.distribution_img);
            myDescription = (TextView) v.findViewById(R.id.dist_text);
            myDescription.setTextSize(10);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MyViewHolder holder = null;
        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_items_distribution, parent, false);
            holder = new MyViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (MyViewHolder) row.getTag();
        }

        Picasso.with(context)
                .load(profModel.get(position).gradeDistributionURL)
                .into(holder.myImage);
        holder.myDescription.setText(profModel.get(position).getName());
        holder.myDescription.setTypeface(null, Typeface.BOLD);
        return row;

    }

    @Override
    public int getCount() {
        return profModel.size();
    }
}
