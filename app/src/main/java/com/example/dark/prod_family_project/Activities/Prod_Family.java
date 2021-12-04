package com.example.dark.prod_family_project.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dark.prod_family_project.Adapters.categoriesAdapter;
import com.example.dark.prod_family_project.Holder.all_holders;
import com.example.dark.prod_family_project.Models.all_categories;
import com.example.dark.prod_family_project.Product_family.add_product;
import com.example.dark.prod_family_project.R;
import com.example.dark.prod_family_project.SessionManager.SessionManager;

import java.util.ArrayList;

public class Prod_Family extends AppCompatActivity {

    ArrayList<all_categories> fetched = new ArrayList<>();
    categoriesAdapter arrayAdapter;
    all_categories model_categories;

    Context ctx;

    private Button add_products,my_products,orders;
    private ImageView settings,logout;
    Intent intent;


    add_product add_product;

    all_holders holder;

    private TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod__family);
        initialize();
        intro();
    }


    private void intro() {

        name.setText(new SessionManager(Prod_Family.this).getName());

        add_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Prod_Family.this,seller_frame_for_fragment.class);
                intent.putExtra("value","add");
                startActivity(intent);
                finish();

            }
        });
        my_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Prod_Family.this,seller_frame_for_fragment.class);
                intent.putExtra("value","edit");
                startActivity(intent);
                finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SessionManager(Prod_Family.this).Logout();
                finish();
                startActivity(new Intent(Prod_Family.this,Login.class));

            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(Prod_Family.this,Orders.class));
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Prod_Family.this,seller_frame_for_fragment.class);
                intent.putExtra("value","settings");
                startActivity(intent);
                finish();
            }
        });

    }


    private void initialize() {

        add_products = (Button)findViewById(R.id.add_products);
        my_products = (Button)findViewById(R.id.my_products);
        orders = (Button)findViewById(R.id.Orders);
        logout = (ImageView)findViewById(R.id.logout_seller);

        settings = (ImageView)findViewById(R.id.SETTINGS);

        name = (TextView)findViewById(R.id.seller_name);
    }
}
