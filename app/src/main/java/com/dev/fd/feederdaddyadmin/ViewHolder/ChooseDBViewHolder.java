package com.dev.fd.feederdaddyadmin.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dev.fd.feederdaddyadmin.R;

public class ChooseDBViewHolder extends RecyclerView.ViewHolder
{
    public TextView txtdbname,txtdbcurrentload;

    public ChooseDBViewHolder(View itemView) {
        super(itemView);
        txtdbname = itemView.findViewById(R.id.txtdeliveryboyname);
        txtdbcurrentload = itemView.findViewById(R.id.txtcurrentload);
    }

}
