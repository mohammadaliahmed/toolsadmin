package com.appsinventiv.toolsbazzaradmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Products.EditProduct;
import com.appsinventiv.toolsbazzaradmin.Models.Product;
import com.appsinventiv.toolsbazzaradmin.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by AliAh on 20/06/2018.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    Context context;
    ArrayList<Product> productList;
    OnProductStatusChanged productStatusChanged;

    public ProductsAdapter(Context context, ArrayList<Product> productList, OnProductStatusChanged productStatusChanged) {
        this.context = context;
        this.productList = productList;
        this.productStatusChanged = productStatusChanged;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Product model = productList.get(position);


        holder.title.setText(model.getTitle());
        holder.wholesalePrice.setText("Rs. " + model.getWholeSalePrice()+"\nWhole sale");
        holder.retailPrice.setText("Rs. " + model.getRetailPrice()+"\nRetail Price");

        holder.subtitle.setText(model.getSubtitle());
        Glide.with(context).load(model.getThumbnailUrl()).into(holder.image);

        if (model.getIsActive().equalsIgnoreCase("true")) {
            holder.switchh.setChecked(true);
        } else if (model.getIsActive().equalsIgnoreCase("false")) {
            holder.switchh.setChecked(false);
        }
        else {
            holder.switchh.setChecked(false);
//            holder.switchh.setVisibility(View.GONE);
        }

        holder.switchh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (productStatusChanged != null) {
                    model.setIsActive("" + b);
                    productStatusChanged.onStatusChanged(compoundButton, model, b);

                }

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, EditProduct.class);
                i.putExtra("productId",model.getId());
                context.startActivity(i);
            }
        });


    }
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle, wholesalePrice,retailPrice;
        ImageView image;

        Switch switchh;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            wholesalePrice = itemView.findViewById(R.id.wholesalePrice);
            retailPrice = itemView.findViewById(R.id.retailPrice);

            image = itemView.findViewById(R.id.image);

            switchh = itemView.findViewById(R.id.switchh);


        }
    }

    public interface OnProductStatusChanged {
        public void onStatusChanged(View v, Product product, boolean status);
    }

}
