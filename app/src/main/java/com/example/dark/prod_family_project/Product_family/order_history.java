package com.example.dark.prod_family_project.Product_family;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.dark.prod_family_project.Adapters.ordersAdapter;
import com.example.dark.prod_family_project.Models.OrdersModels;
import com.example.dark.prod_family_project.R;
import com.example.dark.prod_family_project.SessionManager.SessionManager;
import com.example.dark.prod_family_project.Singletons.RequestQueues;
import com.example.dark.prod_family_project.VolleyRequests.Constants;
import com.example.dark.prod_family_project.VolleyRequests.SellerOrderRequest;
import com.squareup.picasso.Downloader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by abd on 01-Feb-18.
 */

public class order_history extends Fragment {

    private final String TAG = "order_history";
    ArrayList<OrdersModels> orders = new ArrayList<>();
    ordersAdapter adapter;

    ListView listView;

    OrdersModels ordersModels;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.orders_history,container,false);
        initialize(view);
        fetch();

        return view;
    }

    private void fetch() {
        SellerOrderRequest request = new SellerOrderRequest(new SessionManager(getContext()).getId(), "lol", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                int count = 0;
                JSONObject object;
                try{
                    JSONArray array = new JSONArray(response);
                    while(count<array.length()){

                        object = array.getJSONObject(count);
                        ordersModels = new OrdersModels(object.getString("Order_ID"),object.getString("user"),
                                object.getString("image"),object.getString("Name"),object.getString("order_status"),
                                object.getString("payment_type")
                                ,object.getString("Price"));
                        orders.add(ordersModels);
                        count++;
                    }
                    setOrders(orders);


                }catch (Exception e){
                    Log.i(TAG, e.getMessage().toString());
                    Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.getMessage().toString());
                Toast.makeText(getContext(), "Volley "+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueues.getInstance(getContext()).addToRequestQue(request);


    }
    private void setOrders(ArrayList<OrdersModels> orders) {
        this.orders = orders;
        adapter = new ordersAdapter(orders,getContext());
        listView.setAdapter(adapter);

    }
    private void initialize(View view) {

        listView = (ListView)view.findViewById(R.id.orders_history);
    }
}
