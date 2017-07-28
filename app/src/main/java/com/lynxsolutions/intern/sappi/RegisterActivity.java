package com.lynxsolutions.intern.sappi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private static final String PHOTO_URL = "gs://sappi-36768.appspot.com/default.png";

    private EditText etEmail, etPassword, etName, etPasswordConfirm, etPhoneNumber;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        initViews();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    private void initViews() {
        //Initializes the views and sets up the listeners
        etEmail = (EditText) findViewById(R.id.etEmailReg);
        etName = (EditText) findViewById(R.id.etNameReg);
        etPassword = (EditText) findViewById(R.id.etPasswordReg);
        etPasswordConfirm = (EditText) findViewById(R.id.etPasswordConfirmReg);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);

        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validFields()) {
                    createAccount(etEmail.getText().toString(), etPassword.getText().toString());
                }
            }
        });
    }

    private void createAccount(String email, String password) {
        //Creates an account with email and password
        Log.d(TAG, "createAccount: " + email);
        if(validFields()) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail: success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String uId = user.getUid();
                            writeNewUser(uId, etEmail.getText().toString(), etName.getText().toString(),
                                    etPhoneNumber.getText().toString(), PHOTO_URL);
                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            i.putExtra("email", etEmail.getText().toString());
                            startActivity(i);
                            finish();
                        } else {
                            Log.d(TAG, "createAccount: user don't exists!");
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        }
                    } else {
                        Log.d(TAG, "createUserWithEmail: failure");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if(e instanceof FirebaseAuthUserCollisionException) {
                        etEmail.setError(getString(R.string.et_user_exists));
                    }
                    else if(e instanceof FirebaseAuthWeakPasswordException) {
                        etPassword.setError(getString(R.string.et_password_too_weak));
                        etPasswordConfirm.setError(getString(R.string.et_password_too_weak));
                    }
                }
            });
        }
    }

    private boolean validFields() {
        //Checks if the fields are valid
        boolean valid = true;
        if (etEmail.getText().toString().equals("")) {
            valid = false;
            etEmail.setError(getString(R.string.et_email_error));
        }
        if (etName.getText().toString().equals("")) {
            etName.setError(getString(R.string.et_name_error));
            valid = false;
        }
        if (etPassword.getText().toString().equals("")) {
            etPassword.setError(getString(R.string.et_password_error));
            valid = false;
        }
        if (etPasswordConfirm.getText().toString().equals("")) {
            valid = false;
            etPasswordConfirm.setError(getString(R.string.et_password_confirm_error));
        }
        if (!etPassword.getText().toString().equals(etPasswordConfirm.getText().toString())) {
            valid = false;
            etPassword.setError(getString(R.string.et_password_dont_match));
        }
        return valid;
    }

    private void writeNewUser(String userId, String email, String name, String phonenumber, String photo) {
        //Writes a user to the realtime database
        mDatabase.child("users").child(userId).child("email").setValue(email);
        mDatabase.child("users").child(userId).child("name").setValue(name);
        mDatabase.child("users").child(userId).child("phonenumber").setValue(phonenumber);
        mDatabase.child("users").child(userId).child("photo").setValue(photo);
    }

}
