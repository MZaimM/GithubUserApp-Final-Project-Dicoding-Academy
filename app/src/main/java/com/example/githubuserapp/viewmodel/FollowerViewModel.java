package com.example.githubuserapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.githubuserapp.BuildConfig;
import com.example.githubuserapp.model.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class FollowerViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<User>> listFollowerFromApi = new MutableLiveData<>();
    private User user;
    private final ArrayList<User> list = new ArrayList<>();
    private static final String GITHUB_API_KEY = BuildConfig.ApiKey;

    public LiveData<ArrayList<User>> getListFollowerFromApi(){
        return listFollowerFromApi;
    }

    public void setFollowerFromApi(String username){

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.github.com/users/"+username+"/followers";
        client.addHeader("Authorization","token "+GITHUB_API_KEY);
        client.addHeader("User-Agent","request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONArray responseArray = new JSONArray(result);

                    for (int i = 0; i<responseArray.length();i++){
                        JSONObject responseObject = responseArray.getJSONObject(i);
                        String username = responseObject.getString("login");
                        String htmlUrl = responseObject.getString("html_url");
                        String avatar = responseObject.getString("avatar_url");
                        user = new User();
                        user.setPhoto(avatar);
                        user.setUsername(username);
                        user.setHtmlUrl(htmlUrl);
                        list.add(user);
                    }
                    listFollowerFromApi.postValue(list);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", Objects.requireNonNull(error.getMessage()));

            }
        });
    }

}
