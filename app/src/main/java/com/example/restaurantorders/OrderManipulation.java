package com.example.restaurantorders;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class OrderManipulation {
    private static ArrayList<Order> allorders;
    private static Order singleorder;

    public static ArrayList<Order> getAllOrders(Context context) {
        String url = "https://demo.kilimanjarofood.co.ke/api/v1/dispatch/orders";
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("response from api", jsonObject.toString());
                        System.out.println(jsonObject.toString());
                        Log.d("response from api", "onResponse: \n"
                                + jsonObject.toString());
                        try {
                            JSONObject j = jsonObject.getJSONObject("data");
                            Log.d("response from api", j.toString());
                            JSONArray rr = j.getJSONArray("orders");
                            Log.d("response from api", rr.toString());
                            Gson json = new Gson();
                            Type types = new TypeToken<ArrayList<Order>>() {
                            }.getType();
                            ArrayList<Order> orders = json.fromJson(rr.toString(), types);
                            allorders = orders;
                            Order me = orders.get(1);
                            Log.d("response from api", me.getName());
                        } catch (JSONException e) {
                            Log.d("response from api", "paaaapiiii");
                            e.printStackTrace();
                        }
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
        return allorders;
    }

    public static Order getSingleOrder(Context context, Integer orderid) {
        String url = "https://demo.kilimanjarofood.co.ke/api/v1/dispatch/order?orderId="
                + orderid.toString();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
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
                            singleorder = me;
                            Log.d("response from api", me.getName());
                        } catch (JSONException e) {
                            Log.d("response from api", "paaaapiiii");
                            e.printStackTrace();
                        }
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
        return singleorder;
    }
}
