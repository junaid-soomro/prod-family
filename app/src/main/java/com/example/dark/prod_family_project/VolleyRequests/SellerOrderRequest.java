package com.example.dark.prod_family_project.VolleyRequests;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abd on 03-Apr-18.
 */

public class SellerOrderRequest extends StringRequest {

    Map<String,String> parameters;

    public SellerOrderRequest(String ID, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, new Constants().ORDERS, listener, errorListener);
        parameters=new HashMap<>();
        parameters.put("user",ID);

    }
    public SellerOrderRequest(String ID,String lol, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, new Constants().ORDER_HISTORY, listener, errorListener);
        parameters=new HashMap<>();
        parameters.put("user",ID);

    }


    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }



}
