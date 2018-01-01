package com.example.news.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.news.utils.DBHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Влад on 31.12.2017.
 */

public class BaseFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DBHelper mDBHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDBHelper = new DBHelper(mAuth.getCurrentUser(), mDatabase);
        super.onCreate(savedInstanceState);
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    public DatabaseReference getDatabase() {
        return mDatabase;
    }

    public DBHelper getDBHelper() {
        return mDBHelper;
    }
}
