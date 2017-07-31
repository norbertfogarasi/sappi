package com.lynxsolutions.intern.sappi.profile;


import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lynxsolutions.intern.sappi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refToUser = database.getReference("users");

    Button editProfileButton;
    EditText nameText,emailText,phoneText,facebookText;
    ImageView profilePicture;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        initializeViews(view);
        setUserInformationToProfile();
        editProfileIfButtonPressed();
        return view;
    }

    private void initializeViews(View view) {
        profilePicture = view.findViewById(R.id.profile_image);
        nameText = view.findViewById(R.id.edit_name_text);
        emailText = view.findViewById(R.id.edit_email_text);
        phoneText = view.findViewById(R.id.edit_mobile_text);
        facebookText = view.findViewById(R.id.edit_facebook_text);
        editProfileButton = view.findViewById(R.id.save_profile_button);
    }

    private void editProfileIfButtonPressed(){

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String userId = user.getUid();
                refToUser.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserInfo info = dataSnapshot.getValue(UserInfo.class);
                        Log.e("Request_Data",info.getName().toString());
                        String name = nameText.getText().toString();
                        String email = emailText.getText().toString();
                        String phone = phoneText.getText().toString();
                        String url = "gs://sappi-ccc6a.appspot.com/default.png";
                        //Here we make sure the realtime database is changed only if there is any change in data
                        //refToUser.child(userId).child("name").setValue(name);
                        setIfeverythingIsCorrect(name,email,phone,info,userId);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Edit_Data_Logged", "Failed to read value.", databaseError.toException());
                    }
                });
            }
        });
    }

    private void setIfeverythingIsCorrect(String name,String email,String phone,UserInfo userInfo,String userId){

        if(!name.isEmpty() && !name.equals("")){
            if(!userInfo.getName().equals(name)){
                refToUser.child(userId).child("name").setValue(name);
            }
        }
        if(!email.isEmpty() && !email.equals("")){
            if(!userInfo.getEmail().equals(email)){
                refToUser.child(userId).child("email").setValue(email);;
            }
        }
        if(!phone.isEmpty() && !phone.equals("")){
            if(!userInfo.getPhonenumber().equals(phone)){
                refToUser.child(userId).child("phonenumber").setValue(phone);
            }
        }
    }

    private void setUserInformationToProfile(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        refToUser.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo info = dataSnapshot.getValue(UserInfo.class);
                Log.e("Request_Data",info.getName());
                nameText.setText(info.getName());
                emailText.setText(info.getEmail());
                phoneText.setText(info.getPhonenumber());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Request_Data", "Failed to read value.", databaseError.toException());
            }
        });
    }

}
