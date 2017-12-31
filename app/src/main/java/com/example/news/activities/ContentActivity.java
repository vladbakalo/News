package com.example.news.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.example.news.R;
import com.example.news.utils.SavedBundle;
import com.example.news.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.internal.Util;

public class ContentActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    FrameLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        ButterKnife.bind(this);
        setUpToolBar();

        if (getIntent() != null
                && getIntent().getExtras() != null
                && getIntent().getExtras().containsKey(SavedBundle.class.getName())) {
            showPresentations(SavedBundle.fromIntent(getIntent()));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setUpToolBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void showPresentations(SavedBundle savedBundle) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = Utils.getFragmentFromBundle(this, savedBundle.getClassName());

        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (savedBundle.getBundle() != null) {
            fragment.setArguments(savedBundle.getBundle());
        }
        ft.replace(R.id.container, fragment, savedBundle.getClassName());
        ft.commit();
    }

    public static void startSavedBundle(Activity pActivity, SavedBundle pSavedBundle, int pFlags) {
        Intent intent = new Intent(pActivity, ContentActivity.class);
        intent.setFlags(pFlags);
        pSavedBundle.store(intent);
        pActivity.startActivity(intent);

    }
}
