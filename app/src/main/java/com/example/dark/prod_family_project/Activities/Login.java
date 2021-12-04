package com.example.dark.prod_family_project.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.dark.prod_family_project.*;
import com.example.dark.prod_family_project.SessionManager.SessionManager;
import com.example.dark.prod_family_project.VolleyRequests.LoginRequest;
import com.example.dark.prod_family_project.R;
import com.example.dark.prod_family_project.Singletons.RequestQueues;

import org.json.JSONObject;

public class Login extends AppCompatActivity {


    private String username,password;

    private TextInputLayout til_username,til_password;
    private Button login;
    private TextView tv_reg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        askpermission();
        checkSession();
        initialize();
        login();


    }
    @Override
    public void onBackPressed(){


        finishAffinity();


    }
    private void askpermission() {
        ActivityCompat.requestPermissions(Login.this,new String[]{Manifest.permission.INTERNET},0);
        ActivityCompat.requestPermissions(Login.this,new String[]{Manifest.permission.MANAGE_DOCUMENTS},1);
        ActivityCompat.requestPermissions(Login.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
    }

    private void checkSession() {

        if(new SessionManager(Login.this).CheckIfSessionExist()){

            if(new SessionManager(Login.this).getType().equals("admin")){

                startActivity(new Intent(Login.this,AdminDashboard.class));
                finish();
            }
            else if(new SessionManager(Login.this).getType().equals("Customer")){

                startActivity(new Intent(Login.this,Customer_Dashboard.class));
                finish();
            }else
            {

                startActivity(new Intent(Login.this,Prod_Family.class));
                finish();
            }


        }

    }


    public void login(){

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = til_username.getEditText().getText().toString();
                password = til_password.getEditText().getText().toString();
                if(validation(username,password))
                {
                    final ProgressDialog progressDialog = new ProgressDialog(Login.this);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setMessage("Logging You In");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    LoginRequest loginRequest = new LoginRequest(username, password, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("Login Response", response);
                            progressDialog.dismiss();

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success")) {

                                    if(jsonObject.getString("Status").equals("blocked")){
                                        Toast.makeText(Login.this, "Account blocked", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    new SessionManager(Login.this,jsonObject.getString("ID"),jsonObject.getString("Name"),
                                            jsonObject.getString("Email"),jsonObject.getString("Type"),jsonObject.getString("image"));


                                    Toast.makeText(Login.this, "Login success", Toast.LENGTH_SHORT).show();


                                    if(jsonObject.getString("Type").equals("admin")){

                                        startActivity(new Intent(Login.this,AdminDashboard.class));
                                        finish();
                                        return;
                                    }else if(jsonObject.getString("Type").equals("Customer")){

                                        startActivity(new Intent(Login.this,Customer_Dashboard.class));
                                        finish();
                                        return;
                                    }
                                    Toast.makeText(Login.this, jsonObject.getString("Type"), Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Login.this,Prod_Family.class));
                                        finish();
                                        return;
                                } else {
                                    if(jsonObject.getString("status").equals("INVALID"))
                                        Toast.makeText(Login.this, "User Not Found", Toast.LENGTH_SHORT).show();
                                    else{

                                        Toast.makeText(Login.this, "Passwords dont match.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(Login.this, "Bad Response From Server "+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            if (error instanceof ServerError)
                                Toast.makeText(Login.this, "Server Error", Toast.LENGTH_SHORT).show();
                            else if (error instanceof TimeoutError)
                                Toast.makeText(Login.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                            else if (error instanceof NetworkError)
                                Toast.makeText(Login.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
        RequestQueues.getInstance(Login.this).addToRequestQue(loginRequest);
                }
            }
        });

        tv_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });


    }



    public void initialize()
    {

        til_username =(TextInputLayout)findViewById(R.id.email);
        til_password =(TextInputLayout)findViewById(R.id.password);

        login = (Button)findViewById(R.id.Login);

        tv_reg = (TextView)findViewById(R.id.Reg_User);


    }

    private boolean validation(String Name,String Pass){

        if(Name.equals(""))
        {
            til_username.setError("Enter a username");
            return false;

        }

        else if(Pass.equals(""))

        {

            til_password.setError("Enter a password");
            return false;

        }
        til_username.setErrorEnabled(false);
        til_password.setErrorEnabled(false);
        return true;
    }




}
