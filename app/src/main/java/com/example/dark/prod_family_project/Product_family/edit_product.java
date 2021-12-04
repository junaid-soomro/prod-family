package com.example.dark.prod_family_project.Product_family;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.UnicodeSetSpanner;
import android.net.Uri;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.example.dark.prod_family_project.Activities.Login;
import com.example.dark.prod_family_project.Activities.Prod_Family_prod_edit;
import com.example.dark.prod_family_project.Activities.Register;
import com.example.dark.prod_family_project.Adapters.products_for_edit;
import com.example.dark.prod_family_project.CustomerArea.ViewProduct;
import com.example.dark.prod_family_project.Models.product_list;
import com.example.dark.prod_family_project.R;
import com.example.dark.prod_family_project.SessionManager.SessionManager;
import com.example.dark.prod_family_project.Singletons.RequestQueues;
import com.example.dark.prod_family_project.VolleyRequests.Add_edit_productsRequest;
import com.example.dark.prod_family_project.VolleyRequests.Constants;
import com.example.dark.prod_family_project.VolleyRequests.FetchProductsRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by abd on 24-Jan-18.
 */

public class edit_product extends Fragment {

    private static final String TAG = "edit_product";


    products_for_edit adapter;

    ListView list;

    product_list product_list;


    int ID;

    ArrayList<product_list> products = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_product,container,false);
        initialize(view);

        fetchProducts();

        setcredentials(view);


        return view;
    }

    private void fetchProducts() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching product");
        progressDialog.setCancelable(false);
        progressDialog.show();


        final FetchProductsRequest request = new FetchProductsRequest(new SessionManager(getContext()).getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    int count = 0;

                    JSONObject jsonObject;
                    JSONArray array = new JSONArray(response);
                    while (count < array.length()) {
                        jsonObject = array.getJSONObject(count);

                        product_list = new product_list(jsonObject.getString("ID"), jsonObject.getString("Name"),
                                jsonObject.getString("Price"), jsonObject.getString("category"), jsonObject.getString("image"));
                        products.add(product_list);
                        count++;
                    }
                    setProduct(products);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Exception" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i(TAG, error.getMessage());
            }
        });
        RequestQueues.getInstance(getContext()).addToRequestQue(request);

        }


    private void setProduct(ArrayList<product_list> products) {

        this.products = products;
        adapter = new products_for_edit(products,getContext());
        list.setAdapter(adapter);

    }







    private void initialize(View view) {


        list = (ListView)view.findViewById(R.id.prod_edit_list);

    }

    private void setcredentials(View view) {


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose action");

                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {

                        Intent intent = new Intent(getContext(),Prod_Family_prod_edit.class);
                        intent.putExtra("NAME",products.get(i).getName());
                        intent.putExtra("CATEGORY",products.get(i).getCategory());
                        intent.putExtra("ID",products.get(i).getID());
                        intent.putExtra("PRICE",products.get(i).getPrice());
                        intent.putExtra("IMAGE",products.get(i).getImage());
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {

                        deleteProduct(i);
                    }
                });

                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.show();

            }
        });

    }

    private void deleteProduct(int i) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Are you sure? This action is irreversible.");
        ID = i;
        builder.setPositiveButton("Yes Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Please Wait");
                progressDialog.setMessage("Deleting product");
                progressDialog.setCancelable(false);
                progressDialog.show();


                Add_edit_productsRequest request = new Add_edit_productsRequest(products.get(ID).getID(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            if(new JSONObject(response).getBoolean("status")){
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                products.remove(ID);
                                adapter.notifyDataSetChanged();


                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Some error", Toast.LENGTH_SHORT).show();

                            }
                        }
                        catch (Exception e){
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();}

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                });
                RequestQueues.getInstance(getContext()).addToRequestQue(request);

            }
        });
        builder.setNegativeButton("Go back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    builder.show();


    }



}
