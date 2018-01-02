package com.example.news.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.news.R;
import com.example.news.retrofit.model.news.Source;
import com.example.news.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Влад on 02.01.2018.
 *
 */

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.SourceVH>{

    private List<Source> mSources;
    private Context mContext;
    private OnSourceToggleClickListener mOnSourceToggleClickListener;

    public SourceAdapter(List<Source> pSources, @Nonnull Context pContext,
                         OnSourceToggleClickListener pOnSourceToggleClickListener) {
        this.mSources = pSources;
        this.mContext = pContext;
        this.mOnSourceToggleClickListener = pOnSourceToggleClickListener;
    }

    @Override
    public SourceAdapter.SourceVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.source_item, parent, false);
        return new SourceAdapter.SourceVH(view);
    }

    @Override
    public void onBindViewHolder(SourceAdapter.SourceVH holder, int position) {
        Source source = mSources.get(position);
        holder.source = source;
        holder.sourceName.setText(source.getName());
        holder.sourceSwitch.setChecked(source.isChecked());
    }

    @Override
    public int getItemCount() {
        return mSources.size();
    }

    public void updateSource(List<Source> pSources){
        mSources = pSources;
        notifyDataSetChanged();
    }

    public class SourceVH extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_source_name)
        TextView sourceName;
        @BindView(R.id.iv_source_switch)
        Switch sourceSwitch;

        Source source;

        public SourceVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            sourceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    source.setChecked(isChecked);
                    if (mOnSourceToggleClickListener != null)
                        mOnSourceToggleClickListener.onSourceToggle(source, isChecked);
                }
            });
        }

    }

    public interface OnSourceToggleClickListener{
        void onSourceToggle(Source source, boolean isChecked);
    }
}
