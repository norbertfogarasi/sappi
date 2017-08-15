package com.lynxsolutions.intern.sappi.events;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
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
import com.lynxsolutions.intern.sappi.main.NavigationManager;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */

public class EventFeedFragment extends Fragment implements EventClickListener{

    private static final String TAG = EventFeedFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Event> eventList;
    private RecyclerViewAdapter mAdapter;
    private ProgressBar mProgressBar;
    private NavigationManager manager;
    private View view;
    private DatabaseReference mDatabase;

    public EventFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_event_feed, container, false);
        } catch (InflateException e) {
            e.printStackTrace();
        }

        initViews();
        loadData();
        return view;
    }

    private void initViews() {
        getActivity().setTitle("Events Feed");
        eventList = new ArrayList<>();
        manager = new NavigationManager(getFragmentManager());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView = view.findViewById(R.id.postsid);
        mProgressBar = view.findViewById(R.id.progressbar);
        mAdapter = new RecyclerViewAdapter(eventList, getContext(), this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                loadMoreData();
            }
        });
    }

    private void loadData() {

        mDatabase.child("events").orderByKey()

                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.hasChildren()) {}

                        for (DataSnapshot data : dataSnapshot.getChildren()) {

                            Event event = data.getValue(Event.class);
                            eventList.add(0, event);
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

    @Override
    public void onItemClick(Event event) {
        EventDetailFragment eventDetailFragment = new EventDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("event",event);
        eventDetailFragment.setArguments(bundle);
        manager.switchToFragment(eventDetailFragment);
    }
}