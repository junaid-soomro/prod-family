package com.example.dark.prod_family_project.VolleyRequests;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abd on 02-Apr-18.
 */

public class FetchProductsRequest extends StringRequest {

    Map<String,String> parameters;

    public FetchProductsRequest(String ID, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, new Constants().fetch_prod_seller, listener, errorListener);
        parameters=new HashMap<>();
        parameters.put("user_id",ID);

    }


    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
