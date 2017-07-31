package com.lynxsolutions.intern.sappi.cars;

import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

/**
 * Created by Kálmi on 2017. 07. 28..
 */

class RouteAdapter extends FirebaseRecyclerAdapter<Route, RouteViewHolder> {


    private final AdapterItemClickListener listener;

    public RouteAdapter(Class<Route> modelClass, int modelLayout, Class<RouteViewHolder> viewHolderClass, Query ref, AdapterItemClickListener listener) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.listener = listener;
    }

    @Override
    protected void populateViewHolder(RouteViewHolder viewHolder, final Route model, final int position) {
        viewHolder.setData(model);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(model);
            }
        });
    }

}
