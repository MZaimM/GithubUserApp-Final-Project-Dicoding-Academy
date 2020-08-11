package com.example.githubuserapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.githubuserapp.R;
import com.example.githubuserapp.model.User;
import com.example.githubuserapp.view.DetailProfileActivity;

import java.util.ArrayList;

public class AdapterFavorite extends RecyclerView.Adapter<AdapterFavorite.FavoriteViewHolder> {
    private ArrayList<User> listUsers;

    public AdapterFavorite(ArrayList<User> list) {
        this.listUsers = list;
        notifyDataSetChanged();
    }

    public void setDataUser(ArrayList<User> list) {
        this.listUsers = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterFavorite.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row, viewGroup, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFavorite.FavoriteViewHolder holder, int position) {
        User user = listUsers.get(position);
        Glide.with(holder.itemView.getContext())
                .load(user.getPhoto())
                .into(holder.imgPhoto);
        holder.tv_username.setText(user.getUsername());
        holder.tv_htmlUrl.setText(user.getHtmlUrl());
    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgPhoto;
        TextView tv_username, tv_htmlUrl;
        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_photo);
            tv_username = itemView.findViewById(R.id.user_name);
            tv_htmlUrl = itemView.findViewById(R.id.tvhtml_url);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            User user = listUsers.get(getAdapterPosition());
            Intent intent = new Intent(v.getContext(), DetailProfileActivity.class);
            intent.putExtra(DetailProfileActivity.DATA_USER, user);
            v.getContext().startActivity(intent);
        }
    }
}
