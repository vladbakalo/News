package com.example.news.activities;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.news.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = SignUpActivity.class.getSimpleName();

    @BindView(R.id.input_email)
    TextInputEditText mInputEmail;
    @BindView(R.id.input_password)
    TextInputEditText mInputPass;
    @BindView(R.id.input_password_confirm)
    TextInputEditText mInputPassConfirm;
    @BindView(R.id.input_layout_email)
    TextInputLayout mInputLayoutEmail;
    @BindView(R.id.input_layout_password)
    TextInputLayout mInputLayoutPass;
    @BindView(R.id.input_layout_password_confirm)
    TextInputLayout mInputLayoutPassConfirm;

    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.progress_sign_up_text));
    }

    @OnClick({R.id.btn_signup, R.id.link_login})
    public void onClickView(View view){
        switch (view.getId()){
            case R.id.btn_signup:
                signUp();
                break;
            case R.id.link_login:
                login();
                break;
        }
    }

    private void login(){
        this.finish();
    }

    private void signUp(){
        if (!validate())
            return;
        progressDialog.show();

        String email = mInputEmail.getText().toString();
        String pass = mInputPass.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(SignUpActivity.this, "Registration successful.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            processAuth(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                            processAuth(null);
                        }

                        // ...
                    }
                });
    }

    private void processAuth(FirebaseUser firebaseUser){
        progressDialog.dismiss();
        if (firebaseUser == null){

        } else {
            login();
        }
    }

    public boolean validate() {
        boolean valid = true;

        String email = mInputEmail.getText().toString();
        String password = mInputPass.getText().toString();
        String passwordConfirm = mInputPassConfirm.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mInputLayoutEmail.setError("enter a valid email address");
            valid = false;
        } else {
            mInputLayoutEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mInputLayoutPass.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mInputLayoutPass.setError(null);
        }

        if (passwordConfirm.isEmpty() || !passwordConfirm.equals(password)) {
            mInputLayoutPassConfirm.setError("does not match password");
            valid = false;
        } else {
            mInputLayoutPassConfirm.setError(null);
        }

        return valid;
    }
}
