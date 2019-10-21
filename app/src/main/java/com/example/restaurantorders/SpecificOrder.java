package com.example.restaurantorders;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

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
            textdispatchdate;
    Toolbar toolbar;
    MaterialButton buttondispatch, buttoncanceldispatch;
    Order ord;
    ArrayList<OrderItem> items = new ArrayList<>();

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
        textownermail = findViewById(R.id.textownermail);
        textcreateddate = findViewById(R.id.textcreateddate);
        textcountry = findViewById(R.id.textcountry);
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

        buttondispatch.setEnabled(false);
        buttoncanceldispatch.setEnabled(false);

        specificorderid.setText(getIntent().getStringExtra("text"));
        toolbar.setTitle(getIntent().getStringExtra("text"));
        ownername.setText(getIntent().getStringExtra("ownername"));
        dispatchstatus.setText(getIntent().getStringExtra("dispatchstatus"));
        if (Objects.requireNonNull(getIntent().getStringExtra("dispatchstatus")).equals("Pending")) {
            dispatchstatus.setTextColor(Color.GREEN);
            textdispatchstatus.setTextColor(Color.GREEN);
        } else {
            dispatchstatus.setTextColor(Color.RED);
            textdispatchstatus.setTextColor(Color.RED);
        }
        ownernumber.setText(getIntent().getStringExtra("ownernumber"));
        textownermail.setText(getIntent().getStringExtra("textownermail"));
        textcreateddate.setText(getIntent().getStringExtra("textcreateddate"));
        textcountry.setText(getIntent().getStringExtra("textcountry"));
        textdeliveryplace.setText(getIntent().getStringExtra("textdeliveryplace"));
        textdeliverynumber.setText(getIntent().getStringExtra("textdeliverynumber"));
        textdeliverycharges.setText(getIntent().getStringExtra("textdeliverycharges"));
        textpaytype.setText(getIntent().getStringExtra("textpaytype"));
        textpayamt.setText(getIntent().getStringExtra("textpayamt"));
        textpayableamt.setText(getIntent().getStringExtra("textpayableamt"));
        textpaystatus.setText(getIntent().getStringExtra("textpaystatus"));
        textdispatchstatus.setText(getIntent().getStringExtra("dispatchstatus"));
        textpayref.setText(getIntent().getStringExtra("textpayref"));
        textpayno.setText(getIntent().getStringExtra("textpayno"));
        textdispatchdate.setText(getIntent().getStringExtra("textdispatchdate"));


        String orderid = getIntent().getStringExtra("text");
        String orderidd = Objects.requireNonNull(orderid).replace("#", "");
        String url = "https://demo.kilimanjarofood.co.ke/api/v1/dispatch/order?orderId="
                + orderidd;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        RecyclerView recyclerView = findViewById(R.id.recyclerviews);
        final ItemRecyclerViewAdapter adapter = new ItemRecyclerViewAdapter(this, items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


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
                            String pending = "Pending";
                            String dispatched = "Dispatched";
                            if (me.getDispatch_status() == 0) {
                                textdispatchstatus.setText(pending);
                                textdispatchstatus.setTextColor(Color.GREEN);
                                dispatchstatus.setText(pending);
                                dispatchstatus.setTextColor(Color.GREEN);
                            } else {
                                textdispatchstatus.setText(dispatched);
                                textdispatchstatus.setTextColor(Color.RED);
                                dispatchstatus.setText(dispatched);
                                dispatchstatus.setTextColor(Color.RED);
                            }
                            Log.d("response from api", me.getName());
                            String dispatchtime;
                            if (me.getDispatch_time() == null || me.getDispatch_time().equals("")) {
                                dispatchtime = "Pending dispatch/Dispatch time not set";
                                textdispatchdate.setText(dispatchtime);
                            } else {
                                textdispatchdate.setText(me.getDispatch_time());
                            }
                            if (me.getDispatch_status() == 0) {
                                buttondispatch.setEnabled(true);
                            } else {
                                buttoncanceldispatch.setEnabled(true);
                            }
                            items.addAll(me.getCart());
                        } catch (JSONException e) {
                            Log.d("response from api", "paaaapiiii");
                            e.printStackTrace();
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
                    }
                }
        );
        requestQueue.add(objectRequest);


        buttondispatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                progressDialog.show();
                String orderid = getIntent().getStringExtra("text");
                String orderidd = Objects.requireNonNull(orderid).replace("#", "");
                String url = "https://demo.kilimanjarofood.co.ke/api/v1/dispatch/order?orderId="
                        + orderidd;
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date date = new Date();
                String currentdate = formatter.format(date);
                ord.setDispatch_status(1);
                ord.setDispatch_time(currentdate);
                Gson gson = new Gson();
                String gee = gson.toJson(ord);
                Log.d("loooooooooooooooming", gee);
                JsonParser parser = new JsonParser();
                JsonObject json = (JsonObject) parser.parse(gee);
                JSONObject y;
                try {
                    y = new JSONObject(gee);
                    Log.d("boooooooooooooooming", y.toString());
                    JsonObjectRequest objectRequest = new JsonObjectRequest(
                            Request.Method.POST,
                            url,
                            y,
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
                                }
                            }
                    );
                    requestQueue.add(objectRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.hide();
                progressDialog.cancel();
            }
        });

        buttoncanceldispatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                progressDialog.show();
                String orderid = getIntent().getStringExtra("text");
                String orderidd = Objects.requireNonNull(orderid).replace("#", "");
                String url = "https://demo.kilimanjarofood.co.ke/api/v1/dispatch/order?orderId="
                        + orderidd;
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date date = new Date();
                String currentdate = formatter.format(date);
                ord.setDispatch_status(0);
                ord.setDispatch_time(currentdate);
                Gson gson = new Gson();
                String gee = gson.toJson(ord);
                Log.d("loooooooooooooooming", gee);
                JsonParser parser = new JsonParser();
                JsonObject json = (JsonObject) parser.parse(gee);
                JSONObject y;
                try {
                    y = new JSONObject(gee);
                    Log.d("boooooooooooooooming", y.toString());
                    JsonObjectRequest objectRequest = new JsonObjectRequest(
                            Request.Method.POST,
                            url,
                            y,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    System.out.println(jsonObject.toString());
                                    Toast.makeText(SpecificOrder.this,
                                            "Dispatching Cannot be Undone",
                                            Toast.LENGTH_SHORT).show();
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
                                }
                            }
                    );
                    requestQueue.add(objectRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.hide();
                progressDialog.cancel();
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
        super.onPause();
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
