package com.lynxsolutions.intern.sappi.news;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lynxsolutions.intern.sappi.R;
import com.lynxsolutions.intern.sappi.cars.Route;
import com.lynxsolutions.intern.sappi.events.Event;
import com.lynxsolutions.intern.sappi.events.RecyclerViewAdapter;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends Fragment {

    RecyclerView recyclerView;
    ComplexRecyclerViewAdapter mAdapter;
    DatabaseReference databaseReferenceForEvents;
    DatabaseReference getDatabaseReferenceForCars;
    Event event;
    Route route;
    ArrayList<Object> eventAndMapContainer = new ArrayList<>();


    public NewsFeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("News Feed");

        View v = inflater.inflate(R.layout.fragment_news_feed, container, false);

        event = new Event();
        route = new Route();
        databaseReferenceForEvents = FirebaseDatabase.getInstance().getReference("events");
        getDatabaseReferenceForCars = FirebaseDatabase.getInstance().getReference("cars");
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        getDataReady();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void getDataReady(){

        databaseReferenceForEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    event = snapshot.getValue(Event.class);
                    eventAndMapContainer.add(event);
                }
                getDatabaseReferenceForCars.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        /*
                        for(Object obj :eventAndMapContainer){
                            int i = eventAndMapContainer.indexOf(obj);
                            Event event = (Event)eventAndMapContainer.get(i);
                            if(event.ge)
                        }*/



                        int j = 1;
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            route = snapshot.getValue(Route.class);
                            eventAndMapContainer.add(j,route);
                            j += 2;
                        }



                        recyclerView.setAdapter(new ComplexRecyclerViewAdapter(eventAndMapContainer,getContext()));

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



}
