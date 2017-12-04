package com.betteru.ucsd.myapplication4;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Created by Yuting on 11/30/2017.
 */

public class ChallengeActivityResultFragment extends Fragment {
    static ChallengeModel data;
    View view;
    public final static int EDITDIALOG_FRAGMENT = 1;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle args = getArguments();
        data = (ChallengeModel) args.getSerializable("data");
        Log.d("DATA", Integer.toString(data.activitiesIcon.size()));
        view = inflater.inflate(R.layout.fragment_challenge_result, container, false);
        loadChallengeDate();
        loadChallengeName();
        loadChallengeWinner();
        loadChallengeDetailButton();
        return view;
    }
    public void noWinner(Boolean flag)
    {
        TextView noWinner = (TextView) view.findViewById(R.id.textView_noWinner);
        if(flag)
            noWinner.setVisibility(View.VISIBLE);
        else
            noWinner.setVisibility(View.GONE);
    }
    public void loadChallengeName() {
        TextView name = (TextView) view.findViewById(R.id.textView_challengeName);
        name.setText(data.title);
    }
    public void loadChallengeDate(){
        TextView date = (TextView) view.findViewById(R.id.textView_challengeDate);
        date.setText(data.date.format(data.formatter));
    }
    public void loadChallengeWinner(){
        //set GridView
        ListView listViewWinner = (ListView) view.findViewById(R.id.listView_winner);
        if(data.winner == null){
            noWinner(true);
            return;
        }
        noWinner(false);
        ArrayList<String> nameList = data.winner;
        ArrayList<Integer> iconList = new ArrayList<>();
        for(int i = 0; i < nameList.size(); i++)
        {
            int idx = data.participants.indexOf(nameList.get(i));
            if(idx > -1)
                iconList.add(data.participantsIcon.get(idx));
        }
        ChallengeActivityResultAdapter adapter = new ChallengeActivityResultAdapter(this.getActivity(), nameList, iconList, data.activities);
        listViewWinner.setAdapter(adapter);
    }

    public void loadChallengeDetailButton(){
        Button button = (Button) view.findViewById(R.id.button_challengeDetail);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                ChallengeActivityFragment fragment = new ChallengeActivityFragment();
                //ChallengeActivityResultFragment fragment = new ChallengeActivityResultFragment();
                Bundle args = new Bundle();
                args.putSerializable("data", data);
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.fragmentContent, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }
}
