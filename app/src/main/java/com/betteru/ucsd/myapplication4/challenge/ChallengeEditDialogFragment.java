package com.betteru.ucsd.myapplication4.challenge;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.betteru.ucsd.myapplication4.BetterUApplication;
import com.betteru.ucsd.myapplication4.R;
import com.betteru.ucsd.myapplication4.UserModel;

import java.util.ArrayList;

/**
 * Created by Yuting on 12/8/2017.
 */
public class ChallengeEditDialogFragment extends DialogFragment
        implements DialogInterface.OnDismissListener{

    ChallengeModel data;
    View view;
    ArrayAdapter<String> adapter;
    Button button;
    ListView listView;
    Boolean flag;
    UserModel user;
    ArrayList<UserModel> friends;
    ArrayList<String> firstNameList = new ArrayList<>();
    ArrayList<String> idList = new ArrayList<>();

    public static ChallengeEditDialogFragment newInstance(ChallengeModel data, Boolean particpants) {
        ChallengeEditDialogFragment f = new ChallengeEditDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("data", data);
        args.putBoolean("participants", particpants);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //get arguments
        Bundle args = getArguments();
        data = (ChallengeModel)args.getSerializable("data");
        flag = (Boolean)args.get("participants") ;
        //get view
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_challenge_activity, null);
        builder.setView(view);

        user = ((BetterUApplication) getActivity().getApplication()).getCurrentFBUser();
        friends = ((BetterUApplication) getActivity().getApplication()).getFriendList();
        //find component by id
        listView = (ListView) view.findViewById(R.id.listView_challengeActivity);
        button = (Button) view.findViewById(R.id.button_challengeActivity);
        if(flag) {
            builder.setTitle("Edit Challenge Participants");
            loadChosenFriends();
        }
        else {
            builder.setTitle("Edit Challenge Activities");
            loadChosenActivity();
        }
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(flag)
                    onClickFriendsButton();
                else
                    onClickActivityButton();
                Intent i = new Intent().putExtra("data", data);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                dismiss();
                //EditChallengeNameDialogFragment.this.loadChallengeName();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                ChallengeEditDialogFragment.this.getDialog().cancel();
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }
    private void loadChosenActivity(){
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
    }
    private void onClickActivityButton(){
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
    }

    private void loadChosenFriends(){
        //String[] list = getResources().getStringArray(R.array.challenge_activity_array);
        Log.d("friends", Integer.toString(friends.size()));

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
    }
    private void onClickFriendsButton(){
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        ArrayList<String> selectedItems = new ArrayList<String>();
        data.participants.clear();
        data.participants_name.clear();
        for (int i = 0; i < checked.size(); i++) {
            // Item position in adapter
            int position = checked.keyAt(i);
            // Add sport if it is checked i.e.) == TRUE!
            if (checked.valueAt(i)) {
                data.participants.add(idList.get(position));
                data.participants_name.add(firstNameList.get(position));
            }
        }
    }
}

