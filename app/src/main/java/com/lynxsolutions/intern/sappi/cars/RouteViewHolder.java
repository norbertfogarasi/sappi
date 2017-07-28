package com.lynxsolutions.intern.sappi.cars;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lynxsolutions.intern.sappi.R;

/**
 * Created by Kálmi on 2017. 07. 28..
 */

class RouteViewHolder extends RecyclerView.ViewHolder {

    private TextView tvFrom;
    private TextView tvUsername;
    private TextView tvTo;

    public RouteViewHolder(View itemView) {
        super(itemView);

        tvFrom = itemView.findViewById(R.id.from_tv);
        tvUsername = itemView.findViewById(R.id.username_tv);
        tvTo = itemView.findViewById(R.id.to_tv);
    }

    public void setData(Route route) {
        tvFrom.setText(route.getFrom());
        tvTo.setText(route.getTo());
        tvUsername.setText(route.getUsername());
    }
}
