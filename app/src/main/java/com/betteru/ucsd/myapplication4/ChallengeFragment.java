package com.betteru.ucsd.myapplication4;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class ChallengeFragment extends Fragment {

    ArrayList<ChallengeModel> data = new ArrayList<>();
    View view;
    FirebaseFirestore db;
    String userId = "user0001";
    LocalDate date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_challenge, container, false);
        db = FirebaseFirestore.getInstance();
        //loadData();
        loadData(userId);
        loadListView();
        FloatingActionButton button = view.findViewById(R.id.button_challenge_add);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newChallenge();
            }
        });
        return view;
    }

    public ChallengeFragment newInstance() {
        ChallengeFragment fragment = new ChallengeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public void loadNoRecordView(Boolean flag){
        TextView noRecordView = (TextView) getView().findViewById(R.id.textView_noChallengeRecord);
        if(flag == true) noRecordView.setVisibility(View.VISIBLE);
        else noRecordView.setVisibility(View.GONE);
    }

    public void loadData(String userId){
        if(!data.isEmpty())
            return;
        CollectionReference challengeRef;
        Query query;
        try {
            challengeRef = db.collection("challenge");
            query = challengeRef.whereEqualTo("owner", userId);
        }catch (Exception e)
        {
            Log.d("Exception", e.toString());
            return;
        }
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task){
                hideProgressDialog();
                if(task.isSuccessful()){
                    loadNoRecordView(false);
                    for (DocumentSnapshot document : task.getResult()) {

                            Map<String, Object> obj = document.getData();
                            ChallengeModel temp = new ChallengeModel(
                                    (String) obj.get("owner"),
                                    (String) obj.get("title"),
                                    (String) obj.get("time"),
                                    (ArrayList<String>) obj.get("participants"),
                                    (ArrayList<String>) obj.get("activities"));
                            if(obj.containsKey("winner")){
                                    temp.setWinner((ArrayList<String>) obj.get("winner"));
                            }
                            temp.setId(document.getId());
                            data.add(temp);
                    }
                }else {
                    loadNoRecordView(true);
                    Log.d("Data in Cloud", "Error getting documents" , task.getException());
                }
            }
        });
    }
    public void loadData(){
        data.clear();
        ArrayList<String> par = new ArrayList<String>(Arrays.asList("user0002", "user0003"));
        ArrayList<Integer> parIcon = new ArrayList<Integer>();
        parIcon.add(R.drawable.ic_face_black_48dp);
        parIcon.add(R.drawable.ic_face_black_48dp);
        ArrayList<String> act = new ArrayList<String>(Arrays.asList("Running", "Walking", "Showering"));
        ArrayList<Integer> actIcon = new ArrayList<>();
        actIcon.add(R.drawable.ic_directions_run_black_48dp);
        actIcon.add(R.drawable.ic_directions_walk_black_48dp);
        actIcon.add(R.drawable.ic_directions_bike_black_48dp);
        ChallengeModel data1 = new ChallengeModel("user0001",
                "My First Challenge", "2017-01-01", par, act);
        data1.setIcon(parIcon, actIcon);
        ChallengeModel data2 = new ChallengeModel("user0001",
                "My Second Challenge", "2017-01-02", par, act);
        data2.setIcon(parIcon, actIcon);
        ChallengeModel data3 = new ChallengeModel("user0001",
                "My Third Challenge", "2017-01-02", par, act);
        data3.setIcon(parIcon, actIcon);
        data.add(data1);
        data.add(data2);
        data.add(data3);
    }

    private void loadListView() {
        ListView listView = (ListView) view.findViewById(R.id.ListView_challenge);
        ChallengeAdapter adapter = new ChallengeAdapter(this.getActivity(), data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                int pos = position + 1;

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                //ChallengeActivityFragment fragment = new ChallengeActivityFragment();
                ChallengeActivityResultFragment fragment = new ChallengeActivityResultFragment();
                Bundle args = new Bundle();
                args.putSerializable("data", data.get(position));
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.fragmentContent, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    private void newChallenge(){
        String timeStamp = date.now().format(ChallengeModel.formatter);
        ChallengeModel newChallenge = new ChallengeModel(userId,
                "Input Challenge Name",
                timeStamp,
                new ArrayList<String>(),
                new ArrayList<String>());
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        ChallengeActivityFragment fragment = new ChallengeActivityFragment();
        Bundle args = new Bundle();
        args.putSerializable("data",newChallenge);
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.fragmentContent, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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