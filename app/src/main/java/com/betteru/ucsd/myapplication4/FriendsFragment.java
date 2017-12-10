package com.betteru.ucsd.myapplication4;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

import com.betteru.ucsd.myapplication4.BetterUApplication;


public class FriendsFragment extends Fragment {

    View view;
    FirebaseFirestore db;
    ArrayList<UserModel> friendsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_friends, container, false);
        db = FirebaseFirestore.getInstance();
        friendsList = new ArrayList<>();
        loadData();
        loadListView();
        return view;
    }

    private void loadData() {
        friendsList = ((BetterUApplication) getActivity().getApplication()).getFriendList();
        if (friendsList == null) {
            Log.d(BetterUApplication.TAG+"friendfragment", "friend list is null");
            return;
        }
        for (int i = 0; i < friendsList.size(); ++i){
            Log.d(BetterUApplication.TAG+"friendfragment", friendsList.get(i).getUserId() + " " + friendsList.get(i).getName());
        }
    }

    private void loadListView() {
        ListView listView = view.findViewById(R.id.listView_friendsList);
        FriendsAdapter adapter = new FriendsAdapter(this.getActivity(), friendsList);
        listView.setAdapter(adapter);
    }

    public static FriendsFragment newInstance() {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}