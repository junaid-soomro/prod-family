package com.example.dark.prod_family_project.VolleyRequests;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abd on 04-Feb-18.
 */

public class CashOrderRequest extends StringRequest {

    private Map<String,String> parameters;

    public CashOrderRequest(String prodID,int price, String USER,String method,String address,Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST, new Constants().ORDER_SUBMIT, listener, errorListener);
        parameters = new HashMap<>();
        parameters.put("address",address);
        parameters.put("price",String.valueOf(price));
        parameters.put("USER",USER);
        parameters.put("method",method);
        parameters.put("PROD",prodID);
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
