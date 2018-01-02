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

import com.example.news.adapters.SourceAdapter;
import com.example.news.entity.User;
import com.example.news.retrofit.NewsApi;
import com.example.news.retrofit.model.news.Source;
import com.example.news.retrofit.response.news.SourceResponse;
import com.example.news.utils.DividerItemDecoration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SourceFragment extends BaseFragment implements SourceAdapter.OnSourceToggleClickListener {
    public static final String TAG = SourceFragment.class.getSimpleName();

    private RecyclerView.LayoutManager mLayoutManager;
    private List<Source> mSources;
    private List<com.example.news.entity.Source> mSourcesEntity;
    private SourceAdapter mSourceAdapter;

    private NewsApi newsApi;
    private User mUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSources = new ArrayList<>();
        mSourcesEntity = new ArrayList<>();
        mUser = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity());
        setUpPeopleRecycler(recyclerView);

        mSourceAdapter = new SourceAdapter(mSources, getActivity(), this);
        recyclerView.setAdapter(mSourceAdapter);
        getActivity().setTitle(R.string.simpleSource);
        return recyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newsApi = mRetrofit.create(NewsApi.class);

        newsApi.getSources(getString(R.string.news_api_key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SourceResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SourceResponse sourceResponse) {
                        mSources = sourceResponse.getSources();
                        updateList();
                        Log.i(TAG, "Source Response Status: " + sourceResponse.getStatus());
                        Log.i(TAG, "Source Response Data: " + sourceResponse.getSources());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.wtf(TAG, e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        requestUserData();
        getProgressDialog().setMessage(getString(R.string.loading));
        getProgressDialog().show();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mUser != null) {
            String source = com.example.news.entity.Source.putSourceNames(mSourcesEntity);
            Log.i(TAG, "News sources: " + source);
            mUser.setNewsSources(mSourcesEntity);
            getDBHelper().updateUser(mUser);
        }
    }

    private void setUpPeopleRecycler(RecyclerView recycler){
        RecyclerView.LayoutParams layoutParams =
                new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        recycler.setLayoutParams(layoutParams);
        recycler.setLayoutManager(mLayoutManager);
        recycler.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.list_devider));
    }

    private void updateList(){
        if (!mSources.isEmpty() && mUser != null && mUser.getNewsSources() != null && !mUser.getNewsSources().isEmpty()){
            getProgressDialog().dismiss();
            compareSources();
            mSourcesEntity = mUser.getNewsSources();
            mSourceAdapter.updateSource(mSources);
        }
    }

    private void compareSources(){
        for (Source item :
                mSources) {
            for (com.example.news.entity.Source item2 :
                    mUser.getNewsSources()) {
                if (item.getId().equals(item2.getId()))
                    item.setChecked(true);
            }
        }
    }

    private void requestUserData() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mUser = user;
                updateList();
                getDBHelper().getUserReference().removeEventListener(this);
                getProgressDialog().dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        getDBHelper().getUserReference().addValueEventListener(postListener);
    }

    @Override
    public void onSourceToggle(Source source, boolean isChecked) {
        for (com.example.news.entity.Source source1:
                mSourcesEntity) {
            if (source1.getId().equals(source.getId())){
                if (!isChecked) {
                    mSourcesEntity.remove(source1);
                }
                return;
            }
        }
        if (isChecked && mSourcesEntity.size() <= 20) {
            mSourcesEntity.add(new com.example.news.entity.Source(source.getId()));
        }
    }
}
