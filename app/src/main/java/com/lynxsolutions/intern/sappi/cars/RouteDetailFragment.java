package com.lynxsolutions.intern.sappi.cars;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lynxsolutions.intern.sappi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteDetailFragment extends Fragment {

    Route route;

    public RouteDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Car Deatail");
        route = (Route) getArguments().get("route");
        Toast.makeText(getContext(), route.getUsername(), Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.fragment_route_detail, container, false);
    }

}
