package com.lynxsolutions.intern.sappi.profile;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lynxsolutions.intern.sappi.R;
import static android.app.Activity.RESULT_OK;
/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {

    private Uri filePath;
    private static final int PICK_IMAGE_REQUEST = 234;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refToUser = database.getReference("users");

    Button editProfileButton;
    EditText nameText,emailText,phoneText,facebookText;
    //CircleImageView profilePicture;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        initializeViews(view);
        setUserInformationToProfile();
        editProfileIfButtonPressed();
        addImageFromGallery();
        return view;
    }

    private void initializeViews(View view) {
        //profilePicture = view.findViewById(R.id.profile_image);
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

                if(filePath != null){

                    final String photoUri;
                    StorageReference picsRef = mStorageRef.child("profilePictures/dog.jpg");
                    picsRef.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    final String userId = user.getUid();
                                    refToUser.child(userId).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            UserInfo info = dataSnapshot.getValue(UserInfo.class);
                                            Log.e("Request_Data",info.getName());
                                            String name = nameText.getText().toString();
                                            String email = emailText.getText().toString();
                                            String phone = phoneText.getText().toString();
                                            //Here we make sure the realtime database is changed only if there is any change in data
                                            //refToUser.child(userId).child("name").setValue(name);
                                            setIfeverythingIsCorrect(name,email,phone,info,downloadUrl.toString(),userId);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.e("Edit_Data_Logged", "Failed to read value.", databaseError.toException());
                                        }
                                    });

                                    Toast.makeText(getContext(),"Upload Done", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(),"Story couldn't be posted",Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });
    }

    private void setIfeverythingIsCorrect(String name,String email,String phone,UserInfo userInfo,String urlToPic,String userId){

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
        if(!urlToPic.isEmpty() && !urlToPic.equals("")){
            if(!userInfo.getPhoto().equals(urlToPic)){
                refToUser.child(userId).child("photo").setValue(urlToPic);
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
                String facebbokName = "facebook.com/"+info.getName();
                facebookText.setText(facebbokName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Request_Data", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void addImageFromGallery(){

//        profilePicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//            }
//        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

        }
    }
}
