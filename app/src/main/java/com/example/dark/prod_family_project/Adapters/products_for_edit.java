package com.example.dark.prod_family_project.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dark.prod_family_project.Models.product_list;
import com.example.dark.prod_family_project.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by abd on 24-Jan-18.
 */

public class products_for_edit extends BaseAdapter {

    LayoutInflater inflater;

    ArrayList<product_list> list = new ArrayList<>();
    Context context;

    FirebaseStorage storage;
    StorageReference storageReference;

    ImageView imageView;

    product_list product_list;
    TextView name,price,category;

    public products_for_edit(ArrayList<product_list> lists,Context context)
    {
        this.list = lists;
        this.context = context;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout.products_for_edit,null,false);


        setcredentials(view,position);


        return view;

    }

    private void setcredentials(View view,int position) {

        name = (TextView)view.findViewById(R.id.edit_prod_name);
        price = (TextView)view.findViewById(R.id.edit_prod_rate);
        category = (TextView)view.findViewById(R.id.edit_prod_category);
        imageView = (ImageView)view.findViewById(R.id.edit_prod_image1);

        name.setText(list.get(position).getName());
        price.setText(list.get(position).getPrice());
        category.setText(list.get(position).getCategory());

        try{
                    Picasso.with(context).load(list.get(position).getImage()).into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.noimage));
                        }
                    });



        }catch(Exception e){
            Log.d("Exception",""+e.getMessage().toString());
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.noimage));
        }

    }

}
