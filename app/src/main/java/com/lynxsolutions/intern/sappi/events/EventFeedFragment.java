package com.lynxsolutions.intern.sappi.events;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.lynxsolutions.intern.sappi.R;
import com.lynxsolutions.intern.sappi.cars.Route;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventFeedFragment extends Fragment {
    private static final String TAG = EventFeedFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    final List<Event> eventList = new ArrayList<>();
    private RecyclerViewAdapter mAdapter;
    private int currentPage = 0;
    private static final int TOTAL_ITEM_EACH_LOAD = 5;
    private ProgressBar mProgressBar;
    private DatabaseReference mDatabase, db;

    DatabaseReference mPostRef;


    public EventFeedFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Events Feed");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        db = FirebaseDatabase.getInstance().getReference();
        View view = inflater.inflate(R.layout.fragment_event_feed, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.postsid);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        mAdapter = new RecyclerViewAdapter(eventList, getContext());
        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(getApplicationContext());
        //layoutManager.setReverseLayout(true);
        //layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);





        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                loadMoreData();
            }
        });
        loadData();
        return view;
    }

    private void loadData() {



        mDatabase.child("events").orderByKey()

                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.hasChildren()) {


                        }
                        int i = 0;


                        for (DataSnapshot data : dataSnapshot.getChildren()) {


                            Event question = data.getValue(Event.class);


                            eventList.add(0, question);


                            mAdapter.notifyDataSetChanged();


                            mProgressBar.setVisibility(RecyclerView.GONE);


                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mProgressBar.setVisibility(RecyclerView.GONE);

                    }
                });


    }

    private void loadMoreData() {

        //loadData();
        //mProgressBar.setVisibility(RecyclerView.VISIBLE);
    }
}