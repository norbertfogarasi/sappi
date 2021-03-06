package com.lynxsolutions.intern.sappi.events;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lynxsolutions.intern.sappi.R;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {


    private List<Event> EventList; // list for events
    private Context context;
    private EventClickListener listener;

    //viewholder for one event
    class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView vpicture;
        public TextView vtitle;
        public TextView datetime;


        public MyViewHolder(View itemView) {
            super(itemView);
            vpicture=(ImageView) itemView.findViewById(R.id.postpicture);
            vtitle=(TextView)  itemView.findViewById(R.id.posttitle);
            datetime=(TextView) itemView.findViewById(R.id.datetime);
        }
    }

    RecyclerViewAdapter(List<Event> mQuestionList, Context context, EventClickListener listener) {
        this.EventList = mQuestionList;
        this.context=context;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_holder, parent, false);

        return new MyViewHolder(itemView);
    }

    //fill viewholder with data
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Event question = EventList.get(position);
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(question.getImageUrl());
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(holder.vpicture);

        holder.vtitle.setText(question.getTitle());
        holder.datetime.setText(question.getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    listener.onItemClick(question);
            }
        });
    }

    @Override
    public int getItemCount() {
        return EventList.size();
    }
}