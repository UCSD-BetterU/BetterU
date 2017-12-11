package com.betteru.ucsd.myapplication4;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Yuting on 12/10/2017.
 */

public class ChallengePagerFragment extends Fragment {
    ViewPager viewPager;
    ChallengePagerFragment.PickerAdapter adapter;
    UserModel user;
    View view;
    Calendar calendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        user = ((BetterUApplication) getActivity().getApplication()).getCurrentFBUser();
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        adapter = new ChallengePagerFragment.PickerAdapter(getChildFragmentManager());
        viewPager = getView().findViewById(R.id.pager_challenge);
        viewPager.setAdapter(adapter);

        ((AppCompatActivity)getActivity()).setSupportActionBar((Toolbar) getView().findViewById(R.id.toolbar));
        TabLayout tabLayout = getView().findViewById(R.id.tabs_challenge);
        tabLayout.setupWithViewPager(viewPager);
        for(int i=0;i<adapter.getCount();i++) //noinspection ConstantConditions
            tabLayout.getTabAt(i).setText(adapter.getTitle(i));
        loadNewChallengeButton();
    }

    private class PickerAdapter extends FragmentPagerAdapter {
        private static final int NUM_PAGES = 2;
        Fragment ownerFragment;
        Fragment participantFragment;

        PickerAdapter(FragmentManager fm) {
            super(fm);
            ownerFragment = ChallengeFragment.newInstance();
            participantFragment = ChallengeParticipantsFragment.newInstance();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return ownerFragment;
                case 1:
                    return participantFragment;
                default:
                    return ownerFragment;
            }
        }

        int getTitle(int position) {
            switch(position) {
                case 0:
                    return R.string.ChallengeOwner;
                case 1:
                    return R.string.ChallengeParticipants;
                default:
                    return R.string.ChallengeOwner;
            }
        }
    }

    public static ChallengePagerFragment newInstance() {
        ChallengePagerFragment fragment = new ChallengePagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void loadNewChallengeButton(){
        FloatingActionButton button = view.findViewById(R.id.button_challenge_add);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newChallenge();
            }
        });
    }

    private void newChallenge(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 2);
        String timeStamp = ChallengeModel.sdf.format(c.getTime());
        Log.d("challenge add new challenge", timeStamp);
        ChallengeModel newChallenge = new ChallengeModel(user.getUserId(),
                "Input Challenge Name",
                timeStamp,
                new ArrayList<String>(),
                new ArrayList<String>(),
                new ArrayList<String>());
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        ChallengeActivityFragment fragment = new ChallengeActivityFragment();
        Bundle args = new Bundle();
        args.putSerializable("data",newChallenge);
        args.putBoolean("edit", true);
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.fragmentContent, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
