package com.betteru.ucsd.myapplication4;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
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

import java.util.ArrayList;
import java.util.Collection;

import com.betteru.ucsd.myapplication4.BetterUApplication;


public class FriendsFragment extends Fragment {

    View view;
    FirebaseFirestore db;
    ArrayList<String> friendsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_friends, container, false);
        db = FirebaseFirestore.getInstance();
        friendsList = new ArrayList<String>();
        loadData();
        loadListView();
        return view;
    }

    private void loadData() {
        JSONArray friendsData = ((BetterUApplication) getActivity().getApplication()).getFriends();
        for (int i = 0; i < friendsData.length(); ++i) {
            String friendName = null;
            try {
                friendName = friendsData.getJSONObject(i).getString("name");
                this.friendsList.add(friendName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadListView() {
        ListView listView = (ListView) view.findViewById(R.id.listView_friendsList);
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