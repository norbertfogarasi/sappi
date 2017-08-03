package com.lynxsolutions.intern.sappi.profile;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lynxsolutions.intern.sappi.R;
import com.lynxsolutions.intern.sappi.main.MainActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private CircleImageView profilePicture;
    Button editProfileButton;
    TextView emailText,phoneText,facebookText,nameText;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initializeViews(view);
        getDataFromDatabase();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Profile");
    }

    private void getDataFromDatabase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference reToUser= FirebaseDatabase.getInstance().getReference("users");
        reToUser.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo info = dataSnapshot.getValue(UserInfo.class);
                nameText.setText(info.getName());
                emailText.setText(info.getEmail());
                phoneText.setText(info.getPhonenumber());
                try{
                    Glide.with(getContext()).load(info.getPhoto()).diskCacheStrategy(DiskCacheStrategy.NONE).signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                            .into(profilePicture);
                }catch (IllegalArgumentException ex){
                    ex.printStackTrace();
                }
                String facebbokName = "facebook.com/"+info.getName();
                facebookText.setText(facebbokName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initializeViews(View view) {
        profilePicture = view.findViewById(R.id.profile_image);
        nameText = view.findViewById(R.id.nameofuser_text);
        emailText = view.findViewById(R.id.email_text);
        phoneText = view.findViewById(R.id.mobile_text);
        facebookText = view.findViewById(R.id.facebook_text);
        editProfileButton = view.findViewById(R.id.edit_profile_button);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).switchToFragment(new EditProfileFragment());
            }
        });
    }
}
