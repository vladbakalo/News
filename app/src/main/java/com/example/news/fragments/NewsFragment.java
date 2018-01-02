package com.example.news.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.news.R;
import com.example.news.activities.ContentActivity;
import com.example.news.adapters.NewsAdapter;
import com.example.news.adapters.PeopleAdapter;
import com.example.news.entity.Source;
import com.example.news.entity.User;
import com.example.news.retrofit.NewsApi;
import com.example.news.retrofit.model.news.Article;
import com.example.news.retrofit.response.news.NewsResponse;
import com.example.news.retrofit.response.news.SourceResponse;
import com.example.news.utils.DividerItemDecoration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends BaseFragment implements NewsAdapter.OnArticleClickListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = NewsFragment.class.getSimpleName();

    private RecyclerView.LayoutManager mLayoutManager;
    private List<Article> mArticles;
    private NewsAdapter mNewsAdapter;
    private Unbinder butterKniefUnbinder;

    @BindView(R.id.rv_items)
    RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private NewsApi newsApi;
    private User mUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArticles = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        butterKniefUnbinder = ButterKnife.bind(this, root);
        mLayoutManager = new LinearLayoutManager(getActivity());

        swipeRefreshLayout = (SwipeRefreshLayout) root;
        swipeRefreshLayout.setOnRefreshListener(this);
        setUpPeopleRecycler();
        mNewsAdapter = new NewsAdapter(mArticles, getActivity(), this);
        recyclerView.setAdapter(mNewsAdapter);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newsApi = mRetrofit.create(NewsApi.class);

        requestUserData();
        getProgressDialog().setMessage(getString(R.string.loading));
        getProgressDialog().show();
    }

    private void setUpPeopleRecycler(){
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.list_devider));
    }

    private void requestUserData() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mUser = user;
                getNews();
                getDBHelper().getUserReference().removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        getDBHelper().getUserReference().addValueEventListener(postListener);
    }

    private void getNews(){
        String source = Source.putSourceNames(mUser.getNewsSources());
        Log.i(TAG, "News sources: " + source);
        newsApi.getNews(getString(R.string.news_api_key), source)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(NewsResponse newsResponse) {
                        mArticles = newsResponse.getArticles();
                        updateList();
                        swipeRefreshLayout.setRefreshing(false);
                        Log.i(TAG, "News Response Status: " + newsResponse.getStatus());
                        Log.i(TAG, "News Response Data: " + newsResponse.getArticles());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.wtf(TAG, e);
                        getProgressDialog().dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void updateList(){
        mNewsAdapter.pArticles(mArticles);
        getProgressDialog().dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterKniefUnbinder.unbind();
    }

    @Override
    public void onArticleClick(Article article) {
        //ContentActivity.startSavedBundle(getActivity(), DetailArticleFragment.newInstance(article), 0);
    }

    @Override
    public void onRefresh() {
        getNews();
    }
}
