package com.betteru.ucsd.myapplication4;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Yuting on 11/12/2017.
 */

public class ChallengeActivityListFragment extends FragmentActivity {
    ChallengeModel data;
    View view;
    ArrayAdapter<String> adapter;
    Button button;
    ListView listView;

    /** Called when the activity is first created. */
    @Override
    public void onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        Bundle args = getArguments();
        data = (ChallengeModel) args.getSerializable("data");

        view = inflater.inflate(R.layout.fragment_challenge_activity, container, false);
        findViewsById();

        String[] list = getResources().getStringArray(R.array.challenge_activity_array);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SparseBooleanArray checked = listView.getCheckedItemPositions();
                ArrayList<String> selectedItems = new ArrayList<String>();
                for (int i = 0; i < checked.size(); i++) {
                    // Item position in adapter
                    int position = checked.keyAt(i);
                    // Add sport if it is checked i.e.) == TRUE!
                    if (checked.valueAt(i))
                        selectedItems.add(adapter.getItem(position));
                }
                data.activities.clear();
                for(int i = 0; i < selectedItems.size(); i++){
                    data.activities.add(selectedItems.get(i));
                }
                backToDetailPage();
            }
        });
    }

    private void findViewsById() {
        listView = (ListView) view.findViewById(R.id.listView_challengeActivity);
        button = (Button) view.findViewById(R.id.button_challengeActivity);

    }

    public void backToDetailPage(){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        ChallengeActivityFragment fragment = new ChallengeActivityFragment();

        Bundle args = new Bundle();
        args.putSerializable("data",data);
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.fragmentContent, fragment);
        fragmentTransaction.commit();

    }
}
