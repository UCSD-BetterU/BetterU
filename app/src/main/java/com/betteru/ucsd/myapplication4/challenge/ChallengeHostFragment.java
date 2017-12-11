package com.betteru.ucsd.myapplication4.challenge;

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

import com.betteru.ucsd.myapplication4.BetterUApplication;
import com.betteru.ucsd.myapplication4.R;
import com.betteru.ucsd.myapplication4.UserModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class ChallengeHostFragment extends Fragment {

    ArrayList<ChallengeModel> data = new ArrayList<>();
    View view;
    FirebaseFirestore db;
    UserModel user;
    ListView listView;
    ChallengeAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.i("challenge fragment", "on create");
        db = FirebaseFirestore.getInstance();
        user = ((BetterUApplication) getActivity().getApplication()).getCurrentFBUser();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("challenge fragment", "on create view");
        if(view == null) {
            Log.i("challenge fragment", "view == null");
            view = inflater.inflate(R.layout.fragment_challenge, container, false);
            listView = (ListView) view.findViewById(R.id.ListView_challenge);
            adapter = new ChallengeAdapter(this.getActivity(), data);
            listView.setAdapter(adapter);
        }
        return view;
    }
    @Override
    public void onResume(){
        Log.i("challenge list", Integer.toString(data.size()));
        if(data.isEmpty())
            loadData(user.getUserId());
        else {
            loadNoRecordView(false);
            adapter.refresh(data);
            //adapter.notifyDataSetChanged();
            loadListView();
        }
        super.onResume();
    }

    public static ChallengeHostFragment newInstance() {
        ChallengeHostFragment fragment = new ChallengeHostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void loadNoRecordView(Boolean flag){
        TextView noRecordView = (TextView) view.findViewById(R.id.textView_noChallengeRecord);
        if(flag == true) noRecordView.setVisibility(View.VISIBLE);
        else noRecordView.setVisibility(View.GONE);
    }

    public void loadData(String userId){
        showProgressDialog();
        CollectionReference challengeRef;
        Query query;
        try {
            challengeRef = db.collection("challenge");
            query = challengeRef.whereEqualTo("owner", userId).orderBy("time", Query.Direction.DESCENDING);
        }catch (Exception e)
        {
            Log.d("Exception", e.toString());
            hideProgressDialog();
            return;
        }
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task){
                if(task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()) {
                        if(document.exists() && document != null) {
                            loadNoRecordView(false);
                            Map<String, Object> obj = document.getData();
                            ChallengeModel temp = new ChallengeModel(
                                    (String) obj.get("owner"),
                                    (String) obj.get("owner_name"),
                                    (String) obj.get("title"),
                                    (String) obj.get("time"),
                                    (ArrayList<String>) obj.get("participants"),
                                    (ArrayList<String>) obj.get("participants_name"),
                                    (ArrayList<String>) obj.get("activities"));
                            if (obj.containsKey("winner")) {
                                temp.setWinner(
                                        (ArrayList<String>) obj.get("winner"),
                                        (ArrayList<String>) obj.get("winner_name"),
                                        (ArrayList<String>) obj.get("winner_data"));
                            }
                            temp.setId(document.getId());
                            data.add(temp);
                        }
                    }
                }else {
                    loadNoRecordView(true);
                    Log.d("Data in Cloud", "Error getting documents" , task.getException());
                }
                loadListView();
                hideProgressDialog();
            }
        });
    }
    public void loadListView() {
        final Fragment f = this;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                int pos = position + 1;
                FragmentTransaction fragmentTransaction = getParentFragment().getFragmentManager().beginTransaction();
                ChallengeResultFragment fragment = new ChallengeResultFragment();
                Bundle args = new Bundle();
                args.putSerializable("data", data.get(position));
                args.putBoolean("edit", true);
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.fragmentContent, fragment);
                //fragmentTransaction.replace(R.layout.fragment_challenge, fragment);
                /*
                fragmentTransaction.hide(f);
                fragmentTransaction.add(R.id.fragmentContent, fragment);
                */
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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