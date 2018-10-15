package com.appsinventiv.toolsbazzaradmin.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Accounts.PurchaseModel;
import com.appsinventiv.toolsbazzaradmin.Models.PurchaseOrderModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by AliAh on 08/10/2018.
 */

public class FinalizedPOAdapter extends RecyclerView.Adapter<FinalizedPOAdapter.ViewHolder> {
    Context context;
    ArrayList<PurchaseOrderModel> itemList = new ArrayList<>();
    int type;
    ChangeLayout changeLayout;

    public FinalizedPOAdapter(Context context, ArrayList<PurchaseOrderModel> itemList, int type, ChangeLayout changeLayout) {
        this.context = context;
        this.itemList = itemList;
        this.type = type;
        this.changeLayout = changeLayout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.finalized_po_list_item_layout, parent, false);
        FinalizedPOAdapter.ViewHolder viewHolder = new FinalizedPOAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final PurchaseOrderModel model = itemList.get(position);
        float cost = 0;
        for (int i = 0; i < model.getProductsList().size(); i++) {

            cost = cost + (model.getProductsList().get(i).getProduct().getCostPrice()) * (model.getProductsList().get(i).getQuantity());
        }
        holder.cost.setText("Cost: Rs " + CommonUtils.getFormattedPrice(cost));
        holder.which.setText("" + CommonUtils.getYear(model.getTime()));
        holder.date.setText("" + CommonUtils.getFormattedDateOnly(model.getTime()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 1) {
                    changeLayout.onClick(model, position, "year");
                } else if (type == 2) {
                    changeLayout.onClick(model, position, "month");
                } else if (type == 3) {
                    changeLayout.onClick(model, position, "day");
                } else if (type == 4) {
                    changeLayout.onClick(model, position, "itself");
                }
            }
        });

//        for (int i = 0; i < model.getPurchaseModels().size(); i++) {
//
//            cost = cost + (model.getPurchaseModels().get(i).get.getProduct().getCostPrice()) * (model.getProductsList().get(i).getQuantity());
//        }

//        for(int i=0;i<model.getPurchaseModels().size();i++){
//            for(int j=0;j<model.getPurchaseModels().get(i).getProductsList().size();i++){
//                cost=cost+((model.getPurchaseModels().get(i).getProductsList().get(j).getProduct().getCostPrice()));
////                        *(model.getPurchaseModels().get(i).getProductsList().get(j).getQuantity()));
//            }
//
////        }
//        holder.cost.setText("Cost: Rs " + CommonUtils.getFormattedPrice(cost));
//        holder.which.setText("" + CommonUtils.getYear(model.getDate().getTime()));
//        holder.date.setText("" + CommonUtils.getYear(model.getDate().getTime()));


//        holder.cost.setText("Cost: Rs " + CommonUtils.getFormattedPrice(cost));
//        holder.purchase.setText("Purchase: Rs " + CommonUtils.getFormattedPrice(cost));
//
//        holder.which.setText("" + CommonUtils.getYear(model.getTime()));
//        holder.date.setText("" + CommonUtils.getFormattedDateOnly(model.getDate().getTime()
//        ));

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView count, which, date, cost, purchase;

        public ViewHolder(View itemView) {
            super(itemView);
            cost = itemView.findViewById(R.id.cost);
            count = itemView.findViewById(R.id.count);
            purchase = itemView.findViewById(R.id.purchase);
            which = itemView.findViewById(R.id.which);
            date = itemView.findViewById(R.id.date);
        }
    }

    public interface ChangeLayout {
        public void onClick(PurchaseOrderModel model, int position, String type);
    }
}
