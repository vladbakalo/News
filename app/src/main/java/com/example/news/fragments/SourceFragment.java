package com.example.news.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.news.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SourceFragment extends BaseFragment {
    public static final String TAG = SourceFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_source, container, false);

        getActivity().setTitle(R.string.simpleSource);
        return root;
    }

}
