package com.lynxsolutions.intern.sappi.events;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lynxsolutions.intern.sappi.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventFeedFragment extends Fragment {
    private RecyclerView mPostRV;
    private FirebaseRecyclerAdapter<Event, PostViewHolder> mPostAdapter;
    private DatabaseReference mPostRef;

    public EventFeedFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Events Feed");
        View view = inflater.inflate(R.layout.fragment_event_feed, container, false);
        mPostRV = (RecyclerView) view.findViewById(R.id.postsid);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mPostRV.setLayoutManager(layoutManager);
        mPostRef = FirebaseDatabase.getInstance().getReference("events");
        //sendPostToFirebase();
        setupAdapter();
        mPostRV.setAdapter(mPostAdapter);
        return view;
    }




    public static class PostViewHolder extends RecyclerView.ViewHolder {

        public ImageView vpicture;
        public TextView vdescript;
        public TextView vtitle;
        public TextView datetime;


        public PostViewHolder(View itemView) {
            super(itemView);
            vpicture=(ImageView) itemView.findViewById(R.id.postpicture);
            vdescript=(TextView) itemView.findViewById(R.id.postdescription);
            vtitle=(TextView)  itemView.findViewById(R.id.posttitle);
            datetime=(TextView) itemView.findViewById(R.id.datetime);
        }
    }

    private void sendPostToFirebase() {

        String UID = "5634t5erfgsdg";

        Event p=new Event("gs://sappi-ccc6a.appspot.com/flickr Bőr Benedek.jpg","AUG\n11-16","Sziget Fesztival",
                UID,"sziget sziget sdfsdfsdfsd.......");
        Event post = new Event("gs://testproject1-e5b8d.appspot.com/TT20100723_204.jpg",
                "JUN\n22-23","Tusvanyos",UID,"blablablablabla................");

        mPostRef.child(Long.toString(System.currentTimeMillis())).setValue(p);
    }

    private void setupAdapter() {
        mPostAdapter = new FirebaseRecyclerAdapter<Event, PostViewHolder>(
                Event.class,
                R.layout.item_layout_post,
                PostViewHolder.class,
                mPostRef
        ) {

            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, final Event model, int position) {

                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.getImageUrl());
                Glide.with(getContext())
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .into(viewHolder.vpicture);

                viewHolder.vtitle.setText(model.getTitle());
                viewHolder.vdescript.setText(model.getDescription());
                viewHolder.datetime.setText(model.getDate());
                /*viewHolder.postLikeIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateNumLikes(model.getUID());
                    }
                });*/
            }
        };

    }
}