package com.kilimanjarofood.restaurantorders;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class SpecificOrder extends AppCompatActivity {

    TextView specificorderid,
            ownername,
            dispatchstatus,
            ownernumber,
            textownermail,
            textcreateddate,
            textcountry,
            textdeliveryplace,
            textdeliverynumber,
            textdeliverycharges,
            textpaytype,
            textpayamt,
            textpayableamt,
            textpaystatus,
            textdispatchstatus,
            textpayref,
            textpayno,
            textdispatchdate,
            textcart;
    Toolbar toolbar;
    CollapsingToolbarLayout framed;
    MaterialButton buttondispatch, buttoncanceldispatch;
    Order ord;
    ArrayList<OrderItem> items = new ArrayList<>();
    ProgressDialog progressDiag, progressDialog;

    boolean isConnected = true;
    String TAG = "Networking ME:";
    private ConnectivityManager.NetworkCallback connectivityCallback
            = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            isConnected = true;
            Log.d(TAG, "INTERNET CONNECTED");
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }

        @Override
        public void onLost(Network network) {
            Toast.makeText(getApplicationContext(), "No Internet Connection",
                    Toast.LENGTH_SHORT).show();
            isConnected = false;
            Log.d(TAG, "INTERNET LOST");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_order);

        specificorderid = findViewById(R.id.specificorderid);
        toolbar = findViewById(R.id.toolbar);
        ownername = findViewById(R.id.ownername);
        dispatchstatus = findViewById(R.id.dispatchstatus);
        ownernumber = findViewById(R.id.ownernumber);
        textcreateddate = findViewById(R.id.textcreateddate);
        textdeliveryplace = findViewById(R.id.textdeliveryplace);
        textdeliverynumber = findViewById(R.id.textdeliverynumber);
        textdeliverycharges = findViewById(R.id.textdeliverycharges);
        textpaytype = findViewById(R.id.textpaytype);
        textpayamt = findViewById(R.id.textpayamt);
        textpayableamt = findViewById(R.id.textpayableamt);
        textpaystatus = findViewById(R.id.textpaystatus);
        textdispatchstatus = findViewById(R.id.textdispatchstatus);
        textpayref = findViewById(R.id.textpayref);
        textpayno = findViewById(R.id.textpayno);
        textdispatchdate = findViewById(R.id.textdispatchdate);
        buttondispatch = findViewById(R.id.buttondispatch);
        buttoncanceldispatch = findViewById(R.id.buttoncanceldispatch);
        textcart = findViewById(R.id.textcart);
        framed = findViewById(R.id.framed);

        buttondispatch.setEnabled(false);
        buttoncanceldispatch.setEnabled(false);

        specificorderid.setText(getIntent().getStringExtra("text"));
        toolbar.setTitle(getIntent().getStringExtra("text"));
        ownername.setText(getIntent().getStringExtra("ownername"));
        dispatchstatus.setText(getIntent().getStringExtra("dispatchstatus").toUpperCase());
        if (Objects.requireNonNull(getIntent().getStringExtra("dispatchstatus")).equals("Pending")) {
            dispatchstatus.setTextColor(Color.RED);
            textdispatchstatus.setTextColor(Color.RED);
            framed.setBackgroundColor(Color.RED);
        } else {
            dispatchstatus.setTextColor(getResources().getColor(R.color.hound));
            textdispatchstatus.setTextColor(getResources().getColor(R.color.hound));
            framed.setBackgroundColor(getResources().getColor(R.color.hound));
        }
        ownernumber.setText(getIntent().getStringExtra("ownernumber"));
        textcreateddate.setText(getIntent().getStringExtra("textcreateddate"));
        textdeliveryplace.setText(getIntent().getStringExtra("textdeliveryplace"));
        textdeliverynumber.setText(getIntent().getStringExtra("textdeliverynumber"));
        String deliverycharges = "Ksh " + getIntent().getStringExtra("textdeliverycharges");
        textdeliverycharges.setText(deliverycharges);
        textpaytype.setText(getIntent().getStringExtra("textpaytype"));
        String payamt = "Ksh " + getIntent().getStringExtra("textpayamt");
        textpayamt.setText(payamt);
        String payableamt = "Ksh " + getIntent().getStringExtra("textpayableamt");
        textpayableamt.setText(payableamt);
        textpaystatus.setText(getIntent().getStringExtra("textpaystatus"));
        textdispatchstatus.setText(getIntent().getStringExtra("dispatchstatus"));
        textpayref.setText(getIntent().getStringExtra("textpayref"));
        textpayno.setText(getIntent().getStringExtra("textpayno"));
        textdispatchdate.setText(getIntent().getStringExtra("textdispatchdate"));


        String orderid = getIntent().getStringExtra("text");
        String orderidd = Objects.requireNonNull(orderid).replace("#", "");
        String url = "https://kilimanjarofood.co.ke/api/v1/dispatch/order?orderId="
                + orderidd;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        RecyclerView recyclerView = findViewById(R.id.recyclerviews);
        final ItemRecyclerViewAdapter adapter = new ItemRecyclerViewAdapter(this, items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false));

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDiag = new ProgressDialog(this);
        progressDiag.setMessage("Getting Cart Items...");
        progressDiag.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDiag.show();

        final JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        //Log.e("response from api", jsonObject.toString());
                        System.out.println(jsonObject.toString());
                        //Log.d("response from api", "onResponse: \n"
                        //       + jsonObject.toString());
                        try {
                            JSONObject j = jsonObject.getJSONObject("data");
                            Log.d("response from api", j.toString());
                            JSONObject rr = j.getJSONObject("orders");
                            Log.d("response from api", rr.toString());
                            Gson json = new Gson();
                            Order me = json.fromJson(rr.toString(), Order.class);
                            ord = me;
                            textdeliveryplace.setText(me.getDelivery_address());
                            textdeliverynumber.setText(String.valueOf(me.getDelivery_contact_phone_number()));
                            String pending = ("Pending").toUpperCase();
                            String dispatched = ("Dispatched").toUpperCase();
                            if (me.getDispatch_status() == 0) {
                                textdispatchstatus.setText(pending);
                                textdispatchstatus.setTextColor(getResources().getColor(R.color.colorAccent));
                                dispatchstatus.setText(pending);
                                framed.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                dispatchstatus.setTextColor(getResources().getColor(R.color.colorAccent));
                            } else {
                                framed.setBackgroundColor(getResources().getColor(R.color.hound));
                                textdispatchstatus.setText(dispatched);
                                textdispatchstatus.setTextColor(getResources().getColor(R.color.hound));
                                dispatchstatus.setText(dispatched);
                                dispatchstatus.setTextColor(getResources().getColor(R.color.hound));
                            }
                            Log.d("response from api", me.getName());
                            String dispatchtime;
                            if (me.getDispatch_time() == null || me.getDispatch_time().equals("")) {
                                dispatchtime = "Pending dispatch/Dispatch time not set";
                                textdispatchdate.setText(dispatchtime);
                            } else {
                                Date d;
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                DateFormat sd = new SimpleDateFormat("hh:mm a yyyy/MM/dd");
                                String trh = me.getCreated_at();
                                sdf.setLenient(false);
                                try {
                                    d = sdf.parse(trh);
                                    textdispatchdate.setText(sd.format(d));
                                } catch (ParseException e) {
                                    Log.d(TAG, e.getMessage());
                                }
                            }
                            if (me.getDispatch_status() == 0) {
                                buttondispatch.setEnabled(true);
                            } else {
                                buttoncanceldispatch.setEnabled(true);
                            }
                            items.addAll(me.getCart());
                            String Items = "Cart Items: " + items.size();
                            textcart.setText(Items);
                            progressDiag.hide();
                        } catch (JSONException e) {
                            Log.d("response from api", "paaaapiiii");
                            e.printStackTrace();
                            progressDiag.hide();
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("error from api", volleyError.toString());
                        System.out.println(volleyError.toString());
                        Log.d("error from api", "onErrorResponse: \n"
                                + volleyError.toString());
                        Toast.makeText(SpecificOrder.this, "Unable to load cart Items",
                                Toast.LENGTH_SHORT).show();
                        progressDiag.hide();
                    }
                }
        );
        requestQueue.add(objectRequest);


        buttondispatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new AlertDialog.Builder(SpecificOrder.this)
                        .setTitle("Dispatch order " + getIntent().getStringExtra("text"))
                        .setMessage("Do you really want to dispatch this order?")
                        .setIcon(R.drawable.ic_dispatched_24dp)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                progressDialog.setMessage("Dispatching...");
                                progressDialog.show();
                                String orderid = getIntent().getStringExtra("text");
                                String orderidd = Objects.requireNonNull(orderid)
                                        .replace("#", "");
                                String url = "https://kilimanjarofood.co.ke/api/v1/dispatch/order?orderId="
                                        + orderidd;
                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                                JsonObjectRequest objectRequest = new JsonObjectRequest(
                                        Request.Method.POST,
                                        url,
                                        null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject jsonObject) {
                                                System.out.println(jsonObject.toString());
                                                Toast.makeText(SpecificOrder.this,
                                                        "Order dispatched",
                                                        Toast.LENGTH_SHORT).show();
                                                finish();
                                                overridePendingTransition(0, 0);
                                                startActivity(getIntent());
                                                overridePendingTransition(0, 0);
                                                progressDialog.hide();
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError volleyError) {
                                                Log.e("error from api", volleyError.toString());
                                                System.out.println(volleyError.toString());
                                                Log.d("error from api", "onErrorResponse: \n"
                                                        + volleyError.toString());
                                                Toast.makeText(SpecificOrder.this,
                                                        "Unable to update", Toast.LENGTH_SHORT)
                                                        .show();

                                                progressDialog.hide();
                                            }
                                        }
                                );
                                requestQueue.add(objectRequest);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        buttoncanceldispatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new AlertDialog.Builder(SpecificOrder.this)
                        .setTitle("Cancel dispatch for order " + getIntent().getStringExtra("text"))
                        .setMessage("Do you really want to cancel dispatch?")
                        .setIcon(R.drawable.ic_queue_black_24dp)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                progressDialog.setMessage("Canceling Dispatch...");
                                progressDialog.show();
                                String orderid = getIntent().getStringExtra("text");
                                String orderidd = Objects.requireNonNull(orderid)
                                        .replace("#", "");
                                String url = "https://kilimanjarofood.co.ke/api/v1/dispatch/order?orderId="
                                        + orderidd;
                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                                JsonObjectRequest objectRequest = new JsonObjectRequest(
                                        Request.Method.POST,
                                        url,
                                        null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject jsonObject) {
                                                System.out.println(jsonObject.toString());
                                                Toast.makeText(SpecificOrder.this,
                                                        "Dispatching Cannot be Undone",
                                                        Toast.LENGTH_SHORT).show();
                                                progressDialog.hide();
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError volleyError) {
                                                Log.e("error from api", volleyError.toString());
                                                System.out.println(volleyError.toString());
                                                Log.d("error from api", "onErrorResponse: \n"
                                                        + volleyError.toString());
                                                Toast.makeText(SpecificOrder.this,
                                                        "Unable to update", Toast.LENGTH_SHORT).show();
                                                progressDialog.hide();
                                            }
                                        }
                                );
                                requestQueue.add(objectRequest);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }
    // to check if we are monitoring Network
    private boolean monitoringConnectivity = false;

    @Override
    public void onResume() {
        super.onResume();
        checkConnectivity();
    }

    @Override
    public void onPause() {
        // if network is being moniterd then we will unregister the network callback
        if (monitoringConnectivity) {
            final ConnectivityManager connectivityManager
                    = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            Objects.requireNonNull(connectivityManager).unregisterNetworkCallback(connectivityCallback);
            monitoringConnectivity = false;
        }

        progressDiag.dismiss();
        progressDialog.dismiss();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDiag.dismiss();
        progressDialog.dismiss();
    }

    // Method to check network connectivity in Main Activity
    private void checkConnectivity() {
        // here we are getting the connectivity service from connectivity manager
        final ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(
                Context.CONNECTIVITY_SERVICE);

        // Getting network Info
        // give Network Access Permission in Manifest
        final NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();

        // isConnected is a boolean variable
        // here we check if network is connected or is getting connected
        isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

        if (!isConnected) {
            // SHOW ANY ACTION YOU WANT TO SHOW
            // WHEN WE ARE NOT CONNECTED TO INTERNET/NETWORK
            Log.d(TAG, " NO NETWORK!");
// if Network is not connected we will register a network callback to  monitor network
            connectivityManager.registerNetworkCallback(
                    new NetworkRequest.Builder()
                            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                            .build(), connectivityCallback);
            monitoringConnectivity = true;
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, " NOW NETWORK!");
        }
    }

}
