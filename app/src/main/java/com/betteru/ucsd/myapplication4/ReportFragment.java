package com.betteru.ucsd.myapplication4;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ReportFragment extends Fragment {
    ViewPager viewPager;
    PickerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report, container, false);
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
    }

    private class PickerAdapter extends FragmentPagerAdapter {
        private static final int NUM_PAGES = 2;
        Fragment reportFragment;
        Fragment rankingFragment;

        PickerAdapter(FragmentManager fm) {
            super(fm);
            reportFragment = SummaryFragment.newInstance();
            rankingFragment = RankingFragment.newInstance();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return reportFragment;
                case 1:
                    return rankingFragment;
                default:
                    return reportFragment;
            }
        }

        int getTitle(int position) {
            switch(position) {
                case 0:
                    return R.string.report;
                case 1:
                    return R.string.ranking;
                default:
                    return R.string.report;
            }
        }
    }

    public static ReportFragment newInstance() {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

}