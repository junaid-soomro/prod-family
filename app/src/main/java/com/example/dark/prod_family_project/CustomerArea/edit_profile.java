package com.example.dark.prod_family_project.CustomerArea;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.dark.prod_family_project.Activities.Login;
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

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;


public class edit_profile extends Fragment {

    private static final String TAG="edit_profile";

    private TextInputLayout name,email,password,old_password;
    private Button update;

    String NAME,EMAIL,OLD,NEW,ID;

    FirebaseStorage storage;
    StorageReference storageReference;

    Bitmap product_picture;

    Uri filepath;


    ImageView imageView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_profile,container,false);
        initialize(view);
        getcredentials(view);
        return view;
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
                imageView.setImageBitmap(product_picture);
                filepath = imageUri;

                Toast.makeText(getContext(), "result is running", Toast.LENGTH_SHORT).show();


            } catch (FileNotFoundException e) {
                Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }



    private void initialize(View view) {

        name = (TextInputLayout)view.findViewById(R.id.edit_name);
        email = (TextInputLayout)view.findViewById(R.id.edit_email);
        password = (TextInputLayout)view.findViewById(R.id.edit_password);
        old_password = (TextInputLayout)view.findViewById(R.id.old_password);

        update = (Button)view.findViewById(R.id.update_prod);

        imageView = (ImageView)view.findViewById(R.id.edit_image);

    }


    private void getcredentials(View view) {

        setcredentials();

        imageView.setOnClickListener(new View.OnClickListener() {
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
                OLD = old_password.getEditText().getText().toString();
                NEW = password.getEditText().getText().toString();
                ID = new SessionManager(getContext()).getId();


                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Please Wait");
                progressDialog.setMessage("Updating profile");
                progressDialog.setCancelable(false);
                progressDialog.show();

                if(filepath==null) {
                    EditProdile request = new EditProdile(new SessionManager(getContext()).getId(), name.getEditText().getText().toString(),
                            email.getEditText().getText().toString()
                            , old_password.getEditText().getText().toString(),
                            password.getEditText().getText().toString(), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {


                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("PASSWORD")) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Incorrect current password.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                else if(jsonObject.getBoolean("success")){

                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();

                                }


                            } catch (Exception e) {

                                Log.i("Exception", e.getMessage().toString());
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Volley Exception", error.getMessage().toString());
                        }
                    });
                    RequestQueues.getInstance(getContext()).addToRequestQue(request);
                }else{

                    storage = FirebaseStorage.getInstance();
                    storageReference = storage.getReference();

                    StorageReference ref = storageReference.child("product_images/" + UUID.randomUUID().toString());
                    ref.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String url = taskSnapshot.getDownloadUrl().toString();
                            EditProdile request = new EditProdile(ID, NAME, EMAIL, OLD, NEW,url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getBoolean("PASSWORD")) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "Incorrect current password.", Toast.LENGTH_SHORT).show();
                                            return;
                                        }else if(jsonObject.getBoolean("success")){

                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();

                                        }

                                    } catch (Exception e) {

                                        Log.i("Exception", e.getMessage().toString());
                                    }


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("Volley Exception", error.getMessage().toString());
                                }
                            });
                            RequestQueues.getInstance(getContext()).addToRequestQue(request);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Image upload failed", Toast.LENGTH_SHORT).show();

                        }
                    });




                }

            }
        });
        }





    private void setcredentials() {


        try{


            name.getEditText().setText(new SessionManager(getContext()).getName());
            email.getEditText().setText(new SessionManager(getContext()).getEmail());

            Picasso.with(getContext()).load(new SessionManager(getContext()).getImage()).into(imageView);


        }catch(Exception e){
            Log.d("Exception",""+e.getMessage().toString());
            Toast.makeText(getContext(), ""+e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }
}
