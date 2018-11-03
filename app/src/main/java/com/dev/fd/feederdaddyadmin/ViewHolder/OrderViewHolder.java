package com.dev.fd.feederdaddyadmin.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.fd.feederdaddyadmin.R;

public class OrderViewHolder extends RecyclerView.ViewHolder
        //implements View.OnClickListener
         {

    public ImageView imgcalldeliveryboy,imgcallcust,imgcallrest;
    public TextView txtorderid,txtordertime,txttotalamount,txtviewbill,txtdeliveryboyname,txtotp,txtcustaddress,txtrestname,txtcustphone,txtrestphone,txtorderreceivetime;
    public Button btnaccept,btncancel;
    public RelativeLayout rldbinfo,rlort;


    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);


        imgcallcust = itemView.findViewById(R.id.imgcallcust);
        imgcallrest = itemView.findViewById(R.id.imgcallrest);
        imgcalldeliveryboy = itemView.findViewById(R.id.imgcalldeliveryboy);

        txtorderid = itemView.findViewById(R.id.txtorderid);
        txtordertime = itemView.findViewById(R.id.txtordertime);
        txtorderreceivetime = itemView.findViewById(R.id.txtorderreceivetime);
        txttotalamount = itemView.findViewById(R.id.txttotalamount);
        txtviewbill = itemView.findViewById(R.id.txtviewbill);
        txtdeliveryboyname = itemView.findViewById(R.id.txtdeliveryboyname);
        txtotp = itemView.findViewById(R.id.txtdeliveryboyotp);
        txtcustaddress = itemView.findViewById(R.id.txtcustaddress);
        txtrestname = itemView.findViewById(R.id.txtrestname);
        txtcustphone = itemView.findViewById(R.id.txtcustphone);
        txtrestphone = itemView.findViewById(R.id.txtrestphone);

        rldbinfo = itemView.findViewById(R.id.rldeliveryboyinfo);
        rlort = itemView.findViewById(R.id.rlort);


        btnaccept= itemView.findViewById(R.id.btnaccept);
        btncancel = itemView.findViewById(R.id.btncancel);

        




        //itemView.setOnClickListener(this);


    }

    /*public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }*/
}
