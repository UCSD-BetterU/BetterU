package com.betteru.ucsd.myapplication4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class GoalFragment extends Fragment {
    private View view;
    public DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_goal, container, false);
        }
        else
        {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
        }
        ArrayList<String> list = new ArrayList<String>();
        mDatabase = FirebaseDatabase.getInstance().getReference("ranking/user0001/10/31/talking");
        // Read from the database
        mDatabase.addValueEventListener(new ValueEventListener() {
            String TAG = "Goal Fragment";
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.child("talking").getValue(String.class);
                System.out.println(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        // System.out.println(mDatabase.toString());
        list.add("goal 1");
        list.add("goal 2");

        list.add(mDatabase.getKey().toString());


        //instantiate custom adapter
        GoalAdapter adapter = new GoalAdapter(list, getActivity());

        //handle listview and assign adapter
        ListView lView = view.findViewById(R.id.goal_list_view);
        lView.setAdapter(adapter);

        return view;
    }

/*     public static GoalFragment newInstance() {
        GoalFragment fragment = new GoalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    } */
}