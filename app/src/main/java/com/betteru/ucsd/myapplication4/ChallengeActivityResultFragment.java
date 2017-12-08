package com.betteru.ucsd.myapplication4;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuting on 11/30/2017.
 */

public class ChallengeActivityResultFragment extends Fragment {
    static ChallengeModel data;
    View view;
    UserModel user;
    ArrayList<Integer> winner_data;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<ArrayList<Integer>> result;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("challenge result", "on create");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("challenge result", "on create view");
        Bundle args = getArguments();
        data = (ChallengeModel) args.getSerializable("data");
        Log.d("DATA", Integer.toString(data.activitiesIcon.size()));
        view = inflater.inflate(R.layout.fragment_challenge_result, container, false);
        user = ((BetterUApplication) getActivity().getApplication()).getCurrentFBUser();
        loadChallengeDate();
        loadChallengeName();
        loadChallengeWinner();
        loadChallengeDetailButton();
        return view;
    }

    public void noWinner(Boolean flag) {
        TextView noWinner = (TextView) view.findViewById(R.id.textView_noWinner);
        if (flag)
            noWinner.setVisibility(View.VISIBLE);
        else
            noWinner.setVisibility(View.GONE);
    }

    public void loadChallengeName() {
        TextView name = (TextView) view.findViewById(R.id.textView_challengeName);
        name.setText(data.title);
    }

    public void loadChallengeDate() {
        TextView date = (TextView) view.findViewById(R.id.textView_challengeDate);
        date.setText(data.date.format(data.formatter));
    }

    public void loadChallengeWinner() {
        //set GridView
        if (data.date.isAfter(LocalDate.now())) {
            noWinner(true);
            return;
        }
        noWinner(false);
        if(data.winner == null || data.winner.isEmpty()) {
            showProgressDialog();
            Log.d("winner", "generate winner");
            generateChallengeResult();
        }else
            showWinner();

    }

    public void loadChallengeDetailButton() {
        final Fragment f = this;
        Button button = (Button) view.findViewById(R.id.button_challengeDetail);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentTransaction tx = getFragmentManager().beginTransaction();
                ChallengeActivityFragment fragment = new ChallengeActivityFragment();
                Bundle args = new Bundle();
                args.putSerializable("data", data);
                fragment.setArguments(args);
                if(data.winner != null && !data.winner.isEmpty()) {
                    tx.hide(f);
                    tx.add(R.id.fragmentContent, fragment);
                    tx.addToBackStack(null);
                }
                else {
                    tx.replace(R.id.fragmentContent, fragment);
                    tx.addToBackStack(null);
                }
                tx.commit();
            }
        });
    }

    public void generateChallengeResult() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd");
        String year = data.date.format(formatter);
        String day = data.date.format(formatter2);
        ArrayList<String> activity = data.activities;

        ArrayList<String> participants = new ArrayList<>();
        ArrayList<String> participants_name = new ArrayList<>();
        for (int i = 0; i < data.participants.size(); i++) {
            participants.add(data.participants.get(i));
            participants_name.add(data.participants_name.get(i));
        }
        participants.add(user.getUserId());
        participants_name.add(user.getFirstName());

        //initialize result matrix
        result = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < activity.size(); i++) {
            result.add(new ArrayList<Integer>(Collections.nCopies(participants.size(), 0)));
        }
        //TODO: change year and day
        getUserDailyActivity(0, participants, participants_name, "201712", "05", activity);
    }

    public void generateWinner(ArrayList<String> participants, ArrayList<String> participants_name) {
        Log.d("winner all data", result.toString());
        data.winner = new ArrayList<>();
        data.winner_name = new ArrayList<>();
        winner_data = new ArrayList<Integer>();
        for (int i = 0; i < result.size(); i++) {
            int maxValue = Collections.max(result.get(i));
            winner_data.add(maxValue);
            int maxIdx = result.get(i).lastIndexOf(maxValue);
            data.winner.add(participants.get(maxIdx));
            data.winner_name.add(participants_name.get(maxIdx));
        }
        Log.d("winner_name", data.winner_name.toString());
    }

    public void showWinner(){
        ListView listViewWinner = (ListView) view.findViewById(R.id.listView_winner);
        ArrayList<String> idList = data.winner;
        ArrayList<String> nameList = data.winner_name;
        ChallengeActivityResultAdapter adapter = new ChallengeActivityResultAdapter(
                this.getActivity(),
                nameList,
                idList,
                winner_data,
                data.activities);
        listViewWinner.setAdapter(adapter);
    }


    public void getUserDailyActivity(final Integer pIdx, final ArrayList<String>participants,
                                     final ArrayList<String> participants_name,
                                     final String year, final String day, final ArrayList<String> activity){
        DocumentReference df;
        DocumentReference docRef;
        try {
            df = db.collection("extrasensory").document(participants.get(pIdx));
            docRef = df.collection(year).document(day);
        }catch (Exception e)
        {
            Log.d("winner exception", e.toString());
            return;
        }
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Map<String, Object> obj = document.getData();
                        Log.d("winner data", obj.toString());
                        for(int i = 0; i < activity.size(); i++)
                        {
                            String act = activity.get(i);
                            if(obj.containsKey(act))
                                result.get(i).set(pIdx, ((Long) obj.get(act)).intValue());
                        }
                        Log.d("winner get extrasensory data","result");
                        if(pIdx+1 == participants.size()) {
                            generateWinner(participants, participants_name);
                            submitWinner();
                            showWinner();
                            hideProgressDialog();
                        }
                        else
                            getUserDailyActivity(pIdx+1, participants,participants_name,
                                year, day, activity);
                    } else {
                        Log.d("winner get extrasensory data","No such document");
                    }
                }else {
                    Log.d("winner get extrasensory data", "Error getting documents", task.getException());
                }
            }
        });
    }

    public void submitWinner(){
        final Fragment f = this;
        DocumentReference ref = db.collection("challenge").document();
        if(data.id.isEmpty()){
            data.setId(ref.getId());
        }
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("owner",data.ownerId );
        dataMap.put("participants", data.participants);
        dataMap.put("participants_name", data.participants_name);
        dataMap.put("activities", data.activities);
        dataMap.put("time", data.timeStamp);
        dataMap.put("title", data.title);
        dataMap.put("winner", data.winner);
        dataMap.put("winner_name", data.winner_name);
        Log.d("challenge", data.id);
        db.collection("challenge").document(data.id).set(dataMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("winner", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("winner", "Error writing document", e);
                    }
                });
    }

    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this.getContext());
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}