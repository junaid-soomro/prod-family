package com.example.dark.prod_family_project.VolleyRequests;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abd on 19-Jan-18.
 */

public class ProductsRequest extends StringRequest {

    private Map<String, String> parameters;

    public ProductsRequest(String category, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST,new Constants().FETCH_PRODUCTS,listener,errorListener);

        parameters = new HashMap<>();
        parameters.put("category",category);
    }

    public ProductsRequest(String search,String nothing, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST,new Constants().FETCH_PRODUCTS,listener,errorListener);

        parameters = new HashMap<>();
        parameters.put("query",search);
    }


    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
