package com.lynxsolutions.intern.sappi.cars;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lynxsolutions.intern.sappi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteDetailFragment extends Fragment {

    private TextView tvAddedBy;
    private TextView tvRouteDetail;
    private TextView tvFrom;
    private TextView tvTo;
    private TextView tvMyInfo;
    private TextView tvPhone;

    Route route;

    public RouteDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_route_detail, container, false);
        getActivity().setTitle("Car Detail");

        initViews(view);

        //Getting the Bundle Object
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Route route = (Route) bundle.get("route");
            if(route != null) {
                tvAddedBy.append(route.getUsername());
                tvFrom.setText(route.getFrom());
                tvTo.setText(route.getTo());
                tvRouteDetail.setText(route.getDescription());
            }
        }
        //Toast.makeText(getContext(), route.getUsername(), Toast.LENGTH_SHORT).show();
        return view;
    }

    private void initViews(View view) {
        //Initializes the views
        tvAddedBy = view.findViewById(R.id.route_detail_tv_added_by);
        tvFrom = view.findViewById(R.id.route_detail_tv_from);
        tvTo = view.findViewById(R.id.route_detail_tv_to);
        tvRouteDetail = view.findViewById(R.id.route_detail_tv_description);
        //tvRouteDetail.setMovementMethod(new ScrollingMovementMethod());
        tvMyInfo = view.findViewById(R.id.route_detail_tv_my_info);
        tvPhone = view.findViewById(R.id.route_detail_tv_phone);
    }

}
