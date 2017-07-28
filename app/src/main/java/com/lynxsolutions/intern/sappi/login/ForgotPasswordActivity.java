package com.lynxsolutions.intern.sappi.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.lynxsolutions.intern.sappi.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = ForgotPasswordActivity.class.getSimpleName();
    private EditText etEmailForgot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);
        initViews();
    }

    private void initViews() {
        etEmailForgot = (EditText) findViewById(R.id.etEmailForgot);

        findViewById(R.id.btnSendEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etEmailForgot.getText().toString().equals("")) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String emailAddress = etEmailForgot.getText().toString();

                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Email sent.");
                                    }
                                }
                            });
                    Toast.makeText(ForgotPasswordActivity.this, getString(R.string.toast_email_sent), Toast.LENGTH_SHORT).show();
                } else {
                    etEmailForgot.setError(getString(R.string.et_email_error));
                }
            }
        });
    }
}
