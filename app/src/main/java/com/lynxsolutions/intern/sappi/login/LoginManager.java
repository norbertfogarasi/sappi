package com.lynxsolutions.intern.sappi.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lynxsolutions.intern.sappi.R;
import com.lynxsolutions.intern.sappi.main.MainActivity;
import com.lynxsolutions.intern.sappi.profile.UserInfo;

/**
 * Created by fogarasinorbert on 31.07.2017.
 */

class LoginManager {
    private static final String TAG = LoginManager.class.getSimpleName();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;
    private DatabaseReference mDatabase;
    private AppCompatActivity context;

    public LoginManager(AppCompatActivity context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: signed_in" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }
            }
        };
        mDatabase = FirebaseDatabase.getInstance().getReference();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(context, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(TAG, "onConnectionFailed: " + connectionResult);
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mCallbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) context.findViewById(R.id.btnFacebook);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
    }

    public void signInWithEmailAndPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmailAndPassword: success");
                    //progressBar.setVisibility(View.INVISIBLE);
                    context.startActivity(new Intent(context, MainActivity.class));
                    context.finish();
                } else {
                    Log.d(TAG, "signInWithEmailAndPassword: failure");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthInvalidUserException) {
                    Log.d(TAG, "onFailure: No user with this email");
                }
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Log.d(TAG, "onFailure: Invalid email or password");
//                    mEditTextEmail.setError(getString(R.string.et_invalid_email_or_password));
//                    mEditTextPassword.setError(getString(R.string.et_invalid_email_or_password));
                }
            }
        });
    }

    public void signInWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

//                                    String uId = user.getUid();
//                                    writeNewUser(uId, etEmail.getText().toString(), etName.getText().toString(),
//                                            etPhoneNumber.getText().toString(), PHOTO_URL);

                            //Write new user to database
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            writeNewUser(user.getUid(), user.getEmail(), user.getDisplayName(), user.getPhoneNumber(), user.getPhotoUrl().toString());
                            context.startActivity(new Intent(context, MainActivity.class));
                            context.finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar snackbar = Snackbar.make(context.findViewById(R.id.login_container),
                                    R.string.err_failed_to_log_in, Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                });
    }

    public void signInWithFacebook() {

    }

    public void writeNewUser(String userId, String email, String name, String phonenumber, String photo) {
        //Writes a user to the realtime database
        UserInfo user = new UserInfo(email, name, phonenumber, photo);
        mDatabase.child("users").child(userId).setValue(user);
//        mDatabase.child("users").child(userId).child("email").setValue(email);
//        mDatabase.child("users").child(userId).child("name").setValue(name);
//        mDatabase.child("users").child(userId).child("phonenumber").setValue(phonenumber);
//        mDatabase.child("users").child(userId).child("photo").setValue(photo);
    }

    public void addAuthStateListener() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void detachListener() {
        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            writeNewUser(user.getUid(), user.getEmail(), user.getDisplayName(), user.getPhoneNumber(), user.getPhotoUrl().toString());
                            context.startActivity(new Intent(context, MainActivity.class));
                            context.finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar snackbar = Snackbar.make(context.findViewById(R.id.login_container),
                                    R.string.err_failed_to_log_in, Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                });
    }

    private void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        context.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(final Void... params) {
                // Do your loading here. Don't touch any views from here, and then return null
                signInWithGoogle(acct);
                return null;
            }


            @Override
            protected void onPostExecute(final Void result) {
                // Update your views here
            }
        }.execute();
    }
}

