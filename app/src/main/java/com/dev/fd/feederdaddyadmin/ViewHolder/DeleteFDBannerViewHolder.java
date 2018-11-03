package com.dev.fd.feederdaddyadmin.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.fd.feederdaddyadmin.R;


public class DeleteFDBannerViewHolder extends RecyclerView.ViewHolder
        //implements View.OnClickListener
        {

    public ImageView imgdeletebanner,imgbanner;
    public TextView txtbannername;

    public DeleteFDBannerViewHolder(@NonNull View itemView) {
        super(itemView);

        imgdeletebanner = itemView.findViewById(R.id.imgdeletebanner);
        imgbanner = itemView.findViewById(R.id.imgbanner);
        txtbannername = itemView.findViewById(R.id.txtbanner);

    }
}
