package com.lynxsolutions.intern.sappi.events;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lynxsolutions.intern.sappi.R;
import com.lynxsolutions.intern.sappi.cars.CarFeedFragment;
import com.lynxsolutions.intern.sappi.main.NavigationManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailFragment extends Fragment {

    private Event event;
    private NavigationManager manager;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    public EventDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);
        getActivity().setTitle("Event Detail");
        event = (Event) getArguments().get("event");
        manager = new NavigationManager(getFragmentManager());
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(event.getImageUrl());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        String stamp=Long.toString(System.currentTimeMillis());
        Event post=new Event("gs://sappi-ccc6a.appspot.com/TT20100723_204.jpg","AUG\n4","Tusvanyos 2017","9B1EwtScyFcxsznlKXNlLDlg0pm1","Lorem ipsum lorem pismum sdfsdfg" +
                "fgsdfgdfhfgjhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhgdfgdfgdf.!!!?",stamp,"Tusnad");

        //mDatabase.child("events").child(stamp).setValue(post);


        ImageView image = (ImageView)view.findViewById(R.id.eventdetailpic);
        TextView location=(TextView) view.findViewById(R.id.eventdetaillocation);
        TextView descript=(TextView) view.findViewById(R.id.eventdetaildescript);
        location.setText(event.getLocation());
        descript.setText(event.getDescription());
        descript.setMovementMethod(new ScrollingMovementMethod());
        view.findViewById(R.id.textView16).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.switchToMainFragment(new CarFeedFragment());
            }
        });

        view.findViewById(R.id.imageView17).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = mAuth.getCurrentUser();
                String uId = user.getUid();
                mDatabase.child("favorites").child(uId).child(event.getTimestamp()).setValue(event);
                Toast.makeText(getContext(),"Added to favorites",Toast.LENGTH_SHORT).show();
            }
        });


        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(image );
        return view;
    }

}