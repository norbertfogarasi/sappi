package com.lynxsolutions.intern.sappi.cars;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lynxsolutions.intern.sappi.R;
import com.lynxsolutions.intern.sappi.main.NavigationManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteDetailFragment extends Fragment {

    private TextView tvAddedBy;
    private TextView tvRouteDetail;
    private TextView tvFrom;
    private TextView tvTo;
    private ImageView ivProfile;
    private View view;
    private Route route;
    private NavigationManager manager;

    public RouteDetailFragment() {
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
            view = inflater.inflate(R.layout.fragment_route_detail, container, false);
        } catch (InflateException e) {
            e.printStackTrace();
        }

        initViews();
        getRouteObject();
        setData();

        return view;
    }

    private void getRouteObject() {
        //Getting the Bundle Object
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            route = (Route) bundle.get("route");
        }
    }

    private void setData() {
        if(route != null) {
            tvAddedBy.append(route.getUsername());
            tvFrom.setText(route.getFrom());
            tvTo.setText(route.getTo());
            tvRouteDetail.setText(route.getDescription());

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(route.getUid().equals(user.getUid())){
                TextView tvDelete = view.findViewById(R.id.route_detail_tv_phone_or_delete);
                tvDelete.setText("DELETE");
                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteFromDatabase();
                        Toast.makeText(getContext(), "Route deleted", Toast.LENGTH_SHORT).show();
                        manager.switchToMainFragment(new CarFeedFragment());
                    }
                });

            }
            else{
                TextView tvPhone = view.findViewById(R.id.route_detail_tv_phone_or_delete);
                tvPhone.setText("PHONE");
                tvPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Putting the phone number into dialer
                        String uri = "tel:" + route.getPhonenumber();
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(uri));
                        startActivity(intent);
                    }
                });
            }
        }
    }

    private void deleteFromDatabase() {
        DatabaseReference mRef= FirebaseDatabase.getInstance().getReference("cars").child(route.getTimestamp()).getRef();
                mRef.removeValue();
    }

    private void initViews() {
        //Initializes the views
        getActivity().setTitle("Car Detail");
        manager = new NavigationManager(getFragmentManager());
        ivProfile = view.findViewById(R.id.route_detail_profile_image);
        tvAddedBy = view.findViewById(R.id.route_detail_tv_added_by);
        tvFrom = view.findViewById(R.id.route_detail_tv_from);
        tvTo = view.findViewById(R.id.route_detail_tv_to);
        tvRouteDetail = view.findViewById(R.id.route_detail_tv_description);
    }

}
