package com.example.restaurantorders;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class PendingOrders extends Fragment {

    private ArrayList<Order> orderss = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pending_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerviews);
        final RecyclerViewAdapter adapter = new RecyclerViewAdapter(this.getContext(), orderss);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        String url = "https://demo.kilimanjarofood.co.ke/api/v1/dispatch/orders";
        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());


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
                            for (int i = 0; i < orders.size(); i++) {
                                Order g = orders.get(i);
                                if (g.getDispatch_status() == 0) {
                                    orderss.add(g);
                                }
                            }
                            Order me = orders.get(1);
                            Log.d("response from api", me.getName());
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
                });
        requestQueue.add(objectRequest);
    }
}