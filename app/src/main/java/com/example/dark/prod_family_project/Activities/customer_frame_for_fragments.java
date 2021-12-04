package com.example.dark.prod_family_project.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.dark.prod_family_project.CustomerArea.history;
import com.example.dark.prod_family_project.CustomerArea.products;
import com.example.dark.prod_family_project.Product_family.add_product;
import com.example.dark.prod_family_project.Product_family.edit_product;
import com.example.dark.prod_family_project.Product_family.edit_profile;
import com.example.dark.prod_family_project.R;

public class customer_frame_for_fragments extends AppCompatActivity {


    FrameLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_frame_for_fragments);
        callingfragment();
    }

    private void callingfragment() {

        container = (FrameLayout)findViewById(R.id.container);
        String action = getIntent().getStringExtra("value");

        if(action.equals("categories")) {
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            android.support.v4.app.Fragment mFragment = new products();
            transaction.replace(R.id.container, mFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }else if(action.equals("history")){
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            android.support.v4.app.Fragment mFragment = new history();
            transaction.replace(R.id.container, mFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }else if(action.equals("edit")){

            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            android.support.v4.app.Fragment mFragment = new edit_profile();
            transaction.replace(R.id.container, mFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }


    }

    @Override
    public void onBackPressed(){

        finish();
        startActivity(new Intent(customer_frame_for_fragments.this,Customer_Dashboard.class));



    }

}
