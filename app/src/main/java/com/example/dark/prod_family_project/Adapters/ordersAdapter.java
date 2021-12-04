package com.example.dark.prod_family_project.Adapters;

import android.app.Notification;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dark.prod_family_project.*;
import com.example.dark.prod_family_project.Models.OrdersModels;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by abd on 01-Feb-18.
 */

public class ordersAdapter extends BaseAdapter {

    private ArrayList<OrdersModels> orders = new ArrayList<>();
    private Context context;

    ImageView order_image;

    TextView prod_name,user_name,pay_type,order_status,price;


    public ordersAdapter(ArrayList<OrdersModels> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int i) {
        return orders.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view= LayoutInflater.from(context).inflate(R.layout.order_adapter,null,false);
        initialize(view);
        setvalues(view,i);
        return view;

    }

    private void setvalues(View view, int position) {


        Picasso.with(context).load(orders.get(position).getProd_image()).into(order_image);

        prod_name.setText(orders.get(position).getProd_name());
        user_name.setText(orders.get(position).getUsername());
        pay_type.setText(orders.get(position).getPayment_type());
        price.setText(orders.get(position).getPrice());
        order_status.setText(orders.get(position).getStatus());


    }


    private void initialize(View view) {

        order_image = (ImageView)view.findViewById(R.id.order_image);

        prod_name = (TextView)view.findViewById(R.id.order_name);
        user_name = (TextView)view.findViewById(R.id.user_name_order);
        pay_type = (TextView)view.findViewById(R.id.payment_type);
        price = (TextView)view.findViewById(R.id.order_price);
        order_status = (TextView)view.findViewById(R.id.order_status);


    }
}
