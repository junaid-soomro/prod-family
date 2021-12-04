package com.example.dark.prod_family_project.Activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.dark.prod_family_project.Adapters.TabAdapter;
import com.example.dark.prod_family_project.CustomerArea.edit_profile;
import com.example.dark.prod_family_project.CustomerArea.history;
import com.example.dark.prod_family_project.CustomerArea.products;
import com.example.dark.prod_family_project.Product_family.new_orders;
import com.example.dark.prod_family_project.Product_family.order_history;
import com.example.dark.prod_family_project.R;

public class Orders extends AppCompatActivity {


    private TabAdapter tabAdapter;

    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
//this is comment
        tabAdapter = new TabAdapter(getSupportFragmentManager());

        viewPager =(ViewPager)findViewById(R.id.container);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }
    private void setupViewPager(ViewPager viewPager){

        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addfragment(new new_orders(),"new_orders");
        adapter.addfragment(new order_history(),"order_history");
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,Prod_Family.class));
    }
}
