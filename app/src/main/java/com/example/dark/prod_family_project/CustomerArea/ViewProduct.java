package com.example.dark.prod_family_project.CustomerArea;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.dark.prod_family_project.Activities.Login;
import com.example.dark.prod_family_project.Adapters.products_fetch;
import com.example.dark.prod_family_project.R;
import com.example.dark.prod_family_project.Models.product_list;
import com.example.dark.prod_family_project.*;
import com.example.dark.prod_family_project.Singletons.RequestQueues;
import com.example.dark.prod_family_project.VolleyRequests.Constants;
import com.example.dark.prod_family_project.VolleyRequests.ProductsRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.security.cert.CertPathBuilderSpi;
import java.util.ArrayList;

public class ViewProduct extends AppCompatActivity {

    //ArrayList<product_list> producsts = new ArrayList<>();

    String category;
    String query;
    Intent intent;
    GridView gridView_products;

    products_fetch adapter;

    product_list product_list;
    ArrayList<product_list> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        displayproducts();


    }

    private void displayproducts() {

        category = getIntent().getStringExtra("category");
        query = getIntent().getStringExtra("query");

        gridView_products = (GridView) findViewById(R.id.products);


        final ProgressDialog progressDialog = new ProgressDialog(ViewProduct.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching Products");
        progressDialog.setCancelable(false);
        progressDialog.show();
        if (query == null) {





            ProductsRequest productsRequest = new ProductsRequest(category, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        int count = 0;

                        JSONArray array = new JSONArray(response);
                        JSONObject jsonObject;


                        while (count < array.length()) {
                            jsonObject = array.getJSONObject(count);
                            product_list = new product_list(jsonObject.getString("ID"), jsonObject.getString("Name"), jsonObject.getString("Price")
                                    , jsonObject.getString("category"), jsonObject.getString("Image"));


                            products.add(product_list);
                            count++;
                        }
                        setproducts(products);
                    } catch (Exception e) {

                        Log.i("Exception", e.getMessage().toString());

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("VolletErrorproducts", error.getMessage());
                }
            });
            RequestQueues.getInstance(ViewProduct.this).addToRequestQue(productsRequest);
            progressDialog.dismiss();
        }
        else{

            ProductsRequest request = new ProductsRequest(query, "nothing", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONArray array = new JSONArray(response);
                        JSONObject jsonObject;

                        int count = 0;

                        while (count<array.length()){
                            jsonObject = array.getJSONObject(count);

                            product_list = new product_list(jsonObject.getString("ID"),jsonObject.getString("Name"),jsonObject.getString("Price")
                                    ,jsonObject.getString("category"), jsonObject.getString("image"));

                            products.add(product_list);
                            count++;
                        }
                        setproducts(products);
                    } catch (JSONException e) {
                        Log.i("Exception", e.getMessage());;
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("Exception", error.getMessage());
                }
            });

            RequestQueues.getInstance(ViewProduct.this).addToRequestQue(request);
            progressDialog.dismiss();




        }
    }
    private void setproducts(ArrayList<product_list> products){

        this.products = products;
        adapter = new products_fetch(products,ViewProduct.this);
        gridView_products.setAdapter(adapter);

        if(adapter.getCount()==0){

            Toast.makeText(this, "No products found.", Toast.LENGTH_SHORT).show();

        }
    }

    }

