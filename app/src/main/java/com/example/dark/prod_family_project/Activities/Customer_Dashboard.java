package com.example.dark.prod_family_project.Activities;

import android.content.Intent;
import android.provider.ContactsContract;
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

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dark.prod_family_project.Adapters.TabAdapter;
import com.example.dark.prod_family_project.AdminArea.Admin_fragment2;
import com.example.dark.prod_family_project.AdminArea.Admin_fragments;
import com.example.dark.prod_family_project.CustomerArea.edit_profile;
import com.example.dark.prod_family_project.CustomerArea.history;
import com.example.dark.prod_family_project.CustomerArea.products;
import com.example.dark.prod_family_project.R;
import com.example.dark.prod_family_project.SessionManager.SessionManager;

public class Customer_Dashboard extends AppCompatActivity {


    private Button categ,hostory,rating;

    private ImageView settings,logout;

    TextView logoname;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);
        inititalize();
        action();

    }


    private void action() {
        logoname.setText(new SessionManager(Customer_Dashboard.this).getName());

        categ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    intent = new Intent(Customer_Dashboard.this,customer_frame_for_fragments.class);
                    intent.putExtra("value","categories");
                    startActivity(intent);
                    finish();
            }
        });
        hostory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Customer_Dashboard.this,customer_frame_for_fragments.class);
                intent.putExtra("value","history");
                startActivity(intent);
                finish();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Customer_Dashboard.this,customer_frame_for_fragments.class);
                intent.putExtra("value","edit");
                startActivity(intent);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SessionManager(Customer_Dashboard.this).Logout();
                startActivity(new Intent(Customer_Dashboard.this,Login.class));
                finish();
            }
        });

        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Customer_Dashboard.this,Rating.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void inititalize() {

        categ = (Button)findViewById(R.id.categories);
        hostory = (Button)findViewById(R.id.orders_history);

        logoname = (TextView)findViewById(R.id.logoname);

        settings = (ImageView)findViewById(R.id.setngs);
        logout = (ImageView)findViewById(R.id.logoff);

        rating = (Button)findViewById(R.id.rating);
    }

}

