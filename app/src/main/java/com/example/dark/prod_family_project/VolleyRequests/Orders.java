package com.example.dark.prod_family_project.VolleyRequests;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abd on 01-Feb-18.
 */

public class Orders extends StringRequest {

    private Map<String, String> parameters;

    public Orders(String request,String id, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST,new Constants().ORDERS,listener,errorListener);

        parameters = new HashMap<>();
        parameters.put("request",request);
        parameters.put("id",id);
    }

    public Orders(String USER, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST,new Constants().ORDERS,listener,errorListener);

        parameters = new HashMap<>();
        parameters.put("USER",USER);
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
