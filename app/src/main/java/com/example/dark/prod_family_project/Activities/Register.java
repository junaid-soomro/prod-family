package com.example.dark.prod_family_project.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.dark.prod_family_project.R;
import com.example.dark.prod_family_project.VolleyRequests.RegisterRequest;
import com.example.dark.prod_family_project.Singletons.RequestQueues;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.UUID;

public class Register extends AppCompatActivity {

    private TextInputLayout email,name,password;
    RadioButton prod_fam,customer;

    FirebaseStorage storage;
    StorageReference storageReference;

    ImageView imageView;

    boolean IMAGE_STATUS = false;
    Uri filepath;

    Bitmap profilePicture;
    Button register;

    String imgurl = "http://www.meezotech.com/junaid/user_images/";

    String NAME,EMAIL,PASSWORD,TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        initialize();
        RegisterUser();
    }



    private void RegisterUser()
    {

      imageView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
              intent.setType("image/*");
              startActivityForResult(intent, 1000);
          }
      });

            register.setOnClickListener(new View.OnClickListener() {
                @Override


                public void onClick(View view) {

                    storage = FirebaseStorage.getInstance();
                    storageReference = storage.getReference();

                    NAME = name.getEditText().getText().toString();
                    EMAIL = email.getEditText().getText().toString();
                    PASSWORD = password.getEditText().getText().toString();

                    if(prod_fam.isChecked()){TYPE="prod_fam";}
                    else if(customer.isChecked())
                    {
                        TYPE="Customer";
                    }else{TYPE="admin";}

                    if(Validation(NAME,EMAIL,PASSWORD)) {

                        uploadIMAGE();

                }


            }



        });


    }

    private void uploadIMAGE() {

        if(filepath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(Register.this);
            progressDialog.setTitle("Please Wait");
            progressDialog.setMessage("Creating your account");
            progressDialog.setCancelable(false);
            progressDialog.show();
            StorageReference ref = storageReference.child("images/"+UUID.randomUUID().toString());
            ref.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            String url = taskSnapshot.getDownloadUrl().toString();

                            RegisterRequest registerRequest = new RegisterRequest(EMAIL, NAME, PASSWORD, TYPE, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i("Response", response);
                                    progressDialog.dismiss();
                                    try {
                                        if (new JSONObject(response).getBoolean("success")) {
                                            progressDialog.dismiss();
                                            Toast.makeText(Register.this, "Account Successfully Created", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(Register.this, "Something Has Happened. Please Try Again!", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    if (error instanceof ServerError)
                                        Toast.makeText(Register.this, "Server Error "+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    else if (error instanceof TimeoutError)
                                        Toast.makeText(Register.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                                    else if (error instanceof NetworkError)
                                        Toast.makeText(Register.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
                                }
                            });
                            RequestQueues.getInstance(Register.this).addToRequestQue(registerRequest);

                            Toast.makeText(Register.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

         }





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null) {
            //Image Successfully Selected
            try {
                //parsing the Intent data and displaying it in the imageview
                Uri imageUri = data.getData();//Geting uri of the data

                InputStream imageStream = getContentResolver().openInputStream(imageUri);//creating an imputstrea
                profilePicture = BitmapFactory.decodeStream(imageStream);//decoding the input stream to bitmap
                imageView.setImageBitmap(profilePicture);
                IMAGE_STATUS = true;
                filepath =imageUri;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void initialize() {

        email = (TextInputLayout) findViewById(R.id.Email);
        name = (TextInputLayout) findViewById(R.id.Name);
        password = (TextInputLayout) findViewById(R.id.Password);

        register = (Button) findViewById(R.id.Register);

        prod_fam = (RadioButton)findViewById(R.id.prod_family);
        customer = (RadioButton)findViewById(R.id.customer);

        imageView = (ImageView)findViewById(R.id.main_image);
    }

    private boolean Validation(String Name,String Email,String pass)
    {
        if(name.equals("")){
            name.setError("Name empty");
            return false;

        }
        if(email.equals("")){
            email.setError("EMail empty");
            return false;

        }
        if(pass.equals("")){
            password.setError("Pass empty");
            return false;
        }
        if(!(prod_fam.isChecked()) && !(customer.isChecked()) ){

            Toast.makeText(Register.this,"user type not selected",Toast.LENGTH_LONG).show();
            return false;
        }
        if(!IMAGE_STATUS) {
            Toast.makeText(this, "Select A Profile Picture", Toast.LENGTH_SHORT).show();
            return IMAGE_STATUS;

        }

        return IMAGE_STATUS;


    }

    }
