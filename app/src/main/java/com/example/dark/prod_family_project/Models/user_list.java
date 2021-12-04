package com.example.dark.prod_family_project.Models;

/**
 * Created by abd on 18-Jan-18.
 */

public class user_list {

    private String name,email,user_type,user_status;

    public user_list(String name, String email, String user_type,String status) {
        this.user_status = status;
        this.name = name;
        this.email = email;
        this.user_type = user_type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUser_type() {
        return user_type;
    }
}
