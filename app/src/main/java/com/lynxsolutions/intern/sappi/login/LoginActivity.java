package com.lynxsolutions.intern.sappi.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
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
import com.lynxsolutions.intern.sappi.main.MainActivity;
import com.lynxsolutions.intern.sappi.R;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;
    private int progressStatus = 1;

    private EditText mEditTextEmail, mEditTextPassword;
    private Button mLoginButton;
    private TextView tvRegister;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;
    private DatabaseReference mDatabase;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //Checking if I navigated here from the RegisterActivity
        Intent i = getIntent();
        if (i.getExtras() != null) {
            Log.d("stringExtra", i.getStringExtra("email"));
            mEditTextEmail.setText(i.getStringExtra("email"), TextView.BufferType.EDITABLE);
            mEditTextEmail.setSelection(mEditTextEmail.getText().length());
        }

        //Firebase authentication set
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

        //Firebase Google sign-in authentication set
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //Firebase Facebook sign-in authentication set
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.btnFacebook);
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

    private void initViews() {
        //Initializes the views and sets up the listeners
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //Color the textview
        final SpannableStringBuilder sb = new SpannableStringBuilder(tvRegister.getText().toString());
        final ForegroundColorSpan fcs = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary));
        sb.setSpan(fcs, 23, 31, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tvRegister.setText(sb);

        //Initializing the EdiText fields
        mEditTextEmail = (EditText) findViewById(R.id.etEmail);
        mEditTextPassword = (EditText) findViewById(R.id.etPassword);

        //Logiv via email and password
        mLoginButton = (Button) findViewById(R.id.btnLogIn);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validFields()) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setMax(100);
                    new AsyncTask<Void, Integer, Void>() {

                        @Override
                        protected Void doInBackground(final Void... params) {
                            firebaseAuthWithEmailAndPassword();
                            publishProgress(25, 50, 75, 100);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(final Void result) {
                            // Update your views here
//                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        protected void onProgressUpdate(Integer... values) {
                            //super.onProgressUpdate(values);
                            for (int i = 0; i < values.length; i++) {
                                progressBar.setProgress(values[i]);
                                Log.d("onProgressUpdate", values[i].toString());
                            }
                        }
                    }.execute();
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        findViewById(R.id.tvForgotPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        ImageButton mGoogleBtn = (ImageButton) findViewById(R.id.btnGoogle);
        //Google sign-in button
        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private boolean validFields() {
        //Checks if the fields are valid
        boolean valid = true;
        if (mEditTextEmail.getText().toString().equals("")) {
            valid = false;
            mEditTextEmail.setError(getString(R.string.et_email_error));
        }
        if (mEditTextPassword.getText().toString().equals("")) {
            valid = false;
            mEditTextPassword.setError(getString(R.string.et_password_error));
        }
        return valid;
    }


    private boolean firebaseAuthWithEmailAndPassword() {
        mAuth.signInWithEmailAndPassword(mEditTextEmail.getText().toString(), mEditTextPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmailAndPassword: success");
                    progressBar.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Log.d(TAG, "signInWithEmailAndPassword: failure");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthInvalidUserException) {
                    mEditTextEmail.setError(getString(R.string.et_no_user));
                }
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    mEditTextEmail.setError(getString(R.string.et_invalid_email_or_password));
                    mEditTextPassword.setError(getString(R.string.et_invalid_email_or_password));
                }
            }
        });
        return true;
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        progressBar.setVisibility(View.VISIBLE);
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(final Void... params) {
                // Do your loading here. Don't touch any views from here, and then return null
                Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

                AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
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
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                                    Snackbar snackbar = Snackbar.make(findViewById(R.id.login_container),
                                            R.string.err_failed_to_log_in, Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            }
                        });
                return null;
            }


            @Override
            protected void onPostExecute(final Void result) {
                // Update your views here
                progressBar.setVisibility(View.INVISIBLE);
            }
        }.execute();
    }

    private void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            writeNewUser(user.getUid(), user.getEmail(), user.getDisplayName(), user.getPhoneNumber(), user.getPhotoUrl().toString());
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.login_container),
                                    R.string.err_failed_to_log_in, Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not be available
        Log.d(TAG, "onConnectionFailed " + connectionResult);
    }

    private void writeNewUser(String userId, String email, String name, String phonenumber, String photo) {
        //Writes a user to the realtime database
        mDatabase.child("users").child(userId).child("email").setValue(email);
        mDatabase.child("users").child(userId).child("name").setValue(name);
        mDatabase.child("users").child(userId).child("phonenumber").setValue(phonenumber);
        mDatabase.child("users").child(userId).child("photo").setValue(photo);
    }
}