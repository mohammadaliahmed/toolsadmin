package com.appsinventiv.toolsbazzaradmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Models.VendorModel;
import com.appsinventiv.toolsbazzaradmin.R;

import java.util.ArrayList;

/**
 * Created by AliAh on 24/08/2018.
 */

public class VendorsListAdapter extends RecyclerView.Adapter<VendorsListAdapter.ViewHolder> {
    Context context;
    ArrayList<VendorModel> itemList;

    public VendorsListAdapter(Context context, ArrayList<VendorModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vendor_item_layout, parent, false);
        VendorsListAdapter.ViewHolder viewHolder = new VendorsListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final VendorModel model = itemList.get(position);
        holder.name.setText("Name: " + model.getVendorName());
        holder.phone.setText("Phone: " + model.getVendorPhone());
        holder.address.setText("Address: " + model.getVendorAddress());

        holder.dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + model.getVendorPhone()));
                context.startActivity(i);
            }
        });
        holder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://api.whatsapp.com/send?phone=" + model.getVendorPhone();
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone, address;
        ImageView dial, whatsapp;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            address = itemView.findViewById(R.id.address);
            dial = itemView.findViewById(R.id.phone_dial);
            whatsapp = itemView.findViewById(R.id.whatsapp);

        }
    }
}