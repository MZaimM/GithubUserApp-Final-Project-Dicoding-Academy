package com.example.githubuserapp.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.githubuserapp.BuildConfig;
import com.example.githubuserapp.adapter.ListSearchAdapter;
import com.example.githubuserapp.model.User;
import com.example.githubuserapp.view.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<User>> listUserFromApi = new MutableLiveData<>();
    private User user;
    private final ArrayList<User> list = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    private MainActivity mainActivity;
    private static final String GITHUB_API_KEY = BuildConfig.ApiKey;

    public CharSequence setListUserFromApi(String username){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.github.com/search/users?q="+username;
        client.addHeader("Authorization","token "+GITHUB_API_KEY);
        client.addHeader("User-Agent","request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    for (int j=0; j<jsonArray.length();j++){
                        JSONObject responseObject = jsonArray.getJSONObject(j);
                        String userName = responseObject.getString("login");
                        String avatar = responseObject.getString("avatar_url");
                        String htmlUrl = responseObject.getString("html_url");
                        user = new User();
                        user.setUsername(userName);
                        user.setPhoto(avatar);
                        user.setHtmlUrl(htmlUrl);
                        list.add(user);
                    }
                    listUserFromApi.postValue(list);
                }catch (Exception e){
                    mainActivity.showLoading(false);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", Objects.requireNonNull(error.getMessage()));
            }
        });

        return username;
    }

    public LiveData<ArrayList<User>> getListUserFromApi(){
        return listUserFromApi;
    }
}
