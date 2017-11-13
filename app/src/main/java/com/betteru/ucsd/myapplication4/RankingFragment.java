package com.betteru.ucsd.myapplication4;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class RankingFragment extends Fragment {
    ViewPager viewPager;
    PickerAdapter adapter;


    private boolean exercisingRanking = true;
    private boolean cookingRanking = true;
    private boolean houseworkRanking = true;
    private boolean shoppingRanking = true;
    private boolean sleepingRanking = true;
    private boolean workingRanking = true;
    private boolean studyingRanking = true;
    private boolean hangingoutRanking = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ranking, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        adapter = new PickerAdapter(getChildFragmentManager());
        viewPager = getView().findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        ((AppCompatActivity)getActivity()).setSupportActionBar((Toolbar) getView().findViewById(R.id.toolbar));
        TabLayout tabLayout = getView().findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        for(int i=0;i<adapter.getCount();i++) //noinspection ConstantConditions
            tabLayout.getTabAt(i).setText(adapter.getTitle(i));

        FloatingActionButton settingButton = (FloatingActionButton) getView().findViewById(R.id.button_setting);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = RankingSettingsDialogFragment.newInstance(
                        exercisingRanking,
                        cookingRanking,
                        houseworkRanking,
                        shoppingRanking,
                        sleepingRanking,
                        workingRanking,
                        studyingRanking,
                        hangingoutRanking
                );
                dialog.show(getFragmentManager(), "RankingSettingsDialogFragment");
            }
        });

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.hasExtra("exercisingRanking")) {
                    exercisingRanking = intent.getBooleanExtra("exercisingRanking", false);
                }
                if(intent.hasExtra("cookingRanking")) {
                    cookingRanking = intent.getBooleanExtra("cookingRanking", false);
                }
                if(intent.hasExtra("houseworkRanking")) {
                    houseworkRanking = intent.getBooleanExtra("houseworkRanking", false);
                }
                if(intent.hasExtra("shoppingRanking")) {
                    shoppingRanking = intent.getBooleanExtra("shoppingRanking", false);
                }
                if(intent.hasExtra("sleepingRanking")) {
                    sleepingRanking = intent.getBooleanExtra("sleepingRanking", false);
                }
                if(intent.hasExtra("workingRanking")) {
                    workingRanking = intent.getBooleanExtra("workingRanking", false);
                }
                if(intent.hasExtra("studyingRanking")) {
                    studyingRanking = intent.getBooleanExtra("studyingRanking", false);
                }
                if(intent.hasExtra("hangingoutRanking")) {
                    hangingoutRanking = intent.getBooleanExtra("hangingoutRanking", false);
                }
                /*Toast.makeText(
                        getActivity(),
                        "exercisingRanking: "+Boolean.toString(exercisingRanking)+"\n"
                                + "cookingRanking: "+Boolean.toString(cookingRanking)+"\n"
                                + "houseworkRanking: "+Boolean.toString(houseworkRanking)+"\n"
                                + "shoppingRanking: "+Boolean.toString(shoppingRanking)+"\n"
                                + "sleepingRanking: "+Boolean.toString(sleepingRanking)+"\n"
                                + "workingRanking: "+Boolean.toString(workingRanking)+"\n"
                                + "studyingRanking: "+Boolean.toString(studyingRanking)+"\n"
                                + "hangingoutRanking: "+Boolean.toString(hangingoutRanking),
                        Toast.LENGTH_LONG
                ).show();*/
            }
        };
        IntentFilter filter = new IntentFilter("DialogChangeSaved");
        getActivity().registerReceiver(broadcastReceiver,filter);
    }

    private class PickerAdapter extends FragmentPagerAdapter {
        private static final int NUM_PAGES = 2;
        Fragment rankingPeerFragment;
        Fragment rankingAllFragment;

        PickerAdapter(FragmentManager fm) {
            super(fm);
            rankingPeerFragment = RankingPeerFragment.newInstance();
            rankingAllFragment = RankingAllFragment.newInstance();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return rankingPeerFragment;
                case 1:
                    return rankingAllFragment;
                default:
                    return rankingPeerFragment;
            }
        }

        int getTitle(int position) {
            switch(position) {
                case 0:
                    return R.string.ranking_peer;
                case 1:
                    return R.string.ranking_all;
                default:
                    return R.string.ranking_peer;
            }
        }
    }

    public static RankingFragment newInstance() {
        RankingFragment fragment = new RankingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

}