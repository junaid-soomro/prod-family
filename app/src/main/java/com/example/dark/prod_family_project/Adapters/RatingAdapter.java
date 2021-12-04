package com.example.dark.prod_family_project.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.dark.prod_family_project.Activities.Rating;
import com.example.dark.prod_family_project.Models.RatingModel;
import com.example.dark.prod_family_project.R;

import java.util.ArrayList;

/**
 * Created by abd on 01-May-18.
 */

public class RatingAdapter extends BaseAdapter {

    ArrayList<RatingModel> arrayList = new ArrayList<>();
    Context context;

    TextView name;
    RatingBar ratingBar;

    public RatingAdapter(ArrayList<RatingModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.rating_view,null,false);

        name = (TextView)view.findViewById(R.id.rating_name);
        ratingBar = (RatingBar)view.findViewById(R.id.ratingBar);

        name.setText(arrayList.get(i).getName());
        ratingBar.setRating(Float.parseFloat(arrayList.get(i).getRate()));

        return view;
    }
}
