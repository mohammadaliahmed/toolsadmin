package com.appsinventiv.toolsbazzaradmin.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Models.ProductCountModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by AliAh on 07/09/2018.
 */

public class PendingProductsAdapter extends RecyclerView.Adapter<PendingProductsAdapter.ViewHolder> {
    Context context;
    ArrayList<ProductCountModel> itemList;
    IsPurchased isPurchased;

    public PendingProductsAdapter(Context context, ArrayList<ProductCountModel> itemList, IsPurchased isPurchased) {
        this.context = context;
        this.itemList = itemList;
        this.isPurchased = isPurchased;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pending_product_item_layout, parent, false);
        PendingProductsAdapter.ViewHolder viewHolder = new PendingProductsAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ProductCountModel model = itemList.get(position);
        Glide.with(context).load(model.getProduct().getThumbnailUrl()).into(holder.image);
        holder.title.setText(model.getProduct().getTitle());
        holder.quantity.setText("Total Quantity: " + model.getQuantity());
        holder.costPrice.setText("Cost Price: " + model.getProduct().getCostPrice());
        ArrayList<String> abc = new ArrayList<>();
        for (int i = 0; i < model.getOrderId().size(); i++) {
            abc.add("" + model.getOrderId().keySet().toArray()[i]);
        }
        holder.orderIds.setText("Order Ids: " + abc);
        holder.purchased.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isPurchased.addToArray(model.getProduct().getId());
                } else {
                    isPurchased.removeFromArray(model.getProduct().getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, quantity, orderIds, costPrice;
        ImageView image;
        Switch purchased;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            quantity = itemView.findViewById(R.id.quantity);
            orderIds = itemView.findViewById(R.id.orderIds);
            costPrice = itemView.findViewById(R.id.costPrice);
            image = itemView.findViewById(R.id.image);
            purchased = itemView.findViewById(R.id.purchased);

        }
    }

    public interface IsPurchased {
        public void addToArray(String id);

        public void removeFromArray(String id);
    }
}
