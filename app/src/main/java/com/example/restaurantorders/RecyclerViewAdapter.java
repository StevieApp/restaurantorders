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
                intent.putExtra("ownername", orderss.get(position).getName());
                String dispatchstatus;
                if (orderss.get(position).getDispatch_status() == 0) {
                    dispatchstatus = "Pending";
                } else {
                    dispatchstatus = "Dispatched";
                }
                intent.putExtra("dispatchstatus", dispatchstatus);
                String number = "+" + orderss.get(position).getCountry_code()
                        + orderss.get(position).getMobile();
                intent.putExtra("ownernumber", number);
                intent.putExtra("textownermail", orderss.get(position).getEmail());
                intent.putExtra("textcreateddate", orderss.get(position).getCreated_at());
                intent.putExtra("textcountry", orderss.get(position).getDelivery_country());
                intent.putExtra("textdeliveryplace", orderss.get(position).getDelivery_address());
                intent.putExtra("textdeliverynumber",
                        String.valueOf(orderss.get(position).getDelivery_contact_phone_number()));
                intent.putExtra("textdeliverycharges",
                        String.valueOf(orderss.get(position).getDelivery_charge()));
                intent.putExtra("textpaytype", orderss.get(position).getPayment_details_type());
                intent.putExtra("textpayamt",
                        String.valueOf(orderss.get(position).getPayment_details_amount()));
                intent.putExtra("textpayableamt",
                        String.valueOf(orderss.get(position).getTotal()));
                intent.putExtra("textpaystatus", orderss.get(position).getPayment_details_status());
                if (orderss.get(position).getPayment_details_reference() == null ||
                        orderss.get(position).getPayment_details_reference().equals("")) {
                    intent.putExtra("textpayref", "N/A");
                } else {
                    intent.putExtra("textpayref", orderss.get(position).getPayment_details_reference());
                }
                if (orderss.get(position).getPayment_details_phone_number() == null ||
                        orderss.get(position).getPayment_details_phone_number().equals("")) {
                    intent.putExtra("textpayno", "N/A");
                } else {
                    intent.putExtra("textpayno",
                            String.valueOf(orderss.get(position).getPayment_details_phone_number()));
                }
                String dispatchtime;
                if (orderss.get(position).getDispatch_time() == null ||
                        orderss.get(position).getDispatch_time().equals("")) {
                    dispatchtime = "Pending dispatch/Dispatch time not set";
                } else {
                    dispatchtime = orderss.get(position).getDispatch_time();
                }
                intent.putExtra("textdispatchdate", dispatchtime);
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