package com.example.githubuserapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.githubuserapp.view.DetailProfileActivity;
import com.example.githubuserapp.R;
import com.example.githubuserapp.model.User;

import java.util.ArrayList;
import java.util.Collection;

public class ListSearchAdapter extends RecyclerView.Adapter<ListSearchAdapter.SearchViewHolder> implements Filterable {
    private ArrayList<User> listUser;
    private ArrayList<User> listUserFull;
    private OnItemClickCallBack onItemClickCallBack;


    public ListSearchAdapter(ArrayList<User> list) {
        this.listUser = list;
        listUserFull = new ArrayList<>(listUser);
        notifyDataSetChanged();
    }

    public void setDataUser(ArrayList<User> list) {
        this.listUser = list;
        listUserFull = new ArrayList<>(listUser);
        notifyDataSetChanged();
    }

    public void setItemClickCallback(OnItemClickCallBack onItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row, viewGroup, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchViewHolder holder, int position) {
        User user = listUser.get(position);
        Glide.with(holder.itemView.getContext())
                .load(user.getPhoto())
                .into(holder.imgPhoto);
        holder.tvUsername.setText(user.getUsername());
        holder.tvHtmlUrl.setText(user.getHtmlUrl());
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallBack.onItemClicked(listUser.get(holder.getAdapterPosition()));
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    @Override
    public Filter getFilter() {
        return listFilter;
    }

    private final Filter listFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<User> filterList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filterList.addAll(listUserFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (User item : listUserFull) {
                    if (item.getUsername().toLowerCase().contains(filterPattern)) {
                        filterList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listUser.clear();
            listUser.addAll((Collection<? extends User>) results.values);
            notifyDataSetChanged();
        }
    };

    public class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView tvUsername;
        final TextView tvHtmlUrl;
        final ImageView imgPhoto;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.user_name);
            tvHtmlUrl = itemView.findViewById(R.id.tvhtml_url);
            imgPhoto = itemView.findViewById(R.id.img_photo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            User user = listUser.get(getAdapterPosition());
            Intent intent = new Intent(v.getContext(), DetailProfileActivity.class);
            intent.putExtra(DetailProfileActivity.DATA_USER, user);
            v.getContext().startActivity(intent);
        }
    }

    public interface OnItemClickCallBack {
        void onItemClicked(User data);
    }
}
