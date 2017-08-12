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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
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
import com.lynxsolutions.intern.sappi.main.MainActivity;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {

    private static final String TAG = "EditProfileFragment";
    private UserInfo info;
    private Uri filePath;
    private static final int PICK_IMAGE_REQUEST = 234;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refToUser = database.getReference("users");

    private Button editProfileButton;
    private TextView tvName;
    private EditText emailText, etPhone, etFacebook;
    private CircleImageView profilePicture;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Edit Profile");
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        initializeViews(view);
        info = new UserInfo();
        setUserInformationToProfile();
        editProfileIfButtonPressed();
        addImageFromGallery();
        return view;
    }

    private void initializeViews(View view) {
        profilePicture = view.findViewById(R.id.fragment_edit_profile_image);
        tvName = view.findViewById(R.id.fragment_edit_profile_tv_name);
        emailText = view.findViewById(R.id.fragment_edit_profile_et_email);
        etPhone = view.findViewById(R.id.fragment_edit_profile_et_phone);
        etFacebook = view.findViewById(R.id.fragment_edit_profile_et_facebook);
        editProfileButton = view.findViewById(R.id.fragment_edit_profile_button_save);
    }

    private void editProfileIfButtonPressed() {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = user.getUid();
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (filePath != null) {

                    final String photoUri;
                    StorageReference picsRef = mStorageRef.child("profilePictures/" + userId);
                    picsRef.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Log.e("Test",taskSnapshot.getMetadata().getReference().toString());
                                    final String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                                    String name = tvName.getText().toString();
                                    String email = emailText.getText().toString();
                                    String phone = etPhone.getText().toString();
                                    setIfeverythingIsCorrect(name, email, phone, info, downloadUrl, userId);
                                    Toast.makeText(getContext(), "Upload Done", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Image Uploading Failed", Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    final String userId = user.getUid();
                    refToUser.child(userId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserInfo info = dataSnapshot.getValue(UserInfo.class);
                            Log.e("Request_Data", info.getName());
                            String name = tvName.getText().toString();
                            String email = emailText.getText().toString();
                            String phone = etPhone.getText().toString();
                            Log.d("EditProfileFragment", "phone: " + phone);
                            //Here we make sure the realtime database is changed only if there is any change in data
                            //refToUser.child(userId).child("name").setValue(name);
                            setIfeverythingIsCorrect(name, email, phone, info, info.getPhoto(), userId);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("Edit_Data_Logged", "Failed to read value.", databaseError.toException());
                        }
                    });
                }
            }
        });
    }

    private void setIfeverythingIsCorrect(String name, String email, String phone, UserInfo userInfo, String urlToPic, String userId) {

        if (!name.isEmpty() && !name.equals("")) {
            if (!userInfo.getName().equals(name)) {
                refToUser.child(userId).child("name").setValue(name);
            }
        }
        if (!email.isEmpty() && !email.equals("")) {
            if (!userInfo.getEmail().equals(email)) {
                refToUser.child(userId).child("email").setValue(email);
            }
        }
        if (!phone.isEmpty()) {
            Log.d(TAG, "setIfeverythingIsCorrect: phone number pass 1");
            if (!userInfo.getPhonenumber().equals(phone)) {
                Log.d(TAG, "setIfeverythingIsCorrect: phone number pass 2");
                refToUser.child(userId).child("phonenumber").setValue(phone);
            }
        }
        if (!urlToPic.isEmpty() && !urlToPic.equals("")) {
            if (!userInfo.getPhoto().equals(urlToPic)) {
                refToUser.child(userId).child("photo").setValue(urlToPic);
            }
        }

    }

    private void setUserInformationToProfile() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        refToUser.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                info = dataSnapshot.getValue(UserInfo.class);
                tvName.setText(info.getName());
                emailText.setText(info.getEmail());
                if (!etPhone.getText().toString().equals(getString(R.string.et_no_phone_hint))) {
                    etPhone.setText(info.getPhonenumber());
                }
                String facebookName = "facebook.com/" + info.getName();
                //Removing the spaces from the name
                facebookName = facebookName.replace(" ", "");
                etFacebook.setText(facebookName);
                try {
                    Glide.with(getContext()).load(info.getPhoto()).centerCrop().into(profilePicture);
                }catch (IllegalArgumentException ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Request_Data", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void addImageFromGallery() {

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Glide.with(getContext()).load(filePath).diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
                    .into(profilePicture);
        }
    }
}
