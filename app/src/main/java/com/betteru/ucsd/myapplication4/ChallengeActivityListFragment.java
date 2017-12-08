package com.betteru.ucsd.myapplication4;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Yuting on 11/12/2017.
 */

public class ChallengeActivityListFragment extends Fragment {
    ChallengeModel data;
    View view;
    ArrayAdapter<String> adapter;
    Button button;
    ListView listView;
    Boolean flag;
    UserModel user;
    ArrayList<UserModel> friends;

    /** Called when the activity is first created. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        data = (ChallengeModel) args.getSerializable("data");
        flag = (Boolean)args.get("participants") ;
        view = inflater.inflate(R.layout.fragment_challenge_activity, container, false);
        user = ((BetterUApplication) getActivity().getApplication()).getCurrentFBUser();
        friends = ((BetterUApplication) getActivity().getApplication()).getFriendList();
        //find component by id
        listView = (ListView) view.findViewById(R.id.listView_challengeActivity);
        button = (Button) view.findViewById(R.id.button_challengeActivity);
        if(flag)
            chooseParticpants();
        else
            chooseActivity();
        return view;
    }
    private void chooseActivity(){
        //String[] list = getResources().getStringArray(R.array.challenge_activity_array);
        String[] list = ChallengeActivityEnum.getAllName();
        adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_multiple_choice, list);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        for(int i = 0; i < list.length; i++)
        {
            if(data.activities.contains(list[i])) {
                listView.setItemChecked(i, true);
                Log.d("activity found", list[i]);
            }
            else{
                Log.d("activity not found", list[i]);
            }
        }
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
                data.activitiesIcon.clear();
                for(int i = 0; i < selectedItems.size(); i++){
                    String act = selectedItems.get(i);
                    ChallengeActivityEnum obj = ChallengeActivityEnum.get(act);
                    data.activities.add(obj.getName());
                    data.activitiesIcon.add(obj.getIcon());
                }
                backToDetailPage();
            }
        });
    }
    private void chooseParticpants(){
        //String[] list = getResources().getStringArray(R.array.challenge_activity_array);
        Log.d("friends", Integer.toString(friends.size()));
        final ArrayList<String> firstNameList = new ArrayList<>();
        final ArrayList<String> idList = new ArrayList<>();
        for(int i = 0; i < friends.size(); i++)
        {
            firstNameList.add(friends.get(i).getFirstName());
            idList.add(friends.get(i).getUserId());
        }
        Log.d("friends id list", idList.toString());
        Log.d("friends first name", firstNameList.toString());
        adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_multiple_choice, firstNameList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        for(int i = 0; i < idList.size(); i++)
        {
            if(data.participants.contains(idList.get(i))) {
                listView.setItemChecked(i, true);
            }
            else{
                Log.d("participants not found", idList.get(i));
            }
        }
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SparseBooleanArray checked = listView.getCheckedItemPositions();
                ArrayList<String> selectedItems = new ArrayList<String>();
                data.participants.clear();
                data.participants_name.clear();
                for (int i = 0; i < checked.size(); i++) {
                    // Item position in adapter
                    int position = checked.keyAt(i);
                    // Add sport if it is checked i.e.) == TRUE!
                    if (checked.valueAt(i)) {
                        data.participants.add(idList.get(i));
                        data.participants_name.add(firstNameList.get(i));
                    }
                }
                backToDetailPage();
            }
        });
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

    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.toString(e.getEnumConstants()).replaceAll("^.|.$", "").split(", ");
    }
}
