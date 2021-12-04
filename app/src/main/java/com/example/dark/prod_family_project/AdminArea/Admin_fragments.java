package com.example.dark.prod_family_project.AdminArea;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.dark.prod_family_project.Activities.Login;
import com.example.dark.prod_family_project.Adapters.userlistfetch;
import com.example.dark.prod_family_project.Models.user_list;
import com.example.dark.prod_family_project.R;
import com.example.dark.prod_family_project.SessionManager.SessionManager;
import com.example.dark.prod_family_project.Singletons.RequestQueues;
import com.example.dark.prod_family_project.VolleyRequests.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by abd on 18-Jan-18.
 */

public class Admin_fragments extends Fragment {


    private static final String TAG="block_user";
    ArrayList<user_list> users = new ArrayList<>();


    FloatingActionButton fab;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    user_list items;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.blockuser,container,false);

        display_users();
        logout(view);
        initialize_recycler_view(view);

        blockuser();


        return view;
    }

    private void logout(View view) {

        fab = (FloatingActionButton)view.findViewById(R.id.logout_admin);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SessionManager(getContext()).Logout();
                getContext().startActivity(new Intent(getContext(),Login.class));
                getActivity().finish();
            }
        });

    }


    private void blockuser()
    {




    }

    private void initialize_recycler_view(View view){

        recyclerView = (RecyclerView)view.findViewById(R.id.blocklist);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);

    }

    public void display_users(){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, new Constants().FETCH_USERS, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        int count = 0;

                        try{while (count<response.length()) {
                            JSONObject object = response.getJSONObject(count);
                            items = new user_list(object.getString("Name"), object.getString("Email"),object.getString("TYPE"),object.getString("Status"));
                            users.add(items);

                            count++;
                        }
                            setusers(users);
                        }
                        catch (Exception e){}
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueues.getInstance(getContext()).addToRequestQue(jsonArrayRequest);
    }
    public ArrayList<user_list> setusers(ArrayList<user_list> itemsArrayList)
    {

        this.users=itemsArrayList;
        adapter = new userlistfetch(users);
        recyclerView.setAdapter(adapter);

        return null;
    }


}
