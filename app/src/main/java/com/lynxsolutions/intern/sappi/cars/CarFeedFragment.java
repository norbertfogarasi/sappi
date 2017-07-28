package com.lynxsolutions.intern.sappi.cars;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lynxsolutions.intern.sappi.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CarFeedFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<CarRoute> list;
    private FloatingActionButton fab;

    public CarFeedFragment() {}

    private RecyclerView mPostRV;
    private FirebaseRecyclerAdapter<CarRoute, PostViewHolder> mPostAdapter;
    private DatabaseReference mPostRef;



    public static class PostViewHolder extends RecyclerView.ViewHolder {


        public TextView vfrom;
        public TextView vusername;
        public TextView vto;


        public PostViewHolder(View itemView) {
            super(itemView);
            vfrom=(TextView) itemView.findViewById(R.id.from_tv);
            vusername=(TextView) itemView.findViewById(R.id.username_tv);
            vto=(TextView)  itemView.findViewById(R.id.to_tv);
        }

    }

    private void sendPostToFirebase() {

        String UID = "5634t5erfgsdg";

        CarRoute car=new CarRoute("Tokyo","Koronka","New York_Koronka","asedfasdfas","9898797867","4234erw","2017 jun 12","John");


        mPostRef.child(Long.toString(System.currentTimeMillis())).setValue(car);
    }

    private void setupAdapter() {
        mPostAdapter = new FirebaseRecyclerAdapter<CarRoute, PostViewHolder>(
                CarRoute.class,
                R.layout.recycler_item,
                PostViewHolder.class,
                mPostRef
        ) {

            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, final CarRoute model, int position) {




                viewHolder.vfrom.setText(model.getFrom());
                viewHolder.vto.setText(model.getTo());
                viewHolder.vusername.setText(model.getUsername());
                /*viewHolder.postLikeIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateNumLikes(model.getUID());
                    }
                });*/
            }
        };

    }


    /////////////////////////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_car_feed, container, false);
        mPostRV = (RecyclerView) view.findViewById(R.id.recycler_view2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mPostRV.setLayoutManager(layoutManager);
        mPostRef = FirebaseDatabase.getInstance().getReference("cars");
        setupAdapter();
        mPostRV.setAdapter(mPostAdapter);


        //initialiseScreen();
        sendPostToFirebase();
        /*
        recyclerView = view.findViewById(R.id.recycler_view2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fab = view.findViewById(R.id.floating_action_button2);
        fab.setOnClickListener(this);


        list = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            list.add(new CarRoute("innen" + (i+1), "oda", "usr" + (i+1)));
        }
        adapter = new MyAdapter(list,getActivity().getApplicationContext());
        recyclerView.setAdapter(adapter);
*/
        return view;
    }
/*
    @Override
    public void onClick(View view) {
        DetailedFragment fragment2 = new DetailedFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.car_feed, fragment2);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
   */
}