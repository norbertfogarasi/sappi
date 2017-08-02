package com.lynxsolutions.intern.sappi.cars;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.os.Bundle;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.lynxsolutions.intern.sappi.R;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */

public class AddRouteFragment extends Fragment {

    private static final String TAG = "AddFragment";
    private PlaceAutocompleteFragment autocompleteFrom;
    private PlaceAutocompleteFragment autocompleteTo;
    private Place placeFrom;
    private Place placeTo;
    private Button submitBtn;

    public AddRouteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_add_route, container, false);

        autocompleteFrom = (PlaceAutocompleteFragment)getActivity().
                getFragmentManager().findFragmentById(R.id.place_autocomplete_from);


        autocompleteTo = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_to);

        setIcons();

        submitBtn = (Button) view.findViewById(R.id.submit_button);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPostToFirebase();
            }
        });

        autocompleteFrom.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                placeFrom = place;
                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });


        autocompleteTo.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                placeTo = place;
                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });


        return view;
    }

    private void sendPostToFirebase() {



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

/*
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("users");
        mRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                info = dataSnapshot.getValue(UserInfo.class);
                Log.d(TAG, "onDataChange: "+info.getName());
                Log.d(TAG, "onDataChange: "+info.getPhonenumber());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " +databaseError);

            }
        });*/

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();

        Route car = new Route(placeFrom.getName().toString(),placeTo.getName().toString(),"NINCS LEIRAS",
                "NINCS TELEFONSZAM",user.getUid(),dateFormat.format(date),"NINCS NEV");
        FirebaseDatabase.getInstance().getReference("cars").child(Long.toString(System.currentTimeMillis())).setValue(car);
    }

    private void setIcons() {
        ImageView fromIcon = (ImageView)((LinearLayout)autocompleteFrom.getView()).getChildAt(0);
        fromIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_my_location_black_24dp));

        ImageView toIcon = (ImageView)((LinearLayout)autocompleteTo.getView()).getChildAt(0);
        toIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_location_on_black_24dp));
    }
}
