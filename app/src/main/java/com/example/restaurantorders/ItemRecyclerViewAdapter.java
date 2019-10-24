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


    public ItemRecyclerViewAdapter(Context context, ArrayList<OrderItem> items) {
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
        holder.productid.setText(String.valueOf(itemss.get(position).getProduct_id()));
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
        String price = "Ksh " + itemss.get(position).getPrice();
        if (String.valueOf(itemss.get(position).getPrice()).equals("")) {
            holder.itempricing.setText("N/A");
        } else {
            holder.itempricing.setText(price);
        }
        holder.quantity.setText(String.valueOf(itemss.get(position).getQuantity()));
        if (String.valueOf(itemss.get(position).getAccompaniment_id()).equals("")) {
            holder.accompanimentid.setText("N/A");
        } else {
            holder.accompanimentid.setText(String
                    .valueOf(itemss.get(position).getAccompaniment_id()));
        }
        if (itemss.get(position).getProduct_attrubute_size() == null ||
                itemss.get(position).getProduct_attrubute_size().equals("")) {
            holder.attribute.setText("N/A");
        } else {
            holder.attribute.setText(itemss.get(position).getProduct_attrubute_size());
        }
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
                description,
                productid,
                accompanimentid,
                attribute;
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
            productid = itemView.findViewById(R.id.productid);
            accompanimentid = itemView.findViewById(R.id.accompanimentid);
            attribute = itemView.findViewById(R.id.attribute);
        }
    }
}
