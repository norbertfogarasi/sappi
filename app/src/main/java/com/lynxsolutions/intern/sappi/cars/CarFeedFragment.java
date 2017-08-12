package com.lynxsolutions.intern.sappi.cars;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lynxsolutions.intern.sappi.R;
import com.lynxsolutions.intern.sappi.main.NavigationManager;

/**
 * A simple {@link Fragment} subclass.
 */

public class CarFeedFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private DatabaseReference mPostRef;
    private RouteAdapter mAdapter;
    private FloatingActionButton addButton;
    private NavigationManager manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_car_feed, container, false);

        initializeScreen(view);
       // sendPostToFirebase();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    private void initializeScreen(View view) {
        getActivity().setTitle("Car Feed");
        mRecyclerView = view.findViewById(R.id.recycler_view2);
        mPostRef = FirebaseDatabase.getInstance().getReference("cars");

        manager = new NavigationManager(getFragmentManager());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);

        addButton = view.findViewById(R.id.floating_action_button2);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.switchToFragment(new AddRouteFragment());
            }
        });

        mAdapter = new RouteAdapter(Route.class, R.layout.route_holder, RouteViewHolder.class, mPostRef,
                new RouteClickListener() {
                    @Override
                    public void onItemClick(Route route) {
                        RouteDetailFragment routeDetailFragment = new RouteDetailFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("route",route);
                        routeDetailFragment.setArguments(bundle);
                        manager.switchToFragment(routeDetailFragment);
                    }
                });
        mRecyclerView.setAdapter(mAdapter);
    }

}