package com.example.githubuserapp.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.githubuserapp.BuildConfig;
import com.example.githubuserapp.R;
import com.example.githubuserapp.adapter.PagerAdapter;
import com.example.githubuserapp.database.DatabaseContract;
import com.example.githubuserapp.database.DatabaseHelper;
import com.example.githubuserapp.database.UserHelper;
import com.example.githubuserapp.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

import static com.example.githubuserapp.database.DatabaseContract.UserColumns.TABLE_NAME;
import static com.example.githubuserapp.database.DatabaseContract.UserColumns.USERNAME;

public class DetailProfileActivity extends AppCompatActivity {
    private TextView tvName, tvFollower, tvFollowing,tvRepository,tvCompany,tvLocation,tvHtmlUrl;
    private ImageView imgPhoto;
    private FloatingActionButton floatingActionButton;
    public static final String DATA_USER ="data user";
    private static final String GITHUB_API_KEY = BuildConfig.ApiKey;
    private ArrayList<User> userGithub;
    private User users= new User();
    private UserHelper userHelper = new UserHelper(this);

    public static final int RESULT_ADD = 101;
    public static final int RESULT_DELETE = 301;
    private Boolean isFavoriteFalse = false;
    private Boolean isFavoriteTrue = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profile);

        PagerAdapter pagerAdapter = new PagerAdapter(this,getSupportFragmentManager());
        floatingActionButton = findViewById(R.id.floatingActionButton);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        tvName = findViewById(R.id.tvName);
        imgPhoto = findViewById(R.id.img_photo_detail);

        User user = getIntent().getParcelableExtra(DATA_USER);
        String avatarDetail = Objects.requireNonNull(user).getPhoto();


        Glide.with(getApplicationContext())
                .load(avatarDetail)
                .into(imgPhoto);

        if (getSupportActionBar() !=null){
            getSupportActionBar().setTitle(pagerAdapter.setUsername(user.getUsername()));
        }


        setDataUserDetail(pagerAdapter.setUsername(user.getUsername()));

        userHelper = UserHelper.getInstance(getApplicationContext());
        userHelper.open();
        setOnClickFavoriteUser();

    }

    private void setDataUserDetail(final String username){
        tvFollower = findViewById(R.id.tvfollowerDetail);
        tvFollowing = findViewById(R.id.tvfollowingDetail);
        tvRepository = findViewById(R.id.tvrepositoryDetail);
        tvCompany = findViewById(R.id.tvCompany);
        tvLocation = findViewById(R.id.tvLocation);
        tvHtmlUrl = findViewById(R.id.tvHtmlUrl);

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.github.com/users/"+username;
        client.addHeader("Authorization","token "+ GITHUB_API_KEY);
        client.addHeader("User-Agent","request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    userGithub = new ArrayList<>();
                    String result = new String(responseBody);
                    JSONObject jsonObject = new JSONObject(result);
                    String username = jsonObject.getString("login");
                    String avatar = jsonObject.getString("avatar_url");
                    String follower = jsonObject.getString("followers");
                    String following = jsonObject.getString("following");
                    String repo = jsonObject.getString("public_repos");
                    String company = jsonObject.getString("company");
                    String location = jsonObject.getString("location");
                    String name = jsonObject.getString("name");
                    String htmlIUrl = jsonObject.getString("html_url");

                    users.setUsername(username);
                    users.setName(name);
                    users.setPhoto(avatar);
                    users.setFollower(follower);
                    users.setFollowing(following);
                    users.setRepository(repo);
                    users.setLocation(location);
                    users.setCompany(company);
                    users.setHtmlUrl(htmlIUrl);
                    userGithub.add(users);

                    tvName.setText(users.getName());
                    tvFollower.setText(users.getFollower());
                    tvFollowing.setText(users.getFollowing());
                    tvRepository.setText(users.getRepository());
                    tvCompany.setText(users.getLocation());
                    tvLocation.setText(users.getCompany());
                    tvHtmlUrl.setText(users.getHtmlUrl());
                    Linkify.addLinks(tvHtmlUrl,Linkify.WEB_URLS);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void setStatusFavorite(Boolean statusFavorite){
        if (statusFavorite){
            floatingActionButton.setImageResource(R.drawable.baseline_favorite_white_48dp);
        }else {
            floatingActionButton.setImageResource(R.drawable.baseline_favorite_border_white_48dp);
        }



    }

    private void setOnClickFavoriteUser(){
        final User user = getIntent().getParcelableExtra(DATA_USER);
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Cursor cursor = database.query(TABLE_NAME,null,USERNAME + "=?", new String[]{user.getUsername()},null,null,null,"1");
        boolean exist = (cursor.getCount() > 0 );


        if (exist){
            setStatusFavorite(isFavoriteTrue);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isFavoriteTrue){
                        isFavoriteTrue =!isFavoriteTrue;
                        long result = userHelper.insert(users);
                        if (result > 0){
                            users.setId((int) result);
                            setResult(RESULT_ADD);
                            Toast.makeText(DetailProfileActivity.this, "added favorite", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(DetailProfileActivity.this, "gagal added", Toast.LENGTH_SHORT).show();
                        }
                        setStatusFavorite(isFavoriteTrue);
                    }else {
                        isFavoriteTrue = !isFavoriteTrue;
                        long result =  userHelper.deleteUsername(String.valueOf(user.getUsername()));
                        if (result > 0){
                            setResult(RESULT_DELETE);
                            Toast.makeText(DetailProfileActivity.this, "deleted favorite", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(DetailProfileActivity.this, "Gagal Delete", Toast.LENGTH_SHORT).show();
                        }
                        setStatusFavorite(isFavoriteTrue);
                    }
                }
            });

        }else {
            setStatusFavorite(isFavoriteFalse);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isFavoriteFalse){
                        isFavoriteFalse =!isFavoriteFalse;
                        long result = userHelper.insert(users);
                        if (result > 0){
                            users.setId((int) result);
                            setResult(RESULT_ADD);
                            Toast.makeText(DetailProfileActivity.this, "added favorite", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(DetailProfileActivity.this, "gagal added", Toast.LENGTH_SHORT).show();
                        }
                        setStatusFavorite(isFavoriteFalse);
                    }else {
                        isFavoriteFalse = !isFavoriteFalse;
                        long result =  userHelper.delete(String.valueOf(users.getId()));
                        if (result > 0){
                            setResult(RESULT_DELETE);
                            Toast.makeText(DetailProfileActivity.this, "deleted favorite", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(DetailProfileActivity.this, "Gagal Delete", Toast.LENGTH_SHORT).show();
                        }
                        setStatusFavorite(isFavoriteFalse);
                    }
                }
            });
        }




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.favorite){
            Intent favIntent = new Intent(DetailProfileActivity.this, FavoriteUser.class);
            startActivity(favIntent);
        }
        return super.onOptionsItemSelected(item);
    }

}