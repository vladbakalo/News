package com.example.news.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.news.R;
import com.example.news.application.NewsApplication;
import com.example.news.application.views.RoundedImageView;
import com.example.news.fragments.EditProfileFragment;
import com.example.news.fragments.NewsFragment;
import com.example.news.fragments.PeopleFragment;
import com.example.news.fragments.ProfileFragment;
import com.example.news.fragments.SourceFragment;
import com.example.news.fragments.WeatherFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    Retrofit mRetrofit;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    FrameLayout mContainer;

    ImageView mUserAvatar;
    TextView mUserEmail;
    TextView mUserFullName;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((NewsApplication) getApplication()).getNetComponent().inject(this);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        setUpDrawer();
        Log.wtf(TAG, "OnCreate Main activity");

    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpProfileInformation();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_exit) {
            logOut();
        } else {
            onNavigationSelected(id);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setUpDrawer(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        mUserAvatar = header.findViewById(R.id.riv_nav_avatar);
        mUserEmail = header.findViewById(R.id.tv_nav_email);
        mUserFullName = header.findViewById(R.id.tv_nav_full_name);
    }

    private void logOut(){
        mAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void onNavigationSelected(int viewId){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;
        switch (viewId){
            case R.id.nav_news:
                fragment = new NewsFragment();
                break;
            case R.id.nav_source:
                fragment = new SourceFragment();
                break;
            case R.id.nav_weather:
                fragment = new WeatherFragment();
                break;
            case R.id.nav_people:
                fragment = new PeopleFragment();
                break;
            case R.id.nav_profile:
                fragment = new ProfileFragment();
                break;
        }
        if (fragment == null)
            return;
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private void setUpProfileInformation(){
//        Picasso.with(this)
//                .load("https://9to5mac.files.wordpress.com/2017/11/face-id1.jpg?quality=82&strip=all&w=1500")
//                .placeholder(R.drawable.ic_image_load)
//                .error(R.drawable.ic_image_error)
//                .fit()
//                .into(mUserAvatar);
        mUserEmail.setText(mAuth.getCurrentUser().getEmail());
        mUserFullName.setText(mAuth.getCurrentUser().getDisplayName());
    }

    public static void startMainActivity(Activity activity){
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }
}
