package com.appsinventiv.toolsbazzaradmin.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Models.SelectedAdImages;
import com.appsinventiv.toolsbazzaradmin.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by AliAh on 23/01/2018.
 */

public class SelectedImagesAdapter extends RecyclerView.Adapter<SelectedImagesAdapter.ViewHolder> {
    List<SelectedAdImages> mobileAds;
    Context context;
    ChooseOption option;
    //    private List<String> adTitlesList = Collections.emptyList();
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public SelectedImagesAdapter(Context context, List<SelectedAdImages> mobileAds, ChooseOption option) {
        this.mInflater = LayoutInflater.from(context);
        this.mobileAds = mobileAds;
        this.context = context;
        this.option = option;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.picked_images, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SelectedAdImages model = mobileAds.get(position);

        Glide.with(context).load(model.getUrl()).into(holder.adImageView);
        position = position + 1;
        holder.picCount.setText("" + position);


        final int finalPosition = position;
        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option.onDeleteClicked(model, finalPosition);
            }
        });


    }

    @Override
    public int getItemCount() {

        return mobileAds.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View myView;

        public ImageView adImageView;
        public TextView picCount;
        ImageView deleteImage;

        public ViewHolder(View itemView) {
            super(itemView);

            adImageView = itemView.findViewById(R.id.imageview);
            picCount = itemView.findViewById(R.id.pic_count);
            deleteImage = itemView.findViewById(R.id.deleteImage);


        }


    }

    public interface ChooseOption {
        public void onDeleteClicked(SelectedAdImages images,int position);
    }


}
