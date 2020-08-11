package com.example.githubuserapp.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.githubuserapp.view.Fragment.FollowerFragment;
import com.example.githubuserapp.view.Fragment.FollowingFragment;
import com.example.githubuserapp.R;

import java.util.Objects;

public class PagerAdapter extends FragmentPagerAdapter {
    private  final Context context;

    public String setUsername(String username) {
        this.username = username;
        return username;
    }

    public String getUsername() {
        return username;
    }

    String username;
    public PagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;

    }

    @StringRes
    private final int[] TAB_TITLES = new int[]{
      R.string.follower, R.string.following
    };

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new FollowerFragment().newInstance(username);
                break;
            case 1:
                fragment = new FollowingFragment().newInstance(username);
                break;
        }
        return Objects.requireNonNull(fragment);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
