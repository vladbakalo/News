package com.example.news.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.news.R;
import com.example.news.activities.ContentActivity;
import com.example.news.adapters.PeopleAdapter;
import com.example.news.entity.User;
import com.example.news.utils.DividerItemDecoration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleFragment extends BaseFragment implements PeopleAdapter.OnPersonClickListener {
    public static final String TAG = PeopleFragment.class.getSimpleName();

    private RecyclerView.LayoutManager mLayoutManager;
    private List<User> mPeople;
    private PeopleAdapter mPeopleAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPeople = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity());
        setUpPeopleRecycler(recyclerView);

        mPeopleAdapter = new PeopleAdapter(mPeople, getActivity(), this);
        recyclerView.setAdapter(mPeopleAdapter);
        getActivity().setTitle(R.string.people);
        return recyclerView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadPeople();
    }

    private void setUpPeopleRecycler(RecyclerView recycler){
        RecyclerView.LayoutParams layoutParams =
                new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        recycler.setLayoutParams(layoutParams);
        recycler.setLayoutManager(mLayoutManager);
        recycler.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.list_devider));
    }


    private void loadPeople(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPeople.clear();
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    mPeople.add(user);
                }
                mPeopleAdapter.updatePeople(mPeople);
                getDBHelper().getAllUsers().removeEventListener(this);
                Log.w(TAG, "loadAllPeople:onDataChange : " + mPeople.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        getDBHelper().getAllUsers().addValueEventListener(postListener);
    }

    @Override
    public void onPersonClick(User user) {
        ContentActivity.startSavedBundle(getActivity(), DetailPersonFragment.newInstance(user.getUId()), 0);
    }
}
