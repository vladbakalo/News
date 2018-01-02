package com.example.news.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.news.R;
import com.example.news.application.Constants;
import com.example.news.entity.User;
import com.example.news.fragments.EditProfileFragment;
import com.example.news.utils.DBHelper;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Login Activity
 */

public class LoginActivity extends AppCompatActivity implements OnCompleteListener<AuthResult>{
    public static final String TAG = LoginActivity.class.getSimpleName();

    public static final int RC_SIGN_IN = 10110;

    @BindView(R.id.input_email)
    TextInputEditText mInputEmail;
    @BindView(R.id.input_password)
    TextInputEditText mInputPass;
    @BindView(R.id.input_layout_email)
    TextInputLayout mInputLayoutEmail;
    @BindView(R.id.input_layout_password)
    TextInputLayout mInputLayoutPass;
    @BindView(R.id.login_fb_button)
    LoginButton loginButtonFB;
    @BindView(R.id.login_google_button)
    SignInButton loginButtonGoogle;

    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;

    //region LIFE-CYCLE
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        setUpProgressBar();
        setUpLogicForLoginViaFB();
        setUpLogicForLoginViaGoogle();

        checkIntentForAuth();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUiAfterAuth(currentUser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInGoogleResult(task);
        }
    }
    //endregion

    @OnClick({R.id.btn_login, R.id.link_signup, R.id.login_google_button, R.id.login_github_button})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                signIn();
                break;
            case R.id.link_signup:
                signUp();
                break;
            case R.id.login_google_button:
                signInGoogle();
                break;
            case R.id.login_github_button:
                signInGitHub();
                break;
        }
    }

    private void setUpProgressBar() {
        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.progress_login_text));
    }

    private void setUpLogicForLoginViaGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void setUpLogicForLoginViaFB() {

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        firebaseAuthWithFaceBook(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    //region SIGN-IN
    private void signIn() {
        if (!validate())
            return;
        progressDialog.show();
        String email = mInputEmail.getText().toString();
        String password = mInputPass.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, this);
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signInGitHub() {
        StringBuilder stringBuilder = new StringBuilder(Constants.OAUTH_GITHUB);
        stringBuilder.append("?");
        stringBuilder.append("client_id=" + getString(R.string.github_client_id));
        Uri webpage = Uri.parse(stringBuilder.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(intent);
    }

    private void handleSignInGoogleResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            firebaseAuthWithGoogle(account);
            //processGoogleAuth(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithFaceBook(AccessToken token) {
        Log.d(TAG, "firebaseAuthWithFaceBook:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, this);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, this);
    }

    private void firebaseAuthWithGitHub(String token) {
        Log.d(TAG, "firebaseAuthWithGitHub:" + token);

        AuthCredential credential = GithubAuthProvider.getCredential(token);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, this);
    }
    //endregion

    //region PROCESS-AUTH
    private void updateUiAfterAuth(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            progressDialog.show();
            checkIfCompleteRegistration();
        } else {
            progressDialog.dismiss();
        }
    }
    //endregion

    public boolean validate() {
        boolean valid = true;

        String email = mInputEmail.getText().toString();
        String password = mInputPass.getText().toString();

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

        return valid;
    }

    private void checkIntentForAuth(){
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null && intent.getData().getHost().equals("news-of-the-news.firebaseapp.com")){
            Uri uri = intent.getData();
            if (uri.getQueryParameter("code") != null){
                firebaseAuthWithGitHub(uri.getQueryParameter("code"));
            }
            Log.i(TAG, "Data from intent + " + intent.getData().toString());
        }
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "signInWithCredential:success");
            FirebaseUser user = mAuth.getCurrentUser();
            updateUiAfterAuth(user);
        } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "signInWithCredential:failure", task.getException());
            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed),
                    Toast.LENGTH_SHORT).show();
            updateUiAfterAuth(null);
        }
    }

    private void checkIfCompleteRegistration(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final DBHelper mDBHelper = new DBHelper(mAuth.getCurrentUser(), mDatabase);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                progressDialog.dismiss();
                User user = dataSnapshot.getValue(User.class);
                if (user == null){
                    saveUserToDb();
                    ContentActivity.startSavedBundle(LoginActivity.this, EditProfileFragment.newInstanceCompleteRegistration(), 0);
                } else {
                    if (TextUtils.isEmpty(user.getLastName()) || TextUtils.isEmpty(user.getFirstName())) {
                        ContentActivity.startSavedBundle(LoginActivity.this, EditProfileFragment.newInstanceCompleteRegistration(), 0);
                    } else {
                        goToMain();
                    }
                }
                mDBHelper.getUserReference().removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDBHelper.getUserReference().addValueEventListener(postListener);
    }

    private void goToMain(){
        MainActivity.startMainActivity(this);
        finish();
    }

    private void saveUserToDb(){
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DBHelper mDBHelper = new DBHelper(user, mDatabase);
        mDBHelper.createUser();
    }
}
