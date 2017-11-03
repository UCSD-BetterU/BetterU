package com.betteru.ucsd.myapplication4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class GoalFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goal, container, false);
    }

    public static GoalFragment newInstance() {
        GoalFragment fragment = new GoalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}