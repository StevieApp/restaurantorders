package com.example.restaurantorders;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {
    private final String TAG = "RecyclerViewAdapter";
    private ArrayList<OrderItem> itemss;
    private Context mContext;


    public ItemRecyclerViewAdapter(Context context,
                                   ArrayList<OrderItem> items) {
        itemss = items;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_orderitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "OnBindViewHolder: called.");
        holder.itemnumber.setText(String.valueOf(position + 1));
        holder.itemname.setText(itemss.get(position).getProduct_name());
        if (itemss.get(position).getProducts_attribute_accompaniment() == null) {
            holder.accompany.setText("N/A");
        } else {
            holder.accompany.setText(itemss.get(position).getProducts_attribute_accompaniment());
        }
        if (itemss.get(position).getDescription() == null ||
                itemss.get(position).getDescription().equals("")) {
            holder.description.setText("N/A");
        } else {
            holder.description.setText(itemss.get(position).getDescription());
        }
        if (String.valueOf(itemss.get(position).getPrice()).equals("")) {
            holder.itempricing.setText("N/A");
        } else {
            holder.itempricing.setText(String.valueOf(itemss.get(position).getPrice()));
        }
        holder.quantity.setText(String.valueOf(itemss.get(position).getQuantity()));
    }

    @Override
    public int getItemCount() {
        return itemss.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemnumber,
                itemname,
                quantity,
                itempricing,
                accompany,
                description;
        LinearLayout singleorderitem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itempricing = itemView.findViewById(R.id.itempricing);
            itemname = itemView.findViewById(R.id.itemname);
            itemnumber = itemView.findViewById(R.id.itemnumber);
            quantity = itemView.findViewById(R.id.quantity);
            accompany = itemView.findViewById(R.id.accompany);
            description = itemView.findViewById(R.id.description);
            singleorderitem = itemView.findViewById(R.id.singleorderitem);

        }
    }
}
