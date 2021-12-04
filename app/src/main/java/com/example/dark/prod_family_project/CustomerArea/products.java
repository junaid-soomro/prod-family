package com.example.dark.prod_family_project.CustomerArea;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.dark.prod_family_project.Adapters.products_fetch;
import com.example.dark.prod_family_project.Adapters.products_search;
import com.example.dark.prod_family_project.Models.all_categories;
import com.example.dark.prod_family_project.Models.user_list;
import com.example.dark.prod_family_project.R;
import com.example.dark.prod_family_project.Models.product_list;
import com.example.dark.prod_family_project.*;
import com.example.dark.prod_family_project.Singletons.RequestQueues;
import com.example.dark.prod_family_project.VolleyRequests.Constants;
import com.example.dark.prod_family_project.VolleyRequests.ProductsRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by abd on 18-Jan-18.
 */

public class products extends Fragment {

    private static final String TAG="products";

    all_categories send_categories;

    String query;

    Button SEARCH;
    Intent intent;

    ArrayList<String> categories = new ArrayList<>();
    ArrayList<product_list> products = new ArrayList<>();

    product_list model;
    products_fetch adapter;

    ArrayAdapter<String> arrayAdapter;

    products_search arrayAdapter2;

    private ListView listView;
    private AutoCompleteTextView search;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.products_main,container,false);
        initialize(view);
        getcategories();
        search(view);
        onSearch();
        onlistclick(view);
        return view;
    }

    private void onSearch() {

        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //product_list product_list = (product_list) adapterView.getItemAtPosition(i);
                TextView name = (TextView)view.findViewById(R.id.prodNAME);
                search.setText(name.getText().toString());
            }
        });


        SEARCH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                query = search.getText().toString();

                intent = new Intent(getContext(),ViewProduct.class);
                intent.putExtra("query",query);
                startActivity(intent);

            }
        });

    }

    private void search(View view){


        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, new Constants().FETCH_PRODUCTS, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int count = 0;

                try{while (count<response.length()) {
                    JSONObject object = response.getJSONObject(count);
                    model = new product_list(object.getString("Name"),object.getString("category"));
                    products.add(model);
                    count++;
                }
                    set_products(products);
                }
                catch (Exception e){

                    Log.i("Error",e.getMessage().toString() );

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("VolleyError",error.getMessage() );
            }
        });
        RequestQueues.getInstance(getContext()).addToRequestQue(request);
    }

    private void set_products(ArrayList<product_list> products)
    {
        this.products = products;
        arrayAdapter2 = new products_search(products,getContext());
        search.setThreshold(1);
        search.setAdapter(arrayAdapter2);


    }


    private void onlistclick(View view){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                intent = new Intent(getContext(),ViewProduct.class);
                intent.putExtra("category",categories.get(position).toString());
                startActivity(intent);


            }
        });



    }

    private void initialize(View view){

        SEARCH = (Button)view.findViewById(R.id.SEARCH);
        listView = (ListView)view.findViewById(R.id.categories);
        search = (AutoCompleteTextView)view.findViewById(R.id.search_input);
    }

    private void getcategories()                //also getting products

    {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching categories");
        progressDialog.setCancelable(false);
        progressDialog.show();
        arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,categories);
        arrayAdapter.clear();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, new Constants().FETCH_CATEGORIES, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int count = 0;

                try{while (count<response.length()) {
                    JSONObject object = response.getJSONObject(count);
                    categories.add(object.getString("Category"));
                    count++;
                }
                    set_categories(categories);
                }
                catch (Exception e){
progressDialog.dismiss();
                    Log.i("Error",e.getMessage().toString() );

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();Log.i("VolleyError",error.getMessage().toString() );
            }
        });
        RequestQueues.getInstance(getContext()).addToRequestQue(request);
    progressDialog.dismiss();
    }

    protected void set_categories(ArrayList<String> list){


        this.categories=list;

        listView.setAdapter(arrayAdapter);

    }





}
