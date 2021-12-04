package com.example.dark.prod_family_project.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.dark.prod_family_project.Adapters.categoriesAdapter;
import com.example.dark.prod_family_project.Models.all_categories;
import com.example.dark.prod_family_project.R;
import com.example.dark.prod_family_project.Singletons.RequestQueues;
import com.example.dark.prod_family_project.VolleyRequests.Add_edit_productsRequest;
import com.example.dark.prod_family_project.VolleyRequests.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class Prod_Family_prod_edit extends AppCompatActivity {

    Bitmap profilePicture;

    Uri filepath;

    FirebaseStorage storage;
    StorageReference storageReference;

    TextInputLayout name,price,image;

    Spinner category;

    ImageView pic;

    Button update;

    TextView ID,current_categ;

    String default_img_utl="https://firebasestorage.googleapis.com/v0/b/prodfamily-797c6.appspot.com/o/product_images.jpg?alt=media&token=6a8255bd-60e4-4074-a929-a52b42b8244f";

    String id,NAME,CATEGORY,PRICE,IMAGE;

    all_categories model_categories;  //Model classs

    Prod_Family prod_family = new Prod_Family();

    ArrayList<all_categories> fetched = new ArrayList<>();
    categoriesAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod__family_prod_edit);


        initialize();
        fetchcategories();
        setcredentials();
        EditProduct();

    }

    private void fetchcategories() {

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, new Constants().FETCH_CATEGORIES, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int count = 0;

                try{while (count<response.length()) {
                    JSONObject object = response.getJSONObject(count);
                    model_categories=new all_categories(object.getString("Category"));
                    fetched.add(model_categories);
                    count++;
                }
                    set_categories(fetched);
                }
                catch (Exception e){

                    Log.i("Exception Error",e.getMessage().toString() );

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("VolleyError",error.getMessage().toString() );
            }
        });
        RequestQueues.getInstance(Prod_Family_prod_edit.this).addToRequestQue(request);

    }

    private void set_categories(ArrayList<all_categories> fetched) {
        this.fetched = fetched;
        arrayAdapter = new categoriesAdapter(fetched,Prod_Family_prod_edit.this);
        category.setAdapter(arrayAdapter);
    }

    private void setcredentials() {


        id = getIntent().getStringExtra("ID");
        NAME = getIntent().getStringExtra("NAME");
        CATEGORY = getIntent().getStringExtra("CATEGORY");
        PRICE = getIntent().getStringExtra("PRICE");
        IMAGE = getIntent().getStringExtra("IMAGE");



        name.getEditText().setText(NAME);
        ID.setText(id);

        current_categ.setText("Current Category\n"+CATEGORY);


        price.getEditText().setText(PRICE);

        Picasso.with(Prod_Family_prod_edit.this).load(IMAGE).into(pic);

    }

    private void EditProduct() {

        pic.setOnClickListener(new View.OnClickListener() {
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
                CATEGORY = fetched.get(category.getSelectedItemPosition()).getCategories();
                PRICE = price.getEditText().getText().toString();
                id = ID.getText().toString();

                uploadIMAGE();

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null) {
            //Image Successfully Selected
            try {
                //parsing the Intent data and displaying it in the imageview
                Uri imageUri = data.getData();//Geting uri of the data
                InputStream imageStream = getContentResolver().openInputStream(imageUri);//creating an imputstrea
                profilePicture = BitmapFactory.decodeStream(imageStream);//decoding the input stream to bitmap
                pic.setImageBitmap(profilePicture);
                filepath = imageUri;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadIMAGE() {

        final ProgressDialog progressDialog = new ProgressDialog(Prod_Family_prod_edit.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Editing product");
        progressDialog.setCancelable(false);
        progressDialog.show();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().getRoot();

        if(filepath != null)
        {
            StorageReference ref = storageReference.child("product_images/");
            ref.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String image_url = taskSnapshot.getDownloadUrl().toString();
                            Add_edit_productsRequest request = new Add_edit_productsRequest(id, NAME, CATEGORY, PRICE, image_url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    try {

                                        if (new JSONObject(response).getBoolean("status")){

                                            Toast.makeText(Prod_Family_prod_edit.this, "Updated.", Toast.LENGTH_SHORT).show();
                                            finish();
                                            startActivity(new Intent(Prod_Family_prod_edit.this,Prod_Family.class));
                                        }
                                        else {
                                            Toast.makeText(Prod_Family_prod_edit.this, "Some error", Toast.LENGTH_SHORT).show();

                                        }
                                    }catch (Exception e){
                                        Toast.makeText(Prod_Family_prod_edit.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(prod_family, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            RequestQueues.getInstance(Prod_Family_prod_edit.this).addToRequestQue(request);
                            Toast.makeText(Prod_Family_prod_edit.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Prod_Family_prod_edit.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }else if(filepath==null){

            Add_edit_productsRequest request = new Add_edit_productsRequest(id, NAME, CATEGORY, PRICE, IMAGE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {

                        if (new JSONObject(response).getBoolean("status")){

                            Toast.makeText(Prod_Family_prod_edit.this, "Updated.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Prod_Family_prod_edit.this, "Some error", Toast.LENGTH_SHORT).show();

                        }
                    }catch (Exception e){
                        Toast.makeText(Prod_Family_prod_edit.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                }
            });
            RequestQueues.getInstance(Prod_Family_prod_edit.this).addToRequestQue(request);
            Toast.makeText(Prod_Family_prod_edit.this, "Image Uploaded", Toast.LENGTH_SHORT).show();

        }
        else {
            progressDialog.dismiss();
            Toast.makeText(Prod_Family_prod_edit.this, "Failed", Toast.LENGTH_SHORT).show();

        }




    }

    private void initialize() {


            name = (TextInputLayout)findViewById(R.id.up_prod_name);
            category = (Spinner) findViewById(R.id.categoory);
            price = (TextInputLayout)findViewById(R.id.up_prod_rate);

            pic = (ImageView)findViewById(R.id.edit_product_image);

            update = (Button)findViewById(R.id.update_prod);

            ID = (TextView)findViewById(R.id.edit_prod_ID);

            current_categ = (TextView)findViewById(R.id.current_categ);
    }


}
