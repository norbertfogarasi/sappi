package com.lynxsolutions.intern.sappi.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lynxsolutions.intern.sappi.R;
import com.lynxsolutions.intern.sappi.cars.RouteClickListener;
import com.lynxsolutions.intern.sappi.main.NavigationManager;
import com.lynxsolutions.intern.sappi.cars.Route;
import com.lynxsolutions.intern.sappi.cars.RouteDetailFragment;
import com.lynxsolutions.intern.sappi.events.Event;
import com.lynxsolutions.intern.sappi.events.EventClickListener;
import com.lynxsolutions.intern.sappi.events.EventDetailFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends Fragment implements RouteClickListener, EventClickListener {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReferenceForEvents;
    private DatabaseReference getDatabaseReferenceForCars;
    private Event event;
    private Route route;
    private ArrayList<Object> postContainer;
    private ArrayList<Event> eventContainer;
    private ArrayList<Route> routeContainer;
    private View view;
    private NavigationManager manager;

    public NewsFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        } catch (InflateException e) {

        }

        initViews();
        getDataReady();

        return view;
    }

    private void initViews() {
        getActivity().setTitle("News Feed");
        manager = new NavigationManager(getFragmentManager());
        postContainer = new ArrayList<>();
        eventContainer = new ArrayList<>();
        routeContainer = new ArrayList<>();
        event = new Event();
        route = new Route();
        databaseReferenceForEvents = FirebaseDatabase.getInstance().getReference("events");
        getDatabaseReferenceForCars = FirebaseDatabase.getInstance().getReference("cars");
        recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
    }

    private void getDataReady(){

        databaseReferenceForEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    event = snapshot.getValue(Event.class);
                    eventContainer.add(event);
                }

                getDatabaseReferenceForCars.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            route = snapshot.getValue(Route.class);
                            routeContainer.add(route);
                        }
                        setUpAdapter();
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

    private void setUpAdapter() {

        int j = 0;
        int i = 0;
        while (i < routeContainer.size() && j < eventContainer.size()){
            if(Long.parseLong(routeContainer.get(i).getTimestamp()) < Long.parseLong(eventContainer.get(j).getTimestamp())) {
                postContainer.add(routeContainer.get(i++));
            }
            else {
                postContainer.add(eventContainer.get(j++));
            }
        }
        while (i < routeContainer.size()){
            postContainer.add(routeContainer.get(i++));
        }

        while(j < eventContainer.size()){
            postContainer.add(eventContainer.get(j++));
        }

        Collections.reverse(postContainer);
        recyclerView.setAdapter(new ComplexRecyclerViewAdapter(postContainer,getContext(),this,this));
    }

    @Override
    public void onItemClick(Event event) {
        EventDetailFragment eventDetailFragment = new EventDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("event",event);
        eventDetailFragment.setArguments(bundle);
        manager.switchToFragment(eventDetailFragment);
    }

    @Override
    public void onItemClick(Route route) {
        RouteDetailFragment routeDetailFragment = new RouteDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("route",route);
        routeDetailFragment.setArguments(bundle);
        manager.switchToFragment(routeDetailFragment);
    }
}
