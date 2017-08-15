package com.lynxsolutions.intern.sappi.profile;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lynxsolutions.intern.sappi.R;
import com.lynxsolutions.intern.sappi.events.Event;
import com.lynxsolutions.intern.sappi.events.EventDetailFragment;
import com.lynxsolutions.intern.sappi.main.NavigationManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference firebaseDatabase;
    TextView emptyFavText;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Favorites");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        emptyFavText = (TextView)rootView.findViewById(R.id.empty_view);
        // Inflate the layout for this fragment
        recyclerView = (RecyclerView)rootView.findViewById(R.id.favourites_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = user.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("favorites").child(userId);
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    recyclerView.setVisibility(View.GONE);
                    emptyFavText.setVisibility(View.VISIBLE);
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyFavText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Favorites","Something went wrong ...");
            }
        });
        // my_child_toolbar is defined in the layout file
        FirebaseRecyclerAdapter<Event,FavoritesViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Event, FavoritesViewHolder>(
                Event.class,
                R.layout.favourites_recycler_view_rows,
                FavoritesViewHolder.class,
                firebaseDatabase
        ) {
            @Override
            protected void populateViewHolder(FavoritesViewHolder viewHolder, final Event model, int position) {
                viewHolder.eventNameText.setText(model.getTitle());
                viewHolder.eventDescriptionText.setText(model.getDescription());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventDetailFragment eventDetailFragment = new EventDetailFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("event",model);
                        eventDetailFragment.setArguments(bundle);
                        new NavigationManager(getFragmentManager()).switchToFragment(eventDetailFragment);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);

        return rootView;
    }


    public static class FavoritesViewHolder extends RecyclerView.ViewHolder{


        TextView eventNameText,eventDescriptionText;

        public FavoritesViewHolder(View itemView){

            super(itemView);
            eventNameText = itemView.findViewById(R.id.title_of_favourite_event);
            eventDescriptionText = itemView.findViewById(R.id.description_of_event);
        }

    }

}
