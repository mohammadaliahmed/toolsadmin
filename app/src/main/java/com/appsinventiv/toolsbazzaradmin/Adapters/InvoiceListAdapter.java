package com.appsinventiv.toolsbazzaradmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Invoicing.ViewInvoice;
import com.appsinventiv.toolsbazzaradmin.Models.InvoiceModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by AliAh on 12/09/2018.
 */

public class InvoiceListAdapter extends RecyclerView.Adapter<InvoiceListAdapter.ViewHolder> {
    Context context;
    ArrayList<InvoiceModel> itemList;


    public InvoiceListAdapter(Context context, ArrayList<InvoiceModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.invoice_item_list_layout, parent, false);
        InvoiceListAdapter.ViewHolder viewHolder = new InvoiceListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final InvoiceModel model = itemList.get(position);
        holder.date.setText(CommonUtils.getFormattedDateOnly(model.getTime()));
        holder.invoiceNumber.setText("" + model.getId());
        holder.invoiceTotal.setText("" + CommonUtils.getFormattedPrice(model.getGrandTotal()));
        holder.orderId.setText("" + model.getOrderId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ViewInvoice.class);
                i.putExtra("invoiceNumber", model.getId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, invoiceNumber, orderId, invoiceTotal;

        public ViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            invoiceNumber = itemView.findViewById(R.id.invoiceNumber);
            orderId = itemView.findViewById(R.id.orderId);
            invoiceTotal = itemView.findViewById(R.id.invoiceTotal);

        }
    }
}
