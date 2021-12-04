package com.example.dark.prod_family_project.Activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.dark.prod_family_project.Product_family.add_product;
import com.example.dark.prod_family_project.Product_family.edit_product;
import com.example.dark.prod_family_project.Product_family.edit_profile;
import com.example.dark.prod_family_project.R;

public class seller_frame_for_fragment extends AppCompatActivity {


    FrameLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_frame_for_fragment);
        callingfragment();

    }

    @Override
    public void onBackPressed(){

        finish();
        startActivity(new Intent(seller_frame_for_fragment.this,Prod_Family.class));



    }

    private void callingfragment() {
        container = (FrameLayout)findViewById(R.id.container);
        String action = getIntent().getStringExtra("value");

        if(action.equals("add")) {
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            android.support.v4.app.Fragment mFragment = new add_product();
            transaction.replace(R.id.container, mFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }else if(action.equals("edit")){
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            android.support.v4.app.Fragment mFragment = new edit_product();
            transaction.replace(R.id.container, mFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }else if(action.equals("settings")){

            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            android.support.v4.app.Fragment mFragment = new edit_profile();
            transaction.replace(R.id.container, mFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }

    }
}
