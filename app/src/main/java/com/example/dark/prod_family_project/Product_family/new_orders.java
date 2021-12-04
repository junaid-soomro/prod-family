package com.example.dark.prod_family_project.Product_family;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.dark.prod_family_project.Adapters.ordersAdapter;
import com.example.dark.prod_family_project.Models.OrdersModels;
import com.example.dark.prod_family_project.R;
import com.example.dark.prod_family_project.SessionManager.SessionManager;
import com.example.dark.prod_family_project.VolleyRequests.Orders;
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
import com.example.dark.prod_family_project.Singletons.RequestQueues;
import com.example.dark.prod_family_project.VolleyRequests.Add_edit_productsRequest;
import com.example.dark.prod_family_project.VolleyRequests.Constants;
import com.example.dark.prod_family_project.VolleyRequests.SellerOrderRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abd on 01-Feb-18.
 */

public class new_orders extends Fragment {

    private final String TAG = "new_orders";

    private ArrayList<OrdersModels> orders = new ArrayList<>();
    ordersAdapter adapter;

    OrdersModels ordersModels;

    ListView list;

    private String ID;

    private int position;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_orders,container,false);
        initialize(view);
        fetch();
        listOnclick();
        return view;
    }

    private void listOnclick() {

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                position=i;
                ID = orders.get(i).getOrderid();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Choose action");

                builder.setPositiveButton("Accept order", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(acceptorder(ID,i)) {

                            orders.remove(position);
                            adapter.notifyDataSetChanged();
                        }




                    }
                });
                builder.setNegativeButton("Reject Order", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(rejectorder(ID,i)) {
                            orders.remove(position);
                            adapter.notifyDataSetChanged();
                        }

                    }
                });
                builder.show();

            }
        });



    }

    private boolean rejectorder(String id,int i) {


        Orders req = new Orders("reject",id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i(TAG, response.toString());
                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getBoolean("success")){
                        Toast.makeText(getContext(), "Rejected order", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(), "Operation failed", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    Log.i(TAG, e.getMessage().toString());
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "volley"+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueues.getInstance(getContext()).addToRequestQue(req);
            return true;
    }

    private boolean acceptorder(String id,int i) {


        Orders req = new Orders("accept",id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, response.toString());
                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getBoolean("success")){

                        Toast.makeText(getContext(), "Accepted order", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(getContext(), "Operation failed", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    Log.i(TAG, e.getMessage().toString());
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "volley"+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueues.getInstance(getContext()).addToRequestQue(req);

return true;
    }

    private void fetch() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching orders");
        progressDialog.setCancelable(false);
        progressDialog.show();
        SellerOrderRequest request = new SellerOrderRequest(new SessionManager(getContext()).getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, response.toString());
                int count = 0;
                JSONObject object;

                try{
                    JSONArray array = new JSONArray(response);
                    while(count<array.length()){

                      object  = array.getJSONObject(count);
                      ordersModels = new OrdersModels(object.getString("Order_ID"),object.getString("user"),
                              object.getString("image"),object.getString("Name"),object.getString("order_status"),
                              object.getString("payment_type")
                              ,object.getString("Price"));
                        orders.add(ordersModels);
                    count++;
                    }
                        setOrders(orders);
progressDialog.dismiss();

                }catch (Exception e){
                    progressDialog.dismiss();
                    Log.i(TAG, e.getMessage().toString());
                    Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i(TAG, error.getMessage());
                Toast.makeText(getContext(), "Volley "+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueues.getInstance(getContext()).addToRequestQue(request);


    }

    private void setOrders(ArrayList<OrdersModels> orders) {
        this.orders = orders;
        adapter = new ordersAdapter(orders,getContext());
        list.setAdapter(adapter);
        if(adapter.getCount()==0){

            Toast.makeText(getContext(), "No orders", Toast.LENGTH_SHORT).show();
        }
    }

    private void initialize(View view) {

        list = (ListView)view.findViewById(R.id.listView);

    }
}
