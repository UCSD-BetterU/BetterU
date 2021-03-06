package com.betteru.ucsd.myapplication4.challenge;

/**
 * Created by Yuting on 12/11/2017.
 */

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.betteru.ucsd.myapplication4.BetterUApplication;
import com.betteru.ucsd.myapplication4.R;
import com.betteru.ucsd.myapplication4.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuting on 11/30/2017.
 */

public class ChallengeResultFragment extends Fragment {
    static ChallengeModel data;
    Boolean editable;
    View view;
    UserModel user;
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
        editable = (Boolean) args.getBoolean("edit");
        Log.d("challenge detail editable", editable.toString());
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
        date.setText(data.sdf.format(data.date.getTime()));
    }

    public void loadChallengeWinner() {
        //set GridView
        //if (data.date.isAfter(LocalDate.now())) {
        if(data.date.after(Calendar.getInstance())){
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
                ChallengeDetailFragment fragment = new ChallengeDetailFragment();
                Bundle args = new Bundle();
                args.putSerializable("data", data);
                args.putBoolean("edit", editable);
                Log.d("Data edit", editable.toString());
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
        String year = String.format("%04d%02d", data.date.get(Calendar.YEAR), data.date.get(Calendar.MONTH)+1);
        String day = String.format("%02d", data.date.get(Calendar.DAY_OF_MONTH));
        ArrayList<String> activity = data.activities;
        ArrayList<String> participants = new ArrayList<>();
        ArrayList<String> participants_name = new ArrayList<>();
        for (int i = 0; i < data.participants.size(); i++) {
            participants.add(data.participants.get(i));
            participants_name.add(data.participants_name.get(i));
        }
        participants.add(data.ownerId);
        participants_name.add(data.owner_name);
        result = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < activity.size(); i++) {
            result.add(new ArrayList<Integer>(Collections.nCopies(participants.size(), 0)));
        }
        Log.d("winner", participants_name.toString() + " " + year + " " + day);
        getUserDailyActivity(0, participants, participants_name, year, day, activity);
    }

    public void generateWinner(ArrayList<String> participants, ArrayList<String> participants_name) {
        Log.d("winner all data", result.toString());
        data.winner = new ArrayList<>();
        data.winner_name = new ArrayList<>();
        data.winner_data = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            int maxValue = Collections.max(result.get(i));
            data.winner_data.add(Integer.toString(maxValue));
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
        ChallengeResultListAdapter adapter = new ChallengeResultListAdapter(
                this.getActivity(),
                nameList,
                idList,
                data.winner_data,
                data.activities);
        listViewWinner.setAdapter(adapter);
    }


    public void getUserDailyActivity(final Integer pIdx, final ArrayList<String>participants,
                                     final ArrayList<String> participants_name,
                                     final String year, final String day, final ArrayList<String> activity){
        if(pIdx == participants.size()) {
            generateWinner(participants, participants_name);
            submitWinner();
            showWinner();
            hideProgressDialog();
            return;
        }
        DocumentReference df;
        DocumentReference docRef;
        Log.d("winner user daily activity", participants_name.get(pIdx));
        try {
            df = db.collection("extrasensory").document(participants.get(pIdx));
            docRef = df.collection(year).document(day);
        }catch (Exception e){
            getUserDailyActivity(pIdx + 1, participants, participants_name,
                    year, day, activity);
            Log.d("winner exception", e.toString());
            return;
        }
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists() && document != null) {
                        Map<String, Object> obj = document.getData();
                        for(int i = 0; i < activity.size(); i++)
                        {
                            String act = activity.get(i);
                            if(obj.containsKey(act))
                                result.get(i).set(pIdx, ((Long) obj.get(act)).intValue());
                        }
                    } else {
                        Log.d("winner get extrasensory data","No such document");
                        //hideProgressDialog();
                    }
                }else {
                    Log.d("winner get extrasensory data", "Error getting documents", task.getException());
                }
                getUserDailyActivity(pIdx+1, participants,participants_name,year, day, activity);
                hideProgressDialog();
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
        dataMap.put("owner_name", data.owner_name);
        dataMap.put("participants", data.participants);
        dataMap.put("participants_name", data.participants_name);
        dataMap.put("activities", data.activities);
        dataMap.put("time", data.timeStamp);
        dataMap.put("title", data.title);
        dataMap.put("winner", data.winner);
        dataMap.put("winner_name", data.winner_name);
        dataMap.put("winner_data", data.winner_data);
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
