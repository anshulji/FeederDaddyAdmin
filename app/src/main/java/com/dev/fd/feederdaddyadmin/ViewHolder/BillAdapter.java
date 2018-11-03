package com.dev.fd.feederdaddyadmin.ViewHolder;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.dev.fd.feederdaddyadmin.R;
import com.dev.fd.feederdaddyadmin.model.Order;

import java.util.ArrayList;
import java.util.List;

class BillViewHolder extends RecyclerView.ViewHolder{

    public TextView txtfooditemname,txtfooditemqty,txtfooditemtotalcost;


    public BillViewHolder(@NonNull View itemView) {
        super(itemView);

        txtfooditemname = itemView.findViewById(R.id.txtfooditemname);
        txtfooditemtotalcost = itemView.findViewById(R.id.txtfooditemtotalcost);
        txtfooditemqty = itemView.findViewById(R.id.txtfooditemqty);




    }


}

public class BillAdapter extends RecyclerView.Adapter<BillViewHolder>{

    private List<Order> listData = new ArrayList<>();
    private Context context;

    public BillAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.bill_item_layout,null);


        return new BillViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BillViewHolder billViewHolder, final int i) {

       //txtfooditemname
        billViewHolder.txtfooditemname.setText(listData.get(i).getFoodname());
        billViewHolder.txtfooditemqty.setText(listData.get(i).getQuantity());

        billViewHolder.txtfooditemtotalcost.setText("₹"+ Integer.parseInt(listData.get(i).getPrice())* Integer.parseInt(listData.get(i).getQuantity()));

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

}
