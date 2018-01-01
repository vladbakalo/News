package com.example.news.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.news.R;
import com.example.news.activities.ContentActivity;
import com.example.news.application.enums.ECity;
import com.example.news.application.enums.ECountry;
import com.example.news.entity.User;
import com.example.news.utils.SavedBundle;
import com.example.news.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class DetailPersonFragment extends BaseFragment {
    public static final String TAG = DetailPersonFragment.class.getSimpleName();

    public static final String B_USER_UID = "B_USER_UID";

    @BindView(R.id.iv_person_avatar)
    ImageView mUserAvatar;
    @BindView(R.id.tv_profile_first_name)
    TextView mFirstNameView;
    @BindView(R.id.tv_profile_last_name)
    TextView mLastNameView;
    @BindView(R.id.tv_profile_phone)
    TextView mPhoneNameView;
    @BindView(R.id.tv_profile_country)
    TextView mCountryView;
    @BindView(R.id.tv_profile_city)
    TextView mCityView;
    @BindView(R.id.tv_profile_birth_day)
    TextView mBirthDayView;

    private String mUserUid;
    private Unbinder butterKniefUnbinder;
    private User mUser;

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
        if (savedInstanceState != null && savedInstanceState.containsKey(B_USER_UID)){
            mUserUid = savedInstanceState.getString(B_USER_UID);
        }

        if (mUserUid.equals(getAuth().getUid()))
            setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail_person, container, false);
        butterKniefUnbinder = ButterKnife.bind(this, root);

        getActivity().setTitle(R.string.profile);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestUserData();
        getProgressDialog().setMessage(getString(R.string.loading));
        getProgressDialog().show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit_profile:
                ContentActivity.startSavedBundle(getActivity(), EditProfileFragment.newInstanceSimpleEdit(), 0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(B_USER_UID, mUserUid);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        butterKniefUnbinder.unbind();
        super.onDestroyView();
    }

    private void requestUserData() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mUser = user;
                displayUserData();
                getDBHelper().getUserReference(mUserUid).removeEventListener(this);
                getProgressDialog().dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        getDBHelper().getUserReference(mUserUid).addValueEventListener(postListener);
    }

    private void displayUserData() {
        Picasso.with(getActivity())
                .load(mUser.getPhotoPath())
                .placeholder(R.drawable.ic_image_load)
                .error(R.drawable.ic_image_error)
                .into(mUserAvatar);
        mFirstNameView.setText(mUser.getFirstName());
        mLastNameView.setText(mUser.getLastName());
        mPhoneNameView.setText(mUser.getPhone());
        mCountryView.setText(ECountry.getContryById(mUser.getCountryId()).getNameResId());
        mCityView.setText(ECity.getCitiyById(mUser.getCityId()).getNameResId());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mUser.getBirthDay());
        mBirthDayView.setText(Utils.getReadableDate(calendar));
    }
}
