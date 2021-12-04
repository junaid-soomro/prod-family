package com.example.dark.prod_family_project.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.example.dark.prod_family_project.CustomerArea.BillPayment;
import com.example.dark.prod_family_project.Models.product_list;
import java.util.ArrayList;
import com.example.dark.prod_family_project.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

/**
 * Created by abd on 19-Jan-18.
 */

public class products_fetch extends BaseAdapter {

    LayoutInflater inflater;
    Intent intent;

    FirebaseStorage storage;
    StorageReference storageReference;

    ArrayList<product_list> list = new ArrayList<>();
    Context context;
    Button order;

    int Amount=0;

    product_list product_list;
    TextView name,price;

    ImageLoader imageLoader;
    ImageView imageView;

    public products_fetch(ArrayList<product_list> list,Context context) {
        this.list = list;
        this.context = context;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {


        view = inflater.inflate(R.layout.gridview_product,null,false);

        setcredentials(view,position);


        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Quantity");

                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Amount = Integer.parseInt(input.getText().toString())*Integer.parseInt(list.get(position).getPrice().toString());
                        intent = new Intent(context,BillPayment.class);

                        intent.putExtra("ProdName",list.get(position).getID());
                        intent.putExtra("Bill",Amount);
                        context.startActivity(intent);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });


        return view;
    }

    private void setcredentials(View view,int position) {


        name = (TextView)view.findViewById(R.id.product_name);
        price = (TextView)view.findViewById(R.id.product_price);

        name.setText(list.get(position).getName());
        price.setText(list.get(position).getPrice());

        order = (Button)view.findViewById(R.id.order);

        imageView = (ImageView)view.findViewById(R.id.Category_product_image);

        try{
            Picasso.with(context).load(list.get(position).getImage()).into(imageView);

        }catch(Exception e){
            Log.d("Exception",""+e.getMessage().toString());
            Toast.makeText(context, ""+e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }

    }
}
