package com.example.restaurantorders;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final String TAG = "RecyclerViewAdapter";
    private ArrayList<Order> orderss;
    private Context mContext;


    public RecyclerViewAdapter(Context context,
                               ArrayList<Order> orders) {
        orderss = orders;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_order, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "OnBindViewHolder: called.");
        holder.orderdate.setText(orderss.get(position).getCreated_at());
        final String id = "#" + orderss.get(position).getId();
        String owner = "by " + orderss.get(position).getName();
        if (orderss.get(position).getDispatch_status() == 0) {
            holder.orderstatus.setTextColor(Color.GREEN);
        } else {
            holder.orderstatus.setText("Dispatched");
            holder.orderstatus.setTextColor(Color.RED);
        }
        holder.orderid.setText(id);
        holder.orderowner.setText(owner);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Onclicked: clicked on:" + orderss.get(position).getId());
                Toast.makeText(mContext, id, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mContext, SpecificOrder.class);
                intent.putExtra("text", id);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderss.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderid;
        TextView orderdate;
        TextView orderowner, orderstatus;
        LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderstatus = itemView.findViewById(R.id.orderstatus);
            orderdate = itemView.findViewById(R.id.orderdate);
            orderid = itemView.findViewById(R.id.orderid);
            orderowner = itemView.findViewById(R.id.orderowner);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }
}