package com.example.news.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.news.R;
import com.example.news.activities.ContentActivity;
import com.example.news.utils.SavedBundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    @BindView(R.id.btn_edit_profile)
    Button mEditProfile;

    private Unbinder mUnbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        mUnbinder = ButterKnife.bind(this, root);

        getActivity().setTitle(R.string.profile);
        return root;
    }

    @OnClick({R.id.btn_edit_profile})
    public void onClickView(View view){
        switch (view.getId()){
            case R.id.btn_edit_profile:
                ContentActivity.startSavedBundle(getActivity(), SavedBundle.create(EditProfileFragment.class, null), 0);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
