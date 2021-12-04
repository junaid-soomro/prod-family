package com.example.dark.prod_family_project.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dark.prod_family_project.Holder.all_holders;
import com.example.dark.prod_family_project.Models.all_categories;
import com.example.dark.prod_family_project.R;

import java.util.ArrayList;

/**
 * Created by abd on 27-Jan-18.
 */

public class categoriesAdapter extends BaseAdapter {

    ArrayList<all_categories> categories = new ArrayList<>();

    TextView categ;

    ImageView imageView;

    Context context;

    public categoriesAdapter(ArrayList<all_categories> categories,Context ctx) {
        this.context = ctx;

        this.categories = categories;
    }



    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.categories_list_items,null,false);

        initialize(view);
        setvalues(i);
        return view;
    }

    private void setvalues(int position) {

        categ.setText(categories.get(position).getCategories().toString());

    }

    private void initialize(View view) {

        categ = (TextView)view.findViewById(R.id.categ);

       // imageView = (ImageView)view.findViewById(R.id.imageView);

    }
}
