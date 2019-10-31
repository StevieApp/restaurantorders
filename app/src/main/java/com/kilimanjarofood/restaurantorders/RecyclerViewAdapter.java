package com.kilimanjarofood.restaurantorders;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final String TAG = "RecyclerViewAdapter";
    private ArrayList<Order> orderss;
    private Context mContext;
    String ff = "";


    public RecyclerViewAdapter(Context context, ArrayList<Order> orders) {
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

        Date d;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat sd = new SimpleDateFormat("hh:mm a yyyy/MM/dd");
        String trh = orderss.get(position).getCreated_at();
        sdf.setLenient(false);
        try {
            d = sdf.parse(trh);
            holder.orderdate.setText(sd.format(d));
        } catch (ParseException e) {
            Log.d(TAG, e.getMessage());
        }
        final String id = "#" + orderss.get(position).getId();
        String owner = "by " + orderss.get(position).getName();
        String disp = ("Dispatched").toUpperCase();
        String pend = ("Pending").toUpperCase();
        if (orderss.get(position).getDispatch_status() == 1) {
            holder.orderstatus.setText(disp);
            holder.orderstatus.setTextColor(holder.itemView.getContext()
                    .getResources().getColor(R.color.hound));
            holder.frame.setBackgroundColor(holder.itemView.getContext()
                    .getResources().getColor(R.color.hound));
        } else {
            holder.orderstatus.setTextColor(holder.itemView.getContext()
                    .getResources().getColor(R.color.colorAccent));
            holder.frame.setBackgroundColor(holder.itemView.getContext()
                    .getResources().getColor(R.color.colorAccent));
            holder.orderstatus.setText(pend);
        }
        holder.orderid.setText(id);
        String total = "Ksh " + orderss.get(position).getTotal();
        holder.ordertotal.setText(total);
        String add = orderss.get(position).getDelivery_address();
        String addlittle = add.substring(0, add.indexOf(","));
        holder.deliveryadd.setText(add);
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
                if (orderss.get(position).getMobile() == null ||
                        orderss.get(position).getMobile().isEmpty() ||
                        orderss.get(position).getMobile().equals("")) {
                    intent.putExtra("ownernumber", "Phone Number: N/A");
                } else {
                    intent.putExtra("ownernumber", number);
                }
                intent.putExtra("textownermail", orderss.get(position).getEmail());
                Date du;
                SimpleDateFormat sdfu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                DateFormat sdu = new SimpleDateFormat("hh:mm a yyyy/MM/dd");
                String trh = orderss.get(position).getCreated_at();
                sdfu.setLenient(false);
                try {
                    du = sdfu.parse(trh);
                    intent.putExtra("textcreateddate", sdu.format(du));
                } catch (ParseException e) {
                    Log.d(TAG, e.getMessage());
                }
                intent.putExtra("textcountry", orderss.get(position).getDelivery_country());
                intent.putExtra("textdeliveryplace",
                        orderss.get(position).getDelivery_address());
                intent.putExtra("textdeliverynumber",
                        String.valueOf(orderss.get(position).getDelivery_contact_phone_number()));
                intent.putExtra("textdeliverycharges",
                        String.valueOf(orderss.get(position).getDelivery_charge()));
                intent.putExtra("textpaytype",
                        orderss.get(position).getPayment_details_type());
                intent.putExtra("textpayamt",
                        String.valueOf(orderss.get(position).getPayment_details_amount()));
                intent.putExtra("textpayableamt",
                        String.valueOf(orderss.get(position).getTotal()));
                intent.putExtra("textpaystatus", orderss.get(position)
                        .getPayment_details_status());
                if (orderss.get(position).getPayment_details_reference() == null ||
                        orderss.get(position).getPayment_details_reference().equals("")) {
                    intent.putExtra("textpayref", "N/A");
                } else {
                    intent.putExtra("textpayref",
                            orderss.get(position).getPayment_details_reference());
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

        TextView orderid,
                orderdate,
                orderowner,
                orderstatus,
                ordertotal,
                deliveryadd;
        FrameLayout frame;
        LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderstatus = itemView.findViewById(R.id.orderstatus);
            orderdate = itemView.findViewById(R.id.orderdate);
            orderid = itemView.findViewById(R.id.orderid);
            orderowner = itemView.findViewById(R.id.orderowner);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            frame = itemView.findViewById(R.id.frame);
            ordertotal = itemView.findViewById(R.id.ordertotal);
            deliveryadd = itemView.findViewById(R.id.deliveryadd);
        }
    }
}