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
import com.example.news.retrofit.model.news.Article;
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
 */
public class DetailArticleFragment extends BaseFragment {

    public static final String B_ARTICLE = "B_ARTICLE";

    @BindView(R.id.iv_article_image)
    ImageView mArticleImage;
    @BindView(R.id.tv_article_full_title)
    TextView mArticleTitle;
    @BindView(R.id.tv_article_full_description)
    TextView mArticleDescription;
    @BindView(R.id.tv_article_full_source)
    TextView mArticleSource;
    @BindView(R.id.tv_article_full_time)
    TextView mArticleTime;

    private Unbinder butterKniefUnbinder;
    private Article mArticle;

    public static SavedBundle newInstance(Article article){
        Bundle bundle = new Bundle();
        bundle.putParcelable(B_ARTICLE, article);
        return SavedBundle.create(DetailArticleFragment.class, bundle);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mArticle = getArguments().getParcelable(B_ARTICLE);
        }
        if (savedInstanceState != null){
            mArticle = savedInstanceState.getParcelable(B_ARTICLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail_article, container, false);
        butterKniefUnbinder = ButterKnife.bind(this, root);

        getActivity().setTitle(R.string.article);
        displayArticle();
        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(B_ARTICLE, mArticle);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        butterKniefUnbinder.unbind();
        super.onDestroyView();
    }

    private void displayArticle() {
        Picasso.with(getActivity())
                .load(mArticle.getUrlToImage())
                .placeholder(R.drawable.ic_image_load)
                .error(R.drawable.ic_image_error)
                .into(mArticleImage);
        mArticleDescription.setText(mArticle.getDescription());
        mArticleSource.setText(mArticle.getSource().getName());
        mArticleTime.setText(mArticle.getPublishedAt());
        mArticleTitle.setText(mArticle.getTitle());

    }

}
