package com.example.dark.prod_family_project.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.dark.prod_family_project.Adapters.RatingAdapter;
import com.example.dark.prod_family_project.Models.RatingModel;
import com.example.dark.prod_family_project.R;
import com.example.dark.prod_family_project.SessionManager.SessionManager;
import com.example.dark.prod_family_project.Singletons.RequestQueues;
import com.example.dark.prod_family_project.VolleyRequests.Constants;
import com.example.dark.prod_family_project.VolleyRequests.RatingRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Rating extends AppCompatActivity {

    ListView listView;

    RatingModel model;
    RatingAdapter adapter;

    String rating;

    ArrayList<RatingModel> arrayList = new ArrayList<>();

    RatingBar ratingBar;

    int id;


    View v;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Rating.this,Customer_Dashboard.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        listView = (ListView)findViewById(R.id.rating_list);

        work();
        v=getLayoutInflater().inflate(R.layout.ratingbarlayout, null);
        ratingBar = v.findViewById(R.id.ratingg);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = String.valueOf(ratingBar.getRating());
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                id = i;
                AlertDialog.Builder builder = new AlertDialog.Builder(Rating.this);


                builder.setView(v);
                builder.setTitle("Rate this seller.");

                builder.setPositiveButton("Rate", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                                makeReqst(id);
                    }
                });
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                    }
                });
                builder.show();
            }
        });

    }

    private void makeReqst(int id) {
        final ProgressDialog progressDialog = new ProgressDialog(Rating.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Rating seller");
        progressDialog.setCancelable(true);
        progressDialog.show();

        RatingRequest request = new RatingRequest(arrayList.get(id).getId(), rating,new SessionManager(Rating.this).getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.i("Response", response.toString());
                try {
                    if(new JSONObject(response).getBoolean("status")){
                        Toast.makeText(Rating.this, "Rated.", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(Rating.this,Rating.class));
                    }else{
                        Toast.makeText(Rating.this, "Unable to rate salon.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.i("Exception", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("Volley exception", error.getMessage());
            }
        });
        RequestQueues.getInstance(Rating.this).addToRequestQue(request);

    }

    private void work() {
        final ProgressDialog progressDialog = new ProgressDialog(Rating.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Getting Sellers");
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, new Constants().rating, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
progressDialog.dismiss();
                Log.i("Response", response.toString());
                int count = 0;
                JSONObject object;

                while(count<response.length()){

                    try {
                        object = response.getJSONObject(count);
                        model = new RatingModel(object.getString("ID"),object.getString("seller_name"),object.getString("rate"));
                        arrayList.add(model);
                        count++;

                    } catch (JSONException e) {
                        Log.i("Exception", e.getMessage());
                    }
setValues(arrayList);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("Volley Exception", error.getLocalizedMessage());
            }
        });

        RequestQueues.getInstance(Rating.this).addToRequestQue(request);
    }

    private void setValues(ArrayList<RatingModel> arrayList) {
        this.arrayList = arrayList;
        adapter = new RatingAdapter(this.arrayList,Rating.this);
        listView.setAdapter(adapter);
    }
}
