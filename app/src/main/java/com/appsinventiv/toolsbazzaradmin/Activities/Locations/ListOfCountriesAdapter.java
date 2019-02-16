package com.appsinventiv.toolsbazzaradmin.Activities.Locations;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.appsinventiv.toolsbazzaradmin.R;

import java.util.ArrayList;

/**
 * Created by AliAh on 27/11/2018.
 */

public class ListOfCountriesAdapter extends RecyclerView.Adapter<ListOfCountriesAdapter.ViewHolder> {
    Context context;
    ArrayList<String> list;

    public ListOfCountriesAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.choose_category_item, parent, false);
        ListOfCountriesAdapter.ViewHolder viewHolder = new ListOfCountriesAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String title = list.get(position);
        holder.title.setText(title);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AddCountry.class);
                i.putExtra("country", title);
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }

}
