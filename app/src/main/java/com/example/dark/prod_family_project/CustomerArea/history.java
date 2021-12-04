package com.example.dark.prod_family_project.CustomerArea;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.dark.prod_family_project.Adapters.ordersAdapter;
import com.example.dark.prod_family_project.Models.OrdersModels;
import com.example.dark.prod_family_project.R;
import com.example.dark.prod_family_project.SessionManager.SessionManager;
import com.example.dark.prod_family_project.Singletons.RequestQueues;
import com.example.dark.prod_family_project.VolleyRequests.Orders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class history extends Fragment {

    private static final String TAG="history";

    private ListView list;

    ArrayList<OrdersModels> orders = new ArrayList<>();

    OrdersModels ordersModels;

    ordersAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history,container,false);
        showOrders(view);
        return view;
    }

    private void showOrders(View view) {

        list = (ListView)view.findViewById(R.id.history);
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching your orders");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final Orders req = new Orders(new SessionManager(getContext()).getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONArray(response);
                    JSONObject object;
                    int count=0;
                    while (count<array.length()){
                        object = array.getJSONObject(count);

                        ordersModels = new OrdersModels(object.getString("Order_ID"),object.getString("user"),
                                object.getString("image"),object.getString("Name"),object.getString("order_status"),
                                object.getString("payment_type")
                                ,object.getString("Price"));

                        orders.add(ordersModels);
                        count++;

                    }
                    setOrders(orders);
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Log.i(TAG, e.getMessage());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();Log.i(TAG, error.getMessage());
            }
        });

        RequestQueues.getInstance(getContext()).addToRequestQue(req);
progressDialog.dismiss();
    }

    private void setOrders(ArrayList<OrdersModels> orders) {
        this.orders = orders;
        adapter = new ordersAdapter(this.orders,getContext());
        list.setAdapter(adapter);

        if(adapter.getCount()==0){

            Toast.makeText(getContext(), "No orders made.", Toast.LENGTH_SHORT).show();

        }
    }
}
