package com.example.dark.prod_family_project.VolleyRequests;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abd on 23-Jan-18.
 */

public class Add_edit_productsRequest extends StringRequest {

    private Map<String,String> parameters;

    //for Editing product
    public Add_edit_productsRequest(String id, String name, String category,String rate, String image, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, new Constants().ADD_EDIT_PRODUCTS, listener, errorListener);
        parameters = new HashMap<>();
        parameters.put("id",id);
        parameters.put("name",name);
        parameters.put("category",category);
        parameters.put("rate",rate);
        parameters.put("image",image);
    }

    //for Adding product
    public Add_edit_productsRequest(String name, String category,String rate, String image,String user_id,String lol, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, new Constants().ADD_EDIT_PRODUCTS, listener, errorListener);
        parameters = new HashMap<>();
        parameters.put("name",name);
        parameters.put("category",category);
        parameters.put("rate",rate);
        parameters.put("image",image);
        parameters.put("user_id",user_id);
    }

    //for Deleting product
    public Add_edit_productsRequest(String ID, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, new Constants().ADD_EDIT_PRODUCTS, listener, errorListener);
        parameters=new HashMap<>();
        parameters.put("delete",ID);

    }


    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }



}
