package com.appsinventiv.toolsbazzaradmin.Activities.Customers;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Models.Customer;
import com.appsinventiv.toolsbazzaradmin.R;

import java.util.ArrayList;

/**
 * Created by AliAh on 28/11/2018.
 */

public class SellerListAdapter extends RecyclerView.Adapter<SellerListAdapter.ViewHolder> {
    Context context;
    ArrayList<SellerModel> itemList;
    SellerListCallbacks callbacks;

    public SellerListAdapter(Context context, ArrayList<SellerModel> itemList, SellerListCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks = callbacks;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_item_layout, parent, false);
        SellerListAdapter.ViewHolder viewHolder = new SellerListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SellerModel model = itemList.get(position);
        holder.name.setText(model.getVendorName());
        holder.info.setText("Phone: " + model.getPhone());
        if (model.isStatus()) {
            holder.switchh.setChecked(true);
        } else {
            holder.switchh.setChecked(false);
        }
        holder.switchh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                callbacks.onStatusChanged(model, b);
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ViewCustomer.class);
                i.putExtra("customerId", model.getUsername());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, info;
        Switch switchh;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            info = itemView.findViewById(R.id.info);
            switchh = itemView.findViewById(R.id.switchh);
        }
    }

    public interface SellerListCallbacks {
        public void onStatusChanged(SellerModel sellerModel, boolean status);

    }
}
