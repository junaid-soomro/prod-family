package com.example.dark.prod_family_project.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.dark.prod_family_project.Models.product_list;
import com.example.dark.prod_family_project.R;

import java.util.ArrayList;

/**
 * Created by abd on 05-Feb-18.
 */

public class products_search extends ArrayAdapter<product_list> {


    ArrayList<product_list> product_lists = new ArrayList<>();
    ArrayList<product_list> filteredproducts = new ArrayList<>();
    Context context;

    TextView NAME,CATEG;

    public  products_search(ArrayList<product_list> list,Context context){

        super(context,0,list);
        this.context = context;
        this.product_lists = list;

    }

    @Override
    public int getCount() {
        return filteredproducts.size();
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        product_list model =filteredproducts.get(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.search_layout,parent,false);

        NAME = (TextView)convertView.findViewById(R.id.prodNAME);
        CATEG = (TextView)convertView.findViewById(R.id.prodCATEGORY);
        NAME.setText(model.getName());
        CATEG.setText(model.getCategory());

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new product_filter(this,product_lists);
    }

    private class product_filter extends Filter{

        products_search adapter;
        ArrayList<product_list> original = new ArrayList<>();
        ArrayList<product_list> filtered = new ArrayList<>();

        public product_filter(products_search adapter,ArrayList<product_list> list){
            super();
            this.adapter = adapter;
            this.original = list;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            filtered.clear();
            final FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length()==0){
                filtered.addAll(original);
            }else{

                final String filterpattern =charSequence.toString().toLowerCase().trim();
                for(final product_list product_list : original){
                    if(product_list.getName().toLowerCase().contains(filterpattern)){
                    filtered.add(product_list);
                    }
                }
            }
            results.values = filtered;
            results.count =filtered.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            adapter.filteredproducts.clear();
            adapter.filteredproducts.addAll((ArrayList<product_list>) filterResults.values);
            adapter.notifyDataSetChanged();
        }
    }

}
