package com.lynxsolutions.intern.sappi.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lynxsolutions.intern.sappi.R;
import com.lynxsolutions.intern.sappi.cars.Route;
import com.lynxsolutions.intern.sappi.events.Event;
import com.lynxsolutions.intern.sappi.main.MainActivity;

import java.util.ArrayList;

/**
 * Created by farkaszsombor on 03.08.2017.
 */

public class ComplexRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The items to display in your RecyclerView
    private ArrayList<Object> items;
    private Context context;
    private final int EVENT = 0, ROUTE = 1;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ComplexRecyclerViewAdapter(ArrayList<Object> items,Context context) {
        this.items = items;
        this.context = context;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Event) {
            return EVENT;
        } else if (items.get(position) instanceof Route) {
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
                View v1 = inflater.inflate(R.layout.layout_viewholder1, viewGroup, false);
                viewHolder = new ViewHolder1(v1);
                break;
            case ROUTE:
                View v2 = inflater.inflate(R.layout.layout_viewholder2, viewGroup, false);
                viewHolder = new ViewHolder2(v2);
                break;
            default:
                View v3 = inflater.inflate(R.layout.layout_viewholder2, viewGroup, false);
                viewHolder = new ViewHolder2(v3);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {
            case EVENT:
                ViewHolder1 vh1 = (ViewHolder1) viewHolder;
                Event event = (Event)items.get(position);
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(event.getImageUrl());
                if(event != null){
                    vh1.getLabel1().setText(event.getTitle());
                    Glide.with(context)
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .centerCrop()
                            .into(vh1.getImage());
                }
                break;
            case ROUTE:
                ViewHolder2 vh2 = (ViewHolder2) viewHolder;
                Route route = (Route)items.get(position);
                if(route != null){
                    vh2.getLabel1().setText(route.getDate());
                    vh2.getLabel2().setText(route.getDescription());
                }
                break;
        }
    }
}