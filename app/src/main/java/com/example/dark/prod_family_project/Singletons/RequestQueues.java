package com.example.dark.prod_family_project.Singletons;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by abd on 17-Jan-18.
 */

public class RequestQueues {
    private static RequestQueues requestQueues;
    private RequestQueue requestQueue;
    private static Context mctx;

    private RequestQueues(Context ctx) {
        this.mctx = ctx;
        this.requestQueue=getRequestQueue();
    }
    public RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
        {
            requestQueue = Volley.newRequestQueue(mctx.getApplicationContext());

        }
        return requestQueue;
    }

    public static synchronized RequestQueues getInstance(Context context){

        if(requestQueues==null)
        {
            requestQueues = new RequestQueues(context);
        }
        return requestQueues;

    }
    public<T> void addToRequestQue(Request<T> request){
        requestQueue.add(request);
    }

}
