package com.betteru.ucsd.myapplication4.challenge;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.betteru.ucsd.myapplication4.BetterUApplication;
import com.betteru.ucsd.myapplication4.R;
import com.betteru.ucsd.myapplication4.UserModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Yuting on 12/10/2017.
 */

public class ChallengeParticipantFragment extends Fragment {

    ArrayList<ChallengeModel> data = new ArrayList<>();
    View view;
    FirebaseFirestore db;
    UserModel user;
    LocalDate date;
    ListView listView;
    ChallengeAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.i("challenge fragment participants", "on create");
        db = FirebaseFirestore.getInstance();
        user = ((BetterUApplication) getActivity().getApplication()).getCurrentFBUser();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("challenge fragment participants", "on create view");
        if(view == null) {
            Log.i("challenge fragment participants", "view == null");
            view = inflater.inflate(R.layout.fragment_challenge, container, false);
            listView = (ListView) view.findViewById(R.id.ListView_challenge);
            adapter = new ChallengeAdapter(this.getActivity(), data);
            listView.setAdapter(adapter);
        }
        return view;
    }
    @Override
    public void onResume(){
        super.onResume();
        Log.i("challenge list", Integer.toString(data.size()));
        if(data.isEmpty()) {
            loadData(user.getUserId());
        }
        else {
            loadNoRecordView(false);
            //adapter.refresh(data);
            //adapter.notifyDataSetChanged();
            loadListView();
        }
        //super.onResume();
    }

    public static ChallengeParticipantFragment newInstance() {
        ChallengeParticipantFragment fragment = new ChallengeParticipantFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void loadNoRecordView(Boolean flag){
        TextView noRecordView = (TextView) view.findViewById(R.id.textView_noChallengeRecord);
        if(flag == true) noRecordView.setVisibility(View.VISIBLE);
        else noRecordView.setVisibility(View.GONE);
    }


    public void loadData(String userId)
    {
        showProgressDialog();
        DocumentReference df;
        try{
            df = db.collection("challenge_user").document(userId);
        }catch(Exception e){
            Log.e("Exception", e.toString());
            hideProgressDialog();
            return;
        }
        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists() && document != null) {
                        Map<String, Object> obj = document.getData();
                        ArrayList<String> docIdList = new ArrayList<>();
                        for(String id : obj.keySet())
                            docIdList.add(id);
                        Log.d("challenge data", docIdList.toString());
                        if(!docIdList.isEmpty()) {
                            loadParticipantData(docIdList, 0);
                        }
                    } else {
                        Log.d("challenge data", "No such document");
                    }
                }else{
                    Log.d("Data in Cloud", "Error getting documents" , task.getException());
                }
                hideProgressDialog();
            }
        });
    }
    public void loadParticipantData(final ArrayList<String> idList, final Integer idx)
    {
        if(idx == idList.size()){
            Collections.sort(data);
            loadNoRecordView(false);
            loadListView();
            hideProgressDialog();
            return;
        }
        DocumentReference df;
        try{
            df = db.collection("challenge").document(idList.get(idx));
        }catch(Exception e){
            loadParticipantData(idList, idx+1);
            Log.e("Exception", e.toString());
            return;
        }
        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists() && document != null) {
                        Map<String, Object> obj = document.getData();
                        ChallengeModel temp = new ChallengeModel(
                                (String) obj.get("owner"),
                                (String) obj.get("owner_name"),
                                (String) obj.get("title"),
                                (String) obj.get("time"),
                                (ArrayList<String>) obj.get("participants"),
                                (ArrayList<String>) obj.get("participants_name"),
                                (ArrayList<String>) obj.get("activities"));
                        if(obj.containsKey("winner")){
                            temp.setWinner(
                                    (ArrayList<String>) obj.get("winner"),
                                    (ArrayList<String>) obj.get("winner_name"),
                                    (ArrayList<String>) obj.get("winner_data"));
                        }
                        temp.setId(document.getId());
                        data.add(temp);
                    } else {
                        Log.d("challenge data", "No such document");
                    }
                }else{
                    Log.d("Data in Cloud", "Error getting documents" , task.getException());
                }
                loadParticipantData(idList, idx+1);
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
                args.putBoolean("edit", false);
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.fragmentContent,fragment);
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