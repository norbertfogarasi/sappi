package com.lynxsolutions.intern.sappi.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.lynxsolutions.intern.sappi.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = ForgotPasswordActivity.class.getSimpleName();
    private EditText etEmailForgot;
    private Button btnSend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);
        initViews();
    }

    private void initViews() {
        //Initializing the views and setting the listeners
        etEmailForgot = (EditText) findViewById(R.id.etEmailForgot);
        btnSend = (Button) findViewById(R.id.btnSendEmail);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etEmailForgot.getText().toString().equals("")) {
                    sendEmail();
                } else {
                    etEmailForgot.setError(getString(R.string.et_email_error));
                }
            }
        });
    }

    private void sendEmail() {
        Log.d(TAG, "sendEmail: entry");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail(etEmailForgot.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Log.d(TAG, "passwordResetEmail: succes");
                    StringBuilder sb = new StringBuilder(getString(R.string.ing_password_reset_email_sent));
                    sb.append(" ");
                    sb.append(etEmailForgot.getText().toString());
                    Snackbar.make(findViewById(R.id.container_forgot_password), sb, Snackbar.LENGTH_LONG).show();
                }
                else {
                    Log.d(TAG, "passwordResetEmail: failed");
                    Snackbar.make(findViewById(R.id.container_forgot_password), R.string.err_failed_to_send_reset_email,
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}
