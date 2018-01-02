package com.example.news.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.news.R;
import com.example.news.retrofit.model.news.Article;
import com.example.news.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Влад on 02.01.2018.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ArticleVH>{

    private List<Article> mArticles;
    private Context mContext;
    private OnArticleClickListener mOnArticleClickListener;

    public NewsAdapter(List<Article> pArticles, @Nonnull Context pContext,
                       OnArticleClickListener pOnArcticleClickListener) {
        this.mArticles = pArticles;
        this.mContext = pContext;
        this.mOnArticleClickListener = pOnArcticleClickListener;
    }

    @Override
    public NewsAdapter.ArticleVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        return new NewsAdapter.ArticleVH(view);
    }

    @Override
    public void onBindViewHolder(ArticleVH holder, int position) {
        Article article = mArticles.get(position);
        holder.article = article;
        Picasso.with(mContext)
                .load(article.getUrlToImage())
                .placeholder(R.drawable.ic_image_load)
                .error(R.drawable.ic_image_error)
                .fit()
                .into(holder.mArticleImage);
        holder.mArticleHeader.setText(article.getTitle());
        holder.mArticleBody.setText(article.getDescription());
        holder.mArticleTime.setText(article.getPublishedAt());
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public void pArticles(List<Article> pArticles){
        mArticles = pArticles;
        notifyDataSetChanged();
    }

    public class ArticleVH extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_article_header)
        TextView mArticleHeader;
        @BindView(R.id.tv_article_body)
        TextView mArticleBody;
        @BindView(R.id.tv_article_time)
        TextView mArticleTime;
        @BindView(R.id.iv_article_image)
        ImageView mArticleImage;

        Article article;

        public ArticleVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnArticleClickListener.onArticleClick(article);
                }
            });
        }

    }

    public interface OnArticleClickListener {
        void onArticleClick(Article article);
    }
}