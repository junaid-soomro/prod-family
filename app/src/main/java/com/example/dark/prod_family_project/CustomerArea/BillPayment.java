package com.example.dark.prod_family_project.CustomerArea;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.dark.prod_family_project.Activities.Customer_Dashboard;
import com.example.dark.prod_family_project.Activities.Login;
import com.example.dark.prod_family_project.R;
import com.example.dark.prod_family_project.SessionManager.SessionManager;
import com.example.dark.prod_family_project.Singletons.RequestQueues;
import com.example.dark.prod_family_project.VolleyRequests.CashOrderRequest;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class BillPayment extends AppCompatActivity {

    private static final String TAG = "Billpayment";

    int bill;
    String ProductID,payment_method,address,userid;

    public static final String PAYPAL_CLIENT_ID = "AbCo3ryKuececWzRgrLMnP7V0n0oyuzVhKyPH-_QroNdRrDVgRdgDHvfSGtYiO18UJdO8L-xZ37dwoy6";

    private Button paypal,cash;
    TextView amount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_payment);
        initialize();
        setvalues();
        function();

    }

    private void setvalues()
    {
        userid = new SessionManager(this).getId();
        bill = getIntent().getIntExtra("Bill",0);
        ProductID = getIntent().getStringExtra("ProdName");
        amount.setText(String.valueOf(bill));
    }

    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PAYPAL_CLIENT_ID);

    private void function() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delivery Address");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                                address = input.getText().toString();
                                payment_method = "cash";

                                submitrequest();

                    }
                });
                paypal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        address = input.getText().toString();
                        payment_method = "paypal";
                        pay();


                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Toast.makeText(BillPayment.this, "Can not proceed without address.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();

    }

    private void submitrequest() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Submitting order");
        progressDialog.setCancelable(false);
        progressDialog.show();
        CashOrderRequest request = new CashOrderRequest(ProductID, bill, userid, payment_method, address, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getBoolean("success")){
progressDialog.dismiss();
                        Toast.makeText(BillPayment.this, "Order submitted", Toast.LENGTH_SHORT).show();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(BillPayment.this, "Some error.", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Log.i(TAG, e.getMessage());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i(TAG, error.getMessage());
            }
        });

        RequestQueues.getInstance(this).addToRequestQue(request);

        startActivity(new Intent(this, Customer_Dashboard.class));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    if (confirm != null) {
                        try {

                            JSONObject jsonObject = new JSONObject(confirm.toJSONObject().toString(4));
                            JSONObject response = new JSONObject(jsonObject.getString("response"));

                            Toast.makeText(this, "Payment Successful transction Id:-" + response.getString("id"), Toast.LENGTH_SHORT).show();
                            submitrequest();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    private void pay() {
            PayPalPayment payment = new PayPalPayment(new BigDecimal("1.00"), "USD", "sample item",
                    PayPalPayment.PAYMENT_INTENT_SALE);

            Intent intent = new Intent(this, PaymentActivity.class);

            // send the same configuration for restart resiliency
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

            startActivityForResult(intent, 0);

        }




        private void initialize() {

            paypal = (Button)findViewById(R.id.paypal);
            cash = (Button)findViewById(R.id.cash);

            amount = (TextView)findViewById(R.id.amount);

        }



}
