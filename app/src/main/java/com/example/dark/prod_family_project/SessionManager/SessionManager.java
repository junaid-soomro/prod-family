package com.example.dark.prod_family_project.SessionManager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.dark.prod_family_project.VolleyRequests.Constants;

/**
 * Created by abd on 21-Jan-18.
 */

public class SessionManager {

    private String id, name , email , type, image;
    SharedPreferences session ;



    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public SessionManager(){}

    public  SessionManager (Context c)
    {
        session= c.getSharedPreferences(new Constants().SESSION,Context.MODE_PRIVATE);
        this. id = session.getString("id",null);
        this.email= session.getString("email",null);
        this.name = session.getString("name",null);
        this.type = session.getString("type",null);
        this.image = session.getString("image",null);
    }

    public  SessionManager(Context c ,String id, String name , String email, String type,String image)
    {
        session= c.getSharedPreferences(new Constants().SESSION,Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = session.edit();
        editor.putString("id", id);
        editor.putString("name",name);
        editor.putString("email",email);
        editor.putString("type",type);
        editor.putString("image",image);

        editor.commit();

        new SessionManager(c);

    }

    public boolean CheckIfSessionExist(){
        if (session.contains("id"))
            return true;
        else
            return  false;

    }
    public void Logout (){
        SharedPreferences.Editor editor = session.edit();
        editor.clear();
        editor.commit();

        this.id=null;
        this.name=null;
        this.email=null;
        this.type=null;
    }




}
