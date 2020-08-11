package com.example.githubuserapp.preference;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.githubuserapp.R;

public class MyPreference extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_preference);

        getSupportFragmentManager().beginTransaction().add(R.id.setting_holder, new Preference()).commit();

    }
}