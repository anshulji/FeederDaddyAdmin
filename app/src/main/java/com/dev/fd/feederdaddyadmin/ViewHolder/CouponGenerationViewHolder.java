package com.dev.fd.feederdaddyadmin.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.fd.feederdaddyadmin.R;


public class CouponGenerationViewHolder extends RecyclerView.ViewHolder
        //implements View.OnClickListener
        {

    public ImageView imgdeletecoupon;
    public TextView txtcouponcode,txtcoupondiscount;

    public CouponGenerationViewHolder(@NonNull View itemView) {
        super(itemView);

        imgdeletecoupon = itemView.findViewById(R.id.imgdeletecoupon);
        txtcouponcode = itemView.findViewById(R.id.txtcouponcode);
        txtcoupondiscount = itemView.findViewById(R.id.txtcoupondiscount);

    }
}
