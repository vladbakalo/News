package com.example.news.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.news.R;
import com.example.news.utils.DBHelper;
import com.example.news.utils.StorageHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Влад on 31.12.2017.
 *
 */

public class BaseFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DBHelper mDBHelper;
    private StorageReference mStorageRef;
    private StorageHelper mStorageHelper;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDBHelper = new DBHelper(mAuth.getCurrentUser(), mDatabase);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mStorageHelper = new StorageHelper(mStorageRef);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setUpProgressDialog(getActivity());
        super.onActivityCreated(savedInstanceState);
    }

    private void setUpProgressDialog(Context context){
        progressDialog = new ProgressDialog(context,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.progress_sign_up_text));
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

    public StorageReference getStorageRef() {
        return mStorageRef;
    }

    public StorageHelper getStorageHelper() {
        return mStorageHelper;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }
}
