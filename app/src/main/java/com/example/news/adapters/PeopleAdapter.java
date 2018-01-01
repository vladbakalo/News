package com.example.news.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.news.R;
import com.example.news.entity.User;
import com.example.news.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Влад on 31.12.2017.
 *
 */

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PersonVH> {
    private List<User> mPeople;
    private Context mContext;
    private OnPersonClickListener mOnPersonClickListener;

    public PeopleAdapter(List<User> pPeople, @Nonnull Context pContext,
                         OnPersonClickListener pOnPersonClickListener) {
        this.mPeople = pPeople;
        this.mContext = pContext;
        this.mOnPersonClickListener = pOnPersonClickListener;
    }

    @Override
    public PersonVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_item, parent, false);
        return new PersonVH(view);
    }

    @Override
    public void onBindViewHolder(PersonVH holder, int position) {
        User person = mPeople.get(position);
        holder.person = person;
        Picasso.with(mContext)
                .load(person.getPhotoPath())
                .placeholder(R.drawable.ic_image_load)
                .error(R.drawable.ic_image_error)
                .transform(new CircleTransform())
                .into(holder.personAvatar);
        holder.personFullName.setText(person.toString());
    }

    @Override
    public int getItemCount() {
        return mPeople.size();
    }

    public void updatePeople(List<User> pPeople){
        mPeople = pPeople;
        notifyDataSetChanged();
    }

    public class PersonVH extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_person_avatar)
        ImageView   personAvatar;
        @BindView(R.id.tv_person_full_name)
        TextView    personFullName;
        @BindView(R.id.iv_person_info_icon)
        ImageView   personInfoIcon;

        User person;

        public PersonVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            personInfoIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnPersonClickListener != null){
                        mOnPersonClickListener.onPersonClick(person);
                    }
                }
            });
        }

    }

    public interface OnPersonClickListener{
        void onPersonClick(User user);
    }
}
