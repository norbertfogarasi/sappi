package com.lynxsolutions.intern.sappi.cars;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

/**
 * Created by Kálmi on 2017. 07. 28..
 */

class RouteAdapter extends FirebaseRecyclerAdapter<Route, RouteViewHolder> {

    public RouteAdapter(Class<Route> modelClass, int modelLayout, Class<RouteViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(RouteViewHolder viewHolder, Route model, int position) {
        viewHolder.setData(model);
    }

}
