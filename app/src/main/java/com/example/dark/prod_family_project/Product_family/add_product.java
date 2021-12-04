package com.example.dark.prod_family_project.Product_family;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.dark.prod_family_project.Activities.Login;
import com.example.dark.prod_family_project.Activities.Prod_Family;
import com.example.dark.prod_family_project.Adapters.categoriesAdapter;
import com.example.dark.prod_family_project.Models.all_categories;
import com.example.dark.prod_family_project.R;
import com.example.dark.prod_family_project.SessionManager.SessionManager;
import com.example.dark.prod_family_project.Singletons.RequestQueues;
import com.example.dark.prod_family_project.VolleyRequests.Add_edit_productsRequest;
import com.example.dark.prod_family_project.VolleyRequests.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by abd on 23-Jan-18.
 */

public class add_product extends Fragment {


  private static final String TAG = "add_product";

  private TextInputLayout name,rate;

  private Button add_product;

    FirebaseStorage storage;
    StorageReference storageReference;


    private ImageView image;

    Bitmap product_picture;

  private String NAME,RATE,CATEGORY;
    private String IMGURL="https://firebasestorage.googleapis.com/v0/b/prodfamily-797c6.appspot.com/o/product_images.jpg?alt=media&token=6a8255bd-60e4-4074-a929-a52b42b8244f";

  private Spinner category;

  ArrayList<all_categories> fetched = new ArrayList<>();

  categoriesAdapter arrayAdapter;

  all_categories model_categories;
  Uri filepath;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_product,container,false);
        initialize(view);
        setcredentials();
        AddProduct();

        return view;
    }

    private void setcredentials() {

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
        RequestQueues.getInstance(getContext()).addToRequestQue(request);

    }

    private void set_categories(ArrayList<all_categories> fetched) {
        this.fetched = fetched;
        arrayAdapter = new categoriesAdapter(fetched,getContext());
        category.setAdapter(arrayAdapter);

    }


    private void AddProduct() {

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1000);
            }
        });

        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




    NAME = name.getEditText().getText().toString();
    RATE = rate.getEditText().getText().toString();
                CATEGORY = fetched.get(category.getSelectedItemPosition()).getCategories();

                uploadimage();
            }
        });

    }

    private void uploadimage() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Adding Product");
        progressDialog.setCancelable(false);
        progressDialog.show();


        if(filepath!=null) {

    storage = FirebaseStorage.getInstance();
    storageReference = storage.getReference();

    StorageReference ref = storageReference.child("product_images/" + UUID.randomUUID().toString());
    ref.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            String url = taskSnapshot.getDownloadUrl().toString();
            Add_edit_productsRequest request = new Add_edit_productsRequest(NAME, CATEGORY, RATE, url,new SessionManager(getContext())
            .getId(),"what",new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());
                        JSONObject object = new JSONObject(response);

                        if (object.getBoolean("status")) {

                            Toast.makeText(getContext(), "Product added.", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                            getContext().startActivity(new Intent(getContext(),Prod_Family.class));

                        } else {
                            Toast.makeText(getContext(), "Some error", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.i("Exception", e.getMessage().toString());
                        Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    if (error instanceof ServerError)
                        Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                    else if (error instanceof TimeoutError)
                        Toast.makeText(getContext(), "Connection Timed Out", Toast.LENGTH_SHORT).show();
                    else if (error instanceof NetworkError)
                        Toast.makeText(getContext(), "Bad Network Connection", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
            RequestQueues.getInstance(getContext()).addToRequestQue(request);


            Toast.makeText(getContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(getContext(), "Image upload failed", Toast.LENGTH_SHORT).show();
        }
    });
}else{
    progressDialog.dismiss();
    Toast.makeText(getContext(), "Please select an image.", Toast.LENGTH_SHORT).show();

}
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
                image.setImageBitmap(product_picture);
                filepath = imageUri;



            } catch (FileNotFoundException e) {
                Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void initialize(View view)
    {
        name = (TextInputLayout)view.findViewById(R.id.prod_name);
        rate = (TextInputLayout)view.findViewById(R.id.prod_rate);
        category = (Spinner)view.findViewById(R.id.drop_down_categories);


        add_product = (Button)view.findViewById(R.id.add_prod);

        image = (ImageView)view.findViewById(R.id.prod_image);



    }


}
