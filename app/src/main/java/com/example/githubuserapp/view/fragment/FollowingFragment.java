package com.example.githubuserapp.view.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.githubuserapp.R;
import com.example.githubuserapp.adapter.ListSearchAdapter;
import com.example.githubuserapp.model.User;
import com.example.githubuserapp.viewmodel.FollowingViewModel;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FollowingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowingFragment extends Fragment {
    private final ArrayList<User> list = new ArrayList<>();
    private RecyclerView rvFollowing;
    private ProgressBar progressBar;
    private ListSearchAdapter listSearchAdapter;
    private User user;
    private ImageView imgDataEmpty;
    private TextView txtDataEmpty;
    private FollowingViewModel followingViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USERNAME = "username";

    // TODO: Rename and change types of parameters
    private String username;
    private String mParam2;

    public FollowingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param username Parameter 1.
     *
     * @return A new instance of fragment FollowingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public FollowingFragment newInstance(String username) {
        FollowingFragment fragment = new FollowingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBarFollowing);
        imgDataEmpty = view.findViewById(R.id.imgErrorDataNotFound);
        txtDataEmpty = view.findViewById(R.id.textDataNotFound);
        rvFollowing = view.findViewById(R.id.rvFollowing);
        rvFollowing.setHasFixedSize(true);
        rvFollowing.setLayoutManager(new LinearLayoutManager(getActivity()));

        followingViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()), new ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel.class);
        progressBar.setVisibility(View.VISIBLE);
        imgDataEmpty.setVisibility(View.INVISIBLE);
        txtDataEmpty.setVisibility(View.INVISIBLE);
        followingViewModel.setFollowingFromApi(Objects.requireNonNull(getArguments()).getString(ARG_USERNAME));
        listSearchAdapter = new ListSearchAdapter(list);
        rvFollowing.setAdapter(listSearchAdapter);

        followingViewModel.getListFollowerFromApi().observe(getActivity(), new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                if (users != null){
                    listSearchAdapter.setDataUser(users);
                    if (user != null){
                        progressBar.setVisibility(View.INVISIBLE);
                    }else{
                        imgDataEmpty.setVisibility(View.VISIBLE);
                        txtDataEmpty.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });



    }


}