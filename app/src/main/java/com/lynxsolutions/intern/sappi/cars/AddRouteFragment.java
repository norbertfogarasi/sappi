package com.lynxsolutions.intern.sappi.cars;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lynxsolutions.intern.sappi.R;
import com.lynxsolutions.intern.sappi.main.NavigationManager;
import com.lynxsolutions.intern.sappi.profile.UserInfo;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.location.places.ui.PlaceAutocomplete.MODE_OVERLAY;

/**
 * A simple {@link Fragment} subclass.
 */

public class AddRouteFragment extends Fragment{

    public static final int REQUEST_CODE = 1;
    private static final String TAG = "AddFragment";
    private CustomFragment autocompleteFrom;
    private CustomFragment autocompleteTo;
    private Place placeFrom;
    private Place placeTo;
    private UserInfo userInfo;
    private FirebaseUser user;
    private DatabaseReference mRef;
    private NavigationManager manager;
    private EditText description;
    private View view;

    public AddRouteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (autocompleteFrom != null)
            getFragmentManager().beginTransaction().remove(autocompleteFrom).commit();
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
            view = inflater.inflate(R.layout.fragment_add_route, container, false);
        } catch (InflateException e) {
            e.printStackTrace();
        }

        initViews();
        setListeners();
        return view;
    }

    private void setListeners(){

        view.findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fieldsAreSetCorrectly())
                    addNewRoute();
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
    }

    private void initViews() {
        description = view.findViewById(R.id.description_of_car);
        manager = new NavigationManager(getFragmentManager());
        user = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("users");

        autocompleteFrom = new CustomFragment();

        getChildFragmentManager().beginTransaction().add(R.id.linearLayout, autocompleteFrom,"fromFragment").commit();
        autocompleteFrom.setHint("Your location");
        autocompleteFrom.setIcon(R.drawable.ic_from_black_24dp);

        autocompleteTo = new CustomFragment();

        getChildFragmentManager().beginTransaction().add(R.id.linearLayout2, autocompleteTo,"toFragment").commit();
        autocompleteTo.setHint("Choose destination");
        autocompleteTo.setIcon(R.drawable.ic_location_on_black_24dp);

    }

    private boolean fieldsAreSetCorrectly(){
        boolean ok = true;
        if(placeFrom == null || placeTo == null) {
            Toast.makeText(getContext(), "Places must be selected", Toast.LENGTH_SHORT).show();
            ok = false;
        }
        if (description.getText().toString().isEmpty()){
            description.setError("Description must be added");
            ok = false;
        }
        return ok;
    }

    private void addNewRoute(){
        mRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(UserInfo.class);
                if(user != null) {
                    if (userInfo.getPhonenumber().isEmpty())
                        setPhoneNumber();
                    else sendPostToFirebase();
                }
                else {
                    setPhoneNumber();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " +databaseError);
            }
        });
    }

    private void setPhoneNumber() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter your phone number");
        builder.setCancelable(true);

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(input.getText().toString().isEmpty())
                    Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                else {
                    userInfo.setPhonenumber(input.getText().toString());
                    sendPostToFirebase();
                    manager.switchToFragment(new CarFeedFragment());
                    Toast.makeText(getContext(), "Added succesfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void sendPostToFirebase() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String timestamp = Long.toString(System.currentTimeMillis());
        Route car = new Route(placeFrom.getName().toString(),placeTo.getName().toString(),description.getText().toString(),
                userInfo.getPhonenumber(),user.getUid(),dateFormat.format(date),userInfo.getName(), timestamp);
        FirebaseDatabase.getInstance().getReference("cars").child(timestamp).setValue(car);
    }

    private void setIconsAndTexts() {



       /* ImageView fromIcon = (ImageView)((LinearLayout)autocompleteFrom.getView()).getChildAt(0);
        TextView fromText = (TextView)((LinearLayout) autocompleteFrom.getView()).getChildAt(1);
        fromText.setHint("From");
        fromIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_my_location_black_24dp));

        TextView toText = (TextView)((LinearLayout) autocompleteTo.getView()).getChildAt(1);
        toText.setHint("To");
        ImageView toIcon = (ImageView)((LinearLayout)autocompleteTo.getView()).getChildAt(0);
        toIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_location_on_black_24dp));*/
    }



}