package com.lynxsolutions.intern.sappi.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lynxsolutions.intern.sappi.R;
import com.lynxsolutions.intern.sappi.cars.RouteClickListener;
import com.lynxsolutions.intern.sappi.cars.Route;
import com.lynxsolutions.intern.sappi.events.Event;
import com.lynxsolutions.intern.sappi.events.EventClickListener;

import java.util.ArrayList;

/**
 * Created by farkaszsombor on 03.08.2017.
 */

public class ComplexRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RouteClickListener routeListener;
    private EventClickListener eventListener;

    // The itemsList to display in your RecyclerView
    private ArrayList<Object> itemsList;
    private Context context;
    private final int EVENT = 0, ROUTE = 1;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ComplexRecyclerViewAdapter(ArrayList<Object> itemsList, Context context,
                                      RouteClickListener routeListener, EventClickListener eventListener) {
        this.itemsList = itemsList;
        this.context = context;
        this.routeListener = routeListener;
        this.eventListener = eventListener;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.itemsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (itemsList.get(position) instanceof Event) {
            return EVENT;
        } else if (itemsList.get(position) instanceof Route) {
            return ROUTE;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case EVENT:
                View v1 = inflater.inflate(R.layout.layout_event_holder, viewGroup, false);
                viewHolder = new EventHolder(v1);
                break;
            case ROUTE:
                View v2 = inflater.inflate(R.layout.layout_route_holder, viewGroup, false);
                viewHolder = new RouteHolder(v2);
                break;
            default:
                View v3 = inflater.inflate(R.layout.layout_route_holder, viewGroup, false);
                viewHolder = new RouteHolder(v3);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {
            case EVENT:
                EventHolder eventHolder = (EventHolder) viewHolder;
                final Event event = (Event) itemsList.get(position);
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(event.getImageUrl());
                if(event != null){
                    eventHolder.getLabel1().setText(event.getTitle());
                    Glide.with(context)
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .centerCrop()
                            .into(eventHolder.getImage());
                    eventHolder.getBtn().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            eventListener.onItemClick(event);
                        }
                    });

                }
                break;
            case ROUTE:
                RouteHolder routeHolder = (RouteHolder) viewHolder;
                final Route route = (Route) itemsList.get(position);
                if(route != null){
                    routeHolder.getFrom().setText(route.getFrom());
                    routeHolder.getTo().setText(route.getTo());
                    routeHolder.getBtn().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            routeListener.onItemClick(route);
                        }
                    });
                }
                break;
        }
    }
}