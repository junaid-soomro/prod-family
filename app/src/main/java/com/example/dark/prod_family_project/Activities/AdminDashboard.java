package com.example.dark.prod_family_project.Activities;

import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.example.dark.prod_family_project.Adapters.TabAdapter;
import com.example.dark.prod_family_project.AdminArea.*;
import com.example.dark.prod_family_project.CustomerArea.edit_profile;
import com.example.dark.prod_family_project.CustomerArea.history;
import com.example.dark.prod_family_project.CustomerArea.products;
import com.example.dark.prod_family_project.Models.user_list;
import com.example.dark.prod_family_project.R;
import com.example.dark.prod_family_project.*;

import java.util.ArrayList;

public class AdminDashboard extends AppCompatActivity {


    private TabAdapter tabAdapter;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        tabAdapter = new TabAdapter(getSupportFragmentManager());

        viewPager =(ViewPager)findViewById(R.id.container);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }




    private void setupViewPager(ViewPager viewPager){

        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addfragment(new Admin_fragments(),"Block_User");
        adapter.addfragment(new Admin_fragment2(),"Delete_prod");
        viewPager.setAdapter(adapter);

    }

}
