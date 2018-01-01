package com.example.news.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.news.R;
import com.example.news.utils.SavedBundle;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailPersonFragment extends Fragment {
    public static final String TAG = DetailPersonFragment.class.getSimpleName();

    public static final String B_USER_UID = "B_USER_UID";

    private String mUserUid;

    public static SavedBundle newInstance(String pUserUid){
        Bundle bundle = new Bundle();
        bundle.putString(B_USER_UID, pUserUid);
        return SavedBundle.create(DetailPersonFragment.class, bundle);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(B_USER_UID)){
            mUserUid = getArguments().getString(B_USER_UID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail_person, container, false);

        return root;
    }

}
