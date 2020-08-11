package com.example.githubuserapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.githubuserapp.R;
import com.example.githubuserapp.adapter.ListSearchAdapter;
import com.example.githubuserapp.model.User;
import com.example.githubuserapp.preference.MyPreference;
import com.example.githubuserapp.viewmodel.MainViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListSearchAdapter listSearchAdapter;
    private ProgressBar progressBar;
    private ImageView imgErrorDataNotFound;
    private TextView txtErrorDataNotFound;
    private final ArrayList<User> list = new ArrayList<>();
    RecyclerView rvUserGitHub;
    private MainViewModel mainViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        imgErrorDataNotFound = findViewById(R.id.imgErrorDataNotFound);
        txtErrorDataNotFound = findViewById(R.id.textDataNotFound);

        rvUserGitHub = findViewById(R.id.rv_UserGithub);
        rvUserGitHub.setHasFixedSize(true);
        rvUserGitHub.setLayoutManager(new LinearLayoutManager(this));

        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        showLoading(true);
        showDataNotFound(false);
        mainViewModel.setListUserFromApi("users");
        listSearchAdapter = new ListSearchAdapter(list);
        rvUserGitHub.setAdapter(listSearchAdapter);

        mainViewModel.getListUserFromApi().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                if (users != null){
                    listSearchAdapter.setDataUser(users);
                    showLoading(false);
                    showDataNotFound(true);
                }
            }
        });
        /*listSearchAdapter.setItemClickCallback(new ListSearchAdapter.OnItemClickCallBack() {
            @Override
            public void onItemClicked(User data) {
                Intent intent = new Intent(MainActivity.this, DetailProfileActivity.class);
                intent.putExtra(DetailProfileActivity.DATA_USER,user);
                startActivity(intent);
            }
        });*/

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.setting_bar){
            //Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            Intent intent = new Intent(MainActivity.this, MyPreference.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.enter_username));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                showLoading(true);
                showDataNotFound(false);
                listSearchAdapter.getFilter().filter(mainViewModel.setListUserFromApi(newText));
                return false;
            }
        });


        return true;
    }
    public void showLoading(Boolean visibility){
        if (visibility){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    public void showDataNotFound(Boolean visibility){
        if (visibility){
            imgErrorDataNotFound.setVisibility(View.VISIBLE);
            txtErrorDataNotFound.setVisibility(View.VISIBLE);
        }else {
            imgErrorDataNotFound.setVisibility(View.INVISIBLE);
            txtErrorDataNotFound.setVisibility(View.INVISIBLE);
        }
    }


}