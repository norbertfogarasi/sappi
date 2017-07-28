package com.lynxsolutions.intern.sappi.cars;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lynxsolutions.intern.sappi.R;

/**
 * A simple {@link Fragment} subclass.
 */

public class CarFeedFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private DatabaseReference mPostRef;
    private RouteAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_car_feed, container, false);

        initializeScreen(view);
        sendPostToFirebase();

        return view;
    }

    private void initializeScreen(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view2);
        mPostRef = FirebaseDatabase.getInstance().getReference("cars");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new RouteAdapter(Route.class, R.layout.recycler_item, RouteViewHolder.class, mPostRef);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void sendPostToFirebase() {
        String UID = "5634t5erfgsdg";
        Route car = new Route("Tokyo", "Koronka", "New York_Koronka", "asedfasdfas", "9898797867", "4234erw", "2017 jun 12", "John");
        mPostRef.child(Long.toString(System.currentTimeMillis())).setValue(car);
    }

}