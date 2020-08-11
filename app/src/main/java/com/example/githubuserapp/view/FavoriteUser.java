package com.example.githubuserapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.githubuserapp.R;
import com.example.githubuserapp.adapter.AdapterFavorite;
import com.example.githubuserapp.database.UserHelper;
import com.example.githubuserapp.model.User;

import java.util.ArrayList;

public class FavoriteUser extends AppCompatActivity {
    RecyclerView rvUserfavorite;
    private ArrayList<User> list = new ArrayList<>();
    private AdapterFavorite adapterFavorite;
    private UserHelper userHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_user);

        userHelper = new UserHelper(getApplicationContext());
        userHelper.open();
        list = userHelper.getDataUser();

        rvUserfavorite = findViewById(R.id.rv_UserFavorite);
        rvUserfavorite.setLayoutManager(new LinearLayoutManager(this));
        rvUserfavorite.setHasFixedSize(true);
        adapterFavorite = new AdapterFavorite(list);
        rvUserfavorite.setAdapter(adapterFavorite);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Favorite User");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        list = userHelper.getDataUser();
        adapterFavorite.setDataUser(list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userHelper.close();
    }
}