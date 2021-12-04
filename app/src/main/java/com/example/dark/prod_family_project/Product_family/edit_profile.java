package com.example.dark.prod_family_project.Product_family;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.dark.prod_family_project.R;
import com.example.dark.prod_family_project.SessionManager.SessionManager;
import com.example.dark.prod_family_project.Singletons.RequestQueues;
import com.example.dark.prod_family_project.VolleyRequests.EditProdile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by abd on 02-Feb-18.
 */

public class edit_profile extends Fragment{


    ImageView seller_img;

    TextInputLayout name,email,old,new_pass;

    FirebaseStorage storage;
    StorageReference storageReference;

    CheckBox passwd;

    Button update;

    Uri filepath;
    Bitmap product_picture;

    String ID,NAME,EMAIL,OLD,NEW;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seller_edit_profile,null,false);
        initialize(view);
        setcredentials();
        checkpass();
        update();
        return view;
    }

    private void checkpass() {


        old.setVisibility(View.GONE);
        new_pass.setVisibility(View.GONE);

        passwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(passwd.isChecked()) {
                    TranslateAnimation slide = new TranslateAnimation(0, 0, 500,0 );
                    slide.setDuration(1000);

                    old.setVisibility(View.VISIBLE);
                    old.startAnimation(slide);
                    new_pass.setVisibility(View.VISIBLE);
                    new_pass.startAnimation(slide);

                }else{

                    TranslateAnimation slide = new TranslateAnimation(0, 0, 0,1000 );
                    slide.setDuration(1000);

                    old.setVisibility(View.GONE);
                    old.startAnimation(slide);
                    new_pass.setVisibility(View.GONE);
                    new_pass.startAnimation(slide);
                }
            }
        });


    }

    private void update() {

        seller_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1000);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NAME = name.getEditText().getText().toString();
                EMAIL = email.getEditText().getText().toString();
                OLD = old.getEditText().getText().toString();
                NEW = new_pass.getEditText().getText().toString();
                ID = new SessionManager(getContext()).getId();
            if(Validation(NAME,EMAIL,OLD,NEW)){
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Please Wait");
                progressDialog.setMessage("Updating profile");
                progressDialog.setCancelable(false);
                progressDialog.show();



                if(filepath==null){

                    EditProdile req = new EditProdile(ID, NAME, EMAIL, OLD, NEW, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.i("Response", response.toString());
                            try {
                                JSONObject object = new JSONObject(response);

                                if(object.getBoolean("success")){
                            progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();

                                }else if(object.getBoolean("PASSWORD")){
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Incorrect account paasword", Toast.LENGTH_SHORT).show();


                                }


                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                Log.i("Exception", e.getMessage().toString());
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Log.i("Volley", error.getMessage());
                        }
                    }) ;
                    RequestQueues.getInstance(getContext()).addToRequestQue(req);



                }
                else{

                    storage = FirebaseStorage.getInstance();
                    storageReference = storage.getReference();

                    StorageReference ref = storageReference.child("product_images/" + UUID.randomUUID().toString());
                    ref.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String url = taskSnapshot.getDownloadUrl().toString();
                            EditProdile req = new EditProdile(ID, NAME, EMAIL, OLD, NEW,url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.i("Response", response.toString());
                                    try {

                                        JSONObject object = new JSONObject(response);

                                        if(object.getBoolean("success")){
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();

                                        }else if(object.getBoolean("PASSWORD")){
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "Incorrect account paasword", Toast.LENGTH_SHORT).show();


                                        }


                                    } catch (JSONException e) {
                                        progressDialog.dismiss();
                                        Log.i("Exception", e.getMessage().toString());
                                    }


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Log.i("Volley", error.getMessage().toString());
                                }
                            }) ;
                            RequestQueues.getInstance(getContext()).addToRequestQue(req);



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Image uplaod failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }   else{

                Toast.makeText(getContext(), "Human error", Toast.LENGTH_SHORT).show();

            }


            }
        });


    }

    private boolean Validation(String name,String email,String old,String new_pass){

        if(name.equals("") || email.equals("") || old.equals("")
                || new_pass.equals("")){
            Toast.makeText(getContext(), "one or more field'sempty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null) {
            //Image Successfully Selected
            try {
                //parsing the Intent data and displaying it in the imageview
                Uri imageUri = data.getData();//Geting uri of the data
                InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);//creating an imputstrea
                product_picture = BitmapFactory.decodeStream(imageStream);//decoding the input stream to bitmap
                seller_img.setImageBitmap(product_picture);
                filepath = imageUri;

                Toast.makeText(getContext(), "result is running", Toast.LENGTH_SHORT).show();


            } catch (FileNotFoundException e) {
                Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void setcredentials() {

        Picasso.with(getContext()).load(new SessionManager(getContext()).getImage()).into(seller_img);

        name.getEditText().setText(new SessionManager(getContext()).getName());
        email.getEditText().setText(new SessionManager(getContext()).getEmail());

    }

    private void initialize(View view) {

        seller_img = (ImageView)view.findViewById(R.id.seller_image);

        passwd = (CheckBox)view.findViewById(R.id.checkBox2);

        name = (TextInputLayout)view.findViewById(R.id.seller_name);
        email = (TextInputLayout)view.findViewById(R.id.seller_email);
        old = (TextInputLayout)view.findViewById(R.id.seller_old);
        new_pass = (TextInputLayout)view.findViewById(R.id.seller_new);

        update = (Button)view.findViewById(R.id.seller_update);

    }
}
